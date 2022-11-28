package com.example.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.bugs.BugsDialog;
import com.example.juiexample.common.TitleButton;

public class BugsEntranceView extends TitleButton implements View.OnClickListener {

    public BugsEntranceView(Context context) {
        this(context, null);
    }

    public BugsEntranceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setButtonListener(this::onClick);
        setTitle("Bugs");
        setButtonText("ok");
    }

    @Override
    public void onClick(View v) {
        BugsDialog dialog = new BugsDialog(context);
        dialog.show();
    }
}
