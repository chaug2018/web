package com.yzj.ebs.database;

import java.util.List;

import com.yzj.ebs.util.UtilBase;

 
  /**
   *创建于:2012-11-20<br>
   *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
   * 对账单查询数据类
   * @author swl
   * @version 1.0.0
   */
public class BillinfoQueryData implements java.io.Serializable{


	
	protected Long autoId;
	/** 外表，根据账单编号外键关联 AccnoMainData*/
	protected List<AccNoMainData> listAccNoMainData;
	private AccNoMainData accNoMainData;
	
	private static final long serialVersionUID = 1L;
	
	/** 账单编号 */
	private String voucherNo;
	/** 账户名称 */
	private String accName;
	/** 对账日期 */
	private String docDate;
	/** 账单状态 */
	private String docState;
	/** 验印状态 */
	private String proveFlag;
	/** 行号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/** 月末余额 */
	private Double credit;
	/** 账号 */
	private String accNo;
	/** 币种*/
	private String currency;
	/** 余额状态 */
	private String checkFlag;
	/** 最终余额状态 */
	private String finalCheckFlag;
	/** 对账频率*/
	private String accCycle;
	/**  账户类型 */
	private String accType; 
	
	private String	strcredit;
	
	public BillinfoQueryData(String voucherNo,String idBank,String accNo,String accName,
			Double credit,String docDate,String currency,String proveFlag,String finalCheckFlag,String docState,String accCycle){
		super();
		this.voucherNo=voucherNo;
		this.idBank=idBank;
		this.accNo=accNo;
		this.accName=accName;
		this.credit=credit;
		this.docDate=docDate;
		this.currency=currency;
		this.proveFlag=proveFlag;
		this.finalCheckFlag=finalCheckFlag;
		this.docState=docState;	
		this.accCycle=accCycle;
	}
	
	public BillinfoQueryData(String voucherNo,String idBank,String accName,
			String docDate,String proveFlag,String docState){
		super();
		this.voucherNo=voucherNo;
		this.idBank=idBank;
		this.accName=accName;
		this.docDate=docDate;
		this.proveFlag=proveFlag;
		this.docState=docState;	
	}
		// =========== 扩展字段 ====================
		private boolean selected; // 复选框
		private String docStateCN;
		private String proveFlagCN;
		private String csReplyStataCN;
		private String accCycleCN;
		private String accTypeCN;
		private String checkFlagCN;
	

	
	public Long getAutoId() {
			return autoId;
		}
	public void setAutoId(Long autoId) {
			this.autoId = autoId;
		}
	public List<AccNoMainData> getListAccNoMainData() {
			return listAccNoMainData;
		}
	public void setListAccNoMainData(List<AccNoMainData> listAccNoMainData) {
			this.listAccNoMainData = listAccNoMainData;
		}
	public AccNoMainData getAccNoMainData() {
			return accNoMainData;
		}
	public void setAccNoMainData(AccNoMainData accNoMainData) {
			this.accNoMainData = accNoMainData;
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
	public Double getCredit() {
			return credit;
		}
	public void setCredit(Double credit) {
			this.credit = credit;
		}
	public String getAccNo() {
			return accNo;
		}
	public void setAccNo(String accNo) {
			this.accNo = accNo;
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
	public String getAccCycle() {
			return accCycle;
		}
	public void setAccCycle(String accCycle) {
			this.accCycle = accCycle;
		}
	public String getAccType() {
			return accType;
		}
	public void setAccType(String accType) {
			this.accType = accType;
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
	public String getAccCycleCN() {
			return accCycleCN;
		}
	public void setAccCycleCN(String accCycleCN) {
			this.accCycleCN = accCycleCN;
		}
	public String getAccTypeCN() {
			return accTypeCN;
		}
	public void setAccTypeCN(String accTypeCN) {
			this.accTypeCN = accTypeCN;
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
	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}

	public String getFinalCheckFlag() {
		return finalCheckFlag;
	}

	public void setFinalCheckFlag(String finalCheckFlag) {
		this.finalCheckFlag = finalCheckFlag;
	}


	
}
