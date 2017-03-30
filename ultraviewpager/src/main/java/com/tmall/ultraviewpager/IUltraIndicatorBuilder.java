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

/**
 * Created by mikeafc on 15/11/25.
 */
public interface IUltraIndicatorBuilder {
    /**
     * Set focused color for indicator.
     * @param focusColor
     * @return
     */
    IUltraIndicatorBuilder setFocusColor(int focusColor);

    /**
     * Set normal color for indicator.
     * @param normalColor
     * @return
     */
    IUltraIndicatorBuilder setNormalColor(int normalColor);

    /**
     * Set stroke color for indicator.
     * @param strokeColor
     * @return
     */
    IUltraIndicatorBuilder setStrokeColor(int strokeColor);

    /**
     * Set stroke width for indicator.
     * @param strokeWidth
     * @return
     */
    IUltraIndicatorBuilder setStrokeWidth(int strokeWidth);

    /**
     * Set spacing between indicator item ，the default value is item's height.
     * @param indicatorPadding
     * @return
     */
    IUltraIndicatorBuilder setIndicatorPadding(int indicatorPadding);

    /**
     * Set the corner radius of the indicator item.
     * @param radius
     * @return
     */
    IUltraIndicatorBuilder setRadius(int radius);

    /**
     * Sets the orientation of the layout.
     * @param orientation
     * @return
     */
    IUltraIndicatorBuilder setOrientation(UltraViewPager.Orientation orientation);

    /**
     * Set the location at which the indicator should appear on the screen.
     *
     * @param gravity android.view.Gravity
     * @return
     */
    IUltraIndicatorBuilder setGravity(int gravity);

    /**
     * Set focused resource ID for indicator.
     * @param focusResId
     * @return
     */
    IUltraIndicatorBuilder setFocusResId(int focusResId);

    /**
     * Set normal resource ID for indicator.
     * @param normalResId
     * @return
     */
    IUltraIndicatorBuilder setNormalResId(int normalResId);

    /**
     * Set focused icon for indicator.
     * @param bitmap
     * @return
     */
    IUltraIndicatorBuilder setFocusIcon(Bitmap bitmap);

    /**
     * Set normal icon for indicator.
     * @param bitmap
     * @return
     */
    IUltraIndicatorBuilder setNormalIcon(Bitmap bitmap);

    /**
     * Set margins for indicator.
     * @param left   the left margin in pixels
     * @param top    the top margin in pixels
     * @param right  the right margin in pixels
     * @param bottom the bottom margin in pixels
     * @return
     */
    IUltraIndicatorBuilder setMargin(int left, int top, int right, int bottom);

    /**
     * Combine all of the options that have been set and return a new IUltraIndicatorBuilder object.
     */
    void build();
}
