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

package com.tmall.ultraviewpager.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.tmall.ultraviewpager.UltraViewPager;
import com.tmall.ultraviewpager.transformer.UltraDepthScaleTransformer;
import com.tmall.ultraviewpager.transformer.UltraScaleTransformer;


public class PagerActivity extends Activity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private UltraViewPager ultraViewPager;
    private PagerAdapter adapter;

    private Spinner indicatorStyle;
    private Spinner indicatorGravityHor;
    private Spinner indicatorGravityVer;

    private Button indicatorBuildBtn;

    private CheckBox loopCheckBox;
    private CheckBox autoScrollCheckBox;

    private int gravity_hor;
    private int gravity_ver;
    private UltraViewPager.Orientation gravity_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        int style = getIntent().getIntExtra("style", 1);
        ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);

        switch (style){
            case 1:
                setTitle("Horizontal Demo");
                break;
            case 2:
                setTitle("Vertical Demo");
                break;
            case 3:
                setTitle("MultiScreen_H Demo");
                break;
            case 4:
                setTitle("MultiScreen_V Demo");
                break;
            case 5:
                setTitle("ScaleTransformer Demo");
                break;
            case 6:
                setTitle("DepthTransformer Demo");
                break;
            default:
                break;
        }

//        defaultUltraViewPager();

        switch (style) {
            case 1:
                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                adapter = new UltraPagerAdapter(false);
                ultraViewPager.setAdapter(adapter);
                gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
                break;
            case 2:
                ultraViewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
                adapter = new UltraPagerAdapter(false);
                ultraViewPager.setAdapter(adapter);
                gravity_indicator = UltraViewPager.Orientation.VERTICAL;
                break;
            case 3:
            case 5:
            case 6:
                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
                adapter = new UltraPagerAdapter(true);
                ultraViewPager.setAdapter(adapter);
                ultraViewPager.setMultiScreen(0.6f);
                ultraViewPager.setItemRatio(1.0f);
//                ultraViewPager.setRatio(2.0f);
//                ultraViewPager.setMaxHeight(800);
                ultraViewPager.setAutoMeasureHeight(true);
                gravity_indicator = UltraViewPager.Orientation.HORIZONTAL;
                if (style == 5) {
                    ultraViewPager.setPageTransformer(false, new UltraScaleTransformer());
                }
                if (style == 6) {
                    ultraViewPager.setPageTransformer(false, new UltraDepthScaleTransformer());
                }
                break;
            case 4:
                ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.VERTICAL);
                ultraViewPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                adapter = new UltraPagerAdapter(true);
                ultraViewPager.setAdapter(adapter);
                ultraViewPager.setMultiScreen(1.0f);
                ultraViewPager.setAutoMeasureHeight(true);
                gravity_indicator = UltraViewPager.Orientation.VERTICAL;
                break;
        }

        initUI();
    }

    private void initUI() {
        indicatorStyle = (Spinner) findViewById(R.id.indicator);
        indicatorGravityHor = (Spinner) findViewById(R.id.indicator_gravity_hor);
        indicatorGravityVer = (Spinner) findViewById(R.id.indicator_gravity_ver);

        ArrayAdapter<CharSequence> indicatorAdapter = ArrayAdapter.createFromResource(this, R.array.indicator_style, android.R.layout.simple_spinner_item);
        indicatorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicatorStyle.setAdapter(indicatorAdapter);

        ArrayAdapter<CharSequence> indicatorGraHorAdapter = ArrayAdapter.createFromResource(this, R.array.indicator_gravity_hor, android.R.layout.simple_spinner_item);
        indicatorGraHorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicatorGravityHor.setAdapter(indicatorGraHorAdapter);

        ArrayAdapter<CharSequence> indicatorGraVerAdapter = ArrayAdapter.createFromResource(this, R.array.indicator_gravity_ver, android.R.layout.simple_spinner_item);
        indicatorGraVerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        indicatorGravityVer.setAdapter(indicatorGraVerAdapter);

        indicatorStyle.setOnItemSelectedListener(this);
        indicatorGravityHor.setOnItemSelectedListener(this);
        indicatorGravityVer.setOnItemSelectedListener(this);

        loopCheckBox = (CheckBox) findViewById(R.id.loop);
        loopCheckBox.setOnCheckedChangeListener(this);

        autoScrollCheckBox = (CheckBox) findViewById(R.id.autoscroll);
        autoScrollCheckBox.setOnCheckedChangeListener(this);

        indicatorBuildBtn = (Button) findViewById(R.id.indicator_build);
        indicatorBuildBtn.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (ultraViewPager.getIndicator() == null) {
            ultraViewPager.initIndicator();
            ultraViewPager.getIndicator().setOrientation(gravity_indicator);
        }
        if (parent == indicatorStyle) {
            switch (position) {
                case 0:
                    ultraViewPager.disableIndicator();
                    break;
                case 1:
                    ultraViewPager.getIndicator().setFocusResId(0).setNormalResId(0);
                    ultraViewPager.getIndicator().setFocusColor(Color.GREEN).setNormalColor(Color.WHITE)
                            .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case 2:
                    ultraViewPager.getIndicator().setFocusResId(R.mipmap.tm_biz_lifemaster_indicator_selected)
                            .setNormalResId(R.mipmap.tm_biz_lifemaster_indicator_normal);
                    break;
                case 3:
                    break;
            }
        }
        if (parent == indicatorGravityHor) {
            switch (position) {
                case 0:
                    gravity_hor = Gravity.LEFT;
                    break;
                case 1:
                    gravity_hor = Gravity.RIGHT;
                    break;
                case 2:
                    gravity_hor = Gravity.CENTER_HORIZONTAL;
                    break;
            }
            if (ultraViewPager.getIndicator() != null) {
                if (gravity_ver != 0) {
                    ultraViewPager.getIndicator().setGravity(gravity_hor | gravity_ver);
                } else {
                    ultraViewPager.getIndicator().setGravity(gravity_hor);
                }
            }
        }
        if (parent == indicatorGravityVer) {
            switch (position) {
                case 0:
                    gravity_ver = Gravity.TOP;
                    break;
                case 1:
                    gravity_ver = Gravity.BOTTOM;
                    break;
                case 2:
                    gravity_ver = Gravity.CENTER_VERTICAL;
                    break;
            }
            if (ultraViewPager.getIndicator() != null) {
                if (gravity_hor != 0) {
                    ultraViewPager.getIndicator().setGravity(gravity_hor | gravity_ver);
                } else {
                    ultraViewPager.getIndicator().setGravity(gravity_ver);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == loopCheckBox) {
            ultraViewPager.setInfiniteLoop(isChecked);
        }
        if (buttonView == autoScrollCheckBox) {
            if (isChecked)
                ultraViewPager.setAutoScroll(2000);
            else
                ultraViewPager.disableAutoScroll();
        }
    }

    @Override
    public void onClick(View v) {
        ultraViewPager.getIndicator().build();
    }

    /**
     *
     */
    private void defaultUltraViewPager(){
        UltraViewPager ultraViewPager = (UltraViewPager)findViewById(R.id.ultra_viewpager);
        ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        //initialize UltraPagerAdapter，and add child view to UltraViewPager
        PagerAdapter adapter = new UltraPagerAdapter(false);
        ultraViewPager.setAdapter(adapter);

        //initialize built-in indicator
        ultraViewPager.initIndicator();
        //set style of indicators
        ultraViewPager.getIndicator()
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setFocusColor(Color.GREEN)
                .setNormalColor(Color.WHITE)
                .setRadius((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        //set the alignment
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        //construct built-in indicator, and add it to  UltraViewPager
        ultraViewPager.getIndicator().build();

        //set an infinite loop
        ultraViewPager.setInfiniteLoop(true);
        //enable auto-scroll mode
        ultraViewPager.setAutoScroll(2000);
    }
}
