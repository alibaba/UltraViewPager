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

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mikeafc on 15/11/25.
 */
public class UltraViewPagerAdapter extends PagerAdapter {
    public interface UltraViewPagerCenterListener {
        void center();

        void resetPosition();
    }

    private static final int INFINITE_RATIO = 400;

    private PagerAdapter adapter;
    private boolean enableLoop;
    private float multiScrRatio = Float.NaN;
    private boolean hasCentered; //ensure that the first item is in the middle when enabling loop-mode
    private int scrWidth;
    private int infiniteRatio;
    private UltraViewPagerCenterListener centerListener;

    private SparseArray viewArray = new SparseArray();

    public UltraViewPagerAdapter(PagerAdapter adapter) {
        this.adapter = adapter;
        infiniteRatio = INFINITE_RATIO;
    }

    @Override
    public int getCount() {
        int count;
        if (enableLoop) {
            if (adapter.getCount() == 0) {
                count = 0;
            } else {
                count = adapter.getCount() * infiniteRatio;
            }
        } else {
            count = adapter.getCount();
        }
        return count;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position;
        //TODO
        if (enableLoop && adapter.getCount() != 0) {
            realPosition = position % adapter.getCount();
        }

        Object item = adapter.instantiateItem(container, realPosition);
        //TODO
        View childView = null;
        if (item instanceof View)
            childView = (View) item;
        if (item instanceof RecyclerView.ViewHolder)
            childView = ((RecyclerView.ViewHolder) item).itemView;

        int childCount = container.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = container.getChildAt(i);
            if (isViewFromObject(child, item)) {
                viewArray.put(realPosition, child);
                break;
            }
        }

        if (isEnableMultiScr()) {
            if (scrWidth == 0) {
                scrWidth = container.getResources().getDisplayMetrics().widthPixels;
            }
            RelativeLayout relativeLayout = new RelativeLayout(container.getContext());

            if (childView.getLayoutParams() != null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                        (int) (scrWidth * multiScrRatio),
                        ViewGroup.LayoutParams.MATCH_PARENT);

                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                childView.setLayoutParams(layoutParams);
            }

            container.removeView(childView);
            relativeLayout.addView(childView);

            container.addView(relativeLayout);
            return relativeLayout;
        }

        return item;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realPosition = position;

        //TODO
        if (enableLoop && adapter.getCount() != 0)
            realPosition = position % adapter.getCount();

        if (isEnableMultiScr() && object instanceof RelativeLayout) {
            View child = ((RelativeLayout) object).getChildAt(0);
            ((RelativeLayout) object).removeAllViews();
            adapter.destroyItem(container, realPosition, child);
        } else {
            adapter.destroyItem(container, realPosition, object);
        }

        viewArray.remove(realPosition);
    }

    public View getViewAtPosition(int position) {
        return (View) viewArray.get(position);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        // only need to set the center position  when the loop is enabled
        if (!hasCentered) {
            if (adapter.getCount() > 0 && getCount() > adapter.getCount()) {
                centerListener.center();
            }
        }
        hasCentered = true;
        adapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return adapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        adapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return adapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        adapter.startUpdate(container);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        int virtualPosition = position % adapter.getCount();
        return adapter.getPageTitle(virtualPosition);
    }

    @Override
    public float getPageWidth(int position) {
        return adapter.getPageWidth(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        adapter.setPrimaryItem(container, position, object);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return adapter.getItemPosition(object);
    }

    public PagerAdapter getAdapter() {
        return adapter;
    }

    public int getRealCount() {
        return adapter.getCount();
    }

    public void setEnableLoop(boolean status) {
        this.enableLoop = status;
        notifyDataSetChanged();
        if (!status) {
            centerListener.resetPosition();
        } else {
            //try {
            //    centerListener.center();
            //} catch (Exception e) {
            //
            //}
        }
    }

    public boolean isEnableLoop() {
        return enableLoop;
    }

    public void setMultiScrRatio(float ratio) {
        multiScrRatio = ratio;
    }

    public boolean isEnableMultiScr() {
        return !Float.isNaN(multiScrRatio) && multiScrRatio < 1f;
    }

    public void setCenterListener(UltraViewPagerCenterListener listener) {
        centerListener = listener;
    }

    public void setInfiniteRatio(int infiniteRatio) {
        this.infiniteRatio = infiniteRatio;
    }
}
