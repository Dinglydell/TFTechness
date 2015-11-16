package dinglydell.tftechness.recipe;

import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class FurnaceRecipe {
	public ItemStack input;
	public ItemStack output;
	public float xp;
	
	public FurnaceRecipe(ItemStack input, ItemStack output, float f) {
		this.input = input;
		this.output = output;
		this.xp = f;
	}
	
	public void register() {
		GameRegistry.addSmelting(input, output, xp);
	}
}
