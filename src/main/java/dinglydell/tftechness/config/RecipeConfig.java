package dinglydell.tftechness.config;

import net.minecraftforge.common.config.Configuration;

public class RecipeConfig {
	public static boolean gearsEnabled;
	public static boolean tanksEnabled;
	public static boolean coilsEnabled;
	public static boolean upgradeCrafting;
	public static boolean replaceMachine;
	
	public static void loadConfig(Configuration config) {
		config.addCustomCategoryComment("Recipes",
				"true: TFTechness changes the recipe, false: TFTechness leave it alone.");
		RecipeConfig.replaceMachine = config.getBoolean("replaceMachineIngots",
				"Recipes",
				true,
				"Replaces TE machine recipes that result in TF metals with TFT metals.");
		RecipeConfig.gearsEnabled = config.getBoolean("gears", "Recipes", true, "");
		RecipeConfig.tanksEnabled = config.getBoolean("portableTanks", "Recipes", true, "");
		RecipeConfig.coilsEnabled = config.getBoolean("redstoneCoils", "Recipes", true, "");
		RecipeConfig.upgradeCrafting = config.getBoolean("upgradeCrafting",
				"Recipes",
				true,
				"ThermalExpansion machine upgrades.");
		
	}
}
