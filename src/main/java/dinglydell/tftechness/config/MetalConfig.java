package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class MetalConfig {
	public static int ingotFluidmB;
	
	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Metal", "General metal config. For specific metals, see Metals.cfg.");
		ingotFluidmB = config.getInt("ingotmB",
				"Metal",
				100,
				1,
				Integer.MAX_VALUE,
				"The amount of fluid (mB) an ingot of a metal will melt into. Default is 100 to align nicely with TFC metal units");
	}
}
