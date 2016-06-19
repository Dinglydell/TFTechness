package dinglydell.tftechness.tileentities.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import cofh.core.network.PacketCoFHBase;

import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.fluid.FluidMoltenMetal;

public abstract class TileTemperatureControl extends TileTemperature {

	protected static final float specificHeat = 1.5f;
	protected static final int mass = 1200;
	public static final int MAX_TEMPERATURE = 2000;
	protected static final float[] levelExposedSurfaceArea = { 0.03f,
			0.02f,
			0.01f,
			0.0075f };

	protected static final float coolingExposedSurfaceArea = 1;

	public float targetTemperature = 0;

	protected boolean isCooling;

	public TileTemperatureControl() {
		internalTemperature = TFTechness.baseTemp;
	}

	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFloat(targetTemperature);
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		targetTemperature = packet.getFloat();
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

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		targetTemperature = nbt.getFloat("TargetTemperature");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setFloat("TargetTemperature", targetTemperature);
	}

	@Override
	protected float getSurfaceArea() {
		return isCooling ? coolingExposedSurfaceArea
				: levelExposedSurfaceArea[level];
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

	protected void handleMoldOutput(int slot, IFluidTank tnk) {
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
						&& inventory[slot].getItemDamage() > 0
						&& HeatRegistry.getInstance()
								.getIsLiquid(inventory[slot])) {

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

}
