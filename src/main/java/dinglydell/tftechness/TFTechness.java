package dinglydell.tftechness;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.LogManager;

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
import com.bioxx.tfc.api.TFCItems;
import com.bioxx.tfc.api.Constant.Global;
import com.bioxx.tfc.api.Enums.EnumSize;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = TFTechness.MODID, version = TFTechness.VERSION, dependencies = "required-after:terrafirmacraft;required-after:ThermalExpansion;required-after:ThermalFoundation")
public class TFTechness {
	public static final String MODID = "TFTechness";
	public static final String VERSION = "0.1";
	public static org.apache.logging.log4j.Logger logger = LogManager.getLogger("TFTechness");
	public static Material[] materials;
	public static Map<String, HeatRaw> heatMap = new HashMap();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		initHeatMap();
		Configuration config = new Configuration(new File(event.getModConfigurationDirectory(),
				"/TFTechness/Metals.cfg"));
		config.load();
		
		for (Map.Entry<String, HeatRaw> entry : heatMap.entrySet()) {
			ConfigCategory material = config.getCategory(entry.getKey());
			HeatRaw heat = entry.getValue();
			double melt = material.get("MetlingPoint").getDouble(heat.meltTemp);
			double sh = material.get("SpecificHeat").getDouble(heat.specificHeat);
			
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
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		addMetals();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new AnvilRecipeHandler());
		RemoveBatch batch = new RemoveBatch();
		removeGearRecipes(batch);
		batch.Execute();
		
	}
	
	private void addMetals() {
		addUnshaped();
		addIngots();
		addDoubleIngots();
		addSheets();
		addDoubleSheets();
		registerMetals();
		registerAlloys();
		registerHeat();
	}
	
	private void addUnshaped() {
		TFTItems.invarUnshaped = new ItemMeltedMetal().setUnlocalizedName("InvarUnshaped");
		TFTItems.mithrilUnshaped = new ItemMeltedMetal().setUnlocalizedName("MithrilUnshaped");
		TFTItems.electrumUnshaped = new ItemMeltedMetal().setUnlocalizedName("ElectrumUnshaped");
		TFTItems.enderiumUnshaped = new ItemMeltedMetal().setUnlocalizedName("EnderiumUnshaped");
		TFTItems.signalumUnshaped = new ItemMeltedMetal().setUnlocalizedName("SignalumUnshaped");
		
		GameRegistry.registerItem(TFTItems.invarUnshaped, "Unshaped Invar");
		GameRegistry.registerItem(TFTItems.mithrilUnshaped, "Unshaped Mana Infused Metal");
		GameRegistry.registerItem(TFTItems.electrumUnshaped, "Unshaped Electrum");
		GameRegistry.registerItem(TFTItems.enderiumUnshaped, "Unshaped Enderium");
		GameRegistry.registerItem(TFTItems.signalumUnshaped, "Unshaped Signalum");
	}
	
	private void addIngots() {
		TFTItems.invarIngot = new ItemIngot().setUnlocalizedName("InvarIngot");
		TFTItems.mithrilIngot = new ItemIngot().setUnlocalizedName("MithrilIngot");
		TFTItems.electrumIngot = new ItemIngot().setUnlocalizedName("ElectrumIngot");
		TFTItems.enderiumIngot = new ItemIngot().setUnlocalizedName("EnderiumIngot");
		TFTItems.signalumIngot = new ItemIngot().setUnlocalizedName("SignalumIngot");
		
		GameRegistry.registerItem(TFTItems.invarIngot, "Invar Ingot");
		GameRegistry.registerItem(TFTItems.mithrilIngot, "Mana Infused Ingot");
		GameRegistry.registerItem(TFTItems.electrumIngot, "Electrum Ingot");
		GameRegistry.registerItem(TFTItems.enderiumIngot, "Enderium Ingot");
		GameRegistry.registerItem(TFTItems.signalumIngot, "Signalum Ingot");
		
		OreDictionary.registerOre("ingotInvar", TFTItems.invarIngot);
		OreDictionary.registerOre("ingotMithril", TFTItems.mithrilIngot);
		OreDictionary.registerOre("ingotElectrum", TFTItems.electrumIngot);
		OreDictionary.registerOre("ingotEnderium", TFTItems.enderiumIngot);
		OreDictionary.registerOre("ingotSignalum", TFTItems.signalumIngot);
	}
	
	private void addDoubleIngots() {
		TFTItems.invarIngot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName("InvarIngot2x")).setSize(EnumSize.LARGE).setMetal("Invar",
				200);
		TFTItems.mithrilIngot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName("MithrilIngot2x")).setSize(EnumSize.LARGE).setMetal("Mithril",
				200);
		TFTItems.electrumIngot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName("ElectrumIngot2x")).setSize(EnumSize.LARGE).setMetal("Electrum",
				200);
		TFTItems.enderiumIngot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName("EnderiumIngot2x")).setSize(EnumSize.LARGE).setMetal("Enderium",
				200);
		TFTItems.signalumIngot2x = ((ItemIngot) new ItemIngot().setUnlocalizedName("SignalumIngot2x")).setSize(EnumSize.LARGE).setMetal("Signalum",
				200);
		
		GameRegistry.registerItem(TFTItems.invarIngot2x, "Double Invar Ingot");
		GameRegistry.registerItem(TFTItems.mithrilIngot2x, "Double Mana Infused Ingot");
		GameRegistry.registerItem(TFTItems.electrumIngot2x, "Double Electrum Ingot");
		GameRegistry.registerItem(TFTItems.enderiumIngot2x, "Double Enderium Ingot");
		GameRegistry.registerItem(TFTItems.signalumIngot2x, "Double Signalum Ingot");
		
		OreDictionary.registerOre("ingotDoubleInvar", TFTItems.invarIngot2x);
		OreDictionary.registerOre("ingotDoubleMithril", TFTItems.mithrilIngot2x);
		OreDictionary.registerOre("ingotDoubleElectrum", TFTItems.electrumIngot2x);
		OreDictionary.registerOre("ingotDoubleEnderium", TFTItems.enderiumIngot2x);
		OreDictionary.registerOre("ingotDoubleSignalum", TFTItems.signalumIngot2x);
	}
	
	private void addSheets() {
		TFTItems.invarSheet = ((ItemMetalSheet) new ItemMetalSheet(25).setUnlocalizedName("InvarSheet")).setMetal("Invar",
				200);
		TFTItems.mithrilSheet = ((ItemMetalSheet) new ItemMetalSheet(26).setUnlocalizedName("MithrilSheet")).setMetal("Mithril",
				200);
		TFTItems.electrumSheet = ((ItemMetalSheet) new ItemMetalSheet(27).setUnlocalizedName("ElectrumSheet")).setMetal("Electrum",
				200);
		TFTItems.enderiumSheet = ((ItemMetalSheet) new ItemMetalSheet(28).setUnlocalizedName("EnderiumSheet")).setMetal("Enderium",
				200);
		TFTItems.signalumSheet = ((ItemMetalSheet) new ItemMetalSheet(29).setUnlocalizedName("SignalumSheet")).setMetal("Signalum",
				200);
		
		GameRegistry.registerItem(TFTItems.invarSheet, "Invar Sheet");
		GameRegistry.registerItem(TFTItems.mithrilSheet, "Mana Infused Sheet");
		GameRegistry.registerItem(TFTItems.electrumSheet, "Electrum Sheet");
		GameRegistry.registerItem(TFTItems.enderiumSheet, "Enderium Sheet");
		GameRegistry.registerItem(TFTItems.signalumSheet, "Signalum Sheet");
		
		OreDictionary.registerOre("plateInvar", TFTItems.invarSheet);
		OreDictionary.registerOre("plateMithril", TFTItems.mithrilSheet);
		OreDictionary.registerOre("plateElectrum", TFTItems.electrumSheet);
		OreDictionary.registerOre("plateEnderium", TFTItems.enderiumSheet);
		OreDictionary.registerOre("plateSignalum", TFTItems.signalumSheet);
		
	}
	
	private void addDoubleSheets() {
		TFTItems.invarSheet2x = ((ItemMetalSheet) new ItemMetalSheet(25).setUnlocalizedName("InvarSheet2x")).setMetal("Invar",
				400);
		TFTItems.mithrilSheet2x = ((ItemMetalSheet) new ItemMetalSheet(26).setUnlocalizedName("MithrilSheet2x")).setMetal("Mithril",
				400);
		TFTItems.electrumSheet2x = ((ItemMetalSheet) new ItemMetalSheet(27).setUnlocalizedName("ElectrumSheet2x")).setMetal("Electrum",
				400);
		TFTItems.enderiumSheet2x = ((ItemMetalSheet) new ItemMetalSheet(28).setUnlocalizedName("EnderiumSheet2x")).setMetal("Enderium",
				400);
		TFTItems.signalumSheet2x = ((ItemMetalSheet) new ItemMetalSheet(29).setUnlocalizedName("SignalumSheet2x")).setMetal("Signalum",
				400);
		
		GameRegistry.registerItem(TFTItems.invarSheet2x, "Invar Double Sheet");
		GameRegistry.registerItem(TFTItems.mithrilSheet2x, "Mana Infused Double Sheet");
		GameRegistry.registerItem(TFTItems.electrumSheet2x, "Electrum Double Sheet");
		GameRegistry.registerItem(TFTItems.enderiumSheet2x, "Enderium Double Sheet");
		GameRegistry.registerItem(TFTItems.signalumSheet2x, "Signalum Double Sheet");
		
		OreDictionary.registerOre("plateDoubleInvar", TFTItems.invarSheet2x);
		OreDictionary.registerOre("plateDoubleMithril", TFTItems.mithrilSheet2x);
		OreDictionary.registerOre("plateDoubleElectrum", TFTItems.electrumSheet2x);
		OreDictionary.registerOre("plateDoubleEnderium", TFTItems.enderiumSheet2x);
		OreDictionary.registerOre("plateDoubleSignalum", TFTItems.signalumSheet2x);
		
	}
	
	private void registerHeat() {
		HeatRegistry manager = HeatRegistry.getInstance();
		
		materials = getMaterials();
		for (int i = 0; i < materials.length; i++) {
			registerMetalHeat(manager, materials[i]);
		}
		
	}
	
	private void registerMetalHeat(HeatRegistry manager, Material mat) {
		if (!mat.gearOnly) {
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
	
	private void registerAlloys() {
		// Invar
		Alloy invar = new Alloy(TFTMetals.invar, Alloy.EnumTier.TierIV);
		invar.addIngred(Global.WROUGHTIRON, 61.00f, 67.00f);
		invar.addIngred(Global.NICKEL, 33.00f, 39.00f);
		AlloyManager.INSTANCE.addAlloy(invar);
		
		// Electrum
		Alloy electrum = new Alloy(TFTMetals.electrum, Alloy.EnumTier.TierIII);
		electrum.addIngred(Global.GOLD, 50.00f, 60.00f);
		electrum.addIngred(Global.SILVER, 40.00f, 50.00f);
		AlloyManager.INSTANCE.addAlloy(electrum);
	}
	
	private void registerMetals() {
		TFTMetals.invar = new Metal("Invar", TFTItems.invarUnshaped, TFTItems.invarIngot);
		TFTMetals.mithril = new Metal("Mithril", TFTItems.mithrilUnshaped, TFTItems.mithrilIngot);
		TFTMetals.electrum = new Metal("Electrum", TFTItems.electrumUnshaped, TFTItems.electrumIngot);
		TFTMetals.enderium = new Metal("Enderium", TFTItems.enderiumUnshaped, TFTItems.enderiumIngot);
		TFTMetals.signalum = new Metal("Signalum", TFTItems.signalumUnshaped, TFTItems.signalumIngot);
		
		MetalRegistry.instance.addMetal(TFTMetals.invar, Alloy.EnumTier.TierIV);
		MetalRegistry.instance.addMetal(TFTMetals.mithril, Alloy.EnumTier.TierIV);
		MetalRegistry.instance.addMetal(TFTMetals.electrum, Alloy.EnumTier.TierV);
		MetalRegistry.instance.addMetal(TFTMetals.enderium, Alloy.EnumTier.TierVI);
		MetalRegistry.instance.addMetal(TFTMetals.signalum, Alloy.EnumTier.TierV);
		
	}
	
	private void removeGearRecipes(RemoveBatch batch) {
		// TODO: Invar*, Electrum*, Enderium*, Mana infused (mithril)*, signalum, lumium
		// *done, but untextured
		materials = getMaterials();
		for (int i = 0; i < materials.length; i++) {
			batch.addCrafting(materials[i].gear);
		}
		
	}
	
	private Material[] getMaterials() {
		return new Material[] {
		new Material("Gold", TFCItems.goldUnshaped, TFCItems.goldSheet2x, TFItems.gearGold, 2),
				new Material("WroughtIron", TFCItems.wroughtIronUnshaped, TFCItems.wroughtIronSheet2x,
						TFItems.gearIron, 3),
				new Material("Copper", TFCItems.copperUnshaped, TFCItems.copperSheet2x, TFItems.gearCopper, 1),
				new Material("Tin", TFCItems.tinUnshaped, TFCItems.tinSheet2x, TFItems.gearTin, 0),
				new Material("Silver", TFCItems.silverUnshaped, TFCItems.silverSheet2x, TFItems.gearSilver, 2),
				new Material("Lead", TFCItems.leadUnshaped, TFCItems.leadSheet2x, TFItems.gearLead, 2),
				new Material("Nickel", TFCItems.nickelUnshaped, TFCItems.nickelSheet2x, TFItems.gearNickel, 4),
				new Material("Platinum", TFCItems.platinumUnshaped, TFCItems.platinumSheet2x, TFItems.gearPlatinum, 3),
				new Material("Bronze", TFCItems.bronzeUnshaped, TFCItems.bronzeSheet2x, TFItems.gearBronze, 2),
				new Material("Invar", TFTItems.invarUnshaped, TFTItems.invarIngot, TFTItems.invarIngot2x,
						TFTItems.invarSheet, TFTItems.invarSheet2x, TFItems.gearInvar, 4),
				new Material("Mithril", TFTItems.mithrilUnshaped, TFTItems.mithrilIngot, TFTItems.mithrilIngot2x,
						TFTItems.mithrilSheet, TFTItems.mithrilSheet2x, TFItems.gearMithril, 4),
				new Material("Electrum", TFTItems.electrumUnshaped, TFTItems.electrumIngot, TFTItems.electrumIngot2x,
						TFTItems.electrumSheet, TFTItems.electrumSheet2x, TFItems.gearElectrum, 5),
				new Material("Enderium", TFTItems.enderiumUnshaped, TFTItems.enderiumIngot, TFTItems.enderiumIngot2x,
						TFTItems.enderiumSheet, TFTItems.enderiumSheet2x, TFItems.gearEnderium, 6),
				new Material("Signalum", TFTItems.signalumUnshaped, TFTItems.signalumIngot, TFTItems.signalumIngot2x,
						TFTItems.signalumSheet, TFTItems.signalumSheet2x, TFItems.gearSignalum, 5)
		};
	}
}
