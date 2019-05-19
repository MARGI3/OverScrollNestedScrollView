package com.overscroll.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.overscroll.demo.view.StretchTopScrollView;

public class ScrollViewActivity extends AppCompatActivity {

    StretchTopScrollView stretchTopNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_scroll_view_demo);
        setContentView(R.layout.activity_scrollview);

        stretchTopNestedScrollView = findViewById(R.id.stretch_top_view);

        final View bottomView = stretchTopNestedScrollView.getBottomView();

        final TextView descriptionTextView = bottomView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(Globals.DESC));

        stretchTopNestedScrollView.setFactor(1.7f);

        stretchTopNestedScrollView.setChangeListener(new StretchTopScrollView.onOverScrollChanged() {
            @Override
            public void onChanged(float factor) {
                descriptionTextView.setAlpha(factor);
            }
        });
    }


}
