package com.sunnybear.library.controller;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.sunnybear.library.BasicApplication;
import com.sunnybear.library.R;
import com.sunnybear.library.controller.intent.FragmentIntent;
import com.sunnybear.library.model.network.OkHttpRequestHelper;
import com.sunnybear.library.model.network.callback.RequestCallback;
import com.sunnybear.library.util.DiskFileCacheHelper;
import com.sunnybear.library.util.eventbus.EventBusHelper;
import com.sunnybear.library.view.LoadingHUD;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 基础Fragment
 * Created by guchenkai on 2015/11/19.
 */
public abstract class BasicFragment<App extends BasicApplication> extends Fragment {
    private View mFragmentView = null;
    protected BasicFragmentActivity mActivity;

    //fragment管理器
    protected FragmentManager mFragmentManager;
    protected Bundle args;//传递的参数值

    protected App mApp;
    private OkHttpClient mOkHttpClient;
    protected LoadingHUD loading;//加载框

    protected DiskFileCacheHelper mDiskFileCacheHelper;//磁盘文件缓存器

    private Fragment currentFragment;//当前Fragment
    private Fragment targetFragment;//目标Fragment

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    protected abstract int getLayoutId();

    /**
     * 布局创建完成回调
     *
     * @param savedInstanceState savedInstanceState
     */
    public abstract void onViewCreatedFinish(Bundle savedInstanceState);

    /**
     * 收集本Activity请求时的url
     *
     * @return url
     */
    protected abstract String[] getRequestUrls();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BasicFragmentActivity) activity;
        mApp = (App) mActivity.getApplication();
        mOkHttpClient = mApp.getOkHttpClient();
        this.loading = mActivity.loading;
        mDiskFileCacheHelper = mApp.getDiskFileCacheHelper();

        mFragmentManager = mActivity.getSupportFragmentManager();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        EventBusHelper.register(this);//注册EventBus
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("找不到Layout资源,Fragment初始化失败!");
        mFragmentView = inflater.inflate(layoutId, container, false);
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        ButterKnife.bind(this, mFragmentView);
        return mFragmentView;
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewCreatedFinish(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        loading.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        loading.dismiss();
        //Fragment停止时取消所有请求
        String[] urls = getRequestUrls();
        for (String url : urls) {
            OkHttpRequestHelper.newInstance().cancelRequest(url);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mFragmentView != null)
            ((ViewGroup) mFragmentView.getParent()).removeView(mFragmentView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);//反注册EventBus
    }

    /**
     * 返回
     */
    protected void onBackPressed() {
//        List<Fragment> fragments = mFragmentManager.getFragments();
//        int size = fragments.size();
//        if (size > 1) {
//            mFragmentManager.popBackStackImmediate();
//            mFragmentManager.beginTransaction().remove(fragments.get(size - 1)).commit();
//        } else {
//            ActivityManager.getInstance().finishCurrentActivity();
//        }
        mActivity.onBackPressed();
    }

    /**
     * 网络请求
     *
     * @param request      request主体
     * @param cacheType    缓存策略
     * @param callback     请求回调(建议使用SimpleFastJsonCallback)
     * @param interceptors 网络拦截器组
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback, List<Interceptor> interceptors) {
        mActivity.networkRequest(request, cacheType, callback, interceptors);
    }

    /**
     * 网络请求
     *
     * @param request     request主体
     * @param cacheType   缓存策略
     * @param callback    请求回调(建议使用SimpleFastJsonCallback)
     * @param interceptor 网络拦截器
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback, Interceptor interceptor) {
        mActivity.networkRequest(request, cacheType, callback, interceptor);
    }

    /**
     * 网络请求
     *
     * @param request   request主体
     * @param cacheType 缓存策略
     * @param callback  请求回调(建议使用SimpleFastJsonCallback)
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback) {
        mActivity.networkRequest(request, cacheType, callback);
    }

    /**
     * 网络请求
     *
     * @param request  request主体
     * @param callback 请求回调(建议使用SimpleFastJsonCallback)
     */
    protected void networkRequest(Request request, RequestCallback callback) {
        mActivity.networkRequest(request, -1, callback);
    }

    /**
     * 缓存数据
     *
     * @param url    网络地址
     * @param result 数据源
     * @param <T>    数据类型
     */
    public <T extends Serializable> void cacheData(String url, T result) {
        mDiskFileCacheHelper.put(url, result);
    }


    /**
     * 启动Fragment
     *
     * @param intent Fragment意图
     */
    private void startFragment(FragmentIntent intent) {
        currentFragment = intent.getCurrentFragment();
        Class<? extends Fragment> targetFragmentClazz = intent.getTargetFragmentClazz();
        Bundle args = intent.getExtras();
        if (args != null)
            targetFragment = Fragment.instantiate(mActivity, targetFragmentClazz.getName(), args);
        else
            targetFragment = Fragment.instantiate(mActivity, targetFragmentClazz.getName());
        switchFragment(currentFragment, targetFragment);
    }

    /**
     * 跳转Fragment
     *
     * @param targetClass 目标Fragment
     * @param args        传递参数
     */
    protected void startFragment(Class<? extends Fragment> targetClass, Bundle args) {
        FragmentIntent fragmentIntent = new FragmentIntent(this, targetClass, args);
        startFragment(fragmentIntent);
    }

    /**
     * 跳转Fragment
     *
     * @param targetClass 目标Fragment
     */
    protected void startFragment(Class<? extends Fragment> targetClass) {
        FragmentIntent fragmentIntent = new FragmentIntent(this, targetClass, null);
        startFragment(fragmentIntent);
    }

    /**
     * 跳转目标Activity
     *
     * @param targetClass 目标Activity类型
     */
    protected void startActivity(Class<? extends Activity> targetClass) {
        mActivity.startActivity(targetClass);
    }

    /**
     * 跳转目标Activity(传递参数)
     *
     * @param targetClass 目标Activity类型
     * @param args        传递参数
     */
    public void startActivity(Class<? extends Activity> targetClass, Bundle args) {
        mActivity.startActivity(targetClass, args);
    }

    /**
     * 隐式跳转目标Activity
     *
     * @param action 隐式动作
     */
    public void startActivity(String action) {
        mActivity.startActivity(action);
    }

    /**
     * 隐式跳转目标Activity
     *
     * @param action 隐式动作
     */
    public void startActivity(String action, Bundle args) {
        mActivity.startActivity(action, args);
    }

    /**
     * 启动目标Service
     *
     * @param targetClass 目标Service类型
     * @param args        传递参数
     */
    public void startService(Class<? extends Service> targetClass, Bundle args) {
        mActivity.startActivity(targetClass, args);
    }

    /**
     * 启动目标Service
     *
     * @param targetClass 目标Service类型
     */
    public void startService(Class<? extends Service> targetClass) {
        mActivity.startService(targetClass);
    }

    /**
     * 隐式跳转目标Service
     *
     * @param action 隐式动作
     */
    public void startService(String action) {
        mActivity.startService(action);
    }

    /**
     * 隐式跳转目标Service
     *
     * @param action 隐式动作
     */
    protected void startService(String action, Bundle args) {
        mActivity.startService(action, args);
    }

    /**
     * 切换Fragment
     *
     * @param currentFragment 当前fragment
     * @param targetFragment  目标fragment
     */
    private void switchFragment(Fragment currentFragment, Fragment targetFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        String tag = targetFragment.getClass().getName();
        if (currentFragment != null)
            transaction.replace(R.id.fragment_container, targetFragment, targetFragment.getClass().getName()).addToBackStack(tag).commit();
        else
            transaction.add(R.id.fragment_container, targetFragment, targetFragment.getClass().getName()).commit();
    }
}
