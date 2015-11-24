package dinglydell.tftechness.fluid;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/** Used to slowly drain fluid alloys */
public class FluidStackFloat {
	protected Fluid fluid;
	public float amount;

	public FluidStackFloat(Fluid fluid, float amount) {
		this.fluid = fluid;
		this.amount = amount;
	}

	public Fluid getFluid() {
		return fluid;
	}

	/** Gets the nearest FluidStack approximation */
	public FluidStack getFluidStack() {
		return new FluidStack(fluid, (int) amount);
	}

	public NBTBase writeToNBT(NBTTagCompound nbt) {
		nbt.setString("FluidName", FluidRegistry.getFluidName(getFluid()));
		nbt.setFloat("Amount", amount);
		return nbt;
	}

	public static FluidStackFloat loadFluidStackFloatFromNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			return null;
		}
		String fluidName = nbt.getString("FluidName");

		if (fluidName == null || FluidRegistry.getFluid(fluidName) == null) {
			return null;
		}
		return new FluidStackFloat(FluidRegistry.getFluid(fluidName),
				nbt.getFloat("Amount"));
	}

	public FluidStackFloat copy() {
		return new FluidStackFloat(fluid, amount);
	}
}
