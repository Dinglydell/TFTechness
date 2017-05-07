package dinglydell.tftechness.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import cofh.core.gui.GuiBaseAdv;
import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;

import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.gui.element.ElementButtonItem;

public class GuiRFAnvilPlanSelection extends GuiBaseAdv {
	public static final ResourceLocation TEXTURE = new ResourceLocation(
			TFTechness.MODID + ":textures/gui/machine/RFAnvilPlanSelect.png");
	private IPlanHandler planHandler;
	private Map<String, ItemStack> plans;
	private EntityPlayer player;

	public GuiRFAnvilPlanSelection(IPlanHandler planHandler, EntityPlayer player) {
		super(new ContainerRFAnvilPlanSelection(planHandler), TEXTURE);
		this.planHandler = planHandler;
		xSize = 256;
		drawInventory = false;
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();

		plans = getRecipes();

		int margin = 8;
		int xOffset = margin;
		int yOffset = margin;
		for (Entry<String, ItemStack> p : plans.entrySet()) {

			((ElementButtonItem) addElement(new ElementButtonItem(this,
					xOffset, yOffset, p.getKey(), p.getValue())))
					.setTooltip(StringHelper.localize("gui.plans." + p.getKey()));
			xOffset += 20;
			if (xOffset >= (xSize - margin)) {
				xOffset = margin;
				yOffset += 20;
			}

		}
	}

	private Map<String, ItemStack> getRecipes() {
		AnvilManager manager = AnvilManager.getInstance();
		Map<String, ItemStack> plans = new HashMap<String, ItemStack>();
		List<AnvilRecipe> recipes = manager.getRecipeList();
		for (AnvilRecipe r : recipes) {
			if (r.plan != null && !r.plan.equals("")
					&& !plans.containsKey(r.plan)) {
				ItemStack result = planHandler.getResult(r.plan);
				if (result == null) {
					plans.put(r.plan, r.getCraftingResult());
				} else {
					plans.put(r.plan, result);
				}

			}
		}
		return plans;
	}

	public void handleElementButtonClick(String p, int paramInt) {
		playSound("random.click", 1.0F, 1.0F);
		planHandler.setPlan(p);
		planHandler.openGui(player);

	}

}
