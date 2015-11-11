package dinglydell.tftechness.block.machine;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import cofh.api.tileentity.IRedstoneControl;
import cofh.lib.util.helpers.EnergyHelper;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.RedstoneControlHelper;
import cofh.lib.util.helpers.SecurityHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermalexpansion.block.machine.ItemBlockMachine;
import cofh.thermalexpansion.util.ReconfigurableHelper;

import com.bioxx.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.bioxx.tfc.api.Enums.EnumSize;
import com.bioxx.tfc.api.Enums.EnumWeight;
import com.bioxx.tfc.api.Interfaces.IEquipable;

import dinglydell.tftechness.TFTechness;

public class ItemBlockTFTMachine extends ItemTerraBlock implements IEquipable {
	
	public ItemBlockTFTMachine(Block block) {
		super(block);
		
	}
	
	/** addItemInformation */
	@Override
	public void func_77624_a(ItemStack it, EntityPlayer player, List info, boolean flag) {
		super.func_77624_a(it, player, info, flag);
		SecurityHelper.addOwnerInformation(it, info);
		if ((StringHelper.displayShiftForDetail) && (!StringHelper.isShiftKeyDown())) {
			info.add(StringHelper.shiftForDetails());
		}
		if (!StringHelper.isShiftKeyDown()) {
			return;
		}
		SecurityHelper.addAccessInformation(it, info);
		
		info.add(StringHelper.getInfoText("info." + TFTechness.MODID + ".machine." + getName(it)));
	}
	
	protected String getName(ItemStack it) {
		return BlockTFTMachine.Types.values()[ItemHelper.getItemDamage(it)].name().toLowerCase();
	}
	
	@Override
	public String getUnlocalizedNameInefficiently(ItemStack it) {
		return StringHelper.localize(getUnlocalizedName(it))
				+ " ("
				+ StringHelper.localize(new StringBuilder().append("info.thermalexpansion.").append(ItemBlockMachine.NAMES[getLevel(it)]).toString())
				+ ")";
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack it) {
		return "tile." + TFTechness.MODID + ".machine." + getName(it) + ".name";
		
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
		// GL11.glRotatef(-90f, 1f, 0f, 0f);
		// GL11.glTranslatef(-0.5f, -0.5f, -0.3f);
	}
	
	// some methods from te ItemBlockMachine tweaked for tft machines
	public static ItemStack setDefaultTag(ItemStack paramItemStack) {
		return setDefaultTag(paramItemStack, (byte) 0);
	}
	
	public static ItemStack setDefaultTag(ItemStack it, byte level) {
		ReconfigurableHelper.setFacing(it, 3);
		RedstoneControlHelper.setControl(it, IRedstoneControl.ControlMode.DISABLED);
		EnergyHelper.setDefaultEnergyTag(it, 0);
		it.stackTagCompound.setByte("Level", level);
		
		return it;
	}
	
	public static byte getLevel(ItemStack it) {
		if (it.stackTagCompound == null) {
			setDefaultTag(it);
		}
		return it.stackTagCompound.getByte("Level");
	}
}
