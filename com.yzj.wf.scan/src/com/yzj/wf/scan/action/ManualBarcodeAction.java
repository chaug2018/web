/**
 * ManualBarcodeAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2012-2-22
 */
package com.yzj.wf.scan.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 手工分类
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class ManualBarcodeAction implements IConfirmAction {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ManualBarcodeAction.class);

	private ScanToolBarPanel toolBarPanel;

	private static final String pattern = "^[0-9]{10,20}$";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		final BatchScanView view = toolBarPanel.getView();
		final BatchTree tree = view.getBatchTree();
		final TreePath[] paths = tree.getSelectionPaths();
		String showMessage = null;
		if (paths == null || paths.length == 0) {
			logger.info("user select null batchtreenode");
			showMessage = "<html>请在左侧节点中选择需要<font color=red>手工分类</font>的影像资料！</html>";
			JOptionPane.showMessageDialog(view, showMessage, "提示信息", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		boolean isSelected = false;
		for (TreePath path : paths) {
			BatchTreeNode selectedNode = (BatchTreeNode) path.getLastPathComponent();
			if (selectedNode.isSelected()) {
				isSelected = true;
				break;
			}
		}
		if (!isSelected) {
			logger.info("user select null batchtreenode");
			showMessage = "<html>请在左侧节点中选择需要<font color=red>手工分类</font>的影像资料！</html>";
			JOptionPane.showMessageDialog(view, showMessage, "提示信息", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		ArrayList<BatchTreeNode> arrayList = new ArrayList<BatchTreeNode>();
		BatchTreeNode node = null;
		for (int i = 0; i < paths.length; i++) {
			node = (BatchTreeNode) paths[i].getLastPathComponent();
			if (node.getLevel() == 2 && node.isSelected()) {
				arrayList.add(node);
			} else if (node.getLevel() == 1 && node.isSelected()) {
				addChild(arrayList, node);
			}
		}

		if (arrayList.size() == 0) {
			logger.info("user select batchtreenode");
			showMessage = "<html>所选节点下没有可<font color=red>手工分类</font>的影像资料，请重新选择！</html>";
			JOptionPane.showMessageDialog(view, showMessage, "提示信息", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		showMessage = "<html>请输入业务编号或客户编号（<font color=red>10~20位整数</font>）：</html>";
		String nodeName = JOptionPane.showInputDialog(view, showMessage, "提示信息", JOptionPane.INFORMATION_MESSAGE);
		logger.info("user input nodeName is " + nodeName);
		if (nodeName.equals("")) {
			logger.info("user input nodenam is empty string");
			showMessage = "<html>业务编号或客户编号不能为<font color=red>空</font>！</html>";
			JOptionPane.showMessageDialog(view, showMessage, "错误信息", JOptionPane.ERROR_MESSAGE);
			return false;
		}else if(!Pattern.matches(pattern,nodeName)){
			logger.info("user input nodename is not Int type between 10 and 20");
			showMessage = "<html>业务编号或客户编号必须为<font color=red>10~20位整数</font>！</html>";
			JOptionPane.showMessageDialog(view, showMessage, "错误信息", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		Iterator<BatchTreeNode> iterator = arrayList.iterator();
		ImageNode imageNode = null;
		StringBuilder sb = new StringBuilder();
		while (iterator.hasNext()) {
			node = iterator.next();
			imageNode = node.getImageNode();
			if (imageNode != null && imageNode.getBarcode() != null && !imageNode.getBarcode().equals("")) {
				if (!imageNode.getBarcode().startsWith(nodeName)) {
					sb.append("<p><font color=red>").append(node.toString()).append("，识别结果：" + imageNode.getBarcode() + "</font>");
				}
			}
		}
		if (sb.length() != 0) {
			showMessage = "<html>下列已选影像<font color=red>条码识别</font>结果与<font color=red>输入</font>的业务编码或客户编码<font color=red>不一致</font>：" + sb.toString() + "</html>";
			JOptionPane.showMessageDialog(view, showMessage, "错误信息", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		showMessage = "<html>确认将<font color=red>" + arrayList.size() + "</font>张影像资料分配到<font color=red>" + nodeName + "</font>节点下？";
		int result = JOptionPane.showConfirmDialog(view, showMessage, "确认信息", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			BatchTreeNode rootNode = (BatchTreeNode) tree.getModel().getRoot();
			node = getBatchTreeNode(rootNode, nodeName);
			if (node == null) {
				node = new BatchTreeNode(nodeName);
				rootNode.add(node);
			}

			iterator = arrayList.iterator();
			BatchTreeNode childrenNode = null;
			while (iterator.hasNext()) {
				childrenNode = iterator.next();
				childrenNode.setBarCode(nodeName);
				node.add(childrenNode);
			}
			((DefaultTreeModel) tree.getModel()).nodeStructureChanged(rootNode);
			tree.clearNoChildNodes();
			tree.expandTree();
		}
		return true;
	}

	private void addChild(ArrayList<BatchTreeNode> arrayList, BatchTreeNode node) {
		Enumeration<?> enumeration = node.children();
		while (enumeration.hasMoreElements()) {
			arrayList.add((BatchTreeNode) enumeration.nextElement());
		}
	}

	private BatchTreeNode getBatchTreeNode(BatchTreeNode rootNode, String nodeName) {
		Enumeration<?> enumeration = rootNode.children();
		BatchTreeNode node = null;
		while (enumeration.hasMoreElements()) {
			node = (BatchTreeNode) enumeration.nextElement();
			if (node.toString().equals(nodeName)) {
				return node;
			}
		}
		return null;
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
	 * @see com.yinzhijie.util.IConfirmAction#setToolBarPanel(com.yinzhijie.util.ToolBarPanel)
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		this.toolBarPanel = (ScanToolBarPanel) toolBarPanel;
	}
	
}
