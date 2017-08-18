package dinglydell.tftechness.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bioxx.tfc.Handlers.Client.GuiHandler;
import com.bioxx.tfc.TileEntities.TEAnvil;

/** Massive hack - avert ye eyes */
public class GuiHandlerTech extends GuiHandler {
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {

		if (id == 24) {
			TileEntity te;
			try {
				te = world.getTileEntity(x, y, z);
			} catch (Exception e) {
				te = null;
			}
			return new GuiPlanSelectionTech(player, (TEAnvil) te, world, x, y,
					z);
		}

		return super.getClientGuiElement(id, player, world, x, y, z);
	}
}
