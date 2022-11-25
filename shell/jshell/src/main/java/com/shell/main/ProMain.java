package com.shell.main;

import com.shell.main.test.MainTest;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Locale;
import java.util.zip.ZipFile;

import net.dongliu.apk.parser.ApkParser;
import net.dongliu.apk.parser.bean.ApkSignStatus;
import net.dongliu.apk.parser.bean.DexClass;
import net.dongliu.apk.parser.parser.DexParser;

public class ProMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//ShellTool.testEnc();

		File log = new File(AppUtils.LOG_PATH);
		if (log.exists()) {
			log.delete();
		}
		
		//ShellTool.getMenifest("C:\\Users\\win7_32\\Desktop\\audioShell\\EsManagerTool.apk");
		//ShellTool.getMenifest("input/EsManagerTool_zipalign.apk");
		//APKHelper.resignApk("output/shellApk.apk");
		
//		MainFrame2 frame = new MainFrame2();
//		frame.setVisible(true);

		MainTest.test();

	}

}
