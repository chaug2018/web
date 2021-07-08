package com.yzj.wf.scan.action;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.item.ShowProcessWindow;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.ReadConfig;
import com.yzj.wf.scan.util.SwingFileFilter;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 新增影像事件处理类
 * @author 陈林江
 * @version 1.0.1
 */
public class BatchAddFileAction implements IConfirmAction {

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BatchAddFileAction.class);
	private ScanToolBarPanel toolBarPanel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		BatchScanView view = toolBarPanel.getView();
		BatchTree tree = view.getBatchTree();
		BatchTreeNode insertNode = null;
		String message = "";
		TreePath[] treePaths = tree.getSelectionPaths();
		boolean isRoot = false;
		if (treePaths != null) {
			isRoot = ((BatchTreeNode) treePaths[treePaths.length - 1]
					.getLastPathComponent()).getParent() == null;
		}
		if (treePaths == null || treePaths.length == 0 || isRoot) { // 在树的末尾添加节点
			message = ParamDefine.addAtEnd;
		} else {
			message = ParamDefine.confireInsertAtNode_s
					+ treePaths[treePaths.length - 1].toString()
					+ ParamDefine.confireInsertAtNode_e;
			insertNode = (BatchTreeNode) treePaths[treePaths.length - 1]
					.getLastPathComponent();
		}
		int result = JOptionPane.showConfirmDialog(view, message, ParamDefine.confireMessage,
				JOptionPane.OK_CANCEL_OPTION);
		if (result == 0) {
			ReadConfig config = view.getReadConfig();
			String lasetFilePath = config.getPropertyValue("last_choosefile",
					"");
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setMultiSelectionEnabled(true);
			fileChooser.setCurrentDirectory(new File(lasetFilePath));
			fileChooser.setFileFilter(new SwingFileFilter());
			fileChooser.setDialogTitle(ParamDefine.fileSelection);
			result = fileChooser.showDialog(view, ParamDefine.confire);
			if (result == JFileChooser.APPROVE_OPTION) {
				logger.debug("user choose the file");
				final File[] files = fileChooser.getSelectedFiles();
				if (files != null && files.length != 0) {
					new Thread("影像处理进度条线程") {
						public void run() {
							ShowProcessWindow.instance().startProcessWindow(
									ParamDefine.dealingImage);
						}
					}.start();

					final int maxh = Integer.parseInt(config.getPropertyValue(
							"PicMaxHeight", "2500"));
					final int maxw = Integer.parseInt(config.getPropertyValue(
							"PicMaxWidth", "2500"));
					final File tempDir = new File(config.getPropertyValue(
							"batchTempFile",
							System.getProperty("java.io.tmpdir")+"yzj"));
					if(!tempDir.exists()){
						tempDir.exists();
					}
					final BatchTreeNode fInsertNode = insertNode;
					new Thread("影像处理线程") {
						public void run() {
							BatchTreeNode localInsertNode = fInsertNode;
							try {
								for (int i = 0; i < files.length; i++) {
									ShowProcessWindow.instance().setMessage("("+(i+1)+"/"+files.length+")");
									files[i] = ImageUtility.bigToSmallImage(
											files[i], maxh, maxw, tempDir);
									logger.debug("new file is "
											+ files[i].getAbsolutePath());
								}
								ShowProcessWindow.instance()
								.stopProcessWindow();
								for (int i = 0; i < files.length; i++) {
									localInsertNode = addNode(files[i],
											localInsertNode,
											i == files.length - 1);
								}
								if (toolBarPanel.getView().getBatchTree()
										.getAllImageNode().size()
										% ParamDefine.billNum != 0&&ParamDefine.autoFormat) {
									String message = (ParamDefine.noticeAtAddEnd_a
											+ toolBarPanel.getView()
													.getBatchTree()
													.getAllImageNode().size()
											+ ParamDefine.noticeAtAddEnd_b
											+ ParamDefine.billNum + ParamDefine.noticeAtAddEnd_c);
									JOptionPane.showMessageDialog(
											toolBarPanel.getView(), message,
											ParamDefine.notice, JOptionPane.WARNING_MESSAGE);
								}
							} catch (Exception e) {
								logger.error("影像处理出现错误",e);
							} finally {
								ShowProcessWindow.instance()
										.stopProcessWindow();
							}
						}
					}.start();

				} else {
					return false;
				}
			} else {
				return false;
			}
			if (treePaths != null) {
				for (TreePath treePath : treePaths) {
					((BatchTreeNode) treePath.getLastPathComponent())
							.setSelected(false);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private BatchTreeNode addNode(File file, BatchTreeNode insertNode,
			boolean isLast) {
		BatchTree tree = BatchTree.getInstance();
		ImageNode imageNode = new ImageNode(file);
		DefaultTreeModel model = ((DefaultTreeModel)tree.getModel());
		/*
		 * 添加影像
		 */
		BatchTreeNode parentNode = null;
		BatchTreeNode newNode = null;
		int insertPoint = 0; // 插入点
		if (insertNode != null) { // 插入影像
			if (insertNode.getImageNode() == null) { // 插入点不是叶子节点
				parentNode = insertNode;
			} else {
				parentNode = insertNode.getParent();
				insertPoint=parentNode.getIndex(insertNode)+1;
			}
			newNode = new BatchTreeNode(imageNode);
			model.insertNodeInto(newNode, parentNode, insertPoint);// 插入一个新节点
			insertNode = newNode;
		} else { // 新增操作
			int nodesLength = tree.getFirstNode().getChildNodes().length;
			if (nodesLength > 0 // 最后一个节点的子节点小于等于每笔业务的票据数
					&& tree.getFirstNode().getChildNodes()[nodesLength - 1]
							.getChildNodes().length < ParamDefine.billNum) {
				parentNode = tree.getFirstNode().getChildNodes()[nodesLength - 1];
				newNode = new BatchTreeNode(imageNode);
			} else {
				parentNode = new BatchTreeNode();
				model.insertNodeInto(parentNode, tree.getFirstNode(), tree.getFirstNode().getChildCount());
				tree.getFirstNode().add(parentNode);
				newNode = new BatchTreeNode(imageNode);
			}
			model.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
		}
		if (isLast) {// 添加完最后一个节点，格式化树
			tree.insertFormatTree(); // 格式化树
		}
		tree.repaint();
		return insertNode;

	}

	@SuppressWarnings("unused")
	private void saveFiles(final File[] files, final ReadConfig config,
			final BatchScanView view, final BatchTreeNode node) {
		new Thread("影像添加进度条线程") {
			public void run() {
				ShowProcessWindow.instance().startProcessWindow(ParamDefine.addingImage);
			}
		}.start();

		new Thread("影像添加处理线程") {
			public void run() {
				int maxh = Integer.parseInt(config.getPropertyValue(
						"PicMaxHeight", "2500"));
				int maxw = Integer.parseInt(config.getPropertyValue(
						"PicMaxWidth", "2500"));
				File tempDir = new File(config.getPropertyValue(
						"batchTempFile", System.getProperty("java.io.tmpdir")));
				logger.debug("this user set maxh is " + maxh
						+ " , the maxw is " + maxw + ", the tempDir is "
						+ tempDir.getAbsolutePath());
				DefaultTreeModel defaultTreeModel = (DefaultTreeModel) view
						.getBatchTree().getModel();
				int[] childIndices = new int[files.length];
				try {
					for (int i = 0; i < files.length; i++) {
						files[i] = ImageUtility.bigToSmallImage(files[i], maxh,
								maxw, tempDir);
						logger.debug("new file is "
								+ files[i].getAbsolutePath());
					}
					for (int i = 0; i < files.length; i++) {
						childIndices[i] = node.getChildCount();
						node.add(new BatchTreeNode(files[i]));
					}
				} catch (Exception e) {
					logger.error("copy files error", e);
				} finally {
					ShowProcessWindow.instance().stopProcessWindow();
					// BatchTreeNode treeNode = (BatchTreeNode)
					// node.getChildAt(node.getChildCount() - 1);
					defaultTreeModel.nodesWereInserted(node, childIndices);
					node.setSelected(false);
					view.getBatchTree().expandTree(); // 展开树结构
					// treeNode.setSelected(true);
					// view.getBatchTree().setSelectionPath(new
					// TreePath(treeNode));
				}
			}
		}.start();
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
