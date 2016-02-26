package dinglydell.tftechness.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.gui.element.ElementFluidTank;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;

import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFForge;
import dinglydell.tftechness.gui.element.ElementSlotMiniTank;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.gui.element.ElementVerticalSliderSlideable;
import dinglydell.tftechness.tileentities.machine.TileRFForge;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class GuiRFForge extends GuiAugmentableBase implements ISliderHandler {

	public static final int maxTempScale = 2000;
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFForge.png");

	protected TileRFForge myTile;

	protected ElementSlotOverlay[] inputSlots = new ElementSlotOverlay[6];
	protected ElementSlotOverlay[] inputRowSlots = new ElementSlotOverlay[6];
	protected ElementSlotOverlay tankSlotOrange;
	protected ElementSlotOverlay tankSlotRed;
	protected ElementSlotOverlay moldSlotOrange;
	protected ElementSlotOverlay moldSlotYellow;

	public GuiRFForge(InventoryPlayer inv, TileRFForge te) {
		super(new ContainerRFForge(inv, te), te, inv.player, TEXTURE);

		myTile = te;

		generateInfo("tab." + TFTechness.MODID + ".machine.rfforge", 3);
	}

	@Override
	public void initGui() {
		super.initGui();

		for (int i = 0; i < 6; i++) {
			inputSlots[i] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
					this, 62 + i % 3 * 18, 23 + i / 3 * 23)
					.setSlotInfo(Colours.blue.gui(), 0, 2));
			inputRowSlots[i] = (ElementSlotOverlay) addElement(new ElementSlotOverlay(
					this, 62 + i % 3 * 18, 23 + i / 3 * 23)
					.setSlotInfo(i < 3 ? Colours.green.gui() : Colours.purple
							.gui(),
							0,
							2));
		}

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
		// TODO: things here
		addElement(new ElementVerticalSlider(this, 28, 8, 7, 49) {

			@Override
			public void addTooltip(List<String> list) {
				addTemperature(list, myTile.getTemperature());

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
				addTemperature(list, value);
			}
		});
		addElement(new ElementFluidTank(this, 154, 9, myTile.getTank())
				.setSize(16, 42));

		// buttonList.add(new GuiButton(0, 140, 8, 7, 49, ""));
	}

	@Override
	protected void updateElementInformation() {
		for (ElementBase el : inputSlots) {
			el.setVisible(myTile.hasSide(1));
		}
		for (int i = 0; i < inputRowSlots.length; i++) {
			ElementSlotOverlay el = inputRowSlots[i];
			el.setVisible(myTile.hasSide(5 + (i / 3)));
			if (myTile.hasSide(1)) {
				el.slotRender = 1;
			} else {
				el.slotRender = 2;
			}
		}
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

	public void addTemperature(List<String> list, float temp) {
		list.add(TFC_ItemHeat.getHeatColor(temp, Integer.MAX_VALUE));
		list.add((int) temp + TFTechness.degrees + "C");
	}

	@Override
	public void handleElementSlider(String sliderName, int sliderValue) {
		if (sliderName.equals("targetTemperature")) {
			myTile.targetTemperature = sliderValue;
		}
		myTile.sendModePacket();

	}
}
