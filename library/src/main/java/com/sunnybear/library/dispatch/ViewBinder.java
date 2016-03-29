package com.sunnybear.library.dispatch;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Bundle;

/**
 * 绑定View实例
 * Created by sunnybear on 16/1/29.
 */
public abstract class ViewBinder<D extends Dispatch> implements ViewModelBridge {
    protected D mDispatch;

    public ViewBinder(Context context) {
        mDispatch = (D) context;
        if (!(mDispatch instanceof DispatchActivity))
            throw new RuntimeException("ViewBinder中的Content必须是DispatchActivity类型");
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
        ((DispatchActivity) mDispatch).startActivity(targetClass, args);
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
        ((DispatchActivity) mDispatch).startService(targetClass, args);
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
