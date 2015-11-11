package dinglydell.tftechness.block.machine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.IEquipable;

import dinglydell.tftechness.TFTechness;

public class ItemBlockTFTMachine extends ItemTerraBlock implements IEquipable {
	
	public ItemBlockTFTMachine(Block block) {
		super(block);
		
	}
	
	@Override
	public void func_77624_a(ItemStack it, EntityPlayer player, List info, boolean flag) {
		SecurityHelper.addOwnerInformation(it, info);
		if ((StringHelper.displayShiftForDetail) && (!StringHelper.isShiftKeyDown())) {
			info.add(StringHelper.shiftForDetails());
		}
		if (!StringHelper.isShiftKeyDown()) {
			return;
		}
		SecurityHelper.addAccessInformation(it, info);
		
		info.add(StringHelper.getInfoText("info" + TFTechness.MODID + ".machine." + getName(it)));
	}
	
	protected String getName(ItemStack it) {
		return BlockTFTMachine.Types.values()[ItemHelper.getItemDamage(it)].name().toLowerCase();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack it) {
		return "tile." + TFTechness.MODID + ".machine." + getName(it);
		
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
	public boolean getTooHeavyToCarry(ItemStack arg0) {
		return true;
	}
	
	@Override
	public void onEquippedRender() {
		
	}
	
}
