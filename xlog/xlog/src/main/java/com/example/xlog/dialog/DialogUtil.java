package com.example.xlog.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class DialogUtil {

    /**
     * call before dialog.show()
     * @param dialog
     */
    public static final void fixAlertDialog(final AlertDialog dialog){
        if (null != dialog){
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog0) {
                    _fixAlertDialog(dialog);
                }
            });
        }
    }


    /**
     * reset AlertDialog`button
     * 系统的AlertDialog 在某些手机上（小米或谷歌等）UI布局存在问题。
     * 底部的按钮不居中，需要再适配。
     * @param dialog
     */
    private static final void _fixAlertDialog(AlertDialog dialog){
        if (null == dialog){
            return;
        }
        if (Build.VERSION.SDK_INT < 28){
            return;
        }
        Button[] buttons = {dialog.getButton(DialogInterface.BUTTON_NEUTRAL), dialog.getButton(DialogInterface.BUTTON_NEGATIVE), dialog.getButton(DialogInterface.BUTTON_POSITIVE) };
        List<Button> visiableButtons = new ArrayList<>(buttons.length);
        for (Button button : buttons){
            if (null != button && button.getVisibility() == View.VISIBLE){
                visiableButtons.add(button);
            }
        }
        if ( visiableButtons.isEmpty()){
            return;
        }
        int size = visiableButtons.size();
        Button button1 =  visiableButtons.get(0);
        ViewParent parent = button1.getParent();
        DisplayMetrics dm = dialog.getContext().getResources().getDisplayMetrics();
        int buttonWidth = (int) (Math.min(dm.widthPixels, dm.heightPixels) * 0.9F / size);
        buttonWidth = Math.min(dm.widthPixels/2, buttonWidth);
        if (null != parent && parent instanceof LinearLayout) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(buttonWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i<visiableButtons.size(); i++){
                visiableButtons.get(i).setLayoutParams(params);
            }
        }
    }
}
