package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cofh.lib.gui.slot.ISlotValidator;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotValidated;
import cofh.thermalexpansion.gui.container.ContainerTEBase;

import com.bioxx.tfc.api.HeatRegistry;

import dinglydell.tftechness.tileentities.machine.TileRFForge;

public class ContainerRFForge extends ContainerTEBase implements ISlotValidator {

	public ContainerRFForge(InventoryPlayer inv, TileRFForge te) {
		super(inv, te);
		for (int i = 0; i < 6; i++) {
			addSlotToContainer(new SlotValidated(this, te, i, 62 + i % 3 * 18,
					23 + i / 3 * 23));
		}
		SlotValidatedIngotMold valid = new SlotValidatedIngotMold();
		addSlotToContainer(new SlotValidated(valid, te,
				TileRFForge.inputSlotEnd, 154, 53));
		addSlotToContainer(new SlotEnergy(te, te.getChargeSlot(), 8, 53));

	}

	@Override
	public boolean isItemValid(ItemStack is) {
		return HeatRegistry.getInstance().findMatchingIndex(is) != null;
	}

}
