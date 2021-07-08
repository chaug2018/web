/**
 * SwingFileFilter.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-19
 */
package com.yzj.wf.scan.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 过滤非图像格式文件
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class SwingFileFilter extends FileFilter {
	public boolean accept(File f) {
		return ImageUtility.isSupportedImage(f) || f.isDirectory();
	}

	public String getDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("*.jpg");
		sb.append(",*.gif");
		sb.append(",*.crw");
		sb.append(",*.cr2");
		sb.append(",*.dng");
		sb.append(",*.mrw");
		sb.append(",*.nef");
		sb.append(",*.pef");
		sb.append(",*.jpeg");
		sb.append(",*.bmp");
		sb.append(",*.tif");
		return sb.toString();
	}
}
