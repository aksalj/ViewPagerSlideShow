package com.aksalj.viewpagerslideshow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aksalj.viewpagerslideshow.image.SmartImageView;

public class SlideShowFragment extends Fragment {

    int imgRes = -1;
    String imgUrl = null;
    ImageView.ScaleType mScale;

    public SlideShowFragment(){
        super();
    }

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

        View root = inflater.inflate(R.layout.slide,null);
        SmartImageView img = (SmartImageView)root.findViewById(R.id.img);

        if(savedInstanceState != null){
            imgUrl = savedInstanceState.getString("imgUrl");
            imgRes = savedInstanceState.getInt("imgRes");
        }

        if(imgUrl == null)
            img.setImageResource(imgRes);
        else if(imgRes != -1)
            img.setImageUrl(imgUrl);

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("imgUrl",imgUrl);
        outState.putInt("imgRes",imgRes);

        super.onSaveInstanceState(outState);

    }
}

