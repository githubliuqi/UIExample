package com.example.sheep.model;

import android.graphics.Color;

import com.example.sheep.R;

import java.util.Random;

public class GameDef {
    public static final String GAME_EVENT_OVER = "game_event_over";
    public static final String GAME_EVENT_SCORE_UPDATE = "game_event_score_update";
    public static final String GAME_EVENT_LEVEL_UPDATE = "game_event_level_update";


    public static final int[] COLORS = {
            Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED,
            Color.GREEN, Color.CYAN, Color.LTGRAY
    };

    public static final int[] IMAGES = {
            R.drawable.image_apple,
            R.drawable.image_banana,
            R.drawable.image_cake,
            R.drawable.image_cherry,
            R.drawable.image_fish,
            R.drawable.image_grapes,
            R.drawable.image_peach,
            R.drawable.image_pear,
            R.drawable.image_pineapple,
            R.drawable.image_radish,
            R.drawable.image_rice,
            R.drawable.image_salad,
            R.drawable.image_sandwich,
            R.drawable.image_strawberry
    };

    public static final Random RANDOM = new Random(System.currentTimeMillis());

}
