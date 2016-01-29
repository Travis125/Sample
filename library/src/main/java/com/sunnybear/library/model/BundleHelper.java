package com.sunnybear.library.model;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Bundle组合工具
 * Created by sunnybear on 16/1/29.
 */
public final class BundleHelper {
    private Bundle bundle;

    public BundleHelper() {
        bundle = new Bundle();
    }

    public static BundleHelper builder() {
        return new BundleHelper();
    }

    public BundleHelper putString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }

    public BundleHelper putInt(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleHelper putSerializable(String key, Serializable value) {
        bundle.putSerializable(key, value);
        return this;
    }

    public BundleHelper putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public Bundle build() {
        return bundle;
    }
}
