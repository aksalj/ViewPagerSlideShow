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
import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class ViewPagerSlideShow extends FrameLayout {

    private final short FILL_CIRCLE = 0;
    private final short PROGRESSBAR = 1;

    private final short TRANSITION_NONE = 0;
    private final short TRANSITION_FADE = 1;
    private final short TRANSITION_SCALE = 2;
    private final short TRANSITION_ROTATE = 3;

    private Context mCxt;

    private int mTimerMargin = 15;
    private int mTimerPos;
    private int mTimerStyle;
    private boolean mAutoPlay;
    private boolean mPlayReverse = false; //When reach end, play in reverse...
    private int mSlideTransition;
    private boolean mShowTimer;
    private int mSlideDelay;
    private float mTimerOpacity;

    Slider mPager;
    SlideShowAdapter mAdapter;
    LinePageIndicator mIndicator;
    ProgressBar mTimerView;

    Timer mTimer;
    TimerTask mPagerChanger;

    public ViewPagerSlideShow(Context context) {
        this(context, null);
    }

    public ViewPagerSlideShow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerSlideShow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mCxt = context;

        final Resources res = getResources();

        //Load defaults from resources
        final boolean default_autoplay  = res.getBoolean(R.bool.default_autoplay);
        final boolean default_show_timer = res.getBoolean(R.bool.default_show_timer);
        final int default_timer_style = res.getInteger(R.integer.default_timer_style);
        final int default_timer_position = res.getInteger(R.integer.default_timer_position);
        final int default_slide_delay = res.getInteger(R.integer.default_slide_delay);
        final int default_slide_transition = res.getInteger(R.integer.default_transition);
        final float default_timer_opacity = res.getFraction(R.fraction.default_timer_opacity, 2, 1);


        //Retrieve styles attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideShow, defStyle, 0);

        mShowTimer = a.getBoolean(R.styleable.SlideShow_showTimer, default_show_timer);
        mTimerStyle = a.getInteger(R.styleable.SlideShow_timerStyle, default_timer_style);
        mTimerPos = a.getInt(R.styleable.SlideShow_timerPosition, default_timer_position);
        mAutoPlay = a.getBoolean(R.styleable.SlideShow_autoPlay, default_autoplay);
        mSlideDelay = a.getInteger(R.styleable.SlideShow_slideDelay, default_slide_delay);
        mSlideTransition = a.getInteger(R.styleable.SlideShow_slideTransition, default_slide_transition);
        mTimerOpacity = a.getFloat(R.styleable.SlideShow_timerOpacity, default_timer_opacity);
        if(mTimerOpacity < 0 || mTimerOpacity > 1.0f){ mTimerOpacity = default_timer_opacity; }

        a.recycle();


        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,
                Gravity.TOP|Gravity.CENTER_HORIZONTAL));
        container.setOrientation(LinearLayout.VERTICAL);


        setupViewPager(context);
        setupIndicator(context,attrs);
        setupTimerView();


        container.addView(mPager);
        container.addView(mIndicator);
        this.addView(container);
        if(mShowTimer) this.addView(mTimerView);


        if(mAutoPlay){
            play();
        }

    }

    private void setupViewPager(Context context){
        mPager = new Slider(context);

        if(mAutoPlay)
            mPager.setScrollDurationFactor(5); // slow down animation by a factor of 5

        mPager.setId(R.id.viewpager);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
        mPager.setBackgroundColor(Color.WHITE);
        mPager.setLayoutParams(layout);

        mPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedposition = Math.abs(Math.abs(position) - 1);

                switch (mSlideTransition){
                    case TRANSITION_FADE:
                        page.setAlpha(normalizedposition);
                        break;
                    case TRANSITION_SCALE:
                        page.setScaleX(normalizedposition / 2 + 0.5f);
                        page.setScaleY(normalizedposition / 2 + 0.5f);
                        break;
                    case TRANSITION_ROTATE:
                        page.setRotationY(position * -30);
                        break;
                    case TRANSITION_NONE:
                    default:
                        return;
                }
            }
        });

    }

    private void setupIndicator(Context context, AttributeSet attrs){
        mIndicator = new LinePageIndicator(context, attrs);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mIndicator.setLayoutParams(layout);
    }

    private void setupTimerView(){

        if(!mShowTimer) return;

        mTimerView = new ProgressBar(mCxt);
        LayoutParams params;
        switch (mTimerPos){
            case 0: //TOP_LEFT
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.LEFT);
                break;

            case 1: //TOP_RIGHT
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.RIGHT);
                break;
            case 2: //BOTTOM_LEFT
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.LEFT);
                break;
            case 3: //BOTTOM_RIGHT
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.RIGHT);
                break;
            case 4: //CENTER
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER);
                break;
            case 5: //TOP_CENTER
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case 6: //LEFT_CENTER
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.LEFT | Gravity.CENTER_VERTICAL);
                break;
            case 7: //BOTTOM_CENTER
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
            case 8: //RIGHT_CENTER
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM | Gravity.CENTER_VERTICAL);
                break;
            default:
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.TOP | Gravity.LEFT);
        }
        params.width = 50;
        params.height = 50;
        params.setMargins(mTimerMargin,mTimerMargin,mTimerMargin,mTimerMargin + mIndicator.getHeight());
        mTimerView.setLayoutParams(params);
        mTimerView.setAlpha(mTimerOpacity);

        //mTimerView.setBackgroundColor(Color.WHITE);
    }

    public void toggleTimer(){
        //TODO: Show/Hide timer view
    }

    public void setAdapter(SlideShowAdapter adapter){
        mAdapter = adapter;
        mPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mPager);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener){
        setOnSlideChangeListener(listener);
    }

    public void setOnSlideChangeListener(ViewPager.OnPageChangeListener listener){
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
        mTimer.scheduleAtFixedRate(mPagerChanger,(mSlideDelay+mSlideDelay/3),mSlideDelay);
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
