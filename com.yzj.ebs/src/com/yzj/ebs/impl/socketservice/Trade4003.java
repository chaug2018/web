/**
 * Trade4003.java
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
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账信息查询
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class Trade4003 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		Element sId = element.addElement("RSPMSG");
		String errMsg = "";
		Element result = element.addElement("RSPCD");
		if(element.element("PAGENO") == null || element.element("PAGECOUNT") == null)
		{
			errMsg = "分页信息不能为空";
			result.addText("999999");
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
		}
		Integer pageNo = 0;
		Integer pageCount = 0;
		try {
			 pageNo = Integer.parseInt(element.element("PAGENO")
					.getStringValue());
			 pageCount = Integer.parseInt(element.element("PAGECOUNT")
					.getStringValue());
		} catch (Exception e) {
			result.addText("999999");
			errMsg = "分页信息输入有误！";
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
		}
		String custNo = "";
		String accNo  = "";
		if(element.element("QRYACCTNO") !=  null || element.element("CUSTNO") != null)
		{
			// 帐号
						if(element.element("QRYACCTNO") != null)
						{
					    accNo = element.element("QRYACCTNO").getStringValue();
						}
					// 客户号
						if(element.element("CUSTNO") != null)
						{
					    custNo = element.element("CUSTNO").getStringValue();
						}
		
		Element msgType = element.element("MSGTYPE");
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
		if(StringUtils.isEmpty(accNo) && StringUtils.isEmpty(custNo))
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
		//and ad.sendMode = '3' 
		String hql = "select cd.accName,ad.accNo,ad.accNoSon,ad.voucherNo,ad.currency,ad.docDate,ad.credit,ad.checkFlag,ad.accType,ad.sendMode from EBS_CHECKMAINDATA cd,EBS_ACCNOMAINDATA ad  where cd.voucherNo=ad.voucherNo ";
		String totalHql = "select count(*) from EBS_CHECKMAINDATA cd,EBS_ACCNOMAINDATA ad  where cd.voucherNo=ad.voucherNo ";
		if(StringUtils.isNotEmpty(accNo))
		{
			// 将accNo拼接成in的形式
			String[] accNos = accNo.split(",");
			StringBuffer sb = new StringBuffer("");
			for(String n : accNos)
			{
				sb.append(",'"+n+"'");
			}
			hql = hql+" and ad.accNo in ("+sb.substring(1)+")";
			totalHql = totalHql+" and ad.accNo in ("+sb.substring(1)+")";
		}
		if(StringUtils.isNotEmpty(custNo))
		{
			hql = hql + " and cd.custId = '"+custNo+"'";
			totalHql = totalHql + " and cd.custId = '"+custNo+"'";
		}
		// 对账渠道
		if(element.element("SENDMODE") != null && StringUtils.isNotEmpty(element.element("SENDMODE").getStringValue()))
		{
			hql = hql + " and ad.sendMode = '"+element.element("SENDMODE").getStringValue()+"'";
			totalHql = totalHql + " and ad.sendMode = '"+element.element("SENDMODE").getStringValue()+"'";
		}
		// 对账日期
		if(element.element("QRYDOCDT") != null)
		{
			hql = hql + " and ad.docDate = '"+element.element("QRYDOCDT").getStringValue().replace("-", "")+"'";
			totalHql = totalHql + " and ad.docDate = '"+element.element("QRYDOCDT").getStringValue().replace("-", "")+"'";
		}
		// 状态
		if(element.element("DOCSTATE") != null && StringUtils.isNotEmpty(element.element("DOCSTATE").getStringValue()))
		{
			String[] states = element.element("DOCSTATE").getStringValue().split(",");
			String checkFlag = "";
			for(String s : states)
			{
			if("2".equals(s))
			{
				checkFlag += ",'3','4'";
			}else if("3".equals(s))
			{
				checkFlag += ",'2','5'";
			} else
			{
				checkFlag += ",'"+s+"'";
			}
			}
		    hql = hql + " and ad.checkFlag in ("+checkFlag.substring(1)+")";
		    totalHql = totalHql + " and ad.checkFlag in ("+checkFlag.substring(1)+")";
		}
		try {
			
			List<?> list = (List<?>) baseService.findBySql(hql,pageNo-1,pageCount);
			List<Integer> totalCount = (List<Integer>) baseService.findBySql(totalHql);
			result.addText("000000");
			if(list == null || list.size() < 1)
			{
				Element count = element.addElement("RSROWNUM");
				count.addText("0");
				Element total = element.addElement("TOTALROWNUM");
				total.addText("0");
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			} else 
			{
				Element count = element.addElement("RSROWNUM");
				count.addText(String.valueOf(list.size()));
				Element total = element.addElement("TOTALROWNUM");
				if(totalCount != null && totalCount.size() > 0)
				{
				total.addText(String.valueOf(totalCount.get(0)));
				}
				for(int j = 0; j < list.size(); j++)
				{
					Object[] obj = (Object[]) list.get(j);
					Element row = element.addElement("ROW");
					Element accNoE = row.addElement("ACCTNO");
					accNoE.addText(obj[1]==null?"":String.valueOf(obj[1]));
					Element accNameE = row.addElement("ACCTNAME");
					accNameE.addText(obj[0]==null?"":String.valueOf(obj[0]));
					Element accSonE = row.addElement("SUBACCTNO");
					accSonE.addText(obj[2]==null?"":String.valueOf(obj[2]));
					Element voucherNoE = row.addElement("VOUCHERNO");
					voucherNoE.addText(obj[3]==null?"":String.valueOf(obj[3]));
					Element ccyE = row.addElement("CCY");
					ccyE.addText(obj[4]==null?"":String.valueOf(obj[4]));
					Element docDateE = row.addElement("DOCDT");
					docDateE.addText(obj[5]==null?"":String.valueOf(obj[5]));
					// 统计当月所有的累计发生额
					String totalCreditSql = "select sum(traceBal) from ebs_AccnoDetailData_"+String.valueOf(obj[5]).substring(4,6) +" where docDate like '"+String.valueOf(obj[5]).substring(0,4)+String.valueOf(obj[5]).substring(4,6)+"%'  and accno = '"+String.valueOf(obj[1])+"'";
					List<Object> sumCredit =  (List<Object>) baseService.findBySql(totalCreditSql);
					 if(sumCredit != null && sumCredit.size() > 0)
					 {
						 Element totalTraceCredit = row.addElement("TOTALTRACECREDIT");
						 totalTraceCredit.addText(String.valueOf(sumCredit.get(0) == null ? Integer.valueOf(0) :sumCredit.get(Integer.valueOf(0))));
					 }
					Element creditE = row.addElement("CREDIT");
					creditE.addText(obj[6]==null?"":String.valueOf(obj[6]));
					Element docStateE = row.addElement("DOCSTATE");
					String docs = obj[7]==null?"":String.valueOf(obj[7]);
					// 如果是人工条件不符或相符的则转换成不符或相符给网银
					if("3".equals(docs) || "4".equals(docs))
					{
						docs = "2";
					}else if("2".equals(docs) || "5".equals(docs))
					{
						docs = "3";
					}
					docStateE.addText(docs);
					Element accType = row.addElement("ACCTYPE");
					accType.addText(obj[8]==null?"":String.valueOf(obj[8]));
					Element sendMode = row.addElement("SENDMODE");
					sendMode.addText(obj[9]==null?"":String.valueOf(obj[9]));
				}
				if (doc != null) {
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
				}
				
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		}else
		{
			result.addText("999999");
			 errMsg = "账号和客户号不能同时为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		return tradeSet;
	}
}
