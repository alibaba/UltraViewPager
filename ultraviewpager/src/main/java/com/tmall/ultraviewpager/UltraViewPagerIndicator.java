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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

/**
 * Created by mikeafc on 15/11/25.
 */
public class UltraViewPagerIndicator extends View implements ViewPager.OnPageChangeListener, IUltraIndicatorBuilder {
    interface UltraViewPagerIndicatorListener {
        void build();
    }

    private UltraViewPagerView viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int scrollState;

    //attr for custom
    private int radius;
    private int indicatorPadding;
    private boolean animateIndicator;
    private int gravity;
    private UltraViewPager.Orientation orientation = UltraViewPager.Orientation.HORIZONTAL;

    private int marginLeft;
    private int marginTop;
    private int marginRight;
    private int marginBottom;
    //for circle
    private int focusColor;
    private int normalColor;
    //for custom icon
    private Bitmap focusBitmap;
    private Bitmap normalBitmap;

    //paint
    private Paint paintStroke;
    private Paint paintFill;

    float pageOffset;
    float defaultRadius;

    //default
    private static final int DEFAULT_RADIUS = 3;

    private UltraViewPagerIndicatorListener indicatorBuildListener;

    public UltraViewPagerIndicator(Context context) {
        super(context);
        init();
    }

    public UltraViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UltraViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFill.setStyle(Paint.Style.FILL);
        defaultRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, getResources().getDisplayMetrics());
    }

    public void setViewPager(UltraViewPagerView viewPager) {
        this.viewPager = viewPager;
        this.viewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (viewPager == null || viewPager.getAdapter() == null)
            return;

        final int count = ((UltraViewPagerAdapter) viewPager.getAdapter()).getRealCount();
        if (count == 0)
            return;

        int longSize;
        int shortSize;

        int longPaddingBefore;
        int longPaddingAfter;
        int shortPaddingBefore;
        int shortPaddingAfter;
        if (orientation == UltraViewPager.Orientation.HORIZONTAL) {
            longSize = viewPager.getWidth();
            shortSize = viewPager.getHeight();
            longPaddingBefore = getPaddingLeft() + marginLeft;
            longPaddingAfter = getPaddingRight() + marginRight;
            shortPaddingBefore = getPaddingTop() + marginTop;
            shortPaddingAfter = (int) paintStroke.getStrokeWidth() + getPaddingBottom() + marginBottom;
        } else {
            longSize = viewPager.getHeight();
            shortSize = viewPager.getWidth();
            longPaddingBefore = getPaddingTop() + marginTop;
            longPaddingAfter = (int) paintStroke.getStrokeWidth() + getPaddingBottom() + marginBottom;
            shortPaddingBefore = getPaddingLeft() + marginLeft;
            shortPaddingAfter = getPaddingRight() + marginRight;
        }

        final float itemWidth = getItemWidth();
        final int widthRatio = isDrawResIndicator() ? 1 : 2; //bitmap resource X1 : circle  X2
        if (indicatorPadding == 0) {
            indicatorPadding = (int) itemWidth;
        }

        float shortOffset = shortPaddingBefore;
        float longOffset = longPaddingBefore;

        final float indicatorLength = (count - 1) * (itemWidth * widthRatio + indicatorPadding);

        final int horizontalGravityMask = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        final int verticalGravityMask = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        switch (horizontalGravityMask) {
            case Gravity.CENTER_HORIZONTAL:
                longOffset = (longSize - longPaddingBefore - longPaddingAfter - indicatorLength) / 2.0f;
                break;
            case Gravity.RIGHT:
                if (orientation == UltraViewPager.Orientation.HORIZONTAL) {
                    longOffset = longSize - longPaddingAfter - indicatorLength - itemWidth;
                }
                if (orientation == UltraViewPager.Orientation.VERTICAL) {
                    shortOffset = shortSize - shortPaddingAfter - itemWidth;
                }
                break;
            case Gravity.LEFT:
                longOffset += itemWidth;
            default:
                break;
        }

        switch (verticalGravityMask) {
            case Gravity.CENTER_VERTICAL:
                shortOffset = (shortSize - shortPaddingAfter - shortPaddingBefore - itemWidth) / 2;
                break;
            case Gravity.BOTTOM:
                if (orientation == UltraViewPager.Orientation.HORIZONTAL) {
                    shortOffset = shortSize - shortPaddingAfter - getItemHeight();
                }
                if (orientation == UltraViewPager.Orientation.VERTICAL) {
                    longOffset = longSize - longPaddingAfter - indicatorLength;
                }
                break;
            case Gravity.TOP:
                shortOffset += itemWidth;
            default:
                break;
        }

        if (horizontalGravityMask == Gravity.CENTER_HORIZONTAL && verticalGravityMask == Gravity.CENTER_VERTICAL) {
            shortOffset = (shortSize - shortPaddingAfter - shortPaddingBefore - itemWidth) / 2;
        }

        float dX;
        float dY;

        float pageFillRadius = radius;
        if (paintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= paintStroke.getStrokeWidth() / 2.0f; //TODO may not/2
        }

        //Draw stroked circles
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * (itemWidth * widthRatio + indicatorPadding));

            if (orientation == UltraViewPager.Orientation.HORIZONTAL) {
                dX = drawLong;
                dY = shortOffset;
            } else {
                dX = shortOffset;
                dY = drawLong;
            }

            if (isDrawResIndicator()) {
                if (iLoop == viewPager.getCurrentItem())
                    continue;
                canvas.drawBitmap(normalBitmap, dX, dY, paintFill);
            } else {
                // Only paint fill if not completely transparent
                if (paintFill.getAlpha() > 0) {
                    paintFill.setColor(normalColor);
                    canvas.drawCircle(dX, dY, pageFillRadius, paintFill);
                }

                // Only paint stroke if a stroke width was non-zero
                if (pageFillRadius != radius) {
                    canvas.drawCircle(dX, dY, radius, paintStroke);
                }
            }
        }

        //Draw the filled circle according to the current scroll
        float cx = (viewPager.getCurrentItem()) * (itemWidth * widthRatio + indicatorPadding);
        if (animateIndicator)
            cx += pageOffset * itemWidth;
        if (orientation == UltraViewPager.Orientation.HORIZONTAL) {
            dX = longOffset + cx;
            dY = shortOffset;
        } else {
            dX = shortOffset;
            dY = longOffset + cx;
        }

        if (isDrawResIndicator()) {
            canvas.drawBitmap(focusBitmap, dX, dY, paintStroke);
        } else {
            paintFill.setColor(focusColor);
            canvas.drawCircle(dX, dY, radius, paintFill);
        }
    }

    private boolean isDrawResIndicator() {
        return focusBitmap != null && normalBitmap != null;
    }

    private float getItemWidth() {
        if (isDrawResIndicator()) {
            return Math.max(focusBitmap.getWidth(), normalBitmap.getWidth());
        }
        return radius == 0 ? defaultRadius : radius;
    }

    private float getItemHeight() {
        if (isDrawResIndicator()) {
            return Math.max(focusBitmap.getHeight(), normalBitmap.getHeight());
        }
        return radius == 0 ? defaultRadius : radius;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        pageOffset = positionOffset;
        invalidate();

        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            invalidate();
        }

        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        scrollState = state;

        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public IUltraIndicatorBuilder setOrientation(UltraViewPager.Orientation orien) {
        this.orientation = orien;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setRadius(int radius) {
        this.radius = radius;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setIndicatorPadding(int indicatorPadding) {
        this.indicatorPadding = indicatorPadding;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setFocusColor(int focusColor) {
        this.focusColor = focusColor;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setStrokeColor(int strokeColor) {
        paintStroke.setColor(strokeColor);
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setStrokeWidth(int strokeWidth) {
        paintStroke.setStrokeWidth(strokeWidth);
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setFocusResId(int focusResId) {
        try {
            focusBitmap = BitmapFactory.decodeResource(getResources(), focusResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setNormalResId(int normalResId) {
        try {
            normalBitmap = BitmapFactory.decodeResource(getResources(), normalResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setFocusIcon(Bitmap bitmap) {
        focusBitmap = bitmap;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setNormalIcon(Bitmap bitmap) {
        normalBitmap = bitmap;
        return this;
    }

    @Override
    public IUltraIndicatorBuilder setMargin(int left, int top, int right, int bottom) {
        marginLeft = left;
        marginTop = top;
        marginRight = right;
        marginBottom = bottom;
        return this;
    }

    @Override
    public void build() {
        if (indicatorBuildListener != null) {
            indicatorBuildListener.build();
        }
    }

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public void setIndicatorBuildListener(UltraViewPagerIndicatorListener listener) {
        this.indicatorBuildListener = listener;
    }
}
