package dinglydell.tftechness.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Crafting.AnvilManager;
import com.bioxx.tfc.api.Crafting.AnvilRecipe;
import com.bioxx.tfc.api.Crafting.AnvilReq;
import com.bioxx.tfc.api.Crafting.PlanRecipe;
import com.bioxx.tfc.api.Enums.RuleEnum;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dinglydell.tftechness.TFTechness;
import dinglydell.tftechness.block.BlockTankFrame;
import dinglydell.tftechness.config.RecipeConfig;
import dinglydell.tftechness.metal.Material;
import dinglydell.tftechness.metal.TankMap;

public class AnvilRecipeHandler {

	public long wSeed = Long.MIN_VALUE;
	public static final String gearPlan = "gear";
	public static final String tankPlan = "tank";
	public static final String rodPlan = "rod";
	public static final String sheetPlan = "sheet";

	@SubscribeEvent(receiveCanceled = true)
	public void onServerWorldTick(WorldEvent.Load event) {
		World world = event.world;
		long newSeed = world.getWorldInfo().getSeed();
		if (world.provider.dimensionId == 0 && newSeed != wSeed && newSeed != 0) {
			AnvilManager.world = world;
			addRecipes();
			wSeed = world.getWorldInfo().getSeed();
		}
	}

	private void addRecipes() {
		TFTechness.logger.info("Adding anvil recipes.");
		AnvilManager manager = AnvilManager.getInstance();
		manager.addPlan(gearPlan, new PlanRecipe(
				new RuleEnum[] { RuleEnum.UPSETANY,
						RuleEnum.BENDANY,
						RuleEnum.PUNCHLAST }));
		manager.addPlan(tankPlan, new PlanRecipe(
				new RuleEnum[] { RuleEnum.DRAWTHIRDFROMLAST,
						RuleEnum.BENDSECONDFROMLAST,
						RuleEnum.PUNCHLAST }));
		manager.addPlan(rodPlan, new PlanRecipe(
				new RuleEnum[] { RuleEnum.HITLAST,
						RuleEnum.BENDSECONDFROMLAST,
						RuleEnum.BENDLAST }));
		for (Material mat : TFTechness.materials) {
			if (!mat.gearOnly) {
				addDoubleIngotRecipe(manager, mat.ingot, mat.ingot2x, mat.tier);
				addSheetRecipe(manager, mat.ingot2x, mat.sheet, mat.tier);
				addDoubleSheetRecipe(manager, mat.sheet, mat.sheet2x, mat.tier);
			}
			addRodRecipe(manager, mat.rod, mat.ingot, mat.tier);
			if (RecipeConfig.gearsEnabled && mat.gear != null) {
				addGearRecipe(manager, mat.sheet2x, mat.gear, mat.tier);
			}
		}
		if (RecipeConfig.tanksEnabled) {
			addTankRecipes(manager);
		}
	}

	private void addRodRecipe(AnvilManager manager, Item rod, Item ingot,
			int tier) {
		AnvilReq req = AnvilReq.getReqFromInt(tier);
		manager.addRecipe(new AnvilRecipe(new ItemStack(ingot), null, rodPlan,
				req, new ItemStack(rod)));

	}

	private void addTankRecipes(AnvilManager manager) {
		TankMap[] tanks = TFTechness.tankMap;
		for (int i = 0; i < tanks.length; i++) {
			TankMap t = tanks[i];
			ItemStack unf = BlockTankFrame.getItemStack(t.type,
					BlockTankFrame.Stages.unfinished);
			ItemStack frame = BlockTankFrame.getItemStack(t.type,
					BlockTankFrame.Stages.frame);
			// If the anvil requirement for the recipe involving lead is smaller than lead tier, use
			// lead tier
			AnvilReq leadReq = AnvilReq.getReqFromInt(Math.max(t.req.Tier,
					TFTechness.materialMap.get("Lead").tier));
			// Frame is made from unfinished + lead ingot
			manager.addWeldRecipe(new AnvilRecipe(unf, new ItemStack(
					TFCItems.leadIngot, 1), leadReq, frame));
			// // (old) Finished tank is made from frame + glass block
			// manager.addWeldRecipe(new AnvilRecipe(frame, new ItemStack(Blocks.glass, 1), t.req,
			// t.finished));
			if (i == 0) {
				// basic tier unfinished is made from working a sheet
				manager.addRecipe(new AnvilRecipe(t.sheet2x, null, tankPlan,
						t.req, unf));
			} else {
				TankMap prev = tanks[i - 1];

				ItemStack prevUnf = BlockTankFrame.getItemStack(prev.type,
						BlockTankFrame.Stages.unfinished);
				ItemStack prevFrame = BlockTankFrame.getItemStack(prev.type,
						BlockTankFrame.Stages.frame);
				// non-basic tier unfinished is made from previous tier unfinished + sheet
				manager.addWeldRecipe(new AnvilRecipe(prevUnf, t.sheet2x,
						t.req, unf));
				if (i == 1) {
					// 2nd tier unfinished can also be made from sheet + previous tier sheet
					manager.addRecipe(new AnvilRecipe(t.sheet2x, prev.sheet2x,
							tankPlan, t.req, unf));

				}

				// non-basic tier frame can be made from previous tier frame + sheet
				manager.addWeldRecipe(new AnvilRecipe(prevFrame, t.sheet2x,
						t.req, frame));
				// // (old) non-basic tier finished can be made from previous tier finished + sheet
				// manager.addWeldRecipe(new AnvilRecipe(prev.finished, t.sheet2x, t.req,
				// t.finished));
			}

		}

	}

	private void addDoubleIngotRecipe(AnvilManager manager, Item ingot,
			Item ingot2x, int tier) {
		// Double ingots require an anvil 1 tier lower than the material tier.
		AnvilReq req = AnvilReq.getReqFromInt(tier - 1);

		manager.addWeldRecipe(new AnvilRecipe(new ItemStack(ingot),
				new ItemStack(ingot), req, new ItemStack(ingot2x, 1)));
	}

	private void addSheetRecipe(AnvilManager manager, Item ingot2x, Item sheet,
			int tier) {
		AnvilReq req = AnvilReq.getReqFromInt(tier);
		manager.addRecipe(new AnvilRecipe(new ItemStack(ingot2x), null,
				sheetPlan, req, new ItemStack(sheet)));

	}

	private void addDoubleSheetRecipe(AnvilManager manager, Item sheet,
			Item sheet2x, int tier) {
		addDoubleIngotRecipe(manager, sheet, sheet2x, tier);

	}

	private void addGearRecipe(AnvilManager manager, Item material,
			ItemStack gear, int tier) {
		// Whichever is largest - the material tier or tier 3 (wrought iron)
		ArrayList<Item> centralItem = new ArrayList<Item>();
		int gearTier = Math.max(3, tier);
		switch (gearTier) {
		case 3:
			centralItem.add(TFCItems.wroughtIronIngot);
		case 4:
			centralItem.add(TFCItems.steelIngot);
		case 5:
			centralItem.add(TFCItems.blackSteelIngot);

		default:
			centralItem.add(TFCItems.redSteelIngot);
			centralItem.add(TFCItems.blueSteelIngot);
		}
		AnvilReq req = AnvilReq.getReqFromInt(gearTier);
		for (Item it : centralItem) {
			manager.addRecipe(new AnvilRecipe(new ItemStack(material),
					new ItemStack(it), gearPlan, req, gear));
		}
	}

	public static ItemStack getResultFromPlan(String plan) {
		AnvilManager manager = AnvilManager.getInstance();
		List<AnvilRecipe> recipes = manager.getRecipeList();
		for (AnvilRecipe r : recipes) {
			if (plan.equals(r.plan)) {
				return r.getCraftingResult();
			}
		}
		return null;
	}
}
