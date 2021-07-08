package com.yzj.wf.scan.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 * 创建于:2012-8-11<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 位置交换
 * 
 * @author 陈林江
 * @version 1.0
 */
public class ChangePozisionAction implements IConfirmAction {

	private ScanToolBarPanel toolBarPanel;

	private static ChangePozisionAction instance=new ChangePozisionAction();
	
	private ChangePozisionAction(){
		
	}
	public static ChangePozisionAction getInstance(){
		return instance;
	}

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
			showMessge = "<html>请选择<font color='red'>两张</font>需要交换位置的影像</html>";
			JOptionPane.showMessageDialog(view, showMessge, ParamDefine.noticeMessage,
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		List<BatchTreeNode> nodes= new ArrayList<BatchTreeNode>();
		for (TreePath path : treepaths) {
			BatchTreeNode selectedNode = (BatchTreeNode) path
					.getLastPathComponent();
			if (selectedNode.isSelected()&&selectedNode.getImageNode()!=null) {
				nodes.add(selectedNode);
			}
		}
		if (nodes.size()!=2) {
			showMessge ="<html>请选择<font color='red'>两张</font>需要交换位置的影像</html>";
			JOptionPane.showMessageDialog(view, showMessge, ParamDefine.noticeMessage,
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		

		showMessge = "确认交换选中的两张图片的位置?";
		int result = JOptionPane.showConfirmDialog(view, showMessge, ParamDefine.confireMessage,
				JOptionPane.OK_CANCEL_OPTION);
		if (JOptionPane.YES_OPTION == result) {
			File file=nodes.get(0).getImageNode().getFile();
			File file1=nodes.get(1).getImageNode().getFile();
			nodes.get(0).getImageNode().setFile(file1);
			nodes.get(1).getImageNode().setFile(file);
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
	 * @see com.yinzhijie.util.IConfirmAction#setToolBarPanel(com.yinzhijie.util.
	 *      ToolBarPanel)
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		this.toolBarPanel = (ScanToolBarPanel) toolBarPanel;
	}
}
