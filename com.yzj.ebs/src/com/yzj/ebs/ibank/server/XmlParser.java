package com.yzj.ebs.ibank.server;
/**
 * XmlParser.java
 * 版权所有(C) 2010 深圳市银之杰科技股份有限公司
 * 创建:Sankooc 2010-11-5
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;


/**
 * xml封装操作类
 * 
 * 重构计划: 1.与业务类解耦 2.控制编码方式
 * 
 * @author Sankooc
 * @version 1.0
 */
public class XmlParser {

	/**
	 * 
	 * 获取xml封装类
	 * 
	 * @param root
	 * @return 返回xml封装类
	 */
	public static Document createXML(String root) {

		Document document;
		try {
			DocumentFactory factroy = DocumentFactory.getInstance();
			document = factroy.createDocument();
			document.addElement(root);
		} catch (RuntimeException e) {
			e.printStackTrace();
			return null;
		}
		return document;
	}

	/**
	 * 
	 * 在指定的Element下创建指定内容的节点
	 * 
	 * @param eleName :属性名
	 * @param content :属性值
	 */
	public static void createElement(Element ele,String eleName,String text) {
		//添加注释
//			ele.addComment(comment);
			Element child = ele.addElement(eleName);
			child.setText(text);
	}

	/**
	 * 
	 * 创建属性
	 * 
	 * @param ele
	 * @param map
	 */
	public static void createAtt(Element ele, Map<String, String> map) {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			ele.addAttribute(key, map.get(key));
		}

	}

	/**
	 * 
	 * 在指定element下创建子节点
	 * 
	 * @param root
	 * @param node
	 * @return 返回节点元素
	 */
	public static Element build(Element root, String node) {
		String[] strs = node.split("/");
		String[] pim = new String[strs.length - 1];
		System.arraycopy(strs, 1, pim, 0, strs.length - 1);
		return build(root, pim);
	}

	/**
	 * 
	 * 在指定element下创建子节点
	 * 
	 * @param root
	 * @param pim
	 * @return 返回创建的节点元素
	 */
	@SuppressWarnings("unchecked")
	public static Element build(Element root, String[] pim) {
		Element current = root;
		for (String nodeName : pim) {
			current = current.addElement(nodeName);
		}
		return current;
	}

	/**
	 * 
	 * 写xml封装类
	 * 
	 * @param document
	 * @param writer
	 *            写操作类
	 * @param format
	 */
	public static void write(Document document, Writer writer,
			OutputFormat format) {
		XMLWriter xmlWriter = new XMLWriter(writer, format);
		try {
			xmlWriter.write(document);
			xmlWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 写到文件上
	 * 
	 * @param document
	 * @param path
	 */
	public static void xml2File(Document document, String path) {
		try {
			File logFile = new File(path);
			// logFile.createNewFile();
			FileWriter fileWriter = new FileWriter(logFile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("GB2312");
			write(document, fileWriter, format);
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("creat xml failed");
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 输出到内存中
	 * 
	 * @param document
	 * @return 返回字符串格式xml
	 */
	public static String xml2String(Document document) {
		StringWriter stringwriter = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		write(document, stringwriter, format);
		return stringwriter.toString();
	}

	/**
	 * 
	 * 获取元素的属性值集合
	 * 
	 * @param t
	 * @return 获取元素的属性集
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getAtt(Element t) {
		List<Attribute> list = t.attributes();
		Map<String, String> ret = new HashMap<String, String>();
		for (Attribute att : list) {
			ret.put(att.getName(), att.getValue());
		}
		return ret;
	}

	/**
	 * 
	 * 从封装类中取出指定 元素的子节点并返回
	 * 
	 * @param document
	 * @param xpath
	 * @return 返回结果集
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> doc2Map(Document document, String xpath) {
		Map<String, String> result = new HashMap<String, String>();
		Element root = document.getRootElement();
		List<Element> eles = document.selectNodes("/" + root.getName() + xpath);
		if (null == eles || eles.size() == 0) {
			return result;
		}
		Iterator<Node> it = eles.get(0).nodeIterator();
		while (it.hasNext()) {

			Node node = it.next();
			if (node instanceof Element) {
				result.put(node.getName(), ((Element) node)
						.attributeValue("Value"));
			}
		}
		return result;
	}
}
