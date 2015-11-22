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

public class GuiCryoChamber extends GuiAugmentableBase implements ISlideable {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(TFTechness.MODID
			+ ":textures/gui/machine/CryoChamber.png");
	
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
		
		addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));
		
		addElement(new ElementVerticalSlider(this, 152, 8, 16, 61, this));
	}
	
	@Override
	public double getSliderScaledPosition() {
		return 1 - ((myTile.getTemperature() - sliderLowest) / (sliderHighest - sliderLowest));
	}
	
	@Override
	public void addSliderTooltip(List<String> list) {
		list.add((Math.round(myTile.getTemperature() * 100) / 100.0) + TFTechness.degrees + "C");
		
	}
}
