/**
 * SysCacheContext.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-21
 */
package com.yzj.wf.scan.util;

import java.io.File;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.yzj.wf.scan.appletparam.IDealAppletParam;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.tree.CustomTreeNode;

/**
 * 主要包括处理<CODE>ImageNode<CODE>和<CODE>CustomTreeNode<CODE>的方法
 * <p><i>主要实现下述功能：</i>
 *  
 * <li>生成树的根节点</li>
 * <li>获取文档集ID</li>
 * <li>系统退出文件清理</li>
 * <li>加载本地路径图片</li>
 * <li>获取树形层次属性orders<li>
 * <li>根据传入权限树，过滤文档集</li>
 * <li>根据文档集，生成完整的树形结构</li>
 * <li>获取符合条件的<CODE>ImageNode</CODE>集合</li>
 * <li>获取某一树节点的<CODE>ImageNode</CODE>集合</li>
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * 
 * 将操作数据库的存储，和上下文均放在Cache接口中
 * 
 * @author LiuQiangQiang
 * @version 1.0.1
 */
public class SysCacheContext {

	private static final Logger log = org.apache.log4j.Logger.getLogger(SysCacheContext.class);

	/**
	 * 根节点
	 */
	private CustomTreeNode root;

	/**
	 * 树层次属性
	 */
	private String[] orders = null;

	/**
	 * 文档集ID
	 */
	private String appId = null;

	/**
	 * 满足条件的所有ImageNode
	 */
	private Vector<ImageNode> imageNodeAll = new Vector<ImageNode>();


	/**
	 * 操作数据
	 */
	private ImageStoreContext imageStoreContext = new ImageStoreContext();


	/**
	 * APPLET处理参数
	 */
	private IDealAppletParam dealAppletParam;
	
	/**
	 * 读取文件
	 */
	private ReadConfig readconfig;
	
	/**
	 * 扫描插入时，所选择的插入点
	 */
	private BatchTreeNode insertNode = null;

	public SysCacheContext() {

	}

	/**
	 * 获取根节点，如果在配置文件中存在tRootName属性，根节点的名字将命名为tRootName对应的属性值，否则采用默认的<CODE>ImageStoreDef</CODE>的ShowName.ROOTNODE
	 * 
	 * @return ROOT
	 */
	public CustomTreeNode getRootNode() {
		if (root == null) {
			String name = readconfig.getPropertyValue("read_tRootName", ImageStoreDef.ShowName.ROOTNODENAME);
			log.debug("rootName is " + name);
			root = new CustomTreeNode(name, "ROOT");
			root.setRoot(true);
		}
		return root;
	}

	/**
	 * 获取影像树的层次对应的文档属性,如果配置文件中存在配置项tOrders属性，
	 * 首先获取配置文件串解析成数组，否则采用默认的数组为[0-10]的英文小写
	 * 
	 * @return String[] orders
	 */
	public String[] getOrders() {
		if (orders == null) {
			String order = readconfig.getPropertyValue("read_tOrders");
			log.debug("order is " + order);
			if (order != null) {
				orders = order.split(",");
			} else {
				orders = new String[] { "first", "second", "third", "four", "five", "six", "seven", "eight", "nine", "ten" };
			}
		}
		return orders;
	}


	/**
	 * 返回ImagNode集合
	 * 
	 * @return 返回所有内存中存在的ImageNode集合
	 */
	public Vector<ImageNode> getImageNodeAll() {
		return imageNodeAll;
	}


	/**
	 * 扫描模式下，查询文档集存在，追加文档时，将文档集ID放在此字段 质检模式下，查询文档集存在，追加文档时，将文档集第一个ID放在此字段
	 * 
	 * 获取文档集ID
	 * 
	 * @return 如果新增业务返回null
	 */
	public String getAppId() {
		return appId;
	}







	/**
	 * 选择多个文件，加载到图片list中,不允许重复加载相同的图片
	 * 
	 * @param files
	 *            被选中的文件
	 * @return 成功返回TRUE
	 */
	public boolean appendImageNodebyPic(File[] files) {
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				ImageNode imageNode = new ImageNode(files[i]);
				if (!this.imageNodeAll.contains(imageNode)) {
					this.imageNodeAll.add(imageNode);
				}
			}
			return true;
		}
		return false;
	}




	public ImageStoreContext getImageStoreContext() {
		return imageStoreContext;
	}

	public void setImageStoreContext(ImageStoreContext imageStoreContext) {
		this.imageStoreContext = imageStoreContext;
	}

	public IDealAppletParam getDealAppletParam() {
		return dealAppletParam;
	}

	public void setDealAppletParam(IDealAppletParam dealAppletParam) {
		this.dealAppletParam = dealAppletParam;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public ReadConfig getReadconfig() {
		return readconfig;
	}

	public void setReadconfig(ReadConfig readconfig) {
		this.readconfig = readconfig;
	}

	public BatchTreeNode getInsertNode() {
		return insertNode;
	}

	public void setInsertNode(BatchTreeNode insertNode) {
		this.insertNode = insertNode;
	}
	
}
