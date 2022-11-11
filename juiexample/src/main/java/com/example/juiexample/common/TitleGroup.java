package com.example.juiexample.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.example.juiexample.utils.ViewUtils;

public class TitleGroup extends TitleLayout {

    protected final RadioGroup radioGroup;

    public TitleGroup(Context context) {
        this(context, null);
    }

    public TitleGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        radioGroup = createRadioGroup();
        addView(radioGroup);
    }

    private RadioGroup createRadioGroup() {
        return ViewUtils.createRadioGroup(context);
    }

    private RadioButton createRadioButton() {
        return ViewUtils.createRadioButton(context);
    }

    @Override
    public final TitleGroup setTitle(String title) {
        return (TitleGroup) super.setTitle(title);
    }

    public final TitleGroup setData(String[] data) {
        if (data == null || data.length == 0) {
            return this;
        }
        for (int i = 0; i < data.length; i++) {
            RadioButton button = createRadioButton();
            button.setId(i);
            button.setText(data[i]);
            radioGroup.addView(button);
        }
        return this;
    }

    public final TitleGroup setGroupOrientation(int orientation) {
        radioGroup.setOrientation(orientation);
        return this;
    }
}
