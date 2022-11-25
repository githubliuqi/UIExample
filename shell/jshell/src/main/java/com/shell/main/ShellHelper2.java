package com.shell.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EventListener;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.swing.SwingWorker;

import net.dongliu.apk.parser.struct.xml.XmlHeader;

public class ShellHelper2 {

	public ShellHelper2() {
		
	}

	public static void startShell(final String apkPath, final String appName
			, final ShellListener listener)
	{
		SwingWorker<Integer, Object> worker = new SwingWorker<Integer, Object>()
		{

			@Override
			protected Integer doInBackground() throws Exception {
				doShell(apkPath,appName);
				return null;
			}

			@Override
			protected void done() {
				// TODO Auto-generated method stub
				super.done();
				if (null != listener)
				{
					listener.didShell();
				}
			}
			
		};
		worker.execute();
	}
	
	class ShellThread extends Thread
	{
		@Override
		public void run() {
		//	SwingWorker<T, V>
		}
	}
	
	// 加壳事件监听
	interface ShellListener extends EventListener
	{
		public void WillShell();
		
		public void didShell();
		
	}
	
	// 加壳事件
	private static void doShell(String apkPath, String appName) {
		
		
	
		// 解压原apk
		LogUtils.i("解压原apk");
		String srcApkPath = FileHelper.unzipFile(new File(apkPath));
		File srcApk = new File(srcApkPath);
		
		//读取manifest
//		ShellTool.getMenifest(apkPath);
//		try {
//			appName = XmlHelper.getAppName(new FileInputStream("temp/AndroidManifest.xml"));
//		    LogUtils.i("appname = "+appName);
//		} catch (FileNotFoundException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		
		// 解压 解壳apk
		LogUtils.i("解压 解壳apk");
		String unshellApkPath =FileHelper.unzipFile(new File("input/ReforceApk.apk"));
		File unshellApk = new File(unshellApkPath);
		
		// 生成简化apk（只保留dex文件和lib文件夹）
		File sapk = new File("temp/"+srcApk.getName());
		if (sapk.exists())
		{
			FileHelper.deleteFolder(sapk);
		}
		sapk.mkdirs();
		FileHelper.copyFolderToFolder(new File(srcApk + "/lib"), sapk);
		FileHelper.copyFileToFolder(new File(srcApk + "/classes.dex"), sapk);
		// 压缩
		File sapkZip = new File(sapk+".apk");
		if (sapkZip.exists())
		{
			sapkZip.delete();
		}
		try {
			sapkZip.createNewFile();
			ZipOutputStream zzos =new ZipOutputStream(new FileOutputStream(sapkZip));
			FileHelper.zipDir(sapk, "", zzos);
			zzos.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		// 合成加壳dex文件
		LogUtils.i("合成加壳dex文件");
		String unshellDexPath = unshellApkPath+"/classes.dex";
		String shellDexPath = ShellTool.makeShell(sapkZip.getPath(),unshellDexPath,appName);
		//String shellDexPath = ShellTool.makeShell(apkPath,unshellDexPath,appName);
		//生成apk临时文件夹
		LogUtils.i("生成apk临时文件夹");
		String shellApk = "temp/"+srcApk.getName()+"_shell";
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
		LogUtils.i("添加其他文件");
		String[] items = srcApk.list();
		for (int i= 0; i< items.length;i++)
		{
			String item = items[i];
			//System.out.println("item = "+item);
			if(item.equals("AndroidManifest.xml") || item.equals("lib") || item.equals("classes.dex") || item.equals("META-INF"))
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
		}
		
		
		//copy apk到原apk目录
		FileHelper.copyFileToFolder(apkFile, srcApk.getParentFile());
		
		// 删除临时文件
		LogUtils.i(" 删除临时文件");
		FileHelper.deleteFolder(srcApk);// 原apk的解压文件
		FileHelper.deleteFolder(unshellApk); // 解壳apk的解压文件夹
		FileHelper.deleteFolder(file);// 用于压缩后生成apk的临时文件夹
		FileHelper.deleteFolder(sapk); 
		sapkZip.delete();
		new File(shellDexPath).delete();// 合成dex
		apkFile.delete(); // apk
	}
	
}
