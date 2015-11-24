package dinglydell.tftechness.gui.element;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import cofh.lib.gui.GuiBase;
import cofh.lib.util.helpers.StringHelper;
import dinglydell.tftechness.fluid.FluidStackFloat;
import dinglydell.tftechness.fluid.FluidTankAlloy;

public class ElementFluidTankAlloy extends ElementFluidTankMixed {

	public ElementFluidTankAlloy(GuiBase gui, int x, int y, FluidTankAlloy tank) {
		super(gui, x, y, tank);

	}

	@Override
	public void addTooltip(List<String> list) {
		int amt = tank.getFluidAmount();
		if (amt == 0) {
			super.addTooltip(list);
			return;
			// list.add(StringHelper.localize(""));
		} else {
			list.add(StringHelper.getFluidName(((FluidTankAlloy) tank)
					.getAlloy()));
		}

		list.add("" + amt + " / " + tank.getCapacity() + " mB");
		for (FluidStackFloat fsf : tank.getFluids()) {
			FluidStack fs = fsf.getFluidStack();
			list.add("  " + StringHelper.getFluidName(fs) + ": " + fs.amount
					+ " mB" + " (" + (fs.amount * 100 / amt) + "%)");
		}
	}

}
