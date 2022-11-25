package com.shell.main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileHelper {

	
	
	public static byte[] readFileBytes(File file) throws IOException {
		byte[] arrayOfByte = new byte[1024];
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		FileInputStream fis = new FileInputStream(file);
		while (true) {
			int i = fis.read(arrayOfByte);
			if (i != -1) {
				localByteArrayOutputStream.write(arrayOfByte, 0, i);
			} else {
				fis.close();
				localByteArrayOutputStream.close();
				return localByteArrayOutputStream.toByteArray();
			}
		}
	}
	
	public static String getString(InputStream is) throws IOException {
		if (is == null)
		{
			return null;
		}
		BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String info;
		while (null != (info = bis.readLine())) {
			sb.append(info + "\n");
		}
		bis.close();
		return sb.toString();
	 }
	
	
	public static void copyFolderToFolder(File srcFile,File desFile)
	{
		if (!(srcFile.isDirectory() && desFile.isDirectory()))
		{
			return ;
		}
		if (!srcFile.exists())
		{
			return;
		}
		if (!desFile.exists())
		{
			desFile.mkdirs();
		}
		String root = desFile.getPath()+"/"+srcFile.getName();
		File rootDir = new File(root);
		if (!rootDir.exists())
		{
			rootDir.mkdirs();
		}
		
		File[] flist = srcFile.listFiles();
		if (null == flist)
		{
			return ;
		}
		File file = null;
		for (int i = 0; i< flist.length; i++)
		{
			file = flist[i];
			if (file.isFile())
			{// 文件
				copyFileToFolder(file, rootDir);
			}else if (file.isDirectory())
			{// 文件夹
				copyFolderToFolder(file,rootDir);
			}
		}
		
	}
	
	public static void copyFileToFolder(File srcFile,File desDir)
	{
		if (!srcFile.exists())
		{
			return;
		}
		if (!desDir.isDirectory())
		{
			return;
		}
		if (!desDir.exists())
		{
			desDir.mkdirs();
		}
		
		String newFilePath = desDir.getPath()+"/"+srcFile.getName();
		//System.out.println("newFilePath = "+newFilePath);
		File newFile = new File(newFilePath);
		
		if (newFile.exists())
		{
			newFile.delete();
		}
		try {
			newFile.createNewFile();
			FileOutputStream fos =  new FileOutputStream(newFile);
			Files.copy(srcFile.toPath(),fos );
			fos.close();
//			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
//			BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(newFile));
//			byte[] buffer = new byte[1024*8];
//			int length = 0;
//			while(-1 != (length = bis.read(buffer)))
//			{
//				bos.write(buffer, 0, length);
//			}
//			bis.close();
//			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static String unzipFile(File file)
	{
		if (!file.exists())
		{
			return null;
		}
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(file);
			if (zipFile == null)
				return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dir  = null;
		if (file.getName().contains("."))
		{
			dir = file.getPath().substring(0, file.getPath().lastIndexOf("."));
		}else
		{
			dir = file.getPath();
		}
		
		File folder = new File(dir);
		if (folder.exists())
		{
			folder.delete();
		}
		folder.mkdirs();
		
		File tempFile = null;
		Enumeration zipEntries = zipFile.entries();
		for (;zipEntries.hasMoreElements();)
		{
			ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
			String name = zipEntry.getName();
			tempFile = new File(dir+"/"+name);
			//System.out.println("name = "+tempFile.getPath());
			String parent = tempFile.getParent();
			if (!new File(parent).exists())
			{
				new File(parent).mkdirs();
			}
			
			if (new File(name).isDirectory())
			{
				continue;
			}
			
			try {
				tempFile.createNewFile();
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(tempFile);
				byte[] buffer = new byte[1024];
				int length = -1;
				while(-1 != (length = is.read(buffer)))
				{
				   fos.write(buffer, 0, length);
				}
				is.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dir;
	}

	
	
	public static void zipDir(File dir,String basedir, ZipOutputStream zos)
	{
		if (!dir.exists())
		{
			return ;
		}
		File[] files = dir.listFiles();
		for (File f : files)
		{
			if (f.isDirectory())
			{
				zipDir(f, basedir+f.getName()+"/", zos);
			}else if (f.isFile())
			{
				zipFile(f, basedir, zos);
			}
			
		}
	}
	
	
	
	public static void zipFile(File file, String basedir, ZipOutputStream zos) {

		ZipEntry zipEntry = new ZipEntry(basedir + file.getName());
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			zos.putNextEntry(zipEntry);
			byte[] buffer = new byte[1024];
			int length = -1;
			while (-1 != (length = fis.read(buffer))) {
				zos.write(buffer, 0, length);
			}
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void deleteDir(File file, boolean keepDir )
	{
		if (null == file || !file.exists() || !file.isDirectory())
		{
			return ;
		}
		File[] subFiles = file.listFiles();
		for (File f : subFiles)
	    {
	    	if (f.isFile())
	    	{
	    		f.delete();
	    	}else if (f.isDirectory())
	    	{
	    		deleteDir(f,false);
	    	}
	    }
		if (!keepDir)
		{
			file.delete();
		}
	}

	/**
	 * 删除文件夹，根目录也删除
	 * @param file
	 */
	public static void deleteFolder(File file)
	{
		deleteDir(file, false);
	}
	
	/**
	 * 清空文件夹，根目录不删除
	 * @param file
	 */
	public static void clearFolder(File file)
	{
		deleteDir(file, true);
	}
	/**
	 * (递归)删除目录下指定类型后缀的文件
	 * @param folder
	 * @param suffix 后缀类型
	 */
	public static void deleteFileIn(File folder,String[] suffixs, boolean recursive)
	{
		if (null == folder || !folder.exists() || null == suffixs || suffixs.length == 0)
		{
			return ;
		}
		File[] subFiles = folder.listFiles();
		for (File f: subFiles)
		{
			if (f.isFile())
			{
				for (String suffix: suffixs)
				{
				    if (f.getName().endsWith(suffix))
					{
				    	LogUtils.i("delete "+f.getName());
						f.delete();
						break;
					}
				}
			}
			if (recursive)
			{
				if (f.isDirectory())
				{
					deleteFileIn(f, suffixs, true);
				}
			}
		}
	}

	/**
	 * 用 desFile 替换  srcFile 
	 * @param srcFile 
	 * @param desFile
	 * @return
	 */
	public static boolean replaceFile(File srcFile, File desFile)
	{
		if (null == srcFile || !srcFile.exists() || !srcFile.isFile() || null == desFile || !desFile.exists() || !desFile.isFile())
		{
			return false;
		}
		File dir = srcFile.getParentFile();
		srcFile.delete();
		copyFileToFolder(desFile, dir);
		return true;
	}
	
	/**
	 * 递归获取file里所有文件
	 * @param file
	 * @return
	 */
	public static File[] getSubFile(File file)
	{
		ArrayList<File> files = new ArrayList<File>();
		getFiles(file, files,null);
		return files.toArray(new File[files.size()]);
	}
	
	public static File[] getSubFile(File file,String[] suffixs)
	{
		ArrayList<File> files = new ArrayList<File>();
		getFiles(file, files,suffixs);
		return files.toArray(new File[files.size()]);
	}
	
	
	
	private static void getFiles(File file,List<File> files,String[] suffixs)
	{
		if (null == file || !file.exists() || null == files)
		{
			return ;
		}
		if (file.isFile())
		{
			if (null == suffixs)
			{
				files.add(file);
			}else
			{
				for (String suffix : suffixs)
				{
					if (file.getName().endsWith(suffix))
					{
						files.add(file);
						break;
					}
				}
			}
		}else if (file.isDirectory())
		{
			File[] subFiles = file.listFiles();
			for (File f: subFiles)
			{
				getFiles(f, files,suffixs);
			}
		}
	}
	
	public static boolean getUpxso(File upxTool,File soFile,File upxSoFile)
	{
		if (null == soFile || !soFile.exists())
		{
			return false;
		}
		if (null == upxSoFile )
		{
			return false;
		}
		boolean sameName = soFile.getPath() == upxSoFile.getPath();
		if (!sameName)
		{
			if (upxSoFile.exists())
			{
				upxSoFile.delete();
			}
		}
		LogUtils.i("soFile = "+soFile +"\n upxSoFile = "+upxSoFile+"\n ,same = "+sameName);
		String outPath = sameName ? upxSoFile.getPath()+"_upx" : upxSoFile.getPath();
		//String cmdFormat1 = "chmod 755 %s";
		//String cmd1 = String.format(cmdFormat1,  upxTool.getAbsolutePath());
		String cmdFormat2 = "%s -o %s %s";
		String cmd2 = String.format(cmdFormat2, upxTool.getPath(),
				outPath, soFile.getPath());
		try {
			Process pro = Runtime.getRuntime().exec(cmd2);
			InputStream is = pro.getInputStream();
			InputStream err = pro.getErrorStream();
			int code = pro.waitFor();
			LogUtils.i("getUpxso code = "+code);
			
			LogUtils.i(" in :so = "+soFile.getPath()+":\n"+FileHelper.getString(is));
			
			LogUtils.i(" err:so = "+soFile.getPath()+":\n"+FileHelper.getString(err));
			
			if (sameName)
			{
				LogUtils.i("delete so :"+soFile.getPath());
				soFile.delete();
				new File(outPath).renameTo(soFile);
			}
			
			
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
