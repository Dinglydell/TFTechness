package dinglydell.tftechness.util;

import cofh.lib.util.helpers.StringHelper;

import com.bioxx.tfc.api.Metal;

public class Localisation {
	public static final String metalUnlocalisedName = "metal.";
	
	public static String getMetalName(Metal metal) {
		return StringHelper.localize(metalUnlocalisedName + metal.name.toLowerCase());
	}
}
