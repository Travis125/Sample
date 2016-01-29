package com.sunnybear.library.dispatch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.sunnybear.library.BasicApplication;
import com.sunnybear.library.R;
import com.sunnybear.library.controller.intent.FragmentIntent;
import com.sunnybear.library.model.network.OkHttpRequestHelper;
import com.sunnybear.library.model.network.callback.RequestCallback;
import com.sunnybear.library.util.eventbus.EventBusHelper;
import com.sunnybear.library.view.LoadingHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 基础Fragment,主管模组分发
 * Created by sunnybear on 16/1/29.
 */
public abstract class DispatchFragment<VB extends ViewModelBridge> extends Fragment implements Dispatch {
    protected Context mContext;
    protected BasicApplication mApplication;
    protected LoadingHUD loading;
    protected VB mViewBinder;

    private Bundle args;
    private FragmentManager mFragmentManager;
    private View mFragmentView = null;

    @Override
    public final void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        FragmentActivity activity = (FragmentActivity) context;
        if (!(activity instanceof DispatchActivity))
            throw new RuntimeException("DispatchFragment必须依赖DispatchActivity");
        mApplication = (BasicApplication) activity.getApplication();
        this.loading = LoadingHUD.getInstance(mContext);
        loading.setSpinnerType(LoadingHUD.FADED_ROUND_SPINNER);
        mFragmentManager = activity.getSupportFragmentManager();
    }

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        args = getArguments();
        EventBusHelper.register(this);//注册EventBus
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewBinder = getViewBinder(mContext);
        int layoutId = mViewBinder.getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("找不到Layout资源,Fragment初始化失败");
        mFragmentView = inflater.inflate(layoutId, container, false);
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null)
            parent.removeView(mFragmentView);
        ButterKnife.bind(mViewBinder, mFragmentView);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dispatchModel(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        dispatchModelOnStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        loading.dismiss();
        //Fragment停止时取消所有的请求
        String[] urls = getRequestUrls();
        for (String url : urls) {
            OkHttpRequestHelper.newInstance().cancelRequest(url);
        }
    }

    @Override
    public final void onDestroyView() {
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
     * 网络请求
     *
     * @param request      request主体
     * @param cacheType    缓存策略
     * @param callback     请求回调
     * @param interceptors 网络拦截器组
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback, List<Interceptor> interceptors) {
        if (request == null)
            throw new NullPointerException("request为空");
        OkHttpRequestHelper helper = OkHttpRequestHelper.newInstance();
        if (cacheType != -1)
            helper.cacheType(cacheType);
        if (interceptors != null && interceptors.size() > 0)
            helper.addInterceptors(interceptors);
        helper.request(request, callback);
    }

    /**
     * 网络请求
     *
     * @param request     request主体
     * @param cacheType   缓存策略
     * @param callback    请求回调
     * @param interceptor 网络拦截器
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback, Interceptor interceptor) {
        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(interceptor);
        networkRequest(request, cacheType, callback, interceptors);
    }

    /**
     * 网络请求
     *
     * @param request   request主体
     * @param cacheType 缓存策略
     * @param callback  请求回调
     */
    protected void networkRequest(Request request, int cacheType, RequestCallback callback) {
        if (request == null)
            throw new NullPointerException("request为空");
        OkHttpRequestHelper helper = OkHttpRequestHelper.newInstance();
        if (cacheType != -1)
            helper.cacheType(cacheType);
        helper.request(request, callback);
    }

    /**
     * 网络请求
     *
     * @param request  request主体
     * @param callback 请求回调
     */
    protected void networkRequest(Request request, RequestCallback callback) {
        networkRequest(request, -1, callback);
    }

    /**
     * 启动Fragment
     *
     * @param intent Fragment意图
     */
    private void startFragment(FragmentIntent intent) {
        Fragment current = intent.getCurrentFragment();
        Class<? extends Fragment> targetFragmentClass = intent.getTargetFragmentClazz();
        Bundle args = intent.getExtras();
        Fragment target = null;
        if (args != null)
            target = Fragment.instantiate(mContext, targetFragmentClass.getName(), args);
        else
            target = Fragment.instantiate(mContext, targetFragmentClass.getName());
        switchFragment(current, target);
    }

    /**
     * 切换Fragment
     *
     * @param current 当前fragment
     * @param target  目标fragment
     */
    private void switchFragment(Fragment current, Fragment target) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        String targetName = target.getClass().getName();
        if (current != null)
            transaction.replace(R.id.fragment_container, target, targetName).addToBackStack(targetName);
        else
            transaction.add(R.id.fragment_container, target, targetName);
        transaction.commit();
    }

    /**
     * 设置Presenter实例,绑定View
     *
     * @param context 上下文
     */
    protected abstract VB getViewBinder(Context context);

    /**
     * 分发model到ViewBinder
     */
    protected abstract void dispatchModel(@Nullable Bundle savedInstanceState);

    /**
     * 分发model到ViewBinder(onStart时调用)
     */
    protected abstract void dispatchModelOnStart();

    /**
     * 收集请求url
     */
    protected abstract String[] getRequestUrls();
}
