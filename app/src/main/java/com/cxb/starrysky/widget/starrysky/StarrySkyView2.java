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
import com.cxb.starrysky.model.PersonInfo;
import com.cxb.starrysky.utils.DisplayUtil;
import com.cxb.starrysky.widget.imageloader.GlideCircleTransform;
import com.cxb.starrysky.widget.imageloader.ImageLoaderFactory;
import com.cxb.starrysky.widget.imageloader.ImageLoaderWrapper;

import java.util.List;


/**
 * 星空ViewGroup 固定布局
 */

public class StarrySkyView2 extends ViewGroup {

    private static final int CENTER_VIEW_WIDTH_DP = 90;//中间View宽度80dp
    private static final int CENTER_VIEW_HEIGHT_DP = 90;//中间View高度80dp
    private static final int AROUND_VIEW_WIDTH_DP = 50;//周围View宽度50dp
    private static final int AROUND_VIEW_HEIGHT_DP = 50;//周围View高度50dp
    private static final int SCROLL_WIDTH = 2;//移动超过2dp，响应滑动，否则属于点击

    private static final double COEFFICIENT_ADD1 = Math.sqrt(2f) / 4f + 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_SUB1 = Math.sqrt(2f) / 4f - 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_ADD3 = Math.sqrt(2f) * 3f / 4f + 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_SUB3 = Math.sqrt(2f) * 3f / 4f - 1f / 2f;//圆形布局系数

    private OnStarSelectListener mOnStarSelectListener;

    private int mCenterViewWidthPX;//中间View宽度PX
    private int mCenterViewHeightPX;//中间View高度PX
    private int mAroundViewWidthPX;//周围View宽度PX
    private int mAroundViewHeightPX;//周围View高度PX

    private int mShowWidthPX;//在屏幕所占的宽度
    private int mShowHeightPX;//在屏幕所占的高度

    private int mCenterWidthMeasureSpec;
    private int mCenterHeightMeasureSpec;
    private int mAroundWidthMeasureSpec;
    private int mAroundHeightMeasureSpec;

    private int mScrollWidth;//移动范围
    private int mLastTouchX;//最后一次触摸的X坐标
    private int mLastInterceptX;
    private int mLastInterceptY;

    private List<PersonInfo> mPersonList;

    private ImageLoaderWrapper mImageLoader;//图片加载器
    private GlideCircleTransform mTransform;//画圆类

    private Point[][] points;
    private int pointTypePosition = 0;//当前类型

    public StarrySkyView2(Context context) {
        this(context, null, 0);
    }

    public StarrySkyView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarrySkyView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mImageLoader = ImageLoaderFactory.getLoader();
        mTransform = new GlideCircleTransform(context, 1, R.color.white);

        mScrollWidth = DisplayUtil.dip2px(SCROLL_WIDTH);
        mCenterViewWidthPX = DisplayUtil.dip2px(CENTER_VIEW_WIDTH_DP);
        mCenterViewHeightPX = DisplayUtil.dip2px(CENTER_VIEW_HEIGHT_DP);
        mAroundViewWidthPX = DisplayUtil.dip2px(AROUND_VIEW_WIDTH_DP);
        mAroundViewHeightPX = DisplayUtil.dip2px(AROUND_VIEW_HEIGHT_DP);

        mCenterWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mCenterViewWidthPX, MeasureSpec.EXACTLY);
        mCenterHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mCenterViewHeightPX, MeasureSpec.EXACTLY);
        mAroundWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mAroundViewWidthPX, MeasureSpec.EXACTLY);
        mAroundHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mAroundViewHeightPX, MeasureSpec.EXACTLY);

        points = new Point[][]{
                {
                        new Point(-mAroundViewWidthPX * 2, (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX) / 2, -mAroundViewHeightPX * 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX, (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX) / 2, mCenterViewHeightPX + mAroundViewHeightPX),
                        new Point((int) -(mCenterViewWidthPX * COEFFICIENT_SUB1 + mAroundViewWidthPX * COEFFICIENT_ADD3),
                                (int) -(mCenterViewHeightPX * COEFFICIENT_SUB1 + mAroundViewHeightPX * COEFFICIENT_ADD3)),
                        new Point((int) (mCenterViewWidthPX * COEFFICIENT_ADD1 + mAroundViewWidthPX * COEFFICIENT_SUB3),
                                (int) -(mCenterViewHeightPX * COEFFICIENT_SUB1 + mAroundViewHeightPX * COEFFICIENT_ADD3)),
                        new Point((int) -(mCenterViewWidthPX * COEFFICIENT_SUB1 + mAroundViewWidthPX * COEFFICIENT_ADD3),
                                (int) (mCenterViewHeightPX * COEFFICIENT_ADD1 + mAroundViewHeightPX * COEFFICIENT_SUB3)),
                        new Point((int) (mCenterViewWidthPX * COEFFICIENT_ADD1 + mAroundViewWidthPX * COEFFICIENT_SUB3),
                                (int) (mCenterViewHeightPX * COEFFICIENT_ADD1 + mAroundViewHeightPX * COEFFICIENT_SUB3)),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX * 5) / 2, -mAroundViewHeightPX * 7 / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2, -mAroundViewHeightPX * 7 / 2),
                        new Point((mCenterViewWidthPX + mAroundViewWidthPX * 3) / 2, -mAroundViewHeightPX * 7 / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX * 5) / 2, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2),
                        new Point((mCenterViewWidthPX + mAroundViewWidthPX * 3) / 2, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2)
                },
                {
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX) / 2,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX * 3) / 4,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 4,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX * 5) / 4,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX * 3) / 4,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 4,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 5 + mAroundViewWidthPX) / 4,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX * 3) / 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewWidthPX) / 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 + mAroundViewWidthPX) / 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                },
                {
                        new Point(-mAroundViewWidthPX * 2,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX * 2,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX * 2,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX * 2,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point(-mAroundViewWidthPX * 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point(mCenterViewWidthPX + mAroundViewWidthPX,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2)
                }
        };
    }

    public void setPersonList(List<PersonInfo> list) {
        removeAllViews();
        this.mPersonList = list;
        initView();
        invalidate();
    }

    private void initView() {
        if (mPersonList != null) {
            final int personSize = mPersonList.size() > 15 ? 15 : mPersonList.size();
            for (int i = 0; i < personSize; i++) {
                final View starryView = LayoutInflater.from(getContext()).inflate(R.layout.item_starry_sky, this, false);
                starryView.setTag(i);
                starryView.setOnClickListener(click);
                if (i == 0) {
                    starryView.getLayoutParams().width = mCenterViewWidthPX;
                    starryView.getLayoutParams().height = mCenterViewHeightPX;
                } else {
                    starryView.getLayoutParams().width = mAroundViewWidthPX;
                    starryView.getLayoutParams().height = mAroundViewHeightPX;
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
                childView.measure(mCenterWidthMeasureSpec, mCenterHeightMeasureSpec);
            } else {
                childView.measure(mAroundWidthMeasureSpec, mAroundHeightMeasureSpec);
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
            final int centerTop = (mShowHeightPX - mCenterViewHeightPX) / 2;
            setChildViewFrame(centerView, centerLeft, centerTop, mCenterViewWidthPX, mCenterViewHeightPX);

            for (int i = 1; i < childCount; i++) {
                final View aroundView = getChildAt(i);
                final int aroundLeft = centerLeft + points[pointTypePosition][i - 1].x;
                final int aroundTop = centerTop + points[pointTypePosition][i - 1].y;
                setChildViewFrame(aroundView, aroundLeft, aroundTop, mAroundViewWidthPX, mAroundViewHeightPX);
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
                int position = (int) v.getTag();
                mOnStarSelectListener.onStarSelect(position);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                final int distanceX = (int) event.getX() - mLastTouchX;
                if (Math.abs(distanceX) > mShowWidthPX / 3) {
                    if (mOnStarSelectListener != null) {
                        if (distanceX > 0) {
                            pointTypePosition--;
                            if (pointTypePosition < 0) {
                                pointTypePosition = points.length - 1;
                            }
                        } else {
                            pointTypePosition++;
                            if (pointTypePosition >= points.length) {
                                pointTypePosition = 0;
                            }
                        }
                        mOnStarSelectListener.onSliding();
                    }
                }
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
