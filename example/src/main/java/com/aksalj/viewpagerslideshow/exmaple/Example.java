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

        String URLS[] = new String[] {
                "http://i.imgur.com/5g697Ao.jpg",
                "http://i.imgur.com/z7v35T7.jpg",
                "http://i.imgur.com/7v6awZj.jpg",
                "http://i.imgur.com/6dMetEv.png",
                "http://i.imgur.com/49ZuJY1.jpg"
        };

        ViewPagerSlideShow slideshow = (ViewPagerSlideShow)findViewById(R.id.slideshow);
        SlideShowAdapter adapter = new SlideShowAdapter(getSupportFragmentManager(),URLS);
        slideshow.setAdapter(adapter);

    }

}
