package dinglydell.tftechness.render.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cofh.core.render.IconRegistry;
import cofh.core.render.RenderUtils;
import cofh.lib.render.RenderHelper;
import dinglydell.tftechness.item.ItemTFTSteelBucket;

public class RenderBucket implements IItemRenderer {
	
	public static final String redSteelIcon = "TFTRedSteelBucket";
	public static final String blueSteelIcon = "TFTBlueSteelBucket";
	public static final float thickness = 0.0625F;
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}
	
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return helper == ItemRendererHelper.ENTITY_BOBBING || helper == ItemRendererHelper.ENTITY_ROTATION;
	}
	
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		RenderUtils.preItemRender();
		if (type.equals(ItemRenderType.ENTITY)) {
			GL11.glTranslatef(-0.5F, -0.3F, thickness / 2.0F);
		}
		IIcon icon = item.getIconIndex();
		renderIcon(icon, type);
		FluidStack f = FluidContainerRegistry.getFluidForFilledItem(item);
		
		if (f.getFluid().getTemperature() < ItemTFTSteelBucket.tempThreshold) {
			icon = IconRegistry.getIcon(redSteelIcon);
		} else {
			icon = IconRegistry.getIcon(blueSteelIcon);
		}
		renderIcon(icon, type);
		RenderUtils.postItemRender();
	}
	
	private void renderIcon(IIcon icon, ItemRenderType type) {
		if (type.equals(ItemRenderType.INVENTORY)) {
			RenderItem.getInstance().renderIcon(0, 0, icon, 16, 16);
		} else {
			
			RenderHelper.renderItemIn2D(icon);
		}
		
	}
}
