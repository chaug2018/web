package com.yzj.ebs.impl.socketservice;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.yzj.wf.com.ibank.common.server.IBankServerTradeCodeGetter;

/**
 * 创建于:2012-11-9<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * TODO
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class IBankCodeGetterImpl implements IBankServerTradeCodeGetter{

	@Override
	public String getTradeCode(String input) {
		return "5001";
	}
	

}

