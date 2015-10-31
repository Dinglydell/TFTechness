package dinglydell.tftechness.recipe;

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
import dinglydell.tftechness.Material;
import dinglydell.tftechness.TFTechness;

public class AnvilRecipeHandler {
	
	public long wSeed = Long.MIN_VALUE;
	
	@SubscribeEvent(receiveCanceled = true)
	public void onServerWorldTick(WorldEvent.Load event) {
		World world = event.world;
		if (world.provider.dimensionId == 0 && world.getWorldInfo().getSeed() != wSeed) {
			AnvilManager.world = world;
			addRecipes();
			wSeed = world.getWorldInfo().getSeed();
		}
	}
	
	private void addRecipes() {
		AnvilManager manager = AnvilManager.getInstance();
		manager.addPlan("gear", new PlanRecipe(new RuleEnum[] {
				RuleEnum.UPSETANY, RuleEnum.BENDANY, RuleEnum.PUNCHLAST
		}));
		for (Material mat : TFTechness.materials) {
			if (!mat.gearOnly) {
				addDoubleIngotRecipe(manager, mat.ingot, mat.ingot2x, mat.tier);
				TFTechness.logger.info(mat.metal);
				addSheetRecipe(manager, mat.ingot2x, mat.sheet, mat.tier);
				addDoubleSheetRecipe(manager, mat.sheet, mat.sheet2x, mat.tier);
			}
			if (RecipeConfig.gearsEnabled) {
				addGearRecipe(manager, mat.sheet2x, mat.gear, mat.tier);
			}
		}
	}
	
	private void addDoubleIngotRecipe(AnvilManager manager, Item ingot, Item ingot2x, int tier) {
		// Double ingots require an anvil 1 tier lower than the material tier.
		AnvilReq req = AnvilReq.getReqFromInt(tier - 1);
		
		manager.addWeldRecipe(new AnvilRecipe(new ItemStack(ingot),
				new ItemStack(ingot),
				req,
				new ItemStack(ingot2x, 1)));
	}
	
	private void addSheetRecipe(AnvilManager manager, Item ingot2x, Item sheet, int tier) {
		AnvilReq req = AnvilReq.getReqFromInt(tier);
		TFTechness.logger.info(ingot2x.getUnlocalizedName());
		TFTechness.logger.info(sheet.getUnlocalizedName());
		TFTechness.logger.info(tier);
		manager.addRecipe(new AnvilRecipe(new ItemStack(ingot2x), null, "sheet", req, new ItemStack(sheet)));
		
	}
	
	private void addDoubleSheetRecipe(AnvilManager manager, Item sheet, Item sheet2x, int tier) {
		addDoubleIngotRecipe(manager, sheet, sheet2x, tier);
		
	}
	
	private void addGearRecipe(AnvilManager manager, Item material, ItemStack gear, int tier) {
		// Whichever is largest - the material tier or tier 3 (wrought iron)
		AnvilReq req = AnvilReq.getReqFromInt(Math.max(3, tier));
		manager.addRecipe(new AnvilRecipe(new ItemStack(material),
				new ItemStack(TFCItems.wroughtIronIngot),
				"gear",
				req,
				gear));
	}
	
}
