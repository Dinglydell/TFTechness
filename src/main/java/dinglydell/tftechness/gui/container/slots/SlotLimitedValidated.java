package dinglydell.tftechness.gui.container.slots;

import net.minecraft.inventory.IInventory;
import cofh.lib.gui.slot.ISlotValidator;
import cofh.lib.gui.slot.SlotValidated;

public class SlotLimitedValidated extends SlotValidated {

	protected int stackLimit;

	public SlotLimitedValidated(ISlotValidator validator, IInventory inventory,
			int index, int x, int y, int stackLimit) {
		super(validator, inventory, index, x, y);
		this.stackLimit = stackLimit;
	}

	@Override
	public int getSlotStackLimit() {
		return stackLimit;
	}
}
