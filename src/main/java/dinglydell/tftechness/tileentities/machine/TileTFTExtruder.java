package dinglydell.tftechness.tileentities.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalexpansion.block.machine.TileExtruder;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.gui.GuiTFTExtruder;
import dinglydell.tftechness.gui.container.ContainerTFTExtruder;
import dinglydell.tftechness.item.TFCMeta;

public class TileTFTExtruder extends TileExtruder {
	
	public ItemStack[] outputItems;
	public ItemStack secondaryOutput;
	
	public TileTFTExtruder() {
		super();
		inventory = new ItemStack[2];
		outputItems = this.getInventorySlots(0);
		outputItems[0] = new ItemStack(TFCBlocks.stoneIgExCobble);
		outputItems[1] = new ItemStack(TFCBlocks.stoneIgEx);
		// outputItems[2] = new ItemStack(Blocks.obsidian);
		secondaryOutput = ItemHelper.cloneStack(TFCMeta.salt, 1);
	}
	
	@Override
	public int fill(ForgeDirection direction, FluidStack fluid, boolean paramBoolean) {
		if (fluid.getFluid() == TFCFluids.FRESHWATER) {
			
			return getColdTank().fill(fluid, paramBoolean);
		}
		if (fluid.getFluid() == TFCFluids.SALTWATER) {
			
			return getColdTank().fill(fluid, paramBoolean);
		}
		if (fluid.getFluid() == TFCFluids.LAVA) {
			
			return getHotTank().fill(fluid, paramBoolean);
		}
		return 0;
		
	}
	
	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiTFTExtruder(inv, this);
		
	}
	
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerTFTExtruder(inv, this);
	}
	
	public FluidTankAdv getHotTank() {
		return getTank(0);
	}
	
	public FluidTankAdv getColdTank() {
		return getTank(1);
	}
	
	@Override
	protected void processFinish() {
		int water = getColdTank().getFluidAmount();
		boolean salt = getColdTank().getFluid().getFluid() == TFCFluids.SALTWATER;
		super.processFinish();
		if (salt) {
			int dw = water - getColdTank().getFluidAmount();
			if (dw > 0) {
				int i = dw / 1000;
				if (this.inventory[1] == null) {
					this.inventory[1] = ItemHelper.cloneStack(secondaryOutput, i);
				} else {
					this.inventory[1].stackSize += i;
					int max = this.inventory[1].getMaxStackSize();
					if (this.inventory[1].stackSize > max) {
						this.inventory[1].stackSize = max;
					}
				}
			}
		}
	}
	
}
