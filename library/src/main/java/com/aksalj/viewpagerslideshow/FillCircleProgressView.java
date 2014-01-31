package com.aksalj.viewpagerslideshow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

class FillCircleProgressView extends View {
	
	private float mMaxProgress = 100.0f;
	private float mProgress = 0.0f;
	
	private Paint mRingPaint, mArcPaint;
	
	private float mDiameter;
	private float mStartAngle = 0.0f;
    private int mFillAlpha = 100;
	
	RectF mBounds;
	
	public FillCircleProgressView(Context context) {
		this(context, null);
	}

	public FillCircleProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FillCircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		//TODO: Make sure I am no no smaller than 25dp

		
		mRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setColor(Color.WHITE);
		mRingPaint.setStrokeWidth(2.5f);
        mRingPaint.setAlpha((int)(mFillAlpha * 1.5f));
		mRingPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
	    mRingPaint.setStrokeCap(Paint.Cap.ROUND);
	    mRingPaint.setDither(true);
		
		mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mArcPaint.setStyle(Paint.Style.FILL);
		mArcPaint.setColor(Color.WHITE);
		mArcPaint.setAlpha(mFillAlpha);
		
	}
	
	public void setMax(float max){
		if(max > 0) mMaxProgress = max;
	}

    public void setFillOpacity(float alpha){
        if(alpha < 0 || alpha > 1) return;

        float OldRange = 1.0f;
        int NewRange = 255;
        mFillAlpha = (int)((alpha * NewRange) / OldRange);
        mFillAlpha = mFillAlpha > NewRange ? NewRange : mFillAlpha;
        int ringAlpha = (mFillAlpha * 1.5f) > NewRange ? NewRange : (int)(mFillAlpha * 1.5f);

        if(mRingPaint != null && mArcPaint != null){
            mRingPaint.setAlpha(ringAlpha);
            mArcPaint.setAlpha(mFillAlpha);
            postInvalidate();
        }
    }

    public void setRingColor(int color){
        if(mRingPaint != null){
            mRingPaint.setColor(color);
            mRingPaint.setAlpha((int)(mFillAlpha * 1.5f));
            postInvalidate();
        }
    }

    public void setFillColor(int color){
        if(mArcPaint != null){
            mArcPaint.setColor(color);
            mArcPaint.setAlpha(mFillAlpha);
            postInvalidate();
        }
    }
	
	

	public synchronized void setProgress( float progress){
		
		if(progress > mMaxProgress)
			mProgress = mMaxProgress;
		else if(progress < 0)
			mProgress = 0;
		else 
			mProgress = progress;

		postInvalidate();
		
	}
	
	public float getProgress(){ return mProgress; }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xpad = (float)(getPaddingLeft() + getPaddingRight()) + 3.5f;
        float ypad = (float)(getPaddingTop() + getPaddingBottom()) + 3.5f;
        float ww = (float)w - xpad;
        float hh = (float)h - ypad;

        float min = Math.min(ww, hh);
        mDiameter = (min == 0) ? 50 : min;

        mBounds = new RectF(xpad, ypad, ww, hh);
    }

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float progress = 100 * mProgress / mMaxProgress;
		float sweepAngle = progress * 360 / 100;
		
		canvas.drawCircle(mBounds.centerX(),mBounds.centerY(), 
				mDiameter / 2.0f, mRingPaint);
		
		canvas.drawArc(mBounds, mStartAngle, sweepAngle, true, mArcPaint);
		
	}

}
