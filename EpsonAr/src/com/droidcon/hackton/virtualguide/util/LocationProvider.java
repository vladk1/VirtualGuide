package com.droidcon.hackton.virtualguide.util;

import java.util.ArrayList;
import java.util.Date;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationProvider implements LocationListener {

	public interface OnLocationUpdated {

		void onLocationUpdated(Location location);
	}

	private LocationManager locationManager;
	private ArrayList<OnLocationUpdated> callbacks = new ArrayList<LocationProvider.OnLocationUpdated>();
	private Location currentLocation;

	public LocationProvider(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public void addOnLocationListener(OnLocationUpdated listener) {
		callbacks.add(listener);
		if (callbacks.size() == 1) {
			startFetchingLocation();
		}
	}

	public void removeOnLocationListener(OnLocationUpdated listener) {
		callbacks.remove(listener);
		if (callbacks.size() == 0) {
			stopFetchingLocation();
		}
	}

	private void startFetchingLocation() {
		for (String locProvider : locationManager.getAllProviders()) {
			if (locationManager.isProviderEnabled(locProvider)) {
				if (LocationComparator.isBetterLocation(locationManager.getLastKnownLocation(locProvider),
						currentLocation)) {
					currentLocation = locationManager.getLastKnownLocation(locProvider);
					fireLocationChanged();
				}
			}
		}
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				3000, 0, this);
	}

	private void stopFetchingLocation() {
		locationManager.removeUpdates(this);
	}

	// 51.517682, -0.121287
	// 51.517715, -0.120912

//	public Location getLocation() {
//		return generateLocation(51.517715, -0.120912);
//	}

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

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(Location location) {
		if (LocationComparator.isBetterLocation(location, currentLocation)) {
			currentLocation = location;
			fireLocationChanged();
		}
	}

	private void fireLocationChanged() {
		for (OnLocationUpdated callback : callbacks) {
			callback.onLocationUpdated(currentLocation);
		}
	}

	private static class LocationComparator {

		private static final int TWO_MINUTES = 1000 * 60 * 30;

		/**
		 * Determines whether one Location reading is better than the current
		 * Location fix
		 * 
		 * @param location
		 *            The new Location that you want to evaluate
		 * @param currentBestLocation
		 *            The current Location fix, to which you want to compare the
		 *            new one
		 */
		public static boolean isBetterLocation(Location location,
				Location currentBestLocation) {
			if (location == null) {
				return false;
			}
			long timeDiff = new Date().getTime() - location.getTime();
			if ((timeDiff > TWO_MINUTES) || (timeDiff < -TWO_MINUTES)) {
				return false;
			}
			if (currentBestLocation == null) {
				return true;
			}

			// Check whether the new location fix is newer or older
			long timeDelta = location.getTime() - currentBestLocation.getTime();
			boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
			boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
			boolean isNewer = timeDelta > 0;

			// If it's been more than two minutes since the current location,
			// use the new location because the user has likely moved
			if (isSignificantlyNewer) {
				return true;
				// If the new location is more than two minutes older, it must
				// be worse
			} else if (isSignificantlyOlder) {
				return false;
			}

			// Check whether the new location fix is more or less accurate
			int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
					.getAccuracy());
			boolean isLessAccurate = accuracyDelta > 0;
			boolean isMoreAccurate = accuracyDelta < 0;
			boolean isSignificantlyLessAccurate = accuracyDelta > 200;

			// Check if the old and new location are from the same provider
			boolean isFromSameProvider = isSameProvider(location.getProvider(),
					currentBestLocation.getProvider());

			// Determine location quality using a combination of timeliness and
			// accuracy
			if (isMoreAccurate) {
				return true;
			} else if (isNewer && !isLessAccurate) {
				return true;
			} else if (isNewer && !isSignificantlyLessAccurate
					&& isFromSameProvider) {
				return true;
			}
			return false;
		}

		/** Checks whether two providers are the same */
		private static boolean isSameProvider(String provider1, String provider2) {
			if (provider1 == null) {
				return provider2 == null;
			} else {
				return provider1.equals(provider2);
			}
		}
	}

}
