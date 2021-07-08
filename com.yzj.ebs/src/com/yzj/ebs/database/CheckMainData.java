package com.yzj.ebs.database;

import java.util.List;

/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账单基本信息实体类
 * @author qinjingfeng
 * @version 1.0.0
 */
public class CheckMainData  implements java.io.Serializable {

	private static final long serialVersionUID = 7740548282560253580L;
	/** 外表，根据账单编号外键关联 AccnoMainData*/
	protected List<AccNoMainData> accNoMainDataList;
	
	private AccNoMainData accNoMainData;
	

	protected Long autoId;
	/** 账单编号 */
	private String voucherNo;
	/** 账户名称 */
	private String accName;
	/** 对账日期 */
	private String docDate;
	/** 发送日期*/
	private String sendDate;
	/** 回收扫描日期 */
	private String workDate;
	/** 数据处理日期 */
	private String dealDate;
	/** 打印次数 */
	private Integer printTimes;
	/** 账单状态 */
	private String docState;
	/** 验印状态 */
	private String proveFlag;
	/** 正面图像url*/
	private String frontImagePath;
	/** 背面url*/
	private String backImagePath;
	/**
	 * 存储id号
	 */
	private String storeId;
	/** 行号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/**行名*/
	private String bankName;
	/**退信次数*/
	private Integer urgeTimes=0;

	/** 联系地址 */
	private String address;
	/** 邮编 */
	private String zip;
	/** 联系人 */
	private String linkMan;
	/** 联系电话 */
	private String phone;
	/** 客户号 */
	private String custId;
	/** 催收状态  0初始状态  ，无需催收  1需催收*/
	private String rushState;
	/** 催收处理标志 0 未处理 1 已处理*/
	private String rushFlag;
	/** 催收备注*/
	private String rushDesc;
	/** 催收方式*/
	private String rushMethod;
	/** 催收处理意见*/
	private String rushResult;
	/**  退信状态*/
	private String urgeState;
	/**  退信处理标志*/
	private String urgeFlag;
	/**  退信登记备注*/
	private String urgeNote;
	/**  退信登记日期*/
	private String urgeDate;
	/**  退信处理备注*/
	private String urgeDesc;
	/**  退信类型*/
	private String urgeType;
	/**  退信登记人员*/
	private String urgePeople;
	/**  退信处理日期*/
	private String urgeDate1;
	/**  退信处理人员*/
	private String urgePeople1;
	/**  退信处理意见*/
	private String urgeResult;
	/** 发送地址 */
	private String sendAddress;
	
	/**是否是总行对账**/
	private String isHBcheck;
	
	private String rushDate;
	
	private String rushOperCode;
	
	private String customResponse;
	
	//========页面导出所需字段==================
	private String myaccNo;
	private String mystrcredit;
	private String mycurrency;
	private String myfinalCheckFlag;
	@SuppressWarnings("unused")
	private String myaccCycle;
	private String myaccType;
	private String myvoucherNo;
	private String myidBank;
	private String myaccName;
	private String mydocDate;
	private String myproveFlag;
	private String mydocState;
	
	// =========== 扩展字段 ====================
	private boolean selected; // 复选框
	private String docStateCN;
	private String proveFlagCN;
	private String csReplyStataCN;
	private String csReplyFlagCN;
	private String urgeStateCN;
	private String urgeFlagCN;
	private String checkFlagCN;
	
	private String accNo;
	/** 发送方式 */
	private String sendMode;
	
	
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	
	public AccNoMainData getAccNoMainData() {
		return accNoMainData;
	}
	public void setAccNoMainData(AccNoMainData accNoMainData) {
		this.accNoMainData = accNoMainData;
	}
	
	
	public List<AccNoMainData> getAccNoMainDataList() {
		return accNoMainDataList;
	}
	public void setAccNoMainDataList(List<AccNoMainData> accNoMainDataList) {
		this.accNoMainDataList = accNoMainDataList;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getDealDate() {
		return dealDate;
	}
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}
	public Integer getPrintTimes() {
		return printTimes;
	}
	public void setPrintTimes(Integer printTimes) {
		this.printTimes = printTimes;
	}
	public String getDocState() {
		return docState;
	}
	public void setDocState(String docState) {
		this.docState = docState;
	}
	public String getProveFlag() {
		return proveFlag;
	}
	public void setProveFlag(String proveFlag) {
		this.proveFlag = proveFlag;
	}
	public String getFrontImagePath() {
		return frontImagePath;
	}
	public void setFrontImagePath(String frontImagePath) {
		this.frontImagePath = frontImagePath;
	}
	public String getBackImagePath() {
		return backImagePath;
	}
	public void setBackImagePath(String backImagePath) {
		this.backImagePath = backImagePath;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getRushState() {
		return rushState;
	}
	public void setRushState(String rushState) {
		this.rushState = rushState;
	}
	public String getRushFlag() {
		return rushFlag;
	}
	public void setRushFlag(String rushFlag) {
		this.rushFlag = rushFlag;
	}
	public String getRushDesc() {
		return rushDesc;
	}
	public void setRushDesc(String rushDesc) {
		this.rushDesc = rushDesc;
	}
	public String getUrgeState() {
		return urgeState;
	}
	public void setUrgeState(String urgeState) {
		this.urgeState = urgeState;
	}
	public String getUrgeFlag() {
		return urgeFlag;
	}
	public void setUrgeFlag(String urgeFlag) {
		this.urgeFlag = urgeFlag;
	}
	public String getUrgeDesc() {
		return urgeDesc;
	}
	public void setUrgeDesc(String urgeDesc) {
		this.urgeDesc = urgeDesc;
	}
	public String getUrgeNote() {
		return urgeNote;
	}
	public void setUrgeNote(String urgeNote) {
		this.urgeNote = urgeNote;
	}
	public String getUrgeDate() {
		return urgeDate;
	}
	public void setUrgeDate(String urgeDate) {
		this.urgeDate = urgeDate;
	}
	public String getUrgePeople() {
		return urgePeople;
	}
	public void setUrgePeople(String urgePeople) {
		this.urgePeople = urgePeople;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getDocStateCN() {
		return docStateCN;
	}
	public void setDocStateCN(String docStateCN) {
		this.docStateCN = docStateCN;
	}
	public String getProveFlagCN() {
		return proveFlagCN;
	}
	public void setProveFlagCN(String proveFlagCN) {
		this.proveFlagCN = proveFlagCN;
	}
	public String getCsReplyStataCN() {
		return csReplyStataCN;
	}
	public void setCsReplyStataCN(String csReplyStataCN) {
		this.csReplyStataCN = csReplyStataCN;
	}
	public String getCsReplyFlagCN() {
		return csReplyFlagCN;
	}
	public void setCsReplyFlagCN(String csReplyFlagCN) {
		this.csReplyFlagCN = csReplyFlagCN;
	}
	public String getUrgeStateCN() {
		return urgeStateCN;
	}
	public void setUrgeStateCN(String urgeStateCN) {
		this.urgeStateCN = urgeStateCN;
	}
	public String getUrgeFlagCN() {
		return urgeFlagCN;
	}
	public void setUrgeFlagCN(String urgeFlagCN) {
		this.urgeFlagCN = urgeFlagCN;
	}
	public String getCheckFlagCN() {
		return checkFlagCN;
	}
	public void setCheckFlagCN(String checkFlagCN) {
		this.checkFlagCN = checkFlagCN;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRushMethod() {
		return rushMethod;
	}
	public void setRushMethod(String rushMethod) {
		this.rushMethod = rushMethod;
	}
	public String getUrgeType() {
		return urgeType;
	}
	public void setUrgeType(String urgeType) {
		this.urgeType = urgeType;
	}
	public String getRushResult() {
		return rushResult;
	}
	public void setRushResult(String rushResult) {
		this.rushResult = rushResult;
	}
	public String getUrgeResult() {
		return urgeResult;
	}
	public void setUrgeResult(String urgeResult) {
		this.urgeResult = urgeResult;
	}
	public String getUrgeDate1() {
		return urgeDate1;
	}
	public void setUrgeDate1(String urgeDate1) {
		this.urgeDate1 = urgeDate1;
	}
	public String getUrgePeople1() {
		return urgePeople1;
	}
	public void setUrgePeople1(String urgePeople1) {
		this.urgePeople1 = urgePeople1;
	}
	public Integer getUrgeTimes() {
		return urgeTimes;
	}
	public void setUrgeTimes(Integer urgeTimes) {
		this.urgeTimes = urgeTimes;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getMyaccNo() {
		return accNoMainData.getAccNo();
	}
	public void setMyaccNo(String myaccNo) {
		this.myaccNo = myaccNo;
	}
	public String getMystrcredit() {
		return accNoMainData.getStrcredit();
	}
	public void setMycredit(String mystrcredit) {
		this.mystrcredit = mystrcredit;
	}
	public String getMycurrency() {
		return accNoMainData.getCurrency();
	}
	public void setMycurrency(String mycurrency) {
		this.mycurrency = mycurrency;
	}
	public String getMyfinalCheckFlag() {
		return accNoMainData.getFinalCheckFlag();
	}
	public void setMyfinalCheckFlag(String myfinalCheckFlag) {
		this.myfinalCheckFlag = myfinalCheckFlag;
	}
	public String getMyaccCycle() {
		return accNoMainData.getAccCycle();
	}
	public void setMyaccCycle(String myaccCycle) {
		this.myaccCycle = myaccCycle;
	}
	public String getMyaccType() {
		return accNoMainData.getAccType();
	}
	public void setMyaccType(String myaccType) {
		this.myaccType = myaccType;
	}
	public void setMystrcredit(String mystrcredit) {
		this.mystrcredit = mystrcredit;
	}
	public String getRushDate() {
		return rushDate;
	}
	public void setRushDate(String rushDate) {
		this.rushDate = rushDate;
	}
	public String getRushOperCode() {
		return rushOperCode;
	}
	public void setRushOperCode(String rushOperCode) {
		this.rushOperCode = rushOperCode;
	}
	public String getCustomResponse() {
		return customResponse;
	}
	public void setCustomResponse(String customResponse) {
		this.customResponse = customResponse;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getIsHBcheck() {
		return isHBcheck;
	}
	public void setIsHBcheck(String isHBcheck) {
		this.isHBcheck = isHBcheck;
	}
	
}
