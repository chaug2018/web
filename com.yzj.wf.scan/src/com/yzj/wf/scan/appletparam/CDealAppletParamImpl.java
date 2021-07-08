/**
 * CDealAppletParamImpl.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-5
 */
package com.yzj.wf.scan.appletparam;

import java.io.StringReader;

import javax.swing.JApplet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.yzj.wf.scan.tree.CustomTreeNode;

/**
 * IDealAppletParam接口实现类
 * <p>
 * 需要注入<CODE>JApplet</CODE>引用
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 * @see javax.swing.JApplet
 */
public class CDealAppletParamImpl implements IDealAppletParam {

	private JApplet applet = null;

	public CDealAppletParamImpl() {
		this(new JApplet());
	}

	public CDealAppletParamImpl(JApplet applet) {
		this.applet = applet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.parse.IDealAppletParam#getBooleanByParamName(java.lang.String)
	 */
	public boolean getBooleanByParamName(String paramName) throws Exception {
		String paramValue = getStringByParamName(paramName);

		return "1".equals(paramValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.parse.IDealAppletParam#getStringByParamName(java.lang.String)
	 */
	public String getStringByParamName(String paramName) throws Exception {
		if (paramName == null)
			throw new NullPointerException("Applet属性名不能为空!");
		String paramValue = applet.getParameter(paramName);
		if (paramValue == null)
			return "";
		return paramValue.trim();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.parse.IDealAppletParam#getTreeNodeByParamName(javax.swing.tree.DefaultMutableTreeNode,
	 *      java.lang.String, java.lang.String[])
	 */
	public void getTreeNodeByParamName(DefaultMutableTreeNode rootNode,
			String paramName, String[] orders) throws Exception {
		if (rootNode == null || orders == null)
			throw new NullPointerException("Applet属性名不能为空!");
		String paramVaule = getStringByParamName(paramName);

		if (paramVaule.equals(""))
			return;
		Document document = parserStringToXML(paramVaule);
		parserDocument(document, rootNode, orders);
	}

	/**
	 * 解析String成XML文件
	 */
	private Document parserStringToXML(String xmlString) throws Exception {
		if (xmlString == null || xmlString.equals(""))
			return null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();
			Document document = documentBuilder.parse(new InputSource(
					new StringReader(xmlString)));
			return document;
		} catch (Exception e) {
			throw new Exception("APPLET参数 XMLString不符合规范" + e.getMessage());
		}
	}

	private void parserDocument(Document document, DefaultMutableTreeNode root,
			String[] orders) throws Exception {
		if (document == null) {
			return;
		}
		if (!document.getXmlEncoding().equals("UTF-8")) {
			throw new Exception("传入参数不符合编码规范");
		}
		int deepth = 0;
		Element element = document.getDocumentElement();
		// parse First
		builderTreeNode(element, deepth, root, orders);
	}

	private NodeList getNodeListByName(Element element, String name) {
		NodeList nodeList = element.getElementsByTagName(name.toLowerCase());
		if (nodeList.getLength() != 0)
			return nodeList;
		else
			return element.getElementsByTagName(name.toUpperCase());
	}

	private void builderTreeNode(Element element, int num,
			DefaultMutableTreeNode treeNode, String[] orders) throws Exception {
		//防止数组越界访问异常
		if(orders.length <= num)
			return;
		NodeList nodeList = getNodeListByName(element, orders[num]);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Element temp_element = (Element) nodeList.item(i);
			String value = temp_element.getAttribute("value").equals("") ? temp_element
					.getFirstChild().getTextContent()
					: temp_element.getAttribute("value");
			//add num and code		
			int snum = -1;
			String tempnum = temp_element.getAttribute("num");
			if(!tempnum.equals("")){
				snum = Integer.parseInt(tempnum);
			}
			String code = temp_element.getAttribute("code");
			/**
			 * 获取必要的信息,如业务类型，业务层次等,目前树点类属性待定
			 */
			DefaultMutableTreeNode temp_treeNode = new CustomTreeNode(value, orders[num],false,
					snum,code);
			treeNode.add(temp_treeNode);
			builderTreeNode(temp_element, num + 1, temp_treeNode, orders);
		}
	}

	public JApplet getApplet() {
		return applet;
	}

	public void setApplet(JApplet applet) {
		this.applet = applet;
	}

	 
	public void getTreeNodeByParamValue(DefaultMutableTreeNode rootNode,
			String paramValue, String[] orders) throws Exception {
		if (paramValue.equals(""))
			return;
		Document document = parserStringToXML(paramValue);
		parserDocument(document, rootNode, orders);
	}
	
}
