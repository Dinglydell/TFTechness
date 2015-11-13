package dinglydell.tftechness;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.LogManager;

import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.machine.BlockMachine;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalexpansion.item.TEItems;
import cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper;
import cofh.thermalexpansion.util.crafting.RecipeMachine;
import cofh.thermalexpansion.util.crafting.TECraftingHandler;
import cofh.thermalfoundation.item.TFItems;

import com.bioxx.tfc.Core.Recipes;
import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.Core.Metal.AlloyManager;
import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.Items.ItemIngot;
import com.bioxx.tfc.Items.ItemMeltedMetal;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCFluids;
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Enums.EnumSize;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import dinglydell.tftechness.block.BlockTFTMetalSheet;
import dinglydell.tftechness.block.BlockTankFrame;
import dinglydell.tftechness.block.ItemBlockTankFrame;
import dinglydell.tftechness.block.TFTBlocks;
import dinglydell.tftechness.block.machine.BlockTFTMachine;
import dinglydell.tftechness.block.machine.ItemBlockTFTMachine;
import dinglydell.tftechness.config.MachineConfig;
import dinglydell.tftechness.config.RecipeConfig;
import dinglydell.tftechness.item.ItemRod;
import dinglydell.tftechness.item.ItemTFTMetalSheet;
import dinglydell.tftechness.item.ItemTFTSteelBucket;
import dinglydell.tftechness.item.TFCMeta;
import dinglydell.tftechness.item.TFTItems;
import dinglydell.tftechness.metal.AlloyIngred;
import dinglydell.tftechness.metal.Material;
import dinglydell.tftechness.metal.MaterialAlloy;
import dinglydell.tftechness.metal.TFTMetals;
import dinglydell.tftechness.metal.TankMap;
import dinglydell.tftechness.recipe.AnvilRecipeHandler;
import dinglydell.tftechness.recipe.RecipeShapelessUpgrade;
import dinglydell.tftechness.recipe.RemoveBatch;
import dinglydell.tftechness.recipe.TFTCraftingHandler;
import dinglydell.tftechness.render.item.RenderBucket;
import dinglydell.tftechness.tileentities.TETFTMetalSheet;
import dinglydell.tftechness.tileentities.machine.TileTFTAccumulator;
import dinglydell.tftechness.tileentities.machine.TileTFTExtruder;

@Mod(modid = TFTechness.MODID, version = TFTechness.VERSION, dependencies = "required-after:terrafirmacraft;required-after:ThermalFoundation;required-after:ThermalExpansion")
public class TFTechness {
	public static final String MODID = "TFTechness";
	public static final String VERSION = "0.1";
	public static org.apache.logging.log4j.Logger logger = LogManager.getLogger("TFTechness");
	public static Material[] materials;
	public static Map<String, HeatRaw> heatMap = new HashMap();
	public static Map<String, Material> materialMap;
	public static TankMap[] tankMap;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initHeatMap();
		readConfig(event);
		getTFCMetaItems();
		addMetals();
		
		addBlocks();
		
		registerRecipeTypes();
		
	}
	
	private void getTFCMetaItems() {
		TFCMeta.salt = new ItemStack(TFCItems.powder, 1, 9);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Requires some TE init
		addTanks();
		addBuckets();
		registerTileEntities();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new AnvilRecipeHandler());
		RemoveBatch batch = new RemoveBatch();
		
		removeTankRecipes(batch);
		removeCoilRecipes(batch);
		removeMachineRecipes(batch);
		for (Material m : materials) {
			removeGearRecipes(batch, m);
			removeNuggetIngotRecipes(batch, m);
		}
		batch.Execute();
		
		addRecipes();
		
	}
	
	private void registerTileEntities() {
		GameRegistry.registerTileEntity(TETFTMetalSheet.class, "TFTMetalSheet");
		
		GameRegistry.registerTileEntity(TileTFTExtruder.class, "Extruder");
		
		GameRegistry.registerTileEntity(TileTFTAccumulator.class, "Accumulator");
	}
	
	private void registerRecipeTypes() {
		RecipeSorter.register(MODID + ":upgradeshapeless",
				RecipeShapelessUpgrade.class,
				RecipeSorter.Category.SHAPELESS,
				"before:forge:shapelessore");
		
	}
	
	private void addBuckets() {
		RenderBucket bucketRenderer = new RenderBucket();
		for (FluidContainerData fcd : FluidContainerRegistry.getRegisteredFluidContainerData()) {
			Fluid fluid = fcd.fluid.getFluid();
			if (fcd.emptyContainer.getItem() == Items.bucket
					&& !(fluid == FluidRegistry.WATER || fluid == FluidRegistry.LAVA || fluid == TFCFluids.LAVA
							|| fluid == TFCFluids.FRESHWATER || fluid == TFCFluids.SALTWATER)) {
				IIcon icon = fcd.filledContainer.getItem().getIconFromDamage(fcd.filledContainer.getItemDamage());
				Item bucket = new ItemTFTSteelBucket(fcd.fluid.getFluid().getBlock()).setIcon(icon).setUnlocalizedName("steelBucket"
						+ fcd.fluid.getUnlocalizedName());
				
				TFTItems.buckets.put(fcd.fluid.getUnlocalizedName(), bucket);
				
				GameRegistry.registerItem(bucket, "tftBucket" + fcd.fluid.getUnlocalizedName());
				
				MinecraftForgeClient.registerItemRenderer(bucket, bucketRenderer);
			}
		}
	}
	
	private void addBlocks() {
		addMachines();
		addSheetBlocks();
		
	}
	
	private void addMachines() {
		TFTBlocks.machine = new BlockTFTMachine().setBlockName("Machine");
		
		GameRegistry.registerBlock(TFTBlocks.machine, ItemBlockTFTMachine.class, "machine");
		
		BlockTFTMachine.extruder = ItemBlockTFTMachine.setDefaultTag(new ItemStack(TFTBlocks.machine,
				1,
				BlockTFTMachine.Types.EXTRUDER.ordinal()));
		GameRegistry.registerCustomItemStack("extruder", BlockTFTMachine.extruder);
		
		BlockTFTMachine.accumulator = ItemBlockTFTMachine.setDefaultTag(new ItemStack(TFTBlocks.machine,
				1,
				BlockTFTMachine.Types.ACCUMULATOR.ordinal()));
		
	}
	
	private void addSheetBlocks() {
		TFTBlocks.metalSheet = new BlockTFTMetalSheet().setBlockName("MetalSheet").setHardness(80);
		
		GameRegistry.registerBlock(TFTBlocks.metalSheet, "MetalSheet");
	}
	
	private void readConfig(FMLPreInitializationEvent event) {
		logger.info("Loading config");
		Configuration metalConfig = new Configuration(new File(event.getModConfigurationDirectory(),
				"/TFTechness/Metals.cfg"), true);
		metalConfig.load();
		
		for (Map.Entry<String, HeatRaw> entry : heatMap.entrySet()) {
			String key = entry.getKey();
			
			HeatRaw heat = entry.getValue();
			double melt = metalConfig.get(key, "MeltingPoint", heat.meltTemp).getDouble();
			double sh = metalConfig.get(key, "SpecificHeat", heat.specificHeat).getDouble();
			heat = new HeatRaw(sh, melt);
		}
		
		metalConfig.save();
		
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(),
				"/TFTechness/General.cfg"), true);
		config.load();
		config.addCustomCategoryComment("Recipes",
				"true: TFTechness changes the recipe, false: TFTechness leave it alone.");
		RecipeConfig.gearsEnabled = config.getBoolean("gears", "Recipes", true, "");
		RecipeConfig.tanksEnabled = config.getBoolean("portableTanks", "Recipes", true, "");
		RecipeConfig.coilsEnabled = config.getBoolean("redstoneCoils", "Recipes", true, "");
		RecipeConfig.upgradeCrafting = config.getBoolean("upgradeCrafting",
				"Recipes",
				true,
				"ThermalExpansion machine upgrades.");
		
		config.addCustomCategoryComment("Machines",
				"TFTechness changes some the way some ThermalExpansion machines work. (eg, makes the machine use TFC water instead of vanilla). Use this to disable the TFTechness versions and use ThermalExpansion.");
		MachineConfig.extruderEnabled = config.getBoolean("extruder",
				"Machines",
				true,
				"Accepts salt water, fresh water and TFC lava and produces salt when salt water is consumed.");
		MachineConfig.accumulatorEnabled = config.getBoolean("accumulator",
				"Machines",
				true,
				"Produces fresh or salt water instead of vanilla water. Cannot passively produce salt water.");
		config.save();
	}
	
	private void addRecipes() {
		for (Material m : materials) {
			unshapedMetalRecipe(m);
			nuggetIngotRecipe(m);
		}
		tankRecipes();
		coilRecipes();
		machineRecipes();
	}
	
	private void machineRecipes() {
		ItemStack[] augs = new ItemStack[] {};
		String machineFrame = "thermalexpansion:machineFrame";
		if (MachineConfig.extruderEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(BlockTFTMachine.extruder, augs, new Object[] {
					" p ",
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
					TEItems.pneumaticServo
			}));
			
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler.addMachineUpgradeRecipes(BlockTFTMachine.extruder);
			} else {
				TECraftingHandler.addMachineUpgradeRecipes(BlockTFTMachine.extruder);
			}
			
			TECraftingHandler.addSecureRecipe(BlockTFTMachine.extruder);
		} else if (RecipeConfig.upgradeCrafting) {
			TFTCraftingHandler.addMachineUpgradeRecipes(BlockMachine.extruder);
		}
		Object[] accumulator = new Object[] {
				" b ",
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
				TEItems.pneumaticServo
		};
		
		if (MachineConfig.accumulatorEnabled) {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(BlockTFTMachine.accumulator, augs, accumulator));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler.addMachineUpgradeRecipes(BlockTFTMachine.accumulator);
			} else {
				TECraftingHandler.addMachineUpgradeRecipes(BlockTFTMachine.accumulator);
			}
			
			TECraftingHandler.addSecureRecipe(BlockTFTMachine.extruder);
		} else {
			NEIRecipeWrapper.addMachineRecipe(new RecipeMachine(BlockMachine.accumulator, augs, accumulator));
			if (RecipeConfig.upgradeCrafting) {
				TFTCraftingHandler.addMachineUpgradeRecipes(BlockMachine.accumulator);
			}
		}
		
	}
	
	private void coilRecipes() {
		if (RecipeConfig.coilsEnabled) {
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.powerCoilElectrum, new Object[] {
					"r  ", " E ", "  r", Character.valueOf('E'), "rodElectrum", Character.valueOf('r'), "dustRedstone"
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.powerCoilSilver, new Object[] {
					"  r", " S ", "r  ", Character.valueOf('S'), "rodSilver", Character.valueOf('r'), "dustRedstone"
			}));
			GameRegistry.addRecipe(new ShapedOreRecipe(TEItems.powerCoilGold, new Object[] {
					"  r", " G ", "r  ", Character.valueOf('G'), "rodGold", Character.valueOf('r'), "dustRedstone"
			}));
		}
		
	}
	
	private void tankRecipes() {
		if (RecipeConfig.tanksEnabled) {
			for (int i = 0; i < tankMap.length; i++) {
				TankMap t = tankMap[i];
				ItemStack frame = BlockTankFrame.getItemStack(t.type, BlockTankFrame.Stages.frame);
				// Finished tanks are made by surrounding a tank frame with 4 glass blocks
				GameRegistry.addRecipe(new ShapedOreRecipe(t.finished, new Object[] {
						" G ", "GFG", " G ", Character.valueOf('G'), "blockGlass", Character.valueOf('F'), frame
				}));
				if (i > 0) {
					TankMap prev = tankMap[i - 1];
					// Finished tanks can be made with previous tier finished + sheet
					GameRegistry.addRecipe(new RecipeShapelessUpgrade(t.finished, prev.finished, new Object[] {
							prev.finished, t.sheet2x
					}));
				}
			}
		}
	}
	
	private void nuggetIngotRecipe(Material m) {
		GameRegistry.addRecipe(new ShapedOreRecipe(m.ingot, new Object[] {
				"nnn", "nnn", "nnn", Character.valueOf('n'), "nugget" + m.oreName
		}));
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
		
		GameRegistry.registerBlock(TFTBlocks.tankFrame, ItemBlockTankFrame.class, "TankFrame");
		
		// Goes through all the tank types & stages and creates an itemstack for each one
		BlockTank.Types[] types = BlockTank.Types.values();
		BlockTankFrame.Stages[] stages = BlockTankFrame.Stages.values();
		int tankMapIndex = 0;
		for (int i = 0; i < types.length; i++) {
			BlockTank.Types t = types[i];
			
			for (int j = 0; j < stages.length; j++) {
				BlockTankFrame.Stages s = stages[j];
				
				String name = t.name() + s.name();
				ItemStack frame = new ItemStack(TFTBlocks.tankFrame, 1, BlockTankFrame.getMeta(t, s));
				
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
		mat.unshaped = new ItemMeltedMetal().setUnlocalizedName(mat.name + "Unshaped");
		
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
		mat.ingot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName(mat.name + "Ingot2x")).setSize(EnumSize.LARGE).setMetal(mat.name,
				200);
		
		TFTItems.ingots2x.put(mat.name, mat.ingot2x);
		GameRegistry.registerItem(mat.ingot2x, mat.name + "Ingot2x");
		OreDictionary.registerOre("ingotDouble" + mat.name, mat.ingot2x);
	}
	
	private void addSheets(Material mat) {
		mat.sheet = ((ItemTFTMetalSheet) new ItemTFTMetalSheet(mat.name).setUnlocalizedName(mat.name + "Sheet")).setMetal(mat.name,
				200);
		
		TFTItems.sheets.put(mat.name, mat.sheet);
		GameRegistry.registerItem(mat.sheet, mat.name + "Sheet");
		OreDictionary.registerOre("plate" + mat.name, mat.sheet);
		
	}
	
	private void addDoubleSheets(Material mat) {
		mat.sheet2x = ((ItemTFTMetalSheet) new ItemTFTMetalSheet(mat.name).setUnlocalizedName(mat.name + "Sheet2x")).setMetal(mat.name,
				400);
		
		TFTItems.sheets2x.put(mat.name, mat.sheet2x);
		GameRegistry.registerItem(mat.sheet2x, mat.name + "Sheet2x");
		OreDictionary.registerOre("plateDouble" + mat.name, mat.sheet2x);
	}
	
	private void addRod(Material mat) {
		mat.rod = new ItemRod(mat.metal.name).setUnlocalizedName(mat.name + "Rod");
		
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
		addHeat(manager, mat.gear.getItem(), mat.heatRaw, mat.unshaped, 4);
		
	}
	
	private void addHeat(HeatRegistry manager, Item metal, HeatRaw raw, Item unshaped, int quantity) {
		manager.addIndex(new HeatIndex(new ItemStack(metal, 1), raw, new ItemStack(unshaped, quantity)));
		
	}
	
	private void registerAlloy(Material material) {
		
		if (material instanceof MaterialAlloy) {
			MaterialAlloy mat = (MaterialAlloy) material;
			Alloy alloy = new Alloy(mat.metal, mat.alloyTier);
			for (AlloyIngred ing : mat.alloy) {
				alloy.addIngred(MetalRegistry.instance.getMetalFromString(ing.name), ing.min, ing.max);
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
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1, BlockTank.Types.BASIC.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1, BlockTank.Types.HARDENED.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1, BlockTank.Types.REINFORCED.ordinal()));
			batch.addCrafting(new ItemStack(TEBlocks.blockTank, 1, BlockTank.Types.RESONANT.ordinal()));
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
		if (MachineConfig.extruderEnabled) {
			batch.addCrafting(BlockMachine.extruder);
		}
		
		batch.addCrafting(BlockMachine.accumulator);
	}
	
	private void removeNuggetIngotRecipes(RemoveBatch batch, Material m) {
		if (m.nugget != null) {
			batch.addCrafting(new ItemStack(m.ingot), new ItemStack[] {
				m.nugget
			});
		}
	}
	
	private void initHeatMap() {
		// Stock TFC
		heatMap.put("Gold", new HeatRaw(0.6, 1060));
		heatMap.put("WroughtIron", new HeatRaw(0.35, 1535));
		heatMap.put("Copper", new HeatRaw(0.35, 1080));
		heatMap.put("Tin", new HeatRaw(0.14, 230));
		heatMap.put("Silver", new HeatRaw(0.48, 961));
		heatMap.put("Lead", new HeatRaw(0.22, 328));
		heatMap.put("Nickel", new HeatRaw(0.48, 1453));
		heatMap.put("Platinum", new HeatRaw(0.35, 1730));
		heatMap.put("Bronze", new HeatRaw(0.35, 950));
		
		// TF
		heatMap.put("Invar", new HeatRaw(0.52, 1700));
		heatMap.put("Mithril", new HeatRaw(0.9, 660));
		heatMap.put("Electrum", new HeatRaw(0.181, 650));
		heatMap.put("Enderium", new HeatRaw(0.5, 1700));
		heatMap.put("Signalum", new HeatRaw(0.45, 1300));
		heatMap.put("Lumium", new HeatRaw(0.35, 1200));
	}
	
	private TankMap[] getTanks() {
		return new TankMap[] {
				new TankMap("Copper", BlockTank.Types.BASIC),
				new TankMap("Invar", BlockTank.Types.HARDENED),
				new TankMap(new ItemStack(TEBlocks.blockGlass, 1),
						5,
						BlockTank.Types.REINFORCED,
						TFTechness.heatMap.get("Invar")), new TankMap("Enderium", BlockTank.Types.RESONANT)
		};
	}
	
	private Map<String, Material> getMatMap() {
		Map<String, Material> m = new HashMap();
		for (Material mat : materials) {
			m.put(mat.name, mat);
		}
		return m;
	}
	
	private Material[] getMaterials() {
		return new Material[] {
		new Material("Gold",
				TFCItems.goldUnshaped,
				TFCItems.goldIngot,
				TFCItems.goldSheet2x,
				TFItems.gearGold,
				2,
				Global.GOLD,
				new ItemStack(Items.gold_nugget)),
				new Material("WroughtIron",
						"Iron",
						TFCItems.wroughtIronUnshaped,
						TFCItems.wroughtIronIngot,
						TFCItems.wroughtIronSheet2x,
						TFItems.gearIron,
						3,
						Global.WROUGHTIRON,
						TFItems.nuggetIron),
				new Material("Copper",
						TFCItems.copperUnshaped,
						TFCItems.copperIngot,
						TFCItems.copperSheet2x,
						TFItems.gearCopper,
						1,
						Global.COPPER,
						TFItems.nuggetCopper),
				new Material("Tin",
						TFCItems.tinUnshaped,
						TFCItems.tinIngot,
						TFCItems.tinSheet2x,
						TFItems.gearTin,
						0,
						Global.TIN,
						TFItems.nuggetTin),
				new Material("Silver",
						TFCItems.silverUnshaped,
						TFCItems.silverIngot,
						TFCItems.silverSheet2x,
						TFItems.gearSilver,
						2,
						Global.SILVER,
						TFItems.nuggetSilver),
				new Material("Lead",
						TFCItems.leadUnshaped,
						TFCItems.leadIngot,
						TFCItems.leadSheet2x,
						TFItems.gearLead,
						2,
						Global.LEAD,
						TFItems.nuggetLead),
				new Material("Nickel",
						TFCItems.nickelUnshaped,
						TFCItems.nickelIngot,
						TFCItems.nickelSheet2x,
						TFItems.gearNickel,
						4,
						Global.NICKEL,
						TFItems.nuggetNickel),
				new Material("Platinum",
						TFCItems.platinumUnshaped,
						TFCItems.platinumIngot,
						TFCItems.platinumSheet2x,
						TFItems.gearPlatinum,
						3,
						Global.PLATINUM,
						TFItems.nuggetPlatinum),
				new Material("Bronze",
						TFCItems.bronzeUnshaped,
						TFCItems.bronzeIngot,
						TFCItems.bronzeSheet2x,
						TFItems.gearBronze,
						2,
						Global.BRONZE,
						TFItems.nuggetBronze),
				new MaterialAlloy("Invar", TFItems.gearInvar, 4, Alloy.EnumTier.TierIII, new AlloyIngred[] {
						new AlloyIngred("Wrought Iron", 61.00f, 67.00f), new AlloyIngred("Nickel", 33.00f, 39.00f)
				}, TFItems.nuggetInvar),
				new Material("Mithril", TFItems.gearMithril, 4, Alloy.EnumTier.TierIII, TFItems.nuggetMithril),
				new MaterialAlloy("Electrum", TFItems.gearElectrum, 5, Alloy.EnumTier.TierIV, new AlloyIngred[] {
						new AlloyIngred("Gold", 50.00f, 60.00f), new AlloyIngred("Silver", 40.00f, 50.00f)
				}, TFItems.nuggetElectrum),
				new Material("Enderium", TFItems.gearEnderium, 6, Alloy.EnumTier.TierV, TFItems.nuggetEnderium),
				new Material("Signalum", TFItems.gearSignalum, 5, Alloy.EnumTier.TierIV, TFItems.nuggetSignalum),
				new Material("Lumium", TFItems.gearLumium, 5, Alloy.EnumTier.TierIV, TFItems.nuggetLumium)
		};
	}
}
