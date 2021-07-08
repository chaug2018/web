/**
 * JImagePanel.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-16
 */
package com.yzj.wf.scan.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JSplitPane;

import com.yzj.wf.scan.ui.selection.ISelectionChangedListener;
import com.yzj.wf.scan.ui.selection.SelectionChangedEvent;
import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.ui.viewer.DrawLayer;
import com.yzj.wf.scan.ui.viewer.LayerVO;
import com.yzj.wf.scan.util.ImageNode;
import com.yzj.wf.scan.util.ImageUtility;

/**
 * 图片列表的小图
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * 
 *          修改图片列表的显示，需要控制图片是否全图显示， 原有的构造函数仅仅只是设置<i>scaleToParent</i>
 *          目前添加另外的两个构造参数，第一个参数全图显示 第二参数是否添加批注，同时需要扩展功能，当图片大小 显示框变化时，显示图片随着变化。
 * 
 * @author LiuQiangQiang
 * @version 1.0.1
 * 
 *          修改JIMagePanel，每当图像对象赋值为空时，系统去GC一次，以便内存回收
 *          添加一个clear方法，每当要去显示一张图片时，先去clear下值
 * @author LiuQiangQiang
 * @version 1.0.2
 */
public class JImagePanel extends javax.swing.JComponent implements
		ISelectionChangedListener {

	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(JImagePanel.class);

	private static final long serialVersionUID = -9085759807121654478L;

	/** image to display */
	public BufferedImage source = null;

	protected BufferedImage scaleImage = null;

	protected double scale = 1.0;

	/** image origin relative to panel origin */
	protected int originX = 0;
	protected int originY = 0;
	protected int dragX = 0;
	protected int dragY = 0;
	protected int pWidth = 0;
	protected int pHeight = 0;

	private boolean scaleToParent = false;

	private boolean vfullScreen = false;

	private boolean showpostil = false;

	// 存储批注
	private List<LayerVO> layerList = new ArrayList<LayerVO>(8);

	private static final Dimension screenSize = Toolkit.getDefaultToolkit()
			.getScreenSize();

	private double vZoomFactor = 0.15;
	private boolean vWinFitScreen = true;

	public JImagePanel() {
		this(false);
	}

	/** Creates a new instance of ImageDisplay */
	/** default constructor */
	public JImagePanel(final boolean scaleToParent) {
		this(scaleToParent, false, false);
	}

	public JImagePanel(final boolean scaleToParent, final boolean vfullScreen,
			final boolean showpostil) {
		super();
		this.scaleToParent = scaleToParent;
		this.vfullScreen = vfullScreen;
		this.showpostil = showpostil;
		this.addComponentListener(new JIViewerComponentAdapter());
	}

	public JImagePanel(final boolean scaleToParent, final boolean vfullScreen,
			final boolean showpostil, double vZoomFactor, boolean vWinFitScreen) {
		super();
		this.scaleToParent = scaleToParent;
		this.vfullScreen = vfullScreen;
		this.showpostil = showpostil;
		this.vZoomFactor = vZoomFactor;
		this.vWinFitScreen = vWinFitScreen;
		this.addComponentListener(new JIViewerComponentAdapter());
	}

	protected final void setDimensions() {
		final Insets insets = getInsets();
		final Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		if (this.scaleToParent) {
			if (getParent() instanceof JSplitPane) {
				final int divLoc = ((JSplitPane) getParent())
						.getDividerLocation();
				final int divSize = ((JSplitPane) getParent()).getDividerSize();
				final int divOrnt = ((JSplitPane) getParent()).getOrientation();
				if (divOrnt == JSplitPane.VERTICAL_SPLIT) {
					if (((JSplitPane) getParent()).getTopComponent().equals(
							this)) {
						this.pHeight = divLoc - (insets.left + insets.right);
					} else {
						this.pHeight = getParent().getHeight()
								- (divLoc + divSize + insets.top + insets.bottom);
					}
					this.pWidth = getParent().getWidth()
							- (insets.left + insets.right);
				} else {
					if (((JSplitPane) getParent()).getLeftComponent().equals(
							this)) {
						this.pWidth = divLoc - (insets.left + insets.right);
					} else {
						this.pWidth = getParent().getWidth()
								- (divLoc + divSize + insets.left + insets.right);
					}
					this.pHeight = getParent().getHeight()
							- (insets.top + insets.bottom);
				}
			} else {
				this.pWidth = getParent().getWidth()
						- (insets.left + insets.right);
				this.pHeight = getParent().getHeight()
						- (insets.top + insets.bottom);
			}
		} else if (vfullScreen) {
			this.pWidth = dimScreen.width;
			this.pHeight = dimScreen.height;
		} else {
			this.pWidth = (int) (dimScreen.width * .97);
			this.pHeight = (int) (dimScreen.height * .93);
		}
	}

	protected final void scaleImage() {
		this.scale = 1.0;
		final Dimension dimScreen = Toolkit.getDefaultToolkit().getScreenSize();
		setDimensions();
		final Dimension scaleDim = new Dimension(this.pWidth, this.pHeight);
		if (this.scaleToParent) {
			if ((this.source != null) && (scaleDim != null)) {
				this.scale = ImageUtility.scaleFactor(scaleDim, this
						.getParent().getInsets(), this.source.getWidth(),
						this.source.getHeight());
				scale(0);
				final int w = this.scaleImage.getWidth();
				final int h = this.scaleImage.getHeight();
				// setBounds(0, 0, w, h);
				doLayout();
				this.originX = ((this.pWidth / 2) - (w / 2));
				this.originY = ((this.pHeight / 2) - (h / 2));
			}
		} else if (vWinFitScreen || (this.source.getWidth() > dimScreen.width)
				|| (this.source.getHeight() > dimScreen.height)) {
			if (vfullScreen) {
				this.scale = ImageUtility.scaleFactor(scaleDim,
						this.source.getWidth(), this.source.getHeight());
			} else {
				this.scale = ImageUtility.scaleFactor(scaleDim, this
						.getParent().getInsets(), this.source.getWidth(),
						this.source.getHeight());
			}
			scale(0);
			final int w = this.scaleImage.getWidth();
			final int h = this.scaleImage.getHeight();
			if (!vfullScreen) {
				setBounds(dragX, dragY, w, h);
				this.originX = 0;
				this.originY = 0;
			} else {
				setBounds(dragX, dragY, screenSize.width, screenSize.height);
				this.originX = (screenSize.width - w) / 2;
				this.originY = (screenSize.height - h) / 2;
			}
		}
	}

	public void scale(final double adjustment) {
		if ((this.scale < vZoomFactor && adjustment < 0) || this.scale > 1.05
				&& adjustment > 0)
			return;
		final double oldScale = this.scale;
		this.scale += adjustment;
		if (this.scale != 1) {
			final int new_width = (int) (this.source.getWidth() * this.scale);
			final int old_width = (int) (this.source.getWidth() * oldScale);

			final int new_height = (int) (this.source.getHeight() * this.scale);
			final int old_height = (int) (this.source.getHeight() * oldScale);

			this.originX -= (new_width / 2) - (old_width / 2);
			this.originY -= (new_height / 2) - (old_height / 2);

			this.scaleImage = null;
			log.debug("Image scale " + this.scale + ", "
					+ ImageUtility.memoryInf());

			this.scaleImage = new BufferedImage(new_width, new_height,
					BufferedImage.TYPE_INT_RGB);
			final Graphics2D g2d = this.scaleImage.createGraphics();
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
					vfullScreen ? RenderingHints.VALUE_RENDER_QUALITY
							: RenderingHints.VALUE_RENDER_SPEED);
			g2d.drawImage(this.source,
					AffineTransform.getScaleInstance(this.scale, this.scale),
					null);
			g2d.dispose();
		} else {
			this.scaleImage = this.source;
			log.debug("Image scale " + this.scale + ", "
					+ ImageUtility.memoryInf());
		}

		if (this.showpostil) {
			drawLayer();
		}
		repaint();
	}

	public void selectionChanged(SelectionChangedEvent event) {
		try {
			StructuredSelection selection = (StructuredSelection) event
					.getSelection();
			ImageNode imageNode = (ImageNode) selection.getLastElement();
			display(imageNode);
		} catch (Exception e) {
			display(null);
		}
	}

	public void display(ImageNode imageNode) {
		clear();
		if (imageNode != null && imageNode.getFile() != null) {
			if (!imageNode.getFile().exists()) {
//				JOptionPane.showMessageDialog(
//						BatchTree.getInstance().getView(), "图像:"
//								+ imageNode.getFile().getAbsolutePath()
//								+ "已被删除!", "提示", JOptionPane.WARNING_MESSAGE);
				return;
			}
			setImage(ImageUtility.openImage(imageNode));
		} else {
			setImage(ImageUtility.blankImage());
		}
	}

	/** use to display a new image */
	public final void setImage(final BufferedImage im) {
		this.source = null;
		this.scaleImage = null;
		this.source = im;
		scaleImage();
		if (this.showpostil) {
			// 添加批注
			drawLayer();
		}
		repaint();
	}

	/**
	 * 批注发生变化,包括图片，内容，位置等
	 */
	public final void postilChange() {
		this.scaleImage = null;
		log.debug("批注位置和内容发生变化," + ImageUtility.memoryInf());
		scaleImage();
		if (this.showpostil) {
			// 添加批注
			drawLayer();
		}
		repaint();
	}

	public void drawLayer() {
		if (this.scaleImage != null) {
			Graphics2D g2d = this.scaleImage.createGraphics();
			double widthScale = 0.0f;
			double hightScale = 0.0f;
			for (LayerVO layer : layerList) {
				widthScale = (double) getScaleWidth()
						/ (double) layer.getWidth();
				hightScale = (double) getScaleHeight()
						/ (double) layer.getHight();
				layer.setX(layer.getX() * widthScale);
				layer.setY(layer.getY() * hightScale);
				layer.setWidth(getScaleWidth());
				layer.setHight(getScaleHeight());
				layer.setFontSize((int) (layer.getFontSize() * (Math.min(
						widthScale, hightScale))));
				DrawLayer.drawText(g2d, layer, vfullScreen);
			}
		}
	}

	/**
	 * 获取坐标方位的批注信息
	 * 
	 * @param x
	 *            鼠标横坐标
	 * @param y
	 *            鼠标竖坐标
	 * @return 范围内的layer
	 */
	public LayerVO getLayerVO(int x, int y) {
		LayerVO layer = null;
		Graphics2D g2d = this.scaleImage.createGraphics();// 得到图形上下文
		for (LayerVO vo : layerList) {
			if (DrawLayer.isContainsXy(g2d, vo, x, y)) {
				vo.setSelect(true);
				layer = vo;
			} else {
				vo.setSelect(false);
			}
		}
		if (layer == null) {
			layer = new LayerVO();
			layer.setX(x);
			layer.setY(y);
			layer.setWidth(this.scaleImage.getWidth());
			layer.setHight(this.scaleImage.getHeight());
		}
		return layer;
	}

	public void frameResize() {
		if (this.source == null)
			return;
		scaleImage();
		if (this.showpostil) {
			drawLayer();
		}
		repaint();
	}

	/** paint routine */
	@Override
	public final void paint(final Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, this.pWidth, this.pHeight);
		if (this.scaleImage != null) {
			final int width = this.scaleImage.getWidth(this);
			final int height = this.scaleImage.getHeight(this);
			log.debug("originX = " + originX + " originY = " + originY
					+ " width = " + width + " height = " + height);
			g.drawImage(this.scaleImage, this.originX, this.originY, width,
					height, this);
		}
	}

	/**
	 * move image within it's container and repaint it
	 * 
	 * @param x
	 *            the originX to set
	 * @param y
	 *            the originY to set
	 */
	public final void setOrigin(final int x, final int y) {
		this.originX = x;
		this.originY = y;
		repaint();
	}

	/**
	 * @param originX
	 *            the originX to set
	 */
	public synchronized final void setOriginX(final int originX) {
		this.originX = originX;
	}

	/**
	 * @param originY
	 *            the originY to set
	 */
	public synchronized final void setOriginY(final int originY) {
		this.originY = originY;
	}

	/** get the image origin */
	public final Point getOrigin() {
		return new Point(this.originX, this.originY);
	}

	/**
	 * @return the originX
	 */
	public synchronized final int getOriginX() {
		return this.originX;
	}

	/**
	 * @return the originY
	 */
	public synchronized final int getOriginY() {
		return this.originY;
	}

	/**
	 * 
	 * @return the scaleImage of height
	 */
	public final int getScaleHeight() {
		return this.scaleImage.getHeight();
	}

	/**
	 * 
	 * @return the scaleImage of width
	 */
	public final int getScaleWidth() {
		return this.scaleImage.getWidth();
	}

	public synchronized void addLayer(LayerVO layer) {
		if (!this.layerList.contains(layer)) {
			this.layerList.add(layer);
		}
	}

	public synchronized void deleteLayer(LayerVO layer) {
		this.layerList.remove(layer);
	}

	public synchronized void clearLayerList() {
		this.layerList.clear();
	}

	public synchronized List<LayerVO> getLayerList() {
		return this.layerList;
	}

	public synchronized void setScaleToParent(boolean scaleToParent) {
		this.scaleToParent = scaleToParent;
	}

	public synchronized void setVfullScreen(boolean vfullScreen) {
		this.vfullScreen = vfullScreen;
	}

	public synchronized void setShowpostil(boolean showpostil) {
		this.showpostil = showpostil;
	}

	public final int getImageHeight() {
		return this.source.getHeight();
	}

	public final int getImageWidth() {
		return this.source.getWidth();
	}

	public String percent() {
		if (this.scaleImage == null)
			return "";
		return String
				.valueOf((int) (((float) this.scaleImage.getHeight() / (float) this.source
						.getHeight()) * 100))
				+ "%";
	}

	class JIViewerComponentAdapter extends java.awt.event.ComponentAdapter {
		@Override
		public void componentResized(final ComponentEvent e) {
			frameResize();
		}
	}

	/**
	 * 清除资源
	 */
	public void clear() {
		log.debug("clear before " + ImageUtility.memoryInf());
		this.source = null;
		this.scaleImage = null;
		this.layerList.clear();
		log.debug("clear after " + ImageUtility.memoryInf());
	}

	public void setDragX(int dragX) {
		this.dragX = dragX;
	}

	public void setDragY(int dragY) {
		this.dragY = dragY;
	}

	public BufferedImage getSource() {
		return source;
	}

	public void setSource(BufferedImage source) {
		this.source = source;
	}

}
