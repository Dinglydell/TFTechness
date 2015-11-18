package dinglydell.tftechness.tileentities.machine;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalexpansion.block.machine.TilePrecipitator;

import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.item.TFCMeta;
import dinglydell.tftechness.tileentities.IAugmentNBT;

public class TileTFTPrecipitator extends TilePrecipitator implements IAugmentNBT {
	
	protected Map<Fluid, ItemStack[]> processItems = new HashMap();
	protected static int[] processEnergy = {
			800, 800, 1600
	};
	protected static int[] processWater = {
			500, 500, 1000
	};
	protected Fluid lastFluid;
	
	public TileTFTPrecipitator() {
		super();
		lastFluid = TFCFluids.FRESHWATER;
		ItemStack[] fresh = new ItemStack[3];
		fresh[0] = new ItemStack(Items.snowball, 4);
		fresh[1] = new ItemStack(Blocks.snow);
		fresh[2] = ItemHelper.cloneStack(TFCMeta.freshIce, 1);
		processItems.put(TFCFluids.FRESHWATER, fresh);
		
		ItemStack[] salt = new ItemStack[3];
		salt[0] = new ItemStack(Items.snowball, 4);
		salt[1] = new ItemStack(Blocks.snow);
		salt[2] = ItemHelper.cloneStack(TFCMeta.saltIce, 1);
		processItems.put(TFCFluids.SALTWATER, salt);
	}
	
	@Override
	public int fill(ForgeDirection dir, FluidStack fs, boolean b) {
		if ((dir != ForgeDirection.UNKNOWN && sideCache[dir.ordinal()] != 1)
				|| !(fs.getFluid() == TFCFluids.FRESHWATER || fs.getFluid() == TFCFluids.SALTWATER)) {
			return 0;
		}
		int result = getTank().fill(fs, b);
		if (result > 0) {
			lastFluid = fs.getFluid();
		}
		return result;
	}
	
	@Override
	protected boolean canStart() {
		if (energyStorage.getEnergyStored() < processEnergy[getCurSelection()]
				|| (getTank().getFluidAmount() < processWater[getCurSelection()])) {
			return false;
		}
		if (inventory[0] == null) {
			return true;
		}
		if (!inventory[0].isItemEqual(processItems.get(getTankFluid().getFluid())[getCurSelection()])) {
			return false;
		}
		return inventory[0].stackSize + processItems.get(getTankFluid().getFluid())[getCurSelection()].stackSize <= inventory[0].getMaxStackSize();
	}
	
	@Override
	protected void processFinish() {
		if (inventory[0] == null) {
			inventory[0] = getCurrentItemSet()[getPrevSelection()].copy();
		} else {
			inventory[0].stackSize += getCurrentItemSet()[getPrevSelection()].stackSize;
		}
		getTank().drain(processWater[getPrevSelection()], true);
		try {
			Field prev = this.getClass().getSuperclass().getDeclaredField("prevSelection");
			prev.setAccessible(true);
			prev.setByte(this, (byte) getCurSelection());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public ItemStack[] getCurrentItemSet() {
		return processItems.get(getTankFluid().getFluid());
	}
	
	@Override
	public ItemStack[] getInventorySlots(int p) {
		if (getTankFluid() == null || getCurrentItemSet() == null) {
			return processItems.get(lastFluid);
		}
		return getCurrentItemSet();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		lastFluid = FluidRegistry.getFluid(nbt.getInteger("LastFluid"));
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (lastFluid != null) {
			nbt.setInteger("LastFluid", lastFluid.getID());
		}
	}
	
}
