package dinglydell.tftechness.tileentities.machine;

import java.lang.reflect.Field;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import cofh.api.energy.EnergyStorage;
import cofh.core.network.PacketCoFHBase;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.FluidHelper;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.machine.TileAccumulator;
import cofh.thermalexpansion.block.machine.TileMachineBase;

import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.gui.GuiTFTAccumulator;
import dinglydell.tftechness.tileentities.IAugmentNBT;
import dinglydell.tftechness.tileentities.machine.TileTFTMachine.Colours;

public class TileTFTAccumulator extends TileAccumulator implements IAugmentNBT {
	
	public static SideConfig defaultSideConfig;
	public static int TYPE = BlockTFTMachine.Types.ACCUMULATOR.ordinal();
	public static int transferRate = 500;
	protected FluidTankAdv tank2 = new FluidTankAdv(4000);
	protected int adjacentSources2;
	protected int outputTrackerFluid;
	protected int outputTrackerFluid2;
	static {
		defaultSideConfig = new SideConfig();
		defaultSideConfig.numConfig = 4;
		defaultSideConfig.slotGroups = new int[][] {
				new int[0], new int[0], new int[0], new int[0]
		};
		defaultSideConfig.allowInsertionSide = new boolean[] {
				false, false, false, false
		};
		defaultSideConfig.allowExtractionSide = new boolean[] {
				false, false, false, false
		};
		defaultSideConfig.sideTex = new int[] {
				Colours.none.ordinal(), Colours.red.ordinal(), Colours.yellow.ordinal(), Colours.orange.ordinal()
		};
		defaultSideConfig.defaultSides = new byte[] {
				3, 3, 3, 3, 3, 3
		};
		
	}
	
	protected boolean inHell;
	Field adjacentSources;
	
	public TileTFTAccumulator() {
		this.getTank().setLock(TFCFluids.FRESHWATER);
		this.tank2.setLock(TFCFluids.SALTWATER);
		try {
			adjacentSources = this.getClass().getSuperclass().getDeclaredField("adjacentSources");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		energyConfig = defaultEnergyConfig[BlockMachine.Types.ACCUMULATOR.ordinal()].copy();
		energyStorage = new EnergyStorage(0, 0);
		
		sideConfig = defaultSideConfig;
		
	}
	
	@Override
	public String getName() {
		return "tile." + TFTechness.MODID + ".machine.accumulator.name";
	}
	
	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiTFTAccumulator(inv, this);
		
	}
	
	@Override
	protected boolean canStart() {
		return !inHell;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("Hell", inHell);
		nbt.setInteger("Tracker", outputTrackerFluid);
		nbt.setInteger("Tracker2", outputTrackerFluid2);
		nbt.setInteger("Sources2", adjacentSources2);
		tank2.writeToNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		inHell = nbt.getBoolean("Hell");
		outputTrackerFluid2 = nbt.getInteger("Tracker2");
		adjacentSources2 = nbt.getInteger("Sources2");
		tank2.readFromNBT(nbt);
	}
	
	@Override
	public void updateEntity() {
		super.updateEntity();
		
		if (!ServerHelper.isClientWorld(worldObj)) {
			if (isActive && timeCheck()) {
				if (this.adjacentSources2 >= 2) {
					this.tank2.fillLocked(genRate, true);
				}
			}
		}
	}
	
	@Override
	protected void updateAdjacentSources() {
		try {
			inHell = (this.worldObj.getBiomeGenForCoords(this.xCoord, this.yCoord) == BiomeGenBase.hell);
			
			int adjacent = getAdjacentSources(getTankFluid());
			
			adjacentSources.setAccessible(true);
			adjacentSources.setInt(this, adjacent);
			
			adjacentSources2 = getAdjacentSources(getTank2().getFluid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	protected int getAdjacentSources(FluidStack fluid) {
		int adjacent = 0;
		for (int i = 0; i < 6; i++) {
			int[] coords = BlockHelper.getAdjacentCoordinatesForSide(xCoord, yCoord, zCoord, i);
			FluidStack f = FluidHelper.getFluidFromWorld(worldObj, coords[0], coords[1], coords[2]);
			if (f != null && f.getFluid() == fluid.getFluid()) {
				adjacent++;
			}
		}
		return adjacent;
	}
	
	@Override
	public IIcon getTexture(int side, int render) {
		if (render != 0 || side < 2) {
			return super.getTexture(side, render);
		}
		return TileTFTMachine.getTexture(this, side, render);
	}
	
	@Override
	public int getType() {
		return TYPE;
	}
	
	@Override
	protected void onLevelChange() {
		super.onLevelChange();
		
		try {
			Field f = this.getClass().getSuperclass().getSuperclass().getDeclaredField("level");
			f.setAccessible(true);
			this.tank2.setCapacity(4000 * TileMachineBase.FLUID_CAPACITY[f.getInt(this)]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public FluidTankAdv getTank2() {
		return tank2;
		
	}
	
	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection paramForgeDirection) {
		return new FluidTankInfo[] {
				getTank().getInfo(), tank2.getInfo()
		};
	}
	
	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		
		packet.addInt(this.tank2.getFluidAmount());
		
		return packet;
	}
	
	@Override
	protected void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		
		this.tank2.getFluid().amount = packet.getInt();
	}
	
	@Override
	protected void transferOutputFluid() {
		
		if (this.augmentAutoOutput) {
			if (getTank().getFluidAmount() > 0) {
				outputTrackerFluid = output(getTank(), outputTrackerFluid, 1, 3);
			}
			if (tank2.getFluidAmount() > 0) {
				outputTrackerFluid2 = output(tank2, outputTrackerFluid2, 2, 3);
			}
		}
	}
	
	protected int output(FluidTankAdv tank, int outputTracker, int sideA, int sideB) {
		FluidStack output = new FluidStack(tank.getFluid(), transferRate);
		for (int i = outputTracker; i < outputTracker + 6; i++) {
			int j = i % 6;
			if (sideCache[j] == sideA || sideCache[j] == sideB) {
				int amt = FluidHelper.insertFluidIntoAdjacentFluidHandler(this, j, output, true);
				if (amt > 0) {
					tank.drain(amt, true);
					return j;
				}
			}
		}
		return outputTracker;
		
	}
}
