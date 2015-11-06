package dinglydell.tftechness.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.block.tank.BlockTank.Types;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFC_ItemHeat;
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
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
		super.addInformation(is, player, arraylist, flag);
		// From ItemTerra. ItemBlockTerra does not have the danger, weldable and workable flags
		if (TFC_ItemHeat.hasTemp(is)) {
			String s = "";
			if (HeatRegistry.getInstance().isTemperatureDanger(is)) {
				s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.danger") + " | ";
			}
			
			if (HeatRegistry.getInstance().isTemperatureWeldable(is)) {
				s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.weldable") + " | ";
			}
			
			if (HeatRegistry.getInstance().isTemperatureWorkable(is)) {
				s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.workable");
			}
			
			if (!"".equals(s))
				arraylist.add(s);
		}
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
