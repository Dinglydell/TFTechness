package dinglydell.tftechness.tileentities.machine;

import java.lang.reflect.Field;

import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidStack;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermalexpansion.block.machine.TileAccumulator;

import com.bioxx.tfc.api.TFCFluids;

public class TileTFTAccumulator extends TileAccumulator {
	
	Field inHell;
	Field adjacentSources;
	
	public TileTFTAccumulator() {
		this.getTank().setLock(TFCFluids.FRESHWATER);
		try {
			inHell = this.getClass().getSuperclass().getDeclaredField("inHell");
			adjacentSources = this.getClass().getSuperclass().getDeclaredField("adjacentSources");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	protected void updateAdjacentSources() {
		try {
			boolean hell = (this.worldObj.getBiomeGenForCoords(this.xCoord, this.yCoord) == BiomeGenBase.hell);
			inHell.setAccessible(true);
			inHell.setBoolean(this, hell);
			
			int adjacent = 0;
			
			for (int i = 0; i < 6; i++) {
				int[] coords = BlockHelper.getAdjacentCoordinatesForSide(xCoord, yCoord, zCoord, i);
				FluidStack f = FluidHelper.getFluidFromWorld(worldObj, coords[0], coords[1], coords[2]);
				if (f != null && f.getFluid() == getTankFluid().getFluid()) {
					adjacent++;
				}
			}
			adjacentSources.setAccessible(true);
			adjacentSources.setInt(this, adjacent);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
