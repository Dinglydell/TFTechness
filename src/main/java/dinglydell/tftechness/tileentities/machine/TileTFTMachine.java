package dinglydell.tftechness.tileentities.machine;

import net.minecraft.util.IIcon;
import cofh.core.render.IconRegistry;
import cofh.thermalexpansion.block.machine.TileMachineBase;

public class TileTFTMachine {
	
	public static IIcon getTexture(TileMachineBase tile, int side, int render) {
		
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
}
