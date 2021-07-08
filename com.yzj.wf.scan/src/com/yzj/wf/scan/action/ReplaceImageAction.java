package com.yzj.wf.scan.action;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.init.ScanToolBarPanel;
import com.yzj.wf.scan.item.ShowProcessWindow;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.service.IUploadService;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 * 创建于:2012-8-11<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 上传影像至存储
 * 
 * @author 陈林江
 * @version 1.0
 */
public class ReplaceImageAction implements IConfirmAction {

	private ScanToolBarPanel toolBarPanel;

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ReplaceImageAction.class);
	private IUploadService upService;
	private static ReplaceImageAction instance=new ReplaceImageAction();
	
	private ReplaceImageAction(){
		
	}
	public static ReplaceImageAction getInstance(){
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		final BatchScanView view = toolBarPanel.getView();
		final BatchTree tree = view.getBatchTree();
		boolean checkResult = checkAllNode(tree);
		if (!checkResult) {
			return false;
		}
		int result = JOptionPane.showConfirmDialog(view,
				ParamDefine.saveNotice_a
						+ tree.getAllUnUploadImageNode().size()
						+ ParamDefine.saveNotice_b
						+tree.getAllUnUploadBizNum() + ParamDefine.saveNotice_c,
				ParamDefine.noticeMessage, JOptionPane.OK_CANCEL_OPTION);
		if (result != 0) {
			return false;
		}
		new Thread("影像上传进度条线程") {
			public void run() {
				ShowProcessWindow.instance().startProcessWindow(
						ParamDefine.uploading);
			}
		}.start();

		new Thread("影像上传进度条线程") {
			public void run() {
				List<BatchTreeNode> nodes = tree.getAllUnUploadImageNode();
				int all = nodes.size();
				int finish = 0;
				for (int i = 0; i < nodes.size();) { // 循环上传图像
					ShowProcessWindow.instance().setMessage(
							"(" + finish + "/" + all / ParamDefine.billNum
									+ ")");
					List<ImageNode> list = new ArrayList<ImageNode>();
					for (int j = 0; j < ParamDefine.billNum; j++) {
						list.add(nodes.remove(0).getImageNode());
					}
					try {
						upService.upload(list);
					} catch (Exception e) {
						ShowProcessWindow.instance().stopProcessWindow();
						JOptionPane.showMessageDialog(view, e.getMessage()
								+ ParamDefine.finshed + finish
								+ ParamDefine.datas, ParamDefine.noticeMessage,
								JOptionPane.WARNING_MESSAGE);
						return;
					}

					for (int j = 0; j < list.size(); j++) {
						list.get(j).setUploaded(true);
					}
					tree.repaint();
					finish++;
				}
				ShowProcessWindow.instance().stopProcessWindow();
				JOptionPane.showMessageDialog(view, ParamDefine.uploadFinish,
						ParamDefine.noticeMessage,
						JOptionPane.INFORMATION_MESSAGE);
			}
		}.start();
		return true;
	}

	/**
	 * 影像合法性检查
	 * 
	 * @param tree
	 * @return
	 */
	private boolean checkAllNode(BatchTree tree) {
		String message = "";
		boolean result = true;
		List<BatchTreeNode> nodes = tree.getAllUnUploadImageNode();
		if (nodes.size() == 0) {
			message = ParamDefine.noImageToUpload;
			result = false;
		} else if (ParamDefine.autoFormat) {
			if (nodes.size() % ParamDefine.billNum != 0) {// 如果开启了自动格式化,总影像的数目必须是每笔业务所含影像树的倍数
				message = ParamDefine.saveNotice_d + nodes.size()
						+ ParamDefine.saveNotice_e + ParamDefine.billNum
						+ ParamDefine.saveNotice_f;
				result = false;
			} else {
				for (int i = 0; i < nodes.size();) { // 每次取billNum个图像节点，若他们的父节点不是同一个,说明有问题
					BatchTreeNode parent = nodes.get(i).getParent();
					i++;
					for (int j = 1; j < ParamDefine.billNum; i++, j++) {
						if (nodes.get(i).getParent() != parent) {
							message = ParamDefine.saveNotice_g
									+ parent.toString()
									+ ParamDefine.saveNotice_h
									+ parent.getChildCount()
									+ ParamDefine.saveNotice_i;
							result = false;
							i = nodes.size();
							break;
						}
					}

				}
			}
		}
		if (!"".equals(message)) {
			logger.warn(message);
			JOptionPane.showMessageDialog(toolBarPanel.getView(), message,
					ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
		}
		return result;
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

	public IUploadService getUpService() {
		return upService;
	}

	public void setUpService(IUploadService upService) {
		this.upService = upService;
	}
	

}
