package com.shell.main.test;

import com.shell.main.AppUtils;
import com.shell.main.FileHelper;
import com.shell.main.LogUtils;

import java.io.File;

public class MainTest {

    public static void test() {
        String apk = AppUtils.FILE_PATH + "/app.apk";
        String apktoolPath = AppUtils.FILE_PATH + "/apktool_2.7.0.jar";
        String apkSimpleName = new File(apk).getName().replace(".apk", "");
        String outDir = AppUtils.TEMP_PATH + File.separator + apkSimpleName;
        ApkPraser apkPraser = new ApkPraser();
        apkPraser.setApktoolPath(apktoolPath)
                .setApkPath(apk)
                .setOutDir(outDir);
        if (!apkPraser.parse()) {
            FileHelper.deleteFolder(new File(outDir));
            LogUtils.i("反编译原apk失败");
            return;
        }
    }
}
