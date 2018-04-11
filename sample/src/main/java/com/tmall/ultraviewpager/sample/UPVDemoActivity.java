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

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UPVDemoActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.list_content);

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();

        HashMap<String, String> demoList1 = new HashMap<>();
        demoList1.put("name", "Horizontal");
        demoList1.put("style", "1");
        demoList1.put("class", PagerActivity.class.getName());
        list.add(demoList1);

        HashMap<String, String> demoList2 = new HashMap<>();
        demoList2.put("name", "Vertical");
        demoList2.put("style", "2");
        demoList2.put("class", PagerActivity.class.getName());
        list.add(demoList2);

        HashMap<String, String> demoList3 = new HashMap<>();
        demoList3.put("name", "Horizontal with multi screen");
        demoList3.put("style", "3");
        demoList3.put("class", PagerActivity.class.getName());
        list.add(demoList3);

        HashMap<String, String> demoList4 = new HashMap<>();
        demoList4.put("name", "Vertical with multi screen");
        demoList4.put("style", "4");
        demoList4.put("class", PagerActivity.class.getName());
        list.add(demoList4);

        HashMap<String, String> demoList5 = new HashMap<>();
        demoList5.put("name", "ScaleTransformer in horizontal with multi screen");
        demoList5.put("style", "5");
        demoList5.put("class", PagerActivity.class.getName());
        list.add(demoList5);

        HashMap<String, String> demoList6 = new HashMap<>();
        demoList6.put("name", "DepthTransformer in horizontal with multi screen");
        demoList6.put("style", "6");
        demoList6.put("class", PagerActivity.class.getName());
        list.add(demoList6);

        ListAdapter listAdapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_1, new String[]{"name"}, new int[]{android.R.id.text1});
        setListAdapter(listAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String, String> item = (Map<String, String>) l.getItemAtPosition(position);
        String className = item.get("class");
        if (className != null) {
            Intent intent = new Intent();
            intent.putExtra("style", item.get("style"));
            intent.putExtra("name", item.get("name"));
            intent.setComponent(new ComponentName(this, className));
            startActivity(intent);
        }
    }
}
