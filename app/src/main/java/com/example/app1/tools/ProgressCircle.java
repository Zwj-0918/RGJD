package com.example.app1.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class ProgressCircle extends RelativeLayout {
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

    public ProgressCircle(Context context) {
        super(context);
        mContext=context;
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
        mFrontView.invalidateView();
    }
    public void setRect(int left,int top,int right,int bottom){
        mRect = new RectF(left,top,right,bottom);
    }
    public void setAngle(int begin_angle,int end_angle){
        mBeginAngle = begin_angle;
        mEndAngle = end_angle;
    }
    //speed:每次移动几个度数；frames:每秒移动几帧
    public void setmRect(int speed,int frames){
        mRate = speed;
        mInterval = 1000/frames;
    }
    public void setFront(int color, float line, Paint.Style style){
        mFrontColor = color;
        mFrontLine = line;
        mFrontStyle = style;

    }
    public void setShade(int color, float line, Paint.Style style){
        mShadeColor = color;
        mShadeLine = line;
        mShadeStyle = style;
    }
    private class ShadeView extends View{
        private Paint paint;
        public ShadeView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(mShadeColor);
            paint.setStrokeWidth(mShadeLine);
            paint.setStyle(mShadeStyle);
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawArc(mRect,mBeginAngle,360,false,paint);
        }
    }
    private class FrontView extends View{
        private Paint paint;
        private Handler handler = new Handler();

        public FrontView(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setDither(true);
            paint.setColor(mFrontColor);
            paint.setStrokeWidth(mFrontLine);
            paint.setStyle(mFrontStyle);
            //paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }
        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawArc(mRect,mBeginAngle,(float)(mDrawingAngle),false,paint);
        }
        public void invalidateView() {
            handler.postDelayed(drawRunnable,0);
        }
        private Runnable drawRunnable = new Runnable() {
            @Override
            public void run() {
                if(mDrawingAngle>=mEndAngle){
                    mDrawingAngle = mEndAngle;
                    invalidate();
                    handler.removeCallbacks(drawRunnable);
                }else{
                    mDrawingAngle = mSeq*mRate;
                    mSeq++;
                    handler.postDelayed(drawRunnable,(long)(mInterval-mSeq/mFactor));
                    invalidate();
                }
            }
        };

    }
}
