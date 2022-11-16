package com.example.juiexample.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.juiexample.utils.ViewUtils;

public class TitleDialog extends Dialog {

    private Context context;
    private LinearLayout containtView;
    private TextView titleView;
    private TextView closeView;
    private View view;

    public TitleDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        this.context = context;
        setContentView(createContentView());
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private View createContentView() {
        LinearLayout rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setBackgroundColor(Color.GRAY);

        LinearLayout topLayout = new LinearLayout(context);
        topLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.setBackgroundColor(Color.WHITE);
        topLayout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams leftParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        leftParams.weight = 1;
        leftParams.height = 1;
        View leftView = new View(context);
        topLayout.addView(leftView, leftParams);

        titleView = ViewUtils.createTextView(context);
        titleView.setLines(1);
        titleView.getPaint().setFakeBoldText(true);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.weight = 2;
        topLayout.addView(titleView, titleParams);

        closeView = ViewUtils.createTextView(context);
        closeView.setText("close");
        closeView.setTextSize(15);
        LinearLayout.LayoutParams rightParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT);
        rightParams.weight = 1;
        topLayout.addView(closeView, rightParams);

        LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        topParams.height = 160;
        rootView.addView(topLayout, topParams);

        containtView = new LinearLayout(context);
        containtView.setPadding(10, 10, 10, 10);
        rootView.addView(containtView);

        return rootView;
    }

    public TitleDialog setView(View view) {
        this.view = view;
        containtView.removeAllViews();
        containtView.addView(view);
        return this;
    }

    public TitleDialog setTitle(String title) {
        titleView.setText(title);
        return this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (view != null && view.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            viewGroup.removeView(view);
        }
    }
}
