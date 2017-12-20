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

import android.graphics.Bitmap;
import android.util.SparseIntArray;

/**
 * Created by mikeafc on 15/11/30.
 */
interface IUltraViewPagerFeature {
    /**
     * Constructs a indicator with no options. this indicator support set-Method in chained mode.
     * meanwhile focusColor and normalColor are necessary,or the indicator won't be show.
     */
    IUltraIndicatorBuilder initIndicator();

    /**
     * Set options for indicator
     *
     * @param focusColor    defines the color when indicator is focused.
     * @param normalColor   defines the color when indicator is in the default state (not focused).
     * @param radiusInPixel defines the radius of indicator item.
     * @param gravity       specifies how to align the indicator. for example, using Gravity.BOTTOM | Gravity.RIGHT
     */
    IUltraIndicatorBuilder initIndicator(int focusColor, int normalColor, int radiusInPixel, int gravity);

    /**
     * Set options for indicator
     *
     * @param focusColor    defines the color when indicator is focused.
     * @param normalColor   defines the color when indicator is in the default state (not focused).
     * @param strokeColor   stroke color
     * @param strokeWidth   stroke width
     * @param radiusInPixel the radius of indicator item.
     * @param gravity       specifies how to align the indicator. for example, using Gravity.BOTTOM | Gravity.RIGHT
     */
    IUltraIndicatorBuilder initIndicator(int focusColor, int normalColor, int strokeColor, int strokeWidth, int radiusInPixel, int gravity);

    /**
     * Set options for indicator
     *
     * @param focusResId  defines the resource id when indicator is focused.
     * @param normalResId defines the resource id  when indicator is in the default state (not focused).
     * @param gravity     specifies how to align the indicator. for example, using Gravity.BOTTOM | Gravity.RIGHT
     */
    IUltraIndicatorBuilder initIndicator(int focusResId, int normalResId, int gravity);

    /**
     * @param focusBitmap  defines the bitmap when indicator is focused
     * @param normalBitmap defines the bitmap when indicator is in the default state (not focused).
     * @param gravity      specifies how to align the indicator. for example, using Gravity.BOTTOM | Gravity.RIGHT
     * @return
     */
    IUltraIndicatorBuilder initIndicator(Bitmap focusBitmap, Bitmap normalBitmap, int gravity);

    /**
     * Remove indicator
     */
    void disableIndicator();

    /**
     * Enable auto-scroll mode
     *
     * @param intervalInMillis The interval time to scroll in milliseconds.
     */
    void setAutoScroll(int intervalInMillis);

    /**
     * Enable auto-scroll mode with special interval times
     * @param intervalInMillis The default time to scroll
     * @param intervalArray The special interval to scroll, in responding to each frame
     */
    void setAutoScroll(int intervalInMillis, SparseIntArray intervalArray);

    /**
     * Disable auto-scroll mode
     */
    void disableAutoScroll();

    /**
     * Set an infinite loop
     *
     * @param enable enable or disable
     */
    void setInfiniteLoop(boolean enable);

    /**
     * Supply a maximum width for this ViewPager.
     *
     * @param width width
     */
    void setMaxWidth(int width);

    /**
     * Supply a maximum height for this ViewPager.
     *
     * @param height height
     */
    void setMaxHeight(int height);

    /**
     * Set the aspect ratio for UltraViewPager.
     *
     * @param ratio
     */
    void setRatio(float ratio);

    /**
     * Set scroll mode for UltraViewPager.
     *
     * @param scrollMode UltraViewPager.ScrollMode.HORIZONTAL or UltraViewPager.ScrollMode.VERTICAL
     */
    void setScrollMode(UltraViewPager.ScrollMode scrollMode);

    /**
     * Disable scroll direction. the default value is ScrollDirection.NONE
     *
     * @param direction NONE, BACKWARD, FORWARD
     */
    void disableScrollDirection(UltraViewPager.ScrollDirection direction);

    /**
     * Scroll to the last page, and return to the first page when the last page is reached.
     */
    boolean scrollLastPage();

    /**
     * Scroll to the next page, and return to the first page when the last page is reached.
     */
    boolean scrollNextPage();

    /**
     * Set multi-screen mode , the aspect ratio of PageViewer should less than or equal to 1.0f
     */
    void setMultiScreen(float ratio);

    /**
     * Adjust the height of the ViewPager to the height of child automatically.
     */
    void setAutoMeasureHeight(boolean status);

    /**
     * Adjust the height of child item view with aspect ratio.
     *
     * @param ratio aspect ratio
     */
    void setItemRatio(double ratio);

    /**
     * Set the gap between two pages in pixel
     *
     * @param pixel
     */
    void setHGap(int pixel);

    /**
     * Set item margin
     *
     * @param left   the left margin in pixels
     * @param top    the top margin in pixels
     * @param right  the right margin in pixels
     * @param bottom the bottom margin in pixels
     */
    void setItemMargin(int left, int top, int right, int bottom);

    /**
     * Set margins for this ViewPager
     *
     * @param left  the left margin in pixels
     * @param right the right margin in pixels
     */
    void setScrollMargin(int left, int right);

    /**
     * The items.size() would be scale to item.size()*infiniteRatio in fact
     *
     * @param infiniteRatio
     */
    void setInfiniteRatio(int infiniteRatio);
}
