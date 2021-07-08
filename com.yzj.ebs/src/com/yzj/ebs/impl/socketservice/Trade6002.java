/**
 * Trade6002.java
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
import com.yzj.ebs.database.BasicInfo;
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
public class Trade6002 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<Object> baseService,Document doc) throws IBankProcessException {
		String errMsg = "";
		String peopleNo = "";
        if(element.element("TLRNO") != null && StringUtils.isNotEmpty(element.element("TLRNO").getStringValue()))
        {
        	peopleNo = element.element("TLRNO").getStringValue();
        }
        else
        {
        	errMsg = "柜员号不能为空！";
        }
		CustomerSignInfo customer = new CustomerSignInfo();
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
		// 客户号
		if (element.element("CUSTNO") != null
				&& StringUtils.isNotEmpty(element.element("CUSTNO")
						.getStringValue())) {
			customer.setCustid(element.element("CUSTNO")
					.getStringValue());
		} else {
			errMsg = "客户号不能为空！";
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
		// 机构号
		if (element.element("BRCNO") != null
				&& StringUtils.isNotEmpty(element.element("BRCNO")
						.getStringValue())) {
			customer.setBrcNo(element.element("BRCNO").getStringValue());
		} else {
			errMsg = "机构号不能为空！";
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
		// 账号
		if (element.element("ACCTNO") != null
				&& StringUtils.isNotEmpty(element.element("ACCTNO")
						.getStringValue())) {
			customer.setAccNo(element.element("ACCTNO")
					.getStringValue());
		} else {
			errMsg = "账号不能为空！";
		}
		String signId = null;
		// 交易失败原因
		Element failResult = element.addElement("RSPMSG");
		// 交易执行结果
		Element result = element.addElement("RSPCD");
		if (StringUtils.isNotEmpty(errMsg)) {
			failResult.addText(errMsg);
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		try {
			customer.setPeopleNo(peopleNo);
			signId = createSign(customer,baseService);
		} catch (XDocProcException e) {
			e.printStackTrace();
			failResult.addText(e.getMessage());
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		}
		// 返回签约ID
		Element sId = element.addElement("TRNSEQNO");
		sId.addText(signId);
		if (StringUtils.isEmpty(signId)) {
			result.setText("999999");
			failResult.addText("签约时出现未知异常，签约失败。");
			tradeSet.getUpParams().put("OUTPUT",
					new StickerProcessor().checkMessageLeng(doc.asXML()));
			return tradeSet;
		} else {

			result.addText("000000");
		}
		return tradeSet;
	}
	/***
	 * 新开户签约交易
	 * 
	 * @param signInfo
	 *            签约对象
	 * @return
	 * @throws XDocProcException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	private String createSign(CustomerSignInfo signInfo,IBaseService<Object> baseService)
			throws XDocProcException {
		try {

			signInfo = (CustomerSignInfo) baseService.create(signInfo);
			if (signInfo != null && signInfo.getSignID() != null) {
				// 更新客户信息表
				String linkPhone = "";
				if(StringUtils.isNotEmpty(signInfo.getMobilephone()))
				{
					linkPhone = signInfo.getMobilephone();
				} else 
				{
					linkPhone = signInfo.getPhone();
				}
				// 如果不存在则向basicinfo插入一条数据
				List<Object> isExists = (List<Object>) baseService.findBySql("select count(*) from EBS_BASICINFO where accno = '"+signInfo.getAccNo()+"'");
				if(isExists != null &&  isExists.size() >0 && Integer.valueOf(isExists.get(0).toString()) >0)
				{
					
				
				String hql = "update BasicInfo set signTime = '"+signInfo.getSigndate()+"' ,sealMode = " +
						"'"+signInfo.getSendMode()+"',phone = '"+linkPhone+"',zip='"+signInfo.getZip()+"',address = " +
						"'"+signInfo.getAddress()+"',linkMan = '"+signInfo.getLinkman()+"',sendMode=" +
						"'"+signInfo.getSendMode()+ "',custId='"+signInfo.getCustid()+"',accName='"+signInfo.getAccname()+ "'  where accno = '"+signInfo.getAccNo()+"',custId='"+signInfo.getCustid()+"',accName='"+signInfo.getAccname()+"'";
					baseService.ExecQuery(hql);
				}else
				{
					BasicInfo bi = new BasicInfo();
					bi.setSignTime(signInfo.getSigndate());
					bi.setSendMode(signInfo.getSendMode());
					bi.setAddress(signInfo.getAddress());
					bi.setPhone(linkPhone);
					bi.setZip(signInfo.getZip());
					bi.setLinkMan(signInfo.getLinkman());
					bi.setAccNo(signInfo.getAccNo());
					bi.setSealMode(signInfo.getSealmode());
				    bi.setSignFlag("1");
				    bi.setCustId(signInfo.getCustid());
				    bi.setAccName(signInfo.getAccname());
					baseService.create(bi);
				}
				return String.valueOf(signInfo.getSignID());
			}

		} catch (XDocProcException e) {
			throw new XDocProcException(e.getMessage());
		}
		return "";
	}
}
