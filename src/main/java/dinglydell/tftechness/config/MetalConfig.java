package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class MetalConfig {
	public static int ingotFluidmB;
	public static float workableTemperature;
	public static float weldableTemperature;

	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Metal",
				"General metal config. For specific metals, see Metals.cfg.");
		ingotFluidmB = config
				.getInt("ingotmB",
						"Metal",
						100,
						1,
						Integer.MAX_VALUE,
						"The amount of fluid (mB) an ingot of a metal will melt into. Default is 100 to align nicely with TFC metal units");

		workableTemperature = config
				.getFloat("workableTemperature",
						"Metal",
						0.6f,
						0,
						1,
						"The % of melting temperature at which an item can be worked. WARNING: This does not affect use in TFC and is only here because TFC does not have its own constant to refer to.");
		weldableTemperature = config
				.getFloat("weldableTemperature",
						"Metal",
						0.6f,
						0,
						1,
						"The % of melting temperature at which an item can be welded. WARNING: This does not affect use in TFC and is only here because TFC does not have its own constant to refer to.");
	}
}
