package dinglydell.tftechness.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cofh.thermalexpansion.item.TEAugments;
import cofh.thermalexpansion.item.TEItems;
import cpw.mods.fml.common.registry.GameRegistry;

public class TFTAugments {

	public static void init() {

		for (int i = 0; i < cryoUpgrades.length; i++) {
			cryoUpgrades[i] = TEAugments.itemAugment.addItem(512 + i,
					CRYO_UPGRADE + i);
			TEAugments.itemAugment.addAugmentData(512 + i,
					CRYO_UPGRADE,
					i + 1,
					3);
		}

		rfforgeAutoTemp = TEAugments.itemAugment
				.addItem(515, RFFORGE_AUTO_TEMP);
		TEAugments.itemAugment.addAugmentData(515, RFFORGE_AUTO_TEMP, 1, 3);

		rfforgeAutoEject = TEAugments.itemAugment.addItem(516,
				RFFORGE_AUTO_EJECT);
		TEAugments.itemAugment.addAugmentData(516, RFFORGE_AUTO_EJECT, 1, 3);

	}

	public static void addRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[0],
				new Object[] { "   ",
						" R ",
						"rBr",
						Character.valueOf('R'),
						TEItems.powerCoilGold,
						Character.valueOf('r'),
						"dustRedstone",
						Character.valueOf('B'),
						"plateDoubleBronze" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[1],
				new Object[] { " G ",
						" R ",
						"dBd",
						Character.valueOf('R'),
						TEItems.powerCoilGold,
						Character.valueOf('d'),
						"dustBlizz",
						Character.valueOf('B'),
						"plateBronze",
						Character.valueOf('G'),
						"plateGold" }));
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[2],
				new Object[] { "   ",
						" R ",
						"dEd",
						Character.valueOf('R'),
						TEItems.powerCoilGold,
						Character.valueOf('d'),
						"dustCryotheum",
						Character.valueOf('E'),
						"plateDoubleElectrum" }));

		GameRegistry.addRecipe(new ShapedOreRecipe(rfforgeAutoTemp,
				new Object[] { "   ",
						" S ",
						"dEd",
						Character.valueOf('S'),
						"dustSulfur",
						Character.valueOf('d'),
						"dustRedstone",
						Character.valueOf('E'),
						"plateElectrum" }));

		GameRegistry.addRecipe(new ShapedOreRecipe(rfforgeAutoEject,
				new Object[] { "   ",
						" S ",
						"dGd",
						Character.valueOf('S'),
						"dustSulfur",
						Character.valueOf('d'),
						"dustRedstone",
						Character.valueOf('G'),
						"plateGold" }));

	}

	public static ItemStack rfforgeAutoTemp;
	public static ItemStack rfforgeAutoEject;
	public static ItemStack[] cryoUpgrades = new ItemStack[3];
	public static final String CRYO_UPGRADE = "tftCryoEfficiency";
	public static final String RFFORGE_AUTO_TEMP = "tftRFForgeAutoTemp";
	public static final String RFFORGE_AUTO_EJECT = "tftRFForgeAutoEject";

	public static final double[] CRYO_UPGRADE_ENERGY_MOD = { 1, 1.5, 2, 3 };
	public static final float[] CRYO_UPGRADE_INEFFICIENCY_MOD = { 1,
			1.1f,
			1.2f,
			1.3f };

	/**
	 * The auto temperature augment will always keep the target temperature this
	 * much below the melting temperature
	 */
	public static final int AUTO_TEMP_THRESHHOLD = 1;

}
