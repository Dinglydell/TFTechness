package dinglydell.tftechness.tileentities.dynamo;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.thermalexpansion.block.dynamo.TileDynamoSteam;

import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.tileentities.IAugmentNBT;

public class TileTFTDynamoSteam extends TileDynamoSteam implements IAugmentNBT {
	
	public Fluid steam = FluidRegistry.getFluid("steam");
	
	@Override
	public int fill(ForgeDirection dir, FluidStack fs, boolean b) {
		if ((fs == null) || ((!augmentCoilDuct) && (dir.ordinal() == getFacing()))) {
			return 0;
		}
		if (fs.getFluid() == steam) {
			return getSteamTank().fill(fs, b);
		}
		if (fs.getFluid() == TFCFluids.FRESHWATER) {
			return getWaterTank().fill(fs, b);
		}
		return 0;
	}
	
	public FluidTankAdv getSteamTank() {
		return getTank(0);
	}
	
	public FluidTankAdv getWaterTank() {
		return getTank(1);
	}
}
