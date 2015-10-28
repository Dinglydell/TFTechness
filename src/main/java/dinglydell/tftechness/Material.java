package dinglydell.tftechness;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.api.HeatRaw;

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

	public Material(Item unshaped, Item ingot, Item ingot2x, Item sheet, Item sheet2x, ItemStack gear, int tier,
			HeatRaw heatRaw) {
		this.unshaped = unshaped;
		this.ingot = ingot;
		this.ingot2x = ingot2x;
		this.sheet = sheet;
		this.sheet2x = sheet2x;
		this.gear = gear;
		this.tier = tier;
		this.heatRaw = heatRaw;
		this.gearOnly = false;
	}

	public Material(Item unshaped, Item sheet2x, ItemStack gear, int tier, HeatRaw heatRaw) {
		this.gearOnly = true;
		this.unshaped = unshaped;
		this.sheet2x = sheet2x;
		this.gear = gear;
		this.heatRaw = heatRaw;
	}

	public Item getUnshaped() {

	}

}
