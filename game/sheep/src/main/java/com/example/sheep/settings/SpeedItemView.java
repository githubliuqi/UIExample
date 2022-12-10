package com.example.sheep.settings;

import android.content.Context;
import android.os.Bundle;
import android.widget.SeekBar;

import com.example.juiexample.common.TitleSeekbar;
import com.example.juiexample.event.EventHandler;
import com.example.sheep.model.GameDef;

public class SpeedItemView extends TitleSeekbar {

    private static final int MIN_LEVEL = 0;
    private static final int MAX_LEVEL = 10;
    private static final int MIDDLE_LEVEL =  (MIN_LEVEL + MAX_LEVEL) / 2;

    public SpeedItemView(Context context) {
        super(context);
        setTitle("游戏难度");
        setMax(MAX_LEVEL);
        setProgress(MIDDLE_LEVEL);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        super.onProgressChanged(seekBar, progress, fromUser);
        setValue(String.format("%s", progress));
        Bundle bundle = new Bundle();
        bundle.putInt("level", progress);
        EventHandler.getInstance().sendEvent(GameDef.GAME_EVENT_LEVEL_UPDATE, bundle);
    }
}
