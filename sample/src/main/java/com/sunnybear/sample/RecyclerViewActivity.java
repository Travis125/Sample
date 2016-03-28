package com.sunnybear.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.library.dispatch.DispatchActivity;
import com.sunnybear.library.util.ToastUtils;
import com.sunnybear.library.util.eventbus.Subcriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunnybear on 16/1/29.
 */
public class RecyclerViewActivity extends DispatchActivity<RecyclerViewViewBinder> {

    @Override
    protected RecyclerViewViewBinder getViewBinder(Context context) {
        return new RecyclerViewViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        List<String> strings = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            if (i % 2 == 0)
                strings.add("第" + i + "项");
            else
                strings.add("http://i1.mopimg.cn/img/tt/2016-01/916/2016012510130832.jpg790x600.jpg");
        }
        mViewBinder.setRecyclerView(strings);
    }

    @Override
    protected void dispatchModelOnStart() {

    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @Subcriber(tag = "next_tag")
    public void eventPosition(String s) {
        ToastUtils.showToastLong(this, s);
    }
}
