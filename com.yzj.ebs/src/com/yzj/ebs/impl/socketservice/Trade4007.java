/**
 * Trade4007.java
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
public class Trade4007 {
	
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
		
		String hql = "select voucherNo,docDate,accNo,direction,traceDate,traceNo,traceCredit,finalCheckFlag,checkDesc,checkOpTime,inputDesc from EBS_NOTMATCHTABLE where accNo='"+accNo.getStringValue()+"'";
		String totalHql = "select count(*) from EBS_NOTMATCHTABLE where accNo='"+accNo.getStringValue()+"'";
		String docDt = "";
		// 对账日期
		if(element.element("QRYDOCDT") != null && StringUtils.isNotEmpty(element.element("QRYDOCDT").getStringValue()))
		{
			hql = hql + " and docDate = '"+element.element("QRYDOCDT").getStringValue().replace("-", "")+"'";
			totalHql = totalHql + " and docDate = '"+element.element("QRYDOCDT").getStringValue().replace("-", "")+"'";
			docDt = element.element("QRYDOCDT").getStringValue();
		}
		// 对账编号
		if(element.element("QRYVOUCHERNO") != null && StringUtils.isNotEmpty(element.element("QRYVOUCHERNO").getStringValue()))
		{
		    hql = hql + " and voucherNo = '"+element.element("QRYVOUCHERNO").getStringValue()+"'";
		    totalHql = totalHql + " and voucherNo = '"+element.element("QRYVOUCHERNO").getStringValue()+"'";
		}
		try {
			hql = hql +" order by traceDate";
			List<?> list = (List<?>) baseService.findBySql(hql,pageNo-1,pageCount);
			List<Integer> totalCount = (List<Integer>) baseService.findBySql(totalHql);
			result.addText("000000");
			if(StringUtils.isNotEmpty(docDt))
			{
			try {
				if(docDt.length() != 10 || Integer.valueOf(docDt.substring(5,7)) > 12)
				{
					result.setText("999999");
					errMsg = "对账日期格式不正确！";
					
				}
			} catch (NumberFormatException e) {
				result.setText("999999");
				errMsg = "对账日期格式不正确！";
			}
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
			// 统计当月所有的累计发生额
			String totalCreditSql = "select sum(traceBal) from ebs_AccnoDetailData_"+docDt.substring(5,7) +" where  accno = '"+accNo.getStringValue()+"' and docDate like '"+docDt.substring(0,4)+docDt.substring(5,7)+"%' ";
			List<?> sumCredit =  (List<?>) baseService.findBySql(totalCreditSql);
			 if(sumCredit != null && sumCredit.size() > 0)
			 {
				 Element count = element.addElement("TOTALTRACECREDIT");
				 count.addText(String.valueOf(sumCredit.get(0) == null ? 0 :sumCredit.get(0)));
			 }
			}
			if(list == null || list.size() < 1)
			{
				Element total = element.addElement("TOTALROWNUM");
				total.addText("0");
				Element count = element.addElement("RSROWNUM");
				count.addText("0");
				
				
				
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
					// checkMainDATA  实体类中有accNo，映射文件中没有？
					Element vo = row.addElement("VOUCHERNO");
					vo.addText(obj[0]==null?"":String.valueOf(obj[0]));
					// 网银那边发报文有发空null字符串过来，所以要做下处理
					if("null".equals(String.valueOf(obj[0]))){
						vo.setText("");
					}
					Element docd = row.addElement("DOCDT");
					if(element.element("QRYDOCDT") != null)
					{
					docd.addText(element.element("QRYDOCDT").getStringValue());
					}
//					if("null".equals(String.valueOf(obj[1]))){
//						docd.setText("");
//					}
					Element ac = row.addElement("ACCTNO");
					ac.addText(obj[2]==null?"":String.valueOf(obj[2]));
					Element di = row.addElement("DIRECTION");
					di.addText(obj[3]==null?"":String.valueOf(obj[3]));
					if("null".equals(String.valueOf(obj[3]))){
						di.setText("");
					}
					Element tr = row.addElement("TRACEDATE");
					tr.addText(obj[4]==null?"":String.valueOf(obj[4]));
					if("null".equals(String.valueOf(obj[4]))){
						tr.setText("");
					}
					Element tra = row.addElement("TRACENO");
					tra.addText(obj[5]==null?"":String.valueOf(obj[5]));
					Element trace = row.addElement("TRACECREDIT");
					trace.addText(obj[6]==null?"":String.valueOf(obj[6]));
					if("null".equals(String.valueOf(obj[6]))){
						trace.setText("");
					}
					Element che = row.addElement("CHECKFLAG");
					che.addText(obj[7]==null?"":String.valueOf(obj[7]));
					if("null".equals(String.valueOf(obj[7]))){
						che.setText("");
					}
					Element desc = row.addElement("CHECKDESC");
					desc.addText(obj[8]==null?"":String.valueOf(obj[8]));
					Element time = row.addElement("CHECKOPTIME");
					time.addText(obj[9]==null?"":String.valueOf(obj[9]));
					Element in  = row.addElement("INPUTDESC");
					in.addText(obj[10]==null?"":String.valueOf(obj[10]));
				}
				if (doc != null) {
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
				
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return tradeSet;
	}
}
