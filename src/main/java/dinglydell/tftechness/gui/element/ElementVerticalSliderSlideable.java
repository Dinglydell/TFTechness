package dinglydell.tftechness.gui.element;

import cofh.lib.gui.GuiBase;
import dinglydell.tftechness.gui.ISliderHandler;

public abstract class ElementVerticalSliderSlideable extends
		ElementVerticalSlider {

	protected int value;
	protected int minValue;
	protected int maxValue;
	protected boolean isDragging = false;
	protected ISliderHandler slider;

	public ElementVerticalSliderSlideable(GuiBase gui, ISliderHandler slider,
			int x, int y, int width, int height, int minValue, int maxValue,
			int defaultValue) {
		super(gui, x, y, width, height);
		this.minValue = minValue;
		this.value = defaultValue;
		this.maxValue = maxValue;
		this.slider = slider;
	}

	@Override
	public boolean onMousePressed(int mouseX, int mouseY, int mouseButton) {
		isDragging = mouseButton == 0;
		update(mouseY, mouseY);
		return true;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY) {
		isDragging = false;
	}

	@Override
	public boolean onMouseWheel(int mouseX, int mouseY, int movement) {

		if (movement > 0) {
			setValue(value + 1);
		} else if (movement < 0) {
			setValue(value - 1);
		}
		return true;
	}

	private void setValue(int newValue) {
		value = Math.min(maxValue, Math.max(minValue, newValue));
		slider.handleElementSlider("targetTemperature", value);

	}

	@Override
	public void update(int mouseX, int mouseY) {
		if (isDragging) {
			setValue((int) ((maxValue - minValue) * (1 - ((mouseY - posY) / (double) sizeY))));
		}
	}

	@Override
	public double getSliderScaledPosition() {
		return 1 - ((value - minValue) / (double) maxValue);
	}

}
