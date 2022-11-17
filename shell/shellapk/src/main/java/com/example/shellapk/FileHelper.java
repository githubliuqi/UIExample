package com.example.shellapk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
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
	
	public static void writeStreamToFile(InputStream is,File file)
	{
		if (null == file || !file.isFile())
		{
			return ;
		}
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BufferedInputStream bis = new BufferedInputStream(is);
		
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			byte[] buffer = new byte[1024*8];
			int length = -1;
			while (-1 != (length = bis.read(buffer))) {
				bos.write(buffer, 0, length);
			}
			bos.flush();
			bis.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			//FileOutputStream fos =  new FileOutputStream(newFile);
//			Files.copy(srcFile.toPath(),fos );
//			fos.close();
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
			BufferedOutputStream bos= new BufferedOutputStream(new FileOutputStream(newFile));
			byte[] buffer = new byte[1024*8];
			int length = 0;
			while(-1 != (length = bis.read(buffer)))
			{
				bos.write(buffer, 0, length);
			}
			bos.flush();
			bis.close();
			bos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static String unzipFile(File file,File unzipDir)
	{
		if (!file.exists())
		{
			return null;
		}
		if (!unzipDir.exists())
		{
			unzipDir.mkdirs();
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
		
		File tempFile = null;
		Enumeration zipEntries = zipFile.entries();
		for (;zipEntries.hasMoreElements();)
		{
			ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
			String name = zipEntry.getName();
			tempFile = new File(unzipDir.getPath()+"/"+name);
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
				fos.flush();
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
		return unzipDir.getPath();
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
		
		//String dir = file.getPath().substring(0, file.getPath().lastIndexOf("."));
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
				fos.flush();
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
			zos.flush();
			fis.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static void deleteFolder(File file)
	{
		if (!file.exists())
		{
			return ;
		}
		if (!file.isDirectory())
		{
			return;
		}
	    File[] files = file.listFiles();
	    for (File f : files)
	    {
	    	if (f.isFile())
	    	{
	    		f.delete();
	    	}else if (f.isDirectory())
	    	{
	    		deleteFolder(f);
	    	}
	    }
	    file.delete();
	}
}
