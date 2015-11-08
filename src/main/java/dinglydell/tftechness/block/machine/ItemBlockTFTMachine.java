package dinglydell.tftechness.block.machine;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.IEquipable;

public class ItemBlockTFTMachine extends ItemTerraBlock implements IEquipable {
	
	public ItemBlockTFTMachine(Block block) {
		super(block);
		
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
