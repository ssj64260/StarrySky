package com.cxb.starrysky.widget.starrysky;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cxb.starrysky.R;
import com.cxb.starrysky.model.CircleInfo;
import com.cxb.starrysky.model.PersonInfo;
import com.cxb.starrysky.utils.DisplayUtil;
import com.cxb.starrysky.widget.imageloader.GlideCircleTransform;
import com.cxb.starrysky.widget.imageloader.ImageLoaderFactory;
import com.cxb.starrysky.widget.imageloader.ImageLoaderWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 星空ViewGroup
 */

public class StarrySkyView extends ViewGroup {

    private static final int CENTER_VIEW_WIDTH_DP = 80;//中间View宽度80dp
    private static final int AROUND_VIEW_WIDTH_DP = 44;//周围View宽度50dp
    private static final int SPACE_DP = 25;//水平间隔10dp
    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击
    private final float[] angles = {
            11.25f, 22.5f, 33.75f, 45f,
            56.25f, 67.5f, 78.75f, 90f,
            101.25f, 112.5f, 123.75f, 135f,
            146.25f, 157.5f, 168.75f, 180f,
            191.25f, 202.5f, 213.75f, 225f,
            236.25f, 247.5f, 258.75f, 270f,
            281.25f, 292.5f, 303.75f, 315f,
            326.25f, 337.5f, 348.75f, 360f
    };

    private OnStarSelectListener mOnStarSelectListener;

    private int mCenterViewWidthPX;//中间View宽度PX
    private int mAroundViewWidthPX;//周围View宽度PX

    private int mSpaceWidthPX;//间隔距离

    private int mShowWidthPX;//在屏幕所占的宽度
    private int mShowHeightPX;//在屏幕所占的高度

    private int mCenterWidthMeasureSpec;
    private int mAroundWidthMeasureSpec;

    private int mScrollWidth;//移动范围
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    private List<PersonInfo> mPersonList;

    private ImageLoaderWrapper mImageLoader;//图片加载器
    private GlideCircleTransform mTransform;//画圆类

    private List<CircleInfo> circleList;

    private List<CircleInfo> firstQuadrant;//第一象限点集合
    private List<CircleInfo> secondQuadrant;//第二象限点集合
    private List<CircleInfo> thirdQuadrant;//第三象限点集合
    private List<CircleInfo> fourthQuadrant;//第四象限点集合

    public StarrySkyView(Context context) {
        this(context, null, 0);
    }

    public StarrySkyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarrySkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageLoader = ImageLoaderFactory.getLoader();
        mTransform = new GlideCircleTransform(context, 1, R.color.white);

        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
        mCenterViewWidthPX = DisplayUtil.dip2px(CENTER_VIEW_WIDTH_DP);
        mAroundViewWidthPX = DisplayUtil.dip2px(AROUND_VIEW_WIDTH_DP);

        mSpaceWidthPX = DisplayUtil.dip2px(SPACE_DP);

        mCenterWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mCenterViewWidthPX, MeasureSpec.EXACTLY);
        mAroundWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mAroundViewWidthPX, MeasureSpec.EXACTLY);

        final float radius1 = mCenterViewWidthPX * 1f / 2f + mAroundViewWidthPX * 1f / 2f + mSpaceWidthPX;
        final float radius2 = mCenterViewWidthPX * 1f / 2f + mAroundViewWidthPX * 3f / 2f + mSpaceWidthPX * 2;
        final float radius3 = mCenterViewWidthPX * 1f / 2f + mAroundViewWidthPX * 5f / 2f + mSpaceWidthPX * 3;

        firstQuadrant = new ArrayList<>();
        firstQuadrant.add(new CircleInfo(angles[27], radius1));
        firstQuadrant.add(new CircleInfo(angles[31], radius1));
        firstQuadrant.add(new CircleInfo(angles[24], radius2));
        firstQuadrant.add(new CircleInfo(angles[26], radius2));
        firstQuadrant.add(new CircleInfo(angles[28], radius2));
        firstQuadrant.add(new CircleInfo(angles[30], radius2));
        firstQuadrant.add(new CircleInfo(angles[24], radius3));
        firstQuadrant.add(new CircleInfo(angles[26], radius3));
        secondQuadrant = new ArrayList<>();
        secondQuadrant.add(new CircleInfo(angles[19], radius1));
        secondQuadrant.add(new CircleInfo(angles[23], radius1));
        secondQuadrant.add(new CircleInfo(angles[16], radius2));
        secondQuadrant.add(new CircleInfo(angles[18], radius2));
        secondQuadrant.add(new CircleInfo(angles[20], radius2));
        secondQuadrant.add(new CircleInfo(angles[22], radius2));
        secondQuadrant.add(new CircleInfo(angles[20], radius3));
        secondQuadrant.add(new CircleInfo(angles[22], radius3));
        thirdQuadrant = new ArrayList<>();
        thirdQuadrant.add(new CircleInfo(angles[11], radius1));
        thirdQuadrant.add(new CircleInfo(angles[15], radius1));
        thirdQuadrant.add(new CircleInfo(angles[8], radius2));
        thirdQuadrant.add(new CircleInfo(angles[10], radius2));
        thirdQuadrant.add(new CircleInfo(angles[12], radius2));
        thirdQuadrant.add(new CircleInfo(angles[14], radius2));
        thirdQuadrant.add(new CircleInfo(angles[8], radius3));
        thirdQuadrant.add(new CircleInfo(angles[10], radius3));
        fourthQuadrant = new ArrayList<>();
        fourthQuadrant.add(new CircleInfo(angles[3], radius1));
        fourthQuadrant.add(new CircleInfo(angles[7], radius1));
        fourthQuadrant.add(new CircleInfo(angles[0], radius2));
        fourthQuadrant.add(new CircleInfo(angles[2], radius2));
        fourthQuadrant.add(new CircleInfo(angles[4], radius2));
        fourthQuadrant.add(new CircleInfo(angles[6], radius2));
        fourthQuadrant.add(new CircleInfo(angles[4], radius3));
        fourthQuadrant.add(new CircleInfo(angles[6], radius3));

        circleList = new ArrayList<>();
    }

    public void setPersonList(List<PersonInfo> list) {
        removeAllViews();
        this.mPersonList = list;
        initView();
        invalidate();
    }

    private void initView() {
        if (mPersonList != null) {
            final int maxSize = firstQuadrant.size() + secondQuadrant.size() + thirdQuadrant.size() + fourthQuadrant.size() + 1;
//            final int maxSize = circleList.size() + 1;
            final int personSize = mPersonList.size() > maxSize ? maxSize : mPersonList.size();
            final int eachSize = (personSize - 2) / 4 + 1;
            circleList.clear();
            Collections.shuffle(firstQuadrant);
            circleList.addAll(firstQuadrant.subList(0, eachSize));
            Collections.shuffle(secondQuadrant);
            circleList.addAll(secondQuadrant.subList(0, eachSize));
            Collections.shuffle(thirdQuadrant);
            circleList.addAll(thirdQuadrant.subList(0, eachSize));
            Collections.shuffle(fourthQuadrant);
            circleList.addAll(fourthQuadrant.subList(0, eachSize));
            Collections.shuffle(circleList);

            for (int i = 0; i < personSize; i++) {
                final View starryView = LayoutInflater.from(getContext()).inflate(R.layout.item_starry_sky, this, false);
                starryView.setTag(i);
                starryView.setOnClickListener(click);
                if (i == 0) {
                    starryView.getLayoutParams().width = mCenterViewWidthPX;
                    starryView.getLayoutParams().height = mCenterViewWidthPX;
                } else {
                    starryView.getLayoutParams().width = mAroundViewWidthPX;
                    starryView.getLayoutParams().height = mAroundViewWidthPX;
                }

                final ImageView ivAvatar = (ImageView) starryView.findViewById(R.id.iv_avatar);
                final String url = mPersonList.get(i).getMemberImg();
                mImageLoader.loadWithAnimate(ivAvatar, url, mTransform);

                addView(starryView);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mShowWidthPX = MeasureSpec.getSize(widthMeasureSpec);
        mShowHeightPX = MeasureSpec.getSize(heightMeasureSpec);

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (i == 0) {
                childView.measure(mCenterWidthMeasureSpec, mCenterWidthMeasureSpec);
            } else {
                childView.measure(mAroundWidthMeasureSpec, mAroundWidthMeasureSpec);
            }
        }

        setMeasuredDimension(mShowWidthPX, mShowHeightPX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int childCount = getChildCount();
        if (childCount > 0) {
            final View centerView = getChildAt(0);
            final int centerLeft = (mShowWidthPX - mCenterViewWidthPX) / 2;
            final int centerTop = (mShowHeightPX - mCenterViewWidthPX) / 2;
            setChildViewFrame(centerView, centerLeft, centerTop, mCenterViewWidthPX, mCenterViewWidthPX);

            final int centerPointX = (int) (centerLeft + mCenterViewWidthPX / 2f);
            final int centerPointY = (int) (centerTop + mCenterViewWidthPX / 2f);

            for (int i = 1; i < childCount; i++) {
                final View aroundView = getChildAt(i);
                final CircleInfo circle = circleList.get(i - 1);
                final Point aroundPoint = circle.CircularPoint(centerPointX, centerPointY);
                final int aroundLeft = (int) (aroundPoint.x - mAroundViewWidthPX / 2f);
                final int aroundTop = (int) (aroundPoint.y - mAroundViewWidthPX / 2f);
                setChildViewFrame(aroundView, aroundLeft, aroundTop, mAroundViewWidthPX, mAroundViewWidthPX);
            }
        }
    }

    private void setChildViewFrame(View childView, int left, int top, int width, int height) {
        childView.layout(left, top, left + width, top + height);
    }

    public void setOnStarSelectListener(OnStarSelectListener onStarSelectListener) {
        this.mOnStarSelectListener = onStarSelectListener;
    }

    private OnClickListener click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnStarSelectListener != null) {
                final int position = (int) v.getTag();
                mOnStarSelectListener.onStarSelect(position);
            }
        }
    };

    private void moveChildren(float distanceX) {
        int childCount = getChildCount();
        if (childCount > 0) {
            View centerView = getChildAt(0);
            final float scale = 1f - Math.abs(distanceX) / mShowWidthPX;
            final float alpha = 1f - Math.abs(distanceX) / mShowWidthPX / 2f;
            centerView.setScaleX(scale);
            centerView.setScaleY(scale);
            centerView.setAlpha(alpha);

            for (int i = 1; i < childCount; i++) {
                View childView = getChildAt(i);
                childView.setTranslationX((i + 1f) / 10f * distanceX);
                childView.setScaleX(scale);
                childView.setScaleY(scale);
                childView.setAlpha(alpha);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float dX = event.getX() - mLastTouchX;
                moveChildren(dX);
                break;
            case MotionEvent.ACTION_UP:
                final int distanceX = (int) event.getX() - mLastTouchX;
                if (Math.abs(distanceX) > mShowWidthPX / 3) {
                    if (mOnStarSelectListener != null) {
                        mOnStarSelectListener.onSliding();
                        return true;
                    }
                }
                moveChildren(0);
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercerpt = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastInterceptX = (int) event.getX();
                mLastInterceptY = (int) event.getY();
                mLastTouchX = (int) event.getX();
                intercerpt = false;
                break;
            case MotionEvent.ACTION_MOVE:
                final int distanceX = Math.abs((int) event.getX() - mLastInterceptX);
                final int distanceY = Math.abs((int) event.getY() - mLastInterceptY);
                if (distanceX < mScrollWidth && distanceY < mScrollWidth) {
                    intercerpt = false;
                } else {
                    intercerpt = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercerpt = false;
                break;
        }
        return intercerpt;
    }
}
