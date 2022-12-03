package com.example.snaker.model;

import android.graphics.Color;

import java.util.Random;

public class GameDef {
    public static final String GAME_EVENT_OVER = "game_event_over";
    public static final String GAME_EVENT_SCORE_UPDATE = "game_event_score_update";
    public static final String GAME_EVENT_SPEED_UPDATE = "game_event_speed_update";
    public static final String GAME_EVENT_KEY_LEFT = "game_event_key_left";
    public static final String GAME_EVENT_KEY_RIGHT = "game_event_key_right";
    public static final String GAME_EVENT_KEY_UP = "game_event_key_up";
    public static final String GAME_EVENT_KEY_DOWN = "game_event_key_down";
    public static final String GAME_EVENT_KEY_START = "game_event_key_start";
    public static final String GAME_EVENT_KEY_STOP = "game_event_key_stop";
    public static final String GAME_EVENT_KEY_PAUSE = "game_event_key_pause";

    public enum MoveType {
        TYPE_MOVE_LEFT,
        TYPE_MOVE_RIGHT,
        TYPE_MOVE_DOWN,
        TYPE_MOVE_UP,
    }


    public static final int[] COLORS = {
            Color.MAGENTA, Color.BLUE, Color.YELLOW, Color.RED,
            Color.GREEN, Color.CYAN, Color.LTGRAY, Color.LTGRAY
    };

    public static final Random RANDOM = new Random(System.currentTimeMillis());

}
