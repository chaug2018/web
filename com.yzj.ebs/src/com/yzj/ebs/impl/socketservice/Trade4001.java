/**
 * Trade4001.java
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
import com.yzj.ebs.database.BasicInfo;
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
public class Trade4001 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		String accNo = "";
		String custNo  ="";
		Element result = element.addElement("RSPCD");
		if(element.element("QRYACCTNO") != null )
		{
		// 帐号
		 accNo = element.element("QRYACCTNO").getStringValue();
		}
		// 客户号
		if(element.element("CUSTNO") != null)
		{
		 custNo = element.element("CUSTNO").getStringValue();
		}
		String errMsg = "";
		Element sId = element.addElement("RSPMSG");
		if(StringUtils.isEmpty(custNo) && StringUtils.isEmpty(accNo))
		{
			result.addText("999999");
			errMsg = "客户号和账户不能同时为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		Element msgType = element.element("MSGTYPE");
		if(element.element("MSGTYPE") == null)
		{
			result.addText("999999");
			errMsg = "消息类型不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		msgType.setText("RQ");
		
		String hql = "select bc from BasicInfo bc where 1=1";
		if(StringUtils.isNotEmpty(accNo))
		{
			hql = hql+" and ACCNO='"+accNo+"'";
		}
		if(StringUtils.isNotEmpty(custNo))
		{
			hql = hql + " and custId = '"+custNo+"'";
		}
		try {
			result.addText("000000");
			List<BasicInfo> list = (List<BasicInfo>) baseService.findByHql(hql);
			if(list == null || list.size() < 1)
			{
				Element count = element.addElement("RSROWNUM");
				count.addText("0");
				
				
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			} else 
			{
				Element count = element.addElement("RSROWNUM");
				count.addText(String.valueOf(list.size()));
				for(BasicInfo bi : list)
				{
					Element row = element.addElement("ROW");
					Element accNoE = row.addElement("ACCTNO");
					accNoE.addText(bi.getAccNo()==null?"":bi.getAccNo());
					Element accSonE = row.addElement("SUBACCTNO");
					accSonE.addText(bi.getAccSon()==null?"":bi.getAccSon());
					Element sendModeE = row.addElement("SENDMODE");
					sendModeE.addText(bi.getSendMode()==null?"":bi.getSendMode());
					Element linkManE = row.addElement("LINKMAN");
					linkManE.addText(bi.getLinkMan()==null?"":bi.getLinkMan());
					Element phoneE = row.addElement("PHONE");
					phoneE.addText(bi.getPhone()==null?"":bi.getPhone());
					Element addressE = row.addElement("ADDRESS");
					addressE.addText(bi.getAddress()==null?"":bi.getAddress());
					Element singTimeE = row.addElement("SIGNTIME");
					singTimeE.addText(bi.getSignTime()==null?"":bi.getSignTime());
				}
				if (doc != null) {
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
				}
				
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return tradeSet;
	}
}
