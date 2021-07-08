/**
 * ImageIconFactory.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-4-15
 */
package com.yzj.wf.scan.util;

import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * 定义ICON工厂
 * 定义小图标工厂模式，方便统一管理小图片和释放图片资源
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class ImageIconFactory {
	
	private static HashMap<String, ImageIcon> iconFactory = new HashMap<String, ImageIcon>();
	
	/**
	 * 根据资源文件位置获取图片
	 * <i>
	 * <p>
	 * e.g
	 * <p>
	 * ImageIconFactory.getImageIcon("/icons/test.gif");
	 * </i>
	 * @param resoucesPath 资源相对路径
	 * @return 返回ImageIcon
	 */
	public synchronized static ImageIcon getImageIcon(String resoucesPath) {
		if(iconFactory.containsKey(resoucesPath)){
			return iconFactory.get(resoucesPath);
		}else{
			ImageIcon imageIcon = new ImageIcon(ImageIconFactory.class.getResource(resoucesPath));
			iconFactory.put(resoucesPath,imageIcon);
			return imageIcon;			
		}
	}
	
	/**
	 * 清除图片
	 */
	public synchronized static void clearImageIcon(){
		iconFactory.clear();
	}
}
