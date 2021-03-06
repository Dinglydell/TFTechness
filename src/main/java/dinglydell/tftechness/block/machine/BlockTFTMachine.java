package dinglydell.tftechness.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.tileentity.ISidedTexture;
import cofh.core.block.BlockCoFHBase;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.block.TileAugmentable;
import cofh.thermalexpansion.util.ReconfigurableHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.BlockTETFTBase;
import dinglydell.tftechness.tileentities.machine.TileCryoChamber;
import dinglydell.tftechness.tileentities.machine.TileRFAnvil;
import dinglydell.tftechness.tileentities.machine.TileRFCrucible;
import dinglydell.tftechness.tileentities.machine.TileRFForge;
import dinglydell.tftechness.tileentities.machine.TileTFTAccumulator;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;
import dinglydell.tftechness.tileentities.machine.TileTFTPrecipitator;

public class BlockTFTMachine extends BlockTETFTBase {

	public static ItemStack extruder;
	public static ItemStack accumulator;
	public static ItemStack precipitator;
	public static ItemStack cryoChamber;
	public static ItemStack rfForge;
	public static ItemStack rfCrucible;
	public static ItemStack rfAnvil;

	public enum Types {
		EXTRUDER, ACCUMULATOR, PRECIPITATOR, CRYOCHAMBER(true), RFFORGE(true), RFCRUCIBLE(
				true), RFANVIL(true);
		private boolean tftechness = false;

		private Types() {

		}

		private Types(boolean tft) {
			tftechness = tft;
		}

		/**
		 * Determines whether the machine is from TFTechness, or if it's just a
		 * modified version of a TE machine
		 */
		public boolean isTFTechness() {
			return tftechness;
		}
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
		case ACCUMULATOR:
			return new TileTFTAccumulator();
		case PRECIPITATOR:
			return new TileTFTPrecipitator();
		case CRYOCHAMBER:
			return new TileCryoChamber();
		case RFFORGE:
			return new TileRFForge();
		case RFCRUCIBLE:
			return new TileRFCrucible();
		case RFANVIL:
			return new TileRFAnvil();
		}
		return null;

	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase ent, ItemStack it) {
		if (it.stackTagCompound != null) {
			TileAugmentable te = (TileAugmentable) world.getTileEntity(x, y, z);

			te.readAugmentsFromNBT(it.stackTagCompound);
			te.installAugments();
			te.setEnergyStored(it.stackTagCompound.getInteger("Energy"));

			int i = BlockHelper.determineXZPlaceFacing(ent);
			int j = ReconfigurableHelper.getFacing(it);
			byte[] arrayOfByte = ReconfigurableHelper.getSideCache(it,
					te.getDefaultSides());

			te.sideCache[0] = arrayOfByte[0];
			te.sideCache[1] = arrayOfByte[1];
			te.sideCache[i] = 0;
			te.sideCache[BlockHelper.getLeftSide(i)] = arrayOfByte[BlockHelper
					.getLeftSide(j)];
			te.sideCache[BlockHelper.getRightSide(i)] = arrayOfByte[BlockHelper
					.getRightSide(j)];
			te.sideCache[BlockHelper.getOppositeSide(i)] = arrayOfByte[BlockHelper
					.getOppositeSide(j)];
		}

		super.onBlockPlacedBy(world, x, y, z, ent, it);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		IconRegistry.addIcon("TFTMachineBottom",
				"thermalexpansion:machine/Machine_Bottom",
				reg);
		IconRegistry.addIcon("TFTMachineTop",
				"thermalexpansion:machine/Machine_Top",
				reg);
		IconRegistry.addIcon("TFTMachineSide",
				"thermalexpansion:machine/Machine_Side",
				reg);
		for (int i = 0; i < Types.values().length; i++) {
			String titleName = StringHelper.titleCase(Types.values()[i].name()
					.toLowerCase());
			String modid = "thermalexpansion";
			if (Types.values()[i].isTFTechness()) {
				modid = TFTechness.MODID;
			}
			IconRegistry.addIcon("TFTMachineFace" + i, modid
					+ ":machine/Machine_Face_" + titleName, reg);
			IconRegistry.addIcon("TFTMachineActive" + i, modid
					+ ":machine/Machine_Active_" + titleName, reg);
		}
		IconRegistry.addIcon("Config_0",
				"thermalexpansion:config/Config_None",
				reg);
		IconRegistry.addIcon("Config_1",
				"thermalexpansion:config/Config_Blue",
				reg);
		IconRegistry.addIcon("Config_2",
				"thermalexpansion:config/Config_Red",
				reg);
		IconRegistry.addIcon("Config_3",
				"thermalexpansion:config/Config_Yellow",
				reg);
		IconRegistry.addIcon("Config_4",
				"thermalexpansion:config/Config_Orange",
				reg);
		IconRegistry.addIcon("Config_5",
				"thermalexpansion:config/Config_Green",
				reg);
		IconRegistry.addIcon("Config_6",
				"thermalexpansion:config/Config_Purple",
				reg);
		IconRegistry.addIcon("Config_7",
				"thermalexpansion:config/Config_Open",
				reg);

		IconRegistry.addIcon("Config_CB_0",
				"thermalexpansion:config/Config_None",
				reg);
		IconRegistry.addIcon("Config_CB_1",
				"thermalexpansion:config/Config_Blue_CB",
				reg);
		IconRegistry.addIcon("Config_CB_2",
				"thermalexpansion:config/Config_Red_CB",
				reg);
		IconRegistry.addIcon("Config_CB_3",
				"thermalexpansion:config/Config_Yellow_CB",
				reg);
		IconRegistry.addIcon("Config_CB_4",
				"thermalexpansion:config/Config_Orange_CB",
				reg);
		IconRegistry.addIcon("Config_CB_5",
				"thermalexpansion:config/Config_Green_CB",
				reg);
		IconRegistry.addIcon("Config_CB_6",
				"thermalexpansion:config/Config_Purple_CB",
				reg);
		IconRegistry.addIcon("Config_CB_7",
				"thermalexpansion:config/Config_Open",
				reg);

	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (side == 0) {
			return IconRegistry.getIcon("TFTMachineBottom");
		}
		if (side == 1) {
			return IconRegistry.getIcon("TFTMachineTop");
		}
		return side != 3 ? IconRegistry.getIcon("TFTMachineSide")
				: IconRegistry.getIcon("TFTMachineFace" + meta);

	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		ISidedTexture sidedTexture = (ISidedTexture) access.getTileEntity(x,
				y,
				z);
		return sidedTexture == null ? null : sidedTexture.getTexture(side,
				BlockCoFHBase.renderPass);
	}

	@Override
	public boolean isNormalCube(IBlockAccess paramIBlockAccess, int paramInt1,
			int paramInt2, int paramInt3) {
		return false;
	}

	@Override
	public boolean isSideSolid(IBlockAccess paramIBlockAccess, int paramInt1,
			int paramInt2, int paramInt3, ForgeDirection paramForgeDirection) {

		return true;
	}

	public boolean canRenderInPass(int pass) {
		BlockCoFHBase.renderPass = pass;
		return pass < 2;
	}

	public boolean isOpaqueCube() {
		return true;
	}

	@Override
	protected ItemStack setDefaultTag(ItemStack itemStack) {
		return itemStack;
	}

	@Override
	protected int getNumBlocks() {
		return Types.values().length;
	}

}
