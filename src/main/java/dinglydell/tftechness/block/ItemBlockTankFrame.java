package dinglydell.tftechness.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.block.tank.BlockTank.Types;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Interfaces.ISmeltable;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.metal.TFTMetals;

public class ItemBlockTankFrame extends ItemTerraBlock implements ISmeltable {
	
	public static Map<Types, Metal> metalMap = new HashMap();
	static {
		metalMap.put(Types.CREATIVE, null);
		metalMap.put(Types.BASIC, Global.COPPER);
		metalMap.put(Types.HARDENED, TFTMetals.metals.get("Invar"));
		metalMap.put(Types.REINFORCED, TFTMetals.metals.get("Invar"));
		metalMap.put(Types.RESONANT, TFTMetals.metals.get("Enderium"));
	}
	
	public ItemBlockTankFrame(Block b) {
		super(b);
		// setMaxDamage(BlockTankFrame.maxMetadata());
		setMaxStackSize(1);
		setNoRepair();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		int dmg = itemstack.getItemDamage();
		int typeIndex = BlockTankFrame.getTypeIndex(dmg);
		int stageIndex = BlockTankFrame.getStageIndex(dmg);
		String type = BlockTank.NAMES[typeIndex];
		String stage = BlockTankFrame.stageNames[stageIndex];
		return "tile." + TFTechness.MODID + ".tank." + type + "." + stage;
	}
	
	@Override
	public boolean canStack() {
		return false;
	}
	
	@Override
	public short getMetalReturnAmount(ItemStack it) {
		return 0;
	}
	
	@Override
	public Metal getMetalType(ItemStack it) {
		return metalMap.get(BlockTankFrame.getType(it.getItemDamage()));
	}
	
	@Override
	public EnumTier getSmeltTier(ItemStack it) {
		return EnumTier.TierI;
	}
	
	@Override
	public boolean isSmeltable(ItemStack arg0) {
		return true;
	}
	
}
