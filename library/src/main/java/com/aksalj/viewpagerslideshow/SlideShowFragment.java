package com.aksalj.viewpagerslideshow;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aksalj.viewpagerslideshow.image.SmartImageView;

class SlideShowFragment extends Fragment {

    int imgRes = -1;
    String imgUrl = null;
    ImageView.ScaleType mScale;

    public SlideShowFragment(ImageView.ScaleType scaleType, int res){
        imgRes = res;
        mScale = scaleType;
    }
    public SlideShowFragment(ImageView.ScaleType scaleType, String url){
        imgUrl = url;
        mScale = scaleType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ItemLayout layout;
        if(imgUrl == null)
            layout = new ItemLayout(inflater.getContext(),imgRes);
        else
            layout = new ItemLayout(inflater.getContext(),imgUrl);

        return layout;
    }


    private class ItemLayout extends LinearLayout {

        /*
            <?xml version="1.0" encoding="utf-8"?>
            <LinearLayout
                android:orientation="vertical"
                android:layout_width="fill_parent"
                android:layout_height="fill_parent"
                xmlns:android="http://schemas.android.com/apk/res/android">
                <ImageView
                    android:id="@+id/slideImg"
                    android:layout_width="fill_parent"
                    android:layout_height="0.0dip"
                    android:src="@drawable/marketing_tour4"
                    android:scaleType="centerCrop"
                    android:layout_weight="1.0" />
            </LinearLayout>
        */


        SmartImageView img;


        private ItemLayout(Context cxt, int imgRes){
            super(cxt);
            init(cxt);
            img.setImageResource(imgRes);
        }

        private ItemLayout(Context cxt, String imgUrl){
            super(cxt);
            init(cxt);
            img.setImageUrl(imgUrl);
        }

        private void init(Context cxt){
            this.setOrientation(LinearLayout.VERTICAL);
            this.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));

            img = new SmartImageView(cxt);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0,1.0f);
            img.setLayoutParams(params);
            img.setScaleType(mScale);
        }
    }
}

