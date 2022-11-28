package com.example.juiexample.common;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.common.utils.AppUtils;
import com.example.juiexample.utils.ViewUtils;

public class TitleSeekbar extends TitleLayout implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBar;
    private TextView textView;

    public TitleSeekbar(Context context) {
        this(context, null);
    }

    public TitleSeekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        seekBar = new SeekBar(context);

        LayerDrawable progressDrawable = (LayerDrawable) seekBar.getProgressDrawable();
        Drawable[] outDrawables = new Drawable[progressDrawable.getNumberOfLayers()];
        for (int i = 0; i < outDrawables.length; i++) {
            Drawable drawable = progressDrawable.getDrawable(i);
            outDrawables[i] = drawable;
            switch (progressDrawable.getId(i)) {
                case android.R.id.background:// 设置进度条背景
                    break;
                case android.R.id.secondaryProgress:// 设置二级进度条
                    break;
                case android.R.id.progress:// 设置进度条
//                    ClipDrawable oidDrawable = (ClipDrawable) progressDrawable.getDrawable(i);
//                    Drawable drawable = getResources().getDrawable(R.drawable.handleview_layout_seekbar_fg);
//                    ClipDrawable proDrawable = new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
//                    proDrawable.setLevel(oidDrawable.getLevel());
//                    outDrawables[i] = proDrawable;
                    break;
                default:
                    break;
            }
        }
//        progressDrawable = new LayerDrawable(outDrawables);
//        seekBar.setProgressDrawable(progressDrawable);
        seekBar.setOnSeekBarChangeListener(this);
        addView(seekBar);
        LinearLayout.LayoutParams params = (LayoutParams) seekBar.getLayoutParams();
        params.width = AppUtils.dip2px(200);

        textView = ViewUtils.createTextView(context);
        textView.setTextColor(Color.WHITE);
        addView(textView);
    }

    public void setMax(int max) {
        seekBar.setMax(max);
    }

    public void setProgress(int progress) {
        seekBar.setProgress(progress);
    }

    protected void setValue(String value) {
        textView.setText(value);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
