package com.cxb.starrysky.widget.starrysky;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cxb.starrysky.R;
import com.cxb.starrysky.model.PersonInfo;
import com.cxb.starrysky.utils.DisplayUtil;
import com.cxb.starrysky.utils.ToastMaster;
import com.cxb.starrysky.widget.imageloader.GlideCircleTransform;
import com.cxb.starrysky.widget.imageloader.ImageLoaderFactory;
import com.cxb.starrysky.widget.imageloader.ImageLoaderWrapper;

import java.util.List;


/**
 * 星空ViewGroup
 */

public class StarrySkyView extends ViewGroup {

    private static final int CENTER_VIEW_WIDTH_DP = 90;//中间View宽度80dp
    private static final int CENTER_VIEW_HEIGHT_DP = 90;//中间View高度80dp
    private static final int AROUND_VIEW_WIDTH_DP = 50;//周围View宽度50dp
    private static final int AROUND_VIEW_HEIGHT_DP = 50;//周围View高度50dp

    private static final double COEFFICIENT_ADD1 = Math.sqrt(2f) / 4f + 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_SUB1 = Math.sqrt(2f) / 4f - 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_ADD3 = Math.sqrt(2f) * 3f / 4f + 1f / 2f;//圆形布局系数
    private static final double COEFFICIENT_SUB3 = Math.sqrt(2f) * 3f / 4f - 1f / 2f;//圆形布局系数

    private static final int HORITONTAL_SPACE_DP = 10;//水平间隔10dp
    private static final int VERTICAL_SPACE_DP = 10;//垂直间隔10dp

    private static final int HORITONTAL_OFFSET_DP = 25;//水平偏移量20dp
    private static final int VERTICAL_OFFSET_DP = 15;//垂直偏移量10dp

    private OnStarSelectListener mOnStarSelectListener;

    private int mCenterViewWidthPX;//中间View宽度PX
    private int mCenterViewHeightPX;//中间View高度PX
    private int mAroundViewWidthPX;//周围View宽度PX
    private int mAroundViewHeightPX;//周围View高度PX

    private int mVerticalSpaceWidthPX;//垂直间隔距离
    private int mHoritontalSpaceWidthPX;//水平间隔距离

    private int mHoritontalOffsetPX;//水平偏移量
    private int mVerticalOffsetPX;//垂直偏移量

    private int mShowWidthPX;//在屏幕所占的宽度
    private int mShowHeightPX;//在屏幕所占的高度

    private int mCenterWidthMeasureSpec;
    private int mCenterHeightMeasureSpec;
    private int mAroundWidthMeasureSpec;
    private int mAroundHeightMeasureSpec;

    private List<PersonInfo> mPersonList;

    private ImageLoaderWrapper mImageLoader;//图片加载器
    private GlideCircleTransform mTransform;//画圆类

    private Point[][] points;
    private int pointTypePosition = 0;//当前类型

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

        mCenterViewWidthPX = DisplayUtil.dip2px(CENTER_VIEW_WIDTH_DP);
        mCenterViewHeightPX = DisplayUtil.dip2px(CENTER_VIEW_HEIGHT_DP);
        mAroundViewWidthPX = DisplayUtil.dip2px(AROUND_VIEW_WIDTH_DP);
        mAroundViewHeightPX = DisplayUtil.dip2px(AROUND_VIEW_HEIGHT_DP);

        mHoritontalSpaceWidthPX = DisplayUtil.dip2px(HORITONTAL_SPACE_DP);
        mVerticalSpaceWidthPX = DisplayUtil.dip2px(VERTICAL_SPACE_DP);
        mHoritontalOffsetPX = DisplayUtil.dip2px(HORITONTAL_OFFSET_DP);
        mVerticalOffsetPX = DisplayUtil.dip2px(VERTICAL_OFFSET_DP);

        mCenterWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mCenterViewWidthPX, MeasureSpec.EXACTLY);
        mCenterHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mCenterViewHeightPX, MeasureSpec.EXACTLY);
        mAroundWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mAroundViewWidthPX, MeasureSpec.EXACTLY);
        mAroundHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mAroundViewHeightPX, MeasureSpec.EXACTLY);

        points = new Point[][]{
//                {
//                        new Point(-mAroundViewWidthPX, -(mCenterViewHeightPX + mAroundViewHeightPX)),
//                        new Point(mCenterViewWidthPX, -(mCenterViewHeightPX + mAroundViewHeightPX)),
//                        new Point(0, -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
//                        new Point(mCenterViewWidthPX * 2 - mAroundViewWidthPX, -mAroundViewHeightPX),
//                        new Point(-mCenterViewWidthPX, 0),
//                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2, mCenterViewHeightPX - mAroundViewHeightPX),
//                        new Point(-mCenterViewWidthPX, mCenterViewHeightPX),
//                        new Point(0, mCenterViewHeightPX * 2 - mAroundViewHeightPX),
//                        new Point(-mAroundViewWidthPX, mCenterViewHeightPX * 3 - mAroundViewHeightPX),
//                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2, mCenterViewHeightPX * 2)
//                },
                {
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2 + mHoritontalSpaceWidthPX,
                                -(mCenterViewHeightPX * 3 + mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2 + mHoritontalSpaceWidthPX,
                                -(mCenterViewHeightPX + mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2 + mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX - mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2 + mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX * 3 - mAroundViewHeightPX) / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2 + mHoritontalSpaceWidthPX,
                                (mCenterViewHeightPX * 5 - mAroundViewHeightPX) / 2)
                },
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
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX, -mAroundViewHeightPX * 7 / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2, -mAroundViewHeightPX * 7 / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2, -mAroundViewHeightPX * 7 / 2),
                        new Point(-(mCenterViewWidthPX + mAroundViewWidthPX) / 2 - mHoritontalSpaceWidthPX, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2),
                        new Point((mCenterViewWidthPX - mAroundViewHeightPX) / 2, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2),
                        new Point((mCenterViewWidthPX * 3 - mAroundViewWidthPX) / 2, mCenterViewHeightPX + mAroundViewHeightPX * 5 / 2)
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
            pointTypePosition++;
            if (pointTypePosition >= points.length) {
                pointTypePosition = 0;
            }

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
                ToastMaster.toast(mPersonList.get(position).getMemberName());
            }
            int position = (int) v.getTag();
            ToastMaster.toast(mPersonList.get(position).getMemberName());
        }
    };
}
