package com.sunnybear.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.view.recycler.BasicRecyclerView;
import com.sunnybear.library.view.recycler.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;

/**
 * Created by sunnybear on 16/1/29.
 */
public class RecyclerViewViewBinder extends ViewBinder<RecyclerViewActivity> {
    @Bind(R.id.rv_content)
    BasicRecyclerView rv_content;
    private StringAdapter adapter;

    public RecyclerViewViewBinder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    public void onBindView(Bundle args) {
        String string = args.getString("string");
        Logger.d(string);
    }

    @Override
    public void onViewCreatedFinish() {
        adapter = new StringAdapter(mContext, null);
        rv_content.setAdapter(adapter);
    }

    @Override
    public void addListener() {
        rv_content.setOnItemClickListener(new OnItemClickListener<String>() {
            @Override
            public void onItemClick(String s, int position) {
                Bundle bundle = new Bundle();
                bundle.putString("position", s);
                Intent intent = new Intent(mContext, NextActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
//                EventBusHelper.post(s, "tag");
            }
        });
    }

    public void setRecyclerView(List<String> strings) {
        adapter.addAll(strings);
    }
}
