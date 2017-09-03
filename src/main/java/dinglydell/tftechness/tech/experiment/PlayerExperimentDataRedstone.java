package dinglydell.tftechness.tech.experiment;

import net.minecraft.nbt.NBTTagCompound;
import dinglydell.techresearch.experiment.ExperimentNotebook.BlockState;
import dinglydell.techresearch.experiment.PlayerExperimentData;

public class PlayerExperimentDataRedstone extends
		PlayerExperimentData<BlockState> {

	public int[] stateUses = new int[16];

	public PlayerExperimentDataRedstone(NBTTagCompound data) {
		super(data);
		stateUses = data.getIntArray("uses_set");

	}

	public PlayerExperimentDataRedstone() {
		super();
	}

	@Override
	public NBTTagCompound getNBTData() {

		NBTTagCompound nbt = super.getNBTData();

		nbt.setIntArray("uses_set", stateUses);
		return nbt;
	}

	public PlayerExperimentData useExperiment(BlockState context) {
		int meta = context.world.getBlockMetadata(context.x,
				context.y,
				context.z);
		stateUses[meta]++;

		return super.useExperiment(context);
	}

}
