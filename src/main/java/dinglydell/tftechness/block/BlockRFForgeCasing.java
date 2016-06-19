package dinglydell.tftechness.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import dinglydell.tftechness.tileentities.machine.TileRFForgeCasing;

public class BlockRFForgeCasing extends Block implements ITileEntityProvider {

	public BlockRFForgeCasing() {
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileRFForgeCasing();
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {
		super.breakBlock(world, x, y, z, block, meta);
		//		TileRFForgeCasing trfc = (TileRFForgeCasing) world.getTileEntity(x,
		//				y,
		//				z);
		//		if (trfc.hasMaster()) {
		//			TileEntity te = world.getTileEntity(trfc.getMasterX(),
		//					trfc.getMasterY(),
		//					trfc.getMasterZ());
		//			if (te instanceof TileRFForge) {
		//				TileRFForge trf = (TileRFForge) te;
		//				trf.destroyMultiblock();
		//			}
		//		}
		world.removeTileEntity(x, y, z);
	}

	@Override
	public boolean onBlockEventReceived(World worldIn, int x, int y, int z,
			int eventID, int eventParam) {
		super.onBlockEventReceived(worldIn, x, y, z, eventID, eventParam);
		TileEntity tileentity = worldIn.getTileEntity(x, y, z);
		return tileentity == null ? false : tileentity
				.receiveClientEvent(eventID, eventParam);
	}

}
