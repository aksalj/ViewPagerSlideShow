package com.aksalj.viewpagerslideshow.exmaple;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.aksalj.viewpagerslideshow.SlideShowAdapter;
import com.aksalj.viewpagerslideshow.ViewPagerSlideShow;

public class Example extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        int[] IMAGES = new int[]{
                R.drawable.marketing_tour1,
                R.drawable.marketing_tour2,
                R.drawable.marketing_tour3,
                R.drawable.marketing_tour4,
        };

        ViewPagerSlideShow slideshow = (ViewPagerSlideShow)findViewById(R.id.slideshow);
        SlideShowAdapter adapter = new SlideShowAdapter(getSupportFragmentManager(),IMAGES);
        slideshow.setAdapter(adapter);

    }

}
