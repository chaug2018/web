package com.yzj.ebs.edata.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *创建于:2012-9-28<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 解析xml模板
 * @author Lif
 * @version 1.0.0
 */
public class ReadEdataXml {
	
	protected Map<String, List<String>> importTabeFilelMap;
	protected List<String> importTabelList;
	protected List<String> importFileNameList;
	protected List<String> deviceCode;
	private FileWriter filewriter = null;
	/**
	 * 解析导数XML文件
	 * 
	 * @throws Exception
	 *             解析失败时返回异常
	 */
	@SuppressWarnings("unchecked")
	protected void readEdataXml() throws Exception {
		try {
			File edataxmlfile = new File("EData.xml");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(edataxmlfile);
			Element root = doc.getRootElement();
			importTabeFilelMap = new HashMap<String, List<String>>();
			importTabelList = new ArrayList<String>();
			importFileNameList = new ArrayList<String>();
			deviceCode = new ArrayList<String>();
			List<String> tabelfiellist;
			Iterator<Element> tabeliterator = (Iterator<Element>) root
					.elementIterator("TABLE");
			while (tabeliterator.hasNext()) {
				Element tabelfoo = tabeliterator.next();
				Element fiellistfoo = tabelfoo.element("FIELDLIST");
				tabelfiellist = new ArrayList<String>();
				Iterator<Element> fiellistiterator = fiellistfoo
						.elementIterator("FIELD");
				while (fiellistiterator.hasNext()) {
					tabelfiellist.add(fiellistiterator.next().attributeValue(
							"NAME"));
				}
				importTabeFilelMap.put(tabelfoo.attributeValue("NAME"),
						tabelfiellist);
				importTabelList.add(tabelfoo.attributeValue("NAME"));
				importFileNameList.add(tabelfoo.attributeValue("FILENAME"));
				deviceCode.add(tabelfoo.attributeValue("DEVIDECODE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("解析导数EData.xml文件失败");
		}
	}
	
	/**
	 * 添加日志
	 * @param filepath
	 * @param errorStr
	 *            需要写入日志的内容
	 */
	public void writeErrorLog(String filepath,String errorStr) {
		String errorFileName = "EdataError_";
		DateFormat formatDate = new SimpleDateFormat("yyyy_MM_dd");
		errorFileName = formatDate.format(new Date());
		errorFileName = "EdataError_" + errorFileName + ".log";
		try {
			File file = new File(filepath + "/" + errorFileName);
			if (file.exists()) {
				file.delete();
				filewriter = new FileWriter(file, true);
			} else {
				if (file.createNewFile()) {
					filewriter = new FileWriter(file, true);
				} else {
					System.out.println("创建文件失败：" + filepath + "/"
							+ errorFileName);
				}
			}
			if (filewriter != null) {
				try {
					filewriter.write(errorStr + "\r\n");
					filewriter.flush();
				} catch (IOException e) {
					System.out.println("写入日志失败");
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 每天导入数据文件后要删除这个文件夹
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param dataDate
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String filePath,String dataDate) {
		boolean flag = false;
		String path=filePath + "\\"+ dataDate.replace("-", "");
		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(path);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(path);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		boolean flag;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

}
