package dinglydell.tftechness.item;

import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCItems;

public class TFCMeta {
	
	public static ItemStack salt;
	public static ItemStack freshIce;
	public static ItemStack saltIce;
	
	public static void preInit() {
		
		salt = new ItemStack(TFCItems.powder, 1, 9);
		saltIce = new ItemStack(TFCBlocks.ice, 1, 0);
		freshIce = new ItemStack(TFCBlocks.ice, 1, 1);
		
	}
}
