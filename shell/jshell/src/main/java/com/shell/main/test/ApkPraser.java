package com.shell.main.test;

import com.shell.main.FileHelper;
import com.shell.main.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ApkPraser {

    private String apkPath;
    private String outDir;
    private String apktoolPath;

    public ApkPraser() {

    }

    public ApkPraser setApktoolPath(String apktoolPath) {
        this.apktoolPath = apktoolPath;
        return this;
    }

    public ApkPraser setApkPath(String apkPath) {
        this.apkPath = apkPath;
        return this;
    }

    public ApkPraser setOutDir(String outDir) {
        this.outDir = outDir;
        return this;
    }

    public boolean parse() {
        if (null == apkPath || !new File(apkPath).exists() || !apkPath.endsWith(".apk")) {
            return false;
        }
        if (outDir == null) {
            return false;
        }
        /**
         * java -jar apktool.jar d -d com.mianfei.book_5.1.0_85.apk -o com.mianfei.book
         * d --反编译参数
         * -d --使用该参数表示反编译后使用java作为文件后缀名，是为了方便在开发环境中调试
         * -o --反编译后输出目录
         * 如果出现错误: SmaliDebugging has been removed in 2.1.0 onward 。可以将apktool版本降低到>2.1之前，或者不使用-d 参数自己手动修改文件后缀名。
         */
        try {
            FileHelper.clearFolder(new File(outDir));
            String cmdFormat = "java -jar %s -f d  %s -o %s ";
//            String cmdFormat = "java -jar %s -f d -d %s -o %s ";
            String cmdStr = String.format(cmdFormat, apktoolPath, apkPath, outDir);
            Process process = Runtime.getRuntime().exec(cmdStr);
            InputStream error = process.getErrorStream();
            String err = FileHelper.getString(error);
            if (null != err && err.length() > 0) {
                LogUtils.i( "反编译apk错误:"+err);
                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
