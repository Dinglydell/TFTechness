package dinglydell.tftechness.gui.element;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementButtonBase;
import cofh.lib.render.RenderHelper;
import dinglydell.tftechness.TFTechness;

public class ElementButtonItem extends ElementButtonBase {

	//private GuiBase gui;
	private ItemStack itemStack;
	private String name;
	private String tooltip;
	protected static final RenderItem ITEM_RENDERER = new RenderItem();
	private static final String TEXTURE = TFTechness.MODID
			+ ":textures/gui/elements/ButtonItem.png";
	private static final int TEX_WIDTH = 40;
	private static final int TEX_HEIGHT = 20;
	private static final int WIDTH = 20;
	private static final int HEIGHT = 20;

	public ElementButtonItem(GuiBase gui, int posX, int posY, String name,
			ItemStack is) {
		super(gui, posX, posY, WIDTH, HEIGHT);
		setTexture(TEXTURE, TEX_WIDTH, TEX_HEIGHT);
		this.itemStack = is;
		setName(name);
	}

	//public GuiRFPlanButton(int index, int xPos, int yPos, int width,
	//		int height, ItemStack is, GuiBase gui, String s) {
	//	super(index, xPos, yPos, width, height, s);
	//	this.itemStack = is;
	//	this.gui = gui;
	//}

	//@Override
	//public void drawButton(Minecraft mc, int x, int y) {
	//	if (this.visible) {
	//		int k = this.getHoverState(this.field_146123_n) - 1;
	//
	//		TFC_Core.bindTexture(GuiPlanSelection.texture);
	//		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	//		this.drawTexturedModalRect(this.xPosition,
	//				this.yPosition,
	//				176,
	//				k * 18,
	//				18,
	//				18);
	//		//this.field_146123_n = isPointInRegion(x, y);//x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
	//
	//		if (itemStack != null) {
	//			renderInventorySlot(itemStack,
	//					this.xPosition + 1,
	//					this.yPosition + 1);
	//		}
	//
	//		this.mouseDragged(mc, x, y);
	//
	//		if (field_146123_n) {
	//			gui.addTooltips(arg0)(x, y, this.displayString);
	//			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	//		}
	//	}
	//}

	protected void renderInventorySlot(ItemStack is, int x, int y) {
		if (is != null) {
			ITEM_RENDERER
					.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer,
							Minecraft.getMinecraft().getTextureManager(),
							is,
							x,
							y);
		}
	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		//GL11.glColor4f(1, 1, 1, 1);
		RenderHelper.bindTexture(texture);
		if (intersectsWith(mouseX, mouseY)) {
			drawTexturedModalRect(posX, posY, sizeX, 0, sizeX, sizeY);

		} else {
			drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
		}
		renderInventorySlot(itemStack, posX + (sizeX - 16) / 2, posY
				+ (sizeY - 16) / 2);

	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

	@Override
	public boolean onMousePressed(int x, int y, int mouseButton) {
		if (isEnabled()) {
			gui.handleElementButtonClick(getName(), mouseButton);
			return true;
		}
		return false;
	}

	public void setItem(ItemStack itemStack) {
		this.itemStack = itemStack;

	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public void addTooltip(List<String> list) {

		if (tooltip != null) {

			list.add(tooltip);
		}
	}
}
