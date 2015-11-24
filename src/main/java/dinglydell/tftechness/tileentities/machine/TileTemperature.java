package dinglydell.tftechness.tileentities.machine;

import net.minecraft.nbt.NBTTagCompound;
import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.Core.TFC_Climate;

public abstract class TileTemperature extends TileTFTMachine {
	
	/** The heat transfer coefficient of air */
	protected static final float heatTransferCoefficient = 10.45f;
	
	/** The temperature change (°C/K) that occured on the last tick (used for maintaining
	 * temperatures) */
	protected float lastChange;
	/** Current internal temperature (°C) of the block */
	protected float internalTemperature = 22;
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setFloat("Temperature", internalTemperature);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("Temperature")) {
			internalTemperature = nbt.getFloat("Temperature");
		}
	}
	
	@Override
	public void updateEntity() {
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
			float dT = temp - internalTemperature;
			float change = getTemperatureChange(internalTemperature,
					temp,
					getSurfaceArea(),
					getSpecificHeat(),
					getMass());
			internalTemperature += change;
			lastChange = change;
		}
		super.updateEntity();
	}
	
	protected float getTemperatureChange(float temperature, float ambientTemperature, float surfaceArea,
			float specificHeat, float mass) {
		float dT = ambientTemperature - temperature;
		return heatTransferCoefficient * surfaceArea / (specificHeat * mass) * dT;
	}
	
	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFloat(internalTemperature);
		return packet;
	}
	
	@Override
	protected void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		internalTemperature = packet.getFloat();
	}
	
	public float getTemperature() {
		return internalTemperature;
	}
	
	/** Exposed surface area of the cube (usually 6m^2) */
	protected abstract float getSurfaceArea();
	
	/** The specific heat of the block
	 * Used for cooling/warming the block to the ambient temperature */
	protected abstract float getSpecificHeat();
	
	/** Should return the mass (in kg) of the block.
	 * Used for cooling/warming the block to the ambient temperature */
	protected abstract int getMass();
}
