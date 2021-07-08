/**
 * ImageUtility.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-9
 */
package com.yzj.wf.scan.util;

import it.tidalwave.imageio.tiff.TIFFMetadataSupport;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.exif.ExifDirectory;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi;
import com.yzj.wf.scan.item.JIIcon;

/**
 * 常用静态方法
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class ImageUtility {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(ImageUtility.class);

	private static final Set<String> unwanted = new HashSet<String>();

	static {
		unwanted.add("com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageReader");
		unwanted.add("com.sun.media.imageioimpl.plugins.jpeg.CLibJPEGImageWriter");
	}

	public static Icon folderIcon = FileSystemView.getFileSystemView()
			.getSystemIcon(new File(System.getProperty("user.home")));

	/**
	 * 支持图片格式jpg,gif,png,crw,cr2,dng,mrw,nef,pef,jpeg,bmp,tif
	 * 
	 * @param suffix
	 * @return 支持返回TRUE
	 */
	public final static boolean isSupportedImage(final String suffix) {
		return suffix.equals("jpg") || suffix.equals("gif")
				|| suffix.equals("png") || suffix.equals("crw")
				|| suffix.equals("cr2") || suffix.equals("dng")
				|| suffix.equals("mrw") || suffix.equals("nef")
				|| suffix.equals("pef") || suffix.equals("jpeg")
				|| suffix.equals("bmp") || suffix.equals("tif");
	}

	public final static boolean isSupportedImage(final File file) {
		final String fileName = file.getName().toLowerCase();
		return fileName.endsWith(".jpg") || fileName.endsWith(".gif")
				|| fileName.endsWith(".png") || fileName.endsWith(".crw")
				|| fileName.endsWith(".cr2") || fileName.endsWith(".tif")
				|| fileName.endsWith(".dng") || fileName.endsWith(".mrw")
				|| fileName.endsWith(".nef") || fileName.endsWith(".pef")
				|| fileName.endsWith(".jpeg") || fileName.endsWith(".bmp");
	}

	/**
	 * 获取文件小写形式后缀名
	 * 
	 * @param name
	 * @return 不存在返回null
	 */
	public final static String suffix(final String name) {
		final int i = name.lastIndexOf('.');
		if (i > 0) {
			return name.toLowerCase().substring(i + 1);
		}
		return null;
	}

	/**
	 * 获取长度KB
	 * 
	 * @param length
	 * @return 长度超过999，返回结果类似1,000KB
	 */
	public static String length2KB(final long length) {
		final long kbCount = (length + 1024) / 1024;
		final String strlength = String.valueOf(kbCount);
		return String
				.valueOf((kbCount > 999 ? strlength.substring(0,
						strlength.length() - 3)
						+ "," + strlength.substring(strlength.length() - 3)
						: strlength)
						+ " KB ");
	}

	public final static String memoryInf() { // boolean clean) {
		final long freeMem = Runtime.getRuntime().freeMemory();
		final long totalMem = Runtime.getRuntime().totalMemory();
		final long maxMem = Runtime.getRuntime().maxMemory();

		final String msg = "Free Mem: " + freeMem / 1048576 + " Total Mem: "
				+ totalMem / 1048576 + " Max Mem: " + maxMem / 1048576 + "(MB)";

		if (true && ((double) totalMem / (double) maxMem > .75)
				&& (freeMem < 40)) {
			System.runFinalization();
			System.gc();
			log.debug(msg + " - Clean up run");
			return msg + " - Clean up run";
		}
		return msg;
	}

	/**
	 * 获取空余内存
	 * 
	 * @return 返回结果单位MB
	 */
	public final static int freeMem() {
		return (int) Runtime.getRuntime().freeMemory() / 1048576;
	}

	/**
	 * 获取系统图标
	 * 
	 * @param f
	 * @return 返回系统的图标
	 */
	public final static Icon getSystemIcon(final File f) {
		if (f.exists()) {
			return FileSystemView.getFileSystemView().getSystemIcon(f);
		} else {
			return folderIcon;
		}
	}

	public static BufferedImage orientImage(BufferedImage image,
			final int orientation) {
		// log.debug("orientImage orientation = "+orientation);
		if (orientation == 1) {
			return image;
		}

		boolean rotate = false;
		boolean clockWise = false;
		boolean mirrorHorizontal = false;
		boolean mirrorVertical = false;

		switch (orientation) {
		case 2:
			mirrorHorizontal = true;
			break;
		case 3:
			mirrorHorizontal = true;
			mirrorVertical = true;
			break;
		case 4:
			mirrorVertical = true;
			break;
		case 5:
			rotate = true;
			clockWise = true;
			mirrorHorizontal = true;
			break;
		case 6:
			rotate = true;
			clockWise = true;
			break;
		case 7:
			rotate = true;
			mirrorHorizontal = true;
			break;
		case 8:
			rotate = true;
			break;
		}

		final int targetWith = (rotate ? image.getHeight() : image.getWidth());
		final int targetHeight = (rotate ? image.getWidth() : image.getHeight());

		final AffineTransform transform = new AffineTransform();

		if (mirrorVertical) {
			transform.translate(0, targetHeight);
			transform.scale(1, -1);
		}
		if (mirrorHorizontal) {
			transform.translate(targetWith, 0);
			transform.scale(-1, 1);
		}
		if (rotate) {
			if (clockWise) {
				transform.translate(image.getHeight(), 0);
				transform.rotate(Math.PI / 2);
			} else {
				transform.translate(0, image.getWidth());
				transform.rotate(-Math.PI / 2);
			}
		}

		final BufferedImage copy = new BufferedImage(targetWith, targetHeight,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = copy.createGraphics();
		g2d.drawImage(image, transform, null);
		g2d.dispose();

		image = null;
		return copy;
	}

	public static ImageReader getImageReader(final ImageNode node)
			throws FileNotFoundException, IOException {
		ImageInputStream imageInputStream = null;
		try {
			imageInputStream = ImageIO.createImageInputStream(node.getFile());
			for (final Iterator<ImageReader> iterator = ImageIO
					.getImageReadersBySuffix(node.getSuffix()); iterator
					.hasNext();) {
				final ImageReader reader = iterator.next();
				final String pluginClassName = reader.getOriginatingProvider()
						.getPluginClassName();
				if ((reader != null)
						&& !unwanted.contains(pluginClassName)
						&& reader.getOriginatingProvider().canDecodeInput(
								imageInputStream)
						&& !(node.getSuffix().equalsIgnoreCase("tif") && reader
								.getClass().getName()
								.startsWith("it.tidalwave"))) {
					reader.setInput(imageInputStream);
					return reader;
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		try {
			if (imageInputStream != null) {
				imageInputStream.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ImageReader getImageReader(final File file)
			throws FileNotFoundException, IOException {
		ImageInputStream imageInputStream = null;
		try {
			String suffix = suffix(file.getName());
			imageInputStream = ImageIO.createImageInputStream(file);
			for (final Iterator<ImageReader> iterator = ImageIO
					.getImageReadersBySuffix(suffix); iterator.hasNext();) {
				final ImageReader reader = iterator.next();
				final String pluginClassName = reader.getOriginatingProvider()
						.getPluginClassName();
				if ((reader != null)
						&& !unwanted.contains(pluginClassName)
						&& reader.getOriginatingProvider().canDecodeInput(
								imageInputStream)
						&& !(suffix.equalsIgnoreCase("tif") && reader
								.getClass().getName()
								.startsWith("it.tidalwave"))) {
					reader.setInput(imageInputStream);
					return reader;
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}

		try {
			if (imageInputStream != null) {
				imageInputStream.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static BufferedImage openImage(final ImageNode node) {
		try {
			if (node.getSuffix().equals("cr2")
					|| node.getSuffix().equals("crw")) {
				final ImageReader imageReader = getImageReader(node);
				return orientImage(imageReader.readThumbnail(0, 0),
						node.getOrientation());
			} else {
				final ImageReader imageReader = getImageReader(node);
				return orientImage(imageReader.read(0), node.getOrientation());
			}
		} catch (final Exception exp) {
			try {
				return orientImage(ImageIO.read(node.getFile()),
						node.getOrientation());
			} catch (final Exception e) {
				exp.printStackTrace();
			}
			return null;
		}
	}

	public final static float scaleFactor(final Dimension d1, final int w,
			final int h) {
		return Math.min((float) d1.width / w, (float) d1.height / h);
	}

	public final static float scaleFactor(final Dimension d1, final Insets i,
			final int w, final int h) {
		return Math.min((float) (d1.width - (i.left + i.right)) / w,
				(float) (d1.height - (i.top + i.bottom)) / h);
	}

	public final static BufferedImage blankImage() {
		final BufferedImage image = new BufferedImage(400, 400,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, 400, 400);
		g2d.dispose();
		return image;
	}

	// This method returns a buffered image with the contents of an image
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see e661 Determining If an Image Has Transparent
		// Pixels
		final boolean hasAlpha = hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		final GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			final GraphicsDevice gs = ge.getDefaultScreenDevice();
			final GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null),
					image.getHeight(null), transparency);
		} catch (final HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), type);
		}

		// Copy image to buffered image
		final Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(final Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			final BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}

		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		final PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		try {
			pg.grabPixels();
		} catch (final InterruptedException e) {
		}

		// Get the image's color model
		final ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}

	public static final BufferedImage createThumbnailRetry(
			final ImageNode thumbnail) {
		int retryCounter = 2;
		while (retryCounter > 0) {
			try {
				return createThumbnail(thumbnail);
			} catch (final OutOfMemoryError e) {
				// log.debug("Out of memory error detected ! Retrying !");
				// cachedThumbnails.clear();
				System.runFinalization();
				System.gc();
				retryCounter--;
			} catch (final Exception exp) {
				exp.printStackTrace();
				return null;
			}
		}
		return null;
	}

	private static final BufferedImage createThumbnail(final ImageNode node)
			throws IOException {
		if (node.getSuffix().equals("jpg") || node.getSuffix().equals("jpeg")) {
			final BufferedImage inImage = createThumbnailFromEXIF(node);
			if (inImage != null) {
				return inImage;
			}
		}
		return createThumbnailFromFile(node);
	}

	private static final BufferedImage createThumbnailFromFile(
			final ImageNode node) {
		BufferedImage image;
		ImageReader reader = null;
		try {
			reader = getImageReader(node);
			if (reader != null) {
				if (reader.readerSupportsThumbnails()) {
					try {
						if (reader.hasThumbnails(0)) {
							int indx = 0;
							if (reader.getNumThumbnails(0) > 1) {
								indx = 1;
							}
							image = reader.readThumbnail(0, indx);
							node.setHeight(reader.getThumbnailHeight(0, indx));
							node.setWidth(reader.getThumbnailWidth(0, indx));
							final IIOMetadata iiometadata = reader
									.getImageMetadata(0);
							if (iiometadata instanceof TIFFMetadataSupport) {
								node.setOrientation(((TIFFMetadataSupport) reader
										.getImageMetadata(0)).getPrimaryIFD()
										.getOrientation().intValue());
							} else {
								node.setOrientation(1);
							}
							return orientImage(scaleThumbnail(image),
									node.getOrientation());
						}
					} catch (final Exception e) {
						e.printStackTrace();
						try {
							image = ImageIO.read(node.getFile());
							node.setHeight(image.getHeight(null));
							node.setWidth(image.getWidth(null));
							return scaleThumbnail(image);
						} catch (final Exception e1) {
							return blankImage();
						}
					} finally {
						reader.dispose();
					}
				}
				try {
					final ImageReadParam param = reader.getDefaultReadParam();
					// Set Image sampling read every third byte
					// BIG perfomance boost for generating thumbnails
					//
					if (node.getLength() > 1024 * 80) {
						if (node.getLength() > 1024 * 999) {
							param.setSourceSubsampling(8, 8, 0, 0);
						}
						param.setSourceSubsampling(3, 3, 0, 0);
					} else {
						param.setSourceSubsampling(1, 1, 0, 0);
					}

					image = reader.read(0, param);
					node.setHeight(reader.getHeight(0));
					node.setWidth(reader.getWidth(0));

					final IIOMetadata iiometadata = reader.getImageMetadata(0);
					if (iiometadata instanceof TIFFMetadataSupport) {
						node.setOrientation(((TIFFMetadataSupport) reader
								.getImageMetadata(0)).getPrimaryIFD()
								.getOrientation().intValue());
					} else {
						node.setOrientation(1);
					}
					return orientImage(scaleThumbnail(image),
							node.getOrientation());
				} catch (final IndexOutOfBoundsException ioobe) {
					return blankImage();
				} catch (final Exception e) {
					e.printStackTrace();
					image = ImageIO.read(node.getFile());
					node.setHeight(image.getHeight(null));
					node.setWidth(image.getWidth(null));
					return scaleThumbnail(image);
				} finally {
					reader.dispose();
				}
			} else {
				// System.out.println(node.getFile().toString());
				image = ImageIO.read(node.getFile());
				node.setHeight(image.getHeight(null));
				node.setWidth(image.getWidth(null));
				return scaleThumbnail(image);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static BufferedImage byteArrayToBufferedImage(final byte[] _pic) {
		final ByteArrayInputStream in = new ByteArrayInputStream(_pic);
		final JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(in);
		final BufferedImage image;
		try {
			image = decoder.decodeAsBufferedImage();
			in.close();
			return image;
		} catch (final ImageFormatException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final BufferedImage createThumbnailFromEXIF(
			final ImageNode node) {
		try {
			ExifDirectory directory = null;
			if (("jpg").equals(node.getSuffix())) {
				directory = (ExifDirectory) JpegMetadataReader.readMetadata(
						node.getFile()).getDirectory(ExifDirectory.class);
			}
			if ((directory != null) && (directory.containsThumbnail())) {
				if (directory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
					node.setHeight(directory
							.getInt(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT));
				}
				if (directory.containsTag(ExifDirectory.TAG_EXIF_IMAGE_WIDTH)) {
					node.setWidth(directory
							.getInt(ExifDirectory.TAG_EXIF_IMAGE_WIDTH));
				}
				if (directory.containsTag(ExifDirectory.TAG_ORIENTATION)) {
					node.setOrientation(directory
							.getInt(ExifDirectory.TAG_ORIENTATION));
				}
				return orientImage(
						scaleThumbnail(byteArrayToBufferedImage(directory
								.getThumbnailData())),
						node.getOrientation());
			} else {
				final BufferedImage image = ImageIO.read(node.getFile());
				node.setHeight(image.getHeight(null));
				node.setWidth(image.getWidth(null));
				return scaleThumbnail(image);
			}
		} catch (final IndexOutOfBoundsException ioobe) {
			return blankImage();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public final static JIIcon scaleIcon(final BufferedImage inImage,
			final int width, final int height) {
		final double scale = Math.min(
				(double) height / (double) inImage.getHeight(null),
				(double) width / (double) inImage.getWidth(null));
		// final double scale = .75;
		// Determine scaled size of new image.
		int scaledH, scaledW;
		if ((scaledW = (int) (scale * inImage.getWidth(null))) <= 0) {
			scaledW = 1;
		}
		if ((scaledH = (int) (scale * inImage.getHeight(null))) <= 0) {
			scaledH = 1;
		}

		final BufferedImage outImage = new BufferedImage(scaledW, scaledH,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d;
		g2d = outImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		g2d.drawImage(inImage, AffineTransform.getScaleInstance(scale, scale),
				null);
		g2d.dispose();

		return new JIIcon(outImage);
	}

	public final static JIIcon scaleTableIcon(final BufferedImage inImage) {
		final double scale = Math.min(
				(double) 16 / (double) inImage.getHeight(null), (double) 16
						/ (double) inImage.getWidth(null));
		// Determine scaled size of new image.
		int scaledH, scaledW;
		if ((scaledW = (int) (scale * inImage.getWidth(null))) <= 0) {
			scaledW = 1;
		}
		if ((scaledH = (int) (scale * inImage.getHeight(null))) <= 0) {
			scaledH = 1;
		}

		final BufferedImage outImage = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d;
		g2d = outImage.createGraphics();
		g2d.setColor(new Color(235, 235, 235));
		g2d.fillRect(0, 0, 16, 16);
		g2d.translate((16 / 2) - (scaledW / 2), (16 / 2) - (scaledH / 2));
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		g2d.drawImage(inImage, AffineTransform.getScaleInstance(scale, scale),
				null);
		g2d.dispose();

		return new JIIcon(outImage);
	}

	public final static BufferedImage scaleThumbnail(Image inImage) {
		final double scale = Math.min(
				(double) 300 / (double) inImage.getHeight(null), (double) 300
						/ (double) inImage.getWidth(null));
		// Determine scaled size of new image.
		int scaledH, scaledW;
		if ((scaledW = (int) (scale * inImage.getWidth(null))) <= 0) {
			scaledW = 1;
		}
		if ((scaledH = (int) (scale * inImage.getHeight(null))) <= 0) {
			scaledH = 1;
		}

		final BufferedImage outImage = new BufferedImage(scaledW, scaledH,
				BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d;
		g2d = outImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		g2d.drawImage(inImage, AffineTransform.getScaleInstance(scale, scale),
				null);
		g2d.dispose();

		inImage = null;

		return outImage;
	}

	public static final String getNumericPaddedString(final int value,
			final int radix, final int padding) {
		final String str = Integer.toString(value, radix);
		final StringBuffer strBuf = new StringBuffer();

		while ((padding > str.length())
				&& (strBuf.length() < (padding - str.length()))) {
			strBuf.append("0");
		}

		strBuf.append(str);
		return strBuf.toString();
	}

	public static final void deleteFile(final File file) {
		deleteFile(file, 0);
	}

	private static final void deleteFile(final File file, int count) {
		if (file.exists()) {
			if (count > 3) {
				file.deleteOnExit();
			} else if (!file.delete()) {
				System.gc();
				if (!file.delete()) {
					try {
						Thread.sleep(100);
					} catch (final InterruptedException e) {

					}
					deleteFile(file, ++count);
				}
			}
		}
	}

	public static BufferedImage planeRotoImage(BufferedImage sourceImage,
			AffineTransform transform) {
		try {
			AffineTransformOp affineOp = new AffineTransformOp(transform,
					AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			sourceImage = affineOp.filter(sourceImage, null);
			return sourceImage;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 拷贝文件
	 * 
	 * @param localName
	 *            本地文件
	 * @param targetName
	 *            目标文件
	 * @return 是否copy成功
	 */
	public static boolean copyLocalFile(File localFile, File targetFile) {
		try {
			int bytesum = 0;
			int byteread = 0;
			if (localFile.exists()) { // 文件存在时
				// 判断文件夹是否存在
				if (!targetFile.getParentFile().exists()) {
					targetFile.getParentFile().mkdirs();
					targetFile.createNewFile();
				}

				InputStream inStream = new FileInputStream(localFile); // 读入原文件
				FileOutputStream fs = new FileOutputStream(targetFile);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.flush();
				fs.close();
			} else {
				throw new Exception("拷贝的文件不存在！");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean copyFile(File sourcefile, File targetFile)
			throws Exception {
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = new FileInputStream(sourcefile);
			outputStream = new FileOutputStream(targetFile);
			byte[] b = new byte[8192];
			int c = -1;
			while ((c = inputStream.read(b)) > -1) {
				outputStream.write(b, 0, c);
			}
			outputStream.flush();
		} catch (Exception e) {
			throw new Exception("拷贝文件失败!" + e.getMessage());
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
		}

		return true;
	}

	/**
	 * 复制文件到本地计算机
	 * 
	 * @param cl
	 *            class路径
	 * @param fileName
	 *            复制的文件名
	 * @param targetFile
	 *            本地绝对路径
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static void initFileAddr(Class cl, String fileName, File targetFile)
			throws Exception {
		URL url = cl.getResource("file/" + fileName);
		if (url == null) {
			throw new UnsatisfiedLinkError(" 不能找到指定文件：[" + fileName + "]");
		}
		try {
			URI uri = new URI(url.toString());
			String scheme = uri.getScheme();
			if (scheme.equals("file")) {
				File localFile = new File(url.getFile());
				if (!copyLocalFile(localFile, targetFile))
					throw new Exception("复制文件不成功!");
			} else if (scheme.equals("jar")) {
				extract(targetFile, url);
				System.out
						.println("ImageUtility.initFileAddr: Successfully loaded library ["
								+ url + "] from jar file location");
			} else {
				throw new UnsatisfiedLinkError("\n\tUnknown URI-Scheme ["
						+ scheme + "]; Could not load library [" + uri + "]");
			}
		} catch (URISyntaxException urise) {
			throw new UnsatisfiedLinkError(
					"URI-Syntax Exception; Could not load library [" + url
							+ "]");
		}
	}

	private static void extract(File fn, URL url) throws IOException {
		InputStream in = url.openStream();
		FileOutputStream out = new FileOutputStream(fn);
		byte[] buffer = new byte[4096];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		out.close();
		in.close();
	}

	/**
	 * 将大图像转换为小图像，防止内存溢出。目前只支持JPEG编码格式
	 * 
	 * @param file
	 *            原始图像文件
	 * @throws Exception
	 */
	public static File bigToSmallImage(File file) throws Exception {
		final String fileName = file.getName().toLowerCase();

		File targetFile = new File("c:\\" + file.getName());
		if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
			double scale = 1.0;
			boolean isZoom = false;
			int maxHeight = 2000;
			int maxWidth = 2000;
			final ImageReader imageReader = getImageReader(new ImageNode(file));
			final BufferedImage image = imageReader.read(0);
			int image_height = image.getHeight();
			int image_width = image.getWidth();
			while ((image_height > maxHeight || image_width > maxWidth)
					&& scale > 0 && image_height > 0 && image_width > 0) {
				isZoom = true;
				scale -= .05;
				image_height = (int) Math.abs(image.getHeight() * scale);
				image_width = (int) Math.abs(image.getWidth() * scale);
			}
			if (isZoom) {
				int type = BufferedImage.TYPE_INT_RGB;
				BufferedImage tag = new BufferedImage(image_width,
						image_height, type);
				Graphics2D g = tag.createGraphics();
				g.setRenderingHint(RenderingHints.KEY_RENDERING,
						RenderingHints.VALUE_RENDER_SPEED);
				g.drawImage(image,
						AffineTransform.getScaleInstance(scale, scale), null);
				g.dispose();
				ImageIO.write(tag, "jpeg", targetFile);
				return targetFile;
			}
		}
		if (!ImageUtility.copyLocalFile(file, targetFile)) {
			throw new Exception("拷贝图像数据失败！");
		}
		return targetFile;
	}

	public static String createUUID() {
		return UUID.randomUUID().toString();
	}

	public static File saveBufferedImage(BufferedImage bufferImage,
			File saveFile) throws Exception {
		switch (bufferImage.getType()) {
		case BufferedImage.TYPE_BYTE_BINARY:
			IIOImage iIamge = new IIOImage(bufferImage, null, null);
			TIFFImageWriterSpi tiffspi = new TIFFImageWriterSpi();
			ImageWriter writer = tiffspi.createWriterInstance();
			ImageWriteParam param = writer.getDefaultWriteParam();
			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionType("LZW");
			param.setCompressionQuality(0.5f);
			ImageOutputStream ios = ImageIO.createImageOutputStream(saveFile);
			writer.setOutput(ios);
			writer.write(null, iIamge, param);
			ios.close();
			break;
		default:
			ImageIO.write(bufferImage, "jpeg", saveFile);
		}
		return saveFile;
	}

	public static File bigToSmallImage(File sourcefile, int maxh, int maxw,
			File tempDir) throws Exception {
		ImageReader imageReader = null;
		try {
			final String fileName = new GetGuid().toString()+"."+ImageUtility.suffix(sourcefile.getName());
			File targetFile = new File(tempDir, fileName);
			if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
				double scale = 1.0;
				boolean isZoom = false;
				imageReader = getImageReader(sourcefile);
				final BufferedImage image = imageReader.read(0);
				int image_height = image.getHeight();
				int image_width = image.getWidth();
				while ((image_height > maxh || image_width > maxw) && scale > 0
						&& image_height > 0 && image_width > 0) {
					isZoom = true;
					scale -= .05;
					image_height = (int) Math.abs(image.getHeight() * scale);
					image_width = (int) Math.abs(image.getWidth() * scale);
				}
				if (isZoom) {
					int type = BufferedImage.TYPE_INT_RGB;
					BufferedImage tag = new BufferedImage(image_width,
							image_height, type);
					Graphics2D g = tag.createGraphics();
					g.setRenderingHint(RenderingHints.KEY_RENDERING,
							RenderingHints.VALUE_RENDER_SPEED);
					g.drawImage(image,
							AffineTransform.getScaleInstance(scale, scale),
							null);
					g.dispose();
					ImageIO.write(tag, "jpeg", targetFile);
					return targetFile;
				}
			}
			if (!ImageUtility.copyFile(sourcefile, targetFile)) {
				throw new Exception("拷贝图像数据失败！");
			}
			return targetFile;
		} catch (Exception e) {
			throw e;
		} finally {
			if (imageReader != null)
				imageReader.dispose();
			System.gc();
		}
	}

}
