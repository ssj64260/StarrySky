package com.cxb.starrysky.widget.imageloader;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.cxb.starrysky.R;
import com.cxb.starrysky.app.APP;

/**
 * 加载图片实现
 */

public class ImageLoader implements ImageLoaderWrapper {

    @Override
    public void loadWithAnimate(ImageView imageView, String url, BitmapTransformation transformation) {
        Glide.with(APP.getInstance())
                .load(url)
                .error(R.drawable.family_avatar)
                .centerCrop()
                .transform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .animate(R.anim.anime_scale)
                .into(imageView);
    }

    @Override
    public void loadWithoutAnimate(ImageView imageView, String url, BitmapTransformation transformation, int placeholder, int errorImage) {
        Glide.with(APP.getInstance())
                .load(url)
                .placeholder(placeholder)
                .error(errorImage)
                .centerCrop()
                .transform(transformation)
                .dontAnimate()
                .into(imageView);
    }
}
