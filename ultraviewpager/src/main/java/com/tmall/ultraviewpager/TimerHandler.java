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

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;

/**
 * Created by mikeafc on 15/11/25.
 */
class TimerHandler extends Handler implements ViewPager.OnPageChangeListener {

    interface TimerHandlerListener {
        void callBack();
    }

    SparseIntArray specialInterval;
    long interval;
    boolean isStopped = true;
    TimerHandlerListener listener;
    UltraViewPager mUltraViewPager;
    private int currentPosition = 0;

    static final int MSG_TIMER_ID = 87108;

    TimerHandler(UltraViewPager ultraViewPager, TimerHandlerListener listener, long interval) {
        this.mUltraViewPager = ultraViewPager;
        this.listener = listener;
        this.interval = interval;
    }

    @Override
    public void handleMessage(Message msg) {
        if (MSG_TIMER_ID == msg.what) {
            if (listener != null) {
                listener.callBack();
            }
        }
    }

    public void startTimer() {
        sendEmptyMessageDelayed(TimerHandler.MSG_TIMER_ID, getNextInterval());
    }

    private long getNextInterval() {
        long next = interval;
        if (specialInterval != null) {
            long has = specialInterval.get(currentPosition, -1);
            if (has > 0) {
                next = has;
            }
        }
        return next;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = mUltraViewPager.getCurrentItem();
        sendEmptyMessageDelayed(MSG_TIMER_ID, getNextInterval());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
