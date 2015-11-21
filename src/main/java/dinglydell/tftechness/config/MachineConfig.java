package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class MachineConfig {
	
	public static boolean extruderEnabled;
	public static boolean accumulatorEnabled;
	public static boolean precipitatorEnabled;
	public static boolean cryoChamberEnabled;
	public static boolean dynamoSteamEnabled;
	public static boolean dynamoCompressionEnabled;
	
	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Machines",
				"TFTechness changes some the way some ThermalExpansion machines work. (eg, makes the machine use TFC water instead of vanilla). Use this to disable the TFTechness versions and use ThermalExpansion. Includes dynamos.");
		extruderEnabled = config.getBoolean("extruder",
				"Machines",
				true,
				"Accepts salt water, fresh water and TFC lava and produces salt when salt water is consumed.");
		accumulatorEnabled = config.getBoolean("accumulator",
				"Machines",
				true,
				"Produces fresh or salt water instead of vanilla water. Cannot passively produce salt water.");
		precipitatorEnabled = config.getBoolean("precipitator",
				"Machines",
				true,
				"Takes fresh or salt water and produces the appropriate ice. Both fluids can produce snow.");
		dynamoSteamEnabled = config.getBoolean("dynamoSteam",
				"Machines",
				true,
				"Uses fresh water instead of vanilla water");
		dynamoCompressionEnabled = config.getBoolean("dynamoCompression",
				"Machines",
				true,
				"Unlike the others, does not create another block. Simply allows the TE compression dynamo to use fresh water.");
		cryoChamberEnabled = config.getBoolean("cryogenicChamber",
				"Machines",
				true,
				"This is a machine added by TFTechness - known in some circles as a \"fridge\" - it uses RF to keep food nice and cool to prevent decay.");
	}
	
}
