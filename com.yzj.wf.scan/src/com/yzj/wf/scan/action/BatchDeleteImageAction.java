/**
 * BatchDeleteImageAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2012-2-17
 */
package com.yzj.wf.scan.action;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 扫描批量删除
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class BatchDeleteImageAction implements IConfirmAction {

	private ScanToolBarPanel toolBarPanel;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BatchDeleteImageAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		BatchScanView view = toolBarPanel.getView();
		BatchTree tree = view.getBatchTree();
		TreePath[] treepaths = tree.getSelectionPaths();
		String showMessge = null;
		// check
		if (treepaths == null || treepaths.length == 0) {
			showMessge = ParamDefine.deleteImageNotice;
			JOptionPane.showMessageDialog(view, showMessge, ParamDefine.noticeMessage,
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		boolean isSelected = false;
		for (TreePath path : treepaths) {
			BatchTreeNode selectedNode = (BatchTreeNode) path
					.getLastPathComponent();
			if (selectedNode.isSelected()) {
				isSelected = true;
				break;
			}
		}
		if (!isSelected) {
			showMessge = ParamDefine.deleteImageNotice;
			JOptionPane.showMessageDialog(view, showMessge, ParamDefine.noticeMessage,
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		LinkedList<BatchTreeNode> list = new LinkedList<BatchTreeNode>();
		// get node set
		BatchTreeNode tempnode = null;
		for (int i = 0; i < treepaths.length; i++) {
			tempnode = (BatchTreeNode) treepaths[i].getLastPathComponent();
			if (tempnode != null) {
				int number = tempnode.getLevel();
				if (number == 0 && tempnode.isSelected()) {
					// root
					logger.debug("user selected the root node");
					continue;
				} else if (number == 1 && tempnode.isSelected()) {
					// nullnode or other node
					logger.debug("user selected the Null_ID node");
					Enumeration<?> enumeration = tempnode.children();
					while (enumeration.hasMoreElements()) {
						tempnode = (BatchTreeNode) enumeration.nextElement();
						if (list.indexOf(tempnode) == -1
								&& tempnode.isSelected()) {
							logger.debug("the linklist add the node, is "
									+ tempnode);
							list.add(tempnode);
						}
					}
				} else {
					if (list.indexOf(tempnode) == -1 && tempnode.isSelected()) {
						logger.debug("the linklist add the node, is "
								+ tempnode);
						list.add(tempnode);
					}
				}
			}
		}
		// get user info mes
		if (list.size() == 0) {
			showMessge = ParamDefine.deleteImageNotice;
			JOptionPane.showMessageDialog(view, showMessge, ParamDefine.noticeMessage,
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		showMessge = ParamDefine.deleteConfire_a + list.size()
				+ ParamDefine.deleteConfire_b;
		int result = JOptionPane.showConfirmDialog(view, showMessge, ParamDefine.confireMessage,
				JOptionPane.OK_CANCEL_OPTION);
		if (JOptionPane.YES_OPTION == result) {
			// delete node
			Iterator<BatchTreeNode> iterator = list.iterator();
			BatchTreeNode parentnode = null;
			while (iterator.hasNext()) {
				tempnode = iterator.next();
				parentnode = (BatchTreeNode) tempnode.getParent();
				int index = parentnode.getIndex(tempnode);
				parentnode.remove(tempnode);
				if (tempnode.getImageNode() != null) {
					ImageUtility.deleteFile(tempnode.getImageNode().getFile());
				}
				((DefaultTreeModel) tree.getModel()).nodesWereRemoved(
						parentnode, new int[] { index },
						new BatchTreeNode[] { tempnode });
			}
			if (parentnode != null) {
				tree.setSelectionPath(new TreePath(parentnode));
			}
			tree.clearNoChildNodes(); // 清除没有子节点的父节点
			// tree.expandTree();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#getStructuredSelection()
	 */
	public StructuredSelection getStructuredSelection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public boolean isNotifyListener() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yinzhijie.util.IConfirmAction#setToolBarPanel(com.yinzhijie.util.
	 * ToolBarPanel)
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		this.toolBarPanel = (ScanToolBarPanel) toolBarPanel;
	}

}
