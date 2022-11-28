package com.example.juiexample.common.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.juiexample.utils.DrawableUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomBottomSheetDialog extends BottomSheetDialog {

    protected Context context;
    private CustomDialogView dialogView;
    private View view;

    public CustomBottomSheetDialog(@NonNull Context context) {
        super(context, android.R.style.Theme_Panel);
        this.context = context;
        dialogView = new CustomDialogView(context);
        dialogView.setBackground(DrawableUtils.createRoundDrawable(0xFF102b6a));
        dialogView.setRightListener(v -> dismiss());
        setContentView(dialogView);
    }

    public CustomBottomSheetDialog setView(View view) {
        this.view = view;
        dialogView.setView(view);
        return this;
    }

    public CustomBottomSheetDialog setTitle(String title) {
        dialogView.setTitle(title);
        return this;
    }

    public CustomBottomSheetDialog setRightTitle(String title) {
        dialogView.setRightTitle(title);
        return this;
    }

    public CustomBottomSheetDialog setRightListener(View.OnClickListener listener) {
        dialogView.setRightListener(listener);
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
