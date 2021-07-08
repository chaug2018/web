/**
 * AntiClockWiseAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2012-2-17
 */
package com.yzj.wf.scan.action;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.item.ShowProcessWindow;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 逆时针旋转
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class BatchAntiClockWiseRotateAction implements IConfirmAction {

	private ScanToolBarPanel toolBarPanel;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BatchAntiClockWiseRotateAction.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		BatchTree tree = toolBarPanel.getView().getBatchTree();
		final TreePath[] paths = tree.getSelectionPaths();
		if (paths == null || paths.length == 0) {
			logger.warn("没有选择需要反向旋转的影像");
			String temp = ParamDefine.rotateNotice_a;
			JOptionPane.showMessageDialog(toolBarPanel.getView(), temp, ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		boolean isSelected = false;
		for (TreePath path : paths) {
			BatchTreeNode selectedNode = (BatchTreeNode) path.getLastPathComponent();
			if (selectedNode.isSelected()&&selectedNode.getImageNode()!=null) {
				isSelected = true;
				break;
			}
		}
		if (!isSelected) {
			logger.warn("没有选择需要反向旋转的影像");
			String temp = ParamDefine.rotateNotice_b;
			JOptionPane.showMessageDialog(toolBarPanel.getView(), temp, ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		final BatchTreeNode node = (BatchTreeNode) paths[0].getLastPathComponent();
		if (node.getLevel() == 1 && node.getChildCount() == 0) {
			logger.warn("所选节点下没有可操作的影像");
			String temp = ParamDefine.rotateNotice_b;
			JOptionPane.showMessageDialog(toolBarPanel.getView(), temp, ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		new Thread("影像反向旋转进度条线程") {
			public void run() {
				ShowProcessWindow.instance().startProcessWindow(ParamDefine.rotating);
			}
		}.start();
			
		new Thread("影像反向旋转处理线程") {
			long curenttime = 0L;
			public void run() {
				try {
					curenttime = System.currentTimeMillis();
					RotoImage(paths);
					logger.info("影像反向旋转处理完成，系统耗时：" + (System.currentTimeMillis() - curenttime));
				} finally {
					while ((System.currentTimeMillis() - curenttime) < 100);
					ShowProcessWindow.instance().stopProcessWindow();
				}
			}
		}.start();
		return true;
		
	}

	public void RotoImage(final TreePath[] paths) {
		BatchTreeNode node = null;
		BufferedImage bufferedImage = null;
		for (int i = 0; i < paths.length; i++) {
			node = (BatchTreeNode) paths[i].getLastPathComponent();
			if (node != null) {
				if (node.getLevel() == 1 && node.isSelected()) { // 如果所选节点为父级节点，则操作其所有子节点
					BatchTreeNode[] level2Nodes = node.getChildNodes();
					for (BatchTreeNode level2Node : level2Nodes) {
						rotoImage(level2Node, bufferedImage);
					}
				} else {
					if (node.isSelected()) {
						rotoImage(node, bufferedImage);
					}
				}
			} else {
				logger.warn("get null node object from paths[" + i + "]");
			}
		}
		StructuredSelection selection = new StructuredSelection(paths, paths.length - 1);
		toolBarPanel.toolBarPanelChanged(toolBarPanel, selection);
	}
	
	private void rotoImage(BatchTreeNode node, BufferedImage bufferedImage) {
		ImageNode imageNode = node.getImageNode();
		if (imageNode == null) {
			logger.debug("imageNode is null,node is " + node.toString());
		} else {
			try {
				bufferedImage = ImageUtility.openImage(imageNode);
				bufferedImage = planeRotoImage(bufferedImage);
				ImageUtility.saveBufferedImage(bufferedImage, imageNode.getFile());
			} catch (Exception e) {
				logger.error("roto pic is error, ", e);
				throw new RuntimeException(e);
			} finally {
				logger.info("set bufferedImage is null, and set gc");
				bufferedImage = null;
				System.gc();
			}
		}
	}

	public BufferedImage planeRotoImage(BufferedImage imagebuffer) throws Exception {
		AffineTransform transform = new AffineTransform();
		transform = new AffineTransform(0, -1, 1, 0, 0, imagebuffer.getWidth() - 1);
		imagebuffer = ImageUtility.planeRotoImage(imagebuffer, transform);
		return imagebuffer;
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
