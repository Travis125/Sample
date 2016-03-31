package com.sunnybear.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.okhttp.Request;
import com.sunnybear.library.dispatch.DispatchActivity;
import com.sunnybear.library.model.network.CacheType;
import com.sunnybear.library.model.network.callback.SimpleFastJsonSerializableCallback;
import com.sunnybear.library.model.network.request.FormEncodingRequestBuilder;
import com.sunnybear.library.model.network.request.RequestMethod;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.util.eventbus.Subcriber;
import com.sunnybear.library.util.task.SuperTask;

/**
 * Created by sunnybear on 16/1/29.
 */
public class IndexActivity extends DispatchActivity<IndexViewBinder> {
    private Request request;

    @Override
    protected IndexViewBinder getViewBinder(Context context) {
        return new IndexViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        request = FormEncodingRequestBuilder.newInstance()
                .addParam("a", "上海市")
                .build(RequestMethod.GET, "http://gc.ditu.aliyun.com/geocoding");
//        mViewBinder.setImageUrl("http://i1.mopimg.cn/img/tt/2016-01/916/2016012510130832.jpg790x600.jpg");

        SuperTask.with(this)
                .assign(new SuperTask.TaskDescription<String>() {
                    @Override
                    public String onBackground() {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = "Hello Android!!!";
                        SuperTask.post(message);
                        return "Hello Android!!!";
                    }
                })
                .handle(new SuperTask.MessageListener() {
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what) {
                            case 1:
                                Logger.d((String) msg.obj);
                                break;
                        }
                    }
                })
                .finish(new SuperTask.FinishListener<String>() {
                    @Override
                    public void onFinish(@Nullable String result) {
                        mViewBinder.setText(result);
                    }
                }).execute();
    }

    @Override
    protected void dispatchModelOnStart() {

    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    public void getCity() {
        networkRequest(request, CacheType.NETWORK, new SimpleFastJsonSerializableCallback<City>(loading) {
            @Override
            public void onSuccess(String url, City result) {
                Logger.d("网络请求:" + result.toString());
//                tvText.setText("网络请求:" + result.toString());
                mViewBinder.setText("网络请求:" + result.toString());
            }

            @Override
            public void onCacheSuccess(String url, City result) {
                Logger.d("缓存请求:" + result.toString());
//                tvText.setText("缓存请求:" + result.toString());
                mViewBinder.setText("缓存请求:" + result.toString());
            }

            @Override
            public void onFailure(String url, int statusCode, String msg) {
                Logger.e("请求失败," + msg);
            }
        });
    }

    @Subcriber(tag = EVENT_HOME_CLICK)
    public void eventHome(String msg) {
        Logger.d(msg);
    }

    public String[] getOptions() {
        return new String[]{"Java", "Android", "iOS", "C#", "C/C++", "Ruby", "PHP"};
    }
}
