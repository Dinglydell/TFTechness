package dinglydell.tftechness.gui.element;

import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.GuiBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;

public class ElementSlotMiniTank extends ElementSlotOverlay {
	public static final ResourceLocation miniTankTexture = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/elements/Slots.png");

	public ElementSlotMiniTank(GuiBase gui, int x, int y) {
		super(gui, x, y);
		this.sizeX = 16;
		this.texture = miniTankTexture;
		this.sizeY = 40;
	}

	@Override
	protected void drawSlotNoBorder(int arg0, int arg1) {
		int i = this.slotColor / 3 * 128;
		int j = this.slotColor % 3 * 32;
		this.gui.drawTexturedModalRect(arg0, arg1, i, j, this.sizeX, this.sizeY);
	}

	@Override
	protected void drawSlotWithBorder(int x, int y) {

		int i = 32;
		int j = 45;
		int k = this.slotColor * 32;
		// int m = this.slotColor % 3 * 32;
		int m = 0;
		if (slotRender == 1) {
			j /= 2;
			y += j;
			m += j;
		}
		this.gui.drawTexturedModalRect(x - 2, y - 2, k, m, i, j);
	}

}
