package com.droidcon.hackton.virtualguide.util;

import java.io.ByteArrayOutputStream;
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
import android.os.SystemClock;
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
			new AsyncTask<Void, InputStream, Movie>() {
				 
	            protected Movie doInBackground(Void... params) {
	            	Movie movie = null;
	            	try {
						InputStream is = url.openConnection().getInputStream();
						byte[] array = streamToBytes(is);
						movie = Movie.decodeByteArray(array, 0, array.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	return movie;
	            }
	             
	            protected void onPostExecute(Movie movie) {
	            	mMovie = movie;
	            };
	             
	        }.execute();
			
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static byte[] streamToBytes(InputStream is) {
	    ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
	    byte[] buffer = new byte[1024];
	    int len;
	    try {
	        while ((len = is.read(buffer)) >= 0) {
	            os.write(buffer, 0, len);
	        }
	    } catch (java.io.IOException e) {
	    }
	    return os.toByteArray();
	}
	
	/*protected void onDraw(Canvas canvas) {
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

	}*/
	    
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        super.onDraw(canvas);
        final long now = SystemClock.uptimeMillis();

        if (movieStart == 0) {
        	movieStart = now;
        }

	    if (mMovie != null) {
	        final int relTime = (int)((now - movieStart) % mMovie.duration());
	        mMovie.setTime(relTime);
	        mMovie.draw(canvas, 10, 10);
	    }
	    
        this.invalidate();
    }
}