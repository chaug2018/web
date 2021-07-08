/**
 * Trade4008.java
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IdCenterParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账信息查询
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class Trade4008 {
	private IAppTaskAdm taskAdm;
	private IPublicTools publicTools;


	public Trade4008(IAppTaskAdm taskAdm,IPublicTools publicTools)
    {
    	this.taskAdm = taskAdm;
    	this.publicTools = publicTools;
    }
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<?> baseService,Document doc) throws IBankProcessException {
		Element result = element.addElement("RSPCD");
		Element sId = element.addElement("RSPMSG");
		String errMsg = "";
	
		
		
		
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
		List<Element> rows = element.elements("ROW");
		Element total = element.element("RQROWNUM");
		if(total == null || StringUtils.isEmpty(total.getStringValue()))
		{
			result.addText("999999");
			errMsg = "请求总记录数不能为空！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		}
		try {
			Integer totalNum = Integer.parseInt(total.getStringValue());
			if(totalNum<1)
			{
				errMsg = "请求总记录数必须大于0！";
			}else if(rows == null || rows.size()<0){
				errMsg = "核对结果集记录为空！";
			}
			else if(totalNum != rows.size())
			{
				errMsg = "请求总记录数和核对结果集记录条数不相符！";
			}
			if(StringUtils.isNotEmpty(errMsg))
			{
				result.addText("999999");
				errMsg = "请求总记录数格式有误！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
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
		List<NotMatchTable> results = new ArrayList<NotMatchTable>();
		for(Element e : rows)
		{
			NotMatchTable acc = new NotMatchTable();
			if(e.element("DIRECTION") == null && StringUtils.isEmpty(e.element("DIRECTION").getStringValue()) )
			{
				result.addText("999999");
				errMsg = "结果集记录中未达方向必须输入！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			} else if(e.element("ACCTNO") == null && StringUtils.isEmpty(e.element("ACCTNO").getStringValue())){
				result.addText("999999");
				errMsg = "结果集记录中帐号必须输入！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}else if(e.element("TRACEDATE") == null && StringUtils.isEmpty(e.element("TRACEDATE").getStringValue())){
				result.addText("999999");
				errMsg = "结果集记录中交易日期必须输入！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}else if(e.element("TRACECREDIT") == null && StringUtils.isEmpty(e.element("TRACECREDIT").getStringValue())){
				result.addText("999999");
				errMsg = "结果集记录中交易金额必须输入！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}
			else{
			if(e.element("VOUCHERNO") != null)
			{
				acc.setVoucherNo(e.element("VOUCHERNO").getStringValue());
			}
			if(e.element("REMARK") != null)
			{
				acc.setInputDesc(e.element("REMARK").getStringValue());
			}
			if(e.element("DOCDT") != null)
			{
				acc.setDocDate(e.element("DOCDT").getStringValue()==null?"":e.element("DOCDT").getStringValue());
			}
			if(e.element("TRACENO") != null)
			{
				acc.setTraceNo(e.element("TRACENO").getStringValue());
			}
			   acc.setAccNo(e.element("ACCTNO").getStringValue());
			   acc.setDirection(e.element("DIRECTION").getStringValue());
			   acc.setTraceDate(e.element("TRACEDATE").getStringValue());
			   try {
				double credit = Double.valueOf(e.element("TRACECREDIT")
						.getStringValue());
				acc.setTraceCredit(credit);
			} catch (Exception e2) {
				result.addText("999999");
				errMsg = "结果集记录中交易金额格式错误！";
				if (StringUtils.isNotEmpty(errMsg)) {
					sId.addText(errMsg);
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
					return tradeSet;
				}
			}
			}
			results.add(acc);
		}
		String notmatchVouno = results.get(0).getVoucherNo();
		try {
		
		List<Object[]> cmList =  (List<Object[]>) baseService.findBySql("select cd.idCenter,cd.voucherNo,ad.accNo from ebs_checkmaindata cd ,ebs_accnomaindata ad where cd.voucherNo = ad.voucherNo and cd.voucherNo = '"+notmatchVouno+"'");
		if(cmList == null || cmList.size() < 1)
		{
			result.setText("999999");
			errMsg = "账单编号不存在！";
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
		}
		Object[] obj  = cmList.get(0);
			
		// 创建人物至未达录入   用户SID，CODE ，docset 图像属性 。opcode
		DocSet docSet = this.createDocSet(new ArrayList<String>(), String.valueOf(obj[0]));
		docSet.setVoucherNo(String.valueOf(obj[1]));
		try {
			docSet.setDescItem("accno_0", String.valueOf(obj[2]));
			docSet.setDescItem("checkflag_0", "2");
			// 查询子账户
			List<String> accSon = (List<String>) baseService.findBySql("select accnoSon from ebs_accnomaindata where voucherno = '"+notmatchVouno+"'");
			docSet.setDescItem("accson_0", accSon.get(0));
		} catch (Exception e2) {
			errMsg = "创建未达任务时出现异常！";
		}
		
		taskAdm.createTaskFromEbank(docSet, new XPeopleInfo(),"ebill");
		for(NotMatchTable accd : results)
		{
			if(accd.getVoucherNo().equals(docSet.getVoucherNo()))
			{
				accd.setDocId(docSet.getDocId());
			}
		}
		
		
		List<String> hqlList = new ArrayList<String>();
		String workDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		for(NotMatchTable accd : results)
		{
			String voucherNo = accd.getVoucherNo();
			String docdt = accd.getDocDate().replace("-", "");
			String accNo = accd.getAccNo();
			String direction = accd.getDirection();
			String traceDate = accd.getTraceDate();
			String traceNo = accd.getTraceNo();
			String inputDesc = accd.getInputDesc();
			String traceCredit = "";
			String docId = "";
			if(accd.getDocId() != null)
			{
				docId = String.valueOf(accd.getDocId());
			}
			if(accd.getTraceCredit() != null)
			{
				traceCredit = String.valueOf(accd.getTraceCredit());
			}
			
			String seqName = "notmatchtable_autoid.nextval";
			StringBuffer hql = new StringBuffer("");
			hql.append("insert into ").append("ebs_notmatchtable")
			.append("(autoid,voucherNo,docDate,accNo,direction,traceDate,traceNo,traceCredit,docId,checkflag,inputDesc,finalCheckFlag) values("+seqName+",'"+voucherNo+"','"+docdt+"','"+accNo+"','"+direction+"','"+traceDate+"','"+traceNo+"',"+traceCredit+",'"+docId+"','2','"+inputDesc+"','2')");
			//String hql1 = "update EBS_CHECKMAINDATA set workDate = '"+workDate+"',docstate = '3' where  voucherNo='"+voucherNo+"'";
			String hql2 = "update EBS_ACCNOMAINDATA   set checkFlag='2' where voucherNo='"+voucherNo+"'";
			hqlList.add(hql.toString());
			//hqlList.add(hql1);
			hqlList.add(hql2);
		}
		
			baseService.batchSaveOrUpdateByHql(hqlList);
			result.addText("000000");
			if (doc != null) {
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
		} catch (XDocProcException e1) {
			result.setText("999999");
			errMsg = "明细对账结果发送出现异常，请联系技术人员！";
			if (StringUtils.isNotEmpty(errMsg)) {
				sId.addText(errMsg);
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
				return tradeSet;
			}
			e1.printStackTrace();
		}
		result.setText("000000");
		return tradeSet;
	}
	
	public IAppTaskAdm getTaskAdm() {
		return taskAdm;
	}

	public void setTaskAdm(IAppTaskAdm taskAdm) {
		this.taskAdm = taskAdm;
	}
	private DocSet createDocSet(List<String> imgs,String orgNo) throws XDocProcException {
		//  根据账单编号查出机构号 ，对账中心
		BankParam bp=publicTools.getBankParam(orgNo);
		IdCenterParam pc=publicTools.getParamIdcenter(bp.getIdCenter());
		Date date = new Date();
		DocSet docSet = new DocSet();
		docSet.setCredit(0.0);
		docSet.setNeedNotMatch((short)1);     //默认不需要做未达，在发现有未达项时再将次字段改为1
		docSet.setIdCenter(pc.getIdCenterNo());
		docSet.setIdBranch(bp.getIdBranch());
		docSet.setIdBank(bp.getIdBank());
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");
		docSet.setWorkDate(sdfYMD.format(date));
		//docSet.setFrontImagePath(imgs.get(0));
		//docSet.setBackImagePath(imgs.get(1));
		docSet.setStoreId("");
		docSet.setDocTypeId(1);
		docSet.setOpCode100("ebill"); // 扫描柜员id号
		docSet.setIsFree(1);
		docSet.setCallTimes(0);
		docSet.setProveFlag(0);
		
		return docSet;
	}
    public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}
}
