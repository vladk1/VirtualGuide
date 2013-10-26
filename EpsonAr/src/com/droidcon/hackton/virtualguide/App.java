package com.droidcon.hackton.virtualguide;

import com.apiomat.frontend.Datastore;
import com.apiomat.frontend.basics.User;

import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Datastore.configure(User.baseURL, User.apiKey, "kepa", "kepa",
				User.sdkVersion, User.system);

	}
}
