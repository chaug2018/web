/**
 * StickerProcessor.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.common.file.service.FileOperatorService;
import com.yzj.common.file.service.IFileOperator;
import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.CustomerSignInfo;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcess;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 签约接口
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class StickerProcessor implements IBankProcess {
	private IAppTaskAdm taskAdm;
	private IPublicTools publicTools;

	// 数据库操作接口
	private IBaseService<Object> baseService;
	// 签约接口
	private static final String CUSTOMER_SIGN = "6002";
	// 查询交易
	private static final String CUSTOMER_QUERY = "6001";
	// 签约变更
	private static final String SIGN_CHANGE = "6003";
	// 系统对账交易
	private static final String CUSTOMER_EBILL = "6004";
	// 帐号签约信息查询
	private static final String ACC_SIGN_QUERY = "4001";
	// 帐号对仗方式变更发送 
	private static final String ACC_SIGN_CHANGE = "4002";
	// 帐号对帐情况查询
	private static final String ACC_EBILL_QUERY = "4003";
	// 帐号余额对帐结果发送
	private static final String ACC_BALANCE_QUERY = "4004";
	//  帐号交易明细查询
	private static final String ACC_TRADE_QUERY = "4005";
    // 明细对账结果发送
	private static final String ACC_DETAIL_QUERY = "4006";
	 // 账号未达账结果查询
	private static final String ACC_MISS_QUERY = "4007";
     // 账号未达账发送
	private static final String ACC_DETAIL_SEND = "4008";
	
    // 下传路径
	private String unUploadPath;
	// ftp上传路径
	private String ftpPath;


	/***
	 * 重新ibank处理类，实现业务核心交易方法
	 */
	@Override
	public TradeSet execTrade(TradeSet tradeSet) throws IBankProcessException {
		// 数据校验 1.未获取到交易码

		SAXReader reader = new SAXReader();
		Element element = null;
		Document doc = null;
		try {
			if (StringUtils.isNotEmpty(tradeSet.getDownParams().get("INPUT")
					.toString())
					&& tradeSet.getDownParams().get("INPUT").toString()
							.length() > 8) {

				doc = reader.read(new ByteArrayInputStream(tradeSet
						.getDownParams().get("INPUT").toString().substring(8)
						.getBytes()));
				element = doc.getRootElement();
			} else {
				doc = reader.read(new ByteArrayInputStream(tradeSet
						.getDownParams().get("INPUT").toString().getBytes()));
				element = doc.getRootElement();
				// 交易失败原因
				Element failResult = element.addElement("RSPMSG");
				// 交易执行结果
				element.addElement("RSPCD");
				failResult.addText("报文格式不正确！必须由8位长度+报文体。");
				tradeSet.getUpParams().put("OUTPUT",
						checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		} catch (DocumentException e) {
			tradeSet.getUpParams()
					.put("OUTPUT",
							checkMessageLeng("<?xml version=\"1.0\" encoding=\"gbk\"?><root><RSPMSG>报文格式错误！</RSPMSG></root>"));
			e.printStackTrace();
			return tradeSet;
		}
		if (element != null) {
			// 解析报文 根据交易代码判断属于哪个交易
			String tradeId = element.element("TRNCD").getStringValue();
			if (CUSTOMER_QUERY.equals(tradeId)) {
				// 6001交易  
				return new Trade6001().execTrade(tradeSet,element,baseService,doc);
			} else 
			// 6002交易
			if (CUSTOMER_SIGN.equals(tradeId)) {
				return new Trade6002().execTrade(tradeSet,element,baseService,doc);
			} else 
			// 6003交易
			// 签约变更交易
			if (SIGN_CHANGE.equals(tradeId)) {
				return new Trade6003().execTrade(tradeSet,element,baseService,doc);
			}
			// 6004交易
			else if (CUSTOMER_EBILL.equals(tradeId)) {
				return new Trade6004().execTrade(tradeSet,element,baseService,doc,unUploadPath,ftpPath);
			} else if(ACC_SIGN_QUERY.equals(tradeId)) {
			return new Trade4001().execTrade(tradeSet,element,baseService,doc);
				// 4002交易
			} else if(ACC_SIGN_CHANGE.equals(tradeId)) {
				return new Trade4002().execTrade(tradeSet,element,baseService,doc);
				// 4003交易
			} else if(ACC_EBILL_QUERY.equals(tradeId)) {
				return new Trade4003().execTrade(tradeSet,element,baseService,doc);
				// 4004交易
			} else if(ACC_BALANCE_QUERY.equals(tradeId)) {
				return new Trade4004().execTrade(tradeSet,element,baseService,doc);
				// 4005交易
			} else if(ACC_TRADE_QUERY.equals(tradeId)) {
				return new Trade4005().execTrade(tradeSet,element,baseService,doc);
				// 4006交易
			} else if(ACC_DETAIL_QUERY.equals(tradeId)) {
				return new Trade4006().execTrade(tradeSet,element,baseService,doc);
				// 4007交易
			} else if(ACC_MISS_QUERY.equals(tradeId)) {
				return new Trade4007().execTrade(tradeSet,element,baseService,doc);
				// 4008交易
			}  else if(ACC_DETAIL_SEND.equals(tradeId)) {
				return new Trade4008(taskAdm,publicTools).execTrade(tradeSet,element,baseService,doc);
			} else 
			{
				// 返回失败消息，默认为空
				Element error = element.addElement("RSPMSG");
				// 交易码错
				String errMsg = "交易码错！";
				// 交易执行结果
				Element result = element.addElement("RSPCD");
				result.addText("999999");
				error.addText(errMsg);
				
				if (doc != null) {
					tradeSet.getUpParams().put("OUTPUT",
							checkMessageLeng(doc.asXML()));
				}
				return tradeSet;
			}
		}

		if (doc != null) {
			tradeSet.getUpParams().put("OUTPUT", checkMessageLeng(doc.asXML()));
		}
		return tradeSet;
	}
	
    /***
     * 报文长度拼装方法，长度为8为，不足就在前面补0
     * @param message 长度
     * @return 补齐8位后的串
     */
	public String checkMessageLeng(String message) {
		if (StringUtils.isNotEmpty(message)) {
			Integer leng = message.getBytes().length;
			String result = String.valueOf(leng);
			for (int s = result.length(); s < 8; s++) {
				result = "0" + result;
			}
			message = result + message;
		}
		return message;
	}

	public IBaseService<?> getBaseService() {
		return baseService;
	}

	public void setBaseService(IBaseService<Object> baseService) {
		this.baseService = baseService;
	}
	public String getUnUploadPath() {
		return unUploadPath;
	}

	public void setUnUploadPath(String unUploadPath) {
		this.unUploadPath = unUploadPath;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
	public IAppTaskAdm getTaskAdm() {
		return taskAdm;
	}

	public void setTaskAdm(IAppTaskAdm taskAdm) {
		this.taskAdm = taskAdm;
	}
	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

}
