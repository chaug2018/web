/**
 * IGetPanel.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-14
 */
package com.yzj.wf.scan.util;

import javax.swing.JPanel;

/**
 * 初始化并获取applet面板
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public interface IAppletPanel {

	/**
	 * 冲刷面板对象
	 */
	public void prepareImageNodeList();

	/**
	 * 获取整体面板
	 * 
	 * @return 对应面板
	 */
	public JPanel getJPanel();

	/**
	 * 面板销毁
	 */
	public void dispose();
	
	SysCacheContext getSysCacheContext();
	
	ReadConfig getReadConfig();
}
