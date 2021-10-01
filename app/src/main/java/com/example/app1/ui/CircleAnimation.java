package com.example.app1.ui;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class CircleAnimation extends RelativeLayout {
    private final static String TAG = "CircleAnimation";
    private RectF mRect = new RectF(100,10,400,310);
    private int mBeginAngle = 0,mEndAngle = 270;
    private int mFrontColor = 0xffff0000, mShadeColor = 0xffeeeeee;
    private float mFrontLine = 10, mShadeLine = 10;
    private Paint.Style mFrontStyle = Paint.Style.STROKE, mShadeStyle = Paint.Style.STROKE;
    private ShadeView mShadeView;
    private FrontView mFrontView;
    private int mRate = 2, mDrawTimes = 0, mInterval = 70, mFactor,mSeq = 0,mDrawingAngle = 0;
    private Context mContext;
    public CircleAnimation(Context context) {
        super(context);
    }

    private class ShadeView extends View {
        public ShadeView(Context context) {
            super(context);
            mContext=context;
        }
    }
    public void render(){
        removeAllViews();
        mShadeView = new ShadeView(mContext);
        addView(mShadeView);
        mFrontView = new FrontView(mContext);
        addView(mFrontView);
        play();
    }

    private void play() {
        mSeq = 0;
        mDrawingAngle = 0;
        mDrawTimes = mEndAngle/mRate;
        mFactor = mDrawTimes/mInterval +1;
        Log.d(TAG,"mDrawTimes="+mDrawTimes+",mInterval="+mInterval+",mFactor"+mFactor);
    }

    private class FrontView extends View{
        public FrontView(Context context) {
            super(context);
        }
    }
}
