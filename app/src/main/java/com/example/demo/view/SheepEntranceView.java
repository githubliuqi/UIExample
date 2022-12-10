package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.juiexample.common.TitleButton;
import com.example.sheep.SheepDialog;

public class SheepEntranceView extends TitleButton implements View.OnClickListener {

    public SheepEntranceView(Context context) {
        this(context, null);
    }

    public SheepEntranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("Sheep");
    }

    @Override
    public void onClick(View v) {
        SheepDialog dialog = new SheepDialog(context);
        dialog.show();
    }
}
