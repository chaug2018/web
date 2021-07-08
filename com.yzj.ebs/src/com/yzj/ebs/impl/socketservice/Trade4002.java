/**
 * Trade4002.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账号对账方式变更发送交易
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class Trade4002 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		
		String errMsg = "";
		Element sId = element.addElement("RSPMSG");
		Element msgType = element.element("MSGTYPE");
		Element result = element.addElement("RSPCD");
		if(msgType == null || StringUtils.isEmpty(msgType.getStringValue()))
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
		if(element.element("ACCTNO") == null || StringUtils.isEmpty(element.element("ACCTNO").getStringValue()))
		{
			result.addText("999999");
			errMsg = "总账号不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		if(element.element("SENDMODE") == null || StringUtils.isEmpty(element.element("SENDMODE").getStringValue()))
		{
			result.addText("999999");
			errMsg = "对账方式不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		        // 主帐号
				String accNo = element.element("ACCTNO").getStringValue();
			
				// 对账方式
				String sendMode = element.element("SENDMODE").getStringValue();
				// 联系电话
				Element phone = element.element("PHONE");
				// 联系人
				Element linkMan = element.element("LINKMAN");
				// 联系地址
				Element address = element.element("ADDRESS");
				// 对账方式为柜台或邮寄时联系电话必输/对账方式为邮寄时联系人和联系地址必输
				if("1".equals(sendMode))
				{
					if(phone == null || StringUtils.isEmpty(phone.getStringValue()))
					{
						result.addText("999999");
						errMsg = "柜台对账时联系电话必输！";
						if (StringUtils.isNotEmpty(errMsg)) {
							sId.addText(errMsg);
							tradeSet.getUpParams().put("OUTPUT",
									new StickerProcessor().checkMessageLeng(doc.asXML()));
							return tradeSet;
						}
					}
				} else if("2".equals(sendMode))
				{
					if(phone == null || StringUtils.isEmpty(phone.getStringValue()))
					{
						errMsg = "邮寄对账时联系电话必须输入！！";
					}else
					{
						if(phone.getStringValue().length() > 32)
						{
							errMsg = "联系电话长度溢出！";
						}
					}
					if(linkMan == null || StringUtils.isEmpty(linkMan.getStringValue()))
					{
						errMsg = "邮寄对账时联系人必须输入！！";
					}else
					{
						if(linkMan.getStringValue().length() > 32)
						{
							errMsg = "联系人长度溢出！";
						}
					}
					if(address == null || StringUtils.isEmpty(address.getStringValue()))
					{
						errMsg = "邮寄对账时地址必须输入！！";
					}else
					{
						if(address.getStringValue().length() > 256)
						{
							errMsg = "联系地址长度溢出！";
						}
					}
					if(StringUtils.isNotEmpty(errMsg))
					{
						result.addText("999999");
						if (StringUtils.isNotEmpty(errMsg)) {
							sId.addText(errMsg);
							tradeSet.getUpParams().put("OUTPUT",
									new StickerProcessor().checkMessageLeng(doc.asXML()));
							return tradeSet;
						}
					}
				}
				String lMan = linkMan == null?"":linkMan.getStringValue();
				String callNum =  phone == null?"":phone.getStringValue();
				String addre =  address == null?"":address.getStringValue();
				
		String hql = "update BasicInfo   set linkMan='"+lMan+"', phone = '"+callNum+"' ,address = '"+addre+"', sendMode='"+sendMode+"' ,signTime='"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"' where accNo='"+accNo+"'";
		
		try {
			Integer biCount =  (Integer) baseService.ExecQuery(hql);
			if(biCount > 0)
			{
				
				result.addText("000000");
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		} catch (XDocProcException e) {
			result.addText("999999");
			e.printStackTrace();
			sId.addText("变更时出现业务异常！");
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			
		}
		return tradeSet;
	}
}
