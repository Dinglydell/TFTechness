package dinglydell.tftechness.tech.experiment;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.DamageSource;
import dinglydell.techresearch.experiment.Experiment;
import dinglydell.techresearch.researchtype.ResearchType;
import dinglydell.techresearch.util.MapBuilder;
import dinglydell.tftechness.tech.researchtype.TFTResearchTypes;

public class TFTExperiments {
	public static Experiment anvil;
	public static Experiment move;
	public static Experiment swim;
	public static Experiment bucket;
	public static Map<DamageSource, Experiment> hurtMap = new HashMap<DamageSource, Experiment>();
	public static Experiment hurtGeneral;
	public static Experiment jump;

	public static void setupExperiments() {
		anvil = new Experiment("anvil", (new MapBuilder<ResearchType, Double>()
				.put(ResearchType.metallurgy, 10.0).put(ResearchType.smithing,
						20.0).getMap()));

		move = new Experiment("move",
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.motion, 20.0).getMap()));

		swim = new Experiment("swim", (new MapBuilder<ResearchType, Double>()
				.put(ResearchType.motion, 10.0)
				.put(TFTResearchTypes.fluidDynamics, 20.0).getMap()));

		bucket = new Experiment("bucket",
				(new MapBuilder<ResearchType, Double>()
						.put(TFTResearchTypes.fluidDynamics, 20.0).getMap()));

		jump = new Experiment("jump",
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.motion, 20.0).getMap()));
		hurtGeneral = new Experiment("hurt",
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.zoology, 20.0).getMap()));
		hurtMap.put(DamageSource.fall,
				new Experiment("hurtFall",
						(new MapBuilder<ResearchType, Double>()
								.put(ResearchType.zoology, 20.0)
								.put(ResearchType.motion, 5.0).getMap())));

		Experiment hot = new Experiment("hurtHot",
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.zoology, 20.0).getMap()));
		hurtMap.put(DamageSource.lava, hot);

		hurtMap.put(DamageSource.cactus,
				new Experiment("hurtCactus",
						(new MapBuilder<ResearchType, Double>()
								.put(ResearchType.zoology, 20.0).getMap())));

		hurtMap.put(DamageSource.inFire, hot);
		hurtMap.put(DamageSource.onFire, hot);
		hurtMap.put(DamageSource.starve,
				new Experiment("hurtStarve",
						(new MapBuilder<ResearchType, Double>()
								.put(ResearchType.zoology, 20.0).getMap())));
		Experiment suffocate = new Experiment("hurtSuffocate",
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.zoology, 20.0).getMap()));
		hurtMap.put(DamageSource.drown, suffocate);
		hurtMap.put(DamageSource.inWall, suffocate);

	}
}
