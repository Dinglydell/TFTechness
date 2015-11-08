package dinglydell.tftechness.metal;

import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Core.Metal.Alloy.EnumTier;

public class MaterialAlloy extends Material {
	
	public AlloyIngred[] alloy;
	
	public MaterialAlloy(String name,
			ItemStack gear,
			int tier,
			EnumTier alloyTier,
			AlloyIngred[] alloy,
			ItemStack nugget) {
		super(name, gear, tier, alloyTier, nugget);
		this.alloy = alloy;
	}
	
}
