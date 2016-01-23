package dinglydell.tftechness.gui.element;

import java.util.List;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.render.RenderHelper;
import dinglydell.tftechness.TFTechness;

public abstract class ElementVerticalSlider extends ElementBase {
	public static String defaultTexture = TFTechness.MODID
			+ ":textures/gui/elements/SliderIndicator.png";
	protected static final int sliderTextureWidth = 15;
	protected static final int sliderTextureHeight = 5;
	protected static final int sliderTextureInset = 3;

	public ElementVerticalSlider(GuiBase gui, int x, int y, int width,
			int height) {
		super(gui, x, y, width, height);
		setTexture(defaultTexture, sliderTextureWidth, sliderTextureHeight);

	}

	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		RenderHelper.bindTexture(texture);
		// drawTexturedModalRect(posX, posY, 0, 0, 16, sizeY);
		int pos = deScaleY(getSliderScaledPosition()) - sliderTextureHeight / 2;
		drawTexturedModalRect(posX - sliderTextureInset,
				pos,
				0,
				0,
				sliderTextureWidth,
				sliderTextureHeight);
	}

	private int deScaleY(double y) {
		return (int) (posY + Math.min(1, Math.max(0, y)) * sizeY);
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

	}

	@Override
	public abstract void addTooltip(List<String> list);

	public abstract double getSliderScaledPosition();

}
