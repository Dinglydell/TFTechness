package dinglydell.tftechness.block;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cofh.core.item.ItemBlockBase;
import cofh.thermalexpansion.block.tank.BlockTank;

import com.bioxx.tfc.Core.TFC_Core;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Enums.EnumItemReach;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.ISize;

import dinglydell.tftechness.TFTechness;

public class ItemBlockTankFrame extends ItemBlockBase implements ISize {
	
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
	
	// From ItemTerra, cannot extend both ItemBlock and ItemTerra - ItemBlockTerra does not support
	// the weldable flag
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) {
		
		ItemTerra.addSizeInformation(is, arraylist);
		
		ItemTerra.addHeatInformation(is, arraylist);
		
		if (is.hasTagCompound()) {
			if (is.getTagCompound().hasKey("itemCraftingValue")
					&& is.getTagCompound().getShort("itemCraftingValue") != 0)
				arraylist.add("This Item Has Been Worked");
		}
		
		addItemInformation(is, player, arraylist);
	}
	
	public void addItemInformation(ItemStack is, EntityPlayer player, List<String> arraylist) {
		
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
	public EnumItemReach getReach(ItemStack it) {
		return EnumItemReach.MEDIUM;
	}
	
	@Override
	public EnumSize getSize(ItemStack it) {
		return EnumSize.MEDIUM;
	}
	
	@Override
	public EnumWeight getWeight(ItemStack it) {
		return EnumWeight.MEDIUM;
	}
	
}
