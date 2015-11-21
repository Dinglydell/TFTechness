package dinglydell.tftechness.tileentities.machine;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import cofh.api.energy.EnergyStorage;
import cofh.core.network.PacketCoFHBase;
import cofh.core.render.IconRegistry;
import cofh.lib.util.helpers.ServerHelper;
import cofh.thermalexpansion.block.TileAugmentable;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.tileentities.IAugmentNBT;

public abstract class TileTFTMachine extends TileAugmentable implements IAugmentNBT {
	
	public static IIcon getTexture(TileAugmentable tile, int side, int render) {
		if (side < 2) {
			return (side == 0 ? IconRegistry.getIcon("TFTMachineTop") : IconRegistry.getIcon("TFTMachineBottom"));
		}
		return (side != tile.getFacing()) ? IconRegistry.getIcon("TFTMachineSide")
				: (tile.isActive ? IconRegistry.getIcon("TFTMachineActive", tile.getType())
						: IconRegistry.getIcon("TFTMachineFace", tile.getType()));
	}
	
	/** ThermalExpansion side configuration colours */
	public enum Colours {
		none, blue, red, yellow, orange, green, purple, grey;
		
		public int gui() {
			return ordinal() - 1;
		}
	}
	
	public static final int[] capacityMultiplier = {
			2, 3, 4, 5
	};
	public static final int[] numAugments = {
			3, 4, 5, 6
	};
	protected int energyConsumption;
	protected Map<String, Double> consuptionModifiers = new HashMap<String, Double>();
	protected int lastEnergyConsumption;
	protected EnergyConfig energyConfig;
	protected byte level;
	
	public TileTFTMachine() {
		sideConfig = getSideConfig();
		energyConfig = getEnergyConfig();
		energyStorage = new EnergyStorage(energyConfig.maxEnergy, energyConfig.maxPower);
		
	}
	
	@Override
	public void updateEntity() {
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			chargeEnergy();
			if (isActive) {
				int energy = getEnergyConsumption();
				lastEnergyConsumption = spendEnergy(energy);
				energyStorage.modifyEnergyStored(-lastEnergyConsumption);
				
				if (!redstoneControlOrDisable() || shouldDeactivate()) {
					isActive = false;
					onDeactivate();
				}
			} else {
				lastEnergyConsumption = 0;
				if (redstoneControlOrDisable() && shouldActivate()) {
					isActive = true;
					onActivate();
				}
			}
		}
	}
	
	private int getEnergyConsumption() {
		int rf = getMaxEnergyConsumption();
		if (energyStorage.getEnergyStored() - rf < 0) {
			rf = energyStorage.getEnergyStored();
		}
		return rf;
	}
	
	private int getMaxEnergyConsumption() {
		double energy = energyConsumption;
		for (Double d : consuptionModifiers.values()) {
			energy *= d;
		}
		return (int) Math.ceil(energy);
	}
	
	protected void onLevelChange() {
		augments = new ItemStack[numAugments[level]];
		augmentStatus = new boolean[this.augments.length];
		energyConfig.setParams(this.energyConfig.minPower, this.energyConfig.maxPower, this.energyConfig.maxEnergy
				* capacityMultiplier[level]);
	}
	
	protected void resetAugments() {
		super.resetAugments();
		consuptionModifiers = new HashMap();
	}
	
	@Override
	public PacketCoFHBase getPacket() {
		PacketCoFHBase packet = super.getPacket();
		packet.addByte(level);
		return packet;
	}
	
	@Override
	public void handleTilePacket(PacketCoFHBase packet, boolean b) {
		super.handleTilePacket(packet, b);
		if (!b) {
			int l = level;
			level = packet.getByte();
			if (l != level) {
				onLevelChange();
			}
		} else {
			packet.getByte();
		}
	}
	
	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addInt(lastEnergyConsumption);
		return packet;
	}
	
	@Override
	protected void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		lastEnergyConsumption = packet.getInt();
	}
	
	@Override
	public int getInfoEnergyPerTick() {
		return lastEnergyConsumption;
	}
	
	@Override
	public int getInfoMaxEnergyPerTick() {
		return getMaxEnergyConsumption();
	}
	
	@Override
	public String getName() {
		return "tile." + TFTechness.MODID + ".machine."
				+ BlockTFTMachine.Types.values()[getType()].name().toLowerCase() + ".name";
	}
	
	@Override
	public IIcon getTexture(int side, int render) {
		return getTexture(this, side, render);
	}
	
	@Override
	public void readAugmentsFromNBT(NBTTagCompound nbt) {
		level = nbt.getByte("Level");
		onLevelChange();
		super.readAugmentsFromNBT(nbt);
		
	}
	
	@Override
	public void writeAugmentsToNBT(NBTTagCompound nbt) {
		nbt.setByte("Level", level);
		super.writeAugmentsToNBT(nbt);
	}
	
	@Override
	public boolean installAugment(int i) {
		if (super.installAugment(i)) {
			return true;
		}
		//
		// IAugmentItem augment = (IAugmentItem) augments[i].getItem();
		// int augLvl = augment.getAugmentLevel(augments[i], TEAugments.MACHINE_SPEED);
		// if (augLvl > 0) {
		// if (i > level || hasDuplicateAugment(TEAugments.MACHINE_SPEED, augLvl, i)) {
		// return false;
		// }
		// if (hasAugmentChain(TEAugments.MACHINE_SPEED, augLvl)) {
		// consuptionModifier = Math.max(consuptionModifier,
		// TEAugments.MACHINE_SPEED_ENERGY_MOD[augLvl]);
		// return true;
		// }
		//
		// }
		return false;
	}
	
	public int getLevel() {
		return level;
	}
	
	protected abstract boolean shouldActivate();
	
	protected abstract boolean shouldDeactivate();
	
	protected abstract void onActivate();
	
	protected abstract void onDeactivate();
	
	protected abstract int spendEnergy(int rf);
	
	protected abstract SideConfig getSideConfig();
	
	protected abstract EnergyConfig getEnergyConfig();
}
