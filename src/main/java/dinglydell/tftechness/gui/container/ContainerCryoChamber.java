package dinglydell.tftechness.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import cofh.lib.gui.slot.ISlotValidator;
import cofh.lib.gui.slot.SlotEnergy;
import cofh.lib.gui.slot.SlotValidated;
import cofh.thermalexpansion.gui.container.ContainerTEBase;

import com.bioxx.tfc.api.Interfaces.IFood;

import dinglydell.tftechness.tileentities.machine.TileCryoChamber;

public class ContainerCryoChamber extends ContainerTEBase implements ISlotValidator {
	public ContainerCryoChamber(InventoryPlayer inv, TileCryoChamber te) {
		super(inv, te);
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new SlotValidated(this, te, i, 62 + i % 3 * 18, 17 + i / 3 * 18));
		}
		addSlotToContainer(new SlotEnergy(te, te.getChargeSlot(), 8, 53));
		
	}
	
	@Override
	public boolean isItemValid(ItemStack is) {
		return is.getItem() instanceof IFood;
	}
}
