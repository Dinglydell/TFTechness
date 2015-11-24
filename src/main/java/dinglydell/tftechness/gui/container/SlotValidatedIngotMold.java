package dinglydell.tftechness.gui.container;

import com.bioxx.tfc.api.TFCItems;

import net.minecraft.item.ItemStack;
import cofh.lib.gui.slot.ISlotValidator;

public class SlotValidatedIngotMold implements ISlotValidator {

	@Override
	public boolean isItemValid(ItemStack is) {
		return is.getItem() == TFCItems.ceramicMold;
	}

}
