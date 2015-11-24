package dinglydell.tftechness.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;

import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerRFForge;
import dinglydell.tftechness.gui.element.ElementFluidTankAlloy;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.tileentities.machine.TileRFForge;

public class GuiRFForge extends GuiAugmentableBase implements ISlideable {
	
	public static final int maxTempScale = 2000;
	public static final ResourceLocation TEXTURE = new ResourceLocation(TFTechness.MODID
			+ ":textures/gui/machine/RFForge.png");
	
	protected TileRFForge myTile;
	
	public GuiRFForge(InventoryPlayer inv, TileRFForge te) {
		super(new ContainerRFForge(inv, te), te, inv.player, TEXTURE);
		
		myTile = te;
		
		generateInfo("tab." + TFTechness.MODID + ".machine.rfforge", 5);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		
		addElement(new ElementEnergyStored(this, 8, 8, myTile.getEnergyStorage()));
		// TODO: things here
		addElement(new ElementVerticalSlider(this, 28, 8, 7, 49, this));
		addElement(new ElementFluidTankAlloy(this, 131, 9, myTile.getTankA()));
		addElement(new ElementFluidTankAlloy(this, 154, 9, myTile.getTankB()));
	}
	
	@Override
	public double getSliderScaledPosition() {
		return 1 - myTile.getTemperature() / maxTempScale;
	}
	
	@Override
	public void addSliderTooltip(List<String> list) {
		list.add(TFC_ItemHeat.getHeatColor(myTile.getTemperature(), Integer.MAX_VALUE));
		list.add((int) myTile.getTemperature() + TFTechness.degrees + "C");
	}
}
