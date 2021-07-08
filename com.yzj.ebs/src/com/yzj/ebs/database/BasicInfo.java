
package com.yzj.ebs.database;


/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账户信息类
 * @author qinjingfeng
 * @version 1.0.0
 */
public class BasicInfo implements java.io.Serializable {

	private static final long serialVersionUID = -2379548965972709693L;
	private Long autoId;
	private String accNo;
	private String accSon;
	private String idBank;
	private String accName;
	private String sealAccNo;
	private String currency;
	private String address;
	private String zip;
	private String linkMan;
	private String phone;
	private String sendMode;
	private String openDate;
	private String accType;
	private String custId; // 客户号
	private String accState; // 账户状态
	private String idBranch; // 上级管理行
	private String idCenter;
	private String bankName;
	private String faceFlag;//面对面标志
	private String specialFlag;//黑白名单标志
	private String accCycle;//签约对账频率
	private String isSpecile;//是否为特殊帐户表示 0 不是 1 是
	private String isCheck ;//是否对账表示 0对账 1不对账
	private String distributary ;//分流用的
	/**  账单发送地址*/
	private String sendAddress;
	private String remark;//对账状态维护原因
	/** 科目号 */
	protected String subjectNo;
	/**  产品号*/
	protected String productNo;
	/**  产品号描述*/
	protected String productDesc;
	private String signFlag;  //签约标志,0代表未签约,1代表已签约
	private String signTime;
	private String signOpCode; //签约柜员号
	private String signContractNo; //签约合同号
	private String sealMode;  //验印模式
	private String singleAccno;//单账号，主账号加分账号组成

	// ================ 扩展字段 ==============================
	private boolean selected;
	private String currencyCN; // 币种类型
	private String sendModeCN; 
	private String accTypeCN; // 对账方式
	private String keyFlagCN; // 账户分类（重点标记）
	private String accCycleCN; // 签约对账频率
	private String accStateCN;// 账户状态
	private String faceFlagCN;
	private String blackWhiteFlagCN;// 中文
	private String  modifyType;//维护模式
	
	
	
	
	
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccSon() {
		return accSon;
	}
	public void setAccSon(String accSon) {
		this.accSon = accSon;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getSealAccNo() {
		return sealAccNo;
	}
	public void setSealAccNo(String sealAccNo) {
		this.sealAccNo = sealAccNo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
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
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getOpenDate() {
		return openDate;
	}
	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAccState() {
		return accState;
	}
	public void setAccState(String accState) {
		this.accState = accState;
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
	public String getFaceFlag() {
		return faceFlag;
	}
	public void setFaceFlag(String faceFlag) {
		this.faceFlag = faceFlag;
	}
	public String getSpecialFlag() {
		return specialFlag;
	}
	public void setSpecialFlag(String specialFlag) {
		this.specialFlag = specialFlag;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}

	public String getSignOpCode() {
		return signOpCode;
	}
	public void setSignOpCode(String signOpCode) {
		this.signOpCode = signOpCode;
	}
	public String getSignContractNo() {
		return signContractNo;
	}
	public void setSignContractNo(String signContractNo) {
		this.signContractNo = signContractNo;
	}
	public String getSealMode() {
		return sealMode;
	}
	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getCurrencyCN() {
		return currencyCN;
	}
	public void setCurrencyCN(String currencyCN) {
		this.currencyCN = currencyCN;
	}
	public String getSendModeCN() {
		return sendModeCN;
	}
	public void setSendModeCN(String sendModeCN) {
		this.sendModeCN = sendModeCN;
	}
	public String getAccTypeCN() {
		return accTypeCN;
	}
	public void setAccTypeCN(String accTypeCN) {
		this.accTypeCN = accTypeCN;
	}
	public String getKeyFlagCN() {
		return keyFlagCN;
	}
	public void setKeyFlagCN(String keyFlagCN) {
		this.keyFlagCN = keyFlagCN;
	}
	public String getAccCycleCN() {
		return accCycleCN;
	}
	public void setAccCycleCN(String accCycleCN) {
		this.accCycleCN = accCycleCN;
	}
	public String getAccStateCN() {
		return accStateCN;
	}
	public void setAccStateCN(String accStateCN) {
		this.accStateCN = accStateCN;
	}
	public String getFaceFlagCN() {
		return faceFlagCN;
	}
	public void setFaceFlagCN(String faceFlagCN) {
		this.faceFlagCN = faceFlagCN;
	}
	public String getBlackWhiteFlagCN() {
		return blackWhiteFlagCN;
	}
	public void setBlackWhiteFlagCN(String blackWhiteFlagCN) {
		this.blackWhiteFlagCN = blackWhiteFlagCN;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	public String getSingleAccno() {
		return singleAccno;
	}
	public void setSingleAccno(String singleAccno) {
		this.singleAccno = singleAccno;
	}
	public String getModifyType() {
		return modifyType;
	}
	public void setModifyType(String modifyType) {
		this.modifyType = modifyType;
	}
	public String getIsSpecile() {
		return isSpecile;
	}
	public void setIsSpecile(String isSpecile) {
		this.isSpecile = isSpecile;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getDistributary() {
		return distributary;
	}
	public void setDistributary(String distributary) {
		this.distributary = distributary;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
}