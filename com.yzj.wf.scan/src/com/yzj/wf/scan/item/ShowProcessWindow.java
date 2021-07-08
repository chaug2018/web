package com.yzj.wf.scan.item;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JProgressBar;


/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 进程框显示服务
 * @author 陈林江
 * @version 1.0.1
 */
public class ShowProcessWindow {

	private  final ProcessWindow processWindow = new ProcessWindow(null, "处理中...");
	/**
	 * showFlag展现进度条返回true
	 */
	private boolean showFlag = false;

	private boolean stopProcess = false;

	private static final ShowProcessWindow showWindow = new ShowProcessWindow();

	private ShowProcessWindow() {
		processWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				stopProcessWindow();
			}
		});
	}

	public static ShowProcessWindow instance() {
		return showWindow;
	}
	public  void setMessage(String message){
		processWindow.setMesaage(message);
	}

	public synchronized boolean isShowFlaging() {
		if (!this.showFlag)
			return this.showFlag = true;
		return false;
	}

	public synchronized void setShowFlaged() {
		this.showFlag = false;
	}

	public void startProcessWindow(String title) {
		if (!isShowFlaging()) {
			return;
		}
		processWindow.init();
		processWindow.setTitle(title);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		processWindow.setLocation((screenSize.width - processWindow.getWidth()) / 2, (screenSize.height - processWindow.getHeight()) / 2);
		processWindow.pack();
		new ProcessThread(processWindow.getJprogressBar()).start();
		processWindow.setVisible(true);
	}

	class ProcessThread extends Thread {

		JProgressBar jprogressBar;

		ProcessThread(JProgressBar jprogressBar) {
			super("进度条线程");
			this.jprogressBar = jprogressBar;
		}

		public void run() {
			long startTime = System.currentTimeMillis();
			int minimum = jprogressBar.getMinimum();
			int maximum = jprogressBar.getMaximum();
			jprogressBar.setValue(minimum);
			long endTime = 0L;
			while (true) {
				if (stopProcess || !showFlag)
					break;
				endTime = System.currentTimeMillis();
				if ((endTime - startTime) > 600000L)
					break;
				try {
					int value = jprogressBar.getValue() + 1;
					if (value <= maximum)
						jprogressBar.setValue(value);
					else
						jprogressBar.setValue(minimum);
					sleep(10);
				} catch (InterruptedException ignoredException) {
				}
			}
			jprogressBar.setValue(maximum);
			stopProcessWindow();
		}
	}

	public synchronized void stopProcessWindow() {
		stopProcess = false;
		showFlag = false;
		processWindow.setVisible(false);
		processWindow.dispose();
	}
	
}
