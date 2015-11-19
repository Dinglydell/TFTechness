package dinglydell.tftechness.tileentities.machine;

import net.minecraft.util.IIcon;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalexpansion.block.TileAugmentable;
import cofh.thermalexpansion.block.machine.BlockMachine;
import dinglydell.tftechness.TFTechness;
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
	
	public TileTFTMachine() {
		sideConfig = getSideConfig();
	}
	
	@Override
	public void updateEntity() {
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			if (isActive) {
				if (energyStorage.getEnergyStored() - energyConsumption > 0) {
					energyStorage.modifyEnergyStored(-spendEnergy(energyConsumption));
					energyStorage.modifyEnergyStored(-energyConsumption);
				} else {
					energyStorage.modifyEnergyStored(-spendEnergy(energyStorage.getEnergyStored()));
				}
				
				if (!redstoneControlOrDisable() || shouldDeactivate()) {
					isActive = false;
					onDeactivate();
				}
			} else {
				if (redstoneControlOrDisable() || shouldActivate()) {
					isActive = true;
					onActivate();
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return "tile." + TFTechness.MODID + ".machine." + BlockMachine.Types.values()[getType()].name().toLowerCase();
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
}
