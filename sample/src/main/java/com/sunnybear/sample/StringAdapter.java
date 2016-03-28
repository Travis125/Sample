package com.sunnybear.sample;

import android.content.Context;
import android.view.View;

import com.sunnybear.library.view.recycler.adapter.BasicAdapter;

import java.util.List;

/**
 * Created by sunnybear on 16/1/29.
 */
public class StringAdapter extends BasicAdapter<String, StringViewHolder> {

    public StringAdapter(Context context, List<String> strings) {
        super(context, strings);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.activity_recycler_view_text_item;
    }

    @Override
    public StringViewHolder getViewHolder(View itemView, int viewType) {
        return new StringViewHolder(itemView);
    }
}
