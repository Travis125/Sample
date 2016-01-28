package com.sunnybear.library.view.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

import butterknife.ButterKnife;

/**
 * 重新封装ViewHolder
 * Created by guchenkai on 2015/11/9.
 */
public abstract class BasicViewHolder<Item extends Serializable, VH extends BasicViewHolder> extends RecyclerView.ViewHolder {

    public BasicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * 向itemView上绑定数据
     *
     * @param holder   ViewHolder
     * @param item     item数据
     * @param position 该条数据的位置
     */
    public abstract void onBindItem(VH holder, Item item, int position);
}
