package com.sunnybear.library.dispatch;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;

/**
 * 绑定View实例
 * Created by sunnybear on 16/1/29.
 */
public abstract class ViewBinder<AC extends DispatchActivity> implements Presenter {
    protected AC mContext;

    public ViewBinder(Context context) {
        mContext = (AC) context;
    }

    @Override
    public void onBindView(Bundle args) {

    }

    @Override
    public void addListener() {

    }

    /**
     * 跳转Activity
     *
     * @param targetClass 目标Activity类型
     * @param args        传递参数
     */
    protected void startActivity(Class<? extends Activity> targetClass, Bundle args) {
        mContext.startActivity(targetClass, args);
    }

    /**
     * 跳转Activity
     *
     * @param targetClass 目标Activity类型
     */
    protected void startActivity(Class<? extends Activity> targetClass) {
        startActivity(targetClass, null);
    }

    /**
     * 跳转Service
     *
     * @param targetClass 目标Activity类型
     * @param args        传递参数
     */
    protected void startService(Class<? extends Service> targetClass, Bundle args) {
        mContext.startService(targetClass, args);
    }

    /**
     * 跳转Service
     *
     * @param targetClass 目标Activity类型
     */
    protected void startService(Class<? extends Service> targetClass) {
        startService(targetClass, null);
    }
}
