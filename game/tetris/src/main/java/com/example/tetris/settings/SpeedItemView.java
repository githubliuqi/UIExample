package com.example.tetris.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.juiexample.common.TitleSeekbar;
import com.example.juiexample.event.EventHandler;
import com.example.tetris.model.GameDef;

public class SpeedItemView extends TitleSeekbar {

    public SpeedItemView(Context context) {
        super(context);
        setTitle("掉落速度");
        setMax(1000);
        setProgress(500);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
//        setValue(String.format("%s ms", progress));
        Bundle bundle = new Bundle();
        bundle.putInt("speed", progress);
        EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_SPEED_UPDATE, bundle);
    }
}
