package com.yzj.wf.scan.gui.batchtree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.ISelection;
import com.yzj.wf.scan.ui.selection.ISelectionChangedListener;
import com.yzj.wf.scan.ui.selection.ISelectionProvider;
import com.yzj.wf.scan.ui.selection.SelectionChangedEvent;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 * 创建于:2012-8-9<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 影像树
 * @author 陈林江 
 * @version 1.0.1
 */
public class BatchTree extends JTree implements ISelectionChangedListener,
		ISelectionProvider {

	private static final long serialVersionUID = 1894722526755104603L;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BatchTree.class);

	public BatchScanView getView() {
		return view;
	}

	public void setView(BatchScanView view) {
		this.view = view;
	}

	private Vector<ISelectionChangedListener> selectionChangeListener;

	private boolean changeFlag;

	protected BatchScanView view;

	private TreePath[] old_paths;

	private List<BatchTreeNode> nodeList;
	private static BatchTree bTree;
	public boolean isAllExpanded=false;

	public static BatchTree getInstance() {
		return bTree;
	}

	public BatchTree(BatchScanView view) {
		this(null, view);
	}

	public BatchTree(DefaultTreeModel model, BatchScanView view) {
		super(model);
		bTree = this;
		putClientProperty("JTree.lineStyle", "Angled");
		selectionChangeListener = new Vector<ISelectionChangedListener>(1);
		this.view = view;
		setEditable(false);
		setCellRenderer(new BatchTreeCellRenderer());
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

		addMouseListener(new BatchreeNodeSelectionListener());
		addTreeSelectionListener(new BatchTreeSelection());

	}

	public synchronized void setChanged() {
		this.changeFlag = true;
	}

	public synchronized void clearChanged() {
		this.changeFlag = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yinzhijie.ui.selection.ISelectionChangedListener#selectionChanged
	 * (com.yinzhijie.ui.selection.SelectionChangedEvent)
	 */
	public synchronized void selectionChanged(SelectionChangedEvent event) {
		Object object = event.getSource();
		StructuredSelection selection = (StructuredSelection) event
				.getSelection();
		DefaultTreeModel model = ((DefaultTreeModel) BatchTree.getInstance()
				.getModel());
		if (object instanceof ToolBarPanel) {
			Object temp_obj = selection.getLastElement();
			if (temp_obj instanceof File) {
				File file = (File) temp_obj;
				ImageNode imageNode = new ImageNode(file);
				
				/*
				 * 添加影像
				 */
				BatchTreeNode insertNode = view.getSysCacheContext()
						.getInsertNode();
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
					parentNode.insert(newNode, insertPoint); // 插入一个新节点
					view.getSysCacheContext().setInsertNode(newNode);
					insertFormatTree(); // 格式化树
				} else { // 新增操作
					int nodesLength = getFirstNode().getChildNodes().length;
					if (nodesLength > 0 // 最后一个节点的子节点小于等于每笔业务的票据数
							&& getFirstNode().getChildNodes()[nodesLength - 1]
									.getChildNodes().length < ParamDefine.billNum) {
						parentNode = getFirstNode().getChildNodes()[nodesLength - 1];
						newNode = new BatchTreeNode(imageNode);
					} else {
						parentNode = new BatchTreeNode(ParamDefine.billName
								+ (nodesLength + 1));
						model.insertNodeInto(parentNode, getFirstNode(), getFirstNode().getChildCount());
						newNode = new BatchTreeNode(imageNode);
					}
					model.insertNodeInto(newNode, parentNode, parentNode.getChildCount());
					if(!isExpanded(0)){
					expandPath(new TreePath(getFirstNode()));
					}
				}
				repaint();
			}
		}
	}

	/**
	 * 插入节点时的树的格式化处理
	 */
	public synchronized void insertFormatTree() {
		
		BatchTreeNode root = getFirstNode();
		DefaultTreeModel model = ((DefaultTreeModel) BatchTree.getInstance()
				.getModel());
//		this.updateUI();
		if(!ParamDefine.autoFormat){  //不需要自动格式化
			expandPath(new TreePath(getFirstNode()));
			this.repaint();
			return;
		}
		for (int i = 0; i < root.getChildCount(); i++) {
			BatchTreeNode node = root.getChildNodes()[i];
			node.setUserObject(ParamDefine.billName + (i + 1));
			int downNum=0;  //已经下沉的节点数
			for (int j = 0; j < node.getChildNodes().length; j++) {
				if (j < ParamDefine.billNum) {
					model.nodeStructureChanged(node);
				} 
				else {   //该业务节点下的余下影像节点需下移
					if (i == root.getChildCount() - 1) {// 已到最后一个业务节点			
						BatchTreeNode parentNode = new BatchTreeNode();
						model.insertNodeInto(parentNode, root,
								root.getChildCount());
					}
					model.insertNodeInto(node.getChildNodes()[j],
							root.getChildNodes()[i + 1], j+downNum
									- ParamDefine.billNum);
					j--;
					downNum++;
					model.nodeStructureChanged(node);
				}			
			}	
		}
		expandPath(new TreePath(getFirstNode()));
		repaint();
	}
	
	public void formatAllNode(){
		DefaultTreeModel model = ((DefaultTreeModel) BatchTree.getInstance()
				.getModel());
		List<BatchTreeNode> imageNodes=getAllImageNode();
		getFirstNode().removeAllChildren();
		model.reload(getFirstNode());
		for (BatchTreeNode batchTreeNode : imageNodes) {
			BatchTreeNode parentNode=null;
			int nodesLength = getFirstNode().getChildNodes().length;
			if (nodesLength > 0 // 最后一个节点的子节点小于等于每笔业务的票据数
					&& getFirstNode().getChildNodes()[nodesLength - 1]
							.getChildNodes().length < ParamDefine.billNum) {
				parentNode = getFirstNode().getChildNodes()[nodesLength - 1];
			} else {
				parentNode = new BatchTreeNode(ParamDefine.billName
						+ (nodesLength + 1));
				model.insertNodeInto(parentNode, getFirstNode(), getFirstNode().getChildCount());
			}
			model.insertNodeInto(batchTreeNode, parentNode, parentNode.getChildCount());
		}
		expandPath(new TreePath(getFirstNode()));
	}


	public BatchTreeNode getFirstNode() {
		BatchTreeNode node = (BatchTreeNode) ((BatchTreeNode) (this.getModel()
				.getRoot()));
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yinzhijie.ui.selection.ISelectionProvider#addSelectionChangedListener
	 * (com.yinzhijie.ui.selection.ISelectionChangedListener)
	 */

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionChangeListener.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#getProviderId()
	 */

	public String getProviderId() {
		return "BatchTree";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.ui.selection.ISelectionProvider#getSelection()
	 */

	public ISelection getSelection() {
		StructuredSelection selection = null;
		TreePath[] paths = getSelectionPaths();
		if (paths == null) {
			selection = new StructuredSelection();
		} else {
			BatchTreeNode[] treeNodes = new BatchTreeNode[paths.length];
			for (int i = 0; i < paths.length; i++) {
				treeNodes[i] = (BatchTreeNode) paths[i].getLastPathComponent();
			}
			selection = new StructuredSelection(treeNodes, treeNodes.length - 1);
		}
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yinzhijie.ui.selection.ISelectionProvider#removeSelectionChangedListener
	 * (com.yinzhijie.ui.selection.ISelectionChangedListener)
	 */

	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		int index = selectionChangeListener.indexOf(listener);
		if (index > -1) {
			selectionChangeListener.remove(index);
		}
	}

	public void BatchTreeChange(TreeSelectionEvent e) {
		ISelectionChangedListener[] seListeners;
		synchronized (BatchTree.class) {
			if (!this.changeFlag) {
				return;
			}
			seListeners = new ISelectionChangedListener[selectionChangeListener
					.size()];
			seListeners = selectionChangeListener.toArray(seListeners);
		}
		SelectionChangedEvent event = new SelectionChangedEvent(this,
				getSelection());
		for (int i = 0; i < seListeners.length; i++) {
			seListeners[i].selectionChanged(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yinzhijie.ui.selection.ISelectionProvider#setSelection(com.yinzhijie
	 * .ui.selection.ISelection)
	 */

	public void setSelection(ISelection selection) {
		selection = null;
	}

	final class BatchTreeSelection implements
			javax.swing.event.TreeSelectionListener {
		public void valueChanged(final TreeSelectionEvent e) {
			if (!e.isAddedPath())
				return;
			final Thread runner = new Thread() {
				public void run() {
					Runnable runnable = new Runnable() {
						public void run() {
							setChanged();
							BatchTreeChange(e);
							clearChanged();
						}
					};
					SwingUtilities.invokeLater(runnable);
				}
			};
			runner.start();
		}
	}

	public void clearSelectNode() {
		@SuppressWarnings("unused")
		BatchTreeNode node = null;
		if (old_paths != null) {
			for (int i = 0; i < old_paths.length; i++) {
				node = (BatchTreeNode) old_paths[i].getLastPathComponent();
			}
		}
	}

	final class BatchreeNodeSelectionListener extends MouseAdapter {
		public void mousePressed(MouseEvent event) {
			JTree tree = (JTree) event.getSource();

			TreePath treePath = tree.getSelectionPath();
			int clickRow = tree.getRowForLocation(event.getX(), event.getY()); // 鼠标点击的行数
			TreePath path = tree.getPathForLocation(event.getX(), event.getY());
			if (clickRow == -1) { // 未点击在节点上
				return;
			}
			/**
			 * 满足以下条件的点击无效
			 */
//			if(clickRow==0&&event.getClickCount()==1&&event.getButton()==1&&event.getX()>20){
//				return;
//			}
			if(event.getButton()==1&&clickRow==0&&event.getX()>20){
				return;
			}
			if (event.getButton() == 3) { // 右键点击，展开或收拢节点
				if (tree.isExpanded(path)) {
					collapseNodes(path);
					collapsePath(path);

				} else {
					expandNodes(path);
				}
				return;
			}
			if (treePath != null) {
				BatchTreeNode selectedNode = (BatchTreeNode) treePath
						.getLastPathComponent();
				if (selectedNode.getImageNode() != null) {
					// if (event.getClickCount() == 2) {
					// nodeList = new ArrayList<BatchTreeNode>();
					// BatchTreeNode rootNode = (BatchTreeNode) tree
					// .getModel().getRoot();
					// getImageNodeList(rootNode);// 获取包含所有图片叶子节点的List
					// if (nodeList != null && nodeList.size() > 0) {
					// ImageNode[] imageNodes = new ImageNode[nodeList
					// .size()];
					// for (int i = 0; i < nodeList.size(); i++) {
					// imageNodes[i] = nodeList.get(i).getImageNode();
					// imageNodes[i].setIndex(i);// 建立索引
					// }
					// // 大图显示
//					 new ImageViewer(new OrderImageNodeList(imageNodes,
					// selectedNode.getImageNode().getIndex()),
					// true, false, true, view);
					// }
					// }
				} else { // 若不是图片节点，且为双击，则展开或收拢该节点
					// if (event.getClickCount() == 2) {
					// if(clickRow==0){
					// expandNodes(treePath);
					// }else
					// expandNodes(treePath);
					// }
				}
			} else {
				return;
			}
			TreePath[] paths = tree.getSelectionPaths();
			if (paths != null) {
				BatchTreeNode oldNode = null;
				BatchTreeNode newNode = null;
				boolean same = false;
				if (old_paths != null) {
					if (paths.length == 1 && old_paths.length == 1) {
						oldNode = (BatchTreeNode) old_paths[0]
								.getLastPathComponent();
						newNode = (BatchTreeNode) paths[0]
								.getLastPathComponent();
						if (oldNode==newNode) {
							same = true;
							logger.debug("new node is same as old node: "
									+ oldNode);
							oldNode.setSelected(!oldNode.isSelected());
							// ((DefaultTreeModel) tree.getModel())
							// .nodeStructureChanged(oldNode);
						} else {
							changeOldNode(tree);
						}
					} else {
						changeOldNode(tree);
					}
				} else {
					logger.debug("no old tree node is selected");
				}
				if (!same) {
					changeNewNolde(tree, paths);
				}
				old_paths = paths;
			} else {
				logger.debug("paths is null"); // 当前点击时没有选择任何节点
			}
			tree.repaint();
		}
	}

	private void changeOldNode(JTree tree) {
		BatchTreeNode oldNode = null;
		for (int i = 0; i < old_paths.length; i++) {
			oldNode = (BatchTreeNode) old_paths[i].getLastPathComponent();
			if (oldNode != null) {
				logger.debug("old node is " + oldNode);
				oldNode.setSelected(false);
			}
		}
//		if (oldNode != null)
//			((DefaultTreeModel) tree.getModel()).nodeChanged(oldNode);
	}

	private void changeNewNolde(JTree tree, TreePath[] paths) {
		BatchTreeNode newNode = null;
		for (int i = 0; i < paths.length; i++) {
			newNode = (BatchTreeNode) paths[i].getLastPathComponent();
			if (newNode != null) {
				logger.debug("new node is " + newNode);
				newNode.setSelected(true);
			} else {
				logger.debug("no tree node is selected");
			}
		}
//		if (newNode != null)
//			((DefaultTreeModel) tree.getModel()).nodeStructureChanged(newNode);
	}

	/**
	 * 将树形结构的所有图片格式的叶子节点添加到nodeList里
	 * 
	 * @param rootNode
	 *            树形结构的根节点
	 */
	@SuppressWarnings("unused")
	private void getImageNodeList(BatchTreeNode rootNode) {
		if (rootNode.getChildCount() > 0) {
			for (int i = 0; i < rootNode.getChildCount(); i++) {
				BatchTreeNode node = (BatchTreeNode) rootNode.getChildAt(i);
				getImageNodeList(node);
			}
		} else {
			ImageNode imageNode = rootNode.getImageNode();
			if (imageNode != null) {
				nodeList.add(rootNode);
			}
		}
	}

	/**
	 * 获取所有的影像节点
	 * 
	 * @return 影像节点集合
	 */
	public List<BatchTreeNode> getAllImageNode() {
		List<BatchTreeNode> result = new ArrayList<BatchTreeNode>();
		BatchTreeNode[] parentNodes = getFirstNode().getChildNodes();
		for (BatchTreeNode batchTreeNode : parentNodes) {
			BatchTreeNode[] nodes = batchTreeNode.getChildNodes();
			for (BatchTreeNode batchTreeNode2 : nodes) {
				result.add(batchTreeNode2);
			}
		}
		return result;
	}

	/**
	 * 获取所有的未上传的影像节点
	 * 
	 * @return 未上传的影像节点集合
	 */
	public List<BatchTreeNode> getAllUnUploadImageNode() {
		List<BatchTreeNode> result = new ArrayList<BatchTreeNode>();
		BatchTreeNode[] parentNodes = getFirstNode().getChildNodes();
		for (BatchTreeNode batchTreeNode : parentNodes) {
			BatchTreeNode[] nodes = batchTreeNode.getChildNodes();
			for (BatchTreeNode batchTreeNode2 : nodes) {
				if (!batchTreeNode2.getImageNode().isUploaded())
					result.add(batchTreeNode2);
			}
		}
		return result;
	}
	
	/**
	 * 获取所有的未上传的影像节点
	 * 
	 * @return 未上传的影像节点集合
	 */
	public int getAllUnUploadBizNum() {
		int number=0;
		BatchTreeNode[] parentNodes = getFirstNode().getChildNodes();
		for (BatchTreeNode batchTreeNode : parentNodes) {
			BatchTreeNode[] nodes = batchTreeNode.getChildNodes();
			for (BatchTreeNode batchTreeNode2 : nodes) {
				if (!batchTreeNode2.getImageNode().isUploaded())
					number++;
				break;
			}
		}
		return number;
	}

	/**
	 * 清除没有子节点的业务节点
	 */
	public void clearNoChildNodes() {
		BatchTreeNode root = (BatchTreeNode) this.getModel().getRoot();
		for (BatchTreeNode node : root.getChildNodes()) {
			if (node.getImageNode() == null && node.getChildCount() == 0) {
				((DefaultTreeModel) this.getModel()).removeNodeFromParent(node);
				insertFormatTree();
			}
		}
	}

	/**
	 * 展开树结构
	 */
	public void expandTree() {
		BatchTreeNode root = (BatchTreeNode) this.getModel().getRoot();
		expandNodes(new TreePath(root));
	}

	/**
	 * 递归展开所有节点
	 * 
	 * @param parent
	 *            父节点TreePath
	 */
	public void expandNodes(TreePath parent) {
		BatchTreeNode node = (BatchTreeNode) parent.getLastPathComponent();
		if (node.getChildCount() > 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				BatchTreeNode childNode = (BatchTreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(childNode);
				expandNodes(path);
			}
		}
		expandPath(parent);
	}

	/**
	 * 递归折叠所有节点
	 * 
	 * @param parent
	 *            父节点TreePath
	 */
	public void collapseNodes(TreePath parent) {
		BatchTreeNode node = (BatchTreeNode) parent.getLastPathComponent();
		if (node.getChildCount() > 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				BatchTreeNode childNode = (BatchTreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(childNode);
				collapseNodes(path);
			}
		}
		if (parent.getPathCount() != 1) {
			collapsePath(parent);
		}
	}

}
