package dinglydell.tftechness.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.tileentities.machine.TileCryoChamber;
import dinglydell.tftechness.tileentities.machine.TileRFAnvil;
import dinglydell.tftechness.tileentities.machine.TileRFCrucible;
import dinglydell.tftechness.tileentities.machine.TileRFForge;
import dinglydell.tftechness.tileentities.machine.TileTemperature;
import dinglydell.tftechness.tileentities.machine.TileTemperatureControl;
import dinglydell.tftechness.util.MathUtils;

public class TFTWaila implements IWailaDataProvider {
	public static final String baseTempString = TFC_ItemHeat.getHeatColor(0,
			Integer.MAX_VALUE);

	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return currenttip;
	}

	// TODO localiszation
	@Override
	public List<String> getWailaBody(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();
		if (te instanceof TileTemperature) {
			float temp = accessor.getNBTData().getFloat("Temperature");
			addTemperature(currenttip, temp);
		}
		if (te instanceof TileTemperatureControl) {
			float target = accessor.getNBTData().getFloat("TargetTemperature");
			addTemperature(currenttip, target, "Target: ");
		}
		if (te instanceof TileRFAnvil) {
			currenttip.add("Tier: " + accessor.getNBTData().getInteger("Tier"));
		}
		return currenttip;
	}

	private void addTemperature(List<String> currenttip, float temp,
			String prefix) {
		String tmpString = TFC_ItemHeat.getHeatColor(temp, Integer.MAX_VALUE);
		if (tmpString.equals(baseTempString)) {
			currenttip.add(prefix + MathUtils.roundTo(temp, 2)
					+ TFTechness.degrees + "C");
		} else {
			currenttip.add(prefix + tmpString + " ("
					+ (int) MathUtils.roundTo(temp, 0) + TFTechness.degrees
					+ "C)");
		}

	}

	private void addTemperature(List<String> currenttip, float temp) {
		addTemperature(currenttip, temp, "");

	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack,
			List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te,
			NBTTagCompound tag, World world, int x, int y, int z) {
		if (te instanceof TileTemperature) {
			tag.setFloat("Temperature", ((TileTemperature) te).getTemperature());
		}
		if (te instanceof TileTemperatureControl) {
			tag.setFloat("TargetTemperature",
					((TileTemperatureControl) te).targetTemperature);
		}
		if (te instanceof TileRFAnvil) {
			tag.setInteger("Tier", ((TileRFAnvil) te).getTier());
		}
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg) {
		reg.registerBodyProvider(new TFTWaila(), TileRFForge.class);
		reg.registerNBTProvider(new TFTWaila(), TileRFForge.class);
		reg.registerBodyProvider(new TFTWaila(), TileRFCrucible.class);
		reg.registerNBTProvider(new TFTWaila(), TileRFCrucible.class);
		reg.registerBodyProvider(new TFTWaila(), TileCryoChamber.class);
		reg.registerNBTProvider(new TFTWaila(), TileCryoChamber.class);
		reg.registerBodyProvider(new TFTWaila(), TileRFAnvil.class);
		reg.registerNBTProvider(new TFTWaila(), TileRFAnvil.class);

	}

}
