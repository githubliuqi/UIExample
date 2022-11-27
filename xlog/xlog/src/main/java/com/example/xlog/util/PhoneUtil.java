package com.example.xlog.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;

import com.example.xlog.LogVersion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liuqi on 2020/3/29.
 */

public class PhoneUtil {

    /**
     * 判断当前设备是不是平板
     * 可能不够严格准确
     * @param context
     * @return
     */
    public static  boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    /**
     * 获取当前设备的Brand
     * @return
     */
    public static String getBrand(){
        return Build.BRAND;
    }

    /**
     * 获取当前设备的Model
     * @return
     */
    public static String getModel(){
        return Build.MODEL;
    }

    /**
     * 获取当前设备的Serial
     * @return
     */
    public static String getSerial(){
        return Build.SERIAL;
    }

    /**
     * 获取当前设备的Product
     * @return
     */
    public static String getProduct(){
        return Build.PRODUCT;
    }

    /**
     * 获取当前设备的Device
     * @return
     */
    public static String getDevice(){
        return Build.DEVICE;
    }

    /**
     * 获取当前设备的Id
     * @return
     */
    public static String getId(){
        return Build.ID;
    }

    /**
     * 获取当前设备的Display
     * @return
     */
    public static String getDisplay(){
        return Build.DISPLAY;
    }

    /**
     * 获取当前设备的Board
     * @return
     */
    public static String getBoard(){
        return Build.BOARD;
    }

    /**
     * 获取当前设备的Manufacture
     * @return
     */
    public static String getManufacture(){
        return Build.MANUFACTURER;
    }



    /**
     * 获取当前设备的adnroid OS sdk版本 int值
     * @return
     */
    public static int getSDKVersion(){
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前设备的adnroid OS sdk版本 string值，比如:android6.0
     * @return
     */
    public static String getSDKRelease(){
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前设备的App启动时的CPU架构类型
     * @return
     */
    public static String getCPUABI(){
        return Build.CPU_ABI;
    }

    public static String getCPUABI2(){
        return Build.CPU_ABI2;
    }

    /**
     * 获取当前设备支持的所有CPU架构类型
     * @return
     */
    public static String[] getSupportABIs(){
        String[] abis = new String[]{};
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP)
        {
            abis = Build.SUPPORTED_ABIS;
        } else {
            abis = new String[]{Build.CPU_ABI, Build.CPU_ABI2};
        }
        StringBuilder abiStr = new StringBuilder();
        for(String abi:abis)
        {
            abiStr.append(abi);
            abiStr.append(',');
        }
        return  abis;
    }

    /**
     * 获取当前App的版本号
     * @param context
     * @return
     */
    public static String getAppVersion(Context context){
        String appVersion = "";
        if (null == context){
            return "";
        }
        try {
            appVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    public static String getProcessName() {
        int pid = android.os.Process.myPid();
        String name = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/"+pid+"/cmdline"));
            name = reader.readLine();
            if (!TextUtils.isEmpty(name)) {
                name = name.trim();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return name;
    }
    
    public static String getPhoneInfo(){
        StringBuilder sb = new StringBuilder();
        for (String info : getPhoneInfoList()){
            sb.append(info).append("\n");
        }
        return sb.toString();
    }


    public static List<String> getPhoneInfoList(){
        List<String> list = new ArrayList<>();
        list.add("App包名:"+ getProcessName());
        list.add("品牌(BRAND):"+ getBrand());
        list.add("型号(MODEL):"+getModel());
        list.add("系统版本号(SDK_INT):"+getSDKVersion());
        list.add("系统版本名:"+getSDKRelease());
        list.add("CPU_ABI:"+ getCPUABI());
        list.add("CPU_ABI2:"+ getCPUABI2());
        list.add("Support ABIs:"+ Arrays.toString(getSupportABIs()));
        list.add("日志库版本:"+ LogVersion.getLogVersion());
        list.add("App日志根目录:"+ LogUtil.getAppLogRoot());
        return list;
    }


}
