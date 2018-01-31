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
import android.util.SparseIntArray;

/**
 * Created by mikeafc on 15/11/25.
 */
public class TimerHandler extends Handler {

    public interface TimerHandlerListener {
        int getNextItem();
        void callBack();
    }

    SparseIntArray specialInterval;
    long interval;
    boolean isStopped = true;
    TimerHandlerListener listener;

    static final int MSG_TIMER_ID = 87108;

    public TimerHandler(TimerHandlerListener listener, long interval) {
        this.listener = listener;
        this.interval = interval;
    }

    @Override
    public void handleMessage(Message msg) {
        if (MSG_TIMER_ID == msg.what) {
            if (listener != null) {
                int nextIndex = listener.getNextItem();
                listener.callBack();
                tick(nextIndex);
            }
        }
    }

    public void tick(int index) {
        sendEmptyMessageDelayed(TimerHandler.MSG_TIMER_ID, getNextInterval(index));
    }

    private long getNextInterval(int index) {
        long next = interval;
        if (specialInterval != null) {
            long has = specialInterval.get(index, -1);
            if (has > 0) {
                next = has;
            }
        }
        return next;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }

    public void setListener(TimerHandlerListener listener) {
        this.listener = listener;
    }

    public void setSpecialInterval(SparseIntArray specialInterval) {
        this.specialInterval = specialInterval;
    }
}
