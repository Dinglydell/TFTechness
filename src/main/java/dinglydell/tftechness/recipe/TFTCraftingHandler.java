package dinglydell.tftechness.recipe;

import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper;
import cofh.thermalexpansion.util.crafting.RecipeMachineUpgrade;

public class TFTCraftingHandler {
	public static void addMachineUpgradeRecipes(ItemStack is) {
		NEIRecipeWrapper.addUpgradeRecipe(new RecipeMachineUpgrade(1,
				RecipeMachineUpgrade.getMachineLevel(is, 1),
				new Object[] {
						" G ",
						" X ",
						" S ",
						Character.valueOf('S'),
						"plateDoubleInvar",
						Character.valueOf('G'),
						"gearElectrum",
						Character.valueOf('X'),
						RecipeMachineUpgrade.getMachineLevel(is, 0)
				}));
		NEIRecipeWrapper.addUpgradeRecipe(new RecipeMachineUpgrade(2,
				RecipeMachineUpgrade.getMachineLevel(is, 2),
				new Object[] {
						" G ",
						" X ",
						" S ",
						Character.valueOf('S'),
						"blockGlassHardened",
						Character.valueOf('G'),
						"gearSignalum",
						Character.valueOf('X'),
						RecipeMachineUpgrade.getMachineLevel(is, 1)
				}));
		NEIRecipeWrapper.addUpgradeRecipe(new RecipeMachineUpgrade(3,
				RecipeMachineUpgrade.getMachineLevel(is, 3),
				new Object[] {
						" G ",
						" X ",
						" S ",
						Character.valueOf('S'),
						"plateDoubleSilver",
						Character.valueOf('G'),
						"gearEnderium",
						Character.valueOf('X'),
						RecipeMachineUpgrade.getMachineLevel(is, 2)
				}));
	}
	
}
