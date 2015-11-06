package dinglydell.tftechness;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;

import cofh.thermalexpansion.block.TEBlocks;
import cofh.thermalexpansion.block.tank.BlockTank;
import cofh.thermalfoundation.item.TFItems;

import com.bioxx.tfc.Core.Metal.Alloy;
import com.bioxx.tfc.Core.Metal.AlloyManager;
import com.bioxx.tfc.Core.Metal.MetalRegistry;
import com.bioxx.tfc.Items.ItemIngot;
import com.bioxx.tfc.Items.ItemMeltedMetal;
import com.bioxx.tfc.Items.ItemMetalSheet;
import com.bioxx.tfc.api.HeatIndex;
import com.bioxx.tfc.api.HeatRaw;
import com.bioxx.tfc.api.HeatRegistry;
import com.bioxx.tfc.api.Metal;
import com.bioxx.tfc.api.TFCBlocks;
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
import dinglydell.tftechness.item.TFTItems;
import dinglydell.tftechness.metal.AlloyIngred;
import dinglydell.tftechness.metal.Material;
import dinglydell.tftechness.metal.MaterialAlloy;
import dinglydell.tftechness.metal.MetalSnatcher;
import dinglydell.tftechness.metal.TFTMetals;
import dinglydell.tftechness.metal.TankMap;
import dinglydell.tftechness.recipe.AnvilRecipeHandler;
import dinglydell.tftechness.recipe.RecipeConfig;
import dinglydell.tftechness.recipe.RemoveBatch;

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
		addMetals();
		fixSheets();
		
	}
	
	private void fixSheets() {
		TFCBlocks.metalSheet = new BlockTFTMetalSheet().setBlockName("MetalSheet").setHardness(80);
		
		GameRegistry.registerBlock(TFCBlocks.metalSheet, "MetalSheet");
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
		config.save();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Requires some TE init
		addTanks();
	}
	
	private void addTanks() {
		tankMap = getTanks();
		
		registerTanks();
		registerTankHeat();
		
	}
	
	private void registerTanks() {
		TFTBlocks.tankFrame = new BlockTankFrame();
		
		GameRegistry.registerBlock(TFTBlocks.tankFrame, ItemBlockTankFrame.class, "Tank");
		
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
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new AnvilRecipeHandler());
		RemoveBatch batch = new RemoveBatch();
		removeGearRecipes(batch);
		removeTankRecipes(batch);
		batch.Execute();
		
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
				int id = MetalSnatcher.getIdFromMetal(mat.metal);
				addSheets(mat, id);
				addDoubleSheets(mat, id);
			}
			registerHeat(mat);
			registerAlloy(mat);
		}
		
	}
	
	private void addUnshaped(Material mat) {
		mat.unshaped = new ItemMeltedMetal().setUnlocalizedName(mat.name + "Unshaped");
		
		TFTItems.unshaped.put(mat.name, mat.unshaped);
		GameRegistry.registerItem(mat.unshaped, "Unshaped " + mat.name);
	}
	
	private void addIngots(Material mat) {
		mat.ingot = new ItemIngot().setUnlocalizedName(mat.name + "Ingot");
		
		TFTItems.ingots.put(mat.name, mat.ingot);
		GameRegistry.registerItem(mat.ingot, mat.name + " Ingot");
		OreDictionary.registerOre("ingot" + mat.name, mat.ingot);
		
	}
	
	private void addDoubleIngots(Material mat) {
		mat.ingot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName(mat.name + "Ingot2x")).setSize(EnumSize.LARGE).setMetal(mat.name,
				200);
		
		TFTItems.ingots2x.put(mat.name, mat.ingot2x);
		GameRegistry.registerItem(mat.ingot2x, "Double " + mat.name + " Ingot");
		OreDictionary.registerOre("ingotDouble" + mat.name, mat.ingot2x);
	}
	
	private void addSheets(Material mat, int id) {
		mat.sheet = ((ItemMetalSheet) new ItemMetalSheet(id).setUnlocalizedName(mat.name + "Sheet")).setMetal(mat.name,
				200);
		
		TFTItems.sheets.put(mat.name, mat.sheet);
		GameRegistry.registerItem(mat.sheet, mat.name + " Sheet");
		OreDictionary.registerOre("plate" + mat.name, mat.sheet);
		
	}
	
	private void addDoubleSheets(Material mat, int id) {
		mat.sheet2x = ((ItemMetalSheet) new ItemMetalSheet(id).setUnlocalizedName(mat.name + "Sheet2x")).setMetal(mat.name,
				400);
		
		TFTItems.sheets2x.put(mat.name, mat.sheet2x);
		GameRegistry.registerItem(mat.sheet2x, mat.name + " Double Sheet");
		OreDictionary.registerOre("plateDouble" + mat.name, mat.sheet2x);
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
		
		// // Invar
		// Alloy invar = new Alloy(TFTMetals.metals.get("Invar"), mats.get(mats.in));
		// invar.addIngred(Global.WROUGHTIRON, 61.00f, 67.00f);
		// invar.addIngred(Global.NICKEL, 33.00f, 39.00f);
		// AlloyManager.INSTANCE.addAlloy(invar);
		//
		// // Electrum
		// Alloy electrum = new Alloy(TFTMetals.metals.get("Electrum"), Alloy.EnumTier.TierIII);
		// electrum.addIngred(Global.GOLD, 50.00f, 60.00f);
		// electrum.addIngred(Global.SILVER, 40.00f, 50.00f);
		// AlloyManager.INSTANCE.addAlloy(electrum);
	}
	
	private void registerMetal(Material mat) {
		mat.metal = new Metal(mat.name, mat.unshaped, mat.ingot);
		TFTMetals.metals.put(mat.name, mat.metal);
		MetalRegistry.instance.addMetal(mat.metal, mat.alloyTier);
		
	}
	
	private void removeGearRecipes(RemoveBatch batch) {
		// TODO: Invar*, Electrum*, Enderium*, Mana infused (mithril)*, signalum*, lumium*
		// *done, but untextured
		if (RecipeConfig.gearsEnabled) {
			for (int i = 0; i < materials.length; i++) {
				batch.addCrafting(materials[i].gear);
			}
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
		new Material("Gold", TFCItems.goldUnshaped, TFCItems.goldSheet2x, TFItems.gearGold, 2, Global.GOLD),
				new Material("WroughtIron",
						TFCItems.wroughtIronUnshaped,
						TFCItems.wroughtIronSheet2x,
						TFItems.gearIron,
						3,
						Global.WROUGHTIRON),
				new Material("Copper",
						TFCItems.copperUnshaped,
						TFCItems.copperSheet2x,
						TFItems.gearCopper,
						1,
						Global.COPPER),
				new Material("Tin", TFCItems.tinUnshaped, TFCItems.tinSheet2x, TFItems.gearTin, 0, Global.TIN),
				new Material("Silver",
						TFCItems.silverUnshaped,
						TFCItems.silverSheet2x,
						TFItems.gearSilver,
						2,
						Global.SILVER),
				new Material("Lead", TFCItems.leadUnshaped, TFCItems.leadSheet2x, TFItems.gearLead, 2, Global.LEAD),
				new Material("Nickel",
						TFCItems.nickelUnshaped,
						TFCItems.nickelSheet2x,
						TFItems.gearNickel,
						4,
						Global.NICKEL),
				new Material("Platinum",
						TFCItems.platinumUnshaped,
						TFCItems.platinumSheet2x,
						TFItems.gearPlatinum,
						3,
						Global.PLATINUM),
				new Material("Bronze",
						TFCItems.bronzeUnshaped,
						TFCItems.bronzeSheet2x,
						TFItems.gearBronze,
						2,
						Global.BRONZE),
				new MaterialAlloy("Invar", TFItems.gearInvar, 4, Alloy.EnumTier.TierIII, new AlloyIngred[] {
						new AlloyIngred("Wrought Iron", 61.00f, 67.00f), new AlloyIngred("Nickel", 33.00f, 39.00f)
				}),
				new Material("Mithril", TFItems.gearMithril, 4, Alloy.EnumTier.TierIII),
				new MaterialAlloy("Electrum", TFItems.gearElectrum, 5, Alloy.EnumTier.TierIV, new AlloyIngred[] {
						new AlloyIngred("Gold", 50.00f, 60.00f), new AlloyIngred("Silver", 40.00f, 50.00f)
				}),
				new Material("Enderium", TFItems.gearEnderium, 6, Alloy.EnumTier.TierV),
				new Material("Signalum", TFItems.gearSignalum, 5, Alloy.EnumTier.TierIV),
				new Material("Lumium", TFItems.gearLumium, 5, Alloy.EnumTier.TierIV)
		};
	}
}
