package com.medhelp2.mhchat.utils.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.medhelp2.mhchat.R;

public class AspectRatioImageView extends android.support.v7.widget.AppCompatImageView {
    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float mAspectRatio;

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
        mAspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth;
        int newHeight;

        newWidth = getMeasuredWidth();
        newHeight = (int) (newWidth/mAspectRatio);

        setMeasuredDimension(newWidth, newHeight);
    }
}