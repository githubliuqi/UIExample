package com.shell.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LogUtils {

	private static boolean openLog = true;
	private static boolean simpleLog = false;
	private static boolean writeToFile = true;
	public static void i(String log)
	{
		if (openLog)
		{
			if (simpleLog)
			  System.out.println("LogI - "+log);
			else
				System.out.println("LogI - "+getTime() + " "+getLineInfo()+"\n"+log);
		}
		
		File logFile = new File(AppUtils.LOG_PATH);
		if (!logFile.exists())
		{
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ;
			}
		}
		try {
			FileWriter fw = new FileWriter(logFile,true);
			fw.write("\n"+getTime() + " "+getLineInfo()+"\n"+log+"\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static String getTime()
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		return format.format(date);
	}
	
	private static String getLineInfo()
	{
		StackTraceElement ele = new Throwable().getStackTrace()[2];
		String  s = ele.getClassName() +" line -"+ele.getLineNumber();
		return s;
	}
}
