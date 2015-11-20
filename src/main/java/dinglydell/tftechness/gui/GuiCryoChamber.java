package dinglydell.tftechness.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementEnergyStored;
import cofh.thermalexpansion.gui.client.GuiAugmentableBase;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerCryoChamber;
import dinglydell.tftechness.gui.element.ElementVerticalSlider;
import dinglydell.tftechness.tileentities.machine.TileCryoChamber;

public class GuiCryoChamber extends GuiAugmentableBase {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation(TFTechness.MODID
			+ ":textures/gui/machine/CryoChamber.png");
	
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
		
		addElement(new ElementVerticalSlider(this, 152, 8, myTile));
	}
}
