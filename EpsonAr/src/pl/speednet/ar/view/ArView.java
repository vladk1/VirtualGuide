package pl.speednet.ar.view;

import pl.speednet.ar.data.PoiInfo;
import pl.speednet.ar.util.ArArrayAdapter;
import pl.speednet.ar.util.ArEngineHelper;
import pl.speednet.ar.util.poi.LowPassFilter.Mode;
import pl.speednet.ar.util.poi.PoiHelper;
import android.content.Context;
import android.location.Location;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class ArView extends FrameLayout {

	public interface OnArProblemDetected {

		void onSensorLowAccuracy(int sensorType, int accuracy);
	}

	public interface OnPoiClickListener {

		public void onClick(View v, MotionEvent event, PoiInfo info);
	}

	public static final boolean DEBUG = true;

	private PoiHelper poiHelper;
	private ArEngineHelper arEngineHelper;
	private PoiLayout poiLayout;

	private double horizontalFOV = 1.6999d;
	private double verticalFOV = 0.860d;

	public ArView(Context context) {
		this(context, null);
	}

	public ArView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		arEngineHelper = new ArEngineHelper();
		poiHelper = new PoiHelper(getContext());

		poiLayout = new PoiLayout(getContext());
		addView(poiLayout);
	}

	ArEngineHelper getArEngineHelper() {
		return arEngineHelper;
	}

	public void startAr() {
		poiHelper.start();
	}

	public void stopAr() {
		poiHelper.stop();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);

		setMeasuredDimension(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		arEngineHelper.updateParams(width, height, horizontalFOV, verticalFOV);
		super.onLayout(changed, l, t, r, b);
	}

	public void setMaxVisiblePois(int maxPois) {
		PoiLayout.setMaxViews(maxPois);
	}

	public void setUserPosition(Location location) {
		poiHelper.updateMyLocation(location);
	}

	public void setAdapter(ArArrayAdapter<? extends PoiInfo> data) {
		poiHelper.setPois(data);
		poiLayout.setPoiHelper(poiHelper);
		poiLayout.setPoiAdapter(data);
	}

	public void setOnPoiClickListener(OnPoiClickListener clickListener) {
		poiLayout.setPoiClickListener(clickListener);
	}

	public void setLowPassFilterMode(Mode mode) {
		poiHelper.setLowPassFilterMode(mode);
	}

	public void setOnArProblemDetected(OnArProblemDetected arProblemDetected) {
		poiHelper.setOnArProblemDetected(arProblemDetected);
	}
}
