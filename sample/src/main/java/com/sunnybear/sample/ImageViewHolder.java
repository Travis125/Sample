package com.sunnybear.sample;

import android.view.View;

import com.sunnybear.library.view.image.ImageLoaderView;
import com.sunnybear.library.view.recycler.BasicViewHolder;

import butterknife.Bind;

/**
 * Created by chenkai.gu on 2016/3/28.
 */
public class ImageViewHolder extends BasicViewHolder<String> {
    @Bind(R.id.ilv_image)
    ImageLoaderView ilv_image;

    public ImageViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindItem(String s, int position) {
        ilv_image.setImageURL(s);
    }
}
