package dinglydell.tftechness.tileentities.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.network.PacketCoFHBase;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Enums.EnumFuelMaterial;
import com.bioxx.tfc.api.Interfaces.ISmeltable;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.fluid.FluidMoltenMetal;
import dinglydell.tftechness.fluid.TFTFluids;
import dinglydell.tftechness.gui.GuiRFForge;
import dinglydell.tftechness.gui.container.ContainerRFForge;

public class TileRFForge extends TileTemperature {

	protected static final int[] tankCapacity = { 4000, 8000, 16000, 32000 };
	protected static final float specificHeat = 1.5f;
	protected static final int mass = 1200;
	protected static final float exposedSurfaceArea = 0.01f;
	protected static final float coolingExposedSurfaceArea = 1;
	// Currently all items have the same properties
	protected static final float itemSurfaceArea = 0.074f;
	protected static final float itemMass = 10;

	// protected float internalTemperature = TFTechness.baseTemp;
	public float targetTemperature = EnumFuelMaterial.COAL.burnTempMax;
	protected FluidTankAdv tank = new FluidTankAdv(tankCapacity[0]);
	/**
	 * All input slots will have an index >= tankASlotEnd and < this value
	 */
	public static final int inputSlotEnd = 6;
	// protected FluidTankAlloy tankB = new FluidTankAlloy(tankCapacity[0]);
	private boolean isCooling;

	// private FluidTankAdv guiTankA = new FluidTankAdv(tankCapacity[0]);
	// private FluidTankAdv guiTankB = new FluidTankAdv(tankCapacity[0]);

	public TileRFForge() {
		inventory = new ItemStack[8];
		internalTemperature = TFTechness.baseTemp;
	}

	@Override
	protected boolean shouldActivate() {
		return true;
	}

	@Override
	protected boolean shouldDeactivate() {
		return false;
	}

	@Override
	protected void onActivate() {
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFluidStack(tank.getFluid());
		packet.addFloat(targetTemperature);
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		tank.setFluid(packet.getFluidStack());
		targetTemperature = packet.getFloat();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("Tank"));
		targetTemperature = nbt.getFloat("TargetTemperature");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("Tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setFloat("TargetTemperature", targetTemperature);
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			heatInventorySlots();
			handleMoldOutputs();
		}
	}

	protected void handleMoldOutputs() {
		handleMoldOutput(inputSlotEnd, tank);
		// handleMoldOutput(tankBSlotEnd + 1, tankB);

	}

	protected void handleMoldOutput(int slot, FluidTankAdv tnk) {
		if (inventory[slot] != null && tnk.getFluid() != null) {
			int drainAmt = (int) (1 / (float) MetalConfig.ingotFluidmB * 100);
			FluidStack fs = tnk.drain(drainAmt, false);
			FluidMoltenMetal m = (FluidMoltenMetal) fs.getFluid();
			if (HeatRegistry.getInstance().getMeltingPoint(new ItemStack(
					m.getMetal().meltedItem)) < internalTemperature) {

				if (inventory[slot].getItem() == TFCItems.ceramicMold) {
					inventory[slot] = new ItemStack(m.getMetal().meltedItem, 1,
							99);
					TFC_ItemHeat.setTemp(inventory[slot], internalTemperature);
					tnk.drain(drainAmt, true);
				} else if (inventory[slot].getItem() == m.getMetal().meltedItem
						&& inventory[slot].getItemDamage() > 0) {

					inventory[slot].setItemDamage(inventory[slot]
							.getItemDamage() - 1);
					// Proportion of temperature between the amount added an the
					// amount in the mold
					float prop = 1 / (100 - inventory[slot].getItemDamage());
					float temp = prop * internalTemperature + (1 - prop)
							* TFC_ItemHeat.getTemp(inventory[slot]);
					TFC_ItemHeat.setTemp(inventory[slot], temp);
					tnk.drain(drainAmt, true);
				}
			}
		}

	}

	protected void heatInventorySlots() {
		for (int i = 0; i < inputSlotEnd; i++) {

			ItemStack is = inventory[i];
			if (is != null) {
				HeatIndex index = HeatRegistry.getInstance()
						.findMatchingIndex(is);
				if (index != null && index.hasOutput()) {
					float temp = TFC_ItemHeat.getTemp(is);
					float change = getTemperatureChange(temp,
							internalTemperature,
							itemSurfaceArea,
							index.specificHeat,
							itemMass);
					temp += change;
					internalTemperature -= change * 0.001;
					// if (internalTemperature > temp) {
					// temp += TFC_ItemHeat.getTempIncrease(is);
					// } else {
					// temp -= TFC_ItemHeat.getTempDecrease(is);
					// }
					TFC_ItemHeat.setTemp(is, temp);
					if (temp > index.meltTemp
							&& is.getItem() instanceof ISmeltable) {
						ISmeltable its = (ISmeltable) is.getItem();
						Metal m = its.getMetalType(is);
						if (m != null) {
							Fluid f = TFTFluids.metal.get(m.name);
							int amt = its.getMetalReturnAmount(is);
							FluidStack fs = new FluidStack(f, amt);
							// if (tank.fill(fs, false) == amt) {
							tank.fill(fs, true);
							// }
							inventory[i] = null;
						}
					}
				}

			}
		}
	}

	@Override
	protected int spendEnergy(int rf) {
		float j = rf * TFTechness.rfToJoules;
		float tmp = j / (mass * specificHeat);
		if (internalTemperature + tmp > targetTemperature) {
			tmp = Math.max(0, targetTemperature - internalTemperature);
			internalTemperature += tmp;

			isCooling = tmp == 0;

			return (int) Math.ceil((tmp * mass * specificHeat)
					/ TFTechness.rfToJoules);
		}
		isCooling = false;
		internalTemperature += tmp;
		return rf;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiRFForge(inv, this);

	}

	@Override
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerRFForge(inv, this);
	}

	@Override
	protected SideConfig getSideConfig() {

		SideConfig cfg = new SideConfig();
		cfg.numConfig = 6;
		cfg.slotGroups = new int[][] { new int[0],
				new int[0],
				{ 0 },
				{ 1 },
				{ 0, 1 },
				{ 0, 1 } };
		cfg.allowInsertionSide = new boolean[] { false,
				true,
				false,
				false,
				false,
				true };
		cfg.allowExtractionSide = new boolean[] { false,
				true,
				true,
				true,
				true,
				true };
		cfg.allowInsertionSlot = new boolean[] { true,
				false,
				false,
				false,
				false };
		cfg.allowExtractionSlot = new boolean[] { true, true, true, true, false };
		cfg.sideTex = new int[] { Colours.none.ordinal(),
				Colours.blue.ordinal(),
				Colours.red.ordinal(),
				Colours.yellow.ordinal(),
				Colours.orange.ordinal(),
				Colours.grey.ordinal() };
		cfg.defaultSides = new byte[] { 3, 1, 2, 2, 2, 2 };
		return cfg;
	}

	@Override
	protected EnergyConfig getEnergyConfig() {
		energyConsumption = 80;
		EnergyConfig cfg = new EnergyConfig();
		cfg.maxEnergy = 96000;
		cfg.maxPower = 500;
		return cfg;
	}

	@Override
	public int getType() {
		return BlockTFTMachine.Types.RFFORGE.ordinal();
	}

	@Override
	protected void onLevelChange() {
		super.onLevelChange();
		tank.setCapacity(tankCapacity[level]);
	}

	@Override
	protected float getSurfaceArea() {
		return isCooling ? coolingExposedSurfaceArea : exposedSurfaceArea;
	}

	@Override
	protected float getSpecificHeat() {
		return specificHeat;
	}

	@Override
	protected int getMass() {
		return mass;
	}

	@Override
	public PacketCoFHBase getModePacket() {
		PacketCoFHBase packet = super.getModePacket();
		packet.addFloat(targetTemperature);
		return packet;
	}

	@Override
	protected void handleModePacket(PacketCoFHBase packet) {
		super.handleModePacket(packet);
		targetTemperature = packet.getFloat();
	}

	public FluidTankAdv getTank() {
		return tank;
	}

	// public FluidTankAlloy getTankB() {
	// return tankB;
	// }
}
