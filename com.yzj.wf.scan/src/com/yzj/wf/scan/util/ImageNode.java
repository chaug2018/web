/**
 * ImageNode.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-9
 */
package com.yzj.wf.scan.util;

import static com.yzj.wf.scan.util.ImageUtility.getImageReader;
import it.tidalwave.imageio.tiff.IFD;
import it.tidalwave.imageio.tiff.TIFFMetadataSupport;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifDirectory;

/**
 * 图片列表数据元素,需要实现两种方式的构造，通过本地文件构造和通过文档对象构造。 该类主要实现以下功能：
 * <p>
 * <li>获取和更新文档对象属性</li>
 * <p>
 * <i><CODE>ImageNode</CODE>主要保存文档对象属性，属性分别有业务类型，位置索引，批注等。 针对已存在文档对象的<CODE>ImageNode</CODE>修改，分为图片修改和属性修改，属性更改状态见类
 * <CODE>ImageStoreDef.ImageState</CODE>。通过这个类获取到生成指母图片的相关参数。</i>
 * <li>获取生成指母图片的相关参数</li>
 * <li>保存关联的影像结构树</li>
 * <li>图片下载标志</li>
 * <li>属性修改标志</li>
 * <li>位置索引</li>
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * 
 * <p>
 * 
 * <CODE>ImageNode</CODE>由于文档属性过多，跟踪文档属性变化采用二进制，0x00000000,针对文档集对象为空的，
 * 不做状态属性变化
 * <p>
 * <li>第1-4位代表文件内容的状态</li>
 * <li>第5-8位代表业务类型的状态</li>
 * <li>第9-12位代表批注信息的状态</li>
 * <li>第13-16位代表扩展信息的状态</li>
 * <li>第17-20位代表位置索引的状态</li>
 * <p>
 * <i>剩余的做为后续的扩展字段</i>
 * 
 * @author LiuQiangQiang
 * @version 1.0.1
 * @see java.util.Comparator
 */
public class ImageNode implements Comparator<ImageNode>, Serializable {

	private static final long serialVersionUID = 7877857070973999776L;

	private int width = -1;

	private int height = -1;

	private int orientation = -1;

	private long length = 0;
	/**
	 * 图片文件
	 */
	private File file;


	/**
	 * 下载状态TRUE表示已下载
	 */
	private boolean fetchFlag = false;
	
	/**
	 * 文档集ID，默认为空
	 */
	private String appId;
	/**
	 * 后缀名
	 */
	private String suffix;
	/**
	 * 统一资源定位
	 */
	private String externalForm;



	/**
	 * 属性状态
	 */
	private int changeState = 0x0;
	
	/**
	 * 条形码识别值
	 * 
	 */
	private String barcode;
	
	/**
	 * 是否已上传
	 */
	private boolean isUploaded=false;
	public boolean isUploaded() {
		return isUploaded;
	}

	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}

	/**
	 * 
	 * @param file
	 */
	public ImageNode(File file) {
		this.file = file;
		this.fetchFlag = true;
		update();
	}

//	public ImageNode(CustomTreeNode treeNode, IDocObject docObject) {
//		this.treeNode = treeNode;
//		this.docObject = docObject;
//		String temp_file = docObject.getDocFile();
//		if (temp_file != null) {
//			setFile(new File(temp_file));
//		}
//	}

	/**
	 * 获取suffix,获取长度
	 * 
	 */
	@SuppressWarnings("deprecation")
	private void update() {
		try {
			if (file != null) {
				suffix = ImageUtility.suffix(this.file.getName());
				length = file.length();
				externalForm = file.toURL().toExternalForm();
			}
		} catch (Exception e) {
			externalForm = "file:/" + file.getAbsolutePath().replace("\\", "/");
		}
	}



	/**
	 * 是否需要从服务器下载
	 * 
	 * @return 需要下载 返回TRUE
	 */
	public synchronized boolean setFetching() {
		if (!this.fetchFlag)
			return fetchFlag = true;
		return false;
	}

	public synchronized boolean isFetched() {
		return this.fetchFlag;
	}

	public synchronized void setFetched() {
		this.fetchFlag = false;
	}

	

	//加载小图片
	private boolean loading = false;
	/**
	 * 探测小图片是否加载
	 * 
	 * @return 小图片未加载返回TRUE
	 */
	
	public synchronized boolean setAsloading() {
		if (!this.loading)
			return this.loading = true;
		else
			return false;
	}

	/**
	 * 重新加载小图片
	 */
	
	public synchronized void setAsloaded() {
		this.loading = false;
	}
	
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public synchronized int getOrientation() {
		if (this.orientation == -1) {
			try {
				if (getSuffix().equals("jpg")) {
					final Directory directory = JpegMetadataReader
							.readMetadata(getFile()).getDirectory(
									ExifDirectory.class);
					if (directory != null) {
						if (directory
								.containsTag(ExifDirectory.TAG_ORIENTATION)) {
							this.orientation = directory
									.getInt(ExifDirectory.TAG_ORIENTATION);
						}
						if (directory
								.containsTag(ExifDirectory.TAG_EXIF_IMAGE_WIDTH)) {
							this.width = directory
									.getInt(ExifDirectory.TAG_EXIF_IMAGE_WIDTH);
						}
						if (directory
								.containsTag(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT)) {
							this.height = directory
									.getInt(ExifDirectory.TAG_EXIF_IMAGE_HEIGHT);
						}
					}
				} else {
					ImageReader ir=getImageReader(this);
					if(ir==null){
						return orientation;
					}
					final IIOMetadata iioMetadata = getImageReader(this)
							.getImageMetadata(0);
					if (iioMetadata instanceof TIFFMetadataSupport) {
						final IFD ifd = ((TIFFMetadataSupport) iioMetadata)
								.getPrimaryIFD();
						this.orientation = ifd.getOrientation().intValue();
						this.width = ifd.getImageWidth();
					}
				}
			} catch (final Exception e) {
				e.printStackTrace();
				this.orientation = 1;
			}
		}
		return orientation;
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
		update();
	}

//	public IDocObject getDocObject() {
//		return docObject;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ImageNode node1, ImageNode node2) {
		return 0;
	}

//	public void setDocObject(IDocObject docObject) {
//		this.docObject = docObject;
//		String temp_File = this.docObject.getDocFile();
//		setFile(new File(temp_File));
//	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getExternalForm() {
		return externalForm;
	}

	public void setExternalForm(String externalForm) {
		this.externalForm = externalForm;
	}
//
//	public String getPostillayer() {
//		if (postillayer == null && docObject != null)
//			return docObject
//					.getProperty(ImageStoreDef.ObjectProperty.POSTILLAYER);
//		return postillayer;
//	}

//	public void setPostillayer(String postillayer) {
//		if (docObject != null)
//			docObject.setProperty(ImageStoreDef.ObjectProperty.POSTILLAYER,
//					postillayer);
//		// 修改ImageNode属性，当批注修改以后
//		this.postillayer = postillayer;
//	}

	public synchronized boolean isChangeFlag() {
		return changeState > 0;
	}

	public synchronized void clearChangeFlag(){
		this.changeState = 0x0;
	}
	
//	public int getIndex() {
//		if (index == 0 && this.docObject != null) {
//			String str_indexno = docObject
//					.getProperty(ImageStoreDef.ObjectProperty.INDEXNO);
//			if (str_indexno != null)
//				index = Integer.parseInt(str_indexno);
//		}
//		return index;
//	}



	public final String getDim() {
		if ((this.width > 0) && (this.height > 0)) {
			return this.width + " x " + this.height;
		}
		return "";
	}

	/**
	 * <p>
	 * <li>参数的第三位标志为位置状态修改
	 * 
	 * <pre>
	 * e.g 位置状态信息修改
	 * indexchangeFlag = 0x04;
	 * setIndexchangeFlag(indexchangeFlag);
	 * </pre>
	 * 
	 * @param indexchangeFlag
	 *            位置信息状态变化
	 */
	public synchronized void setIndexchangeFlag(int indexchangeFlag) {
		changeState = changeState | ((indexchangeFlag & 0x0F) << 16);
	}

	public int getIndexchageFlag() {
		return (changeState & 0xF0000) >> 16;
	}

	/**
	 * <p>
	 * <li>参数的第一位标志为扩展信息新增</li>
	 * <li>参数的第二位标志为扩展信息删除</li>
	 * <li>参数的第三位标志为扩展信息修改</li>
	 * <li>参数的第四位标志为扩展位</li>
	 * 
	 * <pre>
	 * e.g 验印结果修改
	 * extendchangeFlag = 0x04;
	 * setExtendchangeFlag(extendchangeFlag);
	 * </pre>
	 * 
	 * @param extendchangeFlag
	 *            扩展信息变更状态
	 */
	public synchronized void setExtendchangeFlag(int extendchangeFlag) {
		changeState = changeState | ((extendchangeFlag & 0x0F) << 12);
	}

	/**
	 * 获取扩展信息变更状态
	 * 
	 * @return 返回changeState第13-16位
	 */
	public int getExtendchangeFlag() {
		return (changeState & 0xF000) >> 12;
	}

	/**
	 * <p>
	 * <li>参数的第一位标志为类型新增</li>
	 * <li>参数的第二位标志为类型删除</li>
	 * <li>参数的第三位标志为类型修改</li>
	 * <li>参数的第四位标志为扩展位</li>
	 * 
	 * <pre>
	 * e.g 本地图片分配类型
	 * typeChageFlag = 0x01;
	 * setTypechangeFlag(typeChageFlag);
	 * </pre>
	 * 
	 * @param typeChageFlag
	 *            业务类型变更状态
	 */
	public synchronized void setTypechangeFlag(int typeChageFlag) {
		changeState = changeState | ((typeChageFlag & 0x0F) << 4);
	}

	/**
	 * 获取业务类型信息
	 * 
	 * @return 返回changeState第5-8位
	 */
	public int getTypechangeFlag() {
		return (changeState & 0xF0) >> 4;
	}

	/**
	 * <p>
	 * <li>参数的第一位标志为批注新增</li>
	 * <li>参数的第二位标志为批注删除</li>
	 * <li>参数的第三位标志为批注修改</li>
	 * <li>参数的第四位标志为扩展位</li>
	 * 
	 * <pre>
	 * e.g 批注修改
	 * postilchangeFlag = ox04
	 * setPostilchangeFlag(picchangeFlag)
	 * </pre>
	 * 
	 * @param postilchangeFlag
	 *            状态修改参数
	 */
	public synchronized void setPostilchangeFlag(int postilchangeFlag) {
		changeState = changeState | ((postilchangeFlag & 0x0F) << 8);
	}

	/**
	 * 获取批注的状态信息
	 * 
	 * @return 返回changeState的第9-12位
	 */
	public int getPostilchangeFlag() {
		return (changeState & 0x0F00) >> 8;
	}

	/**
	 * <p>
	 * <li>参数的第一位标志为内容增加</li>
	 * <li>参数的第二位标志为内容删除</li>
	 * <li>参数的第三位标志为内容修改</li>
	 * <li>参数的第四位标志为扩展位</li>
	 * 
	 * <pre>
	 * e.g 内容删除
	 * picchangeFlag = ox02;
	 * setPicchangeFlag(picchangeFlag);
	 * </pre>
	 * 
	 * @param picchangeFlag
	 *            状态修改参数
	 */
	public synchronized void setPicchangeFlag(int picchangeFlag) {
		changeState = changeState | (picchangeFlag & 0x0F);
	}

	/**
	 * 获取内容变化状态
	 * 
	 * @return 返回changeState的1-4位
	 */
	public int getPicchangeFlag() {
		return changeState & 0x0F;
	}

	/**
	 * 重写equals 方法，方便比较重复文件 仅仅针对本地文件比较 *
	 */
//	public boolean equals(Object obj) {
//		if (obj instanceof ImageNode) {
//			ImageNode temp = (ImageNode) obj;
//			if (this.getDocObject() == null && temp.getDocObject() == null) {
//				return getFile().getAbsolutePath().equals(
//						temp.getFile().getAbsolutePath());
//			} else {
//				return super.equals(obj);
//			}
//
//		} else {
//			return super.equals(obj);
//		}
//	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
//		if(this.docObject != null)
			this.appId = appId;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
}
