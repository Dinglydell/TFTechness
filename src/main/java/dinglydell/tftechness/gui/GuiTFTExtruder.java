package dinglydell.tftechness.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementBase;
import cofh.thermalexpansion.gui.client.machine.GuiExtruder;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerTFTExtruder;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class GuiTFTExtruder extends GuiExtruder {
	
	static final ResourceLocation TEXTURE = new ResourceLocation(TFTechness.MODID
			+ ":textures/gui/machine/Extruder.png");
	TileTFTExtruder tile;
	
	static final int slotOutputIndex = 2;
	ElementBase slotOutput;
	ElementBase slotOutputRed;
	ElementBase slotOutput2;
	ElementBase slotOutput2Yellow;
	
	public GuiTFTExtruder(InventoryPlayer inv, TileTFTExtruder te) {
		super(inv, te);
		this.texture = TEXTURE;
		this.inventorySlots = new ContainerTFTExtruder(inv, te);
		tile = te;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		slotOutput = elements.get(slotOutputIndex);
		((ElementSlotOverlay) slotOutput.setPosition(80, 40)).setSlotInfo(Colours.orange.gui(), 0, 2);
		slotOutputRed = addElement(new ElementSlotOverlay(this, 80, 40).setSlotInfo(Colours.red.gui(), 0, 2));
		slotOutput2 = addElement(new ElementSlotOverlay(this, 80, 59).setSlotInfo(Colours.orange.gui(), 0, 2));
		slotOutput2Yellow = addElement(new ElementSlotOverlay(this, 80, 59).setSlotInfo(Colours.yellow.gui(), 0, 2));
		
	}
	
	protected void updateElementInformation() {
		super.updateElementInformation();
		this.slotOutput.setVisible(tile.hasSide(4));
		this.slotOutputRed.setVisible(tile.hasSide(2));
		this.slotOutput2.setVisible(tile.hasSide(4));
		this.slotOutput2Yellow.setVisible(tile.hasSide(3));
		
	}
	
}
