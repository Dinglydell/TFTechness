package dinglydell.tftechness.metal;

import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.tank.BlockTank;

import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.Crafting.AnvilReq;

import dinglydell.tftechness.TFTechness;

public class TankMap {
	
	private TankMap(BlockTank.Types type) {
		this.type = type;
		this.finished = new ItemStack(TEBlocks.blockTank, 1, type.ordinal());
	}
	
	public TankMap(String name, BlockTank.Types type) {
		this(type);
		Material m = TFTechness.materialMap.get(name);
		this.sheet2x = new ItemStack(m.sheet2x, 1);
		this.req = AnvilReq.getReqFromInt(m.tier);
		this.heatRaw = m.heatRaw;
		
	}
	
	public TankMap(ItemStack sheet2x, int tier, BlockTank.Types type, HeatRaw raw) {
		this(type);
		this.sheet2x = sheet2x;
		this.req = AnvilReq.getReqFromInt(tier);
		this.heatRaw = raw;
	}
	
	public ItemStack sheet2x;
	public AnvilReq req;
	public BlockTank.Types type;
	public ItemStack finished;
	public HeatRaw heatRaw;
}
