package dinglydell.tftechness.block.dynamo;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cofh.api.tileentity.IRedstoneControl.ControlMode;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.RedstoneControlHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.block.dynamo.BlockDynamo;
import cofh.thermalexpansion.util.ReconfigurableHelper;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.IEquipable;

import dinglydell.tftechness.TFTechness;

public class ItemBlockTFTDynamo extends ItemTerraBlock implements IEquipable {
	
	public static ItemStack setDefaultTag(ItemStack is) {
		ReconfigurableHelper.setFacing(is, 1);
		RedstoneControlHelper.setControl(is, ControlMode.DISABLED);
		EnergyHelper.setDefaultEnergyTag(is, 0);
		// AugmentHelper.writeAugments(is, BlockDynamo.defaultAugments);
		
		return is;
	}
	
	public ItemBlockTFTDynamo(Block b) {
		super(b);
	}
	
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List list, boolean b) {
		super.addInformation(is, player, list, b);
		SecurityHelper.addOwnerInformation(is, list);
		if ((StringHelper.displayShiftForDetail) && (!StringHelper.isShiftKeyDown())) {
			list.add(StringHelper.shiftForDetails());
		}
		if (StringHelper.isShiftKeyDown()) {
			
			SecurityHelper.addAccessInformation(is, list);
			list.add(StringHelper.localize("info.thermalexpansion.dynamo.generate"));
			list.add(StringHelper.getInfoText("info.thermalexpansion.dynamo." + getName(is)));
			if (ItemHelper.getItemDamage(is) == BlockDynamo.Types.STEAM.ordinal()) {
				list.add(StringHelper.getNoticeText("info.thermalexpansion.dynamo.steam.0"));
			}
		}
		
	}
	
	public String getName(ItemStack is) {
		return BlockTFTDynamo.Types.values()[ItemHelper.getItemDamage(is)].name().toLowerCase();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack it) {
		return "tile." + TFTechness.MODID + ".dynamo." + getName(it) + ".name";
	}
	
	@Override
	public EnumSize getSize(ItemStack is) {
		return EnumSize.HUGE;
	}
	
	@Override
	public EnumWeight getWeight(ItemStack is) {
		return EnumWeight.HEAVY;
	}
	
	@Override
	public EquipType getEquipType(ItemStack is) {
		return EquipType.BACK;
	}
	
	@Override
	public boolean getTooHeavyToCarry(ItemStack is) {
		return true;
	}
	
	@Override
	public void onEquippedRender() {
		
	}
	
}
