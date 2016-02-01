package com.sunnybear.library.dispatch.fragmentstack;

import com.sunnybear.library.dispatch.DispatchFragment;

/**
 *
 * Created by sunnybear on 16/2/1.
 */
public interface HandlerFragment {
    /**
     * 显示Fragment
     *
     * @param fragment fragment实例
     */
    void show(DispatchFragment fragment);

    /**
     * 关闭fragment
     *
     * @param fragment fragment
     */
    void close(DispatchFragment fragment);
}
