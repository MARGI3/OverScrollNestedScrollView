package com.overscroll.demo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * author : magic
 * date   : 19/05/2019
 * mail   : 562224864cross@gmail.com
 */
public class OverScrollBounceBehavior extends CoordinatorLayout.Behavior<View> {

    private static final String TAG = "Behavior";

    private int mNormalHeight = 0;
    private int mMaxHeight = 0;
    private float mFactor = 1.8f;
    private int mOverScrollY;
    private View mTargetView;
    private OnScrollChangeListener mListener;

    public OverScrollBounceBehavior() {
    }

    public OverScrollBounceBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child,
                                       @NonNull View directTargetChild,
                                       @NonNull View target,
                                       int nestedScrollAxes, int type) {
        findTargetView();
        Log.d(TAG, "onStartNestedScroll " + "type = " + type);
        //TYPE_TOUCH handle over scroll
        if (checkTouchType(type) && checkTargetView()) {
            mOverScrollY = 0;
            mNormalHeight = mTargetView.getHeight();
            mMaxHeight = (int) (mNormalHeight * mFactor);
        }
        return true;
    }

    public void setFactor(float factor) {
        this.mFactor = factor;
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mListener = listener;
    }

    public void setTargetView(View targetView) {
        //set a target view from outside, target view should be NestedScrollView child
        this.mTargetView = targetView;
    }

    private void findTargetView() {
        //implement a fixed find target view as you wish
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                               @NonNull View child,
                               @NonNull View target,
                               int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed,
                               int type) {
        //unconsumed == 0 no overScroll
        //unconsumed > 0 overScroll up
        if (dyUnconsumed >= 0) {
            return;
        }
        Log.d(TAG, "onNestedScroll : dyUnconsumed = " + dyUnconsumed);
        mOverScrollY -= dyUnconsumed;
        Log.d(TAG, "onNestedScroll : mOverScrollY = " + mOverScrollY + "type = " + type);
        //TYPE_TOUCH handle over scroll
        if (checkTouchType(type) && checkTargetView()) {
            if (mOverScrollY > 0 && mTargetView.getLayoutParams().height + Math.abs(mOverScrollY) <= mMaxHeight) {
                mTargetView.getLayoutParams().height = mNormalHeight + Math.abs(mOverScrollY);
                mTargetView.requestLayout();
                if (mListener != null) {
                    mListener.onScrollChanged(calculateRate(mTargetView, mMaxHeight, mNormalHeight));
                }
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                   @NonNull View child,
                                   @NonNull View target,
                                   int type) {
        Log.d(TAG, "onStopNestedScroll" + "type = " + type);
        //TYPE_TOUCH handle over scroll
        if (checkTouchType(type)
                && checkTargetView()
                && mTargetView.getHeight() > mNormalHeight) {
            ResetAnimation animation = new ResetAnimation(mTargetView, mNormalHeight, mListener);
            animation.setDuration(300);
            mTargetView.startAnimation(animation);
        }
    }

    private boolean checkTouchType(int type) {
        return type == ViewCompat.TYPE_TOUCH;
    }

    private boolean checkTargetView() {
        return mTargetView != null;
    }

    public static class ResetAnimation extends Animation {
        int targetHeight;
        int originalHeight;
        int extraHeight;
        View view;
        OnScrollChangeListener listener;

        ResetAnimation(View view, int targetHeight, OnScrollChangeListener listener) {
            this.view = view;
            this.targetHeight = targetHeight;
            this.originalHeight = view.getHeight();
            this.extraHeight = this.targetHeight - originalHeight;
            this.listener = listener;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int newHeight = (int) (targetHeight - extraHeight * (1 - interpolatedTime));
            view.getLayoutParams().height = newHeight;
            view.requestLayout();
            if (listener != null) {
                listener.onScrollChanged(calculateRate(view, originalHeight, targetHeight));
            }
        }
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(float rate);
    }

    private static float calculateRate(View targetView, int maxHeight, int targetHeight) {
        float rate = 0;
        if (targetView != null) {
            rate = (maxHeight - (float) targetView.getLayoutParams().height) / (maxHeight - targetHeight);
        }
        return rate;
    }
}
