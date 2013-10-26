package pl.speednet.ar.view;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


import pl.speednet.ar.data.PoiInfo;
import pl.speednet.ar.engine.ScreenPoint;
import pl.speednet.ar.util.ArArrayAdapter;
import pl.speednet.ar.util.ArEngineHelper;
import pl.speednet.ar.util.ScreenUtils;
import pl.speednet.ar.util.UiFriendlyTimer;
import pl.speednet.ar.util.poi.PoiHelper;
import pl.speednet.ar.view.ArView.OnPoiClickListener;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

class PoiLayout extends CenteringLayout {

	/**
	 * Time in ms between UI updates
	 */
	private static final int UPDATE_RATE = 1000 / 30; // 30FPS

	private static final String TAG = PoiLayout.class.getSimpleName();

	private static int MAX_VIEWS = 10;

	private ArEngineHelper arEngineHelper;

	private int rotation;

	private UiFriendlyTimer timer = new UiFriendlyTimer();

	private ArArrayAdapter<? extends PoiInfo> arAdapter;

	private List<PoiInfo> visiblePois;

	private OnPoiClickListener clickListener;

	private SparseArray<View> cachedViews = new SparseArray<View>();

	private PoiHelper poiHelper;

	private OnTouchListener onTouchListener = new OnTouchListener() {

		private Rect rect = new Rect();

		private MotionEvent downEvent;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (ArView.DEBUG) {
				dumpEvent(event);
			}

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					downEvent = event;
				break;
				case MotionEvent.ACTION_UP:
					if ((downEvent != null) && ((event.getEventTime() - downEvent.getEventTime()) < 100)) {
						int selectedPoi = -1;
						for (int i = 0; i < getChildCount(); i++) {
							if (isChildTouched(getChildAt(i), event.getX(), event.getY())) {
								selectedPoi = i;
								break;
							}
						}
						if (selectedPoi >= 0) {
							for (int i = 0; i < arAdapter.getCount(); i++) {
								arAdapter.getItem(i).setSelected(false);
							}
							PoiInfo poiInfo = (PoiInfo) getChildAt(selectedPoi).getTag();
							poiInfo.setSelected(true);
							clickListener.onClick(getChildAt(selectedPoi), event, poiInfo);
						}
					}
				break;
			}
			return true;
		}

		public boolean isChildTouched(View child, float parentX, float parentY) {
			rect.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
			if (rect.contains((int) parentX, (int) parentY)) {
				return true;
			} else {
				return false;
			}
		}

	};

	private Comparator<? super PoiInfo> visiblePoisComparator = new Comparator<PoiInfo>() {

		@Override
		public int compare(PoiInfo lhs, PoiInfo rhs) {
			return (int) ((rhs.getDistance() - lhs.getDistance()) * 1000);
		}
	};

	private int screenWidth;

	private int screenHeigth;

	public static void setMaxViews(int maxViews) {
		MAX_VIEWS = maxViews;
	}

	public PoiLayout(Context context) {
		super(context);
		init();
	}

	public PoiLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PoiLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		rotation = ScreenUtils.getScreenRoataion(getContext());

		setOnTouchListener(onTouchListener);
	}

	private ArView getArView() {
		return (ArView) getParent();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			startTimer();
		} else {
			stopTimer();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		arEngineHelper = getArView().getArEngineHelper();

		startTimer();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		stopTimer();
	}

	private void startTimer() {
		timer.start(UPDATE_RATE, new Runnable() {

			@Override
			public void run() {
				update();
			}
		});
	}

	private void stopTimer() {
		timer.stop();
	}

	private void update() {
		updatePois();
		positionViews();
	}

	private void updatePois() {
		visiblePois = arEngineHelper.findPoiInfoByRotationMatrix(poiHelper.getArModel());

		Collections.sort(visiblePois, visiblePoisComparator);
		if (visiblePois.size() > MAX_VIEWS) {
			visiblePois = visiblePois.subList(visiblePois.size() - MAX_VIEWS, visiblePois.size());
		}
	}

	private void positionViews() {
		removeAllViews();
		PoiInfo tmpPoiInfo;
		View tmpView;
		for (int i = 0; (i < visiblePois.size()) && (i < MAX_VIEWS); i++) {
			tmpPoiInfo = visiblePois.get(i);
			tmpView = arAdapter.getView(tmpPoiInfo.getPositionId(), getConvertView(tmpPoiInfo.getPositionId()), this);
			cachedViews.put(tmpPoiInfo.getPositionId(), tmpView);
			tmpView.setTag(tmpPoiInfo);
			if (tmpView.getParent() != null) {
				((ViewGroup) tmpView.getParent()).removeView(tmpView);
			}
			addView(tmpView, prepareLayoutParams(tmpPoiInfo));
			tmpView.setSelected(tmpPoiInfo.isSelected());
		}
	}

	private View getConvertView(int id) {
		return cachedViews.get(id);
	}

	private LayoutParams prepareLayoutParams(PoiInfo poi) {
		ScreenPoint point = arEngineHelper.convertPoiToScreenPoint(rotation, poi, screenWidth, screenHeigth);
		CenteringLayout.LayoutParams params = new CenteringLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, point.getX(), point.getY(), poi.getOffsetLeft(), poi.getOffsetTop());
		return params;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		screenWidth = right - left;
		screenHeigth = bottom - top;
	}

	public void setPoiAdapter(ArArrayAdapter<? extends PoiInfo> adapter) {
		this.arAdapter = adapter;
	}

	public void setPoiClickListener(OnPoiClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return true;
	}

	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if ((actionCode == MotionEvent.ACTION_POINTER_DOWN) || (actionCode == MotionEvent.ACTION_POINTER_UP)) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_INDEX_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if ((i + 1) < event.getPointerCount()) {
				sb.append(";");
			}
		}
		sb.append("]");
		Log.d(TAG, sb.toString());
	}

	public void setPoiHelper(PoiHelper poiHelper) {
		this.poiHelper = poiHelper;
	}
}
