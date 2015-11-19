package dinglydell.tftechness.metal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.Metal;

import dinglydell.tftechness.TFTechness;

public class Material {
	
	public Item ingot;
	public Item ingot2x;
	public Item sheet;
	public Item sheet2x;
	public Item unshaped;
	public ItemStack gear;
	public Item rod;
	public ItemStack nugget;
	
	public HeatRaw heatRaw;
	public int tier;
	public boolean gearOnly;
	public String name;
	public Metal metal;
	public Alloy.EnumTier alloyTier;
	public String oreName;
	
	private Material(String name, ItemStack gear, int tier, ItemStack nugget, boolean gearOnly) {
		this.gearOnly = gearOnly;
		this.heatRaw = TFTechness.statMap.get(name).heat;
		this.gear = gear;
		this.name = name;
		this.oreName = name;
		this.tier = tier;
		this.nugget = nugget;
	}
	
	public Material(String name, ItemStack gear, int tier, Alloy.EnumTier alloyTier, ItemStack nugget) {
		this(name, gear, tier, nugget, false);
		this.alloyTier = alloyTier;
	}
	
	public Material(String name,
			Item unshaped,
			Item ingot,
			Item sheet2x,
			ItemStack gear,
			int tier,
			Metal metal,
			ItemStack nugget) {
		this(name, gear, tier, nugget, true);
		this.unshaped = unshaped;
		this.ingot = ingot;
		this.sheet2x = sheet2x;
		this.metal = metal;
	}
	
	public Material(String name,
			String oreName,
			Item unshaped,
			Item ingot,
			Item sheet2x,
			ItemStack gear,
			int tier,
			Metal metal,
			ItemStack nugget) {
		this(name, unshaped, ingot, sheet2x, gear, tier, metal, nugget);
		this.oreName = oreName;
	}
}
