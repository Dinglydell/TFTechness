package dinglydell.tftechness.block.machine;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.thermalexpansion.block.BlockTEBase;
import cofh.thermalexpansion.block.machine.TileMachineBase;
import cofh.thermalexpansion.util.ReconfigurableHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockTFTMachine extends BlockTEBase {
	
	public static ItemStack extruder;
	
	public enum Types {
		EXTRUDER
	}
	
	public BlockTFTMachine() {
		super(Material.iron);
		this.setHardness(15.0f);
		this.setResistance(25.0f);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		if (meta >= Types.values().length) {
			return null;
		}
		switch (Types.values()[meta]) {
			case EXTRUDER:
				return new TileTFTExtruder();
		}
		return null;
		
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		int nBlocks = Types.values().length;
		for (int i = 0; i < nBlocks; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public NBTTagCompound getItemStackTag(World world, int x, int y, int z) {
		NBTTagCompound nbt = super.getItemStackTag(world, x, y, z);
		TileMachineBase tile = (TileMachineBase) world.getTileEntity(x, y, z);
		if (tile != null) {
			if (nbt == null) {
				nbt = new NBTTagCompound();
			}
			ReconfigurableHelper.setItemStackTagReconfig(nbt, tile);
			nbt.setInteger("Energy", tile.getEnergyStored(ForgeDirection.UNKNOWN));
			tile.writeAugmentsToNBT(nbt);
		}
		return nbt;
	}
	
	@Override
	public boolean initialize() {
		return false;
	}
	
	@Override
	public boolean postInit() {
		return false;
	}
}
