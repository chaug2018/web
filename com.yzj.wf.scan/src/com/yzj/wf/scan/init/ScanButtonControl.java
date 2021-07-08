package com.yzj.wf.scan.init;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.tree.TreePath;

import uk.co.mmscomputing.device.scanner.Scanner;
import uk.co.mmscomputing.device.scanner.ScannerIOMetadata;
import uk.co.mmscomputing.device.scanner.ScannerListener;
import uk.co.mmscomputing.device.twain.TwainConstants;
import uk.co.mmscomputing.device.twain.TwainIOMetadata;
import uk.co.mmscomputing.device.twain.TwainSource;

import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;
import com.yzj.wf.scan.gui.batchtree.BatchTreeNode;
import com.yzj.wf.scan.imagedeal.dpi.ImageDpiChanger;
import com.yzj.wf.scan.item.BusSetingDialog;
import com.yzj.wf.scan.mainview.BatchScanView;
import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.GetGuid;
import com.yzj.wf.scan.util.ImageIconFactory;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 *扫描相关按钮控制类
 * @author 陈林江
 * @version 1.0.1
 */
public class ScanButtonControl implements TwainConstants, ScannerListener {

	private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ScanButtonControl.class);

	private Scanner scanner = null;
	private JButton driver_Button;
	private JButton set_Button;
	private JButton scan_Button;
	private JButton insert_Button;
	private JButton busSet_Button;
	private boolean isSet = false;
	private ToolBarPanel toolBarPanel;
	private Color color = null;
	protected BatchScanView view;
	private File TempDir = null;
	private Font font = new Font("Default", 0, 10);

	public ScanButtonControl(ToolBarPanel toolBarPanel, BatchScanView view) {
		this.view = view;
		scanner = Scanner.getDevice(); // get a device and set GUI
		if (scanner != null) {// 设置扫描按钮
			scanner.addListener(this);
		}
		this.toolBarPanel = toolBarPanel;
		creatTempDir();
	}

	/**
	 * 创建临时文件夹
	 * 
	 */
	public void creatTempDir() {
		String tempfilepath = view.getReadConfig().getPropertyValue(
				"batchTempFile", System.getProperty("java.io.tmpdir"));
		System.out.println(tempfilepath);
		TempDir = new File(tempfilepath+"yzj");
		if(!TempDir.exists()){
			TempDir.mkdir();
		}
		logger.info("pic save tempdir is " + tempfilepath+"/yzj");
	}

	public void acquire() {
		scanner.acquire();
	}

	public void scanAcquire() {
		view.getSysCacheContext().setInsertNode(null);
		scanner.acquire();
	}

	public void select() {
		scanner.select();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.co.mmscomputing.device.scanner.ScannerListener#update(uk.co.mmscomputing
	 * .device.scanner.ScannerIOMetadata.Type,
	 * uk.co.mmscomputing.device.scanner.ScannerIOMetadata)
	 */
	public void update(ScannerIOMetadata.Type type,
			final ScannerIOMetadata metadata) {
		if (type.equals(ScannerIOMetadata.ACQUIRED)) { // 扫描仪传出的图像
		// new Thread() {
		// public void run() {
			try {
				BufferedImage image = metadata.getImage();
				File file = save(image);
				if (file != null) {
					StructuredSelection selection = new StructuredSelection(
							new File[] { file }, 0);
					toolBarPanel.toolBarPanelChanged(null, selection);
				}
			} catch (Exception e) {
				logger.error("保存图像异常", e);
			}
			// }
			// }.start();
		}

		if (metadata instanceof TwainIOMetadata) {
			TwainIOMetadata data = (TwainIOMetadata) metadata;
			TwainSource source = data.getSource();

			if (data.isState(STATE_SRCMNGOPEN)) { // state = 3
				if (source.isBusy()) {
					driver_Button.setEnabled(false);
					set_Button.setEnabled(false);
					scan_Button.setEnabled(false);
					insert_Button.setEnabled(false);
				} 
				else { // 扫描完毕
					driver_Button.setEnabled(true);
					set_Button.setEnabled(true);
					scan_Button.setEnabled(true);
					insert_Button.setEnabled(true);
				}
			} else if (data.isState(STATE_SRCOPEN)) { // state = 4; can
				if (source.isUIControllable()) { // if it is possible to hide
					source.setShowUI(isSet); // then use
				} else {
					isSet = true; // else set to true whatever
				}
			}
		}

	}

	/**
	 * 将扫描到的图像保存成文件
	 * 
	 * @param filename
	 *            文件名
	 * @param image
	 *            图像
	 */
	private File save(BufferedImage image) throws IOException {
		File imageFile = null;
		try {
			String filename = new GetGuid().toString();
			switch (image.getType()) {
			case BufferedImage.TYPE_BYTE_BINARY:
				filename = filename + ".tif";
				IIOImage iIamge = new IIOImage(image, null, null);
				TIFFImageWriterSpi tiffspi = new TIFFImageWriterSpi();
				ImageWriter writer = tiffspi.createWriterInstance();
				ImageWriteParam param = writer.getDefaultWriteParam();
				param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
				param.setCompressionType("LZW");
				param.setCompressionQuality(0.5f);
				imageFile = new File(TempDir, filename);
				ImageOutputStream ios = ImageIO
						.createImageOutputStream(imageFile);
				writer.setOutput(ios);
				writer.write(null, iIamge, param);
				break;
			case BufferedImage.TYPE_BYTE_INDEXED:
			case BufferedImage.TYPE_3BYTE_BGR:
			default:
				filename = filename + ".jpg";
			imageFile = new File(TempDir, filename);
			ImageDpiChanger.ChangeDpi4Jpg(image, imageFile, 200);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageFile;
	}

	/**
	 * 获取扫描仪驱动选择按钮
	 * 
	 * @return 按钮
	 */
	protected JButton getDriver_Button() {
		if (driver_Button == null) {
			driver_Button = new JButton();
			driver_Button.setIcon(ImageIconFactory
					.getImageIcon("/icons/scanner_driver.png"));
			driver_Button.setMaximumSize(new Dimension(65, 100));
			driver_Button.setText(ParamDefine.selectDevice);
			driver_Button.setFont(font);
			driver_Button.setHorizontalTextPosition(SwingConstants.CENTER);
			driver_Button.setVerticalTextPosition(SwingConstants.BOTTOM);
			driver_Button.addActionListener((ActionListener) EventHandler
					.create(ActionListener.class, this, "select"));
		}
		return driver_Button;
	}

	/**
	 * 获取扫描仪设置按钮
	 * 
	 * @return 按钮
	 */
	protected JButton getSet_Button() {
		if (set_Button == null) {
			set_Button = new JButton();
			set_Button.setIcon(ImageIconFactory
					.getImageIcon("/icons/scanner_set.png"));
			set_Button.setFocusable(false);
			set_Button.addActionListener(new ActionListener() {
				public void actionPerformed(final ActionEvent e) {

				}
			});
			set_Button.setMaximumSize(new Dimension(65, 100));
			set_Button.setHorizontalTextPosition(SwingConstants.CENTER);
			set_Button.setVerticalTextPosition(SwingConstants.BOTTOM);
			set_Button.setText(ParamDefine.scanSetting);
			set_Button.setFont(font);
			color = set_Button.getBackground();
			set_Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isSet) {
						set_Button.setBackground(Color.lightGray);
						isSet = true;
					} else {
						set_Button.setBackground(color);
						isSet = false;
					}
				}
			});
		}
		return set_Button;
	}

	/**
	 * 获取扫描仪启动按钮
	 * 
	 * @return 按钮
	 */
	protected JButton getScan_Button() {
		if (scan_Button == null) {
			scan_Button = new JButton();
			scan_Button.setIcon(ImageIconFactory
					.getImageIcon("/icons/scanner_scan.png"));
			scan_Button.setFocusable(false);
			scan_Button.setMaximumSize(new Dimension(65, 100));
			scan_Button.setText(ParamDefine.scanImage);
			scan_Button.setFont(font);
			scan_Button.setHorizontalTextPosition(SwingConstants.CENTER);
			scan_Button.setVerticalTextPosition(SwingConstants.BOTTOM);
			scan_Button.addActionListener((ActionListener) EventHandler.create(
					ActionListener.class, this, "scanAcquire"));
		}
		return scan_Button;
	}

	/**
	 * 获取扫描插入按钮
	 * 
	 * @return 按钮
	 */
	protected JButton getInsert_Button() {
		if (insert_Button == null) {
			insert_Button = new JButton();
			insert_Button.setIcon(ImageIconFactory
					.getImageIcon("/icons/scanner_insert.png"));
			insert_Button.setFocusable(false);
			insert_Button.setMaximumSize(new Dimension(65, 100));
			insert_Button.setText(ParamDefine.scanInsert);
			insert_Button.setFont(font);
			insert_Button.setHorizontalTextPosition(SwingConstants.CENTER);
			insert_Button.setVerticalTextPosition(SwingConstants.BOTTOM);
			insert_Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TreePath[] paths = view.getBatchTree().getSelectionPaths();
					String message = "";
					if (paths != null && paths.length > 0) {
						BatchTreeNode node = (BatchTreeNode) paths[paths.length - 1]
								.getLastPathComponent();
						if (node.isSelected()) {
							if (node.getImageNode() != null) {
								message = ParamDefine.insertConfire_a
										+ paths[paths.length - 1].toString()
										+ ParamDefine.insertConfire_b;
								int result = JOptionPane.showConfirmDialog(
										view, message, ParamDefine.noticeMessage,
										JOptionPane.OK_CANCEL_OPTION);
								if (result == 0) {
									view.getSysCacheContext().setInsertNode(
											node);
									acquire();
								}
							} else {
								if (node.getParent() == null) { // 根节点
									message = ParamDefine.insertConfire_c
											+ node.toString()
											+ ParamDefine.insertConfire_d;
									JOptionPane.showMessageDialog(view,
											message, ParamDefine.noticeMessage,
											JOptionPane.INFORMATION_MESSAGE);
								} else {
									message = ParamDefine.insertConfire_a
											+ paths[paths.length - 1]
													.toString()
											+ ParamDefine.insertConfire_e;
									int result = JOptionPane.showConfirmDialog(
											view, message, ParamDefine.noticeMessage,
											JOptionPane.OK_CANCEL_OPTION);
									if (result == 0) {
										view.getSysCacheContext()
												.setInsertNode(node);
										acquire();
									}
								}
							}
						} else {
							message =ParamDefine.insertConfire_f;
							JOptionPane.showMessageDialog(view, message,
									ParamDefine.noticeMessage, JOptionPane.INFORMATION_MESSAGE);
						}
					} else {
						message = ParamDefine.insertConfire_f;
						JOptionPane.showMessageDialog(view, message, ParamDefine.noticeMessage,
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			});
		}
		return insert_Button;
	}

	/**
	 * 获取票据类型设置按钮
	 * 
	 * @return 按钮
	 */
	protected JButton getBusSet_Button() {
		if (busSet_Button == null) {
			busSet_Button = new JButton();
			busSet_Button.setIcon(ImageIconFactory
					.getImageIcon("/icons/busType.gif"));
			busSet_Button.setFocusable(false);

			busSet_Button.setMaximumSize(new Dimension(65, 100));
			busSet_Button.setHorizontalTextPosition(SwingConstants.CENTER);
			busSet_Button.setVerticalTextPosition(SwingConstants.BOTTOM);
			busSet_Button.setText(ParamDefine.busType);
			busSet_Button.setFont(font);
			color = set_Button.getBackground();
			busSet_Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					new BusSetingDialog();
				}
			});
		}
		return busSet_Button;
	}

}
