/**
 * AntiClockwiseRotateAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:Heisenberg 2012-3-9
 */
package com.yzj.wf.scan.action;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.yzj.wf.scan.item.ShowProcessWindow;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ImageStoreContext;
import com.yzj.wf.scan.util.ImageUtility;
import com.yzj.wf.scan.util.JIThumbnailCache;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 反向90°旋转
 * 
 * @author Heisenberg
 * @version 1.0.0
 */
public class AntiClockwiseRotateAction implements IConfirmAction {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AntiClockwiseRotateAction.class);

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
			JOptionPane.showMessageDialog(null, "<html>请选择至少<font color=red>1</font>张需要<font color=red>反向旋转</font>的影像资料！</html>", "提示信息", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}

		imageNodes = storeContext.getSelectedImageNode();
		if (imageNodes == null || imageNodes.length == 0)
			return false;

		new Thread() {
			public void run() {
				ShowProcessWindow.instance().startProcessWindow("影像反向旋转中...");
			}
		}.start();

		new Thread() {
			public void run() {
				String message = null;
				try {
					for (int i = 0; i < imageNodes.length; i++)
						planeRotoImage(imageNodes[i]);
					message = "影像反向旋转成功！";
				} catch (Exception e) {
					message = "影像反向旋转失败！失败原因：" + e.getMessage();
					log.error(message);
					JOptionPane.showMessageDialog(null, message, "错误信息", JOptionPane.ERROR_MESSAGE);
				} finally {
					ShowProcessWindow.instance().stopProcessWindow();
				}
			}
		}.start();
		return true;
	}

	/**
	 * 单张影像反向旋转90°
	 * @param imageNode 影像节点 不能为空
	 * @return 旋转成功返回true，否则返回false
	 * @throws Exception 影像处理抛出异常
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

//		// 设置图片改变
//		if (imageNode.getDocObject() != null)
//			imageNode.setPicchangeFlag(0x04);

		// 移除cache
		JIThumbnailCache.instance().removeCacheSmallIcon(imageNode);

		return true;
	}

	/**
	 * 反向90°旋转影像，如果temp_file不为空的话，保存修改后的影像 否则修改不在本地保存
	 * @param imagebuffer 影像源
	 * @param temp_file 写入影像本地文件
	 * @throws Exception 旋转文件时抛出异常
	 */
	public void planeRotoImage(BufferedImage imagebuffer, File temp_file) throws Exception {
		AffineTransform transform = new AffineTransform();
		transform.translate(0, imagebuffer.getWidth());
		transform.rotate(Math.toRadians(270));
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
