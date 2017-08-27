package dinglydell.tftechness.item;

import mods.railcraft.common.blocks.RailcraftBlocks;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCItems;

import erogenousbeef.bigreactors.common.BigReactors;

public class TFTMeta {

	public static ItemStack salt;
	public static ItemStack freshIce;
	public static ItemStack saltIce;
	public static ItemStack anvilRedSteel;

	public static ItemStack brTurbineHousing;
	public static ItemStack brTurbineController;
	public static ItemStack brTurbinePowerPort;
	public static ItemStack brTurbineFluidPort;
	public static ItemStack brTurbineRotorBearing;
	public static ItemStack brTurbineRotorShaft;
	public static ItemStack brTurbineRotorBlade;

	public static ItemStack brTurbineGlass;

	public static ItemStack pitchblende;

	public static ItemStack bituminousCoal;

	public static ItemStack lignite;

	public static ItemStack trackWood;
	public static ItemStack trackWoodJunction;

	public static void preInit() {

		salt = new ItemStack(TFCItems.powder, 1, 9);
		saltIce = new ItemStack(TFCBlocks.ice, 1, 0);
		freshIce = new ItemStack(TFCBlocks.ice, 1, 1);

		anvilRedSteel = new ItemStack(TFCBlocks.anvil, 1, 7);

		pitchblende = new ItemStack(TFCItems.oreChunk, 1, 26);

		bituminousCoal = new ItemStack(TFCItems.oreChunk, 1, 14);

		lignite = new ItemStack(TFCItems.oreChunk, 1, 15);

		brTurbineHousing = new ItemStack(BigReactors.blockTurbinePart);
		brTurbineController = new ItemStack(BigReactors.blockTurbinePart, 1, 1);
		brTurbinePowerPort = new ItemStack(BigReactors.blockTurbinePart, 1, 2);
		brTurbineFluidPort = new ItemStack(BigReactors.blockTurbinePart, 1, 3);
		brTurbineRotorBearing = new ItemStack(BigReactors.blockTurbinePart, 1,
				4);

		brTurbineRotorShaft = new ItemStack(BigReactors.blockTurbineRotorPart,
				1, 0);
		brTurbineRotorBlade = new ItemStack(BigReactors.blockTurbineRotorPart,
				1, 1);

		brTurbineGlass = new ItemStack(BigReactors.blockMultiblockGlass, 1, 1);

		trackWood = new ItemStack(RailcraftBlocks.getBlockTrack(), 1, 736);
		trackWoodJunction = new ItemStack(RailcraftBlocks.getBlockTrack(), 1, 0);

	}
}
