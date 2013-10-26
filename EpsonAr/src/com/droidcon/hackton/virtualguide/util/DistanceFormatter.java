package com.droidcon.hackton.virtualguide.util;

import java.text.DecimalFormat;

public class DistanceFormatter {

	private static DecimalFormat metersDecimalFormat = new DecimalFormat("0");

	private static DecimalFormat kilometersDecimalFormat = new DecimalFormat(
			"0.0");

	public static String convertDistanceToUserFriendlyString(double distance) {
		boolean kilometers = false;
		if (distance > 1000d) {
			distance /= 1000d;
			kilometers = true;
		}

		DecimalFormat decimalFormat;
		String suffix;
		if (kilometers) {
			suffix = " km";
			decimalFormat = kilometersDecimalFormat;
		} else {
			suffix = " m";
			decimalFormat = metersDecimalFormat;
		}
		return decimalFormat.format(distance) + suffix;
	}
}
