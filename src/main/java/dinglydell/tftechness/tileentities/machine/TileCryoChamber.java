package dinglydell.tftechness.tileentities.machine;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.Core.TFC_Climate;
import com.bioxx.tfc.Core.TFC_Core;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.gui.GuiCryoChamber;
import dinglydell.tftechness.gui.container.ContainerCryoChamber;
import dinglydell.tftechness.tileentities.ISlideable;

public class TileCryoChamber extends TileTFTMachine implements ISlideable {
	/** Heat transfer coefficient of air */
	protected static final float heatTransferCoefficient = 10.45f;
	/** Surface area of the cube: 6m^2 */
	protected static final int surfaceArea = 6;
	protected static final float specificHeat = 1.5f;
	protected static final int mass = 1200;
	/** This rounds up the constant values to save the calculation from needing to happen every
	 * single tick */
	protected static final float heatMultiplier = heatTransferCoefficient * surfaceArea / (20 * mass * specificHeat);
	/** The temperature at which the device stops trying so hard to cooldown */
	private static final float slowDownTemperature = 0;
	/** The hightest temperature the device desires to be */
	protected static final float targetTemperatureMax = -5;
	/** The lowest temperature the device desires to be */
	protected static final float targetTemperatureMin = -6;
	/** The lowest temperature the slider should display */
	protected static final float sliderLowest = -10;
	/** The highest temperature the slider should display */
	protected static final float sliderHighest = 40;
	
	protected static final int sliderMarkerIntervals = 10;
	
	protected static final float inefficiency = 5;
	
	protected float lastChange;
	
	protected float internalTemperature = 22;
	
	public TileCryoChamber() {
		super();
		inventory = new ItemStack[10];
		
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
	
	public void updateEntity() {
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord);
			float dT = temp - internalTemperature;
			float change = heatMultiplier * dT;
			internalTemperature += change;
			lastChange = change;
			float enviroDecay = 0;
			// If ambient temperature is below 0, not much can be done as TFC is hardcoded to have
			// no decay.
			if (temp > 0) {
				enviroDecay = TFC_Core.getEnvironmentalDecay(internalTemperature)
						/ TFC_Core.getEnvironmentalDecay(temp);
				// } else {
				// enviroDecay = TFC_Core.getEnvironmentalDecay(internalTemperature);
			}
			TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, enviroDecay);
		}
		super.updateEntity();
		
	}
	
	@Override
	protected int spendEnergy(int rf) {
		if (internalTemperature <= targetTemperatureMin) {
			return 0;
		}
		float tmp = Math.max(0, lastChange);
		float j = tmp * mass * specificHeat * inefficiency;
		int dRf = (int) Math.ceil(j / TFTechness.rfToJoules);
		if (dRf > rf || internalTemperature >= targetTemperatureMax) {
			j = rf * TFTechness.rfToJoules;
			tmp = j / (mass * specificHeat * inefficiency);
			internalTemperature -= tmp;
			return rf;
			
		}
		
		internalTemperature -= tmp;
		return dRf;
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
	
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFloat(internalTemperature);
		return packet;
	}
	
	protected void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		internalTemperature = packet.getFloat();
	}
	
	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiCryoChamber(inv, this);
		
	}
	
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerCryoChamber(inv, this);
	}
	
	@Override
	protected EnergyConfig getEnergyConfig() {
		energyConsumption = 80;
		EnergyConfig cfg = new EnergyConfig();
		cfg.maxEnergy = 96000;
		cfg.maxPower = 160;
		return cfg;
	}
	
	@Override
	public double getSliderScaledPosition() {
		return 1 - ((internalTemperature - sliderLowest) / (sliderHighest - sliderLowest));
	}
	
	@Override
	public void addSliderTooltip(List<String> list) {
		list.add((Math.round(internalTemperature * 100) / 100.0) + "\u00b0C");
		
	}
	
}
