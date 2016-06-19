package dinglydell.tftechness.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFCrucible;
import dinglydell.tftechness.gui.element.ElementFluidTankAlloy;
import dinglydell.tftechness.gui.element.ElementSlotMiniTank;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.gui.element.ElementVerticalSliderSlideable;
import dinglydell.tftechness.tileentities.machine.TileRFCrucible;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class GuiRFCrucible extends GuiAugmentableBase implements ISliderHandler {

	public static final int maxTempScale = GuiRFForge.maxTempScale;
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFCrucible.png");

	protected TileRFCrucible myTile;

	protected ElementSlotOverlay tankSlotBlue;
	protected ElementSlotOverlay tankSlotOrange;
	protected ElementSlotOverlay tankSlotRed;
	protected ElementSlotOverlay moldSlotOrange;
	protected ElementSlotOverlay moldSlotYellow;

	public GuiRFCrucible(InventoryPlayer inv, TileRFCrucible te) {
		super(new ContainerRFCrucible(inv, te), te, inv.player, TEXTURE);

		myTile = te;

		generateInfo("tab." + TFTechness.MODID + ".machine.rfcrucible", 3);
	}

	@Override
	public void initGui() {
		super.initGui();
		tankSlotBlue = (ElementSlotOverlay) addElement(new ElementSlotMiniTank(
				this, 154, 9).setSlotInfo(Colours.orange.gui(), 3, 2));
		tankSlotOrange = (ElementSlotOverlay) addElement(new ElementSlotMiniTank(
				this, 154, 9).setSlotInfo(Colours.orange.gui(), 3, 2));
		tankSlotRed = (ElementSlotOverlay) addElement(new ElementSlotMiniTank(
				this, 154, 9).setSlotInfo(Colours.red.gui(), 3, 2));

		moldSlotOrange = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 154, 53).setSlotInfo(Colours.orange.gui(), 0, 2));
		moldSlotYellow = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
				this, 154, 53).setSlotInfo(Colours.yellow.gui(), 0, 2));

		addElement(new ElementEnergyStored(this, 8, 8,
				myTile.getEnergyStorage()));
		final GuiUtils gu = new GuiUtils();

		addElement(new ElementVerticalSlider(this, 28, 8, 7, 49) {

			@Override
			public void addTooltip(List<String> list) {
				gu.addTemperature(list, myTile.getTemperature());

			}

			@Override
			public double getSliderScaledPosition() {
				return 1 - myTile.getTemperature() / maxTempScale;
			}

		});

		addElement(new ElementVerticalSliderSlideable(this, this, 140, 8, 7,
				49, 0, maxTempScale, (int) myTile.targetTemperature) {

			@Override
			public void addTooltip(List<String> list) {
				list.add(StringHelper.localize("gui." + TFTechness.MODID
						+ ".target"));
				gu.addTemperature(list, value);
			}
		});
		addElement(new ElementFluidTankAlloy(this, 154, 9,
				myTile.getAlloyTank()).setSize(16, 42));

		// buttonList.add(new GuiButton(0, 140, 8, 7, 49, ""));
	}

	@Override
	protected void updateElementInformation() {

		tankSlotOrange.setVisible(myTile.hasSide(4));
		tankSlotRed.setVisible(myTile.hasSide(2));
		moldSlotOrange.setVisible(myTile.hasSide(4));
		moldSlotYellow.setVisible(myTile.hasSide(3));

		if (myTile.hasSide(4)) {
			tankSlotRed.slotRender = 1;
			moldSlotYellow.slotRender = 1;
		} else {
			tankSlotRed.slotRender = 2;
			moldSlotYellow.slotRender = 2;
		}
	}

	@Override
	public void handleElementSlider(String sliderName, int sliderValue) {
		if (sliderName.equals("targetTemperature")) {
			myTile.targetTemperature = sliderValue;
			myTile.sendModePacket();
		}

	}

}
