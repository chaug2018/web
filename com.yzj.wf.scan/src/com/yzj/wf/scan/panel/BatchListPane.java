/**
 * BatchListPane.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2012-2-14
 */
package com.yzj.wf.scan.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.ui.selection.ISelection;
import com.yzj.wf.scan.ui.selection.ISelectionChangedListener;
import com.yzj.wf.scan.ui.selection.SelectionChangedEvent;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.AbstractNodeList;
import com.yzj.wf.scan.util.INodeListModel;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 批量扫描View
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class BatchListPane extends AbstractNodeList {

	private static final long serialVersionUID = 1L;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BatchListPane.class);

	private JImagePanel imagePanel;

	protected BatchScanView batchScanView;
	
	private static BatchListPane instance=null;

	public BatchListPane(BatchScanView batchScanView) {
		this.batchScanView = batchScanView;
		initGUI();
		instance=this;
	}
	public static JImagePanel getShowImagePanel(){
		return instance.getImagePanel();
	}

	private void initGUI() {
		logger.info("Init GUI BatchListPane Start");
		double vZoomFactor = Double.parseDouble(batchScanView.getReadConfig().getPropertyValue("vZoomFactor", "0.15"));
		boolean vWinFitScreen = Boolean.parseBoolean(batchScanView.getReadConfig().getPropertyValue("vWinFitScreen", "true"));
		imagePanel = new JImagePanel(true, false, false, vZoomFactor, vWinFitScreen);
		setLayout(new BorderLayout());
		this.add(imagePanel, BorderLayout.CENTER);
		logger.info("Init GUI BatchListPane End");
		setMinimumSize(new Dimension(400, 100));
		setBackground(Color.LIGHT_GRAY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionChangedListener#selectionChanged(com.yinzhijie.ui.selection.SelectionChangedEvent)
	 */
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSource() instanceof BatchTree) {
			StructuredSelection selection = (StructuredSelection) event.getSelection();
			BatchTreeNode node = (BatchTreeNode) selection.getLastElement();
			if (node != null) {
				logger.debug("get Messgae from BatchTree, node is " + node.toString() + ", imagePane display imagenode");
				ImageNode imageNode = node.getImageNode();
				imagePanel.display(imageNode);
			} else {
				logger.debug("get Messgae from BatchTree, node is null");
			}
		}
		if (event.getSource() instanceof ToolBarPanel) {
			StructuredSelection selection = (StructuredSelection) event.getSelection();
			Object object = selection.getLastElement();
			if (object instanceof TreePath) {
				TreePath path = (TreePath) selection.getLastElement();
				BatchTreeNode node = (BatchTreeNode) path.getLastPathComponent();
				logger.debug("get Message from ToolBarPanel, the node is " + node);
				if (node != null && node.getImageNode() != null) {
					logger.debug("refresh the imagePane display");
					imagePanel.display(node.getImageNode());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.INodeList#getListModel()
	 */
	public INodeListModel getListModel() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.INodeList#restoreSelection(com.yinzhijie.util.ImageNode)
	 */
	public void restoreSelection(ImageNode lastSelectNode) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#addSelectionChangedListener(com.yinzhijie.ui.selection.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#getProviderId()
	 */
	public String getProviderId() {
		return "BatchListPane";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#removeSelectionChangedListener(com.yinzhijie.ui.selection.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#setSelection(com.yinzhijie.ui.selection.ISelection)
	 */
	public void setSelection(ISelection selection) {
	}

	public JImagePanel getImagePanel() {
		return imagePanel;
	}
	
}
