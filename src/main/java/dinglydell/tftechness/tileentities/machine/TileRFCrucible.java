package dinglydell.tftechness.tileentities.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import cofh.core.network.PacketCoFHBase;
import cofh.lib.util.helpers.ServerHelper;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.fluid.FluidMoltenMetal;
import dinglydell.tftechness.fluid.FluidStackFloat;
import dinglydell.tftechness.fluid.FluidTankAlloy;
import dinglydell.tftechness.gui.GuiRFCrucible;
import dinglydell.tftechness.gui.container.ContainerRFCrucible;
import dinglydell.tftechness.item.TFTAugments;
import dinglydell.tftechness.metal.MetalSnatcher;
import dinglydell.tftechness.metal.MetalStat;

public class TileRFCrucible extends TileTemperatureControl implements
		IFluidHandler {

	protected static final int[] tankCapacity = { 4000, 8000, 16000, 32000 };
	protected FluidTankAlloy tank = new FluidTankAlloy(tankCapacity[0]);
	protected float tankFluidTemperature = 0;
	protected boolean locked;
	protected FluidMoltenMetal targetFluid;

	public TileRFCrucible() {
		super(false);
		inventory = new ItemStack[2];
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			adjustTargetTemperature();
			heatFluids();

			if (isHotEnough()
					&& (!locked || tank.getAlloyFluid().getFluid() == targetFluid)) {
				handleMoldOutput(0, tank);
			}
		}
	}

	private void heatFluids() {

		//HeatIndex hi = MetalSnatcher.getHeatIndexFromMetal(tank.getAlloy());
		if (!tank.isEmpty()) {
			MetalStat stats = TFTechness.statMap.get(tank.getAlloy().name);
			float mass = stats.ingotMass * tank.getFluidAmount()
					/ MetalConfig.ingotFluidmB;
			// Total surface area of the fluid in the container (m^2 - 2 (top/bottom) * 4 (other sides) * height)
			float surfaceArea = 2 + 4 * Math.min(tank.getFluidAmount() / 1000f,
					1);
			float change = getTemperatureChange(tankFluidTemperature,
					internalTemperature,
					surfaceArea,
					stats.heat.specificHeat,
					mass);
			tankFluidTemperature += change;
			// amount of energy (joules) transfered into the fluid...
			float e = change * mass * stats.heat.specificHeat;
			// ...to calculate the effect on the internal temperature of the crucible
			internalTemperature -= e / (getMass() * getSpecificHeat());
		}
	}

	private void adjustTargetTemperature() {
		if (hasAugment(TFTAugments.RFFORGE_AUTO_TEMP, 1)) {
			if (tank.getFluidAmount() > 0) {
				float oldTarget = targetTemperature;
				targetTemperature = MetalSnatcher.getHeatIndexFromMetal(tank
						.getAlloy()).meltTemp
						+ TFTAugments.AUTO_TEMP_THRESHHOLD;
				if (oldTarget != targetTemperature) {
					sendModePacket();
				}
			}
		}

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		tank.readFromNBT(nbt.getCompoundTag("Tank"));
		tankFluidTemperature = nbt.getFloat("FluidTemperature");
		locked = nbt.getBoolean("Locked");
		FluidStack fs = FluidStack.loadFluidStackFromNBT(nbt);
		if (fs != null) {
			targetFluid = (FluidMoltenMetal) fs.getFluid();
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("Tank", tank.writeToNBT(new NBTTagCompound()));
		nbt.setFloat("FluidTemperature", tankFluidTemperature);
		nbt.setBoolean("Locked", locked);
		if (targetFluid != null) {
			(new FluidStack(targetFluid, 1)).writeToNBT(nbt);
		}
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
	protected SideConfig getSideConfig() {

		SideConfig cfg = new SideConfig();
		cfg.numConfig = 8;
		cfg.slotGroups = new int[][] { new int[0],
				{ 0, 1 },
				{ 2, 3 },
				{ 4 },
				{ 2, 3, 4 },
				{ 0 },
				{ 1 },
				{ 0, 1, 2, 3, 4 } };
		cfg.allowInsertionSide = new boolean[] { false,
				true,
				false,
				false,
				false,
				true,
				true,
				true };
		cfg.allowExtractionSide = new boolean[] { false,
				true,
				true,
				true,
				true,
				false,
				false,
				true };
		cfg.allowInsertionSlot = new boolean[] { true,
				true,
				false,
				false,
				false,
				false };
		cfg.allowExtractionSlot = new boolean[] { true,
				true,
				true,
				true,
				true,
				false };
		cfg.sideTex = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
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
	public PacketCoFHBase getGuiPacket() {
		PacketCoFHBase packet = super.getGuiPacket();
		packet.addFloat(tankFluidTemperature);

		packet.addBool(targetFluid != null);
		if (targetFluid != null) {
			packet.addFluidStack(new FluidStack(targetFluid, 1));
		}
		packet.addInt(tank.getFluids().size());

		for (FluidStackFloat fs : tank.getFluids()) {
			packet.addFluidStack(fs.getFluidStack());
		}
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
		tankFluidTemperature = packet.getFloat();
		if (packet.getBool()) {
			targetFluid = (FluidMoltenMetal) packet.getFluidStack().getFluid();
		}
		int amt = packet.getInt();

		tank.empty();
		for (int i = 0; i < amt; i++) {
			tank.setFluid(packet.getFluidStack());
		}
	}

	@Override
	public Object getGuiClient(InventoryPlayer inv) {
		return new GuiRFCrucible(inv, this);

	}

	@Override
	public Object getGuiServer(InventoryPlayer inv) {
		return new ContainerRFCrucible(inv, this);
	}

	public boolean isHotEnough() {
		return tank.getMeltTemp() < tankFluidTemperature || tank.isEmpty();
	}

	@Override
	public int getType() {
		return BlockTFTMachine.Types.RFCRUCIBLE.ordinal();
	}

	public FluidTankAlloy getAlloyTank() {
		return tank;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if (!canFill(from, resource.getFluid())) {
			return 0;
		}
		int amt = tank.fill(resource, doFill);
		if (doFill && amt > 0) {
			int prev = tank.getFluidAmount() - amt;
			if (prev == 0) {
				//If the tank was previously empty, internal fluid temperature is inherited from the incoming fluid
				tankFluidTemperature = resource.getFluid()
						.getTemperature(resource);
			} else {
				// Calculate the new temperature of the combined fluids
				float e = prev * internalTemperature + amt
						* resource.getFluid().getTemperature(resource);
				tankFluidTemperature = e / tank.getFluidAmount();

			}
		}
		return amt;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource,
			boolean doDrain) {
		if (resource.getFluid() != tank.getAlloyFluid().getFluid()) {
			return null;
		}
		return drain(from, resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (!isHotEnough()
				|| (from != ForgeDirection.UNKNOWN && sideCache[from.ordinal()] != 2)) {
			return null;
		}
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return tank.fill(new FluidStack(fluid, 1), false) == 1
				&& isHotEnough()
				&& (from == ForgeDirection.UNKNOWN || sideCache[from.ordinal()] == 1);
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return drain(from, new FluidStack(fluid, 1), false) != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] { tank.getInfo() };
	}

	@Override
	public PacketCoFHBase getModePacket() {
		PacketCoFHBase packet = super.getModePacket();
		packet.addBool(this.locked);
		return packet;
	}

	@Override
	protected void handleModePacket(PacketCoFHBase packet) {
		super.handleModePacket(packet);
		this.locked = packet.getBool();
		targetFluid = locked ? (FluidMoltenMetal) tank.getAlloyFluid()
				.getFluid() : null;
		markDirty();
		callNeighborTileChange();
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
		targetFluid = locked ? (FluidMoltenMetal) tank.getAlloyFluid()
				.getFluid() : null;
		this.sendModePacket();
	}

	public boolean getLocked() {

		return this.locked;
	}

	public FluidMoltenMetal getTargetFluid() {

		return this.targetFluid;
	}

}
