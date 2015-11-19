package dinglydell.tftechness.fluid;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.Metal;

import cpw.mods.fml.common.registry.GameRegistry;
import dinglydell.tftechness.block.TFTBlocks;
import dinglydell.tftechness.metal.MetalSnatcher;

public class TFTFluids {
	public static Map<String, Fluid> metal = new HashMap();
	
	public static void createFluids() {
		for (Map.Entry<String, Metal> m : MetalSnatcher.getMetals().entrySet()) {
			Fluid f;
			if (!FluidRegistry.isFluidRegistered(m.getKey().toLowerCase())) {
				HeatIndex hi = MetalSnatcher.getHeatIndexFromMetal(m.getValue());
				float kelvin = hi.meltTemp + 273.15f;
				int lum = Math.max(0, (int) (15 * (hi.meltTemp - 22) / FluidRegistry.LAVA.getTemperature()));
				lum = Math.min(15, lum);
				f = new Fluid(m.getKey().toLowerCase()).setLuminosity(lum).setTemperature((int) kelvin);
				FluidRegistry.registerFluid(f);
			} else {
				f = FluidRegistry.getFluid(m.getKey().toLowerCase());
			}
			
			metal.put(m.getKey(), f);
			Block b = new BlockMoltenMetal(f);
			TFTBlocks.moltenMetal.put(m.getKey(), b);
			GameRegistry.registerBlock(b, "molten" + m.getKey().replaceAll("\\s", ""));
		}
	}
}
