package com.example.sheep.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.common.TLog;
import com.example.common.utils.AppUtils;
import com.example.juiexample.event.EventHandler;
import com.example.juiexample.utils.DrawableUtils;
import com.example.sheep.model.GameDef;

import java.util.ArrayList;
import java.util.List;

public class BucketView extends RelativeLayout {

    private static final String TAG = "BucketView";

    private final Size gridSize;
    private final SparseIntArray typeMap = new SparseIntArray();

    public BucketView(Context context, Size size) {
        super(context);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.TRANSPARENT);
        gradientDrawable.setCornerRadius(5);
        gradientDrawable.setStroke(5, Color.BLUE);
        setBackground(gradientDrawable);
        gridSize = size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gridSize.getWidth() * 7 + 10, gridSize.getHeight() + 10);
        params.topMargin = AppUtils.dip2px(20);
        setLayoutParams(params);
    }

    public BucketView addItemView(GameGridItemView itemView) {
        int count = getItemCount();
        itemView.setPoint(new Point(count * gridSize.getWidth() + 5, 5));
        addView(itemView);
        int type = itemView.getGridType();
        typeMap.put(type, typeMap.get(type) + 1);
        if (typeMap.get(type) == 3) {
            int viewCount = getItemCount();
            List<View> views = new ArrayList<>();
            for (int i = 0; i < viewCount; i++) {
                GameGridItemView view = (GameGridItemView) getChildAt(i);
                TLog.d(TAG, "type"+ view.getGridType());
                if (view.getGridType() == type) {
                    views.add(view);
                }
            }
            for (int i = 0; i < views.size(); i++) {
                removeView(views.get(i));
                typeMap.put(type, typeMap.get(type) - 1);
            }
            EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SCORE_UPDATE, null);
        }
        if (getItemCount() == 7) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("pass", false);
            EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_OVER, bundle);
            return this;
        }
        int itemCount  = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            GameGridItemView view = (GameGridItemView) getChildAt(i);
            view.setPoint(new Point(i * gridSize.getWidth() + 5, 5));
        }
        return this;
    }

    public int getItemCount() {
        return getChildCount();
    }
}
