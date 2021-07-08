/**
 * ClockwiseRotateAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-23
 */
package com.yzj.wf.scan.action;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.yzj.wf.scan.item.ShowProcessWindow;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ImageStoreContext;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.JIThumbnailCache;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 正向90°旋转
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * 
 * 经测试发现，读取4752X3164图片时 全图显示时，如果重新去读取BufferedImage时，系统会OOM 所以直接用<CODE>JImagePanel</CODE>提供source进行修改，
 * 节约资源，先添加新的方法，如下：
 * <p>
 * <i> planeRotoImage(BufferedImage imagebuffer, File temp_file) <i>
 * @author LiuQiangQiang
 * @version 1.0.1
 */
public class ClockwiseRotateAction implements IConfirmAction {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ClockwiseRotateAction.class);

	private ImageNode[] imageNodes;

	private ToolBarPanel toolBarPanel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#confirm()
	 */
	public boolean confirm() {
		ImageStoreContext storeContext = toolBarPanel.getAppletPanel().getSysCacheContext().getImageStoreContext();
		// 核查是否选择多个图片
		if (storeContext.getSelectedImageNode() == null || storeContext.getSelectedImageNode().length == 0) {
			JOptionPane.showMessageDialog(null, ParamDefine.rotateNotice_a, ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		imageNodes = storeContext.getSelectedImageNode();
		if (imageNodes == null || imageNodes.length == 0)
			return false;

		new Thread() {
			public void run() {
				ShowProcessWindow.instance().startProcessWindow(ParamDefine.rotating);
			}
		}.start();

		new Thread() {
			public void run() {
				String message = null;
				try {
					for (int i = 0; i < imageNodes.length; i++)
						planeRotoImage(imageNodes[i]);
					message = ParamDefine.rotateSuccess;
				} catch (Exception e) {
					message =ParamDefine.rotateFail + e.getMessage();
					log.error(message);
					JOptionPane.showMessageDialog(null, message, ParamDefine.noticeMessage, JOptionPane.ERROR_MESSAGE);
				} finally {
					ShowProcessWindow.instance().stopProcessWindow();
				}
			}
		}.start();
		return true;
	}

	/**
	 * 单张影像正向旋转90°
	 * @param imageNode 图片节点 不能为空
	 * @return 旋转成功返回true,否则返回false
	 * @throws Exception 图片处理抛出异常
	 */
	public boolean planeRotoImage(ImageNode imageNode) throws Exception {
		if (imageNode == null)
			return false;

		// 判定图片类型
		if (!ImageUtility.isSupportedImage(imageNode.getSuffix()))
			return false;

		// 图片旋转
		File temp_file = imageNode.getFile();
		BufferedImage imagebuffer = ImageUtility.openImage(imageNode);
		if (imagebuffer == null)
			return false;
		planeRotoImage(imagebuffer, temp_file);

		// 设置图片改变
//		if (imageNode.getDocObject() != null)
//			imageNode.setPicchangeFlag(0x04);

		// 移除cache
		JIThumbnailCache.instance().removeCacheSmallIcon(imageNode);

		return true;
	}

	/**
	 * 正向90°旋转影像，如果temp_file不为空的话，保存修改后的影像 否则修改不在本地保存
	 * @param imagebuffer 影像源
	 * @param temp_file 写入影像本地文件
	 * @throws Exception 旋转文件时 抛出异常
	 */
	public void planeRotoImage(BufferedImage imagebuffer, File temp_file) throws Exception {
		AffineTransform transform = new AffineTransform();
		transform.translate(imagebuffer.getHeight(), 0);
		transform.rotate(Math.toRadians(90));
		imagebuffer = ImageUtility.planeRotoImage(imagebuffer, transform);
		// 保存图片
		if (temp_file != null)
			ImageIO.write(imagebuffer, ImageUtility.suffix(temp_file.getName()), temp_file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#getStructuredSelection()
	 */
	public StructuredSelection getStructuredSelection() {
		StructuredSelection selection = new StructuredSelection(imageNodes, 0);
		return selection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public boolean isNotifyListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		this.toolBarPanel = toolBarPanel;
	}
	
}
