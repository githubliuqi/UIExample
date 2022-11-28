package com.example.bugs;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.example.bugs.leak.AnonymousInnerLeakItem;
import com.example.common.utils.AppUtils;
import com.example.juiexample.common.dialog.CustomDialog;

public class BugsDialog extends CustomDialog {

    public BugsDialog(@NonNull Context context) {
        super(context);
        setTitle("Bugs");

        NestedScrollView nestedScrollView = new NestedScrollView(context);
        LinearLayout rootView = new LinearLayout(context);
        rootView.setOrientation(LinearLayout.VERTICAL);
        int pad = AppUtils.dip2px(10);
        rootView.setPadding(pad, pad, pad, pad);
        rootView.addView(new AnonymousInnerLeakItem(context));
        nestedScrollView.addView(rootView);
        setView(nestedScrollView);
        // 调整高度
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) nestedScrollView.getLayoutParams();
//        params.height = 300;
    }


}
