package dinglydell.tftechness.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cofh.core.gui.GuiBaseAdv;

import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.element.ElementButtonItem;

public class GuiRFAnvilPlanSelection extends GuiBaseAdv {
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFAnvil.png");
	private IPlanHandler planHandler;
	private Map<String, ItemStack> plans;

	public GuiRFAnvilPlanSelection(IPlanHandler planHandler) {
		super(new ContainerRFAnvilPlanSelection(planHandler), TEXTURE);
		this.planHandler = planHandler;
	}

	@Override
	public void initGui() {
		super.initGui();

		plans = getRecipes();
		int xOffset = 5;
		int yOffset = 14;
		for (Entry<String, ItemStack> p : plans.entrySet()) {

			addElement(new ElementButtonItem(this, guiLeft + xOffset, guiTop
					+ yOffset, p.getKey(), 16, 16, p.getValue()));
		}
	}

	private Map<String, ItemStack> getRecipes() {
		AnvilManager manager = AnvilManager.getInstance();
		Map<String, ItemStack> plans = new HashMap<String, ItemStack>();
		List<AnvilRecipe> recipes = manager.getRecipeList();
		for (AnvilRecipe r : recipes) {
			if (r.plan != null && !plans.containsKey(r.plan)) {
				plans.put(r.plan, r.getCraftingResult());
			}
		}
		return plans;
	}

	public void handleElementButtonClick(String p, int paramInt) {
		playSound("random.click", 1.0F, 1.0F);
		planHandler.setPlan(p);
	}

}
