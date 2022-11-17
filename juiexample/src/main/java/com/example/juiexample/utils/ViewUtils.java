package com.example.juiexample.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class ViewUtils {

    public static final TextView createTextView(Context context) {
        TextView view = new TextView(context);
        view.setTextSize(16);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public static final Button createButton(Context context) {
        Button view = new Button(context);
        view.setTextSize(16);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public static final Switch createSwitch(Context context) {
        Switch view = new Switch(context);
        view.setGravity(Gravity.CENTER);
        return view;
    }

    public static final RadioGroup createRadioGroup(Context context) {
        RadioGroup view = new RadioGroup(context);
        return view;
    }

    public static final RadioButton createRadioButton(Context context) {
        RadioButton view = new RadioButton(context);
        view.setTextSize(14);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
