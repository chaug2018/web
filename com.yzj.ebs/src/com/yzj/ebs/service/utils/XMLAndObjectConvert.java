package com.yzj.ebs.service.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 创建于:2020-05-29<br>
 * 版权所有(C) 2020 深圳市银之杰科技股份有限公司<br>
 * XML与对象转换工具类
 *
 * @author dupeng
 * @version 1.0
 */
public class XMLAndObjectConvert {

    private XMLAndObjectConvert() {

    }

    /**
     * 通过XML文本创建相应对象
     * 
     * @param xml
     *            xml文本
     * @param clazz
     *            要创建的Class类
     * @return Object 创建后的对象
     * @throws JAXBException
     */
    public static Object createObjectByXML(String xml, Class<?> clazz) throws Exception {
        JAXBContext context = null;
        Reader reader = null;
        try {
            // 去除非法字符 如：bom
            xml = xml.trim().replaceFirst("^([\\W]+)<", "<");
            context = JAXBContext.newInstance(clazz);
            reader = new StringReader(xml);
            javax.xml.bind.Unmarshaller um = context.createUnmarshaller();
            Object obj = um.unmarshal(reader);
            return obj;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将对象转换成XML文本
     * 
     * @param obj
     * @param clazz
     * @return String 转换后的文本
     * @throws JAXBException
     */
    public static String createXMLByObject(Object obj, Class<?> clazz) throws JAXBException {
        JAXBContext context = null;
        Writer writer = null;
        try {
            context = JAXBContext.newInstance(clazz);
            Marshaller m = context.createMarshaller();
            // m.setProperty(Marshaller.JAXB_FRAGMENT, true); //不生成头部信息
            m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 格式化
            writer = new StringWriter();
            m.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw e;
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String formateXML(String xml) {
        String formatedXML = "";
        try {
            Document document = null;
            document = DocumentHelper.parseText(xml);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            formatedXML = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatedXML;
    }

}
