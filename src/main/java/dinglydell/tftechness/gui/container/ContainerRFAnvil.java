package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.thermalexpansion.gui.container.ContainerTEBase;

import com.bioxx.tfc.Containers.Slots.SlotAnvilIn;

import dinglydell.tftechness.tileentities.machine.TileRFAnvil;

public class ContainerRFAnvil extends ContainerTEBase {

	public ContainerRFAnvil(InventoryPlayer inv, TileRFAnvil te) {
		super(inv, te);
		addSlotToContainer(new SlotAnvilIn(te, 0, 34, 34));
		addSlotToContainer(new SlotRemoveOnly(te, 1, 113, 34));
		addSlotToContainer(new SlotEnergy(te, te.getChargeSlot(), 8, 53));

	}

}
