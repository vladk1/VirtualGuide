package pl.speednet.modechange.view;


import java.util.ArrayList;

import uk.co.ordnancesurvey.android.maps.BitmapDescriptor;
import uk.co.ordnancesurvey.android.maps.BitmapDescriptorFactory;
import uk.co.ordnancesurvey.android.maps.CameraPosition;
import uk.co.ordnancesurvey.android.maps.GridPoint;
import uk.co.ordnancesurvey.android.maps.MapFragment;
import uk.co.ordnancesurvey.android.maps.MapProjection;
import uk.co.ordnancesurvey.android.maps.MarkerOptions;
import uk.co.ordnancesurvey.android.maps.OSMap;
import uk.co.ordnancesurvey.android.maps.OSTileSource;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.droidcon.hackton.virtualguide.R;
import com.droidcon.hackton.virtualguide.ar.ArActivity;

public class MapView extends Activity {

	
	
	/**
     * This API Key is registered for this application.
     *
     * Define your own OS Openspace API KEY details below
     * @see http://www.ordnancesurvey.co.uk/oswebsite/web-services/os-openspace/index.html
     */
    private final static String OS_API_KEY = "E98D7DFC9D265AE5E0430C6CA40A3CE5";
    private final static boolean OS_IS_PRO = false;

    private OSMap mMap;

    //private MainPresenterImpl mPresenter;
    LocationManager mLocationManager;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        
        Location location = getLocation();
        
        // Gets map with mMap
        getMap();
        mMap.setMyLocationEnabled(true);
        
         
        // Get Zoom
        MapProjection mp = MapProjection.getDefault();
        GridPoint localGridPoint = mp.toGridPoint(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(new CameraPosition(localGridPoint, 1), false);
    	
        
        // Get Pointer
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker();
        mMap.addMarker(new MarkerOptions()
        .gridPoint(localGridPoint)
        .title("Map clicked here!")
        .snippet("hi")
        .icon(icon));
        
        
        
        Button changeToView = (Button) findViewById(R.id.toLoliButton);
        changeToView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MapView.this, ArActivity.class);
				startActivity(intent);
			}
        	
        });
     }

 private Location getLocation() {
	 mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
     String provider;
     Criteria criteria = new Criteria();
     provider = mLocationManager.getBestProvider(criteria, false);
     Location location = mLocationManager.getLastKnownLocation(provider);
     return location;
 }

 
 private void getMap() {
	// getting map        
     MapFragment mf = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_fragment));    
     mMap = mf.getMap();
     
     //create list of tileSources
     ArrayList<OSTileSource> sources = new ArrayList<OSTileSource>();

     //create web tile source with API details
     sources.add(mMap.webTileSource(OS_API_KEY, OS_IS_PRO, null));
     mMap.setTileSources(sources);
 }
 
 
}


