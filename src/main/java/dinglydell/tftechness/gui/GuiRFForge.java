package dinglydell.tftechness.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.lib.gui.element.ElementFluidTank;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;

import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFForge;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.gui.element.ElementVerticalSliderSlideable;
import dinglydell.tftechness.tileentities.machine.TileRFForge;

public class GuiRFForge extends GuiAugmentableBase implements ISliderHandler {

	public static final int maxTempScale = 2000;
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFForge.png");

	protected TileRFForge myTile;

	public GuiRFForge(InventoryPlayer inv, TileRFForge te) {
		super(new ContainerRFForge(inv, te), te, inv.player, TEXTURE);

		myTile = te;

		generateInfo("tab." + TFTechness.MODID + ".machine.rfforge", 5);
	}

	@Override
	public void initGui() {
		super.initGui();

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
