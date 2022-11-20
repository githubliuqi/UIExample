package com.example.juiexample.event;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.common.TLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventHandler implements Handler.Callback{

    private static final String TAG = "EventHandler";
    private static final String EVENT_KEY_NAME = "EventHandler_Event_Key";
    private static final EventHandler INSTANCE = new EventHandler();
    private final Handler UI_HANDLER = new Handler(Looper.getMainLooper(), this);
    private final Map<String, List<OnEventListener>> listenerMap = new HashMap<>();

    public static final EventHandler getInstance() {
        return INSTANCE;
    }

    public EventHandler addOnEventListener(String key, OnEventListener listener) {
        if (TextUtils.isEmpty(key) || listener == null) {
            return this;
        }
        List<OnEventListener> list = listenerMap.get(key);
        if (list == null) {
            list = new ArrayList<>();
            listenerMap.put(key, list);
        }
        list.add(listener);
        return this;
    }

    public EventHandler removeOnEventListener(String key, OnEventListener listener) {
        if (TextUtils.isEmpty(key) || listener == null) {
            return this;
        }
        List<OnEventListener> list = listenerMap.get(key);
        if (list != null) {
            list.remove(listener);
        }
        return this;
    }

    public EventHandler sendEvent(String key, Bundle bundle) {
        if (TextUtils.isEmpty(key)) {
            TLog.e(TAG, "sendEvent key is null!!!");
            return this;
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        if (bundle.containsKey(EVENT_KEY_NAME)) {
            TLog.e(TAG, "bundle containsKey : " + EVENT_KEY_NAME);
            return this;
        }
        bundle.putString(EVENT_KEY_NAME, key);
        Message message = UI_HANDLER.obtainMessage();
        message.obj = bundle;
        UI_HANDLER.sendMessage(message);
        TLog.d(TAG, String.format("sendEvent:key=%s, bundle=%s", key, bundle));
        return this;
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (!(msg.obj instanceof Bundle)) {
           return false;
        }
        Bundle bundle = (Bundle) msg.obj;
        String key = bundle.getString(EVENT_KEY_NAME);
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        List<OnEventListener> list = listenerMap.get(key);
        if (list == null) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            OnEventListener listener = list.get(i);
            listener.onEvent(key, bundle);
        }
        return true;
    }
}
