package pl.speednet.ar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CenteringLayout extends ViewGroup {

	private int ml;
	private int mt;
	private int mr;
	private int mb;

	public CenteringLayout(Context context) {
		super(context);
	}

	public CenteringLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CenteringLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			LayoutParams lp = (LayoutParams) child.getLayoutParams();

			switch (lp.offsetLeft) {
				case LayoutParams.START:
					ml = lp.x;
					break;
				case LayoutParams.CENTER:
					ml = lp.x - (child.getMeasuredWidth() / 2);
					break;
				case LayoutParams.END:
					ml = lp.x - child.getMeasuredWidth();
					break;
				default:
					ml = lp.x - lp.offsetLeft;
					break;
			}

			switch (lp.offsetTop) {
				case LayoutParams.START:
					mt = lp.y;
					break;
				case LayoutParams.CENTER:
					mt = lp.y - (child.getMeasuredHeight() / 2);
					break;
				case LayoutParams.END:
					mt = lp.y - child.getMeasuredHeight();
					break;
				default:
					mt = lp.y - lp.offsetTop;
					break;
			}
			mr = (ml + child.getMeasuredWidth());
			mb = (mt + child.getMeasuredHeight());
			child.layout(ml, mt, mr, mb);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}
		setMeasuredDimension(resolveSize(0, widthMeasureSpec), resolveSize(0, heightMeasureSpec));
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams;
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	// FIXME: Ta klasa jest publiczna czyli wykorzystywana w innych miejscach niz tu wiec powinna byc w osobnym pliku a
	// nie tu zglaszana. klasy w klasach zglaszamy tylko prywatne!
	public static class LayoutParams extends ViewGroup.LayoutParams {

		public static final int START = Integer.MAX_VALUE - 2;
		public static final int CENTER = Integer.MAX_VALUE - 3;
		public static final int END = Integer.MAX_VALUE - 4;

		//FIXME: brak modyfikatorów
		int x;
		int y;
		int offsetLeft;
		int offsetTop;

		public int horizontalSpacing;
		public boolean breakLine;

		public LayoutParams(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public LayoutParams(int w, int h) {
			super(w, h);
		}

		public LayoutParams(int w, int h, int x, int y, int offsetLeft, int offsetTop) {
			super(w, h);
			this.x = x;
			this.y = y;
			this.offsetLeft = offsetLeft;
			this.offsetTop = offsetTop;
		}
	}
}
