package dinglydell.tftechness.tileentities.machine;

public interface IRFForgeMultiBlock {
	public boolean hasMaster();

	public boolean isMaster();

	public int getMasterX();

	public int getMasterY();

	public int getMasterZ();

	public void setHasMaster(boolean bool);

	public void setIsMaster(boolean bool);

	public void setMasterCoords(int x, int y, int z);

	public boolean checkForMaster();

	public void reset();
}
