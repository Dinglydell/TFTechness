package dinglydell.tftechness.tileentities.machine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.Core.TFC_Climate;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;

public class TileCryoChamber extends TileTFTMachine {
	/** Heat transfer coefficient of air */
	protected static final float heatTransferCoefficient = 10.45f;
	/** Surface area of the cube: 6m^2 */
	protected static final int surfaceArea = 6;
	protected static final float specificHeat = 1.5f;
	protected static final int mass = 1200;
	/** This rounds up the constant values to save the calculation from needing to happen every
	 * single tick */
	protected static final float heatMultiplier = heatTransferCoefficient * surfaceArea / (20 * mass * specificHeat);
	
	protected float internalTemperature = 22;
	
	public TileCryoChamber() {
		super();
		inventory = new ItemStack[9];
		
	}
	
	public static final int TYPE = BlockTFTMachine.Types.CRYOCHAMBER.ordinal();
	
	@Override
	public int getType() {
		return TYPE;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setFloat("Temperature", internalTemperature);
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("Temperature")) {
			internalTemperature = nbt.getFloat("Temperature");
		}
	}
	
	@Override
	protected boolean shouldActivate() {
		for (ItemStack is : inventory) {
			if (is != null) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected boolean shouldDeactivate() {
		for (ItemStack is : inventory) {
			if (is != null) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	protected void onActivate() {
		
	}
	
	@Override
	protected void onDeactivate() {
		
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
			float dT = temp - internalTemperature;
			float change = heatMultiplier * dT;
			internalTemperature += change;
			TFTechness.logger.info(internalTemperature);
		}
		
	}
	
	@Override
	protected int spendEnergy(int rf) {
		
		return 0;
	}
	
	@Override
	protected SideConfig getSideConfig() {
		SideConfig cfg = new SideConfig();
		cfg.numConfig = 0;
		cfg.slotGroups = new int[0][0];
		cfg.allowInsertionSide = new boolean[0];
		cfg.allowExtractionSide = new boolean[0];
		cfg.allowInsertionSlot = new boolean[0];
		cfg.allowExtractionSlot = new boolean[0];
		cfg.sideTex = new int[0];
		cfg.defaultSides = new byte[] {
				0, 0, 0, 0, 0, 0
		};
		return cfg;
	}
}
