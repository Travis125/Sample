package com.sunnybear.sample;

import android.view.View;
import android.widget.TextView;

import com.sunnybear.library.view.recycler.BasicViewHolder;

import butterknife.Bind;

/**
 * Created by sunnybear on 16/1/29.
 */
public class StringViewHolder extends BasicViewHolder<String> {
    @Bind(R.id.tv_text)
    TextView tv_text;

    public StringViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindItem(String s, int position) {
        tv_text.setText(s);
    }
}
