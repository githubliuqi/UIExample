package com.example.shellapk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import com.excelsecu.shelldec.Dec;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.DexClassLoader;
/*
 * ArrayMap :SDK19开始使用，之前是HashMap
 */
public class ProxyApplication2 extends Application{
	private static final String APP_NAME = "APPLICATION_CLASS_NAME";
	
	private String mAppName = null;
	
	private String apkFileName;
	private String odexPath;
	private String libPath;

	private static String P_LIB_PATH="java.library.path";
	private static String CPU_ABI = null;
	

	@SuppressLint("NewApi")
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		
		
		LogUtils.log(P_LIB_PATH+" = "+System.getProperty(P_LIB_PATH));
		//String oldPath = System.getProperty(P_LIB_PATH);
//		String newPath = oldPath.replace("lib64", "lib");
//		LogUtils.log("set newPath = "+newPath);
//		System.setProperty(P_LIB_PATH, newPath);
//		LogUtils.log("newPath = "+System.getProperty(P_LIB_PATH));
//		LogUtils.log("getPackageCodePath = " +getPackageCodePath());
//		LogUtils.log("getPackageResourcePath" + getPackageResourcePath());
		
		LogUtils.log("CPU_ABI ="+ Build.CPU_ABI);
		LogUtils.log("CPU_ABI2 ="+ Build.CPU_ABI2);
		CPU_ABI = getCPU_ABI(); // 获取当前手机的CPU类型
		LogUtils.log("get CPU_ABI ="+ CPU_ABI);
		LogUtils.log(" ProxyApplication attachBaseContext enter");
		
		try {
			
			File odex = this.getDir("payload_odex", MODE_PRIVATE);
			File libs = this.getDir("payload_lib", MODE_PRIVATE);//payload_lib
			odexPath = odex.getAbsolutePath();
			libPath = libs.getAbsolutePath();
			apkFileName = odex.getAbsolutePath() + "/payload.apk";
			File dexFile = new File(apkFileName);
			
			if (!dexFile.exists())
			{
				dexFile.createNewFile(); 
				byte[] dexdata = this.readDexFileFromApk();
				this.splitPayLoadFromDex(dexdata);
				File appInfo = new File(odexPath+"/app.dat");
				appInfo.createNewFile();
				BufferedWriter bw = new BufferedWriter(new FileWriter(appInfo));
				bw.write(mAppName);
				bw.flush();
				bw.close();
			}
			
			Object currentActivityThread = RefInvoke.invokeStaticMethod(
					"android.app.ActivityThread", "currentActivityThread",
					new Class[] {}, new Object[] {});
			String packageName = this.getPackageName();
//			ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
//					"android.app.ActivityThread", currentActivityThread,
//					"mPackages");
		
			WeakReference wr = null;
			if (isOverSDK19())
			{
				ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
						"android.app.ActivityThread", currentActivityThread,"mPackages");
				wr = (WeakReference) mPackages.get(packageName);
			}else
			{
				HashMap mPackages = (HashMap) RefInvoke.getFieldOjbect(
						"android.app.ActivityThread", currentActivityThread,"mPackages");
				wr = (WeakReference) mPackages.get(packageName);
			}
			
			//WeakReference wr = (WeakReference) mPackages.get(packageName);
			DexClassLoader dLoader = new DexClassLoader(apkFileName, odexPath,
					libPath, (ClassLoader) RefInvoke.getFieldOjbect(
							"android.app.LoadedApk", wr.get(), "mClassLoader"));
			RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
					wr.get(), dLoader);
			
			LogUtils.log("CPU_ABI = "+Build.CPU_ABI);
			LogUtils.log("CPU_ABI2 = "+Build.CPU_ABI2);
		    LogUtils.log(P_LIB_PATH+" = "+System.getProperty(P_LIB_PATH));
		} catch (Exception e) {
			Log.e("demo", "error:"+Log.getStackTraceString(e));
            LogUtils.log(Log.getStackTraceString(e));
			e.printStackTrace();
		}
		LogUtils.log("attachBaseContext function over");
	
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		LogUtils.log("ProxyApplication onCreate enter");
		File appInfo = new File(odexPath+"/app.dat");
		try {
			BufferedReader br = new BufferedReader(new FileReader(appInfo));
			mAppName = br.readLine();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LogUtils.log("应用程序包名 - "+mAppName);
		if (null == mAppName) {
			LogUtils.log("mAppName  is null");
			return;
		}
		Object currentActivityThread = RefInvoke.invokeStaticMethod("android.app.ActivityThread", 
				"currentActivityThread",new Class[] {}, new Object[] {});
		Object mBoundApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mBoundApplication");
		Object loadedApkInfo = RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData", mBoundApplication,"info");
		RefInvoke.setFieldOjbect("android.app.LoadedApk", "mApplication",loadedApkInfo, null);
		Object oldApplication = RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mInitialApplication");
		ArrayList<Application> mAllApplications = (ArrayList<Application>) RefInvoke.getFieldOjbect("android.app.ActivityThread",
				currentActivityThread, "mAllApplications");
		mAllApplications.remove(oldApplication);// 
		ApplicationInfo appinfo_In_LoadedApk = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.LoadedApk", 
				loadedApkInfo,"mApplicationInfo");
		ApplicationInfo appinfo_In_AppBindData = (ApplicationInfo) RefInvoke.getFieldOjbect("android.app.ActivityThread$AppBindData",
				mBoundApplication, "appInfo");
		appinfo_In_LoadedApk.className = mAppName;
		appinfo_In_AppBindData.className = mAppName;
		Application app = (Application) RefInvoke.invokeMethod("android.app.LoadedApk", "makeApplication", loadedApkInfo,
				new Class[] { boolean.class, Instrumentation.class },new Object[] { false, null });							
		RefInvoke.setFieldOjbect("android.app.ActivityThread","mInitialApplication", currentActivityThread, app);
		
		//ArrayMap mProviderMap = (ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");
		//Iterator it = mProviderMap.values().iterator();
		Iterator it = null;
		if (isOverSDK19())
		{
			ArrayMap mProviderMap = (ArrayMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");
			 it = mProviderMap.values().iterator();
		}else
		{
			HashMap mProviderMap = (HashMap) RefInvoke.getFieldOjbect("android.app.ActivityThread", currentActivityThread,"mProviderMap");
			 it = mProviderMap.values().iterator();
		}
		
		LogUtils.log("it = "+it);
		while (it.hasNext()) {
			Object providerClientRecord = it.next();
			if (null == providerClientRecord)
			{
	           break;
			}
			LogUtils.log("providerClientRecord = "+providerClientRecord);
			Object localProvider = RefInvoke.getFieldOjbect(
					"android.app.ActivityThread$ProviderClientRecord",
					providerClientRecord, "mLocalProvider");
			if (null == localProvider)
			{
				continue;
			}
			LogUtils.log("localProvider = "+localProvider);
			LogUtils.log("-- localProvider = "+localProvider+", app = "+app);
			RefInvoke.setFieldOjbect("android.content.ContentProvider",
					"mContext", localProvider, app);
		}
		// 启动源Apk的Application
		app.onCreate();
		LogUtils.log("ProxyApplication onCreate end");
	}
	
	
	/**
	 * �ͷű��ӿǵ�apk�ļ���so�ļ�
	 * @param data
	 * @throws IOException
	 */
	private void splitPayLoadFromDex(byte[] apkdata) throws IOException {
		LogUtils.log("---- splitPayLoadFromDex ---");
		int ablen = apkdata.length;
		// 先解出源Apk的ApplicationName
		byte[] appNamelen = new byte[4];
		System.arraycopy(apkdata, ablen - 4, appNamelen, 0, 4);
		ByteArrayInputStream bais = new ByteArrayInputStream(appNamelen);
		DataInputStream in = new DataInputStream(bais);
		int appNameByteLen = in.readInt();
        byte[] appNameByte = new byte[appNameByteLen];
		System.arraycopy(apkdata, ablen - 4 - appNameByteLen, appNameByte, 0, appNameByteLen);
		String appName = new String(appNameByte);
		mAppName = appName;
		LogUtils.log("解出  应用程序包名 - "+mAppName);
		// 解出源apk
		LogUtils.log("先读出apk...");
		byte[] dexlen = new byte[4];
		System.arraycopy(apkdata, ablen - 8 - appNameByteLen, dexlen, 0, 4);
		bais = new ByteArrayInputStream(dexlen);
		in = new DataInputStream(bais);
		int apkByteLen = in.readInt();
		byte[] newdex = new byte[apkByteLen];
		System.arraycopy(apkdata, ablen - 8-  appNameByteLen- apkByteLen, newdex, 0, apkByteLen);
		// 源apk数据解密
		LogUtils.log("准备解密apk...");
		LogUtils.log("解密前apk大小 ："+newdex.length);
		newdex = decrypt(newdex);
		LogUtils.log("解密后apk大小 ："+newdex.length);
		File file = new File(apkFileName);
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(file);
			localFileOutputStream.write(newdex);
			localFileOutputStream.close();
		} catch (IOException localIOException) {
			throw new RuntimeException(localIOException);
		}
		//复制源Apk的so文件
		ZipInputStream localZipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
		while (true) {
			ZipEntry localZipEntry = localZipInputStream.getNextEntry();
			if (localZipEntry == null) {
				localZipInputStream.close();
				break;
			}
			String name = localZipEntry.getName();
			if (name.startsWith("lib/") && name.endsWith(".so")) {
				if (CPU_ABI == null || !name.contains(CPU_ABI))
				{ // 只复制当前手机CPU支持的so文件
					continue;
				}
				LogUtils.log("copy lib  = "+name);
				File storeFile = new File(libPath + "/"	+ name.substring(name.lastIndexOf('/')));
				storeFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(storeFile);
				byte[] arrayOfByte = new byte[1024];
				while (true) {
					int i = localZipInputStream.read(arrayOfByte);
					if (i == -1)
						break;
					fos.write(arrayOfByte, 0, i);
				}
				fos.flush();
				fos.close();
			}
			localZipInputStream.closeEntry();
		}
		localZipInputStream.close();
	}
	
	private static String getCPU_ABI()
	{
		String x86 = "x86";
		String armeabi = "armeabi";
		if (Build.CPU_ABI.contains(x86))
		{
			return x86;
		}else if (Build.CPU_ABI.contains(armeabi))
		{
			return armeabi;
		}
		return armeabi;
	}

	/**
	 * ��apk�������ȡdex�ļ����ݣ�byte��
	 * @return
	 * @throws IOException
	 */
	private byte[] readDexFileFromApk() throws IOException {
		ByteArrayOutputStream dexByteArrayOutputStream = new ByteArrayOutputStream();
		ZipInputStream localZipInputStream = new ZipInputStream(
				new BufferedInputStream(new FileInputStream(
						this.getApplicationInfo().sourceDir)));
		while (true) {
			ZipEntry localZipEntry = localZipInputStream.getNextEntry();
			if (localZipEntry == null) {
				localZipInputStream.close();
				break;
			}
			if (localZipEntry.getName().equals("classes.dex")) {
				byte[] arrayOfByte = new byte[1024];
				while (true) {
					int i = localZipInputStream.read(arrayOfByte);
					if (i == -1)
						break;
					dexByteArrayOutputStream.write(arrayOfByte, 0, i);
				}
			}
			localZipInputStream.closeEntry();
		}
		localZipInputStream.close();
		return dexByteArrayOutputStream.toByteArray();
	}


	private byte[] decrypt(byte[] srcdata) {
        return Dec.getInstance(getApplicationContext()).dec(srcdata);
	}
	
	
	private static boolean isOverSDK19()
	{
		LogUtils.log("Build.VERSION.SDK_INT = "+Build.VERSION.SDK_INT);
		return Build.VERSION.SDK_INT>=19;
	}
	
	/*
	//�����Ǽ�����Դ
	protected AssetManager mAssetManager;//��Դ������  
	protected Resources mResources;//��Դ  
	protected Theme mTheme;//����  
	
	protected void loadResources(String dexPath) {  
		LogUtils.log("loadResources enter");
        try {  
            AssetManager assetManager = AssetManager.class.newInstance();  
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);  
            LogUtils.log(" Method addAssetPath + "+ addAssetPath);
            addAssetPath.invoke(assetManager, dexPath);
           // int ret = Integer.parseInt((String)(addAssetPath.invoke(assetManager, dexPath)));  
           // LogUtils.log(" Method addAssetPath ret = + "+ ret);
            mAssetManager = assetManager;  
            
        } catch (Exception e) {  
        	LogUtils.log("loadResource error:"+Log.getStackTraceString(e));
            e.printStackTrace();  
            return ;
        }  
        Resources superRes = super.getResources();  
        superRes.getDisplayMetrics();  
        superRes.getConfiguration();  
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),superRes.getConfiguration());  
        mTheme = mResources.newTheme();  
        mTheme.setTo(super.getTheme());
    }  
	
	@Override  
	public AssetManager getAssets() {  
		LogUtils.log("getAssets - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mAssetManager == null ? super.getAssets() : mAssetManager;  
	}  
	
	@Override  
	public Resources getResources() {  
		LogUtils.log("getResources - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mResources == null ? super.getResources() : mResources;  
	}  
	
	@Override  
	public Theme getTheme() {  
		LogUtils.log("getResources - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mTheme == null ? super.getTheme() : mTheme;  
	} 
	*/
	
}
