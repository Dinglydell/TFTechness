package dinglydell.tftechness.tech.experiment;

import dinglydell.techresearch.experiment.Experiment;
import dinglydell.techresearch.researchtype.ResearchType;
import dinglydell.techresearch.util.MapBuilder;

public class TFTExperiments {
	public static Experiment anvil;

	public static void setupExperiments() {
		anvil = new Experiment("anvil", (new MapBuilder<ResearchType, Double>()
				.put(ResearchType.metallurgy, 10.0).put(ResearchType.smithing,
						20.0).getMap()));
	}
}
