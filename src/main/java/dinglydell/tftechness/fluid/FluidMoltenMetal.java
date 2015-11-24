package dinglydell.tftechness.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.api.Metal;

import dinglydell.tftechness.util.Localisation;

public class FluidMoltenMetal extends Fluid {
	
	protected Metal metal;
	
	public FluidMoltenMetal(String fluidName, Metal metal) {
		super(fluidName);
		this.metal = metal;
	}
	
	@Override
	public String getLocalizedName(FluidStack stack) {
		return StringHelper.localize(getUnlocalizedName()) + " " + Localisation.getMetalName(metal);
	}
	
	@Override
	public String getUnlocalizedName() {
		return "fluid.moltenMetal";
	}
}
