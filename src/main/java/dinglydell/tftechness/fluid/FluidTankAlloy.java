package dinglydell.tftechness.fluid;

import java.util.ArrayList;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.Core.Metal.AlloyManager;
import com.bioxx.tfc.Core.Metal.AlloyMetal;
import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.api.Metal;

/** A mixed fluid tank that stores molten fluids and creates alloys */
public class FluidTankAlloy extends FluidTankMixed implements IFluidTank {
	
	protected FluidStack currentAlloy;
	
	public FluidTankAlloy(int capacity) {
		super(capacity);
	}
	
	@Override
	public int fill(FluidStack fluid, boolean doFill) {
		if (!(fluid.getFluid() instanceof FluidMoltenMetal)) {
			return 0;
		}
		
		return super.fill(fluid, doFill);
	}
	
	public FluidStack getAlloy() {
		int totalAmt = getFluidAmount();
		ArrayList<AlloyMetal> ing = new ArrayList<AlloyMetal>();
		for (FluidStack fs : getFluids()) {
			Metal m = ((FluidMoltenMetal) fs.getFluid()).metal;
			ing.add(new AlloyMetal(m, fs.amount / (float) totalAmt * 100f));
		}
		Metal alloy = AlloyManager.INSTANCE.matchesAlloy(ing, Alloy.EnumTier.TierV);
		if (alloy == null || totalAmt == 0) {
			alloy = MetalRegistry.instance.getMetalFromString("Unknown");
		}
		return new FluidStack(TFTFluids.metal.get(alloy.name), totalAmt);
	}
	
	@Override
	public FluidStack getFluid() {
		return currentAlloy;
	}
	
	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		Map<Fluid, FluidStack> fMap = super.drainAll(maxDrain, doDrain);
		int amt = 0;
		for (FluidStack fs : fMap.values()) {
			amt += fs.amount;
		}
		FluidStack fs;
		if (currentAlloy == null) {
			fs = new FluidStack(TFTFluids.metal.get("Unknown"), amt);
		} else {
			fs = new FluidStack(currentAlloy, amt);
		}
		return fs;
	}
}
