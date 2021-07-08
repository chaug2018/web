package com.yzj.ebs;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class SaxDomTest {
	/***
	 * 测试解析xml报文
	 * @param args
	 */
public static void main(String[] args) {
	String str = "<?xml version=\"1.0\" encoding=\"GBK\"?><root><TRCD>ddddddd</TRCD><PWD>dddsss</PWD></root>";
	SAXReader reader = new SAXReader();
	try {
		Document doc = reader.read(new ByteArrayInputStream(str.getBytes()));
		Element element = doc.getRootElement();
		//Element child = element.element("TRCD");
		Element eee = element.addElement("sddd");
		eee.addText("sss");
		System.out.println(doc.asXML());
	} catch (DocumentException e) {
		e.printStackTrace();
	}
}
}
