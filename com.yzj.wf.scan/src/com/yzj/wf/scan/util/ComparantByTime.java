/**
 * ComparantByTime.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-21
 */
package com.yzj.wf.scan.util;

import java.util.Comparator;

/**
 * 文件时间比较
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class ComparantByTime implements Comparator<ImageNode> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(ImageNode arg1, ImageNode arg2) {
		Long time1 = arg1.getFile().lastModified();
		Long time2 = arg2.getFile().lastModified();
		return time1.compareTo(time2);
	}

}
