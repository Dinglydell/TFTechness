package dinglydell.tftechness.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.Items.Tools.ItemSteelBucket;
import com.bioxx.tfc.api.TFCItems;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.render.item.RenderBucket;

public class ItemTFTSteelBucket extends ItemSteelBucket {
	/** The temperature at which the fluid is considered a "hot" fluid. */
	public static final int tempThreshold = 1000;
	
	protected String fluidName;
	protected ItemStack empty;
	
	public ItemTFTSteelBucket(Fluid f) {
		super(f.getBlock());
		if (f.getTemperature() < tempThreshold) {
			empty = new ItemStack(TFCItems.redSteelBucketEmpty);
			setUnlocalizedName("redSteelBucket.name");
		} else {
			empty = new ItemStack(TFCItems.blueSteelBucketEmpty);
			setUnlocalizedName("blueSteelBucket.name");
		}
		fluidName = f.getUnlocalizedName();
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		IconRegistry.addIcon(RenderBucket.blueSteelIcon, TFTechness.MODID + ":items/blueSteelBucket", reg);
		IconRegistry.addIcon(RenderBucket.redSteelIcon, TFTechness.MODID + ":items/redSteelBucket", reg);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack it) {
		return StringHelper.localize(getUnlocalizedName()) + " (" + StringHelper.localize(fluidName) + ")";
		
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack is) {
		return empty;
	}
	
	public ItemTFTSteelBucket setIcon(IIcon icon) {
		itemIcon = icon;
		return this;
	}
	
	public String getFluidName() {
		return fluidName;
	}
	
	public ItemStack getEmpty() {
		return empty;
	}
	
}
