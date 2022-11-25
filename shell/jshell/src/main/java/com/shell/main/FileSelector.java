package com.shell.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class FileSelector extends JPanel {
	
	private JLabel mLabel;
	private JTextField mTextField;
	private JButton    mButton;
	private JFileChooser mFileChooser;
	
	
	private static final float RADIO = 0.8F;
	
	public FileSelector()
	{
		mLabel = new JLabel("hello");
		mTextField = new JTextField();
		mButton    = new JButton();
		mFileChooser = getDefaultFileChooser();
		init();
	}
	
	private void init()
	{
		setLayout(null);
		mLabel.setLocation(0, 0);
		mTextField.setLocation(0, 0);
		mButton.setLocation(0, 0);
		add(mLabel);
		add(mTextField);
		add(mButton);
		
		mButton.setText("...");
		mButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (null != mTextField.getText())
				{
					File file = new File(mTextField.getText().trim());
					if (file.exists())
					{
						mFileChooser.setSelectedFile(file);
					}
				}
						
				mFileChooser.showDialog(new JLabel(), "选择");
			}
		});
	}
	public void setToolTipText(String tips)
	{
		mLabel.setToolTipText(tips);
	}
	
	private JFileChooser getDefaultFileChooser()
	{
		final JFileChooser mFielChooser = new JFileChooser();
		mFielChooser.setVisible(true);
		mFielChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		mFielChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String type = e.getActionCommand();
				if (JFileChooser.APPROVE_SELECTION == type)//OK
				{
					String path = mFielChooser.getSelectedFile().getAbsolutePath();
					mTextField.setText(path);
				}
			}
		});
		return mFielChooser;
	}
	
	public void setDefaultFile(File file)
	{
		if (null == file || !file.exists())
		{
			mTextField.setText("");
		}else
		{
			mTextField.setText(file.getPath());
		}
	}
	
	public void setDefaultFilePath(String path)
	{
		mTextField.setText(null == path?"":path);
	}
	
	public void setFileFilter(FileFilter filter)
	{
		mFileChooser.setFileFilter(filter);
	}
	
	public void setLabelText(String title)
	{
		mLabel.setBackground(Color.green);
		mLabel.setText(title);
	}
	
	public String getFilePath()
	{
		return mTextField.getText();
	}
	
	public File getSelectedFile()
	{
		String text = mTextField.getText();
		if (null == text)
		{
			return null;
		}
		File file = new File(text.trim());
		return file.exists() ? file : null;
	}
	
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		super.setBounds(x, y, width, height);
		mLabel.setBounds(0, 0,  (int) (width*(1-RADIO)/2), height);
		mTextField.setBounds(mLabel.getLocation().x+mLabel.getWidth(), mLabel.getLocation().y, (int) (width*RADIO), height);
		mButton.setBounds(mTextField.getLocation().x+mTextField.getWidth(), mTextField.getLocation().y, (int) (width*(1-RADIO)/2),height);
	}

}
