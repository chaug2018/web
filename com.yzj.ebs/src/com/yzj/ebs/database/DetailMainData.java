package com.yzj.ebs.database;

import com.yzj.ebs.util.UtilBase;

/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 原始数据处理类，存放每月科技部提供的数据
 * @author qinjingfeng
 * @version 1.0.0
 */
public class DetailMainData implements java.io.Serializable {
	
	private static final long serialVersionUID = 1373645648444840547L;

	protected Long autoId;
	
	/** 规则匹配*/
	private String keyType;
	/** 月末余额 */
	private Double credit;
	/** 月累计发生额 */
	private Double totalAmount_month;
	/** 月最大发生额*/
	private Double maxAmount_month;
	/** 月均余额 */
	private Double avBalance_month;
	/** 账单编号 */
	private String voucherNo;
	/** 账号 */
	private String accNo;
	/** 子账号 */
	private String accNoSon;
	/** 打印序号 */
	private String accNoIndex;
	/** 账户名称 */
	private String accName;
	/** 对账日期 */
	private String docDate;
	/** 币种*/
	private String currency;
	/** 发送日期*/
	private String sendDate;

	/** 数据处理日期 */
	private String dealDate;
	/** 发送方式 */
	private String sendMode;
	/** 对账频率*/
	private String accCycle;

	/** 行号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/**行名*/
	private String bankName;

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
	/** 签约标记 */
	private String signFlag;
	/**  账户类型 */
	protected String accType; 
	/**  验印账号 */
	protected String sealAccNo; 
	/** 科目号 */
	protected String subjectNo;
	/**  重点账户标记*/
	protected String keyFlag;
	/**  开户日期*/
	protected String openDate;
	/**  存贷标记*/
	protected String saveLoanFlag;
	/**  产品号*/
	protected String productNo;
	/**  产品号描述*/
	protected String productDesc;
	private String faceFlag;//面对面标志
	// 扩展字段
	/** 币种*/
	private String currencyCN;
	/** 对账频率*/
	private String accCycleCN; 
	/** 签约标记 */
	private String signFlagCN;
	/**  账户类型 */
	protected String accTypeCN; 
	/**  重点账户标记*/
	protected String keyFlagCN;
	/**  存贷标记*/
	protected String saveLoanFlagCN;
	
	private String faceFlagCN;

	private String singleAccno;//单账号，主账号加分账号组成
	
	private String strcredit; //页面展示金额String
	
	
	private String currtype;
	
	private String centerphone;//对账中心电话
	
	
	private String centeraddress; //对账中心地址
	
	
	public String getCurrtype() {
		return currtype;
	}

	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}

	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}

	public String getSingleAccno() {
		return singleAccno;
	}

	public void setSingleAccno(String singleAccno) {
		this.singleAccno = singleAccno;
	}

	public Long getAutoId() {
		return autoId;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getTotalAmount_month() {
		return totalAmount_month;
	}

	public void setTotalAmount_month(Double totalAmount_month) {
		this.totalAmount_month = totalAmount_month;
	}

	public Double getMaxAmount_month() {
		return maxAmount_month;
	}

	public void setMaxAmount_month(Double maxAmount_month) {
		this.maxAmount_month = maxAmount_month;
	}

	public Double getAvBalance_month() {
		return avBalance_month;
	}

	public void setAvBalance_month(Double avBalance_month) {
		this.avBalance_month = avBalance_month;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccNoSon() {
		return accNoSon;
	}

	public void setAccNoSon(String accNoSon) {
		this.accNoSon = accNoSon;
	}

	public String getAccNoIndex() {
		return accNoIndex;
	}

	public void setAccNoIndex(String accNoIndex) {
		this.accNoIndex = accNoIndex;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getDealDate() {
		return dealDate;
	}

	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	public String getSendMode() {
		return sendMode;
	}

	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	public String getAccCycle() {
		return accCycle;
	}

	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
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

	public String getSignFlag() {
		return signFlag;
	}

	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getSealAccNo() {
		return sealAccNo;
	}

	public void setSealAccNo(String sealAccNo) {
		this.sealAccNo = sealAccNo;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(String keyFlag) {
		this.keyFlag = keyFlag;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getSaveLoanFlag() {
		return saveLoanFlag;
	}

	public void setSaveLoanFlag(String saveLoanFlag) {
		this.saveLoanFlag = saveLoanFlag;
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

	public String getFaceFlag() {
		return faceFlag;
	}

	public void setFaceFlag(String faceFlag) {
		this.faceFlag = faceFlag;
	}

	public String getCurrencyCN() {
		return currencyCN;
	}

	public void setCurrencyCN(String currencyCN) {
		this.currencyCN = currencyCN;
	}

	public String getAccCycleCN() {
		return accCycleCN;
	}

	public void setAccCycleCN(String accCycleCN) {
		this.accCycleCN = accCycleCN;
	}

	public String getSignFlagCN() {
		return signFlagCN;
	}

	public void setSignFlagCN(String signFlagCN) {
		this.signFlagCN = signFlagCN;
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

	public String getSaveLoanFlagCN() {
		return saveLoanFlagCN;
	}

	public void setSaveLoanFlagCN(String saveLoanFlagCN) {
		this.saveLoanFlagCN = saveLoanFlagCN;
	}

	public String getFaceFlagCN() {
		return faceFlagCN;
	}

	public void setFaceFlagCN(String faceFlagCN) {
		this.faceFlagCN = faceFlagCN;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getCenterphone() {
		return centerphone;
	}

	public void setCenterphone(String centerphone) {
		this.centerphone = centerphone;
	}

	public String getCenteraddress() {
		return centeraddress;
	}

	public void setCenteraddress(String centeraddress) {
		this.centeraddress = centeraddress;
	}

	
}
