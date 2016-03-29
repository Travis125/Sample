package com.sunnybear.sample;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureFrameLayout;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.model.BundleHelper;
import com.sunnybear.library.util.SDCardUtils;
import com.sunnybear.library.view.image.ImageLoaderView;
import com.sunnybear.player.VideoPlayerActivity;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sunnybear on 16/1/29.
 */
public class IndexViewBinder extends ViewBinder<IndexActivity> implements View.OnClickListener {
    @Bind(R.id.tv_text)
    TextView tv_text;
    @Bind(R.id.iv_image)
    ImageLoaderView iv_image;
    @Bind(R.id.fl_gesture)
    GestureFrameLayout fl_gesture;

    public IndexViewBinder(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreatedFinish() {
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
    }

    public void setImageUrl(String url) {
        iv_image.setImageURL(url);
    }

    public void setText(String text) {
        tv_text.setText(text);
    }

    @OnClick({R.id.btn_execute, R.id.btn_player, R.id.btn_start})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_execute:
                mDispatch.getCity();
                break;
            case R.id.btn_player:
                startActivity(VideoPlayerActivity.class,
                        BundleHelper.builder().putString(VideoPlayerActivity.VIDEO_URL,
                                SDCardUtils.getSDCardPath() + File.separator + "test.mp4").build());
                break;
            case R.id.btn_start:
                startActivity(RecyclerViewActivity.class,
                        BundleHelper.builder().putString("string", "产品傻逼").build());
                break;
        }
    }
}
