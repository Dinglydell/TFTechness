package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerRFAnvilPlanSelection extends Container {

	public ContainerRFAnvilPlanSelection(IPlanHandler planHandler) {
		super();
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {

		return false;
	}

}
