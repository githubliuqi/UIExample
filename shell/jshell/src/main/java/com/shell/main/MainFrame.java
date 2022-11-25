package com.shell.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ProgressBarUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.plaf.metal.MetalProgressBarUI;
import javax.swing.plaf.synth.SynthProgressBarUI;

public class MainFrame extends JFrame{

	private final String FRAME_TITLE = "APK加壳";
	private int mHeight = 300;
	private int mWidth  = 300;
	
	private JFileChooser mFielChooser ;// 文件选择器
	private JButton mBtnSelectApk; //选择apk
	private JProgressBar mProgressBar; // 进度条
	public MainFrame() {
		this.setTitle(FRAME_TITLE);
		this.setSize(mWidth, mHeight);
		getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		//setLayout(new BorderLayout());

		
	   createViews();
	   
	   
		this.setAutoRequestFocus(true);
		this.setAlwaysOnTop(true);
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
		
	}
	

	
	private void createViews()
	{
		// 文件选择器
		mFielChooser = new JFileChooser();
		mFielChooser.setVisible(true);
		mFielChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		mFielChooser.setFileFilter(new FileFilter() {
			
			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return ".apk";
			}
			
			@Override
			public boolean accept(File f) {
				// TODO Auto-generated method stub
				System.out.println("");
				return f.getName().endsWith(".apk");
			}
		});
		
		getContentPane().add(Box.createVerticalStrut(10));
		// 选择APK文件
		
		JPanel panel_chooseApk = new JPanel();
		
		mBtnSelectApk = new JButton("选择Apk文件");
		mBtnSelectApk.addActionListener(new ActionListener() {
			boolean b = false;
			@Override
			public void actionPerformed(ActionEvent e) {
				//mFielChooser.showDialog(new JLabel(), "选择");
				//showTip("tip");
				b = !b;
				showProgress(b);
			}
		});
		getContentPane().add(mBtnSelectApk);
		getContentPane().add(Box.createVerticalStrut(20));
		// 开始加壳
		final JButton btnShell = new JButton("开始加壳");
		btnShell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnShell.setEnabled(false);
				startShell();
			}
		});
		getContentPane().add(btnShell);
		// 进度条
		int space = 30;
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
		panel1.add(Box.createHorizontalStrut(space));
		//panel1.setSize(100,30);
		//panel1.setPreferredSize(new Dimension(100, 30));
		//panel1.setBackground(Color.red);
		mProgressBar= new JProgressBar();
		//mProgressBar.setUI(new WindowsProgressBarUI());
		mProgressBar.setIndeterminate(true);
		//mProgressBar.setToolTipText("正在加壳...");
		mProgressBar.setForeground(Color.RED);
		mProgressBar.setName("name");
		mProgressBar.setString("string");
		mProgressBar.setStringPainted(true);
		mProgressBar.setSize(100, 50);
		//mProgressBar.setBackground(Color.GRAY);
		mProgressBar.setOrientation(JProgressBar.HORIZONTAL);
		mProgressBar.setVisible(true);
		panel1.add(mProgressBar);
		panel1.add(Box.createHorizontalStrut(space));
		getContentPane().add(panel1);
		getContentPane().add(Box.createVerticalGlue());
	}
	
	private void showTip(String tip)
	{
		//JOptionPane.showMessageDialog(this, tip);
		JOptionPane.showMessageDialog(this, tip, "提示", JOptionPane.WARNING_MESSAGE);
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
	
	// 开始加壳
	private void startShell()
	{
		System.out.println("开始加壳");
	}
	
	

}
