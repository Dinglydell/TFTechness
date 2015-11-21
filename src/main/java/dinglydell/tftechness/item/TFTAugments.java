package dinglydell.tftechness.item;

import cofh.thermalexpansion.item.TEAugments;

public class TFTAugments {
	
	public static void init() {
		for (int i = 0; i < numCryoEffeciency; i++) {
			TEAugments.itemAugment.addItem(512 + i, CRYO_UPGRADE + i);
			TEAugments.itemAugment.addAugmentData(512 + i, CRYO_UPGRADE, i + 1, 3);
		}
	}
	
	public static final String CRYO_UPGRADE = "tftCryoEfficiency";
	public static final int numCryoEffeciency = 3;
	public static final double[] CRYO_UPGRADE_ENERGY_MOD = {
			1, 1.5, 2, 3
	};
	public static final float[] CRYO_UPGRADE_INEFFICIENCY_MOD = {
			1, 1.1f, 1.2f, 1.3f
	};
}
