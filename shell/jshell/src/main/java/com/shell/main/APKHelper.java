package com.shell.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class APKHelper {
	public static String AAPT="/Applications/AndroidDeveloper/sdk/android-sdk-macosx/build-tools/30.0.3/aapt";
	private static String APKTOOL=AppUtils.FILE_PATH + "/apktool.jar";//"/usr/local/bin/apktool.jar";
	public static String ANDROID_SDK_JAR= "/Applications/AndroidDeveloper/sdk/android-sdk-macosx/platforms/android-31/android.jar";
	public static String SDKLIB_JAR="/Applications/AndroidDeveloper/sdk/android-sdk-macosx/tools/lib/sdklib-26.0.0-dev.jar";

// mac
//	public static String AAPT="/Applications/AndroidDeveloper/android-sdk-macosx/build-tools/23.0.1/aapt";
//    private static String APKTOOL="file/apktool.jar";//"/usr/local/bin/apktool.jar"; 
//    public static String ANDROID_SDK_JAR= "/Applications/AndroidDeveloper/android-sdk-macosx/platforms/android-22/android.jar";
//	public static String SDKLIB_JAR="/Applications/AndroidDeveloper/android-sdk-macosx/tools/lib/sdklib.jar";

	// windows
//	public static String AAPT="D:/android_sdk_r23.0-windows/build-tools/23.0.3/aapt";
//    private static String APKTOOL="file/apktool.jar";//"/usr/local/bin/apktool.jar"; 
//    public static String ANDROID_SDK_JAR= "D:/android_sdk_r23.0-windows/platforms/android-19/android.jar";
//	public static String SDKLIB_JAR="D:/android_sdk_r23.0-windows/tools/lib/sdklib.jar";
   
	private static final String SHELL_APK_APPLICATION="com.excelsecu.shellapk.ProxyApplication";// 壳apk的ApplicationName
	
	
	
	/**
	 * 对指定apk签名</br>
	 *</br>1.签名前先删除旧的签名记录 
	 *</br>2.生成签名脚本
	 *</br>3.调用签名脚本签名
	 * @param apkPath
	 */
	public static void resignApk(String apkPath)
	{
		if (null == apkPath || !new File(apkPath).exists())
		{
			return ;
		}
		apkPath = new File(apkPath).getAbsolutePath();
		//签名前先删除旧的签名记录 
		// 生成签名脚本
		String scriptPath= makeSignScriptBat(apkPath);
		
		runSignScript(scriptPath);
		LogUtils.i("Apk重新签名完成");
	}
	
	
	/**
	 * windows 平台签名脚本
	 * @param apkPath
	 * 
	 */
	public static String  makeSignScriptBat(String apkPath) 
	{
		String script = null;
		if (null == apkPath)
		{
			return null;
		}
		File file = new File(apkPath);
		String apkName = file.getName();
		String apkParentPath = file.getParent();
		if (!apkName.endsWith(".apk"))
		{
			return null;
		}
		String apkNameNoSuffix = apkName.substring(0, apkName.lastIndexOf(".apk"));
		String apkSignedName = apkNameNoSuffix+"_temp.apk";// 签名后的的apk文件名
		String apkZipalignName = apkNameNoSuffix+"_temp2.apk";// 对齐后的的apk文件名
		String apkSignedPath = apkParentPath +File.separator+apkSignedName;
		String apkZipalignPath = apkParentPath +File.separator+apkZipalignName;
		
		StringBuilder sb = new StringBuilder();
		String keyPath = new File("file\\key.keystore").getAbsolutePath();
		String zipalignPath = new File("file\\zipalign.exe").getAbsolutePath();
		sb.append("SET KEYSTORE="+keyPath+"\r\n");
		sb.append("SET KEYSTORE_ALIAS=apk\r\n");
		sb.append("SET PASS=123456\r\n");
		sb.append("SET UNSIGNED_APK="+apkPath+"\r\n");
		sb.append("SET SIGNED_APK="+apkSignedPath+"\r\n");
		sb.append("SET ZIPALIGN_APK="+apkZipalignPath+"\r\n");
		sb.append("SET JARSIGNER=D:\\jdk1.8\\main\\bin\\jarsigner\r\n");
		sb.append("SET ZIPALIGN_PATH="+zipalignPath+"\r\n");
		//sb.append("SET ZIPALIGN_PATH=D:\\android_sdk_r23.0-windows\\build-tools\\23.0.3\\zipalign\r\n");
		sb.append("%JARSIGNER% -verbose -keystore %KEYSTORE% -storepass %PASS%  -keypass %PASS% -sigfile CERT -digestalg SHA1 -sigalg MD5withRSA -signedjar %SIGNED_APK% %UNSIGNED_APK% %KEYSTORE_ALIAS%\r\n");
		sb.append("%ZIPALIGN_PATH% -v 4 %SIGNED_APK% %ZIPALIGN_APK%\r\n");
		//sb.append("pause\r\n");
		script = sb.toString();
		//String scriptPath = apkParentPath+"\\signApk.bat";
		String scriptPath = "file\\signApk.bat";
		File scriptBat = new File(scriptPath);
		if (!scriptBat.exists())
		{
			try {
				scriptBat.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(scriptBat));
			bw.write(script);
			bw.close();
			
			LogUtils.i("生成Apk签名脚本完成:"+scriptPath);
			//LogUtils.i(script);
			return scriptPath;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static void runSignScript(String scriptPath)
	{
		 try {
			 
				String signScriptPath = scriptPath;
				Process process = Runtime.getRuntime().exec(signScriptPath);
				
				//OutputStream os = process.getOutputStream();
				InputStream is = process.getInputStream();
			    InputStream error = process.getErrorStream();
				LogUtils.i("签名过程："+ FileHelper.getString(is));
				
				LogUtils.i( "签名错误:"+FileHelper.getString(error));
				LogUtils.i("exit value = "+process.exitValue());
				//process.getOutputStream().close();
				process.waitFor();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	
//	public static boolean buildShellApk(File srcApk ,String shellApkPath)
//	{
//		boolean ok = decodeApk(srcApk);
//		if (!ok)
//		{
//			return false;
//		}
//		String apkSimpleName = srcApk.getName().replace(".apk", "");
//		return buildApk(new File("temp/"+apkSimpleName),shellApkPath);
//	}
	
    /**
     *  反编译apk,输出目录：temp/apkname
     * @param apk
     * @return
     */
	public static boolean decodeApk(File apk, File decodeFile)
	{
		if (null == apk || !apk.exists() || !apk.getName().endsWith(".apk"))
		{
			return false;
		}
		
		//String apkSimpleName = apk.getName().replace(".apk", "");
		try {
			FileHelper.clearFolder(decodeFile);
			String cmdFormat = "java -jar %s -f d -d %s -o %s ";
			String cmdStr = String.format(cmdFormat, APKTOOL,apk.getPath(),decodeFile.getPath());
			Process process = Runtime.getRuntime().exec(cmdStr);
			InputStream error = process.getErrorStream();
			String err = FileHelper.getString(error);
			if (null != err && err.length() > 0)
			{
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
	/**
	 * 1、原apk反编译出的res 、asset 、androidmanifest.xml
	 * 2、壳apk的classes.dex、 lib
	 * 3、修改原apk的androidmanifest.xml,Application的name属性为壳apk的Application名称, 删除 debugable和allowbackup属性
	 * 4、aapt打包
	 */
	public static boolean buildApk( File srcApkDecode, String shellApkPath, boolean isSupportUPX)
	{
		// 修改原apk的androidmanifest.xml的Application的name属性为壳apk的Application名称 
		boolean setOk = XmlHelper.setApplicationName(new File(srcApkDecode.getPath()+"/AndroidManifest.xml"),SHELL_APK_APPLICATION);
	    if (!setOk)
	    {
	    	return false;
	    }
		// 读取原apk minSdkVersion targetSdkVersion versionCode versionName
		String[] items = {"minSdkVersion","targetSdkVersion","versionCode","versionName"};
		HashMap<String, String> map = new HashMap<>();
		map.put(items[0], "8");
		map.put(items[1], "17");
		map.put(items[2], "1");
		map.put(items[3], "1.0.0");
		File file = new File(srcApkDecode.getPath()+"/apktool.yml");
		if (file.exists())
		{
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line  = null;
				while(true)
				{
					line = br.readLine();
					if (null == line)
					{
						break;
					}
					line = line.trim();
					for (String item : items)
					{
						if (line.startsWith(item))
						{
							map.put(item, line.split(":")[1].replace("\'", ""));
							break;
						}
					}
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		if (null == srcApkDecode || !srcApkDecode.exists() || !srcApkDecode.isDirectory())
		{
			return false;
		}
		String srcApkDecodeDir = srcApkDecode.getPath().replace('\\','/');
		String res= "temp/resources.ap_";
		String cmdFormat = "%s p -f  --min-sdk-version %s --target-sdk-version %s --version-code %s --version-name %s -M %s -S %s  -I %s -F %s";
		String cmdStr = String.format(cmdFormat, AAPT,map.get(items[0]),map.get(items[1]),map.get(items[2]),map.get(items[3]) ,srcApkDecodeDir+"/AndroidManifest.xml",srcApkDecodeDir+"/res" ,ANDROID_SDK_JAR,res);
		LogUtils.i("打包资源 cmd： "+cmdStr);
		try {
			File resFile = new File(res);
			if (resFile.exists())
			{
				resFile.delete();
			}
			// 先打包资源
			LogUtils.i("准备打包资源... ");
			Process process = Runtime.getRuntime().exec(cmdStr);
			InputStream in = process.getInputStream();
			String instr = FileHelper.getString(in);
			LogUtils.i("打包资源 InputStream = "+instr);
			InputStream error = process.getErrorStream();
			String err = FileHelper.getString(error);
			if (null != err && err.length() >0)
			{
				LogUtils.i( "打包资源时错误:"+err);
				//return false;
			}
			LogUtils.i("打包资源成功");
			
			if (isSupportUPX)
			{
				
				File[] soFiles = FileHelper.getSubFile(new File("input/shell/libs"),new String[]{".so"});
				File upx = new File("file/upx.out");
				if (null != soFiles && soFiles.length >0)
				{
					for (File f:soFiles)
					{
					  boolean b = FileHelper.getUpxso(upx, f, new File(f.getPath().replace("input/shell/libs", "input/shell/libs_upx")));
					  if (!b)
					  {
						  return false;
					  }
					}
				}
			}
			
			String libsPath = isSupportUPX ? "input/shell/libs_upx":"input/shell/libs";
			cmdFormat = "java -cp %s com.android.sdklib.build.ApkBuilderMain %s -v -u -z %s -f %s -rf %s";
			cmdStr = String.format(cmdFormat, SDKLIB_JAR,shellApkPath,res,"input/shell/classes.dex",libsPath);
			File shellApk = new File(shellApkPath);
			if (shellApk.exists())
			{
				shellApk.delete();
			}
			// 打包apk
			process = Runtime.getRuntime().exec(cmdStr);
			error = process.getErrorStream();
			in = process.getInputStream();
			instr = FileHelper.getString(in);
			LogUtils.i("打包apk InputStream = "+instr);
			err = FileHelper.getString(error);
			if (null != err && err.length() >0)
			{
				LogUtils.i( "打包apk时错误:"+err);
				//return false;
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUtils.i( "打包apk失败");
			return false;
		}finally
		{
			// 删除临时文件
			FileHelper.deleteFolder(srcApkDecode);
			new File(res).delete();
		}
		LogUtils.i( "打包apk成功");
		
		return true;
	}
}
