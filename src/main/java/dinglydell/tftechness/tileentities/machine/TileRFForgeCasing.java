package dinglydell.tftechness.tileentities.machine;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileRFForgeCasing extends TileEntity implements IRFForgeMultiBlock {

	private boolean hasMaster;
	private boolean isMaster;
	private int masterX;
	private int masterY;
	private int masterZ;

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		hasMaster = nbt.getBoolean("HasMaster");
		isMaster = nbt.getBoolean("IsMaster");
		masterX = nbt.getInteger("MasterX");
		masterY = nbt.getInteger("MasterY");
		masterZ = nbt.getInteger("MasterZ");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("HasMaster", hasMaster);
		nbt.setBoolean("IsMaster", isMaster);
		nbt.setInteger("MasterX", masterX);
		nbt.setInteger("MasterY", masterY);
		nbt.setInteger("MasterZ", masterZ);
	}

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
		return worldObj.getTileEntity(masterX, masterY, masterZ) instanceof TileRFForge;
	}

	@Override
	public void reset() {
		setHasMaster(false);
		setIsMaster(false);
		setMasterCoords(0, 0, 0);

	}

}
