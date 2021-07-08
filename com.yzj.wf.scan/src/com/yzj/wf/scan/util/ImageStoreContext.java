/**
 * ImageStoreContext.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-8
 */
package com.yzj.wf.scan.util;

import java.util.Vector;

import com.yzj.wf.scan.tree.CustomTreeNode;

/**
 * 上下文数据
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class ImageStoreContext {
	
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ImageStoreContext.class);
	
	/**
	 * 树选择对象
	 */
	protected Vector<CustomTreeNode> selectedTreeNode = new Vector<CustomTreeNode>(2);
	
	/**
	 * 选择节点在selectedTreeNode中的索引
	 */
	protected int lastSelectedTreeNodeIndex = -1;
	
	/**
	 * 图片列表选择节点
	 */
	protected ImageNode[] selectedImageNode = null;
	
	/**
	 * 树tab选择
	 */
	protected int treePaneState = -1;
	
	/**
	 *	最后选择节点在selectedImageNode的索引
	 */
	protected int lastSelectedImageNodeIndex = -1;

	public synchronized int getState(){
		return this.treePaneState;
	}
	
	public synchronized void setState(int state){
		this.treePaneState = state;
		clearList();
	}
	
	public synchronized Vector<CustomTreeNode> getSelectedTreeNode() {
		return selectedTreeNode;
	}

	public synchronized void setSelectedTreeNode(Vector<CustomTreeNode> selectedTreeNode, int lastSelectedTreeNodeIndex) {
		this.selectedTreeNode = selectedTreeNode;
		this.lastSelectedTreeNodeIndex = lastSelectedTreeNodeIndex;
	}
	
	public synchronized void setSelectedTreeNode(Vector<CustomTreeNode> selectedTreeNode, CustomTreeNode treeNode) {
		this.selectedTreeNode = selectedTreeNode;
		this.lastSelectedTreeNodeIndex = selectedTreeNode.indexOf(treeNode);;
	}

	public synchronized int getLastSelectedTreeNodeIndex() {
		return lastSelectedTreeNodeIndex == -1 ? 0 : lastSelectedTreeNodeIndex;
	}

	public synchronized ImageNode[] getSelectedImageNode() {
		return selectedImageNode;
	}

	public synchronized void setSelectedImageNode(ImageNode[] selectedImageNode, int lastSelectedImageNodeIndex) {
		this.lastSelectedImageNodeIndex = lastSelectedImageNodeIndex;
		this.selectedImageNode = selectedImageNode;
	}
	
	public synchronized void setSelectedImageNode(ImageNode[] selectedImageNode, ImageNode imageNode) {
		this.selectedImageNode = selectedImageNode;
		if(selectedImageNode != null){
			for(int i = 0; i < selectedImageNode.length; i++){
				if(selectedImageNode[i] == imageNode){
					this.lastSelectedImageNodeIndex = i;
					return;
				}
			}
		}
	}

	public synchronized int getLastSelectedImageNodeIndex() {
		return lastSelectedImageNodeIndex;
	}
	
	public synchronized ImageNode getLastSelectedImageNode(){
		if(this.selectedImageNode != null && this.lastSelectedImageNodeIndex > -1 && this.selectedImageNode.length > this.lastSelectedImageNodeIndex){
			return this.selectedImageNode[this.lastSelectedImageNodeIndex];
		}
		return null;
	}
	
	public synchronized void clearList(){
		log.info("---------------clear object----------------------");
		this.selectedImageNode = null;
		this.lastSelectedImageNodeIndex = -1;
	}
}
