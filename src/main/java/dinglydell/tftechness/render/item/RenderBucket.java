package dinglydell.tftechness.render.item;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

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
			GL11.glTranslatef(-0.5f, -0.3f, thickness / 2.0f);
		}
		ItemTFTSteelBucket b = (ItemTFTSteelBucket) item.getItem();
		
		IIcon icon = item.getIconIndex();
		renderIcon(icon, type);
		// FluidStack f = FluidContainerRegistry.getFluidForFilledItem(item);
		if (b.isUpsideDown()) {
			GL11.glRotatef(180, 0, 0, 1);
			switch (type) {
				case INVENTORY:
					GL11.glTranslatef(-16f, -17f, 0);
					break;
				case ENTITY:
					GL11.glTranslatef(0, 0, 0.001f);
				default:
					GL11.glTranslatef(-1f, -1f + (1 / 16), 0);
					GL11.glScalef(1.02f, 1.02f, 1.02f);
			}
			
		}
		icon = b.getOverlayIcon();
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
