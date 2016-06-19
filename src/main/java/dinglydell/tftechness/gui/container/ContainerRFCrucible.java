package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.thermalexpansion.gui.container.ContainerTEBase;
import dinglydell.tftechness.gui.container.slots.SlotLimitedValidated;
import dinglydell.tftechness.tileentities.machine.TileRFCrucible;

public class ContainerRFCrucible extends ContainerTEBase {

	public ContainerRFCrucible(InventoryPlayer inv, TileRFCrucible te) {
		super(inv, te);

		SlotValidatedIngotMold valid = new SlotValidatedIngotMold();
		addSlotToContainer(new SlotLimitedValidated(valid, te, 0, 154, 53, 1));
		addSlotToContainer(new SlotEnergy(te, te.getChargeSlot(), 8, 53));

	}

}
