package dinglydell.tftechness.tileentities.machine;

import net.minecraft.util.IIcon;
import cofh.core.render.IconRegistry;
import cofh.thermalexpansion.block.machine.TileMachineBase;

public class TileTFTMachine {
	
	public TileTFTMachine() {
		// TODO Auto-generated constructor stub
	}
	
	public static IIcon getTexture(TileMachineBase tile, int side, int render) {
		
		return tile.isActive ? IconRegistry.getIcon("TFTMachineActive", tile.getType())
				: side != tile.getFacing() ? IconRegistry.getIcon("TFTMachineSide")
						: IconRegistry.getIcon("TFTMachineFace", tile.getType());
	}
	
}
