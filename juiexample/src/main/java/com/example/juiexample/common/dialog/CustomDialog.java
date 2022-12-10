package com.example.juiexample.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {

    protected Context context;
    private CustomDialogView dialogView;
    private View view;

    public CustomDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Light_NoTitleBar);
        this.context = context;
        dialogView = new CustomDialogView(context);
        dialogView.setRightListener(v -> dismiss());
        dialogView.setBackgroundColor(0xFF13223F);
        dialogView.findViewById(CustomDialogView.TOP_LAYOUT_ID).setBackgroundColor(0x7F426ab3);
        setContentView(dialogView);
    }

    public CustomDialog setView(View view) {
        this.view = view;
        dialogView.setView(view);
        return this;
    }

    public CustomDialog setTitle(String title) {
        dialogView.setTitle(title);
        return this;
    }

    public CustomDialog setRightTitle(String title) {
        dialogView.setRightTitle(title);
        return this;
    }

    public CustomDialog setRightListener(View.OnClickListener listener) {
        dialogView.setRightListener(listener);
        return this;
    }

    public CustomDialog setRightVisibility(int visibility) {
        dialogView.setRightVisibility(visibility);
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
