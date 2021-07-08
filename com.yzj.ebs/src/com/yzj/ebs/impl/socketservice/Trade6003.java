/**
 * Trade6003.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
public class Trade6003 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<Object> baseService,Document doc) throws IBankProcessException {

		String errMsg = "";
		// 机构号
		if (element.element("BRCNO") == null
				|| StringUtils.isEmpty(element.element("BRCNO")
						.getStringValue())) {
			errMsg = "签约日期不能为空！";
		}
		// 客户号
		if (element.element("CUSTNO") == null
				|| StringUtils.isEmpty(element.element("CUSTNO")
						.getStringValue())) {
			errMsg = "客户号不能为空！";
		}
		// 客户号
		if (element.element("ACCTNO") == null
				|| StringUtils.isEmpty(element.element("ACCTNO")
						.getStringValue())) {
			errMsg = "账号不能为空！";
		}
		if (StringUtils.isNotEmpty(errMsg)) {
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		CustomerSignInfo customer = new CustomerSignInfo();
		try {
			List<CustomerSignInfo> list = (List<CustomerSignInfo>) baseService
					.findByHql("from CustomerSignInfo where custid = '"
							+ element.element("CUSTNO")
									.getStringValue() + "' and brcNo='"
							+ element.element("BRCNO").getStringValue()
							+ "'");
			if (list != null && list.size() > 0) {
				customer = list.get(0);
			} else {
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}

		} catch (XDocProcException e) {
			e.printStackTrace();
		}

		// 柜面流水
		if (element.element("SRCSEQNO") != null
				&& StringUtils.isNotEmpty(element.element("SRCSEQNO")
						.getStringValue())) {
			customer.setCounterID(element.element("SRCSEQNO")
					.getStringValue());
		} else {
			errMsg = "柜面流水不能为空！";
		}
		// 签约日期
		if (element.element("SIGNDT") != null
				&& StringUtils.isNotEmpty(element.element("SIGNDT")
						.getStringValue())) {
			customer.setSigndate(element.element("SIGNDT")
					.getStringValue());
		} else {
			errMsg = "签约日期不能为空！";
		}
		// 户名
		if (element.element("ACCTNAME") != null
				&& StringUtils.isNotEmpty(element.element("ACCTNAME")
						.getStringValue())) {
			customer.setAccname(element.element("ACCTNAME")
					.getStringValue());
		} else {
			errMsg = "户名不能为空！";
		}
		// 验印方式
		if (element.element("SEALMODE") != null
				&& StringUtils.isNotEmpty(element.element("SEALMODE")
						.getStringValue())) {
			customer.setSealmode(element.element("SEALMODE")
					.getStringValue());
		} else {
			errMsg = "验印方式不能为空！";
		}
		// 地址
		if (element.element("ADDR") != null
				&& StringUtils.isNotEmpty(element.element("ADDR")
						.getStringValue())) {
			customer.setAddress((element.element("ADDR")
					.getStringValue()));
		}
		// 邮编
		if (element.element("ZIPCD") != null
				&& StringUtils.isNotEmpty(element.element("ZIPCD")
						.getStringValue())) {
			customer.setZip(element.element("ZIPCD").getStringValue());
		}
		// 联系人
		if (element.element("LINKMAN") != null
				&& StringUtils.isNotEmpty(element.element("LINKMAN")
						.getStringValue())) {
			customer.setLinkman(element.element("LINKMAN")
					.getStringValue());
		}

		// 联系电话
		if (element.element("TEL") != null) {
			customer.setPhone(element.element("TEL").getStringValue());
		}
		// 手机号
		if (element.element("MOBILE") != null) {
			customer.setMobilephone(element.element("MOBILE")
					.getStringValue());
		}
		// 对账方式
		if (element.element("SENDMODE") != null
				&& StringUtils.isNotEmpty(element.element("SENDMODE")
						.getStringValue())) {
			customer.setSendMode(element.element("SENDMODE")
					.getStringValue());
		} else {
			errMsg = "对账方式不能为空！";
		}
		try {
			String signId = updateSignInfo(customer,baseService);
			// 交易执行结果
			Element result = element.addElement("RSPCD");
			result.addText("000000");
			// 返回失败消息，默认为空
			result = element.addElement("RSPMSG");
			// 签约流水号
			result = element.addElement("TRNSEQNO");
			result.addText(signId);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	
		return tradeSet;
	}
	/***
	 * 签约变更交易
	 * 
	 * @param signInfo
	 *            签约对象
	 * @return
	 * @throws XDocProcException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private String updateSignInfo(CustomerSignInfo signInfo,IBaseService<Object> baseService)
			throws XDocProcException {
		try {
			// 判断是否重复签约
			List<CustomerSignInfo> list = (List<CustomerSignInfo>) baseService
					.findByHql("from CustomerSignInfo where custid = '"
							+ signInfo.getCustid() + "' and brcNo='"
							+ signInfo.getBrcNo() + "'");
			if (list == null || list.size() < 1) {
				throw new XDocProcException("客户不存在！");
			} else {
				signInfo = (CustomerSignInfo) baseService.update(signInfo);
				if (signInfo != null && signInfo.getSignID() != null) {
					String linkPhone = "";
					if(StringUtils.isNotEmpty(signInfo.getMobilephone()))
					{
						linkPhone = signInfo.getMobilephone();
					} else 
					{
						linkPhone = signInfo.getPhone();
					}
					String hql = "update BasicInfo set signTime = '"+signInfo.getSigndate()+"' ,sealMode = " +
							"'"+signInfo.getSendMode()+"',phone = '"+linkPhone+"',zip='"+signInfo.getZip()+"',address = " +
							"'"+signInfo.getAddress()+"',linkMan = '"+signInfo.getLinkman()+"',sendMode=" +
							"'"+signInfo.getSendMode()+"' where accno = '"+signInfo.getAccNo()+"'";
						baseService.ExecQuery(hql);
					return String.valueOf(signInfo.getSignID());
				} else {
					throw new XDocProcException("变更失败,传入参数有误！");
				}
			}
		} catch (XDocProcException e) {
			throw new XDocProcException(e.getMessage());
		}
	}
}
