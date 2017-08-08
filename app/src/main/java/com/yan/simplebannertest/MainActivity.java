package com.yan.simplebannertest;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.yan.simplebanner.SimpleBanner;
import com.yan.simplebanner.BannerIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SimpleBanner simpleBanner;
    BannerIndicator bannerIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        simpleBanner = (SimpleBanner) findViewById(R.id.SimpleCycleViewPager);
        List<String> drawables = new ArrayList<>();
        drawables.add("1");
        drawables.add("2");
        drawables.add("3");

        simpleBanner.setInterval(5000);
        simpleBanner.setPageChangeDuration(500);
        simpleBanner.setBannerDataInit(new SimpleBanner.BannerDataInit<String>() {
            @Override
            public ImageView initImageView() {
                return (ImageView) getLayoutInflater().inflate(R.layout.imageview, null);
            }

            @Override
            public void initImgData(ImageView imageView, String imgPath) {
                if (imgPath.equals("1"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.select_full));
                if (imgPath.equals("2"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg));
                if (imgPath.equals("3"))
                    imageView.setImageDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg_no));
            }
        });
        simpleBanner.setDataSource(drawables);

        //----------------------indicator start------------------------------
        bannerIndicator = (BannerIndicator) findViewById(R.id.indicator);
        bannerIndicator.setMargins(15,15,15,15);
        bannerIndicator.setIndicatorSource(
                ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg),//select
                ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg_no),//unselect
                50//widthAndHeight
        );
        simpleBanner.attachIndicator(bannerIndicator);
        //----------------------indicator end------------------------------


        simpleBanner.setOnBannerItemClickListener(new SimpleBanner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getBaseContext(), "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        simpleBanner.pauseScroll();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        simpleBanner.resumeScroll();
    }
}
