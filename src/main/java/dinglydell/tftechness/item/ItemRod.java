package dinglydell.tftechness.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc.Items.ItemIngot;

public class ItemRod extends ItemIngot {
	
	public ItemRod(String metal) {
		this.setMetal(metal, 100);
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {
		return false;
		
	}
	
}
