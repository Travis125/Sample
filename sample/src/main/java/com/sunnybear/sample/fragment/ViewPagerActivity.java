package com.sunnybear.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunnybear.library.dispatch.DispatchActivity;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.model.BundleHelper;
import com.sunnybear.library.view.viewpager.LazyViewPager;
import com.sunnybear.sample.R;

import butterknife.Bind;

/**
 * Created by guchenkai on 2016/2/2.
 */
public class ViewPagerActivity extends DispatchActivity<ViewPagerActivity.ViewPagerViewBinder> {

    @Override
    protected ViewPagerViewBinder getViewBinder(Context context) {
        return new ViewPagerViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = Fragment.instantiate(mContext, FirstFragment.class.getName());
                        break;
                    case 1:
                        fragment = Fragment.instantiate(mContext, SecondFragment.class.getName(),
                                BundleHelper.builder().putString("second", "第二个Fragment").build());
                        break;
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        mViewBinder.vp_pager.setAdapter(adapter);
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
    public class ViewPagerViewBinder extends ViewBinder<ViewPagerActivity> {
        @Bind(R.id.vp_pager)
        LazyViewPager vp_pager;

        public ViewPagerViewBinder(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.activity_viewpager;
        }

        @Override
        public void onViewCreatedFinish() {

        }
    }
}
