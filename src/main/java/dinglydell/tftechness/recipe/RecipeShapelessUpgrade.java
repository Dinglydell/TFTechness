package dinglydell.tftechness.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cofh.lib.util.helpers.ItemHelper;
import dinglydell.tftechness.TFTechness;

public class RecipeShapelessUpgrade extends ShapelessOreRecipe {
	
	ItemStack upgrade;
	
	public RecipeShapelessUpgrade(Block result, Block upgrade, Object... recipe) {
		super(result, recipe);
		this.upgrade = new ItemStack(upgrade);
	}
	
	public RecipeShapelessUpgrade(Item result, Item upgrade, Object... recipe) {
		super(result, recipe);
		this.upgrade = new ItemStack(upgrade);
	}
	
	public RecipeShapelessUpgrade(ItemStack result, ItemStack upgrade, Object... recipe) {
		super(result, recipe);
		this.upgrade = upgrade;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting craftMatrix) {
		int targetSlot = 0;
		ItemStack stack = null;
		for (targetSlot = 0; targetSlot < 9
				&& (stack == null || !(Item.getIdFromItem(stack.getItem()) == Item.getIdFromItem(upgrade.getItem()) && stack.getItemDamage() == upgrade.getItemDamage())); targetSlot++) {
			TFTechness.logger.info(targetSlot);
			stack = craftMatrix.getStackInSlot(targetSlot);
		}
		targetSlot--;
		if (craftMatrix.getStackInSlot(targetSlot) == null
				|| craftMatrix.getStackInSlot(targetSlot).stackTagCompound == null) {
			return super.getCraftingResult(craftMatrix);
		}
		return ItemHelper.copyTag(getRecipeOutput().copy(), craftMatrix.getStackInSlot(targetSlot));
	}
}
