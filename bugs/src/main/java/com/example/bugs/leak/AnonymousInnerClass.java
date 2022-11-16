package com.example.bugs.leak;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

/**
 * 匿名内部类泄漏
 */
public class AnonymousInnerClass {

    TextView textView;

    private Context context;

    public AnonymousInnerClass(Context context) {
        this.context = context;
        textView = new TextView(context);
    }

    private void test() {
        SpannableStringBuilder builder = new SpannableStringBuilder("span");
        ClickableSpan span = new ClickableSpan() {
            @Override
            public void onClick(View view) {

            }
        };
        builder.setSpan(span, 0, 4,  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
