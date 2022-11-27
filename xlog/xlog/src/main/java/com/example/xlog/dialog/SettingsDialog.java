package com.example.xlog.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ScrollView;

import com.example.xlog.LogConfig;
import com.example.xlog.util.InnerLog;


/**
 * 日志库配置弹框
 */
public class SettingsDialog extends AlertDialog {

    private SettingsView settingsView;

    private Context context;

    private final LogConfig logConfig = LogConfig.getInstance();


    public SettingsDialog(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogUtil.fixAlertDialog(this);
    }

    private void init() {

        ScrollView scrollView = new ScrollView(context);
        settingsView = new SettingsView(context);
        settingsView.addItemView("ServerIP", logConfig.getKeyLogServerIp());
        settingsView.addItemView("ServerPort", logConfig.getKeyLogServerPort()+"");
        scrollView.addView(settingsView);
        setView(scrollView);

        setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        setButton(AlertDialog.BUTTON_POSITIVE, "确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ip = settingsView.getItemText(0);
                String portStr = settingsView.getItemText(1);
                InnerLog.sysout("log-server-ip:"+ip);
                InnerLog.sysout("log-server-port:"+portStr);
                int port = 0;
                try {
                    port = Integer.parseInt(portStr);
                }catch (Exception e){

                }
                logConfig.setLogServer(ip, port);
            }
        });
    }
}
