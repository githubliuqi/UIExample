package com.example.snaker.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.juiexample.common.TitleSeekbar;
import com.example.juiexample.event.EventHandler;
import com.example.snaker.model.GameDef;

public class SpeedItemView extends TitleSeekbar {

    private static final int MIN_INTERVAL_MS = 0;
    private static final int MAX_INTERVAL_MS = 1000;
    private static final int MIDDLE_INTERVAL_MS =  (MIN_INTERVAL_MS + MAX_INTERVAL_MS) / 2;

    public SpeedItemView(Context context) {
        super(context);
        setTitle("刷新间隔");
        setMax(MAX_INTERVAL_MS);
        setProgress(MIDDLE_INTERVAL_MS);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
//        setValue(String.format("%s ms", progress));
        Bundle bundle = new Bundle();
        bundle.putInt("refresh_interval", progress + 100);
        EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SPEED_UPDATE, bundle);
    }
}
