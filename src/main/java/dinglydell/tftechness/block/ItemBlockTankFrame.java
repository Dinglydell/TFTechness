package dinglydell.tftechness.block;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.block.tank.BlockTank.Types;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
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
		HeatIndex i = HeatRegistry.getInstance().findMatchingIndex(itemstack);
		TFTechness.logger.info(i.hasOutput());
		int dmg = itemstack.getItemDamage();
		int typeIndex = BlockTankFrame.getTypeIndex(dmg);
		int stageIndex = BlockTankFrame.getStageIndex(dmg);
		String type = BlockTank.NAMES[typeIndex];
		String stage = BlockTankFrame.stageNames[stageIndex];
		return "tile." + TFTechness.MODID + ".tank." + type + "." + stage;
	}
	
	// From ItemTerra, cannot extend both ItemBlock and ItemTerra - ItemBlockTerra does not support
	// the weldable flag
	// @Override
	// public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
	// TFTechness.logger.info("hi");
	// ItemTerra.addSizeInformation(is, arraylist);
	// HeatIndex ind = HeatRegistry.getInstance().findMatchingIndex(is);
	// TFTechness.logger.info("{ melt: " + ind.meltTemp + ", sh: " + ind.specificHeat + "}");
	// ind.
	// ItemTerra.addHeatInformation(is, arraylist);
	// TFTechness.logger.info(is.hasTagCompound());
	// if (is.hasTagCompound()) {
	// if (is.getTagCompound().hasKey("itemCraftingValue")
	// && is.getTagCompound().getShort("itemCraftingValue") != 0)
	// arraylist.add("This Item Has Been Worked");
	// }
	//
	// addItemInformation(is, player, arraylist);
	// }
	//
	// public void addItemInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
	//
	// if (TFC_ItemHeat.hasTemp(is)) {
	// String s = "";
	// if (HeatRegistry.getInstance().isTemperatureDanger(is)) {
	// s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.danger") + " | ";
	// }
	//
	// if (HeatRegistry.getInstance().isTemperatureWeldable(is)) {
	// s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.weldable") + " | ";
	// }
	//
	// if (HeatRegistry.getInstance().isTemperatureWorkable(is)) {
	// s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.workable");
	// }
	//
	// if (!"".equals(s))
	// arraylist.add(s);
	// }
	// }
	
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
