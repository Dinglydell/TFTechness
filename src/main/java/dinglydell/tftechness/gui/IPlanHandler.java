package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IPlanHandler {

	//String getCraftingPlan();

	//void drawTooltip(int x, int y, String displayString);

	void setPlan(String p);

	ItemStack getResult(String plan);

	boolean openGui(EntityPlayer player);

}
