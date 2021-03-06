package dinglydell.tftechness;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import mods.railcraft.api.crafting.RailcraftCraftingManager;
import mods.railcraft.common.blocks.anvil.BlockRCAnvil;
import mods.railcraft.common.blocks.machine.alpha.EnumMachineAlpha;
import mods.railcraft.common.blocks.machine.beta.EnumMachineBeta;
import mods.railcraft.common.carts.EnumCart;
import mods.railcraft.common.fluids.Fluids;
import mods.railcraft.common.items.ItemPlate.EnumPlate;
import mods.railcraft.common.items.RailcraftItem;
import mods.railcraft.common.items.RailcraftToolItems;
import mods.railcraft.common.util.steam.Steam;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.logging.log4j.LogManager;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cofh.core.util.crafting.RecipeAugmentable;
import cofh.lib.util.helpers.ItemHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermaldynamics.duct.TDDucts;
import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.dynamo.BlockDynamo;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.item.TEAugments;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper;
import cofh.thermalexpansion.util.FuelHandler;
import cofh.thermalexpansion.util.crafting.RecipeMachine;
import cofh.thermalexpansion.util.crafting.SmelterManager;
import cofh.thermalexpansion.util.crafting.SmelterManager.RecipeSmelter;
import cofh.thermalexpansion.util.crafting.TECraftingHandler;
import cofh.thermalexpansion.util.crafting.TransposerManager;
import cofh.thermalfoundation.fluid.TFFluids;
import cofh.thermalfoundation.item.TFItems;

import com.bioxx.tfc.TerraFirmaCraft;
import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Core.TFCTabs;
import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.Core.Metal.AlloyManager;
import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.Items.ItemIngot;
import com.bioxx.tfc.Items.ItemMeltedMetal;
import com.bioxx.tfc.Items.ItemTerra;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCBlocks;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Enums.EnumSize;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import dinglydell.techresearch.TechResearchSettings;
import dinglydell.techresearch.item.TRItems;
import dinglydell.techresearch.researchtype.ResearchType;
import dinglydell.techresearch.techtree.TechNode;
import dinglydell.techresearch.techtree.TechNode.TechNodeTier;
import dinglydell.techresearch.techtree.TechNodeType;
import dinglydell.techresearch.techtree.TechTree;
import dinglydell.techresearch.util.MapBuilder;
import dinglydell.tftechness.block.BlockRFForgeCasing;
import dinglydell.tftechness.block.BlockTFTMetalSheet;
import dinglydell.tftechness.block.BlockTankFrame;
import dinglydell.tftechness.block.ItemBlockTankFrame;
import dinglydell.tftechness.block.TFTBlocks;
import dinglydell.tftechness.block.dynamo.BlockTFTDynamo;
import dinglydell.tftechness.block.dynamo.ItemBlockTFTDynamo;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.block.machine.ItemBlockTFTMachine;
import dinglydell.tftechness.config.BucketConfig;
import dinglydell.tftechness.config.MachineConfig;
import dinglydell.tftechness.config.MetalConfig;
import dinglydell.tftechness.config.RecipeConfig;
import dinglydell.tftechness.fluid.TFTFluids;
import dinglydell.tftechness.gui.GuiHandlerTech;
import dinglydell.tftechness.gui.TFTGuiHandler;
import dinglydell.tftechness.item.ItemRod;
import dinglydell.tftechness.item.ItemTFTMetalSheet;
import dinglydell.tftechness.item.ItemTFTSteelBucket;
import dinglydell.tftechness.item.TFTAugments;
import dinglydell.tftechness.item.TFTItems;
import dinglydell.tftechness.item.TFTMeta;
import dinglydell.tftechness.metal.AlloyIngred;
import dinglydell.tftechness.metal.Material;
import dinglydell.tftechness.metal.MaterialAlloy;
import dinglydell.tftechness.metal.MetalStat;
import dinglydell.tftechness.metal.TFTMetals;
import dinglydell.tftechness.metal.TankMap;
import dinglydell.tftechness.recipe.AnvilRecipeHandler;
import dinglydell.tftechness.recipe.FurnaceRecipe;
import dinglydell.tftechness.recipe.RecipeShapelessUpgrade;
import dinglydell.tftechness.recipe.RemoveBatch;
import dinglydell.tftechness.recipe.TFTCraftingHandler;
import dinglydell.tftechness.render.item.RenderBucket;
import dinglydell.tftechness.tech.TechNodePlan;
import dinglydell.tftechness.tech.event.PlayerEventHandler;
import dinglydell.tftechness.tech.experiment.TFTExperiments;
import dinglydell.tftechness.tech.researchtype.TFTResearchTypes;
import dinglydell.tftechness.tileentities.TETFTMetalSheet;
import dinglydell.tftechness.tileentities.dynamo.TileTFTDynamoSteam;
import dinglydell.tftechness.tileentities.machine.TileCryoChamber;
import dinglydell.tftechness.tileentities.machine.TileRFAnvil;
import dinglydell.tftechness.tileentities.machine.TileRFCrucible;
import dinglydell.tftechness.tileentities.machine.TileRFForge;
import dinglydell.tftechness.tileentities.machine.TileRFForgeCasing;
import dinglydell.tftechness.tileentities.machine.TileTFTAccumulator;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;
import dinglydell.tftechness.tileentities.machine.TileTFTPrecipitator;
import dinglydell.tftechness.util.OreDict;

@Mod(modid = TFTechness.MODID, version = TFTechness.VERSION, dependencies = "required-after:terrafirmacraft;required-after:ThermalFoundation;required-after:ThermalExpansion;required-after:ThermalDynamics;required-after:BigReactors;required-after:techresearch;required-after:Railcraft")
public class TFTechness {
	public static final String MODID = "TFTechness";
	public static final String VERSION = "0.1";
	/** Base temperature of objects (22�C) */
	public static final int baseTemp = 22;
	public static final float rfToJoules = 6.275f;
	/** The degree symbol */
	public static final String degrees = "\u00b0";
	private static final int COKE_CREOSOTE = 500;
	private static final int COKE_COOK_TIME = 1800;
	public static org.apache.logging.log4j.Logger logger = LogManager
			.getLogger("TFTechness");
	public static Material[] materials;
	public static Map<String, MetalStat> statMap = new HashMap();
	public static Map<String, Material> materialMap;
	public static TankMap[] tankMap;

	private static TFTechness instance;

	public static TFTechness getInstance() {
		return instance;
	}

	public TFTechness() {
		instance = this;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initMaps();
		// nasty
		nastyHackFixes();

		readConfig(event);
		TFTMeta.preInit();
		addMetals();
		addBlocks();
		addItems();
		addRailcraftMachineRecipes();
		handleFules();
		registerRecipeTypes();
		setupTechResearch();
		// TFTechness.logger.info("LAVA: " +
		// FluidRegistry.LAVA.getTemperature());
	}

	private void addRailcraftMachineRecipes() {
		if (RailcraftToolItems.getCoalCoke() != null)
			RailcraftCraftingManager.cokeOven.addRecipe(TFTMeta.bituminousCoal,
					true,
					false,
					RailcraftToolItems.getCoalCoke(),
					Fluids.CREOSOTE.get(COKE_CREOSOTE),
					COKE_COOK_TIME);

	}

	private void setupTechResearch() {
		TechResearchSettings.disableDefaultTree();
		TechResearchSettings.disableDefaultExperiments();
		TechNodeType.RegisterType(new TechNodeType("prototype", 0x22B1C7,
				EnumChatFormatting.BLUE));
		setupTechTree();
		TFTExperiments.setupExperiments();
		setupTechEventHandlers();

	}

	private void setupTechTree() {
		//tier 0
		TechNodeTier.createNextTier(2);

		//tier 1
		TechNodeTier.createNextTier(3);

		//tier 2
		TechNodeTier.createNextTier(3);

		//tier 3
		TechNodeTier.createNextTier(3);

		//tier 4
		TechNodeTier.createNextTier(3);

		//tier 5
		TechNodeTier.createNextTier(3);

		TechNode notebook = TechTree.addTechNode(new TechNode("notebook",
				TechNodeType.types.get("application"),
				(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 10.0).getMap()))
				.addItemUnlocked(TRItems.notebook).setTier(TechNodeTier
						.getTier(1)));
		//metallurgy
		TechNode metallurgy = TechTree.addTechNode(ResearchType.metallurgy
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 20.0).getMap())
				.setTier(TechNodeTier.getTier(0)));

		//ferrous metallurgy 
		TechNode ferrousMetallurgy = TechTree
				.addTechNode(TFTResearchTypes.ferrousMetallurgy
						.generateTechNode(new MapBuilder<ResearchType, Double>()
								.put(ResearchType.metallurgy, 20.0).getMap())
						.setTier(TechNodeTier.getTier(1)));

		//smithing
		TechNode smithing = TechTree.addTechNode(ResearchType.smithing
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 20.0).getMap())
				.setTier(TechNodeTier.getTier(0)));

		TechNode motion = TechTree.addTechNode(ResearchType.motion
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 20.0).getMap())
				.setTier(TechNodeTier.getTier(0)));
		TechTree.addTechNode(ResearchType.science
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 20.0).getMap())
				.addRequirementAny(motion).addRequirementAny(metallurgy)
				.setTier(TechNodeTier.getTier(2)));
		//physics
		TechTree.addTechNode(ResearchType.physics
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.science, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));
		//biology
		TechTree.addTechNode(ResearchType.biology
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.science, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));

		TechTree.addTechNode(TFTResearchTypes.fluidDynamics
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.physics, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));

		TechTree.addTechNode(ResearchType.electrics
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.physics, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));

		TechTree.addTechNode(ResearchType.zoology
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.biology, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));
		TechTree.addTechNode(ResearchType.botany
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.biology, 20.0).getMap())
				.setTier(TechNodeTier.getTier(3)));

		TechTree.addTechNode(ResearchType.processing
				.generateTechNode(new MapBuilder<ResearchType, Double>()
						.put(ResearchType.research, 20.0).getMap())
				.addRequirementAny(smithing).setTier(TechNodeTier.getTier(1)));

		TechTree.addTechNode(new TechNodePlan("gears", TechNodeType.types
				.get("application"), new MapBuilder<ResearchType, Double>()
				.put(ResearchType.metallurgy, 5.0)
				.put(ResearchType.smithing, 5.0).put(ResearchType.motion, 5.0)
				.getMap()).addPlan(AnvilRecipeHandler.gearPlan)
				.setTier(TechNodeTier.getTier(2)));
		TechTree.addTechNode(new TechNodePlan("tanks", TechNodeType.types
				.get("application"), new MapBuilder<ResearchType, Double>()
				.put(ResearchType.metallurgy, 5.0)
				.put(ResearchType.smithing, 5.0).getMap())
				.addPlan(AnvilRecipeHandler.tankPlan).setTier(TechNodeTier
						.getTier(3)));

		TechNode coke = TechTree.addTechNode(new TechNode("coke",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>()
						.put(ResearchType.processing, 10.0).getMap())
				.addItemUnlocked(EnumMachineAlpha.COKE_OVEN.getItem())
				.setTier(TechNodeTier.getTier(2)));

		TechNode steam = new TechNode("steam",
				TechNodeType.types.get("theory"),
				new MapBuilder<ResearchType, Double>()
						.put(TFTResearchTypes.fluidDynamics, 10.0).getMap())
				.setTier(TechNodeTier.getTier(4));
		TechTree.addTechNode(steam);

		TechNode lowBoilers = TechTree
				.addTechNode(new TechNode("lowBoilers", TechNodeType.types
						.get("application"),
						new MapBuilder<ResearchType, Double>()
								.put(TFTResearchTypes.fluidDynamics, 10.0)
								.getMap())
						.addRequirementAll(steam)
						.addItemUnlocked(EnumMachineBeta.BOILER_FIREBOX_SOLID
								.getItem())
						.addItemUnlocked(EnumMachineBeta.BOILER_TANK_LOW_PRESSURE
								.getItem()).setTier(TechNodeTier.getTier(4)));

		TechTree.addTechNode(new TechNode("liquidFirebox", TechNodeType.types
				.get("application"), new MapBuilder<ResearchType, Double>()
				.put(TFTResearchTypes.fluidDynamics, 15.0).getMap())
				.addRequirementAll(lowBoilers)
				.addItemUnlocked(EnumMachineBeta.BOILER_FIREBOX_FLUID.getItem())
				.setTier(TechNodeTier.getTier(4)));

		TechNode highBoilers = TechTree.addTechNode(new TechNode("highBoilers",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>()
						.put(TFTResearchTypes.fluidDynamics, 10.0).getMap())
				.addRequirementAll(lowBoilers)
				.addItemUnlocked(EnumMachineBeta.BOILER_TANK_HIGH_PRESSURE
						.getItem()).setTier(TechNodeTier.getTier(4)));
		TechTree.addTechNode(new TechNode("steamLocomotive", TechNodeType.types
				.get("application"), new MapBuilder<ResearchType, Double>()
				.put(TFTResearchTypes.fluidDynamics, 10.0)
				.put(ResearchType.motion, 10.0).getMap())
				.addRequirementAll(highBoilers)
				.addItemUnlocked(EnumCart.LOCO_STEAM_SOLID.getCartItem())
				.setTier(TechNodeTier.getTier(4)));
		//TODO: Method of obtaining electrics
		TechNode induction = TechTree.addTechNode(new TechNode("induction",
				TechNodeType.types.get("theory"),
				new MapBuilder<ResearchType, Double>()
						.put(ResearchType.electrics, 10.0)
						.put(ResearchType.motion, 10.0).getMap())
				.setTier(TechNodeTier.getTier(4)));

		TechNode steamEngine = TechTree.addTechNode(new TechNode("steamEngine",
				TechNodeType.types.get("prototype"),
				new MapBuilder<ResearchType, Double>()
						.put(TFTResearchTypes.fluidDynamics, 10.0)
						.put(ResearchType.motion, 10.0)
						.put(ResearchType.electrics, 10.0).getMap())
				.addRequirementAll(induction)
				.addItemUnlocked(EnumMachineBeta.ENGINE_STEAM_HOBBY.getItem())
				.setTier(TechNodeTier.getTier(4)));

		TechNode turbines = TechTree.addTechNode(new TechNode("steamTurbine",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>()
						.put(TFTResearchTypes.fluidDynamics, 10.0)
						.put(ResearchType.motion, 10.0)
						.put(ResearchType.electrics, 10.0).getMap())
				.addRequirementAll(steamEngine)
				.addItemUnlocked(TFTMeta.brTurbineController)
				.addItemUnlocked(TFTMeta.brTurbineFluidPort)
				.addItemUnlocked(TFTMeta.brTurbineGlass)
				.addItemUnlocked(TFTMeta.brTurbineHousing)
				.addItemUnlocked(TFTMeta.brTurbineRotorBearing)
				.addItemUnlocked(TFTMeta.brTurbineRotorShaft)
				.addItemUnlocked(TFTMeta.brTurbineRotorBlade)
				.addItemUnlocked(TFTMeta.brTurbinePowerPort)
				.setTier(TechNodeTier.getTier(5)));

		//tracks
		TechNode woodTrack = TechTree.addTechNode(new TechNode("woodTrack",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>().put(ResearchType.motion,
						10.0).getMap()).addItemUnlocked(TFTMeta.trackWood)
				.addItemUnlocked(TFTMeta.trackWoodJunction)
				.addRequirementAll(coke).setTier(TechNodeTier.getTier(3)));

		//TFC
		TechNode bloomery = TechTree.addTechNode(new TechNode("bloomery",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>()
						.put(ResearchType.metallurgy, 10.0).getMap())
				.addItemUnlocked(new ItemStack(TFCBlocks.bloomery))
				.setTier(TechNodeTier.getTier(1)));

		TechNode crucible = TechTree.addTechNode(new TechNode("crucible",
				TechNodeType.types.get("application"),
				new MapBuilder<ResearchType, Double>()
						.put(ResearchType.metallurgy, 10.0).getMap())
				.addItemUnlocked(new ItemStack(TFCBlocks.crucible))
				.setTier(TechNodeTier.getTier(1)));

		TechNode blastFurnace = TechTree
				.addTechNode(new TechNode("blastFurnace", TechNodeType.types
						.get("application"),
						new MapBuilder<ResearchType, Double>()
								.put(TFTResearchTypes.ferrousMetallurgy, 10.0)
								.getMap()).addItemUnlocked(new ItemStack(
						TFCBlocks.blastFurnace)).setTier(TechNodeTier
						.getTier(2)));

		//		TechNode switches = TechTree.addTechNode(new TechNode("switches",
		//				TechNodeType.types.get("application"),
		//				new MapBuilder<ResearchType, Double>().put(ResearchType.motion,
		//						10.0).getMap())
		//						.addItemUnlocked(TFTMeta.trackWoodSwitch)
		//						.addItemUnlocked(TFTMeta.trackWye));
	}

	private void setupTechEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());

	}

	private void nastyHackFixes() {
		// nasty stuff happens here. Cover your eyes.
		replaceGuiHandler();
		replaceWater();
		fixWaterToSteam();
	}

	// this is possibly the worst atrocity I've ever committed
	private void replaceGuiHandler() {
		try {
			Field clientGuiHandlersField = NetworkRegistry.class
					.getDeclaredField("clientGuiHandlers");
			clientGuiHandlersField.setAccessible(true);
			Map<ModContainer, IGuiHandler> clientGuiHandlers = (Map<ModContainer, IGuiHandler>) clientGuiHandlersField
					.get(NetworkRegistry.INSTANCE);
			ModContainer tfc = null;
			for (Entry<ModContainer, IGuiHandler> entry : clientGuiHandlers
					.entrySet()) {
				if (entry.getKey().getMod() instanceof TerraFirmaCraft) {
					tfc = entry.getKey();
					break;
				}
			}
			clientGuiHandlers.put(tfc, new GuiHandlerTech());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void replaceWater() {

		try {
			Field water = FluidRegistry.class.getDeclaredField("WATER");
			finalField(water);
			water.set(null, TFCFluids.FRESHWATER);

			Field tag = Fluids.class.getDeclaredField("tag");
			tag.setAccessible(true);
			finalField(tag);
			tag.set(Fluids.WATER, "freshwater");

			// FluidRegistry.WATER = TFCFluids.FRESHWATER;

			// FluidContainerRegistry.isContainer(TFTMeta.salt);
			// Field fluidsField =
			// FluidRegistry.class.getDeclaredField("fluids");
			// fluidsField.setAccessible(true);
			// finalField(fluidsField);
			// BiMap<String, Fluid> fluids = (BiMap<String, Fluid>) fluidsField
			// .get(null);
			// fluids.forcePut("water", TFCFluids.FRESHWATER);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	private void finalField(Field field) throws Exception {
		Field modifiers = Field.class.getDeclaredField("modifiers");
		modifiers.setAccessible(true);
		modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	}

	private void fixWaterToSteam() {
		try {
			Field steamPerWater = Steam.class
					.getDeclaredField("STEAM_PER_UNIT_WATER");
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(steamPerWater, steamPerWater.getModifiers()
					& ~Modifier.FINAL);

			steamPerWater.set(null, 1);
			// Steam.STEAM_PER_UNIT_WATER = 1;
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Requires some TE init
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new TFTGuiHandler());
		registerModOreDict();
		addTanks();
		TFTFluids.createFluids();
		addBuckets(event);
		TFTAugments.init();
		registerTileEntities();

		// Waila
		FMLInterModComms.sendMessage("Waila",
				"register",
				"dinglydell.tftechness.waila.TFTWaila.callbackRegister");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new AnvilRecipeHandler());

		RemoveBatch batch = new RemoveBatch();

		removeTankRecipes(batch);
		removeCoilRecipes(batch);
		removeMachineRecipes(batch);

		removeBigReactorRecipes(batch);
		removeRailcraftRecipes(batch);
		for (Material m : materials) {
			removeGearRecipes(batch, m);
			removeNuggetIngotRecipes(batch, m);
		}
		batch.Execute();
		replaceMachineRecipes();
		addRecipes();

	}

	private void registerModOreDict() {
		OreDictionary.registerOre("plateIron",
				RailcraftItem.plate.getStack(EnumPlate.IRON));
		OreDictionary.registerOre("plateSteel",
				RailcraftItem.plate.getStack(EnumPlate.STEEL));
		OreDictionary.registerOre("plateTin",
				RailcraftItem.plate.getStack(EnumPlate.TIN));

		OreDictionary.registerOre("plateCopper",
				RailcraftItem.plate.getStack(EnumPlate.COPPER));
	}

	private void removeBigReactorRecipes(RemoveBatch batch) {
		batch.addCrafting(TFTMeta.brTurbineHousing);
		batch.addCrafting(TFTMeta.brTurbineController);
		batch.addCrafting(TFTMeta.brTurbinePowerPort);
		batch.addCrafting(TFTMeta.brTurbineFluidPort);

		batch.addCrafting(TFTMeta.brTurbineRotorBearing);
		batch.addCrafting(TFTMeta.brTurbineRotorBlade);
		batch.addCrafting(TFTMeta.brTurbineRotorShaft);

	}

	private void removeRailcraftRecipes(RemoveBatch batch) {
		//batch.addCrafting(new ItemStack(Items.minecart));
		batch.addCrafting(EnumMachineBeta.BOILER_TANK_LOW_PRESSURE.getItem());
		batch.addCrafting(EnumMachineBeta.BOILER_TANK_HIGH_PRESSURE.getItem());
		batch.addCrafting(BlockRCAnvil.getStack());
		batch.addCrafting(EnumMachineAlpha.BLAST_FURNACE.getItem());
		if (MachineConfig.bigReactorsOnly) {
			batch.addCrafting(EnumMachineBeta.ENGINE_STEAM_HIGH.getItem());
			batch.addCrafting(EnumMachineBeta.ENGINE_STEAM_HOBBY.getItem());
			batch.addCrafting(EnumMachineBeta.ENGINE_STEAM_LOW.getItem());
		}
	}

	@Mod.EventHandler
	public void loadComplete(FMLLoadCompleteEvent event) {

		// enderium & other pyrotheum recipes don't get registered until now
		if (RecipeConfig.replaceMachine) {
			replaceSmelterRecipes();
		}
	}

	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TETFTMetalSheet.class, "TFTMetalSheet");

		GameRegistry.registerTileEntity(TileTFTExtruder.class, "Extruder");

		GameRegistry
				.registerTileEntity(TileTFTAccumulator.class, "Accumulator");

		GameRegistry.registerTileEntity(TileTFTPrecipitator.class,
				"Precipitator");

		GameRegistry
				.registerTileEntity(TileTFTDynamoSteam.class, "DynamoSteam");

		GameRegistry.registerTileEntity(TileCryoChamber.class, "CryoChamber");

		GameRegistry.registerTileEntity(TileRFForge.class, "RFForge");

		GameRegistry.registerTileEntity(TileRFForgeCasing.class,
				"RFForgeCasing");

		GameRegistry.registerTileEntity(TileRFCrucible.class, "RFCrucible");

		GameRegistry.registerTileEntity(TileRFAnvil.class, "RFAnvil");
	}

	private void registerRecipeTypes() {
		RecipeSorter.register(MODID + ":upgradeshapeless",
				RecipeShapelessUpgrade.class,
				RecipeSorter.Category.SHAPELESS,
				"before:forge:shapelessore");

	}

	private void addBuckets(FMLInitializationEvent event) {
		RenderBucket bucketRenderer = new RenderBucket();
		for (FluidContainerData fcd : FluidContainerRegistry
				.getRegisteredFluidContainerData()) {
			Fluid fluid = fcd.fluid.getFluid();
			if (fcd.emptyContainer.getItem() == Items.bucket
					&& !(fluid == FluidRegistry.WATER
							|| fluid == FluidRegistry.LAVA
							|| fluid == TFCFluids.LAVA
							|| fluid == TFCFluids.FRESHWATER || fluid == TFCFluids.SALTWATER)) {
				ItemTFTSteelBucket bucket = new ItemTFTSteelBucket(fluid,
						fcd.filledContainer,
						BucketConfig.upsideDown.contains(fluid.getName()),
						BucketConfig.overflowing.contains(fluid.getName()));

				TFTItems.buckets.put(fluid.getName(), bucket);

				GameRegistry.registerItem(bucket,
						"bucket" + StringHelper.titleCase(fluid.getName()));

				ItemStack bucketStack = new ItemStack(bucket);
				FluidContainerRegistry.registerFluidContainer(fcd.fluid,
						bucketStack,
						bucket.getEmpty());
				if (event.getSide() == Side.CLIENT) {
					MinecraftForgeClient.registerItemRenderer(bucket,
							bucketRenderer);
				}
			}
		}
	}

	private void addBlocks() {
		addMachines();
		addDynamos();
		addSheetBlocks();

	}

	private void addItems() {
		TFTItems.yellowCake = new ItemTerra().setUnlocalizedName("yellowCake")
				.setCreativeTab(TFCTabs.TFC_MATERIALS);

		GameRegistry.registerItem(TFTItems.yellowCake, "yellowCake");

		OreDictionary.registerOre("dustUranium", TFTItems.yellowCake);
	}

	private void addMachines() {
		TFTBlocks.machine = new BlockTFTMachine().setBlockName("Machine");

		GameRegistry.registerBlock(TFTBlocks.machine,
				ItemBlockTFTMachine.class,
				"machine");

		BlockTFTMachine.extruder = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.EXTRUDER.ordinal()));
		GameRegistry.registerCustomItemStack("extruder",
				BlockTFTMachine.extruder);

		BlockTFTMachine.accumulator = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.ACCUMULATOR.ordinal()));
		GameRegistry.registerCustomItemStack("accumulator",
				BlockTFTMachine.accumulator);

		BlockTFTMachine.precipitator = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.PRECIPITATOR.ordinal()));
		GameRegistry.registerCustomItemStack("precipitator",
				BlockTFTMachine.precipitator);

		BlockTFTMachine.cryoChamber = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.CRYOCHAMBER.ordinal()));

		BlockTFTMachine.rfForge = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.RFFORGE.ordinal()));

		TFTBlocks.rfForgeCasing = new BlockRFForgeCasing()
				.setBlockName("RFForgeCasing");
		GameRegistry.registerBlock(TFTBlocks.rfForgeCasing, "RFForgeCasing");

		BlockTFTMachine.rfCrucible = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.RFCRUCIBLE.ordinal()));

		BlockTFTMachine.rfAnvil = ItemBlockTFTMachine
				.setDefaultTag(new ItemStack(TFTBlocks.machine, 1,
						BlockTFTMachine.Types.RFANVIL.ordinal()));

	}

	private void addDynamos() {
		TFTBlocks.dynamo = new BlockTFTDynamo().setBlockName("Dynamo");

		GameRegistry.registerBlock(TFTBlocks.dynamo,
				ItemBlockTFTDynamo.class,
				"dynamo");

		BlockTFTDynamo.dynamoSteam = new ItemStack(TFTBlocks.dynamo, 1,
				BlockTFTDynamo.Types.STEAM.ordinal());

		GameRegistry.registerCustomItemStack("dynamoSteam",
				BlockTFTDynamo.dynamoSteam);
	}

	private void addSheetBlocks() {
		TFTBlocks.metalSheet = new BlockTFTMetalSheet()
				.setBlockName("MetalSheet").setHardness(80);

		GameRegistry.registerBlock(TFTBlocks.metalSheet, "MetalSheet");
	}

	private void handleFules() {
		if (MachineConfig.dynamoCompressionEnabled) {
			FuelHandler.registerCoolant(TFCFluids.FRESHWATER.getName(),
					FuelHandler.configFuels.get("Coolants", "water", 400000));
		}

	}

	private void readConfig(FMLPreInitializationEvent event) {
		logger.info("Loading config");

		Configuration metalConfig = new Configuration(new File(
				event.getModConfigurationDirectory(), MODID + "/Metals.cfg"),
				true);
		metalConfig.load();

		for (Entry<String, MetalStat> entry : TFTechness.statMap.entrySet()) {
			String key = entry.getKey();

			HeatRaw heat = entry.getValue().heat;
			double melt = metalConfig.get(key, "MeltingPoint", heat.meltTemp)
					.getDouble();
			double sh = metalConfig.get(key, "SpecificHeat", heat.specificHeat)
					.getDouble();
			entry.getValue().ingotMass = metalConfig.get(key,
					"IngotMass",
					entry.getValue().ingotMass).getInt();
			entry.getValue().heat = new HeatRaw(sh, melt);

		}

		metalConfig.save();

		Configuration config = new Configuration(new File(
				event.getModConfigurationDirectory(), MODID + "/General.cfg"),
				true);
		config.load();
		MetalConfig.loadConfig(config);
		RecipeConfig.loadConfig(config);
		MachineConfig.loadConfig(config);

		config.save();

		BucketConfig.loadConifg(event.getModConfigurationDirectory() + "/"
				+ MODID);
	}

	private void replaceMachineRecipes() {
		if (RecipeConfig.replaceMachine) {
			// (vanilla)
			replaceFurnaceRecipes();
		}
	}

	private void addRecipes() {
		techResearchRecipes();
		for (Material m : materials) {
			unshapedMetalRecipe(m);
			nuggetIngotBlockRecipe(m);
		}
		tankRecipes();
		coilRecipes();
		machineCraftingRecipes();
		augmentRecipes();
		bigReactorRecipes();
		railcraftRecipes();
		dynamoCraftingRecipes();
		machineRecipes();

		TFTAugments.addRecipes();
	}

	private void techResearchRecipes() {
		//TODO: better recipe
		//GameRegistry.addRecipe(new ShapelessOreRecipe(TRItems.notebook,
		//		Items.book, "dustRedstone"));

	}

	private void augmentRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.pneumaticServo,
				" g ", "rsr", Character.valueOf('s'), "plateIron", Character
						.valueOf('g'), "blockGlass", Character.valueOf('r'),
				"dustRedstone"));
		// Auto output 
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemHelper
				.cloneStack(TEAugments.generalAutoOutput, 8), "plateTin",
				TEItems.pneumaticServo));
		// Auto input
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemHelper
				.cloneStack(TEAugments.generalAutoInput, 8), TFCBlocks.hopper,
				TEItems.pneumaticServo));
		// Reconfigurable sides
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemHelper
				.cloneStack(TEAugments.generalReconfigSides, 8), "plateTin",
				"plateGold"));
		// Redstone control
		GameRegistry.addRecipe(new ShapelessOreRecipe(ItemHelper
				.cloneStack(TEAugments.generalRedstoneControl, 8), "plateTin",
				"dustRedstone"));

		// Secondary sieve I
		GameRegistry.addRecipe(new ShapelessOreRecipe(
				TEAugments.machineSecondary[0], "plateDoubleBronze",
				"materialCloth"));
		// Secondary sieve II
		GameRegistry.addRecipe(new ShapedOreRecipe(
				TEAugments.machineSecondary[1], " p ", "gHg", Character
						.valueOf('p'), "plateDoubleBronze", Character
						.valueOf('g'), "dustGlowstone", Character.valueOf('H'),
				"blockGlassHardened"));

	}

	private void machineRecipes() {
		transposerRecipes();
		magmaCrucibleRecipes();
		// smelterRecipes();
	}

	private void replaceFurnaceRecipes() {
		Map<ItemStack, ItemStack> smelt = (Map<ItemStack, ItemStack>) FurnaceRecipes
				.smelting().getSmeltingList();
		Iterator<Map.Entry<ItemStack, ItemStack>> iterator = smelt.entrySet()
				.iterator();
		ArrayList<FurnaceRecipe> pushRecipes = new ArrayList<FurnaceRecipe>();
		while (iterator.hasNext()) {
			Map.Entry<ItemStack, ItemStack> fr = iterator.next();
			for (Material m : materials) {
				if (OreDict.oresMatch(fr.getValue(), new ItemStack(m.ingot))) {
					iterator.remove();
					// avoids modifying the map during iteration
					pushRecipes.add(new FurnaceRecipe(fr.getKey(),
							new ItemStack(m.ingot), FurnaceRecipes.smelting()
									.func_151398_b(fr.getKey())));

				}
			}
		}

		for (FurnaceRecipe fr : pushRecipes) {
			fr.register();
		}
	}

	private void replaceSmelterRecipes() {
		// Replace recipes that result in a tf ingot with recipes that result in
		// a tfc/tft ingot
		for (RecipeSmelter rs : SmelterManager.getRecipeList()) {
			if (rs.getPrimaryOutput().getItem() == TFCItems.steelIngot) {
				SmelterManager.removeRecipe(rs.getPrimaryInput(),
						rs.getSecondaryInput());
			}
			for (Material m : materials) {
				boolean match = OreDict.oresMatch(rs.getPrimaryOutput(),
						new ItemStack(m.ingot));
				boolean match2 = OreDict.oresMatch(rs.getSecondaryOutput(),
						new ItemStack(m.ingot));
				if (match || match2) {
					SmelterManager.removeRecipe(rs.getPrimaryInput(),
							rs.getSecondaryInput());
					ItemStack out;
					if (match) {
						out = new ItemStack(m.ingot,
								rs.getPrimaryOutput().stackSize);
					} else {
						out = rs.getPrimaryOutput();
					}
					ItemStack out2;
					if (match2) {
						out2 = new ItemStack(m.ingot,
								rs.getSecondaryOutput().stackSize);
					} else {
						out2 = rs.getSecondaryOutput();
					}

					SmelterManager.addRecipe(rs.getEnergy(),
							rs.getPrimaryInput(),
							rs.getSecondaryInput(),
							out,
							out2,
							rs.getSecondaryOutputChance());
				}
			}
		}
	}

	private void magmaCrucibleRecipes() {
		for (HeatIndex hi : HeatRegistry.getInstance().getHeatList()) {
			if (hi.getOutputItem() instanceof ItemMeltedMetal) {
				Metal m = MetalRegistry.instance.getMetalFromItem(hi
						.getOutputItem());
				if (m != null) {
					MetalStat stat = statMap.get(m.name.replaceAll(" ", ""));
					int rf = (int) ((hi.meltTemp - baseTemp) * stat.ingotMass
							* hi.specificHeat / rfToJoules);
					ThermalExpansionHelper
							.addCrucibleRecipe(rf,
									hi.input,
									new FluidStack(
											TFTFluids.metal.get(m.name),
											MetalConfig.ingotFluidmB
													* hi.getOutput(new Random()).stackSize));
				}
			}
		}

		// Map<String, Metal> metals = MetalSnatcher.getMetals();
		// for (Entry<String, Fluid> mf : TFTFluids.metal.entrySet()) {
		// Metal m = metals.get(mf.getKey());
		// HeatIndex hi = MetalSnatcher.getHeatIndexFromMetal(m);
		// MetalStat stat = statMap.get(mf.getKey().replaceAll(" ", ""));
		// if (stat == null) {
		// TFTechness.logger.info(mf.getKey());
		// } else {
		// int rf = (int) ((hi.meltTemp - baseTemp) * stat.ingotMass *
		// hi.specificHeat /
		// joulesToRF);
		// ThermalExpansionHelper.addCrucibleRecipe(rf, new ItemStack(m.ingot),
		// new
		// FluidStack(mf.getValue(),
		// MetalConfig.ingotFluidmB));
		// }
		// }
	}

	private void transposerRecipes() {
		// Pitchblende -> Yellow Cake
		TransposerManager.addFillRecipe(800,
				TFTMeta.pitchblende,
				new ItemStack(TFTItems.yellowCake, 1),
				new FluidStack(TFFluids.fluidGlowstone, 250),
				false,
				false);

		// Billon -> Signalum
		// Ingot
		TransposerManager.addFillRecipe(1600,
				new ItemStack(TFTItems.ingots.get("Billon")),
				new ItemStack(TFTItems.ingots.get("Signalum")),
				new FluidStack(TFFluids.fluidRedstone, 1000),
				false,
				false);
		// Double Ingot
		TransposerManager.addFillRecipe(3200,
				new ItemStack(TFTItems.ingots2x.get("Billon")),
				new ItemStack(TFTItems.ingots2x.get("Signalum")),
				new FluidStack(TFFluids.fluidRedstone, 2000),
				false,
				false);
		// Sheet
		TransposerManager.addFillRecipe(3200,
				new ItemStack(TFTItems.sheets.get("Billon")),
				new ItemStack(TFTItems.sheets.get("Signalum")),
				new FluidStack(TFFluids.fluidRedstone, 2000),
				false,
				false);
		// Double sheet
		TransposerManager.addFillRecipe(6400,
				new ItemStack(TFTItems.sheets2x.get("Billon")),
				new ItemStack(TFTItems.sheets2x.get("Signalum")),
				new FluidStack(TFFluids.fluidRedstone, 4000),
				false,
				false);
		// Rod
		TransposerManager.addFillRecipe(1600,
				new ItemStack(TFTItems.rods.get("Billon")),
				new ItemStack(TFTItems.rods.get("Signalum")),
				new FluidStack(TFFluids.fluidRedstone, 1000),
				false,
				false);
	}

	private void bigReactorRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineHousing,
				new Object[] { " g ",
						"gSg",
						" g ",
						Character.valueOf('S'),
						"plateDoubleSteel",
						Character.valueOf('g'),
						"dustGraphite" }));

		GameRegistry.addRecipe(new ShapedOreRecipe(ItemHelper
				.cloneStack(TFTMeta.brTurbineGlass, 2), new Object[] { "HTH",
				Character.valueOf('H'),
				"blockGlassHardened",
				Character.valueOf('T'),
				TFTMeta.brTurbineHousing }));

		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineController,
				new Object[] { " r ",
						"rTr",
						" r ",
						Character.valueOf('T'),
						TFTMeta.brTurbineHousing,
						Character.valueOf('r'),
						"dustRedstone" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbinePowerPort,
				new Object[] { " g ",
						"rTr",
						" c ",
						Character.valueOf('g'),
						"plateGold",
						Character.valueOf('r'),
						"dustRedstone",
						Character.valueOf('T'),
						TFTMeta.brTurbineHousing,
						Character.valueOf('c'),
						TEItems.powerCoilSilver }));
		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineFluidPort,
				new Object[] { " v ",
						"iTi",
						" f ",
						Character.valueOf('v'),
						"plateInvar",
						Character.valueOf('i'),
						"plateIron",
						Character.valueOf('T'),
						TFTMeta.brTurbineHousing,
						Character.valueOf('f'),
						TDDucts.fluidHardened.itemStack }));
		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineFluidPort,
				new Object[] { " v ",
						"iTi",
						" f ",
						Character.valueOf('v'),
						"plateInvar",
						Character.valueOf('i'),
						"plateIron",
						Character.valueOf('T'),
						TFTMeta.brTurbineHousing,
						Character.valueOf('f'),
						TDDucts.fluidHardenedOpaque.itemStack }));
		GameRegistry.addRecipe(new ShapedOreRecipe(
				TFTMeta.brTurbineRotorBearing, new Object[] { " s ",
						"iTi",
						" f ",
						Character.valueOf('s'),
						"plateSteel",
						Character.valueOf('i'),
						"plateIron",
						Character.valueOf('T'),
						TFTMeta.brTurbineHousing,
						Character.valueOf('r'),
						TFTMeta.brTurbineRotorShaft }));

		GameRegistry.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineRotorShaft,
				new Object[] { " i ",
						"ini",
						" i ",
						Character.valueOf('i'),
						"rodWroughtIron",
						Character.valueOf('n'),
						"rodInvar" }));

		GameRegistry
				.addRecipe(new ShapedOreRecipe(TFTMeta.brTurbineRotorBlade,
						new Object[] { "iii",
								Character.valueOf('i'),
								"rodWroughtIron" }));

	}

	private void railcraftRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(
				EnumMachineBeta.BOILER_TANK_LOW_PRESSURE.getItem(),
				new Object[] { "ii ",
						"ii ",
						Character.valueOf('i'),
						"plateIron"

				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(
				EnumMachineBeta.BOILER_TANK_HIGH_PRESSURE.getItem(),
				new Object[] { "ss ",
						"ss ",
						Character.valueOf('s'),
						"plateSteel"

				}));
		GameRegistry.addRecipe(new ShapedOreRecipe(
				EnumMachineBeta.TANK_IRON_WALL.getItem(), "ii ", "ii ",
				Character.valueOf('i'), "plateDoubleIron"));
		GameRegistry.addRecipe(new ShapedOreRecipe(
				EnumMachineBeta.TANK_STEEL_WALL.getItem(),
				new Object[] { "ss ",
						"ss ",
						Character.valueOf('s'),
						"plateDoubleSteel"

				}));

		if (MachineConfig.bigReactorsOnly) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
					EnumMachineBeta.ENGINE_STEAM_HOBBY.getItem(),
					new Object[] { " t ",
							"GbG",
							"brb",
							Character.valueOf('t'),
							TEItems.powerCoilSilver,
							Character.valueOf('G'),
							"gearGold",
							Character.valueOf('r'),
							"dustRedstone",
							Character.valueOf('b'),
							"ingotBronze" }));
		}
	}

	private void machineCraftingRecipes() {
		ItemStack[] augs = new ItemStack[] {};
		String machineFrame = "thermalexpansion:machineFrame";
		// Igneous Extruder
		if (MachineConfig.extruderEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.extruder, augs, new Object[] { " p ",
							"gFg",
							"cSc",
							Character.valueOf('p'),
							Blocks.piston,
							Character.valueOf('g'),
							"blockGlass",
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('c'),
							"gearCopper",
							Character.valueOf('S'),
							TEItems.pneumaticServo }));

			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.extruder);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.extruder);
			}

			TECraftingHandler.addSecureRecipe(BlockTFTMachine.extruder);
		} else if (RecipeConfig.upgradeCrafting) {
			TFTCraftingHandler.addMachineUpgradeRecipes(BlockMachine.extruder);
		}
		Object[] accumulator = new Object[] { " b ",
				"gFg",
				"cSc",
				Character.valueOf('b'),
				TFCItems.redSteelBucketEmpty,
				Character.valueOf('g'),
				"blockGlass",
				Character.valueOf('F'),
				machineFrame,
				Character.valueOf('c'),
				"gearCopper",
				Character.valueOf('S'),
				TEItems.pneumaticServo };
		// Aqueous Accumulator
		if (MachineConfig.accumulatorEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.accumulator, augs, accumulator));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.accumulator);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.accumulator);
			}

			TECraftingHandler.addSecureRecipe(BlockTFTMachine.extruder);
		} else {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockMachine.accumulator, augs, accumulator));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockMachine.accumulator);
			}
		}
		// Glacial Precipitator
		if (MachineConfig.precipitatorEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.precipitator, augs, new Object[] { " p ",
							"iFi",
							"cSc",
							Character.valueOf('p'),
							Blocks.piston,
							Character.valueOf('i'),
							"ingotInvar",
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('c'),
							"gearCopper",
							Character.valueOf('S'),
							TEItems.pneumaticServo }));

			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.precipitator);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.precipitator);
			}

			TECraftingHandler.addSecureRecipe(BlockTFTMachine.precipitator);
		} else if (RecipeConfig.upgradeCrafting) {
			TFTCraftingHandler
					.addMachineUpgradeRecipes(BlockMachine.precipitator);
		}

		// Cryogenic Chamber
		if (MachineConfig.cryoChamberEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.cryoChamber, augs, new Object[] { " C ",
							"iFi",
							"cSc",
							Character.valueOf('C'),
							TFItems.dustCryotheum,
							Character.valueOf('i'),
							TFTMeta.freshIce,
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('c'),
							"gearCopper",
							Character.valueOf('S'),
							TEItems.pneumaticServo }));

			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.cryoChamber);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.cryoChamber);
			}
		}
		if (MachineConfig.rfForgeEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.rfForge, augs, new Object[] { " P ",
							"bFb",
							"gRg",
							Character.valueOf('P'),
							TFItems.dustPyrotheum,
							Character.valueOf('b'),
							"stoneBricks",
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('g'),
							"gearInvar",
							Character.valueOf('R'),
							TEItems.powerCoilGold }));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfForge);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfForge);
			}
		}
		if (MachineConfig.rfCrucibleEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.rfCrucible, augs, new Object[] { " P ",
							"bFb",
							"gRg",
							Character.valueOf('P'),
							TFItems.dustPyrotheum,
							Character.valueOf('b'),
							TFCBlocks.fireBrick,
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('g'),
							"gearInvar",
							Character.valueOf('R'),
							TEItems.powerCoilGold }));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfCrucible);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfCrucible);
			}
		}
		if (MachineConfig.rfAnvilEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(
					BlockTFTMachine.rfAnvil, augs, new Object[] { " P ",
							"bFb",
							"gRg",
							Character.valueOf('P'),
							TFTMeta.anvilRedSteel,
							Character.valueOf('b'),
							"gearTin",
							Character.valueOf('F'),
							machineFrame,
							Character.valueOf('g'),
							"gearCopper",
							Character.valueOf('R'),
							TEItems.powerCoilGold }));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfAnvil);
			} else {
				TECraftingHandler
						.addMachineUpgradeRecipes(BlockTFTMachine.rfAnvil);
			}
		}
	}

	private void dynamoCraftingRecipes() {
		if (!MachineConfig.bigReactorsOnly) {
			ItemStack[] augs = new ItemStack[] {};
			if (MachineConfig.dynamoSteamEnabled) {
				GameRegistry.addRecipe(new RecipeAugmentable(
						BlockTFTDynamo.dynamoSteam, augs, new Object[] { " t ",
								"CcC",
								"crc",
								Character.valueOf('t'),
								TEItems.powerCoilSilver,
								Character.valueOf('C'),
								"gearCopper",
								Character.valueOf('r'),
								"dustRedstone",
								Character.valueOf('c'),
								"ingotCopper" }));

			}
		}
	}

	private void coilRecipes() {
		if (RecipeConfig.coilsEnabled) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
					TEItems.powerCoilElectrum, new Object[] { "r  ",
							" E ",
							"  r",
							Character.valueOf('E'),
							"rodElectrum",
							Character.valueOf('r'),
							"dustRedstone" }));
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.powerCoilSilver,
					new Object[] { "  r",
							" S ",
							"r  ",
							Character.valueOf('S'),
							"rodSilver",
							Character.valueOf('r'),
							"dustRedstone" }));
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.powerCoilGold,
					new Object[] { "  r",
							" G ",
							"r  ",
							Character.valueOf('G'),
							"rodGold",
							Character.valueOf('r'),
							"dustRedstone" }));
		}

	}

	private void tankRecipes() {
		if (RecipeConfig.tanksEnabled) {
			for (int i = 0; i < tankMap.length; i++) {
				TankMap t = tankMap[i];
				ItemStack frame = BlockTankFrame.getItemStack(t.type,
						BlockTankFrame.Stages.frame);
				// Finished tanks are made by surrounding a tank frame with 4
				// glass blocks
				GameRegistry.addRecipe(new ShapedOreRecipe(t.finished,
						new Object[] { " G ",
								"GFG",
								" G ",
								Character.valueOf('G'),
								"blockGlass",
								Character.valueOf('F'),
								frame }));
				if (i > 0) {
					TankMap prev = tankMap[i - 1];
					// Finished tanks can be made with previous tier finished +
					// sheet
					GameRegistry.addRecipe(new RecipeShapelessUpgrade(
							t.finished, prev.finished,
							new Object[] { prev.finished, t.sheet2x }));
				}
			}
		}
	}

	private void nuggetIngotBlockRecipe(Material m) {
		GameRegistry.addRecipe(new ShapedOreRecipe(m.ingot,
				new Object[] { "nnn",
						"nnn",
						"nnn",
						Character.valueOf('n'),
						"nugget" + m.oreName }));
		GameRegistry
				.addRecipe(new ShapelessOreRecipe(new ItemStack(m.ingot, 9),
						new Object[] { "block" + m.oreName }));
	}

	private void unshapedMetalRecipe(Material m) {

		if (!m.gearOnly) {
			// Ingot => Unshaped
			GameRegistry.addShapelessRecipe(new ItemStack(m.unshaped, 1, 0),
					Recipes.getStackNoTemp(new ItemStack(m.ingot, 1)),
					new ItemStack(TFCItems.ceramicMold, 1, 1));
			// Unshaped => Ingot
			GameRegistry.addShapelessRecipe(new ItemStack(m.ingot, 1, 0),
					Recipes.getStackNoTemp(new ItemStack(m.unshaped, 1)));
		}

	}

	private void addTanks() {
		tankMap = getTanks();

		registerTanks();
		registerTankHeat();

	}

	private void registerTanks() {
		TFTBlocks.tankFrame = new BlockTankFrame();

		GameRegistry.registerBlock(TFTBlocks.tankFrame,
				ItemBlockTankFrame.class,
				"TankFrame");

		// Goes through all the tank types & stages and creates an itemstack for
		// each one
		BlockTank.Types[] types = BlockTank.Types.values();
		BlockTankFrame.Stages[] stages = BlockTankFrame.Stages.values();
		int tankMapIndex = 0;
		for (int i = 0; i < types.length; i++) {
			BlockTank.Types t = types[i];

			for (int j = 0; j < stages.length; j++) {
				BlockTankFrame.Stages s = stages[j];

				String name = t.name() + s.name();
				ItemStack frame = new ItemStack(TFTBlocks.tankFrame, 1,
						BlockTankFrame.getMeta(t, s));

				BlockTankFrame.itemStacks.put(name, frame);

				GameRegistry.registerCustomItemStack("tank" + name, frame);

			}

		}

	}

	private void registerTankHeat() {
		HeatRegistry manager = HeatRegistry.getInstance();

		for (TankMap t : tankMap) {
			for (BlockTankFrame.Stages s : BlockTankFrame.Stages.values()) {
				ItemStack frame = BlockTankFrame.getItemStack(t.type, s);
				manager.addIndex(new HeatIndex(frame, t.heatRaw, frame));
			}

		}
	}

	private void addMetals() {
		materials = getMaterials();
		materialMap = getMatMap();
		for (Material mat : materials) {
			if (!mat.gearOnly) {
				addUnshaped(mat);
				addIngots(mat);
				addDoubleIngots(mat);
				registerMetal(mat);
				addSheets(mat);
				addDoubleSheets(mat);
			}
			addRod(mat);
			registerHeat(mat);
			registerAlloy(mat);
		}

	}

	private void addUnshaped(Material mat) {
		mat.unshaped = new ItemMeltedMetal().setUnlocalizedName(mat.name
				+ "Unshaped");

		TFTItems.unshaped.put(mat.name, mat.unshaped);
		GameRegistry.registerItem(mat.unshaped, "Unshaped" + mat.name);
	}

	private void addIngots(Material mat) {
		mat.ingot = new ItemIngot().setUnlocalizedName(mat.name + "Ingot");

		TFTItems.ingots.put(mat.name, mat.ingot);
		GameRegistry.registerItem(mat.ingot, mat.name + "Ingot");
		OreDictionary.registerOre("ingot" + mat.name, mat.ingot);

	}

	private void addDoubleIngots(Material mat) {
		mat.ingot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName(mat.name
				+ "Ingot2x")).setSize(EnumSize.LARGE).setMetal(mat.name, 200);

		TFTItems.ingots2x.put(mat.name, mat.ingot2x);
		GameRegistry.registerItem(mat.ingot2x, mat.name + "Ingot2x");
		OreDictionary.registerOre("ingotDouble" + mat.name, mat.ingot2x);
	}

	private void addSheets(Material mat) {
		mat.sheet = ((ItemTFTMetalSheet) new ItemTFTMetalSheet(mat.name)
				.setUnlocalizedName(mat.name + "Sheet"))
				.setMetal(mat.name, 200);

		TFTItems.sheets.put(mat.name, mat.sheet);
		GameRegistry.registerItem(mat.sheet, mat.name + "Sheet");
		OreDictionary.registerOre("plate" + mat.name, mat.sheet);

	}

	private void addDoubleSheets(Material mat) {
		mat.sheet2x = ((ItemTFTMetalSheet) new ItemTFTMetalSheet(mat.name)
				.setUnlocalizedName(mat.name + "Sheet2x")).setMetal(mat.name,
				400);

		TFTItems.sheets2x.put(mat.name, mat.sheet2x);
		GameRegistry.registerItem(mat.sheet2x, mat.name + "Sheet2x");
		OreDictionary.registerOre("plateDouble" + mat.name, mat.sheet2x);
	}

	private void addRod(Material mat) {
		mat.rod = new ItemRod(mat.metal.name);

		TFTItems.rods.put(mat.name, mat.rod);
		GameRegistry.registerItem(mat.rod, mat.name + "Rod");
		OreDictionary.registerOre("rod" + mat.name, mat.rod);
	}

	private void registerHeat(Material mat) {
		HeatRegistry manager = HeatRegistry.getInstance();
		registerMetalHeat(manager, mat);

	}

	private void registerMetalHeat(HeatRegistry manager, Material mat) {
		if (!mat.gearOnly) {
			addHeat(manager, mat.unshaped, mat.heatRaw, mat.unshaped, 1);
			addHeat(manager, mat.ingot, mat.heatRaw, mat.unshaped, 1);
			addHeat(manager, mat.ingot2x, mat.heatRaw, mat.unshaped, 2);
			addHeat(manager, mat.sheet, mat.heatRaw, mat.unshaped, 2);
			addHeat(manager, mat.sheet2x, mat.heatRaw, mat.unshaped, 4);
		}
		addHeat(manager, mat.rod, mat.heatRaw, mat.unshaped, 1);
		if (mat.gear != null) {
			addHeat(manager, mat.gear.getItem(), mat.heatRaw, mat.unshaped, 4);
		}

	}

	private void addHeat(HeatRegistry manager, Item metal, HeatRaw raw,
			Item unshaped, int quantity) {
		manager.addIndex(new HeatIndex(new ItemStack(metal, 1), raw,
				new ItemStack(unshaped, quantity)));

	}

	private void registerAlloy(Material material) {

		if (material instanceof MaterialAlloy) {
			MaterialAlloy mat = (MaterialAlloy) material;
			Alloy alloy = new Alloy(mat.metal, mat.alloyTier);
			for (AlloyIngred ing : mat.alloy) {
				alloy.addIngred(MetalRegistry.instance
						.getMetalFromString(ing.name), ing.min, ing.max);
			}
			AlloyManager.INSTANCE.addAlloy(alloy);
		}
	}

	private void registerMetal(Material mat) {
		mat.metal = new Metal(mat.name, mat.unshaped, mat.ingot);
		TFTMetals.metals.put(mat.name, mat.metal);
		MetalRegistry.instance.addMetal(mat.metal, mat.alloyTier);

	}

	private void removeGearRecipes(RemoveBatch batch, Material m) {
		if (RecipeConfig.gearsEnabled) {
			batch.addCrafting(m.gear);
		}

	}

	private void removeTankRecipes(RemoveBatch batch) {
		if (RecipeConfig.tanksEnabled) {
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1,
					BlockTank.Types.BASIC.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1,
					BlockTank.Types.HARDENED.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1,
					BlockTank.Types.REINFORCED.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1,
					BlockTank.Types.RESONANT.ordinal()));
		}

	}

	private void removeCoilRecipes(RemoveBatch batch) {
		if (RecipeConfig.coilsEnabled) {
			batch.addCrafting(TEItems.powerCoilElectrum);
			batch.addCrafting(TEItems.powerCoilSilver);
			batch.addCrafting(TEItems.powerCoilGold);
		}
	}

	private void removeMachineRecipes(RemoveBatch batch) {
		batch.addCrafting(TEItems.pneumaticServo);
		if (MachineConfig.extruderEnabled) {
			batch.addCrafting(BlockMachine.extruder);
		}

		batch.addCrafting(BlockMachine.accumulator);

		if (MachineConfig.precipitatorEnabled) {
			batch.addCrafting(BlockMachine.precipitator);
		}

		if (MachineConfig.dynamoSteamEnabled || MachineConfig.bigReactorsOnly) {
			batch.addCrafting(BlockDynamo.dynamoSteam);
		}

		if (MachineConfig.bigReactorsOnly) {
			batch.addCrafting(BlockDynamo.dynamoCompression);
			batch.addCrafting(BlockDynamo.dynamoEnervation);
			batch.addCrafting(BlockDynamo.dynamoMagmatic);
			batch.addCrafting(BlockDynamo.dynamoReactant);
		}
	}

	private void removeNuggetIngotRecipes(RemoveBatch batch, Material m) {
		if (m.nugget != null) {
			batch.addCrafting(new ItemStack(m.ingot),
					new ItemStack[] { m.nugget });
			//batch.addCrafting(new ItemStack(m.ingot));
		}
	}

	private void initMaps() {
		// Stock TFC
		// For multiple metals with the same stat entry
		MetalStat blackSteel = new MetalStat(0.35, 1485, 998);
		MetalStat blueSteel = new MetalStat(0.35, 1540, 975);
		MetalStat copper = new MetalStat(0.35, 1080, 996);
		MetalStat redSteel = new MetalStat(0.35, 1540, 1093);
		MetalStat steel = new MetalStat(0.35, 1540, 844);

		statMap.put("Bismuth", new MetalStat(0.14, 270, 1087));
		statMap.put("BismuthBronze", new MetalStat(0.35, 985, 963));
		statMap.put("BlackBronze", new MetalStat(0.35, 1070, 1313));
		statMap.put("BlackSteel", blackSteel);
		statMap.put("BlueSteel", blueSteel);
		statMap.put("Brass", new MetalStat(0.35, 930, 976));
		statMap.put("Bronze", new MetalStat(0.35, 950, 947));
		statMap.put("Copper", copper);
		statMap.put("Gold", new MetalStat(0.6, 1060, 2147));
		statMap.put("HCBlackSteel", blackSteel);
		statMap.put("HCBlueSteel", blueSteel);
		statMap.put("HCRedSteel", redSteel);
		statMap.put("Lead", new MetalStat(0.22, 328, 1260));
		statMap.put("Nickel", new MetalStat(0.48, 1453, 989));
		statMap.put("PigIron", new MetalStat(0.35, 1500, 900));
		statMap.put("Platinum", new MetalStat(0.35, 1730, 2383));
		statMap.put("RedSteel", redSteel);
		statMap.put("RoseGold", new MetalStat(0.35, 960, 1859));
		statMap.put("Silver", new MetalStat(0.48, 961, 1111));
		statMap.put("Steel", steel);
		statMap.put("SterlingSilver", new MetalStat(0.35, 900, 1082));
		statMap.put("Tin", new MetalStat(0.14, 230, 811));
		// TFC uses copper heat properties for all unknown ingots
		statMap.put("Unknown", copper);
		statMap.put("WeakRedSteel", redSteel);
		statMap.put("WeakBlueSteel", blueSteel);
		statMap.put("WeakSteel", steel);
		statMap.put("WroughtIron", new MetalStat(0.35, 1535, 889));
		statMap.put("Zinc", new MetalStat(0.21, 420, 792));

		// TF
		statMap.put("Invar", new MetalStat(0.52, 1700, 894));
		statMap.put("Mithril", new MetalStat(0.9, 660, 500));
		statMap.put("Electrum", new MetalStat(0.181, 650, 1629));
		statMap.put("Enderium", new MetalStat(0.5, 1750, 1279));
		statMap.put("Signalum", new MetalStat(0.45, 900, 1024));
		statMap.put("Lumium", new MetalStat(0.35, 1200, 2544));

		// TFT
		statMap.put("Billon", new MetalStat(0.35, 950, 1024));
		statMap.put("Uranium", new MetalStat(0.12, 1132, 2122));
	}

	private TankMap[] getTanks() {
		return new TankMap[] { new TankMap("Copper", BlockTank.Types.BASIC),
				new TankMap("Invar", BlockTank.Types.HARDENED),
				new TankMap(new ItemStack(TEBlocks.blockGlass, 1), 5,
						BlockTank.Types.REINFORCED,
						TFTechness.statMap.get("Invar").heat),
				new TankMap("Enderium", BlockTank.Types.RESONANT) };
	}

	private Map<String, Material> getMatMap() {
		Map<String, Material> m = new HashMap();
		for (Material mat : materials) {
			m.put(mat.name, mat);
		}
		return m;
	}

	private Material[] getMaterials() {
		return new Material[] { new Material("Gold", TFCItems.goldUnshaped,
				TFCItems.goldIngot, TFCItems.goldSheet2x, TFItems.gearGold, 2,
				Global.GOLD, new ItemStack(Items.gold_nugget)),
				new Material("WroughtIron", "Iron",
						TFCItems.wroughtIronUnshaped,
						TFCItems.wroughtIronIngot, TFCItems.wroughtIronSheet2x,
						TFItems.gearIron, 3, Global.WROUGHTIRON,
						TFItems.nuggetIron),
				new Material("Copper", TFCItems.copperUnshaped,
						TFCItems.copperIngot, TFCItems.copperSheet2x,
						TFItems.gearCopper, 1, Global.COPPER,
						TFItems.nuggetCopper),
				new Material("Tin", TFCItems.tinUnshaped, TFCItems.tinIngot,
						TFCItems.tinSheet2x, TFItems.gearTin, 0, Global.TIN,
						TFItems.nuggetTin),
				new Material("Silver", TFCItems.silverUnshaped,
						TFCItems.silverIngot, TFCItems.silverSheet2x,
						TFItems.gearSilver, 2, Global.SILVER,
						TFItems.nuggetSilver),
				new Material("Lead", TFCItems.leadUnshaped, TFCItems.leadIngot,
						TFCItems.leadSheet2x, TFItems.gearLead, 2, Global.LEAD,
						TFItems.nuggetLead),
				new Material("Nickel", TFCItems.nickelUnshaped,
						TFCItems.nickelIngot, TFCItems.nickelSheet2x,
						TFItems.gearNickel, 4, Global.NICKEL,
						TFItems.nuggetNickel),
				new Material("Platinum", TFCItems.platinumUnshaped,
						TFCItems.platinumIngot, TFCItems.platinumSheet2x,
						TFItems.gearPlatinum, 3, Global.PLATINUM,
						TFItems.nuggetPlatinum),
				new Material("Bronze", TFCItems.bronzeUnshaped,
						TFCItems.bronzeIngot, TFCItems.bronzeSheet2x,
						TFItems.gearBronze, 2, Global.BRONZE,
						TFItems.nuggetBronze),
				new Material("Uranium", null, 6, Alloy.EnumTier.TierIV, null),
				new MaterialAlloy("Invar", TFItems.gearInvar, 5,
						Alloy.EnumTier.TierIII,
						new AlloyIngred[] { new AlloyIngred("Wrought Iron",
								61.00f, 67.00f),
								new AlloyIngred("Nickel", 33.00f, 39.00f) },
						TFItems.nuggetInvar),
				new Material("Mithril", TFItems.gearMithril, 5,
						Alloy.EnumTier.TierIII, TFItems.nuggetMithril),
				new MaterialAlloy("Electrum", TFItems.gearElectrum, 4,
						Alloy.EnumTier.TierIII,
						new AlloyIngred[] { new AlloyIngred("Gold", 50.00f,
								60.00f),
								new AlloyIngred("Silver", 40.00f, 50.00f) },
						TFItems.nuggetElectrum),
				new Material("Enderium", TFItems.gearEnderium, 7,
						Alloy.EnumTier.TierV, TFItems.nuggetEnderium),
				new Material("Signalum", TFItems.gearSignalum, 5,
						Alloy.EnumTier.TierIV, TFItems.nuggetSignalum),
				new Material("Lumium", TFItems.gearLumium, 5,
						Alloy.EnumTier.TierIV, TFItems.nuggetLumium),
				new MaterialAlloy("Billon", null, 5, Alloy.EnumTier.TierIV,
						new AlloyIngred[] { new AlloyIngred("Copper", 70.00f,
								80.00f),
								new AlloyIngred("Silver", 20.00f, 30.00f) },
						null) };
	}
}
