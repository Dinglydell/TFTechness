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

import com.bioxx.tfc.api.HeatIndex;

import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.fluid.FluidStackFloat;
import dinglydell.tftechness.fluid.FluidTankAlloy;
import dinglydell.tftechness.gui.GuiRFCrucible;
import dinglydell.tftechness.gui.container.ContainerRFCrucible;
import dinglydell.tftechness.item.TFTAugments;
import dinglydell.tftechness.metal.MetalSnatcher;

public class TileRFCrucible extends TileTemperatureControl implements
		IFluidHandler {

	protected static final int[] tankCapacity = { 4000, 8000, 16000, 32000 };
	protected FluidTankAlloy tank = new FluidTankAlloy(tankCapacity[0]);

	public TileRFCrucible() {
		super();
		inventory = new ItemStack[2];
	}

	@Override
	public void updateEntity() {
		super.updateEntity();
		if (!ServerHelper.isClientWorld(this.worldObj)) {
			adjustTargetTemperature();
			handleMoldOutput(0, tank);
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
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setTag("Tank", tank.writeToNBT(new NBTTagCompound()));
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
		cfg.numConfig = 4;
		cfg.slotGroups = new int[][] { new int[0], { 0 }, { 1 }, { 0, 1 } };
		cfg.allowInsertionSide = new boolean[] { false, true, false, true };
		cfg.allowExtractionSide = new boolean[] { false, true, true, true };
		cfg.allowInsertionSlot = new boolean[] { true, false, false };
		cfg.allowExtractionSlot = new boolean[] { true, true, false };
		cfg.sideTex = new int[] { 0, 1, 4, 7 };
		cfg.defaultSides = new byte[] { 1, 1, 2, 2, 2, 2 };
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
		packet.addInt(tank.getFluids().size());
		for (FluidStackFloat fs : tank.getFluids()) {
			packet.addFluidStack(fs.getFluidStack());
		}
		return packet;

	}

	@Override
	public void handleGuiPacket(PacketCoFHBase packet) {
		super.handleGuiPacket(packet);
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

	protected boolean isHotEnough() {
		HeatIndex hi = MetalSnatcher.getHeatIndexFromMetal(tank.getAlloy());
		return hi.meltTemp < internalTemperature;
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
		return tank.fill(resource, doFill);
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

}
