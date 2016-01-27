package com.sunnybear.sample;

import com.sunnybear.library.BasicApplication;
import com.sunnybear.library.controller.ActivityManager;
import com.sunnybear.library.util.Logger;
import com.sunnybear.library.util.SDCardUtils;

import java.io.File;

/**
 * Created by sunnybear on 16/1/26.
 */
public class GlobalApplication extends BasicApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected boolean isDebug() {
        return true;
    }

    @Override
    protected String getLogTag() {
        return "sunnybear_library";
    }

    @Override
    protected String getSdCardPath() {
        return SDCardUtils.getSDCardPath() + File.separator + getLogTag();
    }

    @Override
    protected String getNetworkCacheDirectoryPath() {
        return sdCardPath + File.separator + "newtwork_cache";
    }

    @Override
    protected int getNetworkCacheSize() {
        return 20 * 1024 * 1024;
    }

    @Override
    protected int getNetworkCacheMaxAgeTime() {
        return 0;
    }

    @Override
    protected void onCrash(Throwable ex) {
        Logger.e("APP崩溃了,错误信息是" + ex.getMessage());
        ex.printStackTrace();
        ActivityManager.getInstance().killProcess(getApplicationContext());
    }
}
