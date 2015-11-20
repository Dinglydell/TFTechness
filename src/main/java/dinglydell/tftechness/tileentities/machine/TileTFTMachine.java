package dinglydell.tftechness.tileentities.machine;

import net.minecraft.util.IIcon;
import cofh.api.energy.EnergyStorage;
import cofh.core.network.PacketCoFHBase;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalexpansion.block.TileAugmentable;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.tileentities.IAugmentNBT;

public abstract class TileTFTMachine extends TileAugmentable implements IAugmentNBT {
	
	public static IIcon getTexture(TileAugmentable tile, int side, int render) {
		
		return (side != tile.getFacing()) ? IconRegistry.getIcon("TFTMachineSide")
				: (tile.isActive ? IconRegistry.getIcon("TFTMachineActive", tile.getType())
						: IconRegistry.getIcon("TFTMachineFace", tile.getType()));
	}
	
	/** ThermalExpansion side configuration colours */
	public enum Colours {
		none, blue, red, yellow, orange, green, purple, grey;
		
		public int gui() {
			return ordinal() - 1;
		}
	}
	
	protected int energyConsumption;
	protected int lastEnergyConsumption;
	protected EnergyConfig energyConfig;
	
	public TileTFTMachine() {
		sideConfig = getSideConfig();
		energyConfig = getEnergyConfig();
		energyStorage = new EnergyStorage(energyConfig.maxEnergy, energyConfig.maxPower);
	}
	
	@Override
	public void updateEntity() {
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			chargeEnergy();
			if (isActive) {
				if (energyStorage.getEnergyStored() - energyConsumption > 0) {
					lastEnergyConsumption = spendEnergy(energyConsumption);
					energyStorage.modifyEnergyStored(-lastEnergyConsumption);
				} else {
					lastEnergyConsumption = 0;
					energyStorage.modifyEnergyStored(-spendEnergy(energyStorage.getEnergyStored()));
				}
				
				if (!redstoneControlOrDisable() || shouldDeactivate()) {
					isActive = false;
					onDeactivate();
				}
			} else {
				lastEnergyConsumption = 0;
				if (redstoneControlOrDisable() || shouldActivate()) {
					isActive = true;
					onActivate();
				}
			}
		}
	}
	
	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addInt(lastEnergyConsumption);
		return packet;
	}
	
	@Override
	protected void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		lastEnergyConsumption = packet.getInt();
	}
	
	@Override
	public int getInfoEnergyPerTick() {
		return lastEnergyConsumption;
	}
	
	@Override
	public int getInfoMaxEnergyPerTick() {
		return energyConsumption;
	}
	
	@Override
	public String getName() {
		return "tile." + TFTechness.MODID + ".machine."
				+ BlockTFTMachine.Types.values()[getType()].name().toLowerCase() + ".name";
	}
	
	@Override
	public IIcon getTexture(int side, int render) {
		return getTexture(this, side, render);
	}
	
	protected abstract boolean shouldActivate();
	
	protected abstract boolean shouldDeactivate();
	
	protected abstract void onActivate();
	
	protected abstract void onDeactivate();
	
	protected abstract int spendEnergy(int rf);
	
	protected abstract SideConfig getSideConfig();
	
	protected abstract EnergyConfig getEnergyConfig();
}
