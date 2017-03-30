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

/**
 * Created by mikeafc on 15/11/25.
 */
class TimerHandler extends Handler {
    interface TimerHandlerListener {
        void callBack();
    }

    long interval;
    boolean isInTouchMode;
    TimerHandlerListener listener;

    static final int MSG_TIMER_ID = 87108;

    TimerHandler(TimerHandlerListener listener, long interval) {
        this.listener = listener;
        this.interval = interval;
    }

    @Override
    public void handleMessage(Message msg) {
        if (MSG_TIMER_ID == msg.what) {
            if (listener != null && !isInTouchMode)
                listener.callBack();
            sendEmptyMessageDelayed(MSG_TIMER_ID, interval);
        }
    }
}
