package com.yan.simplebanner;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yan on 2016/12/3.
 */

public class Banner extends ViewPager {
    private long interval = 1500;
    private int currentPosition;
    private Context context;

    private MyHandler myHandler;
    private MyPagerAdapter pagerAdapter;
    private List<Object> mDataSource;

    private int dataSourceSize = 0;
    private BannerIndicator indicator;

    public Banner(Context context) {
        super(context);
        init(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOffscreenPageLimit(3);
        addOnPageChangeListener(mOnPageChangeListener);

        mDataSource = new ArrayList<>();
        myHandler = new MyHandler(this);
    }

    public void attachIndicator(BannerIndicator bannerIndicator) {
        indicator = bannerIndicator;
        indicator.setImageIndicator(dataSourceSize);
        setIndicatorSelectItem(1);
    }


    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setPageChangeDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new BannerScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class BannerScroller extends Scroller {
        private int mDuration = 1000;

        public BannerScroller(Context context, int duration) {
            super(context);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }


    public void setDataSource(Object dataSource) {
        mDataSource.clear();
        if (!(dataSource instanceof List)) {
            return;
        }
        List<Object> data = (List<Object>) dataSource;
        dataSourceSize = data.size();

        if (dataSourceSize >= 2) {
            mDataSource.add(data.get(dataSourceSize - 1));
            mDataSource.addAll(data);
            mDataSource.add(data.get(0));
        } else {
            mDataSource.add(data.get(0));
            mDataSource.add(data.get(0));
        }

        if (mDataSource == null || mDataSource.size() == 1) {
            setEnabled(false);
            setClickable(false);
            setFocusable(false);
            setFocusableInTouchMode(false);
        }
        pagerAdapter = new MyPagerAdapter(mDataSource);
        setAdapter(pagerAdapter);
        if (indicator != null) {
            indicator.setImageIndicator(dataSourceSize);
        }

        currentPosition = 1;
        setCurrentItem(1);
    }

    public void showNextView() {
        if (mDataSource == null || mDataSource.size() == 1)
            return;
        setCurrentItem(currentPosition + 1, true);
        setIndicatorSelectItem(currentPosition);
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float baifen, int offset) {

        }


        @Override
        public void onPageSelected(int position) {
            if (pagerAdapter.getCount() == 0)
                return;
            currentPosition = position;
            if (position < 1) {
                currentPosition = pagerAdapter.getCount() - 2;
            } else if (position >= pagerAdapter.getCount() - 1) {
                currentPosition = 1;
            }
            setIndicatorSelectItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (pagerAdapter.getCount() == 0)
                return;
            if (state == ViewPager.SCROLL_STATE_SETTLING) {

            } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                currentPosition = Banner.this.getCurrentItem();
                if (currentPosition < 1) {
                    currentPosition = pagerAdapter.getCount() - 2;
                    setCurrentItem(currentPosition, false);
                } else if (currentPosition >= pagerAdapter.getCount() - 1) {
                    currentPosition = 1;
                    setCurrentItem(currentPosition, false);
                }
                pauseScroll();
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                setCurrentItem(currentPosition, false);
                resumeScroll();
            }
        }
    };

    public void resumeScroll() {
        if (mDataSource == null || mDataSource.size() == 1)
            return;
        myHandler.sendMessageDelayed(myHandler.obtainMessage(MyHandler.MESSAGE_CHECK), interval);
    }

    public void pauseScroll() {
        myHandler.removeMessages(MyHandler.MESSAGE_CHECK);
    }

    public interface BannerDataInit {
        ImageView initImageView();

        void initImgData(ImageView imageView, Object imgPath);
    }

    BannerDataInit bannerDataInit;

    public void setBannerDataInit(BannerDataInit bannerDataInit) {
        this.bannerDataInit = bannerDataInit;
    }

    private void setIndicatorSelectItem(int position) {
        if (indicator == null) return;
        if (position == 0) {
            indicator.setSelectItem(dataSourceSize - 1);
        } else if (position == dataSourceSize + 1) {
            indicator.setSelectItem(0);
        } else {
            indicator.setSelectItem(position - 1);
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        private final ArrayList<ImageView> viewList;

        public MyPagerAdapter(List<Object> data) {
            if (data == null) {
                viewList = new ArrayList<>();
                return;
            }

            viewList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                ImageView imageView;
                imageView = bannerDataInit.initImageView();
                final int finalI = i;
                imageView.setOnClickListener(
                        new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (finalI == 0) {
                                    if (onBannerItemClickListener != null) {
                                        onBannerItemClickListener.onItemClick(dataSourceSize - 1);
                                    }
                                } else if (finalI == dataSourceSize + 1) {
                                    if (onBannerItemClickListener != null) {
                                        onBannerItemClickListener.onItemClick(0);
                                    }
                                } else {
                                    if (onBannerItemClickListener != null) {
                                        onBannerItemClickListener.onItemClick(finalI - 1);
                                    }
                                }

                            }
                        });

                bannerDataInit.initImgData(imageView, mDataSource.get(i));
                viewList.add(imageView);
            }
        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView(viewList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "title" + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(viewList.get(position), lp);
            return viewList.get(position);
        }

    }

    private static class MyHandler extends Handler {
        public static final int MESSAGE_CHECK = 9001;
        private WeakReference<Banner> innerObject;

        public MyHandler(Banner context) {
            this.innerObject = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (MESSAGE_CHECK == msg.what) {
                Banner banner = innerObject.get();
                if (banner == null)
                    return;
                if (banner.getContext() instanceof Activity) {
                    Activity activity = (Activity) banner.getContext();
                    if (activity.isFinishing())
                        return;
                }
                banner.showNextView();

                removeMessages(MESSAGE_CHECK);
                sendMessageDelayed(obtainMessage(MESSAGE_CHECK), banner.interval);
                return;
            }
            super.handleMessage(msg);
        }

    }

    OnBannerItemClickListener onBannerItemClickListener;

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);

    }
}