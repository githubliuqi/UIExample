package com.example.tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameDef {
    public static final String GAME_EVENT_OVER = "game_event_over";
    public static final String GAME_EVENT_SCORE_UPDATE = "game_event_score_update";
    public static final String GAME_EVENT_KEY_LEFT = "game_event_key_left";
    public static final String GAME_EVENT_KEY_RIGHT = "game_event_key_right";
    public static final String GAME_EVENT_KEY_UP = "game_event_key_up";
    public static final String GAME_EVENT_KEY_DOWN = "game_event_key_down";
    public static final String GAME_EVENT_KEY_START = "game_event_key_start";
    public static final String GAME_EVENT_KEY_STOP = "game_event_key_stop";
    public static final String GAME_EVENT_KEY_PAUSE = "game_event_key_pause";

    public enum KeyType {
        TYPE_MOVE_LEFT,
        TYPE_MOVE_RIGHT,
        TYPE_MOVE_DOWN,
        TYPE_SWITCH_STYLE,
    }

    public enum GridType {
        TYPE_DEFAULT,
        TYPE_RED,
        TYPE_GREEN,
        TYPE_MAGENTA,
        TYPE_BLUE,
        TYPE_YELLOW,
    }

    public static final GridType[][] ACTIVEBOX_TYPE_ARRAY_0 = new GridType[][]{
            {GridType.TYPE_GREEN, GridType.TYPE_GREEN},
            {GridType.TYPE_GREEN, GridType.TYPE_GREEN},
    };

    public static final GridType[][] ACTIVEBOX_TYPE_ARRAY_1 = new GridType[][]{
            {GridType.TYPE_MAGENTA},
            {GridType.TYPE_MAGENTA},
            {GridType.TYPE_MAGENTA},
            {GridType.TYPE_MAGENTA}
    };

    public static final GridType[][] ACTIVEBOX_TYPE_ARRAY_2 = new GridType[][]{
            {GridType.TYPE_MAGENTA, GridType.TYPE_MAGENTA, GridType.TYPE_MAGENTA, GridType.TYPE_MAGENTA}
    };

    public static final GridType[][] ACTIVEBOX_TYPE_ARRAY_3 = new GridType[][]{
            {GridType.TYPE_MAGENTA, GridType.TYPE_DEFAULT},
            {GridType.TYPE_MAGENTA, GridType.TYPE_DEFAULT},
            {GridType.TYPE_MAGENTA, GridType.TYPE_MAGENTA}
    };

    public static final GridType[][] ACTIVEBOX_TYPE_ARRAY_4 = new GridType[][]{
            {GridType.TYPE_DEFAULT, GridType.TYPE_GREEN, GridType.TYPE_GREEN},
            {GridType.TYPE_GREEN, GridType.TYPE_GREEN, GridType.TYPE_DEFAULT}
    };

    public static final List<GridType[][]> ACTIVEBOX_TYPE_LIST = new ArrayList<>(Arrays.asList(
            ACTIVEBOX_TYPE_ARRAY_0,
            ACTIVEBOX_TYPE_ARRAY_1,
            ACTIVEBOX_TYPE_ARRAY_2,
            ACTIVEBOX_TYPE_ARRAY_3,
            ACTIVEBOX_TYPE_ARRAY_4
    ));
}
