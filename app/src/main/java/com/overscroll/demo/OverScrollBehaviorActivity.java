package com.overscroll.demo;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class OverScrollBehaviorActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_nested_scroll_view_behavior_demo);
        setContentView(R.layout.activity_nestedscrollview_behavior);

        //CoordinatorLayout direct child
        nestedScrollView = findViewById(R.id.nested_content);

        final View targetView = findViewById(R.id.over_scroll_id);

        final TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(Globals.DESC));

        ViewGroup.LayoutParams layoutParams = nestedScrollView.getLayoutParams();

        if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.Behavior behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior();
            if (behavior instanceof OverScrollBounceBehavior) {

                ((OverScrollBounceBehavior) behavior).setTargetView(targetView);
                ((OverScrollBounceBehavior) behavior).setOnScrollChangeListener(new OverScrollBounceBehavior.OnScrollChangeListener() {
                    @Override
                    public void onScrollChanged(float rate) {
                        descriptionTextView.setAlpha(rate);
                    }
                });
            }
        }
    }
}
