/**
 * Trade6001.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CustomerSignInfo;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 签约接口
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class Trade6001 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<Object> baseService,Document doc) throws IBankProcessException {

		// 交易失败原因
		Element failResult = element.addElement("RSPMSG");
		// 交易执行结果
		Element result = element.addElement("RSPCD");
		// 账号
		if(element.element("ACCTNO") ==null || StringUtils.isEmpty(element.element("ACCTNO").getStringValue()))
		{
			result.setText("999999");
			failResult.addText("账号不能为空。");
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		if(element.element("BRCNO") ==null || StringUtils.isEmpty(element.element("BRCNO").getStringValue()))
		{
			result.setText("999999");
			failResult.addText("机构号不能为空。");
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		String brcNo = element.element("BRCNO").getStringValue();
		// 客户号
		String cusId = "";
		if(element.element("CUSTNO") != null)
		{
			cusId = element.element("CUSTNO").getStringValue();
		}
		// 帐号
		String accNo = element.element("ACCTNO").getStringValue();
		String errMsg = "";
		if (StringUtils.isEmpty(brcNo)) {
			errMsg = "机构号不能为空！";
		} else if (StringUtils.isEmpty(cusId)) {
			errMsg = "客户号不能为空！";
		} 
		Element sId = element.addElement("RSPMSG");
		if (StringUtils.isNotEmpty(errMsg)) {
			sId.addText(errMsg);
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		CustomerSignInfo customer = new CustomerSignInfo();
		try {
			List<CustomerSignInfo> list = (List<CustomerSignInfo>) baseService
					.findByHql("from CustomerSignInfo where custid = '"
							+ cusId + "' and brcNo='" + brcNo + "' and accno = '"+accNo+"'");
			if (list == null || list.size() < 1) {
				result.setText("000000");
				sId.addText("找不到签约记录！");
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			} else {
				// 交易成功，返回结果
				customer = list.get(0);
				// 交易执行结果
				result.addText("000000");
				// 户名
				result = element.addElement("ACCTNAME");
				result.addText(customer.getAccname());
				// 验印方式
				result = element.addElement("SEALMODE");
				result.addText(customer.getSealmode());
				// 地址
				result = element.addElement("ADDR");
				result.addText(customer.getAddress());
				// 邮编
				result = element.addElement("ZIPCD");
				result.addText(customer.getZip());
				// 联系人
				result = element.addElement("LINKMAN");
				result.addText(customer.getLinkman());
				// 联系电话
				result = element.addElement("TEL");
				result.addText(customer.getPhone());
				// 手机号
				result = element.addElement("MOBILE");
				result.addText(customer.getMobilephone());
				// 对账方式
				result = element.addElement("SENDMODE");
				result.addText(customer.getSendMode());
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}

	
		return tradeSet;
	}
	
}
