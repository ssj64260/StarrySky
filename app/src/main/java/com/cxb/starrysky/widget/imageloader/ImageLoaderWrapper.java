package com.cxb.starrysky.widget.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 加载图片接口
 */

public interface ImageLoaderWrapper {

    void loadWithoutAnimate(ImageView imageView, String url, BitmapTransformation transformation, int placeholder, int errorImage);

    void loadWithAnimate(ImageView imageView, String url, BitmapTransformation transformation);
}
