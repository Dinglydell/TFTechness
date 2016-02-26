package dinglydell.tftechness.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerCryoChamber;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.tileentities.machine.TileCryoChamber;

public class GuiCryoChamber extends GuiAugmentableBase {

	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/CryoChamber.png");

	/** The lowest temperature the slider should display */
	protected static final float sliderLowest = -10;
	/** The highest temperature the slider should display */
	protected static final float sliderHighest = 40;

	TileCryoChamber myTile;

	public GuiCryoChamber(InventoryPlayer inv, TileCryoChamber te) {
		super(new ContainerCryoChamber(inv, te), te, inv.player, TEXTURE);

		myTile = te;

		generateInfo("tab." + TFTechness.MODID + ".machine.cryochamber", 5);
	}

	@Override
	public void initGui() {
		super.initGui();

		addElement(new ElementEnergyStored(this, 8, 8,
				myTile.getEnergyStorage()));

		addElement(new ElementVerticalSlider(this, 160, 8, 7, 61) {

			@Override
			public void addTooltip(List<String> list) {
				list.add((Math.round(myTile.getTemperature() * 100) / 100.0)
						+ TFTechness.degrees + "C");

			}

			@Override
			public double getSliderScaledPosition() {
				return 1 - ((myTile.getTemperature() - sliderLowest) / (sliderHighest - sliderLowest));
			}

		});
	}

}
