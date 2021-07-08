/**
 * JIThumbnailCache.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-16
 */
package com.yzj.wf.scan.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import com.yzj.wf.scan.item.JIIcon;

/**
 * 获取小图片
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
@SuppressWarnings("unused")
public class JIThumbnailCache {

	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JIThumbnailCache.class);

	private static JIThumbnailCache instanse;

	private final Map<String, JIIcon> cacheThumbnails;

	private ExecutorService qExecutor;

	private boolean processAllIcons = false;
	
	
	private int iDimWidth,iDimHeight;

	private JIThumbnailCache(ReadConfig readConfig) {
		String str_number = readConfig.getPropertyValue("read_qExecutorNumber", "80");
		final int number = Integer.parseInt(str_number);
		log.debug("qExceutor Number is " + str_number);
		cacheThumbnails = Collections.synchronizedMap(new LinkedHashMap<String, JIIcon>(number) {
					private static final long serialVersionUID = 1591465585402962060L;
					protected boolean removeEldestEntry(Map.Entry<String, JIIcon> eldest) {
						return size() > number;
					}
				});
		qExecutor = Executors.newFixedThreadPool(2);
		iDimWidth = Integer.parseInt(readConfig.getPropertyValue("iDimWidth", "150"));
		iDimHeight = Integer.parseInt(readConfig.getPropertyValue("iDimHeight", "150"));
		ImageIO.setUseCache(false);
	}

	public static JIThumbnailCache instance() {
		return instanse;
	}
	
	public static JIThumbnailCache getInstanse() {
		return instanse;
	}
	
	public static void init(ReadConfig readConfig){
		instanse = new JIThumbnailCache(readConfig);
	}

	public void removeCacheSmallIcon(ImageNode imageNode) {
		if (imageNode.getFile() != null)
			this.cacheThumbnails.remove(imageNode.getExternalForm());
	}


	public final boolean isProcessAllIcons() {
		return processAllIcons;
	}

	public final void setProcessAllIcons(boolean processAllIcons) {
		this.processAllIcons = processAllIcons;
	}

	public final synchronized void close() {
		if(qExecutor != null){
			qExecutor.shutdown();
		}
		System.runFinalization();
		System.gc();
	}
	
}
