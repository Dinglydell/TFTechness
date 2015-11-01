package dinglydell.tftechness.metal;

import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Core.Metal.Alloy.EnumTier;

public class MaterialAlloy extends Material {
	
	public AlloyIngred[] alloy;
	
	public MaterialAlloy(String name, ItemStack gear, int tier, EnumTier alloyTier, AlloyIngred[] alloy) {
		super(name, gear, tier, alloyTier);
		this.alloy = alloy;
	}
	
}
