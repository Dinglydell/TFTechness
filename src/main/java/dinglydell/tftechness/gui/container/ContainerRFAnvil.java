package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.thermalexpansion.gui.container.ContainerTEBase;

import com.bioxx.tfc.Containers.Slots.SlotAnvilFlux;
import com.bioxx.tfc.Containers.Slots.SlotAnvilIn;

import dinglydell.tftechness.tileentities.machine.TileRFAnvil;

public class ContainerRFAnvil extends ContainerTEBase {

	public ContainerRFAnvil(InventoryPlayer inv, TileRFAnvil te) {
		super(inv, te);
		addSlotToContainer(new SlotAnvilIn(te, 0, 34, 31));
		addSlotToContainer(new SlotAnvilIn(te, 1, 58, 31));
		addSlotToContainer(new SlotAnvilFlux(te, 2, 34, 53));
		addSlotToContainer(new SlotRemoveOnly(te, 3, 113, 31));
		addSlotToContainer(new SlotEnergy(te, te.getChargeSlot(), 8, 53));

	}

}
