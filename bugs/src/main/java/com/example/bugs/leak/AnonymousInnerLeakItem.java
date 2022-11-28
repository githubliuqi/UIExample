package com.example.bugs.leak;

import android.content.Context;

import com.example.juiexample.common.TitleButton;

public class AnonymousInnerLeakItem extends TitleButton {

    public AnonymousInnerLeakItem(Context context) {
        super(context);
        setTitle("泄漏--匿名内部类");
    }
}
