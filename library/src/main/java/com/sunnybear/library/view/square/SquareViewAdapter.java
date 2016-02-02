package com.sunnybear.library.view.square;

import android.view.View;

import java.io.Serializable;

/**
 * 九宫格控件适配器
 * Created by guchenkai on 2016/2/2.
 */
public interface SquareViewAdapter<T extends Serializable> {

    int getCount();

    T getItem(int position);

    String getImageUrl(int position);

    void onItemClick(View view, int index, T item);
}
