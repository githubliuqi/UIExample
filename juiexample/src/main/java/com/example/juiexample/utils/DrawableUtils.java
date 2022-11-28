package com.example.juiexample.utils;

import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

import com.example.common.utils.AppUtils;

public class DrawableUtils {

    public static Drawable createRoundDrawable(int color) {
        // 外部矩形弧度
        int r = AppUtils.dip2px(10);
        float[] outerR = new float[] { r, r, r, r, 0, 0, 0, 0};
        // 内部矩形与外部矩形的距离
        RectF inset = new RectF(100, 100, 50, 50);
        // 内部矩形弧度
        float[] innerRadii = new float[] { 20, 20, 20, 20, 20, 20, 20, 20 };

        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        //指定填充颜色
        drawable.getPaint().setColor(color);
        // 指定填充模式
        drawable.getPaint().setStyle(Paint.Style.FILL);
        return drawable;
    }

    public static Drawable createRoundGradientDrawable(int color) {
        int r = AppUtils.dip2px(10);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setCornerRadius(r);
        return gradientDrawable;
    }
}
