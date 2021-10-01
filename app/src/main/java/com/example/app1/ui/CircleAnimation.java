package com.example.app1.ui;

import android.content.Context;
import android.graphics.RectF;
import android.widget.RelativeLayout;

public class CircleAnimation extends RelativeLayout {
    private final static String TAG = "CircleAnimation";
    private RectF mRect = new RectF(100,10,400,310);
    private int mBeginAngle = 0,mEndAngle = 270;
    public CircleAnimation(Context context) {
        super(context);
    }
}
