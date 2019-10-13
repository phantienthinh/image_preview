package com.mgosu.imagepreview.ui.wiget.slide;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.mgosu.imagepreview.R;
import com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.PageIndicatorView;
import com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.animation.type.AnimationType;
import com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.draw.controller.DrawController;
import com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.draw.data.Orientation;
import com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.draw.data.RtlMode;
import com.mgosu.imagepreview.ui.wiget.slide.Transformations.FadeTransformation;

import static com.mgosu.imagepreview.ui.wiget.slide.IndicatorView.draw.controller.AttributeController.getRtlMode;

public class SliderView extends FrameLayout {

    public static final int AUTO_CYCLE_DIRECTION_RIGHT = 0;
    public static final int AUTO_CYCLE_DIRECTION_LEFT = 1;
    public static final int AUTO_CYCLE_DIRECTION_BACK_AND_FORTH = 2;

    private final Handler mHandler = new Handler();
    private boolean mFlagBackAndForth;
    private boolean mIsAutoCycle;
    private int mAutoCycleDirection;
    private int mScrollTimeInSec;
    private CircularSliderHandle mCircularSliderHandle;
    private DataSetObserver mDataSetObserver;
    private PagerAdapter mPagerAdapter;
    private Runnable mSliderRunnable;
    private SliderPager mSliderPager;
    private PageIndicatorView mPagerIndicator;

    public SliderView(Context context) {
        super(context);
        setupSlideView(context);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupSlideView(context);
        setUpAttributes(context, attrs);
    }

    public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupSlideView(context);
        setUpAttributes(context, attrs);
    }

    private void setUpAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SliderView, 0, 0);

        int indicatorOrientation = typedArray.getInt(R.styleable.SliderView_sliderIndicatorOrientation, Orientation.HORIZONTAL.ordinal());
        Orientation orientation;
        if (indicatorOrientation == 0) {
            orientation = Orientation.HORIZONTAL;
        } else {
            orientation = Orientation.VERTICAL;
        }

        int indicatorRtlMode = typedArray.getInt(R.styleable.SliderView_sliderIndicatorRtlMode, RtlMode.Off.ordinal());
        RtlMode rtlMode = getRtlMode(indicatorRtlMode);
        int sliderAnimationDuration = typedArray.getInt(R.styleable.SliderView_sliderAnimationDuration, SliderPager.DEFAULT_SCROLL_DURATION);
        int sliderScrollTimeInSec = typedArray.getInt(R.styleable.SliderView_sliderScrollTimeInSec, 2);
        boolean sliderCircularHandlerEnabled = typedArray.getBoolean(R.styleable.SliderView_sliderCircularHandlerEnabled, true);
        boolean sliderAutoCycleEnabled = typedArray.getBoolean(R.styleable.SliderView_sliderAutoCycleEnabled, true);
        boolean sliderStartAutoCycle = typedArray.getBoolean(R.styleable.SliderView_sliderStartAutoCycle, false);
        int sliderAutoCycleDirection = typedArray.getInt(R.styleable.SliderView_sliderAutoCycleDirection, AUTO_CYCLE_DIRECTION_RIGHT);

        setSliderAnimationDuration(sliderAnimationDuration);
        setScrollTimeInSec(sliderScrollTimeInSec);
        setCircularHandlerEnabled(sliderCircularHandlerEnabled);
        setAutoCycle(sliderAutoCycleEnabled);
        setAutoCycleDirection(sliderAutoCycleDirection);
        if (sliderStartAutoCycle) {
            startAutoCycle();
        }

        typedArray.recycle();
    }

    private void setupSlideView(Context context) {

        View wrapperView = LayoutInflater
                .from(context)
                .inflate(R.layout.slider_view, this, true);

        mSliderPager = wrapperView.findViewById(R.id.vp_slider_layout);
        mCircularSliderHandle = new CircularSliderHandle(mSliderPager);
        mSliderPager.addOnPageChangeListener(mCircularSliderHandle);
        mSliderPager.setOffscreenPageLimit(4);

        mPagerIndicator = wrapperView.findViewById(R.id.pager_indicator);
        mPagerIndicator.setViewPager(mSliderPager);
    }

    public void setOnIndicatorClickListener(DrawController.ClickListener listener) {
        mPagerIndicator.setClickListener(listener);
    }

    public void setCurrentPageListener(CircularSliderHandle.CurrentPageListener listener) {
        mCircularSliderHandle.setCurrentPageListener(listener);
    }

    public void setSliderAdapter(final PagerAdapter pagerAdapter) {
        mPagerAdapter = pagerAdapter;
        //set slider adapter
        //registerAdapterDataObserver();
        mSliderPager.setAdapter(pagerAdapter);
        //setup with indicator
        mPagerIndicator.setCount(getAdapterItemsCount());
        mPagerIndicator.setDynamicCount(true);
    }

    public PagerAdapter getSliderAdapter() {
        return mPagerAdapter;
    }

    private void registerAdapterDataObserver() {

        if (mDataSetObserver != null) {
            mPagerAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mDataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mSliderPager.setOffscreenPageLimit(getAdapterItemsCount() - 1);
            }
        };

        mPagerAdapter.registerDataSetObserver(mDataSetObserver);
    }

    public boolean isAutoCycle() {
        return mIsAutoCycle;
    }

    public void setAutoCycle(boolean autoCycle) {
        this.mIsAutoCycle = autoCycle;
        if (!mIsAutoCycle && mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }
    }

    public void setOffscreenPageLimit(int limit) {
        mSliderPager.setOffscreenPageLimit(limit);
    }

    public void setCircularHandlerEnabled(boolean enable) {
        mSliderPager.clearOnPageChangeListeners();
        if (enable) {
            mSliderPager.addOnPageChangeListener(mCircularSliderHandle);
        }
    }

    public int getScrollTimeInSec() {
        return mScrollTimeInSec;
    }

    public void setScrollTimeInSec(int time) {
        mScrollTimeInSec = time;
    }

    public void setSliderTransformAnimation(SliderAnimations animation) {

        switch (animation) {

            case FADETRANSFORMATION:
                mSliderPager.setPageTransformer(false, new FadeTransformation());
                break;
        }

    }

    public void setCustomSliderTransformAnimation(ViewPager.PageTransformer animation) {
        mSliderPager.setPageTransformer(false, animation);
    }

    public void setSliderAnimationDuration(int duration) {
        mSliderPager.setScrollDuration(duration);
    }

    public void setCurrentPagePosition(int position) {

        if (getSliderAdapter() != null) {
            mSliderPager.setCurrentItem(position, true);
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public int getCurrentPagePosition() {

        if (getSliderAdapter() != null) {
            return mSliderPager.getCurrentItem();
        } else {
            throw new NullPointerException("Adapter not set");
        }
    }

    public void setIndicatorAnimationDuration(long duration) {
        mPagerIndicator.setAnimationDuration(duration);
    }

    public void setIndicatorGravity(int gravity) {
        LayoutParams layoutParams = (LayoutParams) mPagerIndicator.getLayoutParams();
        layoutParams.gravity = gravity;
        mPagerIndicator.setLayoutParams(layoutParams);
    }

    public void setIndicatorPadding(int padding) {
        mPagerIndicator.setPadding(padding);
    }

    public void setIndicatorOrientation(Orientation orientation) {
        mPagerIndicator.setOrientation(orientation);
    }

    public void setIndicatorAnimation(IndicatorAnimations animations) {

        switch (animations) {
            case DROP:
                mPagerIndicator.setAnimationType(AnimationType.DROP);
                break;
            case FILL:
                mPagerIndicator.setAnimationType(AnimationType.FILL);
                break;
            case NONE:
                mPagerIndicator.setAnimationType(AnimationType.NONE);
                break;
            case SWAP:
                mPagerIndicator.setAnimationType(AnimationType.SWAP);
                break;
            case WORM:
                mPagerIndicator.setAnimationType(AnimationType.WORM);
                break;
            case COLOR:
                mPagerIndicator.setAnimationType(AnimationType.COLOR);
                break;
            case SCALE:
                mPagerIndicator.setAnimationType(AnimationType.SCALE);
                break;
            case SLIDE:
                mPagerIndicator.setAnimationType(AnimationType.SLIDE);
                break;
            case SCALE_DOWN:
                mPagerIndicator.setAnimationType(AnimationType.SCALE_DOWN);
                break;
            case THIN_WORM:
                mPagerIndicator.setAnimationType(AnimationType.THIN_WORM);
                break;
        }
    }

    public void setIndicatorVisibility(boolean visibility) {
        if (visibility) {
            mPagerIndicator.setVisibility(VISIBLE);
        } else {
            mPagerIndicator.setVisibility(GONE);
        }
    }

    private int getAdapterItemsCount() {
        try {
            return getSliderAdapter().getCount();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public void startAutoCycle() {

        if (mSliderRunnable != null) {
            mHandler.removeCallbacks(mSliderRunnable);
            mSliderRunnable = null;
        }

        mSliderRunnable = new Runnable() {
            @Override
            public void run() {

                try {
                    // check is on auto scroll mode
                    if (!mIsAutoCycle) {
                        return;
                    }

                    int currentPosition = mSliderPager.getCurrentItem();

                    if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_BACK_AND_FORTH) {
                        if (currentPosition == 0) {
                            mFlagBackAndForth = true;
                        }
                        if (currentPosition == getAdapterItemsCount() - 1) {
                            mFlagBackAndForth = false;
                        }
                        if (mFlagBackAndForth) {
                            mSliderPager.setCurrentItem(++currentPosition, true);
                        } else {
                            mSliderPager.setCurrentItem(--currentPosition, true);
                        }
                    } else if (mAutoCycleDirection == AUTO_CYCLE_DIRECTION_LEFT) {
                        if (currentPosition == 0) {
                            mSliderPager.setCurrentItem(getAdapterItemsCount() - 1, true);
                        } else {
                            mSliderPager.setCurrentItem(--currentPosition, true);
                        }
                    } else {
                        if (currentPosition == getAdapterItemsCount() - 1) {
                            // if is last item return to the first position
                            mSliderPager.setCurrentItem(0, true);
                        } else {
                            // continue smooth transition between pager
                            mSliderPager.setCurrentItem(++currentPosition, true);
                        }
                    }


                } finally {
                    mHandler.postDelayed(this, mScrollTimeInSec * 1000);
                }

            }
        };

        //Run the loop for the first time
        mHandler.postDelayed(mSliderRunnable, mScrollTimeInSec * 1000);
    }

    public void setAutoCycleDirection(int direction) {
        mAutoCycleDirection = direction;
    }

    public int getAutoCycleDirection() {
        return mAutoCycleDirection;
    }

    public int getIndicatorRadius() {
        return mPagerIndicator.getRadius();
    }

    public void setIndicatorRtlMode(RtlMode rtlMode) {
        mPagerIndicator.setRtlMode(rtlMode);
    }

    public void setIndicatorRadius(int pagerIndicatorRadius) {
        this.mPagerIndicator.setRadius(pagerIndicatorRadius);
    }

    public void setIndicatorMargin(int margin) {
        LayoutParams layoutParams = (LayoutParams) mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(margin, margin, margin, margin);
        mPagerIndicator.setLayoutParams(layoutParams);
    }

    public void setIndicatorSelectedColor(int color) {
        this.mPagerIndicator.setSelectedColor(color);
    }

    public int getIndicatorSelectedColor() {
        return this.mPagerIndicator.getSelectedColor();
    }

    public void setIndicatorUnselectedColor(int color) {
        this.mPagerIndicator.setUnselectedColor(color);
    }

    public int getIndicatorUnselectedColor() {
        return this.mPagerIndicator.getUnselectedColor();
    }

}
