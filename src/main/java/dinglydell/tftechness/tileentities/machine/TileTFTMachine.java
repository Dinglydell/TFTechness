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
import cofh.thermalexpansion.core.TEProps;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.tileentities.IAugmentNBT;

public abstract class TileTFTMachine extends TileAugmentable implements
		IAugmentNBT {
	protected static final int[] AUTO_TRANSFER = { 8, 16, 32, 64 };

	public static IIcon getTexture(TileAugmentable tile, int side, int render) {
		if (side < 2) {
			return (side == 0 ? IconRegistry.getIcon("TFTMachineTop")
					: IconRegistry.getIcon("TFTMachineBottom"));
		}

		return (side != tile.getFacing()) ? IconRegistry
				.getIcon("TFTMachineSide") : (tile.isActive ? IconRegistry
				.getIcon("TFTMachineActive", tile.getType()) : IconRegistry
				.getIcon("TFTMachineFace", tile.getType()));
	}

	/** ThermalExpansion side configuration colours */
	public enum Colours {
		none, blue, red, yellow, orange, green, purple, grey;

		public int gui() {
			return ordinal() - 1;
		}
	}

	public static final int[] capacityMultiplier = { 2, 3, 4, 5 };
	public static final int[] numAugments = { 3, 4, 5, 6 };
	protected int energyConsumption;
	protected Map<String, Double> consuptionModifiers = new HashMap<String, Double>();
	protected int lastEnergyConsumption;
	protected EnergyConfig energyConfig;
	protected byte level;
	/**
	 * Key: Value Side config: output slots
	 * */
	protected Map<Integer, int[]> outputSlots = new HashMap();

	public TileTFTMachine() {
		sideConfig = getSideConfig();
		energyConfig = getEnergyConfig();
		energyStorage = new EnergyStorage(energyConfig.maxEnergy,
				energyConfig.maxPower);

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
				if (redstoneControlOrDisable()) {
					transferOutput();
					if (shouldActivate()) {
						isActive = true;
						onActivate();
					}
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
		energyConfig.setParams(this.energyConfig.minPower,
				this.energyConfig.maxPower,
				this.energyConfig.maxEnergy * capacityMultiplier[level]);
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
		return "tile."
				+ TFTechness.MODID
				+ ".machine."
				+ BlockTFTMachine.Types.values()[getType()].name()
						.toLowerCase() + ".name";
	}

	@Override
	public IIcon getTexture(int side, int render) {
		if (render == 0) {
			if (side == 0) {
				return IconRegistry.getIcon("TFTMachineTop");

			}
			if (side == 1) {
				return IconRegistry.getIcon("TFTMachineBottom");
			}
			return (side != getFacing()) ? IconRegistry
					.getIcon("TFTMachineSide") : (isActive ? IconRegistry
					.getIcon("TFTMachineActive", getType()) : IconRegistry
					.getIcon("TFTMachineFace", getType()));
		}
		if (side < 6) {
			return IconRegistry.getIcon(TEProps.textureSelection,
					this.sideConfig.sideTex[this.sideCache[side]]);
		}
		return IconRegistry.getIcon("TFTMachineSide");
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
		// int augLvl = augment.getAugmentLevel(augments[i],
		// TEAugments.MACHINE_SPEED);
		// if (augLvl > 0) {
		// if (i > level || hasDuplicateAugment(TEAugments.MACHINE_SPEED,
		// augLvl, i)) {
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

	protected void transferOutput() {
		if (!this.augmentAutoOutput || this.outputSlots.isEmpty()) {
			return;
		}
		for (int side = 0; side < 6; side++) {

			//HeatIndex index = HeatRegistry.getInstance()
			//		.findMatchingIndex(inventory[i]);
			//float temp = TFC_ItemHeat.getTemp(inventory[i]);
			//if (temp <= index.meltTemp * 0.99) {
			//	continue;
			//}
			//
			if (outputSlots.containsKey((int) this.sideCache[side])) {
				//TODO: investigate exactly how this function works
				for (int slot : outputSlots.get((int) this.sideCache[side]))
					this.transferItem(slot, AUTO_TRANSFER[this.level], side);
			}
		}

	}

	protected abstract boolean shouldActivate();

	protected abstract boolean shouldDeactivate();

	protected abstract void onActivate();

	protected abstract void onDeactivate();

	protected abstract int spendEnergy(int rf);

	protected abstract SideConfig getSideConfig();

	protected abstract EnergyConfig getEnergyConfig();
}
