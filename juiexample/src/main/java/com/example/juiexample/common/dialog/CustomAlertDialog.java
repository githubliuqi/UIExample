package com.example.juiexample.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.common.utils.AppUtils;
import com.example.juiexample.utils.DrawableUtils;

public class CustomAlertDialog extends Dialog {

    private Context context;
    private TextView titleView;
    private TextView messageView;

    public CustomAlertDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar);
        this.context = context;
        LinearLayout rootView = new LinearLayout(context);
        rootView.setGravity(Gravity.CENTER);
        rootView.setBackground(DrawableUtils.createRoundGradientDrawable(Color.WHITE));
        rootView.setOrientation(LinearLayout.VERTICAL);

        titleView = new TextView(context);
        titleView.setText("温馨提示");
        titleView.setTextSize(16);
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = AppUtils.dip2px(10);
        titleParams.bottomMargin = titleParams.topMargin;
        rootView.addView(titleView, titleParams);

        messageView = new TextView(context);
        messageView.setText("message");
        messageView.setPadding(10, 10, 10, 10);
        messageView.setTextSize(15);
        messageView.setTextColor(Color.BLACK);
        rootView.addView(messageView);

        Button okBtn = new Button(context);
        okBtn.setText("确定");
        okBtn.setTextSize(15);
        okBtn.setTextColor(0xFF1E90FF);
        okBtn.setBackground(null);
        okBtn.setGravity(Gravity.CENTER);
        okBtn.setOnClickListener(v -> {dismiss(); onClickOk();});
        rootView.addView(okBtn);

        int width = AppUtils.dip2px(250);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                width, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        setContentView(rootView, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(0x5F000000);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }

    public void onClickOk() {

    }
}
