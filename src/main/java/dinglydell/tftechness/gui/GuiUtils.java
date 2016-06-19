package dinglydell.tftechness.gui;

import java.util.List;

import com.bioxx.tfc.api.TFC_ItemHeat;

import dinglydell.tftechness.TFTechness;

public class GuiUtils {

	public void addTemperature(List<String> list, float temp) {
		list.add(TFC_ItemHeat.getHeatColor(temp, Integer.MAX_VALUE));
		list.add((int) temp + TFTechness.degrees + "C");
	}
}
