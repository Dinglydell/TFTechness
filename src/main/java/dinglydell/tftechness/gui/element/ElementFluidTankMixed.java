package dinglydell.tftechness.gui.element;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.gui.element.ElementFluidTank;
import cofh.lib.render.RenderHelper;
import cofh.lib.util.helpers.StringHelper;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.fluid.FluidTankMixed;

public class ElementFluidTankMixed extends ElementBase {
	
	protected FluidTankMixed tank;
	public static final String mixedTankUnlocalised = "info." + TFTechness.MODID + ".tank.mixed.name";
	public static final int scale = 50;
	
	public ElementFluidTankMixed(GuiBase gui, int x, int y, FluidTankMixed tank) {
		super(gui, x, y, 16, scale);
		this.texture = ElementFluidTank.DEFAULT_TEXTURE;
		this.texW = 64;
		this.texH = 64;
		this.tank = tank;
	}
	
	@Override
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {
		int y = posY + sizeY;
		for (FluidStack fs : tank.getFluids()) {
			int amt = getScaledAmount(fs);
			gui.drawFluid(posX, y -= amt, fs, sizeX, amt);
			RenderHelper.bindTexture(texture);
			GL11.glScalef(1, scale / (float) ElementFluidTank.DEFAULT_SCALE, 1);
			drawTexturedModalRect(posX, posY, 32, 1, sizeX, sizeY);
			GL11.glScalef(1, (float) ElementFluidTank.DEFAULT_SCALE / scale, 1);
		}
		
	}
	
	private int getScaledAmount(FluidStack fs) {
		if (tank.getCapacity() == 0) {
			return sizeY;
		}
		return (int) sizeY * (1 - fs.amount / tank.getCapacity());
	}
	
	@Override
	public void drawForeground(int arg0, int arg1) {
		
	}
	
	@Override
	public void addTooltip(List<String> list) {
		list.add(StringHelper.localize(mixedTankUnlocalised));
		int amt = tank.getFluidAmount();
		list.add("" + amt + " / " + tank.getCapacity() + " mB");
		for (FluidStack fs : tank.getFluids()) {
			list.add("  " + StringHelper.getFluidName(fs) + ": " + fs.amount + " mB" + " (" + (fs.amount * 100 / amt)
					+ "%)");
		}
	}
	
}
