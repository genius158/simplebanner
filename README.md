# simplebanner
![GIF](GIF.gif)
# 原理
在adapter原来的数据的最前面插入最后面的元素，最后面同理，与viewpager那种size设置成无限相比，这样做性能相对更好，更节省内存。而size无限，单它的当前项为一个较大的数值，内部计算也会相应的增加计算压力。

# include lib
 Gradle:
      个人并不提倡引包，本身作也就两个类，直接拷贝到项目会更方便，也好改indicator。
      
# how to use
## 

     banner.resumeScroll();//回复
     banner.pauseScroll();//暂停
     
     banner.setInterval(5000);
        banner.setPageChangeDuration(500);
        banner.setBannerDataInit(new Banner.BannerDataInit() {
            @Override
            public ImageView initImageView() {
                //设置图片加载的控件（如：Fresco 这里可以传入SimpleDraweeView）
                return (ImageView) getLayoutInflater().inflate(R.layout.imageview, null);
            }

            @Override
            public void initImgData(ImageView imageView, Object imgPath) {
                //可在这里控制图片的加载，
                //ImageLoader.getInstance().displayImage(imgPath+"", imageView, options);
                //((SimpleDraweeView)imageView).setImageURI(Uri.parse(imgPath+""));
                 }
        });
        banner.setDataSource(drawables);

        //----------------------indicator start------------------------------
        bannerIndicator = (BannerIndicator) findViewById(R.id.indicator);
        bannerIndicator.setIndicatorSource(
                ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg),//select
                ContextCompat.getDrawable(getBaseContext(), R.drawable.select_bg_no),//unselect
                50//widthAndHeight
        );
        banner.attachIndicator(bannerIndicator);
        //----------------------indicator end------------------------------


        banner.setOnBannerItemClickListener(new Banner.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getBaseContext(), "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });

## LICENSE

    Copyright 2016 yan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


