package com.example.xlog.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by liuqi on 2020/4/18.
 */

public class ObjectUtil {

    /**
     * 将一个Object对象转为1个字符串（还需要不断完善）
     * 目前支持常见的集合、Throwable、Json。
     *
     * @param object
     * @return
     */
    public static String objectToString(Object object){

        if (null == object){
            return "null";
        }
        if (object instanceof JSONObject){
            try {
                return ((JSONObject) object).toString(4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (object instanceof JSONArray){
            try {
                return ((JSONArray) object).toString(4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (Throwable.class.isAssignableFrom(object.getClass())){
            return Log.getStackTraceString((Throwable) object);
        }
        if (object.getClass().isArray()){
            try {
                return Arrays.deepToString((Object[]) object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

}
