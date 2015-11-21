package dinglydell.tftechness.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cofh.thermalexpansion.item.TEAugments;
import cofh.thermalexpansion.item.TEItems;
import cpw.mods.fml.common.registry.GameRegistry;

public class TFTAugments {
	
	public static void init() {
		for (int i = 0; i < cryoUpgrades.length; i++) {
			cryoUpgrades[i] = TEAugments.itemAugment.addItem(512 + i, CRYO_UPGRADE + i);
			TEAugments.itemAugment.addAugmentData(512 + i, CRYO_UPGRADE, i + 1, 3);
		}
	}
	
	public static void addRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[0], new Object[] {
				"   ",
				" R ",
				"rBr",
				Character.valueOf('R'),
				TEItems.powerCoilGold,
				Character.valueOf('r'),
				"dustRedstone",
				Character.valueOf('B'),
				"plateDoubleBronze"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[1], new Object[] {
				" G ",
				" R ",
				"dBd",
				Character.valueOf('R'),
				TEItems.powerCoilGold,
				Character.valueOf('d'),
				"dustBlizz",
				Character.valueOf('B'),
				"plateBronze",
				Character.valueOf('G'),
				"plateGold"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(cryoUpgrades[3], new Object[] {
				"   ",
				" R ",
				"dEd",
				Character.valueOf('R'),
				TEItems.powerCoilGold,
				Character.valueOf('d'),
				"dustCryotheum",
				Character.valueOf('E'),
				"plateDoubleElectrum"
		}));
	}
	
	public static ItemStack[] cryoUpgrades = new ItemStack[3];
	public static final String CRYO_UPGRADE = "tftCryoEfficiency";
	public static final double[] CRYO_UPGRADE_ENERGY_MOD = {
			1, 1.5, 2, 3
	};
	public static final float[] CRYO_UPGRADE_INEFFICIENCY_MOD = {
			1, 1.1f, 1.2f, 1.3f
	};
}
