package dinglydell.tftechness.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import cofh.core.item.ItemBlockBase;
import cofh.thermalexpansion.block.tank.BlockTank;
import dinglydell.tftechness.TFTechness;

public class ItemBlockTankFrame extends ItemBlockBase {
	
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
	
}
