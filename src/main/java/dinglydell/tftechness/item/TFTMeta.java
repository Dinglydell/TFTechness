package dinglydell.tftechness.item;

import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCItems;

import erogenousbeef.bigreactors.common.BigReactors;

public class TFTMeta {

	public static ItemStack salt;
	public static ItemStack freshIce;
	public static ItemStack saltIce;

	public static ItemStack brTurbineController;

	public static void preInit() {

		salt = new ItemStack(TFCItems.powder, 1, 9);
		saltIce = new ItemStack(TFCBlocks.ice, 1, 0);
		freshIce = new ItemStack(TFCBlocks.ice, 1, 1);

		brTurbineController = new ItemStack(BigReactors.blockTurbinePart, 1, 1);
	}
}
