package dinglydell.tftechness.metal;

import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.block.tank.BlockTank;

import com.bioxx.tfc.api.Crafting.AnvilReq;

import dinglydell.tftechness.TFTechness;

public class TankMap {
	
	private TankMap(BlockTank.Types type, ItemStack finished) {
		this.type = type;
		this.finished = finished;
	}
	
	public TankMap(String name, BlockTank.Types type, ItemStack finished) {
		this(type, finished);
		Material m = TFTechness.materialMap.get(name);
		this.sheet2x = new ItemStack(m.sheet2x, 1);
		this.req = AnvilReq.getReqFromInt(m.tier);
		
	}
	
	public TankMap(ItemStack sheet2x, int tier, BlockTank.Types type, ItemStack finished) {
		this(type, finished);
		this.sheet2x = sheet2x;
		this.req = AnvilReq.getReqFromInt(tier);
	}
	
	public ItemStack sheet2x;
	public AnvilReq req;
	public BlockTank.Types type;
	public ItemStack finished;
}
