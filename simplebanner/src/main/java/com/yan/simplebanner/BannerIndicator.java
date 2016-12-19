package com.yan.simplebanner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2016/12/12.
 */

public class BannerIndicator extends LinearLayout {
    private Drawable selectSource;
    private Drawable unSelectSource;
    private List<ImageView> imageIndicators;
    private Context context;
    private int lastPosition = -1;
    private int widthAndHeight = -1;

    private void init(Context context) {
        this.context = context;
        setGravity(Gravity.CENTER);
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
        if (imageIndicators == null)
            imageIndicators = new ArrayList<>();
        imageIndicators.clear();
        removeAllViews();

        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            if (i == 0)
                imageView.setImageDrawable(selectSource);
            else
                imageView.setImageDrawable(unSelectSource);
            imageIndicators.add(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            LayoutParams layoutParams = null;
            if (widthAndHeight == -1) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                layoutParams = new LayoutParams(widthAndHeight, widthAndHeight);
            }
            layoutParams.setMargins(10, 10, 10, 10);
            addView(imageView, layoutParams);
        }
    }

    public void setSelectItem(int position) {
        if (lastPosition == position)
            return;
        else
            lastPosition = position;

        for (ImageView indicator : imageIndicators) {
            indicator.setImageDrawable(unSelectSource);
        }
        if (imageIndicators != null && !imageIndicators.isEmpty())
            imageIndicators.get(position).setImageDrawable(selectSource);
    }

    public BannerIndicator(Context context) {
        super(context);
        init(context);
    }

    public BannerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public BannerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }
}
