package com.sunnybear.library;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.okhttp.OkHttpClient;
import com.sunnybear.library.model.network.OkHttpManager;
import com.sunnybear.library.model.network.interceptor.NetworkInterceptor;
import com.sunnybear.library.model.network.interceptor.ResponseInfoInterceptor;
import com.sunnybear.library.util.CrashHandler;
import com.sunnybear.library.util.DiskFileCacheHelper;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.util.hawk.Hawk;
import com.sunnybear.library.util.hawk.LogLevel;
import com.sunnybear.library.view.image.ImagePipelineConfigFactory;

import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.dao.query.QueryBuilder;

/**
 * 基础Application
 * Created by guchenkai on 2015/11/19.
 */
public abstract class BasicApplication extends Application {
    private static Context mContext;
    private OkHttpClient mOkHttpClient;//OkHttpClient
    private static int maxAge;//网络缓存最大时间

    private DiskFileCacheHelper mDiskFileCacheHelper;//磁盘文件缓存器

    public static boolean isDebug;
    public static String sdCardPath;//SdCard路径
    public static Map<String, String> emojis;//表情包映射

    public static Map<String, String> getEmojis() {
        return emojis;
    }

    public static void setEmojis(Map<String, String> emojis) {
        BasicApplication.emojis = emojis;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        //sdCard缓存路径
        sdCardPath = getSdCardPath();
        //ButterKnife的Debug模式
        ButterKnife.setDebug(isDebug());
        //偏好设置文件初始化
        Hawk.init(getApplicationContext(), getPackageName(), isDebug() ? LogLevel.FULL : LogLevel.NONE);
        //日志输出
        Logger.init(getLogTag()).hideThreadInfo().setLogLevel(isDebug() ? Logger.LogLevel.FULL : Logger.LogLevel.NONE);
        //OkHttp初始化
        mOkHttpClient = OkHttpManager.getInstance(getNetworkCacheDirectoryPath(), getNetworkCacheSize())
                .addInterceptor(new NetworkInterceptor())
                .addInterceptor(new ResponseInfoInterceptor()).build();
        //Fresco初始化
        Fresco.initialize(getApplicationContext(),
                ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(getApplicationContext(), mOkHttpClient));
        //网络缓存最大时间
        maxAge = getNetworkCacheMaxAgeTime();
        //磁盘文件缓存器
        mDiskFileCacheHelper = DiskFileCacheHelper.get(getApplicationContext(), getLogTag());
        //数据库调试
        QueryBuilder.LOG_SQL = isDebug();
        QueryBuilder.LOG_VALUES = isDebug();
        //app_id配置
        isDebug = isDebug();
        //捕捉系统崩溃异常
        CrashHandler.getInstance().init(getApplicationContext(), new CrashHandler.OncrashHandler() {
            @Override
            public void onCrashHandler(Throwable ex) {
                onCrash(ex);
            }
        });
    }

    public static BasicApplication getInstance() {
        return (BasicApplication) mContext;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }

    public DiskFileCacheHelper getDiskFileCacheHelper() {
        return mDiskFileCacheHelper;
    }

    /**
     * debug模式
     *
     * @return 是否开启
     */
    protected abstract boolean isDebug();

    /**
     * 设置调试日志标签名
     *
     * @return 调试日志标签名
     */
    protected abstract String getLogTag();

    /**
     * 设置sdCard路径
     *
     * @return sdCard路径
     */
    protected abstract String getSdCardPath();

    /**
     * 网络缓存文件夹路径
     *
     * @return 缓存文件夹路径
     */
    protected abstract String getNetworkCacheDirectoryPath();

    /**
     * 网络缓存文件大小
     *
     * @return 缓存文件大小
     */
    protected abstract int getNetworkCacheSize();

    /**
     * 网络缓存最大时间
     *
     * @return 缓存最大时间
     */
    protected abstract int getNetworkCacheMaxAgeTime();

    /**
     * 异常信息处理
     *
     * @param ex 异常信息
     */
    protected abstract void onCrash(Throwable ex);
}
