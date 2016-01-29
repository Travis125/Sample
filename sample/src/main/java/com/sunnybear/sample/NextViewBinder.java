package com.sunnybear.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.util.eventbus.EventBusHelper;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sunnybear on 16/1/29.
 */
public class NextViewBinder extends ViewBinder<NextActivity> implements View.OnClickListener {
    @Bind(R.id.tv_text)
    TextView tv_text;

    public NextViewBinder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recycler_view_item;
    }

    @Override
    public void onBindView(Bundle args) {
        tv_text.setText(args.getString("position"));
    }

    @Override
    public void onViewCreatedFinish() {

    }

    @OnClick(R.id.tv_text)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_text:
                EventBusHelper.post("产品是个大傻逼", "next_tag");
                break;
        }
    }
}
