package dinglydell.tftechness.tileentities.machine;

import java.lang.reflect.Field;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.energy.EnergyStorage;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.lib.util.helpers.ItemHelper;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.machine.TileExtruder;
import cofh.thermalexpansion.block.machine.TileMachineBase;

import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.gui.GuiTFTExtruder;
import dinglydell.tftechness.gui.container.ContainerTFTExtruder;
import dinglydell.tftechness.item.TFCMeta;

public class TileTFTExtruder extends TileExtruder {
	
	public static SideConfig defaultSideConfig;
	public static int TYPE = BlockTFTMachine.Types.EXTRUDER.ordinal();
	
	static {
		defaultSideConfig = new SideConfig();
		defaultSideConfig.numConfig = 6;
		defaultSideConfig.slotGroups = new int[][] {
				new int[0], {
					0
				}, {
						1, 2
				}, {
					3
				}, {
						1, 2, 3
				}, {
						0, 1, 2, 3
				}
		};
		defaultSideConfig.allowInsertionSide = new boolean[] {
				false, true, false, false, false, true
		};
		defaultSideConfig.allowExtractionSide = new boolean[] {
				false, true, true, true, true, true
		};
		defaultSideConfig.allowInsertionSlot = new boolean[] {
				true, false, false, false, false
		};
		defaultSideConfig.allowExtractionSlot = new boolean[] {
				true, true, true, true, false
		};
		defaultSideConfig.sideTex = new int[] {
				0, 1, 2, 3, 4, 7
		};
		defaultSideConfig.defaultSides = new byte[] {
				3, 1, 2, 2, 2, 2
		};
	}
	
	public ItemStack[] outputItems;
	public ItemStack secondaryOutput;
	protected int outputTracker;
	protected int outputTrackerSecondary;
	// TileExtruder seems to magically be able to see this variable from TileMachineBase, despite
	// the fact it is private.
	protected byte level;
	
	public TileTFTExtruder() {
		super();
		inventory = new ItemStack[2];
		outputItems = this.getInventorySlots(0);
		outputItems[0] = new ItemStack(TFCBlocks.stoneIgExCobble);
		outputItems[1] = new ItemStack(TFCBlocks.stoneIgEx);
		// outputItems[2] = new ItemStack(Blocks.obsidian);
		secondaryOutput = ItemHelper.cloneStack(TFCMeta.salt, 1);
		energyConfig = defaultEnergyConfig[BlockMachine.Types.EXTRUDER.ordinal()].copy();
		energyStorage = new EnergyStorage(0, 0);
		
		sideConfig = defaultSideConfig;
		
	}
	
	public String getName() {
		return "tile." + TFTechness.MODID + ".machine.extruder.name";
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
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("Tracker", this.outputTracker);
		nbt.setInteger("Tracker2", this.outputTrackerSecondary);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.outputTracker = nbt.getInteger("Tracker2");
		this.outputTrackerSecondary = nbt.getInteger("Tracker2");
	}
	
	@Override
	public void onLevelChange() {
		super.onLevelChange();
		Field f;
		try {
			// ew
			f = super.getClass().getSuperclass().getDeclaredField("level");
			f.setAccessible(true);
			this.level = f.getByte(this);
			f.setAccessible(false);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}
	
	@Override
	protected void transferOutput() {
		// super.transferOutput();
		if (this.augmentAutoOutput) {
			if (this.inventory[0] != null) {
				try {
					for (int i = outputTracker; i < outputTracker + 6; i++) {
						int j = i % 6;
						if ((this.sideCache[j] == 2 || this.sideCache[j] == 4)
								&& transferItem(0, TileMachineBase.AUTO_TRANSFER[this.level], j)) {
							outputTracker = j;
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (this.inventory[1] != null) {
				for (int i = outputTrackerSecondary; i < outputTrackerSecondary + 6; i++) {
					int j = i % 6;
					if ((this.sideCache[j] == 3 || this.sideCache[j] == 4)
							&& transferItem(1, TileMachineBase.AUTO_TRANSFER[this.level], j)) {
						outputTrackerSecondary = j;
						break;
					}
				}
			}
		}
	}
	
	@Override
	protected void processFinish() {
		int water = getColdTank().getFluidAmount();
		FluidStack f = getColdTank().getFluid();
		boolean salt = f != null && f.getFluid() == TFCFluids.SALTWATER;
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
	
	@Override
	protected boolean canStart() {
		if (this.inventory[1] != null && this.inventory[1].stackSize == secondaryOutput.getMaxStackSize()) {
			return false;
		}
		return super.canStart();
	}
	
	@Override
	public int getType() {
		return TYPE;
	}
	
	@Override
	public IIcon getTexture(int side, int render) {
		if (render != 0 || side < 2) {
			return super.getTexture(side, render);
		}
		return TileTFTMachine.getTexture(this, side, render);
	}
	
}