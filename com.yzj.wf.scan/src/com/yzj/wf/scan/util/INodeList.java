/**
 * INodeList.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-15
 */
package com.yzj.wf.scan.util;

import com.yzj.wf.scan.ui.selection.ISelectionProvider;

/**
 * 扩展ListPane功能
 * <li>实现restoreSelection</li>
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public interface INodeList extends ISelectionProvider {
	/**
	 * 视图转换时，需要恢复选择项
	 * 
	 * @param lastSelectNode
	 */
	void restoreSelection(ImageNode lastSelectNode);
	
	/**
	 * 获取model，以便方便恢复数据
	 * 
	 * @return 返回INodeListModel接口
	 */
	INodeListModel getListModel();
}
