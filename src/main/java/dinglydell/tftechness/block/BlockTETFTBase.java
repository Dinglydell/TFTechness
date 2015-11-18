package dinglydell.tftechness.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermalexpansion.block.BlockTEBase;
import cofh.thermalexpansion.block.TileReconfigurable;
import cofh.thermalexpansion.util.ReconfigurableHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dinglydell.tftechness.tileentities.IAugmentNBT;

public abstract class BlockTETFTBase extends BlockTEBase {
	
	public BlockTETFTBase(Material mat) {
		super(mat);
	}
	
	@Override
	public void getSubBlocks(Item it, CreativeTabs creat, List list) {
		int nBlocks = getNumBlocks();
		for (int i = 0; i < nBlocks; i++) {
			list.add(setDefaultTag(new ItemStack(it, 1, i)));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		int nBlocks = getNumBlocks();
		for (int i = 0; i < nBlocks; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
		
	}
	
	@Override
	public boolean initialize() {
		return false;
	}
	
	@Override
	public boolean postInit() {
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p, int meta, float sideX,
			float sideY, float sideZ) {
		TileEntity te = world.getTileEntity(x, y, z);
		if (te instanceof IFluidHandler && FluidHelper.fillHandlerWithContainer(world, (IFluidHandler) te, p)) {
			return true;
		}
		return super.onBlockActivated(world, x, y, z, p, meta, sideX, sideY, sideZ);
		
	}
	
	@Override
	public NBTTagCompound getItemStackTag(World world, int x, int y, int z) {
		NBTTagCompound nbt = super.getItemStackTag(world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null) {
			if (nbt == null) {
				nbt = new NBTTagCompound();
			}
			if (tile instanceof TileReconfigurable) {
				TileReconfigurable tileRec = (TileReconfigurable) tile;
				ReconfigurableHelper.setItemStackTagReconfig(nbt, tileRec);
			}
			nbt.setInteger("Energy", ((IEnergyProvider) tile).getEnergyStored(ForgeDirection.UNKNOWN));
			((IAugmentNBT) tile).writeAugmentsToNBT(nbt);
		}
		return nbt;
	}
	
	protected abstract ItemStack setDefaultTag(ItemStack itemStack);
	
	protected abstract int getNumBlocks();
}
