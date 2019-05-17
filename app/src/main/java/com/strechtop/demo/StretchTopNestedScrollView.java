package com.strechtop.demo;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

public class StretchTopNestedScrollView extends NestedScrollView {

    private View mTopView, mBottomView;
    private int mNormalHeight, mMaxHeight;
    private onOverScrollChanged mChangeListener;
    private float mFactor = 1.6f;

    private interface OnTouchEventListener {
        void onTouchEvent(MotionEvent ev);
    }

    public StretchTopNestedScrollView(Context context) {
        this(context, null);
    }

    public StretchTopNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public StretchTopNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setFactor(float f) {
        mFactor = f;

        mTopView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNormalHeight = mTopView.getHeight();
                mMaxHeight = (int) (mNormalHeight * mFactor);
            }
        }, 50);
    }

    public View getTopView() {
        return mTopView;
    }

    public View getBottomView() {
        return mBottomView;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 1)
            throw new IllegalArgumentException("Root layout must be a LinearLayout, and only one child on this view!");

        if (getChildCount() == 0 || !(getChildAt(0) instanceof LinearLayout))
            throw new IllegalArgumentException("Root layout is not a LinearLayout!");

        if (getChildCount() == 1 && (getChildAt(0) instanceof LinearLayout)) {
            LinearLayout parent = (LinearLayout) getChildAt(0);

            if (parent.getChildCount() != 2) {
                throw new IllegalArgumentException("Root LinearLayout's has not EXACTLY two Views!");
            } else {
                mTopView = parent.getChildAt(0);
                mBottomView = parent.getChildAt(1);

                mTopView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mNormalHeight = mTopView.getHeight();
                        mMaxHeight = (int) (mNormalHeight * mFactor);
                    }
                }, 50);
            }
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        if (scrollY == 0) {
            //down, zoom in
            if (deltaY < 0 && mTopView.getLayoutParams().height + Math.abs(deltaY) > mMaxHeight) {
                mTopView.getLayoutParams().height = mMaxHeight;
            } else if (deltaY < 0 && mTopView.getLayoutParams().height + Math.abs(deltaY) <= mMaxHeight) {
                mTopView.getLayoutParams().height += Math.abs(deltaY);
            }
            //up, zoom out
            else if (deltaY > 0 && mTopView.getLayoutParams().height - Math.abs(deltaY) < mNormalHeight) {
                mTopView.getLayoutParams().height = mNormalHeight;
            } else if (deltaY > 0 && mTopView.getLayoutParams().height - Math.abs(deltaY) >= mNormalHeight) {
                mTopView.getLayoutParams().height -= Math.abs(deltaY);
            }
        }

        if (mChangeListener != null) mChangeListener.onChanged(
                (mMaxHeight - (float) mTopView.getLayoutParams().height) / (mMaxHeight - mNormalHeight)
        );

        if (deltaY != 0 && scrollY == 0) {
            mTopView.requestLayout();
            mBottomView.requestLayout();
        }

        if (mTopView.getLayoutParams().height == mNormalHeight) {
            super.overScrollBy(deltaX, deltaY, scrollX,
                    scrollY, scrollRangeX, scrollRangeY,
                    maxOverScrollX, maxOverScrollY, isTouchEvent);
        }

        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        touchListener.onTouchEvent(ev);
        return super.onTouchEvent(ev);
    }

    public interface onOverScrollChanged {
        void onChanged(float v);
    }

    public void setChangeListener(onOverScrollChanged changeListener) {
        mChangeListener = changeListener;
    }

    private OnTouchEventListener touchListener = new OnTouchEventListener() {
        @Override
        public void onTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mTopView != null && mTopView.getHeight() > mNormalHeight) {
                    ResetAnimation animation = new ResetAnimation(mTopView, mNormalHeight);
                    animation.setDuration(400);
                    mTopView.startAnimation(animation);
                }
            }
        }
    };

    public class ResetAnimation extends Animation {
        int targetHeight;
        int originalHeight;
        int extraHeight;
        View mView;

        ResetAnimation(View view, int targetHeight) {
            this.mView = view;
            this.targetHeight = targetHeight;
            originalHeight = view.getHeight();
            extraHeight = this.targetHeight - originalHeight;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) (targetHeight - extraHeight * (1 - interpolatedTime));
            mView.getLayoutParams().height = newHeight;
            mView.requestLayout();

            if (mChangeListener != null) mChangeListener.onChanged(
                    (mMaxHeight - (float) mTopView.getLayoutParams().height) / (mMaxHeight - mNormalHeight)
            );

        }
    }
}
