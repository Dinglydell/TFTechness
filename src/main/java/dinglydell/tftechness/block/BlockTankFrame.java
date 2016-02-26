package dinglydell.tftechness.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.ThermalExpansion;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.block.tank.BlockTank.Types;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dinglydell.tftechness.TFTechness;

public class BlockTankFrame extends Block {
	public Map<String, IIcon> icons = new HashMap();

	public static enum Stages {
		unfinished, frame
	};

	public static String[] stageNames = new String[] { "unfinished", "frame" };
	/**
	 * String key = type + stage
	 * 
	 * 
	 * eg. Unfinished Hardened Tank's key = hardened + unfinished =
	 * hardenedunfinished
	 */
	public static Map<String, ItemStack> itemStacks = new HashMap();

	public BlockTankFrame() {
		super(Material.iron);
		setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
		setHardness(4.0F);
		setResistance(60.0F);
		setStepSound(soundTypeMetal);
		setCreativeTab(ThermalExpansion.tabBlocks);
		setBlockTextureName(TFTechness.MODID + ":Tank");

	}

	@Override
	public boolean isSideSolid(IBlockAccess paramIBlockAccess, int paramInt1,
			int paramInt2, int paramInt3, ForgeDirection paramForgeDirection) {
		if ((paramForgeDirection == ForgeDirection.UP)
				|| (paramForgeDirection == ForgeDirection.DOWN)) {
			return true;
		}
		return false;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x,
			int y, int z, double paramDouble1, double paramDouble2,
			double paramDouble3) {
		int meta = world.getBlockMetadata(x, y, z);
		return BlockTank.RESISTANCE[meta] * getStatMultiplier(meta);

	}

	public static int getStatMultiplier(int meta) {
		int stage = getStageIndex(meta);
		int nStages = Stages.values().length;
		return stage / (nStages + 1);
	}

	@Override
	public float getBlockHardness(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		int index = getTypeIndex(meta);
		return BlockTank.HARDNESS[index] * getStatMultiplier(meta);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < BlockTank.Types.values().length; i++) {
			int stLength = Stages.values().length;
			for (int j = 0; j < stLength; j++) {
				int meta = i * stLength + j;
				String text = textureName + "_"
						+ StringHelper.titleCase(BlockTank.NAMES[i]) + "_"
						+ StringHelper.titleCase(stageNames[j]);
				String top = text + "_TopBottom";
				icons.put("TopBottom" + meta, reg.registerIcon(top));

				String side = text + "_Side";
				icons.put("Side" + meta, reg.registerIcon(side));
			}
		}
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int damageDropped(int metadata) {
		return metadata;
	}

	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		int nBlocks = maxMetadata();
		for (int i = 0; i < nBlocks; i++) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side < 2) {
			return icons.get("TopBottom" + meta);
		} else {
			return icons.get("Side" + meta);
		}
	}

	public static int maxMetadata() {
		return BlockTank.Types.values().length * Stages.values().length;
	}

	public static int getMeta(BlockTank.Types t, Stages s) {
		return t.ordinal() * Stages.values().length + s.ordinal();
	}

	public static int getTypeIndex(int meta) {
		return meta / Stages.values().length;
	}

	public static int getStageIndex(int meta) {
		int nStages = Stages.values().length;
		return meta % nStages;
	}

	public static BlockTank.Types getType(int meta) {
		return BlockTank.Types.values()[meta / Stages.values().length];
	}

	public static Stages getStage(int meta) {
		Stages[] vals = Stages.values();
		int nStages = vals.length;
		return vals[meta % nStages];
	}

	public static ItemStack getItemStack(Types t, Stages s) {
		return itemStacks.get(t.name() + s.name());
	}

}
