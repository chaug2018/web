/**
 * ProcessWindow.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-23
 */
package com.yzj.wf.scan.item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

import com.sun.java.swing.plaf.windows.WindowsEditorPaneUI;
import com.yzj.wf.scan.paramdefine.ParamDefine;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 进程窗口，支持信息的动态更新
 * @author 陈林江
 * @version 1.0.1
 */
public final class ProcessWindow extends JDialog {

	private static final long serialVersionUID = 8666616651346785196L;

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ProcessWindow.class);

	private JProgressBar jprogressBar;
	private JLabel describeLabel;

	protected ProcessWindow(String title) {
		this(null, title);
	}

	protected ProcessWindow(Frame frame, String title) {
		super(frame, title, true);
		try {
			initGUI();
		} catch (Exception e) {
			log.error("初始化进度条失败，失败原因：" + e.getMessage());
		}
	}
	
	public void init(){
		describeLabel.setText(ParamDefine.pleaseWaiting);
	}
	
	public void setMesaage(String message){
		describeLabel.setText(ParamDefine.pleaseWaiting+message);
	}

	private void initGUI() throws Exception {
		JPanel basePanel = new JPanel();
		Border panelBorder = BorderFactory.createCompoundBorder(new EtchedBorder(EtchedBorder.RAISED, Color.white, new Color(148, 145, 140)), BorderFactory.createEmptyBorder(5, 10, 5, 10));
		basePanel.setBorder(panelBorder);
		basePanel.setLayout(new BorderLayout());

		jprogressBar = new JProgressBar(0, 100);
		jprogressBar.setForeground(new Color(89, 89, 179));
		jprogressBar.setBounds(8, 57, 200, 16);
		jprogressBar.setPreferredSize(new java.awt.Dimension(200, 16));

		describeLabel = new JLabel(ParamDefine.pleaseWaiting);
		describeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		basePanel.add(describeLabel, BorderLayout.NORTH);
		basePanel.add(jprogressBar, BorderLayout.SOUTH);
		setAlwaysOnTop(true);
		setBackground(Color.gray);
		this.getContentPane().add(basePanel);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		this.setResizable(false);
	}

	public JProgressBar getJprogressBar() {
		return jprogressBar;
	}

	public void setJprogressBar(JProgressBar jprogressBar) {
		this.jprogressBar = jprogressBar;
	}
	protected void processWindowEvent(WindowEvent e) {
		if(e.getID()==WindowEvent.WINDOW_CLOSING){
			JOptionPane.showMessageDialog(this,"<html><font color='red'>操作未完成，无法取消!</font></html>","提示", JOptionPane.INFORMATION_MESSAGE);
		}else{
			super.processWindowEvent(e);
		}
	}
}
