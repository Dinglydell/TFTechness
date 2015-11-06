package dinglydell.tftechness.block;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

import com.bioxx.tfc.Reference;
import com.bioxx.tfc.Blocks.BlockMetalSheet;
import com.bioxx.tfc.api.Metal;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.metal.MetalSnatcher;

public class BlockTFTMetalSheet extends BlockMetalSheet {
	
	public BlockTFTMetalSheet() {
		super();
		icons = new IIcon[MetalSnatcher.getMetals().size()];
		
	}
	
	@Override
	public void registerBlockIcons(IIconRegister registerer) {
		TFTechness.logger.info(MetalSnatcher.getMetals().size());
		int i = 0;
		for (Metal m : MetalSnatcher.getMetalsAsArray()) {
			icons[i] = registerer.registerIcon(Reference.MOD_ID + ":" + "metal/" + m.name);
			i++;
		}
		
	}
}
