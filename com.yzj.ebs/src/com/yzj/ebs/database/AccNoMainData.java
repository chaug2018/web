package com.yzj.ebs.database;

import com.yzj.ebs.util.UtilBase;

/**
 *创建于:2012-9-24<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账号明细主表，根据账单编号存放账户明细
 * @author qinjingfeng
 * @version 1.0.0
 */
public class AccNoMainData  implements java.io.Serializable {
	
	private static final long serialVersionUID = -2494392488134484996L;
	
	private CheckMainData checkMainData;
	
	protected Long autoId;
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
	/** 币种*/
	private String currency;
	/** 余额状态 */
	private String checkFlag;
	/** 最终余额状态 */
	private String finalCheckFlag;
	/** 发送方式*/
	private String sendMode;
	/** 对账频率*/
	private String accCycle;
	/** 签约标记 */
	private String signFlag;
	/** 验印账号*/
	private String sealAccNo; 
	/**  账户类型 */
	private String accType; 
	/** 科目号 */
	private String subjectNo;
	/**  重点账户标记*/
	private String keyFlag;
	/**  开户日期*/
	private String openDate;
	/**  存贷标记*/
	private String saveLoanFlag;
	/**  产品号*/
	private String productNo;
	/**  产品号描述*/
	private String productDesc;
	
	/** 账号至账单的多对一关联 */
	private DocSet docset;
	/** 未达标记  0不是未达，1是未达*/
	private Integer matchFlag;
	
	private String faceFlag;//面对面标志
	
	private String currencyCN;
	private String checkFlagCN;
	private String sendModeCN;
	private String accCycleCN;
	private String signFlagCN;
	private String accTypeCN; 
	private String keyFlagCN;
	private String faceFlagCN;
	private String strcredit; //页面展示金额String
	
	/** 对账日期 */
	private String docDate;
	private String singleAccno;//单账号，主账号加分账号组成
	
	private String currtype;

	private String myvoucherNo;
	private String myidBank;
	private String myaccName;
	private String mydocDate;
	private String myproveFlag;
	private String mydocState;
	private String myworkDate;
	
	/** 行号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/**行名*/
	private String bankName;
	
	private String result;//对账结果 成功 == 验印相符并且余额相符
	
	private String custid;
	
	private String sendaddress;
	
	private String isHBcheck;
	
	private String linkMan;//联系人
	private String distributary; //面对面分流标识
	
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
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
	
	public String getMyworkDate() {
		return checkMainData.getWorkDate();
	}
	public void setMyworkDate(String myworkDate) {
		this.myworkDate = myworkDate;
	}
	public String getMyvoucherNo() {
		return checkMainData.getVoucherNo();
	}
	public void setMyvoucherNo(String myvoucherNo) {
		this.myvoucherNo = myvoucherNo;
	}
	public String getMyidBank() {
		return checkMainData.getIdBank();
	}
	public void setMyidBank(String myidBank) {
		this.myidBank = myidBank;
	}
	public String getMyaccName() {
		return checkMainData.getAccName();
	}
	public void setMyaccName(String myaccName) {
		this.myaccName = myaccName;
	}
	public String getMydocDate() {
		return checkMainData.getDocDate();
	}
	public void setMydocDate(String mydocDate) {
		this.mydocDate = mydocDate;
	}
	public String getMyproveFlag() {
		return checkMainData.getProveFlag();
	}
	public void setMyproveFlag(String myproveFlag) {
		this.myproveFlag = myproveFlag;
	}
	public String getMydocState() {
		return checkMainData.getDocState();
	}
	public void setMydocState(String mydocState) {
		this.mydocState = mydocState;
	}
	public CheckMainData getCheckMainData() {
		return checkMainData;
	}
	public void setCheckMainData(CheckMainData checkMainData) {
		this.checkMainData = checkMainData;
	}
	public String getCurrtype() {
		return currtype;
	}
	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}
	public String getSingleAccno() {
		return singleAccno;
	}
	public void setSingleAccno(String singleAccno) {
		this.singleAccno = singleAccno;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
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
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
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
	public String getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
	public String getSealAccNo() {
		return sealAccNo;
	}
	public void setSealAccNo(String sealAccNo) {
		this.sealAccNo = sealAccNo;
	}
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
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
	public DocSet getDocset() {
		return docset;
	}
	public void setDocset(DocSet docset) {
		this.docset = docset;
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
	public String getCheckFlagCN() {
		return checkFlagCN;
	}
	public void setCheckFlagCN(String checkFlagCN) {
		this.checkFlagCN = checkFlagCN;
	}
	public String getSendModeCN() {
		return sendModeCN;
	}
	public void setSendModeCN(String sendModeCN) {
		this.sendModeCN = sendModeCN;
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
	public String getFaceFlagCN() {
		return faceFlagCN;
	}
	public void setFaceFlagCN(String faceFlagCN) {
		this.faceFlagCN = faceFlagCN;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
		
	public String getFinalCheckFlag() {
		return finalCheckFlag;
	}
	public void setFinalCheckFlag(String finalCheckFlag) {
		this.finalCheckFlag = finalCheckFlag;
	}
	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}
	public Integer getMatchFlag() {
		return matchFlag;
	}
	public void setMatchFlag(Integer matchFlag) {
		this.matchFlag = matchFlag;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getSendaddress() {
		return sendaddress;
	}
	public void setSendaddress(String sendaddress) {
		this.sendaddress = sendaddress;
	}
	public String getIsHBcheck() {
		return isHBcheck;
	}
	public void setIsHBcheck(String isHBcheck) {
		this.isHBcheck = isHBcheck;
	}
	public String getLinkMan() {
		return linkMan;
	}
	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}
	public String getDistributary() {
		return distributary;
	}
	public void setDistributary(String distributary) {
		this.distributary = distributary;
	}
	
}
