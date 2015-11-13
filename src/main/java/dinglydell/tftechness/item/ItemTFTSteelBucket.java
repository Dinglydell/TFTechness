package dinglydell.tftechness.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import cofh.core.render.IconRegistry;

import com.bioxx.tfc.Items.Tools.ItemSteelBucket;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.render.item.RenderBucket;

public class ItemTFTSteelBucket extends ItemSteelBucket {
	
	public ItemTFTSteelBucket(Block b) {
		super(b);
	}
	
	@Override
	public void registerIcons(IIconRegister reg) {
		IconRegistry.addIcon(RenderBucket.blueSteelIcon, TFTechness.MODID + ":items/blueSteelBucket", reg);
		IconRegistry.addIcon(RenderBucket.redSteelIcon, TFTechness.MODID + ":items/redSteelBucket", reg);
	}
	
	public Item setIcon(IIcon icon) {
		itemIcon = icon;
		return this;
	}
}
