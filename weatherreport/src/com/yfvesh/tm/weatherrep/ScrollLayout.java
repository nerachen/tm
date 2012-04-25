package com.yfvesh.tm.weatherrep;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {

	// private float startX;
	// private float startY;
	public static boolean startTouch = true;
	// private boolean canMove = true;
	private static final String TAG = "ScrollLayout";
	private Scroller mScroller;

	/*
	 * 
	 * �ٶ�׷��������Ҫ��Ϊ��ͨ����ǰ�����ٶ��жϵ�ǰ�����Ƿ�Ϊfling
	 */
	private VelocityTracker mVelocityTracker;
	/*
	 * 
	 * ��¼��ǰ��Ļ�±꣬ȡֵ��Χ�ǣ�0 �� getChildCount()-1
	 */
	private static int mCurScreen;
	// private int mDefaultScreen = 1;
	/*
	 * 
	 * Touch״ֵ̬ 0����ֹ 1������
	 */
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	/*
	 * 
	 * ��¼��ǰtouch�¼�״̬--������TOUCH_STATE_SCROLLING������ֹ��TOUCH_STATE_REST Ĭ�ϣ�
	 */

	private int mTouchState = TOUCH_STATE_REST;
	private static final int SNAP_VELOCITY = 600;

	/*
	 * 
	 * ��¼touch�¼��б���Ϊ�ǻ����¼�ǰ�����ɻ�������
	 */
	private int mTouchSlop;

	/*
	 * 
	 * ��¼����ʱ�ϴ���ָ������λ��
	 */

	private float mLastMotionX;

	private float mLastMotionY;

	private OnScrollToScreenListener onScrollToScreen = null;

	public ScrollLayout(Context context, AttributeSet attrs) {

		this(context, attrs, 0);

	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);

		mScroller = new Scroller(context);

		// mCurScreen = mDefaultScreen;

		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		System.out.println("aaaaaaaaaaaaaaaaaaaaa" + mTouchSlop);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int childLeft = 0;

		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {

			final View childView = getChildAt(i);

			if (childView.getVisibility() != View.GONE) {

				final int childWidth = childView.getMeasuredWidth();

				//����ÿ��childview�����꣬��֤����
				childView.layout(childLeft, 0, childLeft + childWidth,

				childView.getMeasuredHeight());

				childLeft += childWidth;

			}

		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		Log.e(TAG, "onMeasure");

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY) {

			throw new IllegalStateException(

			"ScrollLayout only canmCurScreen run at EXACTLY mode!");

		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode != MeasureSpec.EXACTLY) {

			throw new IllegalStateException(

			"ScrollLayout only can run at EXACTLY mode!");

		}

		// The children are given the same width and height as the scrollLayout

		final int count = getChildCount();

		for (int i = 0; i < count; i++) {

			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);

		}

		// Log.e(TAG, "moving to screen "+mCurScreen);

		scrollTo(mCurScreen * width, 0);

		doScrollAction(mCurScreen);

	}

	/**
	 * 
	 * �������ƣ�snapToDestination �������������ݵ�ǰλ�û�������Ӧ����
	 * 
	 * 
	 * 
	 * @param whichScreen
	 */

	public void snapToDestination() {

		final int screenWidth = getWidth();

		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;

		snapToScreen(destScreen);

	}

	/**
	 * 
	 * �������ƣ�snapToScreen ��������������������whichScreen����0��ʼ�������棬�й���Ч��
	 * 
	 * @param whichScreen
	 */

	public void snapToScreen(int whichScreen) {

		// get the valid layout page

		int a  = getScrollX();
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();

			mScroller.startScroll(getScrollX(), 0, delta, 0,

			Math.abs(delta) * 2);
			
			//����̫�죬�޷����ƶ���ʱ��
			//scrollTo(whichScreen * getWidth(),0);

			mCurScreen = whichScreen;

			doScrollAction(mCurScreen);

			invalidate(); // Redraw the layout

		}

	}

	/**
	 * 
	 * �������ƣ�setToScreen ����������ָ������ת����whichScreen����0��ʼ��������
	 * 
	 * @param whichScreen
	 */

	public void setToScreen(int whichScreen) {

		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

		mCurScreen = whichScreen;

		scrollTo(whichScreen * getWidth(), 0);

		doScrollAction(whichScreen);

	}

	public int getCurScreen() {

		return mCurScreen;

	}

	@Override
	public void computeScroll() {

		// TODO Auto-generated method stub

		if (mScroller.computeScrollOffset()) {

			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

			postInvalidate();

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mVelocityTracker == null) {

			mVelocityTracker = VelocityTracker.obtain();

		}

		mVelocityTracker.addMovement(event);

		final int action = event.getAction();

		final float x = event.getX();

		// final float y = event.getY();

		switch (action) {

		case MotionEvent.ACTION_DOWN:

			Log.e(TAG, "event down!");

			if (!mScroller.isFinished()) {

				mScroller.abortAnimation();

			}

			mLastMotionX = x;

			break;

		case MotionEvent.ACTION_MOVE:

			int deltaX = (int) (mLastMotionX - x);

			mLastMotionX = x;

			scrollBy(deltaX, 0);

			break;

		case MotionEvent.ACTION_UP:

			Log.e(TAG, "event : up");

			final VelocityTracker velocityTracker = mVelocityTracker;

			velocityTracker.computeCurrentVelocity(1000);

			int velocityX = (int) velocityTracker.getXVelocity();

			Log.e(TAG, "velocityX:" + velocityX);

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {

				// Fling enough to move left

				Log.e(TAG, "snap left");

				snapToScreen(mCurScreen - 1);

			} else if (velocityX < -SNAP_VELOCITY

			&& mCurScreen < getChildCount() - 1) {

				// Fling enough to move right

				Log.e(TAG, "snap right");

				snapToScreen(mCurScreen + 1);

			} else {

				snapToDestination();

			}

			if (mVelocityTracker != null) {

				mVelocityTracker.recycle();

				mVelocityTracker = null;

			}

			mTouchState = TOUCH_STATE_REST;

			break;

		case MotionEvent.ACTION_CANCEL:

			mTouchState = TOUCH_STATE_REST;

			break;

		}

		return true;

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		// TODO Auto-generated method stub

		Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

		final int action = ev.getAction();

		if ((action == MotionEvent.ACTION_MOVE)

		&& (mTouchState != TOUCH_STATE_REST)) {

			return true;

		}

		final float x = ev.getX();

		final float y = ev.getY();

		switch (action) {

		case MotionEvent.ACTION_DOWN:

			mLastMotionX = x;

			mLastMotionY = y;

			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST

			: TOUCH_STATE_SCROLLING;

			break;

		case MotionEvent.ACTION_MOVE:

			final int xDiff = (int) Math.abs(mLastMotionX - x);

			if (xDiff > mTouchSlop) {

				if (Math.abs(mLastMotionY - y) / Math.abs(mLastMotionX - x) < 1)

					mTouchState = TOUCH_STATE_SCROLLING;

			}

			break;

		case MotionEvent.ACTION_CANCEL:

		case MotionEvent.ACTION_UP:

			mTouchState = TOUCH_STATE_REST;

			break;

		}

		return mTouchState != TOUCH_STATE_REST;

	}

	/**
	 * 
	 * �������ƣ�doScrollAction �����������������л�����ʱִ����Ӧ����
	 * 
	 * @param index
	 */

	private void doScrollAction(int whichScreen) {

		if (onScrollToScreen != null) {

			onScrollToScreen.doAction(whichScreen);

		}

	}

	/**
	 * 
	 * �������ƣ�setOnScrollToScreen ���������������ڲ��ӿڵ�ʵ����ʵ��
	 * 
	 * @param index
	 */

	public void setOnScrollToScreen(

	OnScrollToScreenListener paramOnScrollToScreen) {

		onScrollToScreen = paramOnScrollToScreen;

	}

	/**
	 * 
	 * �ӿ����ƣ�OnScrollToScreen �ӿ���������������ĳ������ʱ���Ե��øýӿ��µ�doAction()����ִ��ĳЩ����
	 * 
	 * @author wader
	 */

	public abstract interface OnScrollToScreenListener {

		public void doAction(int whichScreen);

	}

	/**
	 * 
	 * ָ��Ĭ��ҳ��λ��
	 * 
	 * @param position
	 */

	public void setDefaultScreen(int position) {

		mCurScreen = position;

	}

}
