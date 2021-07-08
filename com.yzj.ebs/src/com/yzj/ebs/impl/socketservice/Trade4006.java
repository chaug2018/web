/**
 * Trade4006.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoDetailData;
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
public class Trade4006 {

	public TradeSet execTrade(TradeSet tradeSet, Element element,
			IBaseService<?> baseService, Document doc)
			throws IBankProcessException {
		Element result = element.addElement("RSPCD");
		Element sId = element.addElement("RSPMSG");
		String errMsg = "";

		Element msgType = element.element("MSGTYPE");
		if (msgType == null) {
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
		List<Element> rows = element.elements("ROW");
		Element total = element.element("RQROWNUM");
		
		if (total == null || StringUtils.isEmpty(total.getStringValue())) {
			result.addText("999999");
			errMsg = "请求总记录数不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		Integer totalNum = 0;
		try {
		    totalNum = Integer.parseInt(total.getStringValue());
			if (totalNum < 0) {
				errMsg = "请求总记录数必须大于或等于0！";
			} else if (rows == null || rows.size() < 0) {
				errMsg = "核对结果集记录为空！";
			} else if (totalNum != rows.size()) {
				errMsg = "请求总记录数和核对结果集记录条数不相符！";
			}
			if (StringUtils.isNotEmpty(errMsg)) {
				result.addText("999999");
				errMsg = "请求总记录数格式有误！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams()
							.put("OUTPUT",
									new StickerProcessor().checkMessageLeng(doc
											.asXML()));
					return tradeSet;
				}
			}
		} catch (Exception e) {
			result.addText("999999");
			errMsg = "请求总记录数格式有误！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		// 判断总记录条数是否为0 ，如果为0时该账号下所有子账户在请求时间段内都为相符
		
			Element beginDate = element.element("BEGDOCDT");
			Element endDate = element.element("ENDDOCDT");
			Element qyAccno = element.element("QRYACCTNO");
			String now = new SimpleDateFormat("yyyyMMdd").format(new Date());
			if(beginDate == null || StringUtils.isEmpty(beginDate.getStringValue()))
			{
				result.addText("999999");
				errMsg = "请求总数为0时开始日期不能为空！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}else if(endDate == null || StringUtils.isEmpty(endDate.getStringValue()))
			{
				result.addText("999999");
				errMsg = "请求总数为0时结束日期不能为空！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			} else if(qyAccno == null || StringUtils.isEmpty(qyAccno.getStringValue())){
				result.addText("999999");
				errMsg = "请求总数为0时账号不能为空！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}
			
			String entityName = "ebs_AccnoDetailData_" + beginDate.getStringValue().split("-")[1];
			
		if(totalNum > 0){
		List<AccNoDetailData> results = new ArrayList<AccNoDetailData>();
		for (Element e : rows) {
			AccNoDetailData acc = new AccNoDetailData();

			if (e.element("ACCTNO") == null
					|| StringUtils
							.isEmpty(e.element("ACCTNO").getStringValue())) {
				errMsg = "结果集中主帐号必须输入！";
			}

			if (StringUtils.isNotEmpty(errMsg)) {

				result.addText("999999");
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams()
							.put("OUTPUT",
									new StickerProcessor().checkMessageLeng(doc
											.asXML()));
					return tradeSet;
				}
			}
			acc.setCheckFlag("2");
			acc.setWorkDate(e.element("WORKDT").getStringValue());
			if (e.element("SUBACCTNO") != null) {
				acc.setAccSon(e.element("SUBACCTNO").getStringValue());
				String hql = "update "+entityName+" set checkdate='"+now+"', checkflag = '3' where workDate >= '"+beginDate.getStringValue().replace("-", "")+"' and workDate <= '"+endDate.getStringValue().replace("-", "")+"' and accNo = '"+qyAccno.getStringValue()+"' and  accSon = '"+acc.getAccSon()+"'";
					Integer queryNum = 0;
					try {
						queryNum = (Integer) baseService.ExecQuery(hql);
						if(queryNum < 1)
						{
							errMsg += acc.getAccSon();
						} 
					} catch (XDocProcException e1) {
						e1.printStackTrace();
					}
			}
			acc.setAccNo(e.element("ACCTNO").getStringValue());
			acc.setWorkDate(e.element("WORKDT").getStringValue());
			acc.setTraceNo(new Long (e.element("TRACENO").getStringValue()));
			results.add(acc);
		}
		
		List<String> hqlList = new ArrayList<String>();
		for (AccNoDetailData accd : results) {
			String dt = accd.getWorkDate();
//			String traceNo =  accd.getTraceNo();
			String traceNo="";
			String accNo = accd.getAccNo();
			String accSon = accd.getAccSon();
			String checkFlag = accd.getCheckFlag();
			if (dt.split("-") != null && dt.split("-").length < 2) {
				result.addText("999999");
				errMsg = "交易日期格式有误！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams()
							.put("OUTPUT",
									new StickerProcessor().checkMessageLeng(doc
											.asXML()));
					return tradeSet;
				}
			}
			String entityName2 = "ebs_AccnoDetailData_" + dt.split("-")[1];
			StringBuffer hql2 = new StringBuffer("");

			hql2.append("update " + entityName2 + " set checkdate='"+now+"', checkflag = '"
					+ checkFlag + "' where    traceNo = '" + traceNo + "' and accNo = '"+accNo+"'");
			if (StringUtils.isNotEmpty(dt)) {
				hql2.append(" and workDate = '" + dt.replace("-", "") + "'");
			}
			if (StringUtils.isNotEmpty(accSon)) {
				hql2.append(" and accSon = '" + accSon + "'");
			}
			hqlList.add(hql2.toString());
		}
		if (StringUtils.isNotEmpty(errMsg)) {
			sId.addText("如下账号找不到更新记录："+errMsg);
			result.addText("000000");
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		try {
			baseService.batchSaveOrUpdateByHql(hqlList);
			result.addText("000000");
		} catch (XDocProcException e1) {
			result.addText("999999");
			errMsg = "明细对账结果发送出现异常，请联系技术人员！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
			e1.printStackTrace();
		}
		}
		result.setText("000000");
		if (doc != null) {
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
		}
		return tradeSet;
	}
}
