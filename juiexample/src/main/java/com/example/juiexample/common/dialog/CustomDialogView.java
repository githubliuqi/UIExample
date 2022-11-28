package com.example.juiexample.common.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.juiexample.utils.ViewUtils;

public class CustomDialogView extends LinearLayout {

    private static final int TITLEVIEW_ID = View.generateViewId();
    private static final int RIGHTVIEW_ID = View.generateViewId();

    private Context context;
    private LinearLayout containtView;
    private TextView titleView;
    private TextView rightView;

    public CustomDialogView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);

        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        leftParams.weight = 1;
        leftParams.height = 1;
        View leftView = new View(context);
        topLayout.addView(leftView, leftParams);

        titleView = ViewUtils.createTextView(context);
        titleView.setId(TITLEVIEW_ID);
        titleView.setLines(1);
        titleView.setTextColor(Color.WHITE);
        titleView.getPaint().setFakeBoldText(true);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.weight = 2;
        topLayout.addView(titleView, titleParams);

        rightView = ViewUtils.createTextView(context);
        rightView.setId(RIGHTVIEW_ID);
        rightView.setText("关闭");
        rightView.setTextSize(15);
        rightView.setTextColor(Color.WHITE);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        rightParams.weight = 1;
        topLayout.addView(rightView, rightParams);

        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        topParams.height = 160;
        addView(topLayout, topParams);

        containtView = new LinearLayout(context);
        containtView.setGravity(Gravity.CENTER);
        containtView.setPadding(10, 10, 10, 10);
        addView(containtView);
    }

    public CustomDialogView setView(View view) {
        containtView.removeAllViews();
        containtView.addView(view);
        return this;
    }

    public CustomDialogView setTitle(String title) {
        titleView.setText(title);
        return this;
    }

    public CustomDialogView setRightTitle(String title) {
        rightView.setText(title);
        return this;
    }

    public CustomDialogView setRightListener(OnClickListener listener) {
        rightView.setOnClickListener(listener);
        return this;
    }
}
