package com.droidcon.hackton.virtualguide.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.apiomat.frontend.virtualguidemain.POI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;

public class GIFView extends View {

	private Movie mMovie;
	private long movieStart;

	public GIFView(Context context) {
		this(context, null);
	}

	public GIFView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GIFView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void initializeView(POI poi) {
		try {
			final URL url = new URL(poi.getGifURL());
			new AsyncTask<Void, InputStream, InputStream>() {
				 
	            protected InputStream doInBackground(Void... params) {
	            	InputStream is = null;
	            	try {
						is = url.openConnection().getInputStream();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	return is;
	            }
	             
	            protected void onPostExecute(InputStream is) {
	            	mMovie = Movie.decodeStream(is);
	            };
	             
	        }.execute();
			
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void onDraw(Canvas canvas) {
	    canvas.drawColor(Color.TRANSPARENT);
	    super.onDraw(canvas);
	    long now = android.os.SystemClock.uptimeMillis();
	
	    if (movieStart == 0) {
	        movieStart = (int) now;
	    }
	    if (mMovie != null) {
	        int relTime = (int) ((now - movieStart) % mMovie.duration());
	        mMovie.setTime(relTime);
	        mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight()
	                - mMovie.height());
	        this.invalidate();
	    }
	}
}