package com.aksalj.viewpagerslideshow;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;

public class SlideShowAdapter extends FragmentPagerAdapter
{

    private int[] SlideShowResources = null;
    private String[] SlideShowUrls = null;

    private ImageView.ScaleType mScale = ImageView.ScaleType.CENTER_CROP;

    public SlideShowAdapter(FragmentManager fragmentManager, String[] urls) {
        super(fragmentManager);
        SlideShowUrls = urls;
    }

    public SlideShowAdapter(FragmentManager fragmentManager, int[] imgRes) {
        super(fragmentManager);
        SlideShowResources = imgRes;
    }

    public void setImageScaleType(ImageView.ScaleType type){
        mScale = type;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int i) {
        if(SlideShowUrls != null)
            return new SlideShowFragment(mScale, SlideShowUrls[i]);
        else if(SlideShowResources != null)
            return new SlideShowFragment(mScale, SlideShowResources[i]);

        return null;
    }

    @Override
    public int getCount() {
        if(SlideShowUrls != null)
            return SlideShowUrls.length;
        else if(SlideShowResources != null)
            return SlideShowResources.length;

        return 0;
    }

}
