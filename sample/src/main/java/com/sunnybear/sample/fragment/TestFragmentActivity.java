package com.sunnybear.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.sunnybear.library.dispatch.DispatchActivity;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.sample.R;

import butterknife.Bind;

/**
 * Created by sunnybear on 16/2/1.
 */
public class TestFragmentActivity extends DispatchActivity<TestFragmentActivity.TestFragmentViewBinder> {

    @Override
    protected TestFragmentViewBinder getViewBinder(Context context) {
        return new TestFragmentViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        setAnim(R.anim.next_in, R.anim.next_out, R.anim.quit_in, R.anim.quit_out);
        addFragment(FirstFragment.class);
    }

    @Override
    protected void dispatchModelOnStart() {

    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    /**
     *
     */
    public class TestFragmentViewBinder extends ViewBinder<TestFragmentActivity> {
        @Bind(R.id.fragment_container)
        FrameLayout fragment_container;

        public TestFragmentViewBinder(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.activity_fragment;
        }

        @Override
        public void onViewCreatedFinish() {

        }
    }
}
