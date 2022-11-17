package com.example.shellapk;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;

import com.excelsecu.shelldec.Dec;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
import java.util.zip.ZipOutputStream;

import dalvik.system.DexClassLoader;
/*
 *  ArrayMap :SDK19开始使用，之前是HashMap
 */
public class ProxyApplication extends Application{
	private static final String APP_NAME = "APPLICATION_CLASS_NAME";
	
	private String mAppName = null;
	
	private String dexFileName;
	private String odexPath;
	private String libPath;
	private String appNamePath;
	private static String P_LIB_PATH="java.library.path";
	private static String CPU_ABI = null;
	
	

	@SuppressLint("NewApi")
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		
		
	
		CPU_ABI = getCPU_ABI(); // 鑾峰彇褰撳墠鎵嬫満鐨凜PU绫诲瀷
		
		try {
			File odex = this.getDir("payload_odex", MODE_PRIVATE);
			File libs = this.getDir("payload_lib", MODE_PRIVATE);//payload_lib
			File dex = this.getDir("payload_dex", MODE_PRIVATE);
			File x86 = new File(libs.getPath()+"/x86");
			File armeabi = new File(libs.getPath()+"/armeabi");
			
			
			odexPath = odex.getAbsolutePath();
			libPath = libs.getAbsolutePath();
			appNamePath = odexPath+"/app.dat";
			dexFileName = dex.getAbsolutePath() + "/classes.dex";
			File dexFile = new File(dexFileName);
			if (!dexFile.exists())
			{
				x86.mkdirs();
				armeabi.mkdirs();
				dexFile.createNewFile(); 
				byte[] dexdata = this.readDexFileFromApk();
				this.splitPayLoadFromDex(dexdata);
				File appInfo = new File(appNamePath);
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
			//String libPaths =libPath+File.pathSeparator+libPath+"/x86"+File.pathSeparator+libPath+"/armeabi";
			String libPaths =libPath+File.pathSeparator+x86.getPath()+File.pathSeparator+armeabi.getPath();
			//LogUtils.log("libPaths = "+libPaths);

			DexClassLoader dLoader = null;
			try
			{   // dex鏂囦欢鏍煎紡鍔犺浇
				//LogUtils.log("try load classes.dex enter");
				dLoader = new DexClassLoader(dexFileName, odexPath,
						libPaths, (ClassLoader) RefInvoke.getFieldOjbect(
								"android.app.LoadedApk", wr.get(), "mClassLoader"));
				
				//LogUtils.log("DexClassLoader = "+dLoader);
				//LogUtils.log("try load classes.dex end");
				mAppName = getAppName();
				if (TextUtils.isEmpty(mAppName))
				{
					//LogUtils.log("mAppName is null");
					return ;
				}
				Class clazz = dLoader.loadClass(mAppName);
				//LogUtils.log("loadClass "+mAppName+ ":"+clazz);
				
			}catch(Exception e)
			{
				//LogUtils.log("try load classes.dex Exception :\n "+e.getLocalizedMessage());
				// zip鏂囦欢鏍煎紡鍔犺浇
				File dexZip = new File(dex.getAbsoluteFile()+"/dex.zip");
				if (!dexZip.exists())
				{   // dex杞瑉ip鏍煎紡
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dexZip));
					FileHelper.zipFile(dexFile, "", zos);
					zos.close();
				}
				//LogUtils.log("try load dex.zip enter");
				dLoader = new DexClassLoader(dexZip.getAbsolutePath(), odexPath,
						libPaths, (ClassLoader) RefInvoke.getFieldOjbect(
								"android.app.LoadedApk", wr.get(), "mClassLoader"));
				
				//LogUtils.log("DexClassLoader = "+dLoader);
			}
			
			if (null == dLoader)
			{
				//LogUtils.log("dLoader is null");
				return ;
			}
			RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
					wr.get(), dLoader);
			//LogUtils.log(" replace mClassLoader of  android.app.LoadedApk");
			
		} catch (Exception e) {
            //LogUtils.log(Log.getStackTraceString(e));
			e.printStackTrace();
		}
		//LogUtils.log("attachBaseContext function over");
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		//LogUtils.log("ProxyApplication onCreate enter");
//		File appInfo = new File(odexPath+"/app.dat");
//		try {
//			BufferedReader br = new BufferedReader(new FileReader(appInfo));
//			mAppName = br.readLine();
//			br.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		mAppName = getAppName();
		//LogUtils.log("搴旂敤绋嬪簭鍖呭悕 - "+mAppName);
		if (null == mAppName) {
			//LogUtils.log("mAppName  is null");
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
		
		//LogUtils.log("it = "+it);
		while (it.hasNext()) {
			Object providerClientRecord = it.next();
			if (null == providerClientRecord)
			{
	           break;
			}
			//LogUtils.log("providerClientRecord = "+providerClientRecord);
			Object localProvider = RefInvoke.getFieldOjbect(
					"android.app.ActivityThread$ProviderClientRecord",
					providerClientRecord, "mLocalProvider");
			if (null == localProvider)
			{
				continue;
			}
			//LogUtils.log("localProvider = "+localProvider);
			//LogUtils.log("-- localProvider = "+localProvider+", app = "+app);
			RefInvoke.setFieldOjbect("android.content.ContentProvider",
					"mContext", localProvider, app);
		}
		// 鍚姩婧怉pk鐨凙pplication
		app.onCreate();
		
		// 鏍￠獙
		//Dec.getInstance(this).verify((Object)this);
		//LogUtils.log("ProxyApplication onCreate end");
	}
	
	private String getAppName()
	{
		File appInfo = new File(appNamePath);
		if (null == appInfo || !appInfo.exists())
		{
			return null;
		}
		String appName = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(appInfo));
			appName = br.readLine();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//LogUtils.log("搴旂敤绋嬪簭鍖呭悕 - "+appName);
		if (null == appName) {
			//LogUtils.log("appName  is null");
		}
		return appName;
	}
	
	/**
	 * 锟酵放憋拷锟接壳碉拷apk锟侥硷拷锟斤拷so锟侥硷拷
	 * @param data
	 * @throws IOException
	 */
	private void splitPayLoadFromDex(byte[] apkdata) throws IOException {
		//LogUtils.log("---- splitPayLoadFromDex ---");
		int ablen = apkdata.length;
		// 鍏堣В鍑烘簮Apk鐨凙pplicationName
		byte[] appNamelen = new byte[4];
		System.arraycopy(apkdata, ablen - 4, appNamelen, 0, 4);
		ByteArrayInputStream bais = new ByteArrayInputStream(appNamelen);
		DataInputStream in = new DataInputStream(bais);
		int appNameByteLen = in.readInt();
        byte[] appNameByte = new byte[appNameByteLen];
		System.arraycopy(apkdata, ablen - 4 - appNameByteLen, appNameByte, 0, appNameByteLen);
		String appName = new String(appNameByte);
		mAppName = appName;
		//LogUtils.log("瑙ｅ嚭  搴旂敤绋嬪簭鍖呭悕 - "+mAppName);
		// 瑙ｅ嚭婧恆pk鐨刣ex鏂囦欢
		//LogUtils.log("鍏堣鍑篸ex...");
		byte[] dexlen = new byte[4];
		System.arraycopy(apkdata, ablen - 8 - appNameByteLen, dexlen, 0, 4);
		bais = new ByteArrayInputStream(dexlen);
		in = new DataInputStream(bais);
		int apkByteLen = in.readInt();
		byte[] newdex = new byte[apkByteLen];
		System.arraycopy(apkdata, ablen - 8-  appNameByteLen- apkByteLen, newdex, 0, apkByteLen);
		// 婧恆pk 鐨刣ex鏁版嵁瑙ｅ瘑
		//LogUtils.log("鍑嗗瑙ｅ瘑dex...");
		//LogUtils.log("瑙ｅ瘑鍓峝ex澶у皬 锛�"+newdex.length);
		newdex = decrypt(newdex);
		//LogUtils.log("瑙ｅ瘑鍚巇ex澶у皬 锛�"+newdex.length);
		File file = new File(dexFileName);
		try {
			FileOutputStream localFileOutputStream = new FileOutputStream(file);
			localFileOutputStream.write(newdex);
			localFileOutputStream.flush();
			localFileOutputStream.close();
			//LogUtils.log("dex size:"+file.length());
		} catch (IOException localIOException) {
			//LogUtils.log("鍐欏叆鍘焌pk鐨刣ex澶辫触:"+localIOException.getLocalizedMessage());
			throw new RuntimeException(localIOException);
			
		}
		decSoFile();
		// 瑙ｅ瘑so鏂囦欢锛坅ssets/sos鐩綍涓嬶級
//		new Thread(new  Runnable()
//		{
//			@Override
//			public void run() {
//				//LogUtils.log("绾跨▼閲岃В瀵唖o鏂囦欢...");
//				decSoFile();
//			}
//		}).start();
	}
	
	private void decSoFile() {
		// 瑙ｅ瘑so鏂囦欢锛坅ssets/sos鐩綍涓嬶級
		//LogUtils.log("瑙ｅ瘑so鏂囦欢锛坅ssets/sos鐩綍涓嬶級");
		try {
			AssetManager am = getAssets();
			String[] files = am.list("sos");
			if (null == files || files.length <= 0)
			{
				//LogUtils.log("assets/sos鏃犲姞瀵嗙殑so鏂囦欢");
				return ;
			}
			File libRoot = new File(libPath);
			for (String s : files) {
				if (s.endsWith(".sos")) {
					if (CPU_ABI == null || !s.contains(CPU_ABI)) {
						continue;
					}
					//LogUtils.log("瑙ｅ瘑锛�"+s);
					//DataInputStream dis = new DataInputStream(am.open("sos/"+ s));
					BufferedInputStream bis = new BufferedInputStream(am.open("sos/"
							+ s));
					File zip = new File(libRoot.getPath() + "/"
							+ s.replace(".sos", ".zip"));
					DataOutputStream dos = new DataOutputStream(
							new FileOutputStream(zip));
					ByteArrayOutputStream byteOS = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024*8];
					int length = -1;
					while (-1 != (length = bis.read(buffer))) {
						byteOS.write(buffer, 0, length);
					}
					byteOS.flush();
					bis.close();
					byteOS.close();
					byte[] encData = byteOS.toByteArray();
					//LogUtils.log("鍑嗗瑙ｅ瘑...");
					byte[] decData = decrypt(encData);
					//LogUtils.log("瑙ｅ瘑瀹屾垚");
					dos.write(decData);
					dos.flush();
					dos.close();
					//LogUtils.log("鍑嗗瑙ｅ帇");
					FileHelper.unzipFile(zip);
					zip.delete();
					//LogUtils.log("瑙ｅ帇瀹屾垚");
				}
			}
		} catch (IOException e) {
               //LogUtils.log("瑙ｅ瘑so鏂囦欢澶辫触锛�"+e.getLocalizedMessage());
		}
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
	 * 锟斤拷apk锟斤拷锟斤拷锟斤拷锟饺ex锟侥硷拷锟斤拷锟捷ｏ拷byte锟斤拷
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
				dexByteArrayOutputStream.flush();
			}
			localZipInputStream.closeEntry();
		}
		localZipInputStream.close();
		return dexByteArrayOutputStream.toByteArray();
	}


	// //直锟接凤拷锟斤拷锟斤拷锟捷ｏ拷锟斤拷锟竭匡拷锟斤拷锟斤拷锟斤拷约锟斤拷锟斤拷芊锟斤拷锟�
	private byte[] decrypt(byte[] srcdata) {
        return Dec.getInstance(getApplicationContext()).dec(srcdata);
	}
	
	
	private static boolean isOverSDK19()
	{
		//LogUtils.log("Build.VERSION.SDK_INT = "+Build.VERSION.SDK_INT);
		return Build.VERSION.SDK_INT>=19;
	}
	
	
	
	/*
	protected AssetManager mAssetManager;//锟斤拷源锟斤拷锟斤拷锟斤拷
	protected Resources mResources;//锟斤拷源  
	protected Theme mTheme;//锟斤拷锟斤拷  
	
	protected void loadResources(String dexPath) {  
		//LogUtils.log("loadResources enter");
        try {  
            AssetManager assetManager = AssetManager.class.newInstance();  
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);  
            //LogUtils.log(" Method addAssetPath + "+ addAssetPath);
            addAssetPath.invoke(assetManager, dexPath);
           // int ret = Integer.parseInt((String)(addAssetPath.invoke(assetManager, dexPath)));  
           // //LogUtils.log(" Method addAssetPath ret = + "+ ret);
            mAssetManager = assetManager;  
            
        } catch (Exception e) {  
        	//LogUtils.log("loadResource error:"+Log.getStackTraceString(e));
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
		//LogUtils.log("getAssets - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mAssetManager == null ? super.getAssets() : mAssetManager;  
	}  
	
	@Override  
	public Resources getResources() {  
		//LogUtils.log("getResources - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mResources == null ? super.getResources() : mResources;  
	}  
	
	@Override  
	public Theme getTheme() {  
		//LogUtils.log("getResources - mAssetManager is null ?" +((mAssetManager == null)?true:false));
	    return mTheme == null ? super.getTheme() : mTheme;  
	} 
	*/
}
