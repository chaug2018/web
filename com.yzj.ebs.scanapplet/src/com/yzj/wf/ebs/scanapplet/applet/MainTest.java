package com.yzj.wf.ebs.scanapplet.applet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.yzj.wf.ebs.scanapplet.tran.UploadService4ebs;
import com.yzj.wf.scan.service.ScanEntrance;

/**
 *创建于:2013-4-16<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 
 * @author 陈林江
 * @version 1.0
 */
public class MainTest extends JFrame{
	
	public MainTest() {
		try {
			ScanEntrance scanEntrance = new ScanEntrance("Scan_ch_ebs", true,true, "1,对账单",new UploadService4ebs());
			JPanel panel=scanEntrance.startScanService();
			this.setBounds(200, 100, 600, 400);
			this.setVisible(true);
			this.add(panel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //初始化扫描组件参数
		
	}
	
	public static void main(String[] args) {
		//new MainTest();
		
		MainApp app  = new MainApp();
		app.init();
		System.out.println("2");
	}
}
