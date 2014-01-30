package com.aksalj.viewpagerslideshow;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerSlideshow extends FrameLayout {

    private final short TOP_LEFT = 0;
    private final short TOP_RIGHT = 1;
    private final short BOTTOM_LEFT = 2;
    private final short BOTTOM_RIGHT = 3;
    private final short CENTER = 4;
    private final short TOP_CENTER = 5;
    private final short LEFT_CENTER = 6;
    private final short BOTTOM_CENTER = 7;
    private final short RIGHT_CENTER = 8;

    private final short FILL_CIRCLE = 0;
    private final short PROGRESSBAR = 1;

    private Context mCxt;

    private int mTimerMargin = 15;
    private int mTimerPos;
    private int mTimerStyle;
    private boolean mAutoPlay;
    private boolean mPlayReverse = false; //When reach end, play in reverse...
    private boolean mShowTimer;
    private int mSlideDelay;
    private float mTimerOpacity;

    ViewPager mPager;
    SlideShowAdapter mAdapter;
    LinePageIndicator mIndicator;
    View mTimerView;

    Timer mTimer;
    TimerTask mPagerChanger;

    public ViewPagerSlideshow(Context context) {
        super(context, null);
    }

    public ViewPagerSlideshow(Context context, AttributeSet attrs) {
        super(context, attrs, R.attr.Slideshow);
    }

    public ViewPagerSlideshow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mCxt = context;

        final Resources res = getResources();

        //Load defaults from resources
        final boolean default_autoplay  = res.getBoolean(R.bool.default_autoplay);
        final boolean default_show_timer = res.getBoolean(R.bool.default_show_timer);
        final int default_timer_style = res.getInteger(R.integer.default_timer_style);
        final int default_timer_position = res.getInteger(R.integer.default_timer_position);
        final int default_slide_delay = res.getInteger(R.integer.default_slide_delay);
        final float default_timer_opacity = res.getDimension(R.dimen.default_timer_opacity);


        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Slideshow, defStyle, 0);

        mShowTimer = a.getBoolean(R.styleable.Slideshow_showTimer, default_show_timer);
        mTimerStyle = a.getInteger(R.styleable.Slideshow_timerStyle,default_timer_style);
        mTimerPos = a.getInt(R.styleable.Slideshow_timerPosition,default_timer_position);
        mAutoPlay = a.getBoolean(R.styleable.Slideshow_autoplay, default_autoplay);
        mSlideDelay = a.getInteger(R.styleable.Slideshow_slideDelay, default_slide_delay);
        mTimerOpacity = a.getFloat(R.styleable.Slideshow_timerOpacity,default_timer_opacity);
        if(mTimerOpacity < 0 || mTimerOpacity > 1.0f){ mTimerOpacity = default_timer_opacity; }

        a.recycle();


        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        container.setOrientation(LinearLayout.VERTICAL);


        mPager = new ViewPager(context);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0, 1.0f);
        mPager.setLayoutParams(layout);

        container.addView(mPager);

        mIndicator = new LinePageIndicator(context, attrs);
        layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mIndicator.setLayoutParams(layout);
        mIndicator.setViewPager(mPager);
        container.addView(mIndicator);

        this.addView(container);

        if(mShowTimer){
            setupTimerView();
        }

        if(mAutoPlay){
            play();
        }

    }

    private void setupTimerView(){
        mTimerView = new View(mCxt);
        LayoutParams params;
        switch (mTimerPos){
            case TOP_LEFT:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.LEFT);
                break;
            case TOP_CENTER:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case TOP_RIGHT:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.RIGHT);
                break;
            case BOTTOM_LEFT:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.LEFT);
                break;
            case BOTTOM_RIGHT:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.RIGHT);
                break;
            case BOTTOM_CENTER:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
            case LEFT_CENTER:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case RIGHT_CENTER:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
                break;
            case CENTER:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                break;
            default:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.LEFT);
        }
        params.setMargins(mTimerMargin,mTimerMargin,mTimerMargin,mTimerMargin);
        mTimerView.setLayoutParams(params);
        mTimerView.setAlpha(mTimerOpacity);

        mTimerView.setMinimumWidth(10);
        mTimerView.setMinimumHeight(10);
        mTimerView.setBackgroundColor(Color.GREEN);
    }

    public void toggleTimer(){
        //TODO: Show/Hide timer view
    }

    public void setAdapter(SlideShowAdapter adapter){
        mAdapter = adapter;
        mPager.setAdapter(mAdapter);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        mIndicator.setOnPageChangeListener(listener);
    }

    public void play(){
        stop();
        mTimer = new Timer("autoplay",true);
        mPagerChanger = new TimerTask() {
            @Override
            public void run() {
                mPager.post(new Runnable() {
                    @Override
                    public void run() {
                        next();
                    }
                });
            }
        };
        mTimer.scheduleAtFixedRate(mPagerChanger,0,mSlideDelay);
    }

    public void stop(){
        if(mTimer == null) return;
        mTimer.cancel();
        mTimer = null;

    }

    public void next(){
        if(mAdapter == null) return;
        int current = mPager.getCurrentItem();
        if(current == mAdapter.getCount() - 1){
            mPager.setCurrentItem(0,true);
        }else{
            mPager.setCurrentItem(current+1,true);
        }
    }

    public void prev(){
        if(mAdapter == null) return;
        int current = mPager.getCurrentItem();
        if(current == 0){
            mPager.setCurrentItem(mAdapter.getCount() - 1,true);
        }else{
            mPager.setCurrentItem(current-1,true);
        }
    }

}
