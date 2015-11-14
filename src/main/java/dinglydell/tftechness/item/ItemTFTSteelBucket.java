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
	protected ItemStack ironBucket;
	protected ItemStack empty;
	protected boolean upsideDown;
	
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
		fluidName = f.getUnlocalizedName();
	}
	
	public ItemTFTSteelBucket(Fluid f, ItemStack filledContainer, boolean upsideDown) {
		this(f, filledContainer);
		this.upsideDown = upsideDown;
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		IconRegistry.addIcon(RenderBucket.blueSteelIcon, TFTechness.MODID + ":blueSteelBucket", reg);
		IconRegistry.addIcon(RenderBucket.redSteelIcon, TFTechness.MODID + ":redSteelBucket", reg);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack it) {
		return StringHelper.localize(getUnlocalizedName()) + " (" + StringHelper.localize(fluidName) + ")";
		
	}
	
	@Override
	public ItemStack getContainerItem(ItemStack is) {
		return empty;
	}
	
	@Override
	public IIcon getIconFromDamage(int meta) {
		return ironBucket.getIconIndex();
	}
	
	public String getFluidName() {
		return fluidName;
	}
	
	public ItemStack getEmpty() {
		return empty;
	}
	
	public boolean isUpsideDown() {
		return upsideDown;
	}
}
