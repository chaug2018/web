/**
 * IDealAppletParam.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-5
 */
package com.yzj.wf.scan.appletparam;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 统一处理控件外部传入参数，主要有三种类型的处理方法
 * <li> 根据属性名，获取boolean值
 * <li> 根据属性名，获取String值
 * <li> 根据属性名，获取完整影像树结构树
 * @author LiuQiangQiang
 * @version 1.0.0
 * @see	javax.swing.tree.DefaultMutableTreeNode
 */
public interface IDealAppletParam {
	
	/**
	 * 统一处理Boolean类型返回值，通过paramName获取对应的值
	 * 
	 * <p><i>系统默认处理结果'1'返回TRUE,'0'返回FALSE</i>
	 * 
	 * @param paramName 属性名
	 * @return 允许使用功能返回TRUE，否则返回FALSE
	 * @throws Exception paranName为空是返回异常或者未知异常
	 */
	boolean getBooleanByParamName(String paramName) throws Exception;
	
	/**
	 * 统一获取String类型返回值，通过paramName获取对应的值
	 * 
	 * <p><i>系统默认处理结果，不存在对应的属性，返回空对象</i>
	 * 
	 * @param paramName 属性名
	 * @return 如果存在返回，返回去除前后多余空格的<CODE>String<CODE>，否则返回空对象
	 * @throws Exception paramName为空返回空异常或者未知异常
	 */
	String getStringByParamName(String paramName) throws Exception;
	
	/**
	 * 将外部传入的XML串，解析成树形结构,添加在rootNode节点下
	 * 
	 * <p><i>XML标签的属性值默认采用大写VALUE或者小写value</i>
	 * 
	 * <p>
	 * Examples:
	 * <blockquote><pre>
	 * String[] orders = new String[]{"first", "second", "third"}
	 * DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("影像查看");
	 * getTreeNodeByParamName(rootNode, "PurView", orders);
	 * </pre></blockquote>
	 * 
	 * @param rootNode 根节点，不能为空
	 * @param paramName 属性名,不能为空
	 * @param orders 不同层次标签的名称数组,不能为空
	 * @throws Exception 传入参数为空返回异常或者未知异常
	 */
	void getTreeNodeByParamName(DefaultMutableTreeNode rootNode, String paramName, String[] orders) throws Exception;
	
	
	/**
	 * 将外部传入的XML串，解析成树形结构,添加在rootNode节点下
	 * 
	 * <p><i>XML标签的属性值默认采用大写VALUE或者小写value</i>
	 * 
	 * <p>
	 * Examples:
	 * <blockquote><pre>
	 * String[] orders = new String[]{"first", "second", "third"}
	 * DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("影像查看");
	 * getTreeNodeByParamValue(rootNode, "<?xml version='1.0' encoding='UTF-8'?><ROOT></ROOT>", orders);
	 * </pre></blockquote>
	 * 
	 * @param rootNode 根节点，不能为空
	 * @param paramValue 属性值,不能为空
	 * @param orders 不同层次标签的名称数组,不能为空
	 * @throws Exception 传入参数为空返回异常或者未知异常
	 */
	void getTreeNodeByParamValue(DefaultMutableTreeNode rootNode, String paramValue, String[] orders) throws Exception;

}
