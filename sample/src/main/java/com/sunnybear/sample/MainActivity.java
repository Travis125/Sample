package com.sunnybear.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.sunnybear.library.controller.BasicFragmentActivity;
import com.sunnybear.library.model.network.CacheType;
import com.sunnybear.library.model.network.callback.SimpleFastJsonSerializableCallback;
import com.sunnybear.library.model.network.request.FormEncodingRequestBuilder;
import com.sunnybear.library.model.network.request.RequestMethod;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.view.image.ImageLoaderView;
import com.sunnybear.library.view.image.processor.BlurProcessor;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BasicFragmentActivity implements View.OnClickListener {
    @Bind(R.id.tv_text)
    TextView tvText;
    @Bind(R.id.iv_image)
    ImageLoaderView ivImage;

    private Request request;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onViewCreatedFinish(Bundle saveInstanceState) {
        request = FormEncodingRequestBuilder.newInstance()
                .addParam("a", "上海市")
                .build(RequestMethod.GET, "http://gc.ditu.aliyun.com/geocoding");

        ivImage.addProcessor(new BlurProcessor(100))
                .setImageURL("http://i1.mopimg.cn/img/tt/2016-01/1092/20160125101310688.jpg790x600.jpg");
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @OnClick(R.id.btn_execute)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                networkRequest(request, CacheType.CACHE, new SimpleFastJsonSerializableCallback<City>(loading) {
                    @Override
                    public void onSuccess(String url, City result) {
                        Logger.d("网络请求:" + result.toString());
                        tvText.setText("网络请求:" + result.toString());
                    }

                    @Override
                    public void onCacheSuccess(String url, City result) {
                        Logger.d("缓存请求:" + result.toString());
                        tvText.setText("缓存请求:" + result.toString());
                    }

                    @Override
                    public void onFailure(String url, int statusCode, String msg) {
                        Logger.e("请求失败," + msg);
                    }
                });
                break;
        }
    }
}
