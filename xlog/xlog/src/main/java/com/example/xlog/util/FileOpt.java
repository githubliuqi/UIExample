
package com.example.xlog.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOpt {
    
    private final File file;

    // 文件创建时首先写入内容
    private final String firstInfo;
    
    public FileOpt(String path) {
        this(path, "");
    }

    public FileOpt(String path, String firstInfo) {
        file = new File(path);
        this.firstInfo = firstInfo;
        prepare();
    }

    private void prepare(){
        if (!file.exists()){
            file.getParentFile().mkdirs();  // 递归创建缺失的上级目录
            if (file.exists() && file.isDirectory()) {
                file.delete();
            }
            if (!file.exists()) {
                try {
                    System.out.println("创建文件："+file.getAbsolutePath());
                    file.createNewFile();
                    write(firstInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("创建文件异常："+ Log.getStackTraceString(e));
                }
            }
        }
    }

    public File getFile(){
        return file;
    }

    public void write(String writeStr) {
        if (null == writeStr){
            writeStr = "";
        }
        byte[] bytes = writeStr.getBytes();
        write(bytes, 0, bytes.length);
    }

    public void write(byte[] buffer, int offset, int count) {
        prepare();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file, true);
            fOut.write(buffer, offset, count);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != fOut){
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String read() {
        String str = "";
        FileInputStream fIn = null;
        try {
            if (!file.exists()) {
                return "";
            }
            fIn = new FileInputStream(file);
            int length = fIn.available();

            byte[] buf = new byte[length];
            fIn.read(buf);

            str = new String(buf, "UTF-8");
        } catch (IOException e) {

        }finally {
            if (null != fIn){
                try {
                    fIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public void delete() {
        if (file.exists()) {
            file.delete();
        }
    }

    public void clear() {
        if (!file.exists()) {
            return;
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file, false);
            fOut.write("".getBytes());
        }catch (Exception e) {

        }finally {
            if (null != fOut){
                try {
                    fOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public String addNewLine(String str) {
        if (0 != file.length()) {
            str = "\n" + str;
        }
        return str;
    }
    
    public byte[] addNewLine(byte[] buffer, int offset, int count)  {
        if (0 != file.length()) {
        	byte[] result = new byte[count+1];
        	System.arraycopy(buffer, 0, result, 0, count);
        	result[count] = '\n';
        	return result;
        }
        return buffer;
    }

    public static String getFileName(File file){
        if (null == file || null == file.getName()){
            return "";
        }
        String name = file.getName();
        if (name.contains(".")){
            int pos = name.lastIndexOf(".");
            return name.substring(0, pos);
        }
        return name;
    }

    public static String getFileSuffix(File file){
        if (null == file || null == file.getName()){
            return "";
        }
        String name = file.getName();
        if (name.contains(".")){
            int pos = name.lastIndexOf(".");
            return name.substring(pos, name.length());
        }
        return "";
    }
}
