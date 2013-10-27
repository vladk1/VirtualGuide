package com.droidcon.hackton.virtualguide.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Build;
import android.text.method.MovementMethod;
import android.util.AttributeSet;
import android.view.View;

import com.apiomat.frontend.virtualguidemain.POI;

public class GIFView extends View {

	private static final int DEFAULT_MOVIEW_DURATION = 1000;

	private int mMovieResourceId;
	private Movie mMovie;

	private long mMovieStart;
	private int mCurrentAnimationTime = 0;

	/**
	 * Position for drawing animation frames in the center of the view.
	 */
	private float mLeft;
	private float mTop;

	/**
	 * Scaling factor to fit the animation within view bounds.
	 */
	private float mScale;

	/**
	 * Scaled movie frames width and height.
	 */
	private int mMeasuredMovieWidth;
	private int mMeasuredMovieHeight;

	private volatile boolean mPaused = false;
	private boolean mVisible = true;

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
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
					setMovie(movie);
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

	public void setMovieResource(int movieResId) {
		this.mMovieResourceId = movieResId;
		mMovie = Movie.decodeStream(getResources().openRawResource(
				mMovieResourceId));
		requestLayout();
	}

	public void setMovie(Movie movie) {
		this.mMovie = movie;
		requestLayout();
	}

	public Movie getMovie() {
		return mMovie;
	}

	public void setMovieTime(int time) {
		mCurrentAnimationTime = time;
		invalidate();
	}

	public void setPaused(boolean paused) {
		this.mPaused = paused;

		/**
		 * Calculate new movie start time, so that it resumes from the same
		 * frame.
		 */
		if (!paused) {
			mMovieStart = android.os.SystemClock.uptimeMillis()
					- mCurrentAnimationTime;
		}

		invalidate();
	}

	public boolean isPaused() {
		return this.mPaused;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (mMovie != null) {
			int movieWidth = mMovie.width();
			int movieHeight = mMovie.height();

			/*
			 * Calculate horizontal scaling
			 */
			float scaleH = 1f;
			int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

			if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
				int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
				if (movieWidth > maximumWidth) {
					scaleH = (float) movieWidth / (float) maximumWidth;
				}
			}

			/*
			 * calculate vertical scaling
			 */
			float scaleW = 1f;
			int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

			if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
				int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
				if (movieHeight > maximumHeight) {
					scaleW = (float) movieHeight / (float) maximumHeight;
				}
			}

			/*
			 * calculate overall scale
			 */
			mScale = 1f / Math.max(scaleH, scaleW);

			mMeasuredMovieWidth = (int) (movieWidth * mScale);
			mMeasuredMovieHeight = (int) (movieHeight * mScale);

			setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

		} else {
			/*
			 * No movie set, just set minimum available size.
			 */
			setMeasuredDimension(getSuggestedMinimumWidth(),
					getSuggestedMinimumHeight());
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		/*
		 * Calculate left / top for drawing in center
		 */
		mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
		mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

		mVisible = getVisibility() == View.VISIBLE;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (mMovie != null) {
			if (!mPaused) {
				updateAnimationTime();
				drawMovieFrame(canvas);
				invalidateView();
			} else {
				drawMovieFrame(canvas);
			}
		}
	}

	/**
	 * Invalidates view only if it is visible. <br>
	 * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
	 * 
	 */
	@SuppressLint("NewApi")
	private void invalidateView() {
		if (mVisible) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				postInvalidateOnAnimation();
			} else {
				invalidate();
			}
		}
	}

	/**
	 * Calculate current animation time
	 */
	private void updateAnimationTime() {
		long now = android.os.SystemClock.uptimeMillis();
		int movieDelayFactor = 3;

		if (mMovieStart == 0) {
			mMovieStart = now;
		}

		int dur = mMovie.duration() * movieDelayFactor;

		if (dur == 0) {
			dur = DEFAULT_MOVIEW_DURATION;
		}

		mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
		mCurrentAnimationTime /= movieDelayFactor;
	}

	/**
	 * Draw current GIF frame
	 */
	private void drawMovieFrame(Canvas canvas) {

		mMovie.setTime(mCurrentAnimationTime);

		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(mScale, mScale);
		mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
		canvas.restore();
	}

	@SuppressLint("NewApi")
	@Override
	public void onScreenStateChanged(int screenState) {
		super.onScreenStateChanged(screenState);
		mVisible = screenState == SCREEN_STATE_ON;
		invalidateView();
	}

	@SuppressLint("NewApi")
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = visibility == View.VISIBLE;
		invalidateView();
	}
}