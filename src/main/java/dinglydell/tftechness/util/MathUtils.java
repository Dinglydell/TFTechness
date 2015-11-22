package dinglydell.tftechness.util;

public class MathUtils {
	public static double roundTo(double num, int dp) {
		int dpM = (int) Math.pow(10, dp);
		return Math.round(num * dpM) / dpM;
	}
}
