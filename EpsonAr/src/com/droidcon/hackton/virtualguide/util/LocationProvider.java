package com.droidcon.hackton.virtualguide.util;

import android.location.Location;

public class LocationProvider {

	// 51.517682, -0.121287
	// 51.517715, -0.120912

	public Location getLocation() {
		return generateLocation(51.517715, -0.120912);
	}

	public static Location generateLocation(double lat, double lon) {
		return generateLocation(lat, lon, 0);
	}

	public static Location generateLocation(double lat, double lon, double alt) {
		Location l = new Location("generic");
		l.setLatitude(lat);
		l.setLongitude(lon);
		l.setAltitude(alt);
		return l;
	}
}
