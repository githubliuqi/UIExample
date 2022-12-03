package com.example.bugs.leak;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bugs.BugsActivity;
import com.example.juiexample.common.TitleButton;
import com.example.juiexample.utils.ViewUtils;

public class AnonymousInnerLeakItem extends TitleButton implements View.OnClickListener {

    public AnonymousInnerLeakItem(Context context) {
        super(context);
        setTitle("泄漏-匿名内部类");
        setButtonListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, BugsActivity.class);
        intent.putExtra("isAnonymousInnerClass", true);
        Class s = AnonymousInnerLeakTestView.class;
        intent.putExtra("viewClass", AnonymousInnerLeakTestView.class.getName());
        context.startActivity(intent);
    }

    private static class AnonymousInnerLeakTestView extends LinearLayout {

        private TextView textView;

        public AnonymousInnerLeakTestView(Context context) {
            super(context);
            setOrientation(LinearLayout.VERTICAL);
            setGravity(Gravity.CENTER);
            textView = ViewUtils.createTextView(context);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            addView(textView);

            TextView info = ViewUtils.createTextView(context);
            info.setText("泄漏原理：\n当前Activity持有匿名Handler对象，\n导致Activity不能及时释放");
            addView(info);
        }

        public void setAnonymousInnerObject(Object object) {
            if (object == null || !(object instanceof Handler)) {
                return;
            }
            Activity activity = (Activity) getContext();
            Handler handler = (Handler) object;
            ClickableSpan span = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    handler.sendEmptyMessageDelayed(1, 10 * 1000);
                    activity.finish();
                }
            };
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append("点击后退出Activity\n");
            builder.append("多试几次易泄漏\n");
            builder.setSpan(span, 0, builder.length(),  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
        }
    }
}
