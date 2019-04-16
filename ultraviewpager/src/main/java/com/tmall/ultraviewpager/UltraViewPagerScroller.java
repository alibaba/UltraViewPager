package com.tmall.ultraviewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Jimmy Sun on 19/04/16.
 */
class UltraViewPagerScroller extends Scroller {
    private int mDuration = 1000;

    UltraViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    /**
     * Set the scroll speed.
     *
     * @param duration The default duration to scroll
     */
    void setScrollDuration(int duration) {
        mDuration = duration;
    }
}
