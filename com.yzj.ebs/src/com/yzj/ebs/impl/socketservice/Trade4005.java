/**
 * Trade4004.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class Trade4005 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		Element result = element.addElement("RSPCD");
		Element sId = element.addElement("RSPMSG");
		String errMsg = "";
		if(element.element("PAGENO") == null || element.element("PAGECOUNT") == null)
		{
			result.addText("999999");
			errMsg = "分页信息不能为空";
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
		}
		// 必输项
		Element accNo = element.element("QRYACCTNO");
		if(accNo == null || StringUtils.isEmpty(accNo.getStringValue()))
		{
			errMsg = "账号不能为空！";
		}
		Element begDocDt = element.element("BEGDOCDT");
		if(begDocDt == null || StringUtils.isEmpty(begDocDt.getStringValue()))
		{
			errMsg = "开始日期不能为空！";
		}
		Element docState = element.element("DOCSTATE");
		if(docState == null || StringUtils.isEmpty(docState.getStringValue()))
		{
			errMsg = "状态不能为空！";
		}
		Element orderBy = element.element("ORDERBY");
		if(orderBy == null || StringUtils.isEmpty(orderBy.getStringValue()))
		{
			errMsg = "排序类型不能为空！";
		}
		if(StringUtils.isNotEmpty(errMsg))
		{
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
		// 结束日期
		Element endDate = element.element("ENDDOCDT");
		if(endDate != null && StringUtils.isNotEmpty(endDate.getStringValue()))
		{
			// 判断开始日期和结束日期是否在一个月期间
			try {
				new SimpleDateFormat("yyyy-MM-dd").parse(begDocDt.getStringValue());
				new SimpleDateFormat("yyyy-MM-dd").parse(endDate.getStringValue());
				String[] startFormat = begDocDt.getStringValue().split("-");
				String[] endFormat = endDate.getStringValue().split("-");
				if(startFormat.length != 3 && endFormat.length != 3)
				{
					result.addText("999999");
					errMsg = "开始日期或结束日期格式不正确！";
					if (StringUtils.isNotEmpty(errMsg)) {
						sId.addText(errMsg);
						tradeSet.getUpParams().put("OUTPUT",
								new StickerProcessor().checkMessageLeng(doc.asXML()));
						return tradeSet;
					}
				}
				if(!startFormat[1].equals(endFormat[1]))
				{
					result.addText("999999");
					errMsg = "开始日期和结束日期必须是同一个月！";
					if (StringUtils.isNotEmpty(errMsg)) {
						sId.addText(errMsg);
						tradeSet.getUpParams().put("OUTPUT",
								new StickerProcessor().checkMessageLeng(doc.asXML()));
						return tradeSet;
					}
				}
			} catch (ParseException e) {
				result.addText("999999");
				e.printStackTrace();
				errMsg = "开始日期或结束日期格式有误！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}
		}
		if(begDocDt.getStringValue().split("-").length != 3)
		{
			result.addText("999999");
			errMsg = "开始日期或结束日期格式不正确！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		// 子账号
		String hql = "select workDate,traceNo,accNo,accSon,dcFlag,traceBal,credit,currType,to_Accno,to_Accname,abs ,checkFlag from ebs_AccnoDetailData_"+begDocDt.getStringValue().split("-")[1]+" where 1=1";
		String totalHql = "select count(*) from ebs_AccnoDetailData_"+begDocDt.getStringValue().split("-")[1]+" where 1=1";
		String ory = "asc";
		if("2".equals(orderBy.getStringValue()))
		{
			ory = "desc";
		}
		String sqlDocstate = "";
		String[] docStates = docState.getStringValue().split(",");
		for(String state : docStates)
		{
			if("1".equals(state))
			{
				sqlDocstate = sqlDocstate+"'1',";
			} else if("2".equals(state))
			{
				sqlDocstate = sqlDocstate+"'3','4',";
			}else if("3".equals(docState.getStringValue()))
			{
				sqlDocstate = sqlDocstate+"'2','5',";
			}
		}
		// 转换checkflag
//				if("1".equals(docState.getStringValue()))
//				{
//					sqlDocstate = "checkFlag='"+docState.getStringValue()+"'";
//				} else if("2".equals(docState.getStringValue()))
//				{
//					sqlDocstate = "checkFlag in ('3','4')";
//				} else if("3".equals(docState.getStringValue()))
//				{
//					sqlDocstate = "checkFlag in ('2','5')";
//				}  
				if(!"0".equals(docState.getStringValue()))
				{
					sqlDocstate = " checkFlag in  ("+sqlDocstate.substring(0,sqlDocstate.length()-1)+")";
					hql = hql   +" and   "+sqlDocstate;
					totalHql = totalHql + " and   "+sqlDocstate;
				}
		 hql = hql+" and accNo='"+accNo.getStringValue()+"' and workDate >='"+begDocDt.getStringValue().replace("-", "")+"'";
		totalHql = totalHql+" and accNo='"+accNo.getStringValue()+"' and workDate >='"+begDocDt.getStringValue().replace("-", "")+"'";
		// 对账日期
		if(element.element("ENDDOCDT") != null && StringUtils.isNotEmpty(element.element("ENDDOCDT").getStringValue()))
		{
			hql = hql + " and workDate <= '"+element.element("ENDDOCDT").getStringValue().replace("-", "")+"'";
			totalHql = totalHql + " and workDate <= '"+element.element("ENDDOCDT").getStringValue().replace("-", "")+"'";
		}
		// 对账日期
		if(element.element("QRYSUBACCTNO") != null && StringUtils.isNotEmpty(element.element("QRYSUBACCTNO").getStringValue()))
		{
		    hql = hql + " and accSon = '"+element.element("QRYSUBACCTNO").getStringValue()+"'";
		    totalHql = totalHql + " and accSon = '"+element.element("QRYSUBACCTNO").getStringValue()+"'";
		}
		hql = hql + " order by workDate "+ory;
		try {
			List<?> list = (List<?>) baseService.findbyPage(hql,pageNo-1,pageCount);
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
					Element wo = row.addElement("WORKDT");
					wo.addText(obj[0]==null?"":String.valueOf(obj[0]));
					Element tr = row.addElement("TRACENO");
					tr.addText(obj[1]==null?"":String.valueOf(obj[1]));
					Element ac = row.addElement("ACCTNO");
					ac.addText(obj[2]==null?"":String.valueOf(obj[2]));
					Element su = row.addElement("SUBACCTNO");
					su.addText(obj[3]==null?"":String.valueOf(obj[3]));
					Element dc = row.addElement("DCFLAG");
					dc.addText(obj[4]==null?"":String.valueOf(obj[4]));
					Element trl = row.addElement("TRACEBAL");
					trl.addText(obj[5]==null?"":String.valueOf(obj[5]));
					Element cr = row.addElement("CREDIT");
					cr.addText(obj[6]==null?"":String.valueOf(obj[6]));
					Element cc = row.addElement("CCY");
					cc.addText(obj[7]==null?"":String.valueOf(obj[7]));
					Element opp = row.addElement("OPPOACCTNO");
					opp.addText(obj[8]==null?"":String.valueOf(obj[8]));
					Element oppn = row.addElement("OPPOACCTNAME");
					oppn.addText(obj[9]==null?"":String.valueOf(obj[9]));
					Element re = row.addElement("REMARK");
					re.addText(obj[10]==null?"":String.valueOf(obj[10]));
					Element ch = row.addElement("CHECKFLAG");
					ch.addText(obj[11]==null?"":String.valueOf(obj[11]));
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
	public static void main(String[] args) {
		String asa = "dssdsd,";
		System.out.println(asa.substring(0,asa.length()-1));
	}
}
