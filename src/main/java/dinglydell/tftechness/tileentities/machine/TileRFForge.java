package dinglydell.tftechness.tileentities.machine;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import cofh.core.network.PacketCoFHBase;
import cofh.core.util.fluid.FluidTankAdv;
import cofh.lib.util.helpers.ServerHelper;

import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.TFC_ItemHeat;
import com.bioxx.tfc.api.Interfaces.ISmeltable;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.fluid.FluidMoltenMetal;
import dinglydell.tftechness.fluid.TFTFluids;
import dinglydell.tftechness.gui.GuiRFForge;
import dinglydell.tftechness.gui.container.ContainerRFForge;

public class TileRFForge extends TileTemperature implements IRFForgeMultiBlock {

	protected static final int[] tankCapacity = { 4000, 8000, 16000, 32000 };
	protected static final float specificHeat = 1.5f;
	protected static final int mass = 1200;
	protected static final float[] levelExposedSurfaceArea = { 0.03f,
			0.02f,
			0.01f,
			0.0075f };
	protected static final float coolingExposedSurfaceArea = 1;
	// Currently all items have the same properties
	protected static final float itemSurfaceArea = 0.074f;
	protected static final float itemMass = 10;

	// protected float internalTemperature = TFTechness.baseTemp;
	public float targetTemperature = 0;
	protected FluidTankAdv tank = new FluidTankAdv(tankCapacity[0]);
	/**
	 * All input slots will have an index >= tankASlotEnd and < this value
	 */
	public static final int inputSlotEnd = 6;
	// protected FluidTankAlloy tankB = new FluidTankAlloy(tankCapacity[0]);
	private boolean isCooling;

	// private FluidTankAdv guiTankA = new FluidTankAdv(tankCapacity[0]);
	// private FluidTankAdv guiTankB = new FluidTankAdv(tankCapacity[0]);

	public TileRFForge() {
		inventory = new ItemStack[8];
		internalTemperature = TFTechness.baseTemp;
	}

	@Override
	protected boolean shouldActivate() {
		return true;
	}

	@Override
	protected boolean shouldDeactivate() {
		return false;
	}

	@Override
	protected void onActivate() {
	}

	@Override
	protected void onDeactivate() {
	}

	@Override
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFluidStack(tank.getFluid());
		packet.addFloat(targetTemperature);
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		tank.setFluid(packet.getFluidStack());
		targetTemperature = packet.getFloat();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		hasMaster = nbt.getBoolean("HasMaster");
		isMaster = nbt.getBoolean("IsMaster");
		direction = ForgeDirection.values()[nbt.getInteger("Direction")];
		tank.readFromNBT(nbt.getCompoundTag("Tank"));
		targetTemperature = nbt.getFloat("TargetTemperature");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("HasMaster", hasMaster);
		nbt.setBoolean("IsMaster", isMaster);
		nbt.setInteger("Direction", direction.ordinal());
		nbt.setTag("Tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setFloat("TargetTemperature", targetTemperature);
	}

	@Override
	public void updateEntity() {
		if (hasMaster()) {
			if (isMaster()) {
				super.updateEntity();
				if (!ServerHelper.isClientWorld(this.worldObj)) {
					heatInventorySlots();
					handleMoldOutputs();
				}
			}
		} else {
			setupMultiblock(checkForMultiblock());

		}
	}

	private void setupMultiblock(ForgeDirection fd) {
		if (fd != null) {
			direction = fd;
			int cx = xCoord + fd.offsetX;
			int cy = yCoord + fd.offsetY;
			int cz = zCoord + fd.offsetZ;
			for (int x = cx - 1; x <= cx + 1; x++) {
				for (int y = cy - 1; y <= cy + 1; y++) {
					for (int z = cz - 1; z <= cz + 1; z++) {
						//if centre block
						if (x == cx && y == cy && z == cz) {
							continue;
						}
						IRFForgeMultiBlock te = (IRFForgeMultiBlock) worldObj
								.getTileEntity(x, y, z);
						te.setHasMaster(true);
						te.setMasterCoords(xCoord, yCoord, zCoord);
					}
				}
			}

			this.setIsMaster(true);
		}

	}

	/**
	 * Probably overcomplicated method that checks whether the multiblock
	 * structure is valid
	 */
	private ForgeDirection checkForMultiblock() {

		boolean eastWest = false;
		// must either be a block west and east or north and south
		if (checkBlock(xCoord - 1, yCoord, zCoord)
				&& checkBlock(xCoord + 1, yCoord, zCoord)) {
			// must be east/west
			eastWest = true;

		}
		boolean northSouth = false;
		if (checkBlock(xCoord, yCoord, zCoord - 1)
				&& checkBlock(xCoord, yCoord, zCoord + 1)) {
			// must be north south
			northSouth = true;
		}
		if (eastWest && northSouth)
			// cannot be both north/south and east/west
			return null;

		if (eastWest) {
			//controller has blocks east/west
			if (checkBlock(xCoord, yCoord, zCoord + 2)) {
				//validate south
				// S C S    N
				// S   S  W + E
				// S S S    S
				if (checkCube(ForgeDirection.SOUTH)) {
					return ForgeDirection.SOUTH;
				}
			}

			//validate north - if south valid will not reach here
			if (checkCube(ForgeDirection.NORTH)) {
				return ForgeDirection.NORTH;
			}

		} else {
			// controller has blocks north/south
			if (checkBlock(xCoord + 2, yCoord, zCoord)) {
				//validate east
				// S S S    N
				// C   S  W + E
				// S S S    S
				if (checkCube(ForgeDirection.EAST)) {
					return ForgeDirection.EAST;
				}
			}
			//validate west - if east valid will not reach here
			if (checkCube(ForgeDirection.WEST)) {
				return ForgeDirection.WEST;
			}

		}

		return null;

	}

	private boolean checkCube(ForgeDirection fd) {
		int cx = xCoord + fd.offsetX;
		int cy = yCoord + fd.offsetY;
		int cz = zCoord + fd.offsetZ;
		for (int x = cx - 1; x <= cx + 1; x++) {
			for (int y = cy - 1; y <= cy + 1; y++) {
				for (int z = cz - 1; z <= cz + 1; z++) {
					//if centre block
					if (x == cx && y == cy && z == cz) {
						//must be empty
						Block block = worldObj.getBlock(x, y, z);
						if (!(block == null || block.getMaterial() == Material.air)) {
							return false;
						} else {
							continue;
						}
					}
					if (!checkBlock(x, y, z)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkBlock(int x, int y, int z) {
		if (x == xCoord && y == yCoord && z == zCoord) {
			return true;
		}
		TileEntity te = worldObj.getTileEntity(x, y, z);
		return (te instanceof IRFForgeMultiBlock && !(te instanceof TileRFForge));
	}

	protected void handleMoldOutputs() {
		handleMoldOutput(inputSlotEnd, tank);
		// handleMoldOutput(tankBSlotEnd + 1, tankB);

	}

	protected void handleMoldOutput(int slot, FluidTankAdv tnk) {
		if (inventory[slot] != null && tnk.getFluid() != null) {
			int drainAmt = (int) (1 / (float) MetalConfig.ingotFluidmB * 100);
			FluidStack fs = tnk.drain(drainAmt, false);
			FluidMoltenMetal m = (FluidMoltenMetal) fs.getFluid();
			if (HeatRegistry.getInstance().getMeltingPoint(new ItemStack(
					m.getMetal().meltedItem)) < internalTemperature) {

				if (inventory[slot].getItem() == TFCItems.ceramicMold) {
					inventory[slot] = new ItemStack(m.getMetal().meltedItem, 1,
							99);
					TFC_ItemHeat.setTemp(inventory[slot], internalTemperature);
					tnk.drain(drainAmt, true);
				} else if (inventory[slot].getItem() == m.getMetal().meltedItem
						&& inventory[slot].getItemDamage() > 0) {

					inventory[slot].setItemDamage(inventory[slot]
							.getItemDamage() - 1);
					// Proportion of temperature between the amount added an the
					// amount in the mold
					float prop = 1 / (100 - inventory[slot].getItemDamage());
					float temp = prop * internalTemperature + (1 - prop)
							* TFC_ItemHeat.getTemp(inventory[slot]);
					TFC_ItemHeat.setTemp(inventory[slot], temp);
					tnk.drain(drainAmt, true);
				}
			}
		}

	}

	protected void heatInventorySlots() {
		for (int i = 0; i < inputSlotEnd; i++) {

			ItemStack is = inventory[i];
			if (is != null) {
				HeatIndex index = HeatRegistry.getInstance()
						.findMatchingIndex(is);
				if (index != null && index.hasOutput()) {
					float temp = TFC_ItemHeat.getTemp(is);
					float change = getTemperatureChange(temp,
							internalTemperature,
							itemSurfaceArea,
							index.specificHeat,
							itemMass);
					temp += change;
					internalTemperature -= change * 0.001;
					// if (internalTemperature > temp) {
					// temp += TFC_ItemHeat.getTempIncrease(is);
					// } else {
					// temp -= TFC_ItemHeat.getTempDecrease(is);
					// }
					TFC_ItemHeat.setTemp(is, temp);
					if (temp > index.meltTemp
							&& is.getItem() instanceof ISmeltable) {
						ISmeltable its = (ISmeltable) is.getItem();
						Metal m = its.getMetalType(is);
						if (m != null) {
							Fluid f = TFTFluids.metal.get(m.name);
							int amt = its.getMetalReturnAmount(is);
							FluidStack fs = new FluidStack(f, amt);
							// if (tank.fill(fs, false) == amt) {
							tank.fill(fs, true);
							// }
							inventory[i] = null;
						}
					}
				}

			}
		}
	}

	@Override
	protected int spendEnergy(int rf) {
		float j = rf * TFTechness.rfToJoules;
		float tmp = j / (mass * specificHeat);
		if (internalTemperature + tmp > targetTemperature) {
			tmp = Math.max(0, targetTemperature - internalTemperature);
			internalTemperature += tmp;

			isCooling = tmp == 0;

			return (int) Math.ceil((tmp * mass * specificHeat)
					/ TFTechness.rfToJoules);
		}
		isCooling = false;
		internalTemperature += tmp;
		return rf;
	}

	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiRFForge(inv, this);

	}

	@Override
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerRFForge(inv, this);
	}

	@Override
	protected SideConfig getSideConfig() {

		SideConfig cfg = new SideConfig();
		cfg.numConfig = 8;
		cfg.slotGroups = new int[][] { new int[0],
				new int[0],
				{ 0 },
				{ 1 },
				{ 0, 1 },
				{ 0, 1 },
				{ 0, 1 },
				{ 0, 1 } };
		cfg.allowInsertionSide = new boolean[] { false,
				true,
				false,
				false,
				false,
				true,
				true,
				false };
		cfg.allowExtractionSide = new boolean[] { false,
				true,
				true,
				true,
				true,
				true,
				true,
				false };
		cfg.allowInsertionSlot = new boolean[] { true,
				false,
				false,
				false,
				false,
				false,
				false };
		cfg.allowExtractionSlot = new boolean[] { true, true, true, true, false };
		cfg.sideTex = new int[] { Colours.none.ordinal(),
				Colours.blue.ordinal(),
				Colours.red.ordinal(),
				Colours.yellow.ordinal(),
				Colours.orange.ordinal(),
				Colours.green.ordinal(),
				Colours.purple.ordinal(),
				Colours.grey.ordinal() };
		cfg.defaultSides = new byte[] { 3, 1, 2, 2, 2, 2 };
		return cfg;
	}

	@Override
	protected EnergyConfig getEnergyConfig() {
		energyConsumption = 80;
		EnergyConfig cfg = new EnergyConfig();
		cfg.maxEnergy = 96000;
		cfg.maxPower = 500;
		return cfg;
	}

	@Override
	public int getType() {
		return BlockTFTMachine.Types.RFFORGE.ordinal();
	}

	@Override
	protected void onLevelChange() {
		super.onLevelChange();
		tank.setCapacity(tankCapacity[level]);
	}

	@Override
	protected float getSurfaceArea() {
		return isCooling ? coolingExposedSurfaceArea
				: levelExposedSurfaceArea[level];
	}

	@Override
	protected float getSpecificHeat() {
		return specificHeat;
	}

	@Override
	protected int getMass() {
		return mass;
	}

	@Override
	public PacketCoFHBase getModePacket() {
		PacketCoFHBase packet = super.getModePacket();
		packet.addFloat(targetTemperature);
		return packet;
	}

	@Override
	protected void handleModePacket(PacketCoFHBase packet) {
		super.handleModePacket(packet);
		targetTemperature = packet.getFloat();
	}

	public FluidTankAdv getTank() {
		return tank;
	}

	private boolean isMaster;
	private boolean hasMaster;
	private int masterX;
	private int masterY;
	private int masterZ;
	private ForgeDirection direction;

	@Override
	public boolean hasMaster() {
		return hasMaster;
	}

	@Override
	public boolean isMaster() {
		return isMaster;
	}

	@Override
	public int getMasterX() {
		return masterX;
	}

	@Override
	public int getMasterY() {
		return masterY;
	}

	@Override
	public int getMasterZ() {
		return masterZ;
	}

	@Override
	public void setHasMaster(boolean has) {
		// if (has) {
		// throw new IllegalArgumentException(
		// "RFForge controller cannot have a master");
		// }
		hasMaster = has;

	}

	@Override
	public void setIsMaster(boolean is) {
		isMaster = is;

	}

	@Override
	public void setMasterCoords(int x, int y, int z) {
		masterX = x;
		masterY = y;
		masterZ = z;
	}

	@Override
	public boolean checkForMaster() {
		return false;
	}

	@Override
	public void reset() {
		isMaster = false;
		hasMaster = false;
		masterX = 0;
		masterY = 0;
		masterZ = 0;

	}

	// public FluidTankAlloy getTankB() {
	// return tankB;
	// }
}
