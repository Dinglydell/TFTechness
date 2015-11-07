package dinglydell.tftechness.recipe;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import dinglydell.tftechness.TFTechness;

public class RemoveBatch {
	private ArrayList<ItemStack> crafting = new ArrayList<ItemStack>();
	
	public void addCrafting(ItemStack item) {
		crafting.add(item);
		
	}
	
	public void Execute() {
		ExecuteCrafting();
		
	}
	
	private void ExecuteCrafting() {
		TFTechness.logger.info("Removing recipes");
		Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
		while (iterator.hasNext()) {
			IRecipe recipe = iterator.next();
			if (recipe == null)
				continue;
			ItemStack output = recipe.getRecipeOutput();
			
			if (output != null) {
				boolean found = false;
				for (int i = 0; i < crafting.size() && !found; i++) {
					found = OreDictionary.itemMatches(output, crafting.get(i), false);
				}
				if (found) {
					iterator.remove();
				}
			}
		}
		
	}
	
}
