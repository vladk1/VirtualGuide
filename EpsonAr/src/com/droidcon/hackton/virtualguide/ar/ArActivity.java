package com.droidcon.hackton.virtualguide.ar;

import java.util.List;

import pl.speednet.ar.data.PoiInfo;
import pl.speednet.ar.util.ArArrayAdapter;
import pl.speednet.ar.view.ArView;
import pl.speednet.ar.view.ArView.OnPoiClickListener;
import pl.speednet.modechange.view.MapView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcel;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.apiomat.frontend.ApiomatRequestException;
import com.apiomat.frontend.callbacks.AOMCallback;
import com.apiomat.frontend.virtualguidemain.POI;
import com.droidcon.hackton.virtualguide.R;
import com.droidcon.hackton.virtualguide.info.InfoActivity;
import com.droidcon.hackton.virtualguide.util.DistanceFormatter;
import com.droidcon.hackton.virtualguide.util.LocationProvider;
import com.droidcon.hackton.virtualguide.util.LocationProvider.OnLocationUpdated;
import com.squareup.picasso.Picasso;

public class ArActivity extends Activity implements OnLocationUpdated {

	private LocationProvider locationProvider;
	private ArView arView;
	private PoiAdapter poiAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ar);
		POI.getPOIsAsync("", new AOMCallback<List<POI>>() {

			@Override
			public void isDone(List<POI> resultObject,
					ApiomatRequestException exception) {
				if (resultObject != null) {
					poiAdapter.clear();
					for (POI serverPoi : resultObject) {
						Poi p = new Poi(LocationProvider.generateLocation(
								serverPoi.getLocationLatitude(),
								serverPoi.getLocationLongitude()));
						p.setName(serverPoi.getName());
						p.setImageUrl(serverPoi.getImageURL());

						Resources r = getResources();
						p.setOffsetLeft((int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 32,
								r.getDisplayMetrics()));
						p.setOffsetTop((int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 80,
								r.getDisplayMetrics()));

						poiAdapter.add(p);
					}
					poiAdapter.notifyDataSetChanged();
				}
			}
		});

		poiAdapter = new PoiAdapter(this);
		locationProvider = new LocationProvider(
				(LocationManager) getSystemService(Context.LOCATION_SERVICE));

		Button changeModeButton = (Button) findViewById(R.id.toMapButton);
		changeModeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ArActivity.this, MapView.class);
				startActivity(intent);

			}

		});

		arView = (ArView) findViewById(R.id.ar_view);
		arView.setAdapter(poiAdapter);
		arView.setOnPoiClickListener(new OnPoiClickListener() {

			@Override
			public void onClick(View v, MotionEvent event, PoiInfo info) {
				Poi poi = (Poi) info;

				Intent intent = new Intent(ArActivity.this, InfoActivity.class);
				intent.putExtra("POI", poi.getName());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		arView.startAr();
		locationProvider.addOnLocationListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		arView.stopAr();
		locationProvider.removeOnLocationListener(this);
	}

	private class PoiAdapter extends ArArrayAdapter<Poi> {

		LayoutInflater inflater;

		public PoiAdapter(Context context) {
			super(context);
			inflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ar_dongle, parent,
						false);
			}

			Poi poi = getItem(position);

			((TextView) convertView.findViewById(R.id.distance))
					.setText(DistanceFormatter
							.convertDistanceToUserFriendlyString(poi
									.getDistance()));

			Picasso.with(getContext()).load(poi.getImageUrl()).resize(64, 64)
					.centerInside()
					.into(((ImageView) convertView.findViewById(R.id.image)));
			return convertView;
		}
	}

	private static class Poi extends PoiInfo {

		private String imageUrl;

		public Poi(Location location) {
			super(location);
		}

		public Poi(Parcel in) {
			super(in);
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

	}

	@Override
	public void onLocationUpdated(Location location) {
		arView.setUserPosition(location);
	}
}
