package com.droidcon.hackton.virtualguide.ar;

import com.droidcon.hackton.epsonar.R;
import com.droidcon.hackton.virtualguide.util.DistanceFormatter;
import com.droidcon.hackton.virtualguide.util.LocationProvider;

import pl.speednet.ar.data.PoiInfo;
import pl.speednet.ar.util.ArArrayAdapter;
import pl.speednet.ar.view.ArView;
import pl.speednet.ar.view.ArView.OnPoiClickListener;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ArActivity extends Activity {

	private LocationProvider locationProvider;
	private ArView arView;
	private PoiAdapter poiAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ar);

		poiAdapter = new PoiAdapter(this);
		locationProvider = new LocationProvider();

		arView = (ArView) findViewById(R.id.ar_view);
		arView.setUserPosition(locationProvider.getLocation());
		arView.setAdapter(poiAdapter);
		arView.setOnPoiClickListener(new OnPoiClickListener() {

			@Override
			public void onClick(View v, MotionEvent event, PoiInfo info) {
				Poi poi = (Poi) info;
				Toast.makeText(ArActivity.this,
						"Poi clicked id " + poi.getName(), Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		arView.startAr();
	}

	@Override
	protected void onPause() {
		super.onPause();
		arView.stopAr();
	}

	private class PoiAdapter extends ArArrayAdapter<Poi> {

		LayoutInflater inflater;

		public PoiAdapter(Context context) {
			super(context);
			inflater = LayoutInflater.from(context);
			add(makePoi(
					LocationProvider.generateLocation(51.518276, -0.121995),
					"3 Southampton Place, WC1"));
			add(makePoi(
					LocationProvider.generateLocation(51.518436, -0.122886),
					"Bloomsbury Square west, WC1"));
			add(makePoi(
					LocationProvider.generateLocation(51.519526, -0.122638),
					"Bloomsbury Square east, WC1"));
		}

		private Poi makePoi(Location location, String name) {
			Poi p = new Poi(location);
			p.setName(name);
			return p;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ar_dongle, parent,
						false);
			}

			((TextView) convertView.findViewById(R.id.distance))
					.setText(DistanceFormatter
							.convertDistanceToUserFriendlyString(getItem(
									position).getDistance()));
			return convertView;
		}
	}

	private static class Poi extends PoiInfo {

		public Poi(Location location) {
			super(location);
		}

		public Poi(Parcel in) {
			super(in);
		}
	}
}
