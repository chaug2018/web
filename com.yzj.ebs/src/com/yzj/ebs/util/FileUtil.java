package com.yzj.ebs.util;

import java.io.File;

/**
 * 创建于:2013-1-4<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 文件工具类
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class FileUtil {

	/**
	 * 根据路径获取文件对象
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件对象
	 */
	public static File getFileByPath(String filePath) {
		if (filePath == null || filePath.equals("")) {
			return null;
		}
		File file = new File(filePath);
		if (file.exists()) {
			// 如果是绝对路径，则直接返回
			return file;
		}
		String path = "";
		try {
			path = FileUtil.class.getClassLoader().getResource("").toURI().getPath();
		} catch (Exception e1) {
		}
		if (path.contains("classes")) {
			// web环境下
			path = path.replace("classes", filePath);
		} else if (path.contains("bin")) {
			// Java Application
			path = path.replace("bin", filePath);
		} else {
			path = filePath;
		}
		file = new File(path);
		if (file.exists()) {
			return file;
		} else {
			return null;
		}
	}
}
