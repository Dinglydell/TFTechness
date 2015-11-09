package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import cofh.lib.gui.slot.SlotCustomInventory;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.thermalexpansion.gui.container.ContainerTEBase;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;

public class ContainerTFTExtruder extends ContainerTEBase {
	
	public ContainerTFTExtruder(InventoryPlayer inv, TileTFTExtruder te) {
		super(inv, te);
		addSlotToContainer(new SlotRemoveOnly(te, 0, 80, 40));
		addSlotToContainer(new SlotRemoveOnly(te, 1, 80, 59));
		
		addSlotToContainer(new SlotCustomInventory(te, 0, null, 0, 50, 19, false));
		addSlotToContainer(new SlotCustomInventory(te, 0, null, 1, 80, 19, false));
		addSlotToContainer(new SlotCustomInventory(te, 0, null, 2, 110, 19, false));
		
	}
	
}
