package com.yzj.wf.ebs.scanapplet.applet;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.yzj.wf.ebs.scanapplet.common.AppletParamDefine;
import com.yzj.wf.ebs.scanapplet.tran.UploadService4ebs;
import com.yzj.wf.scan.service.ScanEntrance;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 扫描控件applet类
 * @author 陈林江
 * @version 1.0.1
 */
public class MainApp extends JApplet {


	private static final long serialVersionUID = 5790704250791018758L;

    private ScanEntrance scanEntrance=null;  //扫描模块入口

	public MainApp() {
		super();
	}
	

	/**
	 * 创建图形界面
	 */
	private void createGUI(JPanel panel) throws Exception {
		try {
			Container cp = this.getContentPane();
			cp.setLayout(new BorderLayout());
			cp.add(panel, BorderLayout.CENTER);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("创建图形界面失败，错误【" + ex.getMessage() + "】");
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						initData();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null,ex.getMessage(),
								"提示", JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		} catch (Exception e) {
			System.out.println(getClass().getName() + "初始化失败：【"
					+ e.getMessage() + "】");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 初始化必要数据
	 * 
	 */
	private void initData() throws Exception {
		//初始获取applet数据接口
		String busTypes=this.getParameter("busTypes");
		String imageServerAdress=this.getParameter("imageServerAdress");
		String bizServerAdress=this.getParameter("bizServerAdress");
		String userId=this.getParameter("userId");
		String orgId=this.getParameter("orgId");
		String language=this.getParameter("language");
		String autoFormat=this.getParameter("autoFormat");
		String sIsDelete=this.getParameter("isDelete");

		
		
		System.out.println("传入业务类型参数为:"+busTypes);
		System.out.println("传入影像接收地址参数为:"+imageServerAdress);
		System.out.println("传入的业务数据接收地址参数为:"+bizServerAdress);;
		System.out.println("传入的用户id为:"+userId);
		System.out.println("传入的机构id为:"+orgId);
		System.out.println("传入的语言参数为:"+language);
		System.out.println("传入的是否自动格式化参数为:"+autoFormat);
		System.out.println("传入的是否上传完毕后删除的参数为:"+sIsDelete);
		
		AppletParamDefine.imageServerAdress=imageServerAdress;
		AppletParamDefine.bizServerAdress=bizServerAdress;
		AppletParamDefine.userId=userId;
		AppletParamDefine.orgId=orgId;
		boolean isAutoFormat=true;
		boolean isDelete=true;
		if("0".equals(autoFormat)){
			isAutoFormat=false;
		}
		if("0".equals(sIsDelete)){
			isDelete=false;
		}
		/**
		 * 若想使用自己的语言包,可参考如下注释掉的两段代码
		 */
//		File languageFile=new File(getClass().getResource("Scan_ch_ebs.properties").getFile());
//		scanEntrance=new ScanEntrance(languageFile, isAutoFormat,isDelete, busTypes,new UploadService4ebs());
		scanEntrance=new ScanEntrance(language, isAutoFormat,isDelete, busTypes,new UploadService4ebs()); //初始化扫描组件参数
		JPanel panel=scanEntrance.startScanService();
		this.createGUI(panel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.applet.Applet#stop()
	 */
	public void stop() {	
		scanEntrance.stopScanService();
		clearCacheAndClose();
	}

	private void clearCacheAndClose() {
			destroy();
			super.stop();
	}
	
}
