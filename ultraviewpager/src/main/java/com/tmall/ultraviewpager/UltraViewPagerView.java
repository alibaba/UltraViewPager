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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tmall.ultraviewpager.transformer.UltraVerticalTransformer;

/**
 * Created by mikeafc on 15/11/25.
 */
public class UltraViewPagerView extends ViewPager implements UltraViewPagerAdapter.UltraViewPagerCenterListener {

    private UltraViewPagerAdapter pagerAdapter;

    //Internal state to schedule a new measurement pass.
    private boolean needsMeasurePage;

    private float multiScrRatio = Float.NaN;
    private boolean enableLoop;
    private boolean autoMeasureHeight;
    private double itemRatio = Double.NaN;
    private int constrainLength;

    private int itemMarginLeft;
    private int itemMarginTop;
    private int itemMarginRight;
    private int itemMarginBottom;

    private float ratio = Float.NaN;

    private UltraViewPager.ScrollMode scrollMode = UltraViewPager.ScrollMode.HORIZONTAL;

    public UltraViewPagerView(Context context) {
        super(context);
        init(context, null);
    }

    public UltraViewPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setClipChildren(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        onMeasurePage(widthMeasureSpec, heightMeasureSpec);
    }

    protected void onMeasurePage(int widthMeasureSpec, int heightMeasureSpec) {
        if (pagerAdapter == null) {
            return;
        }
        View child = pagerAdapter.getViewAtPosition(getCurrentItem());
        if (child == null) {
            child = getChildAt(0);
        }
        if (child == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if ((view.getPaddingLeft() != itemMarginLeft ||
                    view.getPaddingTop() != itemMarginTop ||
                    view.getPaddingRight() != itemMarginRight ||
                    view.getPaddingBottom() != itemMarginBottom)) {
                view.setPadding(itemMarginLeft, itemMarginTop, itemMarginRight, itemMarginBottom);
            }
        }

        ViewGroup.LayoutParams lp = child.getLayoutParams();
        final int childWidthSpec = getChildMeasureSpec(widthMeasureSpec, 0, lp.width);
        final int childHeightSpec = getChildMeasureSpec(heightMeasureSpec, 0, lp.height);

        int childWidth = (int) ((MeasureSpec.getSize(childWidthSpec) - getPaddingLeft() - getPaddingRight()) * pagerAdapter.getPageWidth(getCurrentItem()));
        int childHeight = MeasureSpec.getSize(childHeightSpec) - getPaddingTop() - getPaddingBottom();

        if (!needsMeasurePage || childWidth == 0 && childHeight == 0) {
            return;
        }

        if (!Double.isNaN(itemRatio)) {
            int itemHeight = (int) (childWidth / itemRatio);
            for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
                View view = getChildAt(i);
                view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
            }
        } else {
            for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
                View view = getChildAt(i);
                if (pagerAdapter.getPageWidth(getCurrentItem()) != 1) {
                    view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                } else {
                    view.measure(childWidthSpec, childHeightSpec);
                }
            }
        }

        final boolean isHorizontalScroll = scrollMode == UltraViewPager.ScrollMode.HORIZONTAL;

        childWidth = itemMarginLeft + child.getMeasuredWidth() + itemMarginRight;
        childHeight = itemMarginTop + child.getMeasuredHeight() + itemMarginBottom;

        if (!Float.isNaN(ratio)) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (getMeasuredWidth() / ratio), MeasureSpec.EXACTLY);
            setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
            for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
                View view = getChildAt(i);
                view.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        } else {
            if (autoMeasureHeight) {
                if (isHorizontalScroll) {
                    constrainLength = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                    setMeasuredDimension(getMeasuredWidth(), childHeight);
                } else {
                    constrainLength = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    setMeasuredDimension(childWidth, getMeasuredHeight());
                }

                needsMeasurePage = childHeight == itemMarginTop + itemMarginBottom;
            }
        }

        if (!pagerAdapter.isEnableMultiScr()) {
            return;
        }

        int pageLength = isHorizontalScroll ? getMeasuredWidth() : getMeasuredHeight();

        final int childLength = isHorizontalScroll ? child.getMeasuredWidth() : child.getMeasuredHeight();

        // Check that the measurement was successful
        if (childLength > 0) {
            needsMeasurePage = false;
            int difference = pageLength - childLength;
            if (getPageMargin() == 0) {
                setPageMargin(-difference);
            }
            int offscreen = (int) Math.ceil((float) pageLength / (float) childLength) + 1;
            setOffscreenPageLimit(offscreen);
            requestLayout();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Schedule a new measurement pass as the dimensions have changed
        needsMeasurePage = true;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            if (pagerAdapter == null || pagerAdapter.getAdapter() != adapter) {
                pagerAdapter = new UltraViewPagerAdapter(adapter);
                pagerAdapter.setCenterListener(this);
                pagerAdapter.setEnableLoop(enableLoop);
                pagerAdapter.setMultiScrRatio(multiScrRatio);
                needsMeasurePage = true;
                constrainLength = 0;
                super.setAdapter(pagerAdapter);
            }
        } else {
            super.setAdapter(adapter);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        if (pagerAdapter.getCount() != 0 && pagerAdapter.isEnableLoop()) {
            item = pagerAdapter.getCount() / 2 + item % pagerAdapter.getRealCount();
        }
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public int getCurrentItem() {
        if (pagerAdapter != null && pagerAdapter.getCount() != 0) {
            int position = super.getCurrentItem();
            return position % pagerAdapter.getRealCount();
        }
        return super.getCurrentItem();
    }

    public int getNextItem() {
        if (pagerAdapter.getCount() != 0) {
            int next = super.getCurrentItem() + 1;
            return next % pagerAdapter.getRealCount();
        }
        return 0;
    }

    /**
     * Set the currently selected page.
     *
     * @param item         Item index to select
     * @param smoothScroll True to smoothly scroll to the new item, false to transition immediately
     */
    public void setCurrentItemFake(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    /**
     * Get the currently selected page.
     */
    public int getCurrentItemFake() {
        return super.getCurrentItem();
    }

    public void setMultiScreen(float ratio) {
        multiScrRatio = ratio;
        if (pagerAdapter != null) {
            pagerAdapter.setMultiScrRatio(ratio);
            needsMeasurePage = true;
        }
        float pageMargin = (1 - ratio) * getResources().getDisplayMetrics().widthPixels;
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL) {
            setPageMargin((int) pageMargin);
        } else {
            setPageMargin((int) -pageMargin);
        }
    }

    public void setEnableLoop(boolean status) {
        enableLoop = status;
        if (pagerAdapter != null) {
            pagerAdapter.setEnableLoop(enableLoop);
        }
    }

    public void setItemRatio(double itemRatio) {
        this.itemRatio = itemRatio;
    }

    public void setAutoMeasureHeight(boolean autoMeasureHeight) {
        this.autoMeasureHeight = autoMeasureHeight;
    }

    public void setScrollMode(UltraViewPager.ScrollMode scrollMode) {
        this.scrollMode = scrollMode;
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL)
            setPageTransformer(false, new UltraVerticalTransformer());
    }

    public UltraViewPager.ScrollMode getScrollMode() {
        return scrollMode;
    }

    public int getConstrainLength() {
        return constrainLength;
    }

    public void setItemMargin(int left, int top, int right, int bottom) {
        itemMarginLeft = left;
        itemMarginTop = top;
        itemMarginRight = right;
        itemMarginBottom = bottom;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    @Override
    public void center() {
        setCurrentItem(0);
    }

    @Override
    public void resetPosition() {
        setCurrentItem(getCurrentItem());
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL) {
            boolean intercept = super.onInterceptTouchEvent(swapTouchEvent(ev));
            //If not intercept, touch event should not be swapped.
            swapTouchEvent(ev);
            return intercept;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (scrollMode == UltraViewPager.ScrollMode.VERTICAL)
            return super.onTouchEvent(swapTouchEvent(ev));
        return super.onTouchEvent(ev);
    }
}
