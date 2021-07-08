/**
 * IConfirmAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-22
 */
package com.yzj.wf.scan.util;

import com.yzj.wf.scan.ui.selection.StructuredSelection;

/**
 * 操作面板的操作类必须实现的接口
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public interface IConfirmAction {

	/**
	 * 每一个类型添加到操作面板的操作类，都必须实现的方法。 当<CODE>JComboBox</CODE>选择被操作后，操作面板会调用
	 * confirm方法
	 * 
	 * @return 成功返回TRUE
	 */
	boolean confirm();

	/**
	 * 是否本次操作需要通知<CODE>StorePanel</CODE>的监听者
	 * 
	 * @return 需要通知返回TRUE
	 */
	boolean isNotifyListener();

	/**
	 * isNotifyListener返回TRUE，<CODE>StorePanel</CODE>进调用本方法获取
	 * 选择源，如果返回为NULL,系统会用它本身getSelection方法产生的结果作为事件源
	 * 
	 * @return 没有特殊要求返回NULL
	 */
	StructuredSelection getStructuredSelection();

	/**
	 * 注册按钮区对象，用于内部通知其他组件,可与isNotifyListener方法配合使用
	 * @param toolBarPanel
	 */
	void setToolBarPanel(ToolBarPanel toolBarPanel);
}
