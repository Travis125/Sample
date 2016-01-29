package com.sunnybear.sample;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.sunnybear.library.controller.eventbus.Subcriber;
import com.sunnybear.library.dispatch.DispatchFragmentActivity;
import com.sunnybear.library.model.network.CacheType;
import com.sunnybear.library.model.network.callback.SimpleFastJsonSerializableCallback;
import com.sunnybear.library.model.network.request.FormEncodingRequestBuilder;
import com.sunnybear.library.model.network.request.RequestMethod;
import com.sunnybear.library.util.Logger;

/**
 * Created by sunnybear on 16/1/29.
 */
public class IndexActivity extends DispatchFragmentActivity<IndexViewBinder> {
    private Request request;

    @Override
    protected IndexViewBinder getViewBinder(Context context) {
        return new IndexViewBinder(context);
    }

    @Override
    protected void dispatchModel() {
        request = FormEncodingRequestBuilder.newInstance()
                .addParam("a", "上海市")
                .build(RequestMethod.GET, "http://gc.ditu.aliyun.com/geocoding");
        mViewBinder.setImageUrl("http://i1.mopimg.cn/img/tt/2016-01/916/2016012510130832.jpg790x600.jpg");
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
}
