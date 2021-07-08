/**
 * HiddenFileFilter.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-21
 */
package com.yzj.wf.scan.util;

import java.io.File;
import java.io.FileFilter;

/**
 * 过滤隐藏文件、DLL文件以及非图像格式文件
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class HiddenFileFilter implements FileFilter {

	public HiddenFileFilter() {

	}

	public boolean accept(File f) {
		if(f.isDirectory())
			return false;
		String suffix =  ImageUtility.suffix(f.getName());
		if(suffix == null)
			return false;
		
		return ImageUtility.isSupportedImage(suffix);
	}

	public String getDescription() {
		return "load image only";
	}

}
