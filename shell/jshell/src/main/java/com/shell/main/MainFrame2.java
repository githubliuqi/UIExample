package com.shell.main;

import com.shell.main.ShellHelper.ShellListener;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

public class MainFrame2 extends JFrame{

	private final String FRAME_TITLE = "APK加壳";
	private int mHeight = 300;
	private int mWidth  = 450;
	
	
	private JProgressBar mProgressBar;
	final FileSelector[] mSettings = new FileSelector[4];
	final JButton mBtnShell = new JButton("开始加壳");
	final JButton mBtnDecodeApk = new JButton("反编译Apk");
	final JCheckBox mCheckBox = new JCheckBox();
	static final Properties mProperties = new Properties();
	static final String[] mConfigParams = {"aapt","sdk","sdklib","apk"};

	static final String CONFIG_PATH =  AppUtils.FILE_PATH + "/config.txt";
	
	public MainFrame2() {
		this.setTitle(FRAME_TITLE);
		this.setSize(mWidth, mHeight);
		getContentPane().setLayout(null );
	    setResizable(false);
	    initViews();
	   
		this.setAutoRequestFocus(true);
		//this.setAlwaysOnTop(true);
		setCenterOnWindow();
		// 窗口关闭时程序退出
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				super.windowClosing(e);
//				System.exit(0);
//			}
//		});
		
//		int os_type = ShellTool.getOSType();
//		if (os_type == 0)
//		{
//			showDialog("未知操作系统！");
//			return ;
//		}
//		
//		if (ShellTool.getJDKVersion() == null)
//		{
//			showDialog("请安装JDK");
//			return ;
//		}
		//showDialog("请安装JDK");
	}
	

	
	private void initViews()
	{
		File file = new File("./");
		System.out.println("file:"+file.getAbsolutePath());
		try {
			mProperties.load(new FileInputStream(new File(CONFIG_PATH)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel label = new JLabel("请配置以下参数：(鼠标悬停在左侧文本上查看提示信息)");
		label.setBounds(5, 20, mWidth, 20);
		getContentPane().add(label);
		int y = label.getLocation().y+label.getHeight()+5;
		
		
		String[] tips = {"SDK root/build-tools/23.0.3(version自选)/aapt",
				"SDK root/platforms/android-19(version自选)/android.jar","SDK root/tools/lib/sdklib.jar","请选择待加壳的apk"};
		String[] defaultPath={
				"/Applications/AndroidDeveloper/sdk/android-sdk-macosx/build-tools/30.0.3/aapt",
				"/Applications/AndroidDeveloper/sdk/android-sdk-macosx/platforms/android-31/android.jar",
				"/Applications/AndroidDeveloper/sdk/android-sdk-macosx/tools/lib/sdklib-26.0.0-dev.jar",
				""
		};
		int h = 25;
		for (int i = 0; i< mSettings.length;i++)
		{
			mSettings[i] = new FileSelector();
			String cfg = mProperties.getProperty(mConfigParams[i])+"";
			//LogUtils.i("cfg = "+cfg);
			mSettings[i].setDefaultFilePath(cfg.length() == 0? defaultPath[i]:cfg);
			mSettings[i].setLabelText(mConfigParams[i]+":");
			mSettings[i].setToolTipText(tips[i]);
			mSettings[i].setBounds(5,y+ i*(h+5), mWidth - 30, h);
			getContentPane().add(mSettings[i]);
		}
		mSettings[0].setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().startsWith("aapt"))
				{
					return true;
				}
				return false;
			}
		});
		
		mSettings[1].setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ".jar";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().equals("android.jar"))
				{
					return true;
				}
				return false;
			}
		});
		mSettings[2].setFileFilter(new FileFilter() {
				
				@Override
				public String getDescription() {
					// TODO Auto-generated method stub
					return ".jar";
				}
				
				@Override
				public boolean accept(File f) {
					if (f.getName().endsWith("sdklib.jar"))
					{
						return true;
					}
					return false;
				}
		});
        mSettings[3].setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ".apk";
			}
			
			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".apk"))
				{
					return true;
				}
				return false;
			}
		});
        // upx选择
        mCheckBox.setBounds(0, mSettings[3].getLocation().y+mSettings[3].getHeight()+20, 200, 30);
        mCheckBox.setText("upx(Windows系统上无效)");
        mCheckBox.setVisible(false);
        getContentPane().add(mCheckBox);
        if (ShellTool.OS_TYPE_MAC != ShellTool.getOSType())
        {
        	mCheckBox.setSelected(false);
        	mCheckBox.setEnabled(false);
        }

		// 开始加壳
		mBtnShell.setBounds((mWidth - 200)/2, mCheckBox.getLocation().y+mCheckBox.getHeight()+5, 100, 30);
		mBtnShell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mBtnShell.setEnabled(false);
				startShell(mSettings[3].getFilePath());
			}
		});
		getContentPane().add(mBtnShell);
		
		
		mBtnDecodeApk.setBounds(mBtnShell.getWidth()+mBtnShell.getX()+10,mBtnShell.getLocation().y, 100, 30);
		mBtnDecodeApk.setVisible(false);
		mBtnDecodeApk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mBtnDecodeApk.setEnabled(false);
				//startShell(mApkLocation.getText(),mApkApplicationName.getText());
				decodeApk(mSettings[3].getFilePath());
			}
		});
		getContentPane().add(mBtnDecodeApk);
		
		// 进度条
		
		mProgressBar= new JProgressBar();
		mProgressBar.setBounds(50, mBtnShell.getLocation().y + mBtnShell.getHeight() + 10, mWidth - 50*2, 20);
		//mProgressBar.setUI(new WindowsProgressBarUI());
		mProgressBar.setIndeterminate(true);
		mProgressBar.setToolTipText("正在加壳...");
		mProgressBar.setForeground(Color.RED);
		mProgressBar.setName("name");
		mProgressBar.setString("正在加壳...");
		mProgressBar.setStringPainted(true);
		
		mProgressBar.setOrientation(JProgressBar.HORIZONTAL);
		mProgressBar.setVisible(false);
		
		getContentPane().add(mProgressBar);
	}
	
	private void showTip(String tip)
	{
		//JOptionPane.showMessageDialog(this, tip);
		JOptionPane.showMessageDialog(this, tip, "提示", JOptionPane.WARNING_MESSAGE);
		
		//showDialog(tip);
	}
	

    private void showDialog(String s)
    {
    	int result = JOptionPane.showConfirmDialog(this,s ,"提示",JOptionPane.DEFAULT_OPTION);
    	//System.out.println("result = "+result);
    	//LogUtils.i("result = "+result);
    	if (result == JOptionPane.OK_OPTION || result == JOptionPane.CLOSED_OPTION)
    	{
    		System.exit(0);
    	}
    }
	 // 设置居中显示到window
	private void setCenterOnWindow()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		int w = toolkit.getScreenSize().width;
		int h = toolkit.getScreenSize().height;
		setLocation((w - getWidth())/2, (h - getHeight())/2);
	}
	
	// 显示/关闭进度条
	private void showProgress(boolean show)
	{
		if (null != mProgressBar)
		{
			mProgressBar.setVisible(show);
		}
	}
	
	private boolean checkSettings()
	{
		for(FileSelector fs: mSettings)
		{
			if (null == fs.getSelectedFile() ||  !fs.getSelectedFile().exists())
			{
				return false;
			}
		}
		APKHelper.AAPT = mSettings[0].getFilePath().replace("\\", "/");
		APKHelper.ANDROID_SDK_JAR = mSettings[1].getFilePath().replace("\\", "/");
		APKHelper.SDKLIB_JAR=mSettings[2].getFilePath().replace("\\", "/");
		return true;
	}
	
	// 开始加壳
	private void startShell(String apkPath)
	{
		
		LogUtils.i("upx "+mCheckBox.isSelected());
		if (!checkSettings())
		{
			showTip("请正确配置参数!");
			mBtnShell.setEnabled(true);
			return ;
		}
		for (int i= 0;i<mConfigParams.length;i++ )
		{
			mProperties.setProperty(mConfigParams[i],mSettings[i].getFilePath() );
		}
		try {
			mProperties.save(new FileOutputStream(new File(CONFIG_PATH)), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("开始加壳");
		LogUtils.i("start shell..");
		
		ShellHelper.startShell(apkPath,mCheckBox.isSelected(),new ShellListener() {
			
			@Override
			public void WillShell() {
				EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						showProgress(true);
					}
				});
				
			}

			@Override
			public void shellFinished(final String message) {
                 EventQueue.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						mBtnShell.setEnabled(true);
						showProgress(false);
						showTip(message);
						//showDialog(message);
					}
				});
			}
		});
	}
	
	// 反编译Apk
	private void decodeApk(String apkPath)
	{
		
		boolean ok = APKHelper.decodeApk(new File(apkPath) , new File(apkPath.replace(".apk", "")));
		String message = ok ? "反编译成功":"反编译失败";
		//showTip(message);
	    mBtnDecodeApk.setEnabled(true);
		showDialog(message);
	}

}
