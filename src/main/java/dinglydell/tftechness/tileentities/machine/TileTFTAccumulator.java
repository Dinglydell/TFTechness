package dinglydell.tftechness.tileentities.machine;

import java.lang.reflect.Field;

import net.minecraft.util.IIcon;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.energy.EnergyStorage;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.FluidHelper;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.machine.TileAccumulator;
import cofh.thermalexpansion.block.machine.TileMachineBase;

import com.bioxx.tfc.api.TFCFluids;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;

public class TileTFTAccumulator extends TileAccumulator {
	
	public static SideConfig defaultSideConfig;
	public static int TYPE = BlockTFTMachine.Types.ACCUMULATOR.ordinal();
	static {
		defaultSideConfig = TileMachineBase.defaultSideConfig[BlockMachine.Types.ACCUMULATOR.ordinal()];
		
	}
	
	Field inHell;
	Field adjacentSources;
	
	public TileTFTAccumulator() {
		this.getTank().setLock(TFCFluids.FRESHWATER);
		try {
			inHell = this.getClass().getSuperclass().getDeclaredField("inHell");
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
	protected void updateAdjacentSources() {
		try {
			boolean hell = (this.worldObj.getBiomeGenForCoords(this.xCoord, this.yCoord) == BiomeGenBase.hell);
			inHell.setAccessible(true);
			inHell.setBoolean(this, hell);
			
			int adjacent = 0;
			
			for (int i = 0; i < 6; i++) {
				int[] coords = BlockHelper.getAdjacentCoordinatesForSide(xCoord, yCoord, zCoord, i);
				FluidStack f = FluidHelper.getFluidFromWorld(worldObj, coords[0], coords[1], coords[2]);
				if (f != null && f.getFluid() == getTankFluid().getFluid()) {
					adjacent++;
				}
			}
			adjacentSources.setAccessible(true);
			adjacentSources.setInt(this, adjacent);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public IIcon getTexture(int side, int render) {
		if (render != 0 || side < 2) {
			if (side < 6) {
				TFTechness.logger.info(this.sideCache[side]);
				
			}
			return super.getTexture(side, render);
		}
		return TileTFTMachine.getTexture(this, side, render);
	}
	
	@Override
	public int getType() {
		return TYPE;
	}
}
