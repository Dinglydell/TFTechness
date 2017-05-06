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
import dinglydell.tftechness.gui.container.ContainerRFAnvil;

public class TileRFAnvil extends TileTFTMachine {
	public static final int inputSlotEnd = 1;
	protected static final float itemSurfaceArea = 0.074f;
	protected static final float itemMass = 10;
	protected String plan = "pickaxe";

	protected int requiredEnergy = 1;
	protected int initialRequiredEnergy = 1;

	public TileRFAnvil() {
		super();
		inventory = new ItemStack[3];

	}

	@Override
	protected boolean shouldActivate() {
		AnvilManager manager = AnvilManager.getInstance();
		//plan = "pickaxe";
		AnvilRecipe recipe = manager.findMatchingRecipe(new AnvilRecipe(
				inventory[0], null, plan, false, AnvilReq.REDSTEEL.Tier));

		return recipe != null && hasWorkableTemperature(0);

	}

	// TODO: Centralised location for this sort of thing
	protected boolean hasWorkableTemperature(int slot) {
		HeatRegistry heatReg = HeatRegistry.getInstance();
		ItemStack is = inventory[slot];

		if (TFC_ItemHeat.hasTemp(is)) {
			HeatIndex index = heatReg.findMatchingIndex(is);
			if (index != null) {
				float temp = TFC_ItemHeat.getTemp(is);
				float workTemp = index.meltTemp
						* MetalConfig.workableTemperature;
				return temp < index.meltTemp && temp > workTemp;

			}
		}
		return false;
	}

	@Override
	protected boolean shouldDeactivate() {

		return !shouldActivate() || requiredEnergy <= 0;
	}

	@Override
	protected void onActivate() {
		AnvilManager manager = AnvilManager.getInstance();
		AnvilRecipe recipe = manager.findMatchingRecipe(new AnvilRecipe(
				inventory[0], null, plan, false, AnvilReq.REDSTEEL.Tier));

		initialRequiredEnergy = recipe.anvilreq * 1000;
		requiredEnergy = initialRequiredEnergy;
	}

	@Override
	protected void onDeactivate() {
		if (requiredEnergy <= 0) {
			AnvilManager manager = AnvilManager.getInstance();
			AnvilRecipe recipe = manager.findMatchingRecipe(new AnvilRecipe(
					inventory[0], null, plan, false, AnvilReq.REDSTEEL.Tier));
			inventory[1] = recipe.result.copy();
			TFC_ItemHeat.setTemp(inventory[1],
					TFC_ItemHeat.getTemp(inventory[0]));
			inventory[0] = null;

		}
		requiredEnergy = 1;
		initialRequiredEnergy = 1;
	}

	@Override
	protected SideConfig getSideConfig() {
		SideConfig cfg = new SideConfig();
		cfg.numConfig = 8;
		cfg.slotGroups = new int[][] { new int[0],
				{ 0, 1 },
				{ 2, 3 },
				{ 4 },
				{ 2, 3, 4 },
				{ 0 },
				{ 1 },
				{ 0, 1, 2, 3, 4 } };
		cfg.allowInsertionSide = new boolean[] { false,
				true,
				false,
				false,
				false,
				true,
				true,
				true };
		cfg.allowExtractionSide = new boolean[] { false,
				true,
				true,
				true,
				true,
				false,
				false,
				true };
		cfg.allowInsertionSlot = new boolean[] { true,
				true,
				false,
				false,
				false,
				false };
		cfg.allowExtractionSlot = new boolean[] { true,
				true,
				true,
				true,
				true,
				false };
		cfg.sideTex = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
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

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);

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
		// TODO Auto-generated method stub
		float num = (1 - (float) requiredEnergy / initialRequiredEnergy) * arg0;
		return (int) num;
	}

}
