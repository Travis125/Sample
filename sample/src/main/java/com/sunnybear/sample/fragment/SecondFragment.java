package com.sunnybear.sample.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.sunnybear.library.dispatch.DispatchFragment;
import com.sunnybear.library.dispatch.ViewBinder;
import com.sunnybear.library.dispatch.fragmentstack.StackManager;
import com.sunnybear.library.util.Logger;
import com.sunnybear.sample.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by sunnybear on 16/2/1.
 */
public class SecondFragment extends DispatchFragment<SecondFragment.SecondFragmentViewBinder> {

    @Override
    protected SecondFragmentViewBinder getViewBinder(Context context) {
        return new SecondFragmentViewBinder(context);
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

    /**
     *
     */
    public class SecondFragmentViewBinder extends ViewBinder<SecondFragment> implements View.OnClickListener {

        @Bind(R.id.tv_text)
        TextView tv_text;

        public SecondFragmentViewBinder(Context context) {
            super(context);
        }

        @Override
        public int getLayoutId() {
            return R.layout.fragment_first;
        }

        @Override
        public void onViewCreatedFinish() {

        }

        @Override
        public void onBindView(Bundle args) {
            String text = args.getString("second");
            tv_text.setText(text);
        }

        @OnClick(R.id.btn_start)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    Logger.d("启动下一个Fragment");
                    open(FirstFragment.class, StackManager.SINGLE_TOP);
                    break;
            }
        }
    }
}
