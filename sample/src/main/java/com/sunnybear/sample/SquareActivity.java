package com.sunnybear.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sunnybear.library.dispatch.DispatchActivity;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.view.square.SquareGridView;
import com.sunnybear.library.view.square.SquareViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 九宫格试例
 * Created by guchenkai on 2016/2/2.
 */
public class SquareActivity extends DispatchActivity<SquareActivity.SquareViewBinder> {

    @Override
    protected SquareViewBinder getViewBinder(Context context) {
        return new SquareViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        final List<String> images = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            images.add("http://i1.mopimg.cn/img/tt/2016-02/1069/20160201115414108.jpg790x600.jpg");
        }
        SquareViewAdapter<String> adapter = new SquareViewAdapter<String>() {
            @Override
            public int getCount() {
                return images.size();
            }

            @Override
            public String getItem(int position) {
                return images.get(position);
            }

            @Override
            public String getImageUrl(int position) {
                return images.get(position);
            }

            @Override
            public void onItemClick(View view, int index, String item) {

            }
        };
        mViewBinder.sv_images.setAdapter(adapter);
    }

    @Override
    protected void dispatchModelOnStart() {

    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    /**
     *
     */
    public class SquareViewBinder extends ViewBinder<SquareActivity> {
        @Bind(R.id.sv_images)
        SquareGridView sv_images;

        public SquareViewBinder(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.activity_square;
        }

        @Override
        public void onViewCreatedFinish() {

        }
    }
}
