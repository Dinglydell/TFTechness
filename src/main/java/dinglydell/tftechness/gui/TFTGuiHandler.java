package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class TFTGuiHandler implements IGuiHandler {

	enum TFTGui {
		AnvilPlanSelection
	}

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (TFTGui.values()[ID]) {
		case AnvilPlanSelection:
			return new ContainerRFAnvilPlanSelection(
					(IPlanHandler) world.getTileEntity(x, y, z));

		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (TFTGui.values()[ID]) {
		case AnvilPlanSelection:
			return new GuiRFAnvilPlanSelection(
					(IPlanHandler) world.getTileEntity(x, y, z), player);

		}
		return null;
	}

}
