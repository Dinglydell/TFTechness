package dinglydell.tftechness.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.Items.Tools.ItemSteelBucket;
import com.bioxx.tfc.api.TFCItems;

import dinglydell.tftechness.TFTechness;

public class ItemTFTSteelBucket extends ItemSteelBucket {
	/** The temperature at which the fluid is considered a "hot" fluid. */
	public static final int tempThreshold = 1000;
	
	protected Fluid fluid;
	protected ItemStack ironBucket;
	protected ItemStack empty;
	protected boolean upsideDown;
	protected boolean overflowing;
	
	public ItemTFTSteelBucket(Fluid f, ItemStack filledContainer) {
		super(f.getBlock());
		if (f.getTemperature() < tempThreshold) {
			empty = new ItemStack(TFCItems.redSteelBucketEmpty);
			setUnlocalizedName("redSteelBucket.name");
		} else {
			empty = new ItemStack(TFCItems.blueSteelBucketEmpty);
			setUnlocalizedName("blueSteelBucket.name");
		}
		ironBucket = filledContainer;
		fluid = f;
	}
	
	public ItemTFTSteelBucket(Fluid f, ItemStack filledContainer, boolean upsideDown, boolean overflowing) {
		this(f, filledContainer);
		this.upsideDown = upsideDown;
		this.overflowing = overflowing;
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		if (overflowing) {
			if (fluid.getTemperature() < tempThreshold) {
				itemIcon = reg.registerIcon(TFTechness.MODID + ":redSteelBucketOverflowing");
			} else {
				itemIcon = reg.registerIcon(TFTechness.MODID + ":blueSteelBucketOverflowing");
			}
		} else if (fluid.getTemperature() < tempThreshold) {
			itemIcon = reg.registerIcon(TFTechness.MODID + ":redSteelBucket");
		} else {
			itemIcon = reg.registerIcon(TFTechness.MODID + ":blueSteelBucket");
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack it) {
		return StringHelper.localize(getUnlocalizedName()) + " (" + StringHelper.localize(fluid.getUnlocalizedName())
				+ ")";
		
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack is) {
		return empty;
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return ironBucket.getIconIndex();
	}
	
	public IIcon getOverlayIcon() {
		return itemIcon;
	}
	
	public String getFluidName() {
		return fluid.getUnlocalizedName();
	}
	
	public ItemStack getEmpty() {
		return empty;
	}
	
	public boolean isUpsideDown() {
		return upsideDown;
	}
}
