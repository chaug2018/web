/**
 * AbstractNodeList.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-15
 */
package com.yzj.wf.scan.util;

import javax.swing.JPanel;

import com.yzj.wf.scan.ui.selection.ISelectionChangedListener;

/**
 * 抽象基类，方便后期功能扩展
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public abstract class AbstractNodeList extends JPanel implements INodeList,
		ISelectionChangedListener {
	/**
	 * 清除资源
	 */
	public void clear(){
		
	}
}
