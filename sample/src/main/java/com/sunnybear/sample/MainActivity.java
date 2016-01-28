package com.sunnybear.sample;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.squareup.okhttp.Request;
import com.sunnybear.library.controller.BasicFragmentActivity;
import com.sunnybear.library.model.network.CacheType;
import com.sunnybear.library.model.network.callback.SimpleFastJsonSerializableCallback;
import com.sunnybear.library.model.network.request.FormEncodingRequestBuilder;
import com.sunnybear.library.model.network.request.RequestMethod;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.util.SDCardUtils;
import com.sunnybear.library.view.image.ImageLoaderView;
import com.sunnybear.player.VideoPlayerActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BasicFragmentActivity implements View.OnClickListener {
    @Bind(R.id.tv_text)
    TextView tvText;
    @Bind(R.id.iv_image)
    ImageLoaderView ivImage;
    @Bind(R.id.fl_gesture)
    GestureFrameLayout fl_gesture;//缩放控件

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

        fl_gesture.getController().getSettings()
                .setMaxZoom(5f)//最大放大等级
                .setPanEnabled(true)//是否启用了平移
                .setZoomEnabled(true)//是否启用了缩放
                .setDoubleTapEnabled(true)//是否启用通过双击缩放
                .setRotationEnabled(false)//是否启用了旋转的姿态
                .setRestrictBounds(true)//是否应该保存在图像转换边界
                .setOverscrollDistance(0f, 0f)//Overscroll距离像素。用户将能够“滚动”这个距离。不能小于0
                .setOverzoomFactor(2f)//Overzoom因素。用户将能够“变焦”这个因素。不能< 1
                .setFillViewport(false)//如果设置为true小图像缩放以适合整个窗口(或整个运动区域如果是集),即使这需要缩放级别高于最大缩放级别。
                .setFitMethod(Settings.Fit.INSIDE)//设置显示窗口区域内图像拟合方法
                .setGravity(Gravity.CENTER);//图像引力窗口区域内
        ivImage/*.addProcessor(new BlurProcessor(10))*/
                /*.addProcessor(new WatermarkProcessor(R.mipmap.ic_launcher, WatermarkProcessor.WatermarkLocation.TOP_LEFT))*/
                .setImageURL("http://i1.mopimg.cn/img/tt/2016-01/916/2016012510130832.jpg790x600.jpg");
    }

    private void addListener() {
        fl_gesture.getController().setOnGesturesListener(new GestureController.OnGestureListener() {
            @Override
            public void onDown(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }
        });
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    @OnClick({R.id.btn_execute, R.id.btn_player})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                networkRequest(request, CacheType.NETWORK, new SimpleFastJsonSerializableCallback<City>(loading) {
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
            case R.id.btn_player:
                Bundle bundle = new Bundle();
                bundle.putString(VideoPlayerActivity.VIDEO_URL,
                        SDCardUtils.getSDCardPath() + File.separator + "test.mp4");
                startActivity(VideoPlayerActivity.class, bundle);
                break;
        }
    }
}
