package dinglydell.tftechness.gui.container;

import net.minecraft.item.ItemStack;
import cofh.lib.gui.slot.ISlotValidator;

import com.bioxx.tfc.Items.ItemMeltedMetal;
import com.bioxx.tfc.api.TFCItems;

public class SlotValidatedIngotMold implements ISlotValidator {

	@Override
	public boolean isItemValid(ItemStack is) {
		return is.getItem() instanceof ItemMeltedMetal
				&& is.getItemDamage() > 1
				|| is.getItem() == TFCItems.ceramicMold;
	}

}
