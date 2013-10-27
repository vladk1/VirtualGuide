package com.droidcon.hackton.virtualguide.info;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.apiomat.frontend.ApiomatRequestException;
import com.apiomat.frontend.callbacks.AOMCallback;
import com.apiomat.frontend.virtualguidemain.POI;
import com.droidcon.hackton.virtualguide.R;
import com.droidcon.hackton.virtualguide.util.GIFView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		String name = getIntent().getStringExtra("POI");		
		
		POI.getPOIsAsync("name==\"" + name + "\"", new AOMCallback<List<POI>>() {

			@Override
			public void isDone(List<POI> resultObject,
					ApiomatRequestException exception) {
				for (POI serverPoi : resultObject) {
					Log.i("InfoActivity", serverPoi.getGifURL());
					GIFView gifView = (GIFView) findViewById(R.id.gif_view);
					TextView text = (TextView) findViewById(R.id.info);
					text.setText(serverPoi.getDescription());
					gifView.initializeView(serverPoi);
				}
			}
		});		
	}
}
