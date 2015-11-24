package dinglydell.tftechness.fluid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/** A fluid tank that allows a for a mixture of fluids in one tank */
public class FluidTankMixed {
	
	protected int capacity;
	protected Map<Fluid, FluidStack> contents = new HashMap<Fluid, FluidStack>();
	
	public FluidTankMixed(int capacity) {
		this.capacity = capacity;
	}
	
	public int fill(FluidStack fluid, boolean doFill) {
		int quantity = fluid.amount;
		int amt = getFluidAmount();
		if (amt + quantity > capacity) {
			quantity = capacity - amt;
		}
		if (!doFill) {
			return quantity;
		}
		if (contents.containsKey(fluid.getFluid())) {
			contents.get(fluid.getFluid()).amount += quantity;
		} else {
			fluid.copy();
			contents.put(fluid.getFluid(), new FluidStack(fluid.getFluid(), quantity));
		}
		return quantity;
	}
	
	/** Drains from all fluids proportionally. Returns the Fluid that was drained.
	 * 
	 * Due to integer rounding, the total fluid drained will likely be slightly lower than maxDrain */
	public Map<Fluid, FluidStack> drainAll(int maxDrain, boolean doDrain) {
		if (contents.isEmpty()) {
			return new HashMap<Fluid, FluidStack>();
		}
		Map<Fluid, FluidStack> drained = new HashMap<Fluid, FluidStack>();
		
		int total = getFluidAmount();
		maxDrain = Math.min(total, maxDrain);
		for (FluidStack f : getFluids()) {
			int amt = f.amount / total * maxDrain;
			drained.put(f.getFluid(), new FluidStack(f.getFluid(), amt));
			if (doDrain) {
				f.amount -= amt;
			}
		}
		return drained;
	}
	
	/** Drains the tank of a specific fluid and returns the fluid that was drained. */
	public FluidStack drain(FluidStack drain, boolean doDrain) {
		FluidStack fs = contents.get(drain.getFluid());
		int amt = Math.min(drain.amount, fs.amount);
		if (doDrain) {
			fs.amount -= amt;
		}
		return new FluidStack(fs.getFluid(), amt);
	}
	
	/** The total quantity of all fluids in the tank */
	public int getFluidAmount() {
		if (contents.isEmpty()) {
			return 0;
		}
		int amt = 0;
		for (FluidStack fs : getFluids()) {
			amt += fs.amount;
		}
		return amt;
	}
	
	/** The total number of fluids in the tank */
	public int getNumFluids() {
		return contents.size();
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public Collection<FluidStack> getFluids() {
		return contents.values();
	}
	
	/** The "dominant" fluid in the tank - the fluid with the highest quantity */
	public FluidStack getDominantFluid() {
		FluidStack dom = null;
		for (FluidStack fs : contents.values()) {
			if (dom == null || fs.amount > dom.amount) {
				dom = fs;
			}
		}
		return dom;
	}
	
	/** Empties the tank of all fluids and returns the old contents */
	public Map<Fluid, FluidStack> empty() {
		Map<Fluid, FluidStack> m = contents;
		contents = new HashMap();
		return m;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		empty();
		NBTTagList tags = nbt.getTagList("Fluids", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound fluid = tags.getCompoundTagAt(i);
			FluidStack fs = FluidStack.loadFluidStackFromNBT(fluid);
			if (fs != null) {
				contents.put(fs.getFluid(), fs);
			}
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList tags = new NBTTagList();
		for (FluidStack fs : getFluids()) {
			tags.appendTag(fs.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("Fluids", tags);
		return nbt;
	}
	
	/** Forcidly sets the amount of a fluid in the tank */
	public void setFluid(FluidStack fs) {
		contents.put(fs.getFluid(), fs.copy());
	}
	
}
