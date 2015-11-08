package dinglydell.tftechness.block.machine;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cofh.thermalexpansion.block.machine.TileExtruder;

import com.bioxx.tfc.api.TFCFluids;

public class TileTFTExtruder extends TileExtruder {
	
	@Override
	public int fill(ForgeDirection direction, FluidStack fluid, boolean paramBoolean) {
		if (fluid.getFluid() == TFCFluids.FRESHWATER) {
			
			return getTank(1).fill(fluid, paramBoolean);
		}
		if (fluid.getFluid() == TFCFluids.SALTWATER) {
			
			return getTank(1).fill(fluid, paramBoolean);
		}
		if (fluid.getFluid() == TFCFluids.LAVA) {
			
			return getTank(0).fill(fluid, paramBoolean);
		}
		return 0;
		
	}
}
