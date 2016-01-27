package com.sunnybear.library.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

/**
 * 图片模糊工具
 * Created by guchenkai on 2016/1/12.
 */
public class FastBlur {

    /**
     * 高斯图片模糊
     *
     * @param context   context
     * @param oldBitmap 原始图片
     * @param radius    模糊半径
     * @return 高斯模糊后的图片
     */
    public static Bitmap apply(Context context, Bitmap oldBitmap, int radius) {
        final Bitmap bitmap = oldBitmap.copy(oldBitmap.getConfig(), true);
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, oldBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        oldBitmap.recycle();
        rs.destroy();
        input.destroy();
        output.destroy();
        script.destroy();

        return bitmap;
    }

    /**
     * 模糊原始图片
     *
     * @param context   context
     * @param oldBitmap 原始图片
     * @param radius    模糊半径
     */
    public static void applyOriginal(Context context, Bitmap oldBitmap, int radius) {
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, oldBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(oldBitmap);

        rs.destroy();
        input.destroy();
        output.destroy();
        script.destroy();
    }
}
