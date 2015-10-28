package dinglydell.tftechness;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.Metal;

public class Material {
	
	public Item ingot;
	public Item ingot2x;
	public Item sheet;
	public Item sheet2x;
	public Item unshaped;
	public ItemStack gear;
	public HeatRaw heatRaw;
	public int tier;
	public boolean gearOnly;
	public String name;
	public Metal metal;
	
	public Material(String name, ItemStack gear, int tier) {
		this.heatRaw = TFTechness.heatMap.get(name);
		this.gearOnly = false;
		this.gear = gear;
		this.name = name;
	}
	
	public Material(String name, Item unshaped, Item sheet2x, ItemStack gear, int tier, Metal metal) {
		this.gearOnly = true;
		this.unshaped = unshaped;
		this.tier = tier;
		this.sheet2x = sheet2x;
		this.gear = gear;
		this.heatRaw = TFTechness.heatMap.get(name);
		this.name = name;
		this.metal = metal;
	}
	
}
