package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class MachineConfig {
	
	public static boolean extruderEnabled;
	public static boolean accumulatorEnabled;
	public static boolean cryoChamberEnabled;
	
	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Machines",
				"TFTechness changes some the way some ThermalExpansion machines work. (eg, makes the machine use TFC water instead of vanilla). Use this to disable the TFTechness versions and use ThermalExpansion.");
		extruderEnabled = config.getBoolean("extruder",
				"Machines",
				true,
				"Accepts salt water, fresh water and TFC lava and produces salt when salt water is consumed.");
		accumulatorEnabled = config.getBoolean("accumulator",
				"Machines",
				true,
				"Produces fresh or salt water instead of vanilla water. Cannot passively produce salt water.");
		cryoChamberEnabled = config.getBoolean("cryogenicChamber",
				"Machines",
				true,
				"This is a machine added by TFTechness - known in some circles as a \"fridge\" - it uses RF to keep food nice and cool to prevent decay.");
	}
	
}
