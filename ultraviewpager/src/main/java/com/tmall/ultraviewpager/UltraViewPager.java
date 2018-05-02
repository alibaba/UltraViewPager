/*
 *
 *  MIT License
 *
 *  Copyright (c) 2017 Alibaba Group
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 *
 */

package com.tmall.ultraviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by mikeafc on 15/10/26.<br>
 * UltraViewPager is a super extension for ViewPager.<br>
 * It's actually a RelativeLayout in order to display indicator, UltraViewPager offers some usual
 * method delegate for ViewPager, you can also invoke more method by call getViewPager() and get the actual
 * ViewPager.
 */
public class UltraViewPager extends RelativeLayout implements IUltraViewPagerFeature {

    public enum ScrollMode {
        HORIZONTAL(0), VERTICAL(1);
        int id;

        ScrollMode(int id) {
            this.id = id;
        }

        static ScrollMode getScrollMode(int id) {
            for (ScrollMode scrollMode : values()) {
                if (scrollMode.id == id)
                    return scrollMode;
            }
            throw new IllegalArgumentException();
        }
    }

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }

    public enum ScrollDirection {
        NONE(0), BACKWARD(1), FORWARD(2);
        int id;

        ScrollDirection(int id) {
            this.id = id;
        }

        static ScrollDirection getScrollDirection(int id) {
            for (ScrollDirection direction : values()) {
                if (direction.id == id)
                    return direction;
            }
            throw new IllegalArgumentException();
        }
    }

    private final Point size;
    private final Point maxSize;

    private float ratio = Float.NaN;

    //Maximum width of child when enable multiScreen.
    private int maxWidth = -1;

    //Maximum height of child when enable multiScreen.
    private int maxHeight = -1;

    private UltraViewPagerView viewPager;
    private UltraViewPagerIndicator pagerIndicator;

    private TimerHandler timer;


    public UltraViewPager(Context context) {
        super(context);
        size = new Point();
        maxSize = new Point();
        initView();
    }

    public UltraViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        size = new Point();
        maxSize = new Point();
        initView();
        initView(context, attrs);
    }

    public UltraViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        size = new Point();
        maxSize = new Point();
        initView();
    }

    private void initView() {
        viewPager = new UltraViewPagerView(getContext());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setId(viewPager.hashCode());
        } else {
            viewPager.setId(View.generateViewId());
        }

        addView(viewPager, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.UltraViewPager);
        setAutoScroll(ta.getInt(R.styleable.UltraViewPager_upv_autoscroll, 0));
        setInfiniteLoop(ta.getBoolean(R.styleable.UltraViewPager_upv_infiniteloop, false));
        setRatio(ta.getFloat(R.styleable.UltraViewPager_upv_ratio, Float.NaN));
        setScrollMode(ScrollMode.getScrollMode(ta.getInt(R.styleable.UltraViewPager_upv_scrollmode, 0)));
        disableScrollDirection(ScrollDirection.getScrollDirection(ta.getInt(R.styleable.UltraViewPager_upv_disablescroll, 0)));
        setMultiScreen(ta.getFloat(R.styleable.UltraViewPager_upv_multiscreen, 1f));
        setAutoMeasureHeight(ta.getBoolean(R.styleable.UltraViewPager_upv_automeasure, false));
        setItemRatio(ta.getFloat(R.styleable.UltraViewPager_upv_itemratio, Float.NaN));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!Float.isNaN(ratio)) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize / ratio), MeasureSpec.EXACTLY);
        }
        size.set(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        if (maxWidth >= 0 || maxHeight >= 0) {
            maxSize.set(maxWidth, maxHeight);
            constrainTo(size, maxSize);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(size.x, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(size.y, MeasureSpec.EXACTLY);
        }
        if (viewPager.getConstrainLength() > 0) {
            if (viewPager.getConstrainLength() == heightMeasureSpec) {
                viewPager.measure(widthMeasureSpec, heightMeasureSpec);
                setMeasuredDimension(size.x, size.y);
            } else {
                if (viewPager.getScrollMode() == ScrollMode.HORIZONTAL) {
                    super.onMeasure(widthMeasureSpec, viewPager.getConstrainLength());
                } else {
                    super.onMeasure(viewPager.getConstrainLength(), heightMeasureSpec);
                }
            }
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startTimer();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startTimer();
        } else {
            stopTimer();
        }
    }

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        stopTimer();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        startTimer();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (timer != null) {
            final int action = ev.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                stopTimer();
            }
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                startTimer();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public IUltraIndicatorBuilder initIndicator() {
        disableIndicator();
        pagerIndicator = new UltraViewPagerIndicator(getContext());
        pagerIndicator.setViewPager(viewPager);
        pagerIndicator.setIndicatorBuildListener(new UltraViewPagerIndicator.UltraViewPagerIndicatorListener() {
            @Override
            public void build() {
                removeView(pagerIndicator);
                addView(pagerIndicator, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
        return pagerIndicator;
    }

    @Override
    public IUltraIndicatorBuilder initIndicator(int focusColor, int normalColor, int radiusInPixel, int gravity) {
        return initIndicator()
                .setFocusColor(focusColor)
                .setNormalColor(normalColor)
                .setRadius(radiusInPixel)
                .setGravity(gravity);
    }

    @Override
    public IUltraIndicatorBuilder initIndicator(int focusColor, int normalColor, int strokeColor, int strokeWidth, int radiusInPixel, int gravity) {
        return initIndicator()
                .setFocusColor(focusColor)
                .setNormalColor(normalColor)
                .setStrokeWidth(strokeWidth)
                .setStrokeColor(strokeColor)
                .setRadius(radiusInPixel)
                .setGravity(gravity);
    }

    @Override
    public IUltraIndicatorBuilder initIndicator(int focusResId, int normalResId, int gravity) {
        return initIndicator()
                .setFocusResId(focusResId)
                .setNormalResId(normalResId)
                .setGravity(gravity);
    }

    @Override
    public IUltraIndicatorBuilder initIndicator(Bitmap focusBitmap, Bitmap normalBitmap, int gravity) {
        return initIndicator()
                .setFocusIcon(focusBitmap)
                .setNormalIcon(normalBitmap)
                .setGravity(gravity);
    }

    @Override
    public void disableIndicator() {
        if (pagerIndicator != null) {
            removeView(pagerIndicator);
            pagerIndicator = null;
        }
    }

    public IUltraIndicatorBuilder getIndicator() {
        return pagerIndicator;
    }

    private TimerHandler.TimerHandlerListener mTimerHandlerListener = new TimerHandler.TimerHandlerListener() {
        @Override
        public int getNextItem() {
            return UltraViewPager.this.getNextItem();
        }

        @Override
        public void callBack() {
            scrollNextPage();
        }
    };

    @Override
    public void setAutoScroll(int intervalInMillis) {
        if (0 == intervalInMillis) {
            return;
        }
        if (timer != null) {
            disableAutoScroll();
        }
        timer = new TimerHandler(mTimerHandlerListener, intervalInMillis);
        startTimer();
    }

    @Override
    public void setAutoScroll(int intervalInMillis, SparseIntArray intervalArray) {
        if (0 == intervalInMillis) {
            return;
        }
        if (timer != null) {
            disableAutoScroll();
        }
        timer = new TimerHandler(mTimerHandlerListener, intervalInMillis);
        timer.specialInterval = intervalArray;
        startTimer();
    }

    @Override
    public void disableAutoScroll() {
        stopTimer();
        timer = null;
    }

    @Override
    public void setScrollMode(ScrollMode scrollMode) {
        viewPager.setScrollMode(scrollMode);
    }

    @Override
    public void setInfiniteLoop(boolean enableLoop) {
        viewPager.setEnableLoop(enableLoop);
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setRatio(float ratio) {
        this.ratio = ratio;
        viewPager.setRatio(ratio);
    }

    @Override
    public void setHGap(int pixel) {
        int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        viewPager.setMultiScreen((screenWidth - pixel) / (float) screenWidth);
        viewPager.setPageMargin(pixel);
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void disableScrollDirection(UltraViewPager.ScrollDirection direction) {

    }

    @Override
    public boolean scrollLastPage() {
        boolean isChange = false;
        if (viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0) {
            final int curr = viewPager.getCurrentItemFake();
            int lastPage = 0;
            if (curr > 0) {
                lastPage = curr - 1;
                isChange = true;
            }
            viewPager.setCurrentItemFake(lastPage, true);
        }
        return isChange;
    }

    @Override
    public boolean scrollNextPage() {
        boolean isChange = false;
        if (viewPager != null && viewPager.getAdapter() != null && viewPager.getAdapter().getCount() > 0) {
            final int curr = viewPager.getCurrentItemFake();
            int nextPage = 0;
            if (curr < viewPager.getAdapter().getCount() - 1) {
                nextPage = curr + 1;
                isChange = true;
            }
            viewPager.setCurrentItemFake(nextPage, true);
        }
        return isChange;
    }

    @Override
    public void setMultiScreen(float ratio) {
        if (ratio <= 0 || ratio > 1) {
            throw new IllegalArgumentException("");
        }
        if (ratio <= 1f) {
            viewPager.setMultiScreen(ratio);
        }
    }

    @Override
    public void setAutoMeasureHeight(boolean status) {
        viewPager.setAutoMeasureHeight(status);
    }

    @Override
    public void setItemRatio(double ratio) {
        viewPager.setItemRatio(ratio);
    }

    @Override
    public void setItemMargin(int left, int top, int right, int bottom) {
        viewPager.setItemMargin(left, top, right, bottom);
    }

    @Override
    public void setScrollMargin(int left, int right) {
        viewPager.setPadding(left, 0, right, 0);
    }

    /**
     * delegate viewpager
     */

    public void setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
    }

    public void setOffscreenPageLimit(int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    public PagerAdapter getAdapter() {
        return viewPager.getAdapter() == null ? null : ((UltraViewPagerAdapter) viewPager.getAdapter()).getAdapter();
    }

    public PagerAdapter getWrapAdapter() {
        return viewPager.getAdapter();
    }

    public void refresh() {
        if (viewPager.getAdapter() != null) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (pagerIndicator == null) {
            //avoid registering the same listener twice
            viewPager.removeOnPageChangeListener(listener);
            viewPager.addOnPageChangeListener(listener);
        } else {
            pagerIndicator.setPageChangeListener(listener);
        }
    }

    public void setCurrentItem(int item) {
        viewPager.setCurrentItem(item);
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        viewPager.setCurrentItem(item, smoothScroll);
    }

    public int getCurrentItem() {
        return viewPager.getCurrentItem();
    }

    public int getNextItem() {
        return viewPager.getNextItem();
    }

    public void setPageTransformer(boolean reverseDrawingOrder, ViewPager.PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    private void constrainTo(Point size, Point maxSize) {
        if (maxSize.x >= 0) {
            if (size.x > maxSize.x) {
                size.x = maxSize.x;
            }
        }
        if (maxSize.y >= 0) {
            if (size.y > maxSize.y) {
                size.y = maxSize.y;
            }
        }
    }

    private void startTimer() {
        if (timer == null || viewPager == null || !timer.isStopped) {
            return;
        }
        timer.listener = mTimerHandlerListener;
        timer.removeCallbacksAndMessages(null);
        timer.tick(0);
        timer.isStopped = false;
    }

    private void stopTimer() {
        if (timer == null || viewPager == null || timer.isStopped) {
            return;
        }
        timer.removeCallbacksAndMessages(null);
        timer.listener = null;
        timer.isStopped = true;
    }

    @Override
    public void setInfiniteRatio(int infiniteRatio) {
        if (viewPager.getAdapter() != null
                && viewPager.getAdapter() instanceof UltraViewPagerAdapter) {
            ((UltraViewPagerAdapter) viewPager.getAdapter()).setInfiniteRatio(infiniteRatio);
        }
    }

}
