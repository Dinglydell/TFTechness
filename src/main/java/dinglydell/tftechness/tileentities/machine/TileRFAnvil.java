package dinglydell.tftechness.tileentities.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cofh.core.network.PacketCoFHBase;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.AnvilReq;

import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.gui.GuiRFAnvil;
import dinglydell.tftechness.gui.IPlanHandler;
import dinglydell.tftechness.gui.container.ContainerRFAnvil;

public class TileRFAnvil extends TileTFTMachine implements IPlanHandler {
	public static final int inputSlotEnd = 1;
	protected static final float itemSurfaceArea = 0.074f;
	protected static final float itemMass = 10;
	public static final int FLUX_SLOT = 2;
	public static final int OUTPUT_SLOT = 3;
	private String plan = "";

	protected int requiredEnergy = 1;
	protected int initialRequiredEnergy = 1;
	public boolean weldMode;

	public TileRFAnvil() {
		super();
		inventory = new ItemStack[5];

	}

	@Override
	protected boolean shouldActivate() {
		//plan = "pickaxe";
		if (inventory[OUTPUT_SLOT] != null) {
			return false;
		}
		AnvilRecipe recipe = findRecipe();
		if (this.weldMode) {

			return recipe != null && hasWeldableTemperature(0)
					&& hasWeldableTemperature(1);
		}

		return recipe != null && hasWorkableTemperature(0)
				&& (recipe.input2 == null || hasWorkableTemperature(1));

	}

	private boolean hasWeldableTemperature(int slot) {
		return hasSolidTemperature(slot, MetalConfig.weldableTemperature);
	}

	/**
	 * Returns true if the item in slot is solid and has a temperature of at
	 * least (temperature * melting point)
	 */
	private boolean hasSolidTemperature(int slot, float temperature) {

		HeatRegistry heatReg = HeatRegistry.getInstance();
		ItemStack is = inventory[slot];

		if (TFC_ItemHeat.hasTemp(is)) {
			HeatIndex index = heatReg.findMatchingIndex(is);
			if (index != null) {
				float temp = TFC_ItemHeat.getTemp(is);
				float workTemp = index.meltTemp * temperature;
				return temp < index.meltTemp && temp > workTemp;

			}
		}
		return false;
	}

	public void setMode(boolean mode) {
		this.weldMode = mode;
		sendModePacket();
	}

	// TODO: Centralised location for this sort of thing
	protected boolean hasWorkableTemperature(int slot) {
		return hasSolidTemperature(slot, MetalConfig.workableTemperature);
	}

	@Override
	protected boolean shouldDeactivate() {

		return !shouldActivate() || requiredEnergy <= 0;
	}

	@Override
	protected void onActivate() {

		initialRequiredEnergy = getRecipeCost();
		requiredEnergy = initialRequiredEnergy;
	}

	private AnvilRecipe findRecipe() {
		return findRecipe(plan);
	}

	private AnvilRecipe findRecipe(String plan) {
		AnvilManager manager = AnvilManager.getInstance();
		if (this.weldMode) {
			AnvilRecipe recipe = manager
					.findMatchingWeldRecipe(new AnvilRecipe(inventory[0],
							inventory[1], "", 0, inventory[FLUX_SLOT] != null,
							getTier(), null));
			if (recipe == null) {
				recipe = manager.findMatchingWeldRecipe(new AnvilRecipe(
						inventory[1], inventory[0], "", 0,
						inventory[FLUX_SLOT] != null, getTier(), null));
			}
			return recipe;
		}
		AnvilRecipe recipe = manager
				.findMatchingRecipe(new AnvilRecipe(inventory[0], inventory[1],
						plan, false, AnvilReq.REDSTEEL.Tier));
		if (recipe == null) {
			recipe = manager.findMatchingRecipe(new AnvilRecipe(inventory[1],
					inventory[0], plan, false, AnvilReq.REDSTEEL.Tier));
		}
		return recipe;

	}

	@Override
	protected void onDeactivate() {
		if (requiredEnergy <= 0) {
			AnvilRecipe recipe = findRecipe();
			inventory[OUTPUT_SLOT] = recipe.result.copy();
			float newTemperature = TFC_ItemHeat.getTemp(inventory[0]);
			inventory[0] = null;
			if (recipe.input2 != null) {
				newTemperature = (newTemperature + TFC_ItemHeat
						.getTemp(inventory[1])) / 2;
				inventory[1] = null;
			}
			TFC_ItemHeat.setTemp(inventory[OUTPUT_SLOT], newTemperature);
			if (recipe.isFlux()) {
				inventory[FLUX_SLOT].stackSize--;
				if (inventory[FLUX_SLOT].stackSize <= 0) {
					inventory[FLUX_SLOT] = null;
				}
			}

		}
		requiredEnergy = 1;
		initialRequiredEnergy = 1;
	}

	@Override
	protected SideConfig getSideConfig() {
		SideConfig cfg = new SideConfig();
		cfg.numConfig = 5;
		//index of inventory slots available at each step of configuration
		cfg.slotGroups = new int[][] { new int[0],
				{ 0, 1 },
				{ OUTPUT_SLOT },
				{ 0 },
				{ 1 },
				{ 0, 1, OUTPUT_SLOT } };
		//unsure, possibly whether something can be inserted on a side with this config
		cfg.allowInsertionSide = new boolean[] { true, false, true, true, true };
		//whether something can be extracted on a side with this config
		cfg.allowExtractionSide = new boolean[] { false,
				true,
				false,
				false,
				true };
		//index corresponds to slot index - whether something can be inserted in this slot
		cfg.allowInsertionSlot = new boolean[] { true, true, false, true, false };
		cfg.allowExtractionSlot = new boolean[] { true, true, true, true, false };
		//colour of each config
		cfg.sideTex = new int[] { Colours.blue.ordinal(),
				Colours.orange.ordinal(),
				Colours.green.ordinal(),
				Colours.purple.ordinal(),
				Colours.grey.ordinal() };
		//default for the 6 sides
		cfg.defaultSides = new byte[] { 3, 1, 2, 2, 2, 2 };
		return cfg;
	}

	@Override
	protected EnergyConfig getEnergyConfig() {
		energyConsumption = 20;
		EnergyConfig cfg = new EnergyConfig();
		cfg.maxEnergy = 96000;
		cfg.maxPower = 500;
		return cfg;
	}

	@Override
	public int getType() {
		return BlockTFTMachine.Types.RFANVIL.ordinal();
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!worldObj.isRemote) {
			TFC_Core.handleItemTicking(this,
					this.worldObj,
					xCoord,
					yCoord,
					zCoord);
		}
	}

	@Override
	protected int spendEnergy(int rf) {
		//if (inventory[0] == null) {
		//	return 0;
		//}
		//ItemStack is = inventory[0];
		//HeatIndex index = HeatRegistry.getInstance().findMatchingIndex(is);
		if (isActive) {
			requiredEnergy -= rf;
			return rf + Math.min(0, requiredEnergy);
		}
		return 0;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiRFAnvil(inv, this);

	}

	@Override
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerRFAnvil(inv, this);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.weldMode = nbt.getBoolean("WeldMode");
		this.plan = nbt.getString("Plan");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("WeldMode", this.weldMode);
		nbt.setString("Plan", this.plan);
	}

	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addInt(requiredEnergy);
		packet.addInt(initialRequiredEnergy);
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		this.requiredEnergy = packet.getInt();
		this.initialRequiredEnergy = packet.getInt();
	}

	@Override
	public int getScaledProgress(int arg0) {

		float num = (1 - (float) requiredEnergy / initialRequiredEnergy) * arg0;
		return (int) num;
	}

	@Override
	public PacketCoFHBase getModePacket() {
		PacketCoFHBase packet = super.getModePacket();

		packet.addBool(this.weldMode);
		packet.addString(this.plan);
		return packet;
	}

	@Override
	protected void handleModePacket(PacketCoFHBase packet) {
		super.handleModePacket(packet);

		this.weldMode = packet.getBool();
		this.plan = packet.getString();
		markDirty();
		callNeighborTileChange();
	}

	public int getRecipeCost() {
		AnvilRecipe recipe = findRecipe();
		return recipe == null ? -1 : (1 + recipe.anvilreq) * 1000;
	}

	@Override
	public void setPlan(String p) {
		this.plan = p;
		this.sendModePacket();
		this.openInventory();
	}

	public ItemStack getResult() {
		AnvilRecipe recipe = findRecipe();
		return recipe == null ? null : recipe.result;
	}

	public ItemStack getResult(String plan) {
		AnvilRecipe recipe = findRecipe(plan);
		return recipe == null ? null : recipe.result;
	}

	public String getPlan() {
		return plan;
	}

	public int getTier() {

		return AnvilReq.REDSTEEL.Tier + this.level;
	}

}
