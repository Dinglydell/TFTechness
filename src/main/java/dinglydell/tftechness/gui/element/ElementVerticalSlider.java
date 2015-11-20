package dinglydell.tftechness.gui.element;

import java.util.List;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.render.RenderHelper;
import dinglydell.tftechness.tileentities.ISlideable;

public class ElementVerticalSlider extends ElementBase {
	ISlideable slider;
	
	public ElementVerticalSlider(GuiBase gui, int x, int y, ISlideable slide) {
		super(gui, x, y, 16, 61);
		slider = slide;
		texture = ElementEnergyStored.DEFAULT_TEXTURE;
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		RenderHelper.bindTexture(texture);
		// drawTexturedModalRect(posX, posY, 0, 0, 16, sizeY);
		int pos = deScaleY(slider.getSliderScaledPosition());
		drawTexturedModalRect(posX, pos, 0, 0, sizeX, 1);
	}
	
	private int deScaleY(double y) {
		return (int) (posY + Math.min(1, Math.max(0, y)) * sizeY);
	}
	
	@Override
	public void drawForeground(int mouseX, int mouseY) {
		
	}
	
	@Override
	public void addTooltip(List<String> list) {
		slider.addSliderTooltip(list);
	}
	
}
