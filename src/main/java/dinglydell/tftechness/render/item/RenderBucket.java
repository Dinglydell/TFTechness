package dinglydell.tftechness.render.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.render.IconRegistry;
import cofh.core.render.RenderUtils;
import dinglydell.tftechness.item.ItemTFTSteelBucket;

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
		RenderUtils.preItemRender();
		
		// if (type.equals(IItemRenderer.ItemRenderType.ENTITY)) {
		// GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
		// GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
		// GL11.glScaled(0.75D, 0.75D, 0.75D);
		// GL11.glTranslated(-0.5D, -0.6D, 0.0D);
		// } else if (type.equals(IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON)) {
		// GL11.glTranslated(1.0D, 1.0D, 0.0D);
		// GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
		// } else if (type.equals(IItemRenderer.ItemRenderType.EQUIPPED)) {
		// GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
		// GL11.glTranslated(-1.0D, -1.0D, 0.0D);
		// }
		
		RenderItem renderItem = RenderItem.getInstance();
		
		IIcon icon = item.getIconIndex();
		renderItem.renderIcon(0, 0, icon, 16, 16);
		FluidStack f = FluidContainerRegistry.getFluidForFilledItem(item);
		
		if (f.getFluid().getTemperature() < ItemTFTSteelBucket.tempThreshold) {
			icon = IconRegistry.getIcon(redSteelIcon);
		} else {
			icon = IconRegistry.getIcon(blueSteelIcon);
		}
		renderItem.renderIcon(0, 0, icon, 16, 16);
		
		RenderUtils.postItemRender();
	}
}
