package com.strechtop.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    StretchTopNestedScrollView stretchTopNestedScrollView;
//    StretchTopScrollView stretchTopNestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stretchTopNestedScrollView = findViewById(R.id.stretch_top_view);

        final View bottomView = stretchTopNestedScrollView.getBottomView();

        final TextView descriptionTextView = bottomView.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(Html.fromHtml(Globals.DESC));

        stretchTopNestedScrollView.setFactor(1.7f);

        stretchTopNestedScrollView.setChangeListener(new StretchTopNestedScrollView.onOverScrollChanged() {
            @Override
            public void onChanged(float factor) {
                descriptionTextView.setAlpha(factor);
            }
        });
//        stretchTopNestedScrollView.setChangeListener(new StretchTopScrollView.onOverScrollChanged() {
//            @Override
//            public void onChanged(float factor) {
//                descriptionTextView.setAlpha(factor);
//            }
//        });
    }


}
