package com.example.sheep.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.juiexample.common.grid.GridItemView;

public class GameGridItemView extends GridItemView {

    private final GradientDrawable gradientDrawable = new GradientDrawable();
    private int strokeWidth = 1;
    private int strokeColor = 0xAF666666;
    private int color = 0x2F666666;
    private Rect rect = new Rect();
    private int layerIndex = 0;
    private int imageResId = 0;

    private ImageView imageView;
    private View shadowView;

    public GameGridItemView(Context context) {
        super(context);
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        gradientDrawable.setColor(color);
        setBackground(gradientDrawable);

        imageView = new ImageView(context);
        RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.topMargin = 1;
        params.leftMargin = 1;
        params.rightMargin = 1;
        params.bottomMargin = 1;
        addView(imageView, params);

        shadowView = new View(context);
        shadowView.setBackgroundColor(0xAF000000);
        addView(shadowView, params);
    }

    public GameGridItemView showShadow(boolean show) {
        shadowView.setVisibility(show ? VISIBLE : GONE);
        return this;
    }

    public boolean isShadow() {
        return shadowView.getVisibility() == VISIBLE;
    }

    public GameGridItemView setColor(int color) {
        this.color = color;
        gradientDrawable.setColor(color);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        gradientDrawable.setStroke(strokeWidth, strokeColor);
        setBackground(gradientDrawable);
        return this;
    }

    public GameGridItemView setCornerRadius(int radius) {
        gradientDrawable.setCornerRadius(radius);
        setBackground(gradientDrawable);
        return this;
    }

    public GridItemView setPoint(Point point) {
        this.point.x = point.x;
        this.point.y = point.y;
        rect.left = point.x;
        rect.top = point.y;
        rect.right = rect.left + getSize().getWidth();
        rect.bottom = rect.top + getSize().getHeight();
        setLayoutParams();
        return this;
    }

    public Rect getRect() {
        return rect;
    }

    public void setLayerIndex(int index) {
        layerIndex = index;
    }

    public int getLayerIndex() {
        return layerIndex;
    }

    private void setLayoutParams() {
        Size size = getSize();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(size.getWidth(), size.getHeight());
        }
        params.leftMargin = getPoint().x;
        params.topMargin = getPoint().y;
//        TLog.d(TAG, String.format("leftMargin=%s, topMargin=%s", params.leftMargin, params.topMargin));
        setLayoutParams(params);
    }

    public void setImageResId(int resId) {
        this.imageResId = resId;
        imageView.setBackgroundResource(imageResId);
    }

    @Override
    public void copyFrom(GridItemView srcView) {
        if (srcView == null) {
            return;
        }
        GameGridItemView srcItemView = (GameGridItemView) srcView;
//        setColor(srcItemView.color);
        setImageResId(srcItemView.imageResId);
    }
}
