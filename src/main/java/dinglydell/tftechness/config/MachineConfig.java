package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class MachineConfig {
	
	public static boolean extruderEnabled;
	public static boolean accumulatorEnabled;
	
	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Machines",
				"TFTechness changes some the way some ThermalExpansion machines work. (eg, makes the machine use TFC water instead of vanilla). Use this to disable the TFTechness versions and use ThermalExpansion.");
		MachineConfig.extruderEnabled = config.getBoolean("extruder",
				"Machines",
				true,
				"Accepts salt water, fresh water and TFC lava and produces salt when salt water is consumed.");
		MachineConfig.accumulatorEnabled = config.getBoolean("accumulator",
				"Machines",
				true,
				"Produces fresh or salt water instead of vanilla water. Cannot passively produce salt water.");
	}
	
}
