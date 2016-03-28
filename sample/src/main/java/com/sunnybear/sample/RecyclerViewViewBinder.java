package com.sunnybear.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.view.recycler.BasicRecyclerView;
import com.sunnybear.library.view.recycler.BasicViewHolder;
import com.sunnybear.library.view.recycler.adapter.BasicAdapter;
import com.sunnybear.library.view.recycler.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;

/**
 * Created by sunnybear on 16/1/29.
 */
public class RecyclerViewViewBinder extends ViewBinder<RecyclerViewActivity> {
    @Bind(R.id.rv_content)
    BasicRecyclerView rv_content;
    private BasicAdapter<String, BasicViewHolder> adapter;

    public RecyclerViewViewBinder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    public void onBindView(Bundle args) {
//        String string = args.getString("string");
//        Logger.d(string);
    }

    @Override
    public void onViewCreatedFinish() {
        adapter = new BasicAdapter<String, BasicViewHolder>(mContext, null) {
            private final int TYPE_TEXT = 1;
            private final int TYPE_IMAGE = 2;

            @Override
            public int getItemViewType(int position) {
                if (position % 2 == 0)
                    return TYPE_IMAGE;
                return TYPE_TEXT;
            }

            @Override
            public int getLayoutId(int viewType) {
                switch (viewType) {
                    case TYPE_TEXT:
                        return R.layout.activity_recycler_view_text_item;
                    case TYPE_IMAGE:
                        return R.layout.activity_recycler_view_image_item;
                }
                return -1;
            }

            @Override
            public BasicViewHolder getViewHolder(View itemView, int viewType) {
                switch (viewType) {
                    case TYPE_TEXT:
                        return new StringViewHolder(itemView);
                    case TYPE_IMAGE:
                        return new ImageViewHolder(itemView);
                }
                return null;
            }
        };
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
