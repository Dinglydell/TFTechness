package dinglydell.tftechness.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.bioxx.tfc.Items.ItemMetalSheet;
import com.bioxx.tfc.TileEntities.TEMetalSheet;

import dinglydell.tftechness.block.TFTBlocks;
import dinglydell.tftechness.tileentities.TETFTMetalSheet;

public class ItemTFTMetalSheet extends ItemMetalSheet {
	public String metal;
	
	public ItemTFTMetalSheet(String metal) {
		super(0);
		this.metal = metal;
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z,
			int side, float hitX, float hitY, float hitZ) {
		boolean success = super.onItemUse(itemstack, entityplayer, world, x, y, z, side, hitX, hitY, hitZ);
		if (success && isValid(world, x, y, z)) {
			int[] sides = sidesMap[side];
			world.setBlock(sides[0] + x, sides[1] + y, sides[2] + z, TFTBlocks.metalSheet);
			TETFTMetalSheet te = (TETFTMetalSheet) world.getTileEntity(sides[0] + x, sides[1] + y, sides[2] + z);
			te.metal = this.metal;
		}
		return success;
	}
	
	@Override
	public boolean isValid(World world, int i, int j, int k) {
		Block block = world.getBlock(i, j, k);
		if (block.isAir(world, i, j, k))
			return true;
		if (block == TFTBlocks.metalSheet && world.getTileEntity(i, j, k) instanceof TETFTMetalSheet) {
			TEMetalSheet te = (TEMetalSheet) world.getTileEntity(i, j, k);
			if (te.metalID == this.metalID)
				return true;
		}
		return false;
	}
	
}
