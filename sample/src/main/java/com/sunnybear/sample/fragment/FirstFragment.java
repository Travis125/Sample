package com.sunnybear.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sunnybear.library.dispatch.DispatchFragment;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.model.BundleHelper;
import com.sunnybear.library.util.Logger;
import com.sunnybear.sample.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sunnybear on 16/2/1.
 */
public class FirstFragment extends DispatchFragment<FirstFragment.FirstFragmentViewBinder> {

    @Override
    protected FirstFragmentViewBinder getViewBinder(Context context) {
        return new FirstFragmentViewBinder(context);
    }

    @Override
    protected void dispatchModel(@Nullable Bundle savedInstanceState) {
        setAnim(R.anim.next_in, R.anim.next_out, R.anim.quit_in, R.anim.quit_out);
        Logger.d("FirstFragment---onCreated");
    }

    @Override
    protected void dispatchModelOnStart() {
        Logger.d("FirstFragment---onStart");
    }

    @Override
    public void onNewIntent() {
        Logger.d("firstFragment重新启动");
    }

    @Override
    protected String[] getRequestUrls() {
        return new String[0];
    }

    /**
     *
     */
    public class FirstFragmentViewBinder extends ViewBinder<FirstFragment> implements View.OnClickListener {

        @Bind(R.id.tv_text)
        TextView tv_text;

        public FirstFragmentViewBinder(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.fragment_first;
        }

        @Override
        public void onViewCreatedFinish() {
            tv_text.setText("第一个fragment");
        }

        @OnClick(R.id.btn_start)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
//                    ToastUtils.showToastLong(mContext.getActivity(), "启动第二个Fragment");
                    Logger.d("启动第二个Fragment");
                    open(SecondFragment.class, BundleHelper.builder().putString("second", "第二个Fragment").build());
                    break;
            }
        }
    }
}
