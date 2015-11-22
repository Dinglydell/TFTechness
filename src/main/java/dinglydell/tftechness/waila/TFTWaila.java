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
import dinglydell.tftechness.tileentities.machine.TileRFForge;
import dinglydell.tftechness.tileentities.machine.TileTemperature;
import dinglydell.tftechness.util.MathUtils;

public class TFTWaila implements IWailaDataProvider {
	public static final String baseTempString = TFC_ItemHeat.getHeatColor(0, Integer.MAX_VALUE);
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
		return null;
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		TileEntity te = accessor.getTileEntity();
		if (te instanceof TileTemperature) {
			float temp = accessor.getNBTData().getFloat("Temperature");
			String tmpString = TFC_ItemHeat.getHeatColor(temp, Integer.MAX_VALUE);
			if (tmpString.equals(baseTempString)) {
				currenttip.add(MathUtils.roundTo(temp, 2) + TFTechness.degrees + "C");
			} else {
				currenttip.add(tmpString + " (" + (int) MathUtils.roundTo(temp, 0) + TFTechness.degrees + "C)");
			}
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
			IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return currenttip;
	}
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x,
			int y, int z) {
		if (te instanceof TileTemperature) {
			tag.setFloat("Temperature", ((TileTemperature) te).getTemperature());
		}
		return tag;
	}
	
	public static void callbackRegister(IWailaRegistrar reg) {
		reg.registerBodyProvider(new TFTWaila(), TileRFForge.class);
		reg.registerNBTProvider(new TFTWaila(), TileRFForge.class);
		reg.registerBodyProvider(new TFTWaila(), TileCryoChamber.class);
		reg.registerNBTProvider(new TFTWaila(), TileCryoChamber.class);
	}
	
}
