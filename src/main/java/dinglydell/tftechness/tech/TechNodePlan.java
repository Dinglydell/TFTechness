package dinglydell.tftechness.tech;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cofh.lib.util.helpers.StringHelper;
import dinglydell.techresearch.researchtype.ResearchType;
import dinglydell.techresearch.techtree.TechNode;
import dinglydell.techresearch.techtree.TechNodeType;

/** A tech node that can restrict anvil plans */
public class TechNodePlan extends TechNode {

	public List<String> plans = new ArrayList<String>();

	public TechNodePlan(String id, TechNodeType type,
			Map<ResearchType, Double> costs) {
		super(id, type, costs);
		this.setUnlocalisedName("tech.tftechness." + id);
		this.setDecsription("tech.tftechness." + id + ".desc");
	}

	public TechNodePlan addPlan(String plan) {
		this.plans.add(plan);
		return this;

	}

	@Override
	public List<String> getUnlockedDisplay() {

		List<String> unlocked = super.getUnlockedDisplay();

		for (String p : plans) {
			unlocked.add(StringHelper.localize("gui.tftechness.anvilplan")
					+ ": " + StringHelper.localize("gui.plans." + p));
		}

		return unlocked;
	}

}
