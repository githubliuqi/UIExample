package com.shell.main;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.dongliu.apk.parser.ApkParser;


public class ShellTool {
	
	
	static byte[] pbKey = {0x37,0x6A,0x64,0x6B,0x61,0x66,0x76,0x51,0x34,0x2D,0x57,0x45,
		    0x2E,0x2F,0x31,0x47,0x48,0x70,0x3D,0x41,0x64,0x67,0x78,0x67};
	static byte[] pbIV = new byte[0x08];
	
	// 最终生成加壳apk的文件路径
	//private static final String SHELLED_APK_PATH = "file/ShelledApk.apk";
	// 解壳apk文件路径，该apk将被替换dex文件，从而生成加壳apk ShelledApk.apk
	//private static final String UNSHELLED_APK_PATH = "file/ReforceApk.apk";
	//新生成的加壳dex文件路径
	private static final String SHELLED_DEX_PATH = "temp/classes.dex";
	
	
	private static final byte[] ENC_KEY = {0x37,0x6A,0x64,0x6B,0x61,0x66,0x76,0x51,0x34,0x2D,0x57,0x45,
	    0x2E,0x2F,0x31,0x47,0x48,0x70,0x3D,0x41,0x64,0x67,0x78,0x67};
	
	private static final int OS_TYPE_UNKNOWN = 0x00; // 未知操作系统
	private static final int OS_TYPE_WINDOWS = 0x10; // windows系统，其他子系统可以扩展，暂不考虑
	public static final int OS_TYPE_MAC     = 0x20; // mac系统
	
	// 当前系统类型
    private static int CURRENT_OS_TYPE = OS_TYPE_UNKNOWN; // 当前系统类型
	
	public static String makeShell(String apkPath, String unshellDex, String appName)
	{
		LogUtils.i("makeShell enter -\n apkPath = "+apkPath+"\n appName = "+appName);
		if (apkPath == null || appName == null)
		{
			LogUtils.i("apkPath or appName is null");
			return null;
		}
		String apkName = apkPath.substring(apkPath.lastIndexOf("/")+1);
		//LogUtils.log("apk name = "+ apkName);
		byte[] applicationName = appName.getBytes();
		try {
			
			File payloadSrcFile = new File(apkPath);  
			//File unShellDexFile = new File("input/classes.dex");	
			File unShellDexFile = new File(unshellDex);	
			byte[] payloadArray = encrpt(FileHelper.readFileBytes(payloadSrcFile));
			byte[] unShellDexArray = FileHelper.readFileBytes(unShellDexFile);
			int payloadLen = payloadArray.length;
			int unShellDexLen = unShellDexArray.length;
			int appNameLen = applicationName.length;
			int totalLen = payloadLen + unShellDexLen +appNameLen +8;
			byte[] newdex = new byte[totalLen]; 
			// 合并数据
			// 解壳程序dex
			System.arraycopy(unShellDexArray, 0, newdex, 0, unShellDexLen);
			// apk
			System.arraycopy(payloadArray, 0, newdex, unShellDexLen, payloadLen);
			// apk长度数据
			System.arraycopy(intToByte(payloadLen), 0, newdex, unShellDexLen+payloadLen, 4);
            // apk application name
			System.arraycopy(applicationName, 0, newdex, unShellDexLen+payloadLen+4,appNameLen );
			 // apk application name length
			System.arraycopy(intToByte(appNameLen), 0, newdex, unShellDexLen+payloadLen+appNameLen+4,4 );
			// 修改加壳后dex头信息
			fixFileSizeHeader(newdex);
			fixSHA1Header(newdex);
			fixCheckSumHeader(newdex);
			// 重新生成classes.dex文件
			File file = new File(SHELLED_DEX_PATH);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileOutputStream localFileOutputStream = new FileOutputStream(file);
			localFileOutputStream.write(newdex);
			localFileOutputStream.flush();
			localFileOutputStream.close();

			return SHELLED_DEX_PATH;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//LogUtils.log("makeShell end");
		return null;
	}
	

	
	
	/*
	 * 加密apk
	 * 
	 */
	public static byte[] encrpt(byte[] srcdata){
		//return srcdata;
		byte[] ret = null;
		try {
			LogUtils.i("准备加密...");
			LogUtils.i("加密前apk大小："+srcdata.length);
			ret= DES.enc(DES.DES112, DES.CBC, pbKey, pbIV, srcdata);
			LogUtils.i("加密后apk大小："+ret.length);
			return ret;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改checksum属性
	 * @param dexBytes
	 */
	private static void fixCheckSumHeader(byte[] dexBytes) {
		Adler32 adler = new Adler32();
		adler.update(dexBytes, 12, dexBytes.length - 12);
		long value = adler.getValue();
		int va = (int) value;
		byte[] newcs = intToByte(va);
		
		byte[] recs = new byte[4];
		for (int i = 0; i < 4; i++) {
			recs[i] = newcs[newcs.length - 1 - i];
			LogUtils.i(Integer.toHexString(newcs[i]));
		}
		System.arraycopy(recs, 0, dexBytes, 8, 4);
		LogUtils.i(Long.toHexString(value));
		
	}


	/**
	 * int תbyte[]
	 * @param number
	 * @return
	 */
	public static byte[] intToByte(int number) {
		byte[] b = new byte[4];
		for (int i = 3; i >= 0; i--) {
			b[i] = (byte) (number % 256);
			number >>= 8;
		}
		return b;
	}

	/**
	 *修改SHA1属性ֵ
	 *  @param dexBytes
	 * @throws NoSuchAlgorithmException
	 */
	private static void fixSHA1Header(byte[] dexBytes)
			throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(dexBytes, 32, dexBytes.length - 32);
		byte[] newdt = md.digest();
		System.arraycopy(newdt, 0, dexBytes, 12, 20);
		String hexstr = "";
		for (int i = 0; i < newdt.length; i++) {
			hexstr += Integer.toString((newdt[i] & 0xff) + 0x100, 16)
					.substring(1);
		}
		LogUtils.i(hexstr);
	}

	/**
	 *修改filesize属性 ֵ
	 * @param dexBytes
	 */
	private static void fixFileSizeHeader(byte[] dexBytes) {
		byte[] newfs = intToByte(dexBytes.length);
		LogUtils.i(Integer.toHexString(dexBytes.length));
		byte[] refs = new byte[4];
		for (int i = 0; i < 4; i++) {
			refs[i] = newfs[newfs.length - 1 - i];
			LogUtils.i(Integer.toHexString(newfs[i]));
		}
		System.arraycopy(refs, 0, dexBytes, 32, 4);
	}

	
	 
	 /**
	  * 获取当前系统类型
	  * @return
	  */
	 public static int getOSType()
	 {
		 if (CURRENT_OS_TYPE != OS_TYPE_UNKNOWN)
		 {
			 return CURRENT_OS_TYPE;
		 }
		 String osName = System.getProperty("os.name");
		 LogUtils.i("OS_NAME = "+osName);
		 // 详细的再处理吧
		 if (osName.equalsIgnoreCase("MAC OS X") || osName.equalsIgnoreCase("MAC OS"))
		 {
			 CURRENT_OS_TYPE = OS_TYPE_MAC;
		 }else if (osName.contains("Windows")) // Windows 7
		 {
			 CURRENT_OS_TYPE = OS_TYPE_WINDOWS;
		 }
		 return CURRENT_OS_TYPE;
	 }
	 
	 private static String getString(InputStream is) throws IOException {
		if (is == null)
		{
			return null;
		}
		BufferedReader bis = new BufferedReader(new InputStreamReader(is,"utf-8"));
		StringBuilder sb = new StringBuilder();
		String info;
		while (null != (info = bis.readLine())) {
			String s = new String(info.getBytes(),"utf-8");
			System.out.println(info+s+ "\n");
			sb.append(s + "\n");
		}
		return sb.toString();
	 }
	/**
	 * 获取JDK版本信息
	 * @return
	 */
	 public static String getJDKVersion()
	 {
		int osType = getOSType();
		String cmdPath = null;
		if (osType == OS_TYPE_UNKNOWN)
		{
			return null;
		}else if (osType == OS_TYPE_MAC)
		{
			cmdPath = "file/checkJDK.sh";
			try {
				Process p = Runtime.getRuntime().exec(cmdPath);
				String jdkVersion = getString(p.getInputStream());
				LogUtils.i("JDK version - "+jdkVersion);
				return jdkVersion;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (osType == OS_TYPE_WINDOWS)
		{
			cmdPath = "file/checkJDK.bat";
			try {
				Process p = Runtime.getRuntime().exec(cmdPath);
				String jdkVersion = getString(p.getInputStream());
				LogUtils.i("JDK version - "+jdkVersion); // 与MAC下返回不一致，后续再处理吧
				return jdkVersion;
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	 }
	 
	 /**
	  * 解析apk的AndroidManifest.xml文件
	  * @param apkPath
	  * @return
	  */
	public static String getMenifest(String apkPath) {
		
		ApkParser parser;
		try {
			parser = new ApkParser(apkPath);
			parser.setPreferredLocale(Locale.getDefault());
			String manifestXml = parser.getManifestXml();
			parser.close();
			// System.out.println(parser.getManifestXml());
			File file = new File("temp/AndroidManifest.xml");
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			fw.write(manifestXml);
			fw.close();
			return manifestXml;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}



//
// class File2HexExample {
//	 public static void convertToHex(PrintStream out, File file) throws IOException
//	 { 
//		 LogUtils.i("正在转换 - "+file.getAbsolutePath()+" ...");
//		 InputStream is = new FileInputStream(file);
//		 int bytesCounter = 0; 
//		 int value = 0;
//		 StringBuilder sbHex = new StringBuilder(); 
//		 StringBuilder sbText = new StringBuilder();
//		 StringBuilder sbResult = new StringBuilder(); 
//		while ((value = is.read()) != -1) { // 使用 "X" 格式将值转换为十六进制
//			sbHex.append(String.format("%02X ", value)); // 如果字符不可转换，那么打印符号“.”
//			if (!Character.isISOControl(value)) {
//				sbText.append((char) value);
//			} else {
//				sbText.append(".");
//			} // 如果读取了十六个字节，则重置计数器 //让 StringBuilder 更为清晰可读，添加空格
//			if (bytesCounter == 15) {
//				sbResult.append(sbHex).append(" ").append(sbText).append("\n");
//				sbHex.setLength(0);
//				sbText.setLength(0);
//				bytesCounter = 0;
//			} else {
//				bytesCounter++;
//			}
//		} // 如果还能得到内容
//		if (bytesCounter != 0) { // 为了可读性，添加更多空格
//			for (; bytesCounter < 16; bytesCounter++) { // 每个字符 3 个空格
//				sbHex.append(" ");
//			}
//			sbResult.append(sbHex).append(" ").append(sbText).append("\n");
//		}
//		out.print(sbResult);
//		is.close();
//	}
//// public static void main(String[] args) throws IOException { //将结果打印到控制台 
////	 convertToHex(System.out, new File("C:\\test.zip")); //将结果输出到文件 // 
////	 convertToHex(new PrintStream("c:/file.hex"), new File("c:/file.txt")); } 
////	 }
////		 
////	 }
//	  
 //}
