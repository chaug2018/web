/**
 * INodeListModel.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-16
 */
package com.yzj.wf.scan.util;

import java.util.Vector;

/**
 * List模型需要实现接口
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public interface INodeListModel {
	
	/**
	 * 调取model更新数据
	 * 
	 * @param delegate model需要显示的数据
	 */
	void setData(Vector<ImageNode> delegate);
	
	/**
	 * 获取model中所有的数据
	 * 
	 * @return model中数据集合
	 */
	OrderImageNodeList getOrderImageNodeList();
	
	/**
	 * 
	 * 扩展使用方法，当树机构tab变换时，调用reload方法
	 */
	void reload();
}
