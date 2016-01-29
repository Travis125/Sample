package com.sunnybear.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sunnybear.library.dispatch.DispatchActivity;

/**
 * Created by sunnybear on 16/1/29.
 */
public class NextActivity extends DispatchActivity<NextViewBinder> {
    @Override
    protected NextViewBinder getViewBinder(Context context) {
        return new NextViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void dispatchModelOnStart() {

    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }
}
