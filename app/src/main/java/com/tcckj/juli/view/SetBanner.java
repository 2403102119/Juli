package com.tcckj.juli.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.tcckj.juli.R;
import com.tcckj.juli.util.ImageLoadUtil;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.List;

/**
 * 设置轮播图
 */

public class SetBanner {
    @SuppressLint("StaticFieldLeak")
    public static Activity context;

    public static void startBanner(Activity ctx, Banner banner, List<String> list){
        context=ctx;
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(list);
        banner.setDelayTime(2000);
        banner.setBannerAnimation(Transformer.Accordion);
        banner.start();
    }
    /**
     * banner图的图片加载器
     */
    public static class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            ImageLoadUtil.showImage(SetBanner.context,(String) path,imageView);
//            imageView.setImageResource(R.mipmap.advertising);
        }
    }
}
