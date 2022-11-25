package com.shell.main;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.EventListener;
import java.util.zip.ZipOutputStream;

public class ShellHelper {

	private static final String SHELL_APK_PATH="input/shell/ReforceApk.apk";
	private static final String UPX_TOOL= "file/upx.out";
	
	public ShellHelper() {
		
	}

	public static void startShell(final String apkPath,boolean isSupportUPX, final ShellListener listener)
	{
		new ShellThread(apkPath, isSupportUPX,listener).start();
	}
	
	static class ShellThread extends Thread
	{
		ShellListener listener;
		String apkPath;
		boolean isSupportUPX= false;
		public ShellThread(String apkPath,boolean isSupportUPX, ShellListener listener)
		{
			this.apkPath = apkPath;
			this.listener = listener;
			this.isSupportUPX = isSupportUPX;
		}
		@Override
		public void run() {
		    if (null != listener)
		    {
		    	listener.WillShell();
		    }
		   String result = doShell(apkPath,isSupportUPX);
		   if (null != listener)
		   {
			  listener.shellFinished(result);
		   }
		}
	}
	
	// 加壳事件监听
	interface ShellListener extends EventListener
	{
		public void WillShell();
		
		public void shellFinished(String message);
		
	}
	
	// 加壳事件
	private static String doShell(String apkPath ,boolean isSupportUPX) {
		
		if (null == apkPath || !new File(apkPath).exists() || !apkPath.endsWith(".apk"))
		{
			return "请选择apk!";
		}
		// 反编译原apk
		String apkSimpleName = new File(apkPath).getName().replace(".apk", "");
		File apkDecodeFile = new File("temp/"+apkSimpleName); 
		boolean ok = APKHelper.decodeApk(new File(apkPath),apkDecodeFile);
		
		if (!ok)
		{
			FileHelper.deleteFolder(apkDecodeFile);
			return "反编译原apk失败";
		}
		// 获取原apk application
		String srcAppName = XmlHelper.getApplicationName(new File("temp/"+apkSimpleName+"/AndroidManifest.xml"));
		LogUtils.i("反编译出原apk的ApplicationName ："+srcAppName);
		if (null == srcAppName)
		{
			FileHelper.deleteFolder(apkDecodeFile);
			return "反编译原apk的ApplicationName失败";
		}
		// 解压原apk
		LogUtils.i("解压原apk");
		String srcApkPath = null;
		File srcApk = null;
		try {
			srcApkPath = FileHelper.unzipFile(new File(apkPath));
			srcApk = new File(srcApkPath);
		} catch (Exception e) {
			FileHelper.deleteFolder(apkDecodeFile);
			FileHelper.deleteFolder(srcApk);
			return "解压原apk失败";
		}
		
		if (!new File(SHELL_APK_PATH).exists())
		{
			LogUtils.i("自行编译生成解壳apk");
			
			// 检查文件
			String checkFileResult = checkFile();
			if (null != checkFileResult )
			{
				FileHelper.deleteFolder(apkDecodeFile);
				FileHelper.deleteFolder(srcApk);
				return checkFileResult;
			}
			// 替换.9.png ,decode得到的.9.png格式不严格，无法打包资源
			File[] resFiles = FileHelper.getSubFile(new File(apkDecodeFile.getPath()+"/res"),new String[]{".9.png",".9.PNG"});
			File temp9Patch = new File("input/shell/temp.9.png");
			if (null != resFiles && resFiles.length >0 )
			{
				for (File res: resFiles)
				{
					FileHelper.replaceFile(res, temp9Patch);
					File f = new File(res.getParent()+"/"+temp9Patch.getName());
					f.renameTo(res);
				}
			}
			// 生成解壳apk
			ok = APKHelper.buildApk( new File("temp/"+apkSimpleName),SHELL_APK_PATH,isSupportUPX);
			if (!ok)
			{
				FileHelper.deleteFolder(apkDecodeFile);
				FileHelper.deleteFolder(srcApk);
				new File(SHELL_APK_PATH).delete();
				return "生成解壳apk失败";
			}
			LogUtils.i(" 生成解壳apk");
		}else
		{
			LogUtils.i("已存在解壳apk");
			FileHelper.deleteFolder(new File("temp/"+apkSimpleName));
		}
		
		// 解压 解壳apk
		LogUtils.i("解压 解壳apk");
		String unshellApkPath = null;
		File unshellApk = null;
		try
		{
		   unshellApkPath =FileHelper.unzipFile(new File(SHELL_APK_PATH));
		   unshellApk = new File(unshellApkPath);
		}catch (Exception e)
		{
			return "解压 解壳apk失败";
		}
		
		// 加密原apk的dex文件，合成加壳dex文件
		LogUtils.i("合成加壳dex文件");
		String unshellDexPath = unshellApkPath+"/classes.dex";
		String shellDexPath = ShellTool.makeShell(srcApkPath+"/classes.dex",unshellDexPath,srcAppName);
		//String shellDexPath = ShellTool.makeShell(apkPath,unshellDexPath,appName);
		
		// upx 对so文件加壳
		if (isSupportUPX)
		{
			File[] soFiles = FileHelper.getSubFile(new File(srcApkPath + "/lib"), new String[]{".so"});
			if (null != soFiles && soFiles.length >0)
			{
				File upx = new File(UPX_TOOL);
				for (File f : soFiles) {
				  boolean b = FileHelper.getUpxso(upx, f, f);
				  if (!b)
				  {
					  return "upx 对so文件加壳 ERROR";
				  }
				}
			}
		}
		// 加密原apk的so文件,按ABI平台文件夹压缩后加密
		LogUtils.i("加密原apk的so文件,按ABI平台文件夹压缩后加密");
		File[] srcSubFiles = new File(srcApkPath + "/lib").listFiles();
		if (null != srcSubFiles && srcSubFiles.length > 0)
		{
			try {
				
				for (File f : srcSubFiles) {
					if (!f.isDirectory()) {
						continue;
					}
					String name = f.getName();
					File zip = new File(f.getPath() + ".temp");
					LogUtils.i("压缩" + name);
					if (!zip.exists()) {
						zip.createNewFile();
					}
					ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(
							zip));
					FileHelper.zipDir(f, "", zos);
					zos.close();
					// 加密
					LogUtils.i("加密" + name);
					byte[] data = ShellTool.encrpt(FileHelper.readFileBytes(zip));
					File sos = new File(f.getPath() + ".sos");
					DataOutputStream dos = new DataOutputStream(
							new FileOutputStream(sos));
					dos.write(data);
					dos.close();
				}
			} catch (IOException e) {
	              LogUtils.i("加密so文件失败");
	              return "加密so文件失败";
			}
		}
		
		//生成apk临时文件夹
		LogUtils.i("生成apk临时文件夹");
		String shellApk = isSupportUPX ?"temp/"+srcApk.getName()+"_upx_shell": "temp/"+srcApk.getName()+"_shell";
		File file = new File(shellApk);
		if (file.exists())
		{
			file.delete();
		}
		file.mkdir();
		
		// 添加 dex文件
		LogUtils.i("添加 dex文件");
		FileHelper.copyFileToFolder(new File(shellDexPath), file);
		// 添加AndroidManifest.xml
		LogUtils.i("添加AndroidManifest.xml");
		FileHelper.copyFileToFolder(new File(unshellApkPath+"/AndroidManifest.xml"), file);
		// 添加lib目录
		LogUtils.i("添加lib目录");
		FileHelper.copyFolderToFolder(new File(unshellApkPath+"/lib"), file);
		// 添加res
		//FileHelper.copyFolderToFolder(new File(unshellApkPath+"/res"), file);
		// 添加resources.arsc
		//FileHelper.copyFileToFolder(new File(unshellApkPath+"/resources.arsc"), file);
		// 添加 assets
		//FileHelper.copyFolderToFolder(new File(unshellApkPath+"/assets"), file);
		
		LogUtils.i("添加其他文件");
		String[] filterItems = {"AndroidManifest.xml","lib","classes.dex","META-INF"};
		String[] items = srcApk.list();
		for (int i= 0; i< items.length;i++)
		{
			String item = items[i];
			//System.out.println("item = "+item);
			if (Arrays.asList(filterItems).contains(item))
			{
				continue;
			}
			File temp = new File(srcApkPath+"/"+item);
			if (temp.isDirectory())
			{
				FileHelper.copyFolderToFolder(temp, file);
			}else if (temp.isFile())
			{
				FileHelper.copyFileToFolder(temp, file);
			}
		}
		
		
		// 添加加密的so文件到assets目录
		LogUtils.i("添加加密的so文件到assets目录");

		File assets = new File(shellApk+"/assets/sos");
		srcSubFiles = new File(srcApkPath + "/lib").listFiles();
		if (null != srcSubFiles && srcSubFiles.length > 0)
		{
			if (!assets.exists())
			{
				assets.mkdirs();
			}
			for (File f :srcSubFiles)
			{
				if (f.isFile() && f.getName().endsWith(".sos"))
				{
					LogUtils.i("添加"+f);
					FileHelper.copyFileToFolder(f, assets);
				}
			}
		}
		
		
		// 压缩文件,得到apk
		LogUtils.i("压缩文件,得到apk");
		String shellApkZip = shellApk+".apk";
		File apkFile = new File(shellApkZip);
		if (apkFile.exists())
		{
			apkFile.delete();
		}
		try {
			apkFile.createNewFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ZipOutputStream zos;
		try {
			zos = new ZipOutputStream(new FileOutputStream(apkFile));
			FileHelper.zipDir(file, "", zos);
			zos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "压缩文件,得到加壳apk失败";
		}
		
		
		//copy apk到原apk目录
		FileHelper.copyFileToFolder(apkFile, srcApk.getParentFile());
		
		// 删除临时文件
		LogUtils.i(" 删除临时文件");
		FileHelper.deleteFolder(srcApk);// 原apk的解压文件
		FileHelper.deleteFolder(unshellApk); // 解壳apk的解压文件夹
		FileHelper.deleteFolder(file);// 用于压缩后生成apk的临时文件夹
		new File(shellDexPath).delete();// 合成dex
		apkFile.delete(); // apk
		new File(SHELL_APK_PATH).delete();
		return "加壳成功";
	}
	
	
	
	
	private static String checkFile()
	{
		File libs = new File("input/shell/libs");
		if (!libs.exists())
		{
			return "壳apk的lib库缺失";
		}
		if (!new File("input/shell/classes.dex").exists())
		{
			return "壳apk的classes.dex文件缺失";
		}
		if (!new File("input/shell/temp.9.png").exists())
		{
			return "临时文件temp.9.png缺失";
		}
		return null;
	}
	
	
}
