package com.sunnybear.library.dispatch;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.sunnybear.library.BasicApplication;
import com.sunnybear.library.R;
import com.sunnybear.library.controller.ActivityManager;
import com.sunnybear.library.model.network.OkHttpRequestHelper;
import com.sunnybear.library.model.network.callback.RequestCallback;
import com.sunnybear.library.util.KeyboardUtils;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.util.StringUtils;
import com.sunnybear.library.util.ToastUtils;
import com.sunnybear.library.util.eventbus.EventBusHelper;
import com.sunnybear.library.view.LoadingHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * 基础FragmentActivity,主管模组分发
 * Created by sunnybear on 16/1/29.
 */
public abstract class DispatchActivity<VB extends ViewModelBridge> extends AppCompatActivity implements Dispatch {
    protected static final String EVENT_HOME_CLICK = "home_click";//点击Home键的EventBus标签

    private Context mContext;
    protected BasicApplication mApplication;
    protected VB mViewBinder;
    protected LoadingHUD loading;
    private Bundle args;
    //Home键广播接受器
    private HomeBroadcastReceiver mBroadcastReceiver = new HomeBroadcastReceiver();

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinder = getViewBinder(this);
        int layoutId = mViewBinder.getLayoutId();
        if (layoutId == 0)
            throw new RuntimeException("找不到Layout资源,Activity初始化失败");
        setContentView(layoutId);
        //声明ButterKnife
        ButterKnife.bind(mViewBinder, this);

        mContext = this;
        mApplication = (BasicApplication) getApplication();
        args = getIntent().getExtras();

        mViewBinder.onBindView(args != null ? args : new Bundle());
        mViewBinder.onViewCreatedFinish();
        mViewBinder.addListener();

        this.loading = LoadingHUD.getInstance(this);
        loading.setSpinnerType(LoadingHUD.FADED_ROUND_SPINNER);
        //添加当前Activity到管理堆栈
        ActivityManager.getInstance().addActivity(this);
        EventBusHelper.register(this);//注册EventBus
        //监听Home键
        registerReceiver(mBroadcastReceiver, Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //分发model到Presenter
        dispatchModel(savedInstanceState);
    }

    @Override
    protected final void onStart() {
        super.onStart();
        dispatchModelOnStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        loading.dismiss();
        //Activity停止时取消所有的请求
        String[] urls = getRequestUrls();
        for (String url : urls) {
            OkHttpRequestHelper.newInstance().cancelRequest(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusHelper.unregister(this);//反注册EvenBus
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 注册广播接收器
     *
     * @param receiver 广播接收器
     * @param actions  广播类型
     */
    protected void registerReceiver(BroadcastReceiver receiver, String... actions) {
        IntentFilter filter = new IntentFilter();
        for (String action : actions) {
            filter.addAction(action);
        }
        super.registerReceiver(receiver, filter);
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
     * 添加fragment
     *
     * @param targetClass 目标fragmentClass
     * @param args        传递参数
     */
    protected void addFragment(Class<? extends Fragment> targetClass, Bundle args) {
        String fragmentName = targetClass.getName();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, Fragment.instantiate(mContext, fragmentName, args), fragmentName).commit();
    }

    /**
     * 添加fragment
     *
     * @param targetClass 目标fragmentClass
     */
    protected void addFragment(Class<? extends Fragment> targetClass) {
        addFragment(targetClass, null);
    }

    /**
     * 跳转Activity
     *
     * @param targetClass 目标Activity类型
     * @param args        传递参数
     */
    protected void startActivity(Class<? extends Activity> targetClass, Bundle args) {
        Intent intent = new Intent(mContext, targetClass);
        if (args != null)
            intent.putExtras(args);
        super.startActivity(intent);
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
        Intent intent = new Intent(mContext, targetClass);
        if (args != null)
            intent.putExtras(args);
        super.startService(intent);
    }

    /**
     * 跳转Service
     *
     * @param targetClass 目标Activity类型
     */
    protected void startService(Class<? extends Service> targetClass) {
        startService(targetClass, null);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev != null && ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isShouldHideInput(view, ev))
                KeyboardUtils.closeKeyboard(mContext, view);
            return super.dispatchTouchEvent(ev);
        }
        //必不可少,否则所有的组件都不会有TouchEvent事件了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    /**
     * 是否隐藏软键盘
     *
     * @param view  对应View
     * @param event 事件
     */
    private boolean isShouldHideInput(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前位置
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    private long exitTime = 0;

    /**
     * 双击退出app
     *
     * @param exit 退出间隔时间(毫秒数)
     */
    protected void exit(long exit) {
        if (System.currentTimeMillis() - exitTime > exit) {
            ToastUtils.showToastLong(mContext, "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            ActivityManager.getInstance().killProcess(mContext.getApplicationContext());
        }
    }

    /**
     * 双击退出app
     */
    protected void exit() {
        exit(2000);
    }

    /**
     * 监听Home键广播接收器
     */
    private static class HomeBroadcastReceiver extends BroadcastReceiver {
        private String SYSTEM_REASON = "reason";
        private String SYSTEM_HOME_KEY = "homekey";
        private String SYSTEM_HOME_KEY_LONG = "recentapp";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (StringUtils.equals(action, Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (StringUtils.equals(reason, SYSTEM_HOME_KEY))//表示点击了Home键,程序到后台
                    EventBusHelper.post("点击Home,程序退到后台", EVENT_HOME_CLICK);
                else if (StringUtils.equals(reason, SYSTEM_HOME_KEY_LONG))//表示长按Home键,显示最近使用的程序列表
                    Logger.i("长按Home键, 显示最近使用的程序列表");
            }
        }
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
