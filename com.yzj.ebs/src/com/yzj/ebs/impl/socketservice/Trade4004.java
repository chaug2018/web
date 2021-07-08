/**
 * Trade4004.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
public class Trade4004 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		
		String errMsg = "";
		Element voucherId = element.element("VOUCHERNO");
		Element docState = element.element("DOCSTATE");
		Element msgType = element.element("MSGTYPE");
		Element sId = element.addElement("RSPMSG");
		Element result = element.addElement("RSPCD");
		if(msgType == null)
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
		if(voucherId == null || StringUtils.isEmpty(voucherId.getStringValue()))
		{
			result.addText("999999");
			errMsg = "账单编号不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}else
		{
			if(!"1".equals(docState.getStringValue()) &&!"2".equals(docState.getStringValue())&&!"3".equals(docState.getStringValue())&&!"4".equals(docState.getStringValue()))
			{
				result.addText("999999");
				errMsg = "状态数据有误，请重新输入！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}
		}
		if(docState != null && StringUtils.isEmpty(docState.getStringValue()))
		{
			errMsg = "状态不能为空！";
			result.addText("999999");
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		String workDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String checkFlag = "";
		if("2".equals(docState.getStringValue()))
		{
			checkFlag = "3";
		}else if("3".equals(docState.getStringValue()))
		{
			checkFlag = "2";
		}
		// docstate 已回收 /回收日期
		String hql2 = "update CheckMainData set proveflag = '0', workDate = '"+workDate+"',docstate = '3' where  voucherNo='"+voucherId.getStringValue()+"'";
		String hql = "update  AccNoMainData   set finalcheckFlag='"+checkFlag+"' ,checkflag = '"+checkFlag+"' where voucherNo='"+voucherId.getStringValue()+"'";
		
		try {
			Integer biCount =  (Integer) baseService.ExecQuery(hql);
			Integer count2 =  (Integer) baseService.ExecQuery(hql2);
			if(biCount > 0 && count2 > 0)
			{
				// 根据对账单查询对账日期
				List<String> dt =(List<String>) baseService.findBySql("select DOCDATE from ebs_checkmaindata where voucherNo = '"+voucherId.getStringValue()+"'");
				String docDate = dt.get(0);
				if(StringUtils.isEmpty(docDate) || docDate.length() > 8 || Integer.valueOf(docDate.substring(4,6)) > 12)
				{
					result.setText("999999");
					errMsg = "该对账单对账日期格式不正确，请联系技术人员！";
					if (StringUtils.isNotEmpty(errMsg)) {
						sId.addText(errMsg);
						tradeSet.getUpParams().put("OUTPUT",
								new StickerProcessor().checkMessageLeng(doc.asXML()));
						return tradeSet;
					}
				}
				// 更新发生额明细状态
				//String detailHql = "update ebs_AccnoDetailData_"+docDate.substring(4,6) +" set checkFlag = '"+checkFlag+"' where voucherNo = '"+voucherId.getStringValue()+"'";
				 //baseService.ExecQuery(detailHql);
				result.addText("000000");
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			} else 
			{
				result.addText("999999");
				sId.addText("没有更新的记录，请检查更新条件！");
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
			sId.addText("变更时出现业务异常！");
			result.addText("999999");
			
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			
		}
		return tradeSet;
	}
}
