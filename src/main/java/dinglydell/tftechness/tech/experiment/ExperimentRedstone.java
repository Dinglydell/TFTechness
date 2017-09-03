package dinglydell.tftechness.tech.experiment;

import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import dinglydell.techresearch.PlayerTechDataExtendedProps;
import dinglydell.techresearch.experiment.ExperimentNotebook;
import dinglydell.techresearch.experiment.PlayerExperimentData;
import dinglydell.techresearch.researchtype.ResearchType;

public class ExperimentRedstone extends ExperimentNotebook {

	public ExperimentRedstone(String name, Map<ResearchType, Double> baseValue) {
		super(name, baseValue);

	}

	@Override
	public PlayerExperimentData getData(NBTTagCompound expTag) {

		return new PlayerExperimentDataRedstone(expTag);
	}

	@Override
	public PlayerExperimentData getBlankData() {

		return new PlayerExperimentDataRedstone();
	}

	@Override
	protected int getUses(PlayerTechDataExtendedProps ptdep, BlockState context) {
		if (!ptdep.getExperiments().containsKey(this)) {
			return 0;
		}
		PlayerExperimentDataRedstone edr = (PlayerExperimentDataRedstone) ptdep
				.getExperiments().get(this);
		int meta = context.world.getBlockMetadata(context.x,
				context.y,
				context.z);
		return edr.stateUses[meta];
		//return super.getUses(ptdep, context);
	}
}
