package dinglydell.tftechness.render.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.render.IconRegistry;

public class RenderBucket implements IItemRenderer {
	
	public static final String redSteelIcon = "TFTRedSteelBucket";
	public static final String blueSteelIcon = "TFTBlueSteelBucket";
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		RenderItem renderItem = RenderItem.getInstance();
		IIcon icon = item.getIconIndex();
		renderItem.renderIcon(0, 0, icon, 16, 16);
		FluidStack f = FluidContainerRegistry.getFluidForFilledItem(item);
		
		if (item.getItemDamage() == 0) {
			icon = IconRegistry.getIcon(redSteelIcon);
		} else {
			icon = IconRegistry.getIcon(blueSteelIcon);
		}
		renderItem.renderIcon(0, 0, icon, 16, 16);
	}
}
