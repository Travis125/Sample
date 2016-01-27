package com.sunnybear.library.model.network.callback;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunnybear.library.util.JsonUtils;
import com.sunnybear.library.util.Logger;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * FastJson解析json(反射方法获取泛型类型)
 * Created by guchenkai on 2016/1/19.
 */
abstract class FastJsonSerializableCallback<T extends Serializable> extends JSONObjectCallback {
    private Class<? extends Serializable> clazz;

    public FastJsonSerializableCallback() {
        this.clazz = getGenericClass();
    }

    /**
     * 获取泛型类型
     *
     * @return 泛型类型
     */
    private Class<? extends Serializable> getGenericClass() {
        Type genType = this.getClass().getGenericSuperclass();
        Type generic = ((ParameterizedType) genType).getActualTypeArguments()[0];
        if (!(generic instanceof Class))//泛型为array类型
            try {
                Field mArgs = generic.getClass().getDeclaredField("args");
                mArgs.setAccessible(true);
                Object o = mArgs.get(generic);

                Field mTypes = o.getClass().getDeclaredField("types");
                mTypes.setAccessible(true);
                ArrayList list = (ArrayList) mTypes.get(o);

                Object o1 = list.get(0);
                Field mRawType = o1.getClass().getDeclaredField("rawType");
                mRawType.setAccessible(true);
                return (Class<? extends Serializable>) mRawType.get(o1);
            } catch (Exception e) {
                Logger.e("获取泛型类型错误.", e);
                return null;
            }
        else//泛型为object类型
            return (Class<? extends Serializable>) generic;
    }

    /**
     * 网络请求成功
     *
     * @param url    网络地址
     * @param result 请求结果
     */
    @Override
    public final void onSuccess(String url, JSONObject result) {
        processData(url, result, true);
    }

    /**
     * 缓存请求成功
     *
     * @param url    网络地址
     * @param result 请求结果
     */
    @Override
    public final void onCacheSuccess(String url, JSONObject result) {
        processData(url, result, false);
    }

    /**
     * 处理数据
     *
     * @param url       url
     * @param result    请求返回结果
     * @param isNetwork 是否是网络请求
     */
    private void processData(String url, JSONObject result, boolean isNetwork) {
        String data = result.toJSONString();
        JsonUtils.JsonType type = JsonUtils.getJSONType(result.toJSONString());
        if (clazz == null) return;//获取泛型类型错误
        switch (type) {
            case JSON_TYPE_OBJECT:
                if (isNetwork)
                    onSuccess(url, (T) JSON.parseObject(data, clazz));
                else
                    onCacheSuccess(url, (T) JSON.parseObject(data, clazz));
                break;
            case JSON_TYPE_ARRAY:
                if (isNetwork)
                    onSuccess(url, (T) JSON.parseArray(data, clazz));
                else
                    onCacheSuccess(url, (T) JSON.parseArray(data, clazz));
                break;
            case JSON_TYPE_ERROR:
                onFailure(url, -200, "data数据返回错误");
                Logger.e(JSONObjectCallback.TAG, "result=" + result.toJSONString());
                break;
        }
    }

    /**
     * 网络请求成功回调
     *
     * @param url    网络地址
     * @param result 请求结果
     */

    public abstract void onSuccess(String url, T result);

    /**
     * 缓存请求成功回调
     *
     * @param url    网络地址
     * @param result 请求结果
     */
    public abstract void onCacheSuccess(String url, T result);
}
