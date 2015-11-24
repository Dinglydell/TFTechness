package dinglydell.tftechness.tileentities.machine;

import java.util.Random;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Enums.EnumFuelMaterial;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.fluid.FluidMoltenMetal;
import dinglydell.tftechness.fluid.FluidStackFloat;
import dinglydell.tftechness.fluid.FluidTankAlloy;
import dinglydell.tftechness.fluid.FluidTankMixed;
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
	protected float targetTemperature = EnumFuelMaterial.COAL.burnTempMax;
	protected FluidTankAlloy tankA = new FluidTankAlloy(tankCapacity[0]);
	/** All slots that map to tank A will have an index < this value */
	public static final int tankASlotEnd = 3;
	/**
	 * All slots that map to tank B will have an index >= tankASlotEnd and <
	 * this value
	 */
	public static final int tankBSlotEnd = 6;
	protected FluidTankAlloy tankB = new FluidTankAlloy(tankCapacity[0]);
	private boolean isCooling;

	// private FluidTankAdv guiTankA = new FluidTankAdv(tankCapacity[0]);
	// private FluidTankAdv guiTankB = new FluidTankAdv(tankCapacity[0]);

	public TileRFForge() {
		inventory = new ItemStack[9];
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
		packet.addInt(tankA.getNumFluids());
		for (FluidStackFloat fs : tankA.getFluids()) {
			packet.addFluidStack(fs.getFluidStack());
		}
		packet.addInt(tankB.getNumFluids());
		for (FluidStackFloat fs : tankB.getFluids()) {
			packet.addFluidStack(fs.getFluidStack());
		}
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		int tankNum = packet.getInt();
		tankA.empty();
		for (int i = 0; i < tankNum; i++) {
			tankA.setFluid(packet.getFluidStack());
		}
		tankNum = packet.getInt();
		tankB.empty();
		for (int i = 0; i < tankNum; i++) {
			tankB.setFluid(packet.getFluidStack());
		}

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tankA.readFromNBT(nbt.getCompoundTag("TankA"));
		tankB.readFromNBT(nbt.getCompoundTag("TankB"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("TankA", tankA.writeToNBT(new NBTTagCompound()));
		nbt.setTag("TankB", tankB.writeToNBT(new NBTTagCompound()));
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
		handleMoldOutput(tankBSlotEnd, tankA);
		handleMoldOutput(tankBSlotEnd + 1, tankB);

	}

	protected void handleMoldOutput(int slot, FluidTankAlloy tank) {
		if (inventory[slot] != null && !tank.isEmpty()) {
			int drainAmt = (int) (1 / (float) MetalConfig.ingotFluidmB * 100);
			FluidStack fs = tank.drain(drainAmt, false);
			FluidMoltenMetal m = (FluidMoltenMetal) fs.getFluid();
			if (HeatRegistry.getInstance().getMeltingPoint(new ItemStack(
					m.getMetal().meltedItem)) < internalTemperature) {

				if (inventory[slot].getItem() == TFCItems.ceramicMold) {
					inventory[slot] = new ItemStack(m.getMetal().meltedItem, 1,
							99);
					TFC_ItemHeat.setTemp(inventory[slot], internalTemperature);
					tank.drain(drainAmt, true);
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
					tank.drain(drainAmt, true);
				}
			}
		}

	}

	protected void heatInventorySlots() {
		for (int i = 0; i < tankBSlotEnd; i++) {

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
					// if (internalTemperature > temp) {
					// temp += TFC_ItemHeat.getTempIncrease(is);
					// } else {
					// temp -= TFC_ItemHeat.getTempDecrease(is);
					// }
					TFC_ItemHeat.setTemp(is, temp);

					if (temp > index.meltTemp) {
						Metal m = MetalRegistry.instance.getMetalFromItem(index
								.getOutputItem());
						if (m != null) {
							Fluid f = TFTFluids.metal.get(m.name);
							int amt = index.getOutput(new Random()).stackSize
									* MetalConfig.ingotFluidmB;
							FluidTankMixed tank;
							if (i < tankASlotEnd) {
								tank = tankA;
							} else {
								tank = tankB;
							}
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
		tankA.setCapacity(tankCapacity[level]);
		tankB.setCapacity(tankCapacity[level]);
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

	public FluidTankAlloy getTankA() {
		return tankA;
	}

	public FluidTankAlloy getTankB() {
		return tankB;
	}
}
