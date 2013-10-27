package com.droidcon.hackton.virtualguide.ar;

import java.util.List;

import pl.speednet.ar.data.PoiInfo;
import pl.speednet.ar.util.ArArrayAdapter;
import pl.speednet.ar.view.ArView;
import pl.speednet.ar.view.ArView.OnPoiClickListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apiomat.frontend.ApiomatRequestException;
import com.apiomat.frontend.callbacks.AOMCallback;
import com.apiomat.frontend.virtualguidemain.POI;
import com.droidcon.hackton.virtualguide.R;
import com.droidcon.hackton.virtualguide.info.InfoActivity;
import com.droidcon.hackton.virtualguide.util.DistanceFormatter;
import com.droidcon.hackton.virtualguide.util.LocationProvider;
import com.squareup.picasso.Picasso;

public class ArActivity extends Activity {

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
				poiAdapter.clear();
				for (POI serverPoi : resultObject) {
					Poi p = new Poi(LocationProvider.generateLocation(
							serverPoi.getLocationLatitude(),
							serverPoi.getLocationLongitude()));
					p.setName(serverPoi.getName());
					p.setImageUrl(serverPoi.getImageURL());
					poiAdapter.add(p);
				}
				poiAdapter.notifyDataSetChanged();
			}
		});

		poiAdapter = new PoiAdapter(this);
		locationProvider = new LocationProvider();

		arView = (ArView) findViewById(R.id.ar_view);
		arView.setUserPosition(locationProvider.getLocation());
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
}
