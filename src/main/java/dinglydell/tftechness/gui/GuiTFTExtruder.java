package dinglydell.tftechness.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.element.ElementBase;
import cofh.thermalexpansion.gui.client.machine.GuiExtruder;
import cofh.thermalexpansion.gui.element.ElementSlotOverlay;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.container.ContainerTFTExtruder;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;

public class GuiTFTExtruder extends GuiExtruder {
	
	static final ResourceLocation TEXTURE = new ResourceLocation(TFTechness.MODID
			+ ":textures/gui/machine/Extruder.png");
	TileTFTExtruder tile;
	
	static final int slotOutputIndex = 2;
	ElementBase slotOutput;
	ElementBase slotOutput2;
	
	public GuiTFTExtruder(InventoryPlayer inv, TileTFTExtruder te) {
		super(inv, te);
		this.texture = TEXTURE;
		this.inventorySlots = new ContainerTFTExtruder(inv, te);
		tile = te;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		Slot s = (Slot) this.inventorySlots.inventorySlots.get(2);
		slotOutput = elements.get(slotOutputIndex);
		((ElementSlotOverlay) slotOutput.setPosition(80, 40)).setSlotInfo(3, 0, 2);
		this.slotOutput2 = addElement(new ElementSlotOverlay(this, 80, 59).setSlotInfo(3, 0, 2));
		
	}
	
	protected void updateElementInformation() {
		super.updateElementInformation();
		this.slotOutput2.setVisible(tile.hasSide(2));
		
	}
	
}
