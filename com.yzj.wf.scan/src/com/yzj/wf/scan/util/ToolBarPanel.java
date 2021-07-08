/**
 * ToolBarPanel.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-14
 */
package com.yzj.wf.scan.util;

import javax.swing.JPanel;

import com.yzj.wf.scan.ui.selection.ISelection;
import com.yzj.wf.scan.ui.selection.ISelectionProvider;

/**
 * 抽象工具栏基类，方便后期功能扩展
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public abstract class ToolBarPanel extends JPanel implements ISelectionProvider {
	/**
	 * 改变当前已关联的Panel
	 * 
	 * @param source
	 *            提供者
	 * @param selection
	 */
	public abstract void toolBarPanelChanged(ISelectionProvider source,
			ISelection selection);
	
	public abstract IAppletPanel getAppletPanel();
}
