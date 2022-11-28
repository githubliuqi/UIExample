package com.example.demo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.example.juiexample.common.IconButton;
import com.example.juiexample.common.TitleButton;
import com.example.juiexample.common.dialog.CustomDialog;

public class TestButton extends TitleButton implements View.OnClickListener {

    public TestButton(Context context) {
        this(context, null);
    }

    public TestButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("TestButton");
        setButtonText("TestButton");
    }

    @Override
    public void onClick(View v) {
        eventHandler.sendEvent("TestButton", null);
        CustomDialog dialog = new CustomDialog(context);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        IconButton iconButton1 = new IconButton(context);
        iconButton1.setBackgroundColor(Color.RED);
        ll.addView(iconButton1);
        IconButton iconButton2 = new IconButton(context);
        iconButton2.setBackgroundColor(Color.GREEN);
        ll.addView(iconButton2);
        IconButton iconButton3 = new IconButton(context);
        iconButton3.setBackgroundColor(Color.BLUE);
        ll.addView(iconButton3);
        dialog.setView(ll).setTitle("Test");
        dialog.show();
    }
}
