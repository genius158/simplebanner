package com.yan.simplebanner;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by yan on 2016/12/12.
 */

public class BannerIndicator extends LinearLayout {
    private Drawable selectSource;
    private Drawable unSelectSource;
    private Context context;
    private int lastPosition = -1;
    private int widthAndHeight = -1;

    private Rect margins;

    public void setMargins(int left, int top, int right, int bottom) {
        if (margins != null) {
            margins.left = left;
            margins.top = top;
            margins.right = right;
            margins.bottom = bottom;
            return;
        }
        margins = new Rect(left, top, right, bottom);
    }

    private void init(Context context) {
        this.context = context;
        setGravity(Gravity.CENTER);
        if (margins == null) {
            margins = new Rect(10, 10, 10, 10);
        }
    }

    public void setIndicatorSource(Drawable selectSource, Drawable unSelectSource) {
        this.selectSource = selectSource;
        this.unSelectSource = unSelectSource;
    }

    public void setIndicatorSource(Drawable selectSource, Drawable unSelectSource, int widthAndHeight) {
        this.selectSource = selectSource;
        this.unSelectSource = unSelectSource;
        this.widthAndHeight = widthAndHeight;
    }

    public void setImageIndicator(int size) {
        removeAllViews();

        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            if (i == 0) {
                imageView.setImageDrawable(selectSource);
            } else {
                imageView.setImageDrawable(unSelectSource);
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LayoutParams layoutParams;
            if (widthAndHeight == -1) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams(widthAndHeight, widthAndHeight);
            }
            layoutParams.setMargins(margins.left, margins.top, margins.right, margins.bottom);
            addView(imageView, layoutParams);
        }
    }

    public void setSelectItem(int position) {
        if (lastPosition == position) {
            return;
        }
        lastPosition = position;

        for (int i = 0; i < getChildCount(); i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(position == i ? selectSource : unSelectSource);
        }
    }

    public BannerIndicator(Context context) {
        this(context, null);
    }

    public BannerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
}
