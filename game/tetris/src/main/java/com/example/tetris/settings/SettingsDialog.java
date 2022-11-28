package com.example.tetris.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.example.common.utils.AppUtils;
import com.example.juiexample.common.dialog.CustomBottomSheetDialog;

public class SettingsDialog extends CustomBottomSheetDialog {

    public SettingsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("设置");

        NestedScrollView nestedScrollView = new NestedScrollView(context);
        LinearLayout rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        int pad = AppUtils.dip2px(10);
        rootView.setPadding(pad, pad, pad, pad);
        rootView.addView(new SpeedItemView(context));
        nestedScrollView.addView(rootView);
        setView(nestedScrollView);
        // 调整高度
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nestedScrollView.getLayoutParams();
//        params.height = 300;
    }
}
