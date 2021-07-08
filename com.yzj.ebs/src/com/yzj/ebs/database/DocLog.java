package com.yzj.ebs.database;


/**
 * 票据日志类 
 * @author qinjingfeng
 * @update 2012-9-19
 */

public class DocLog implements java.io.Serializable {
	private static final long serialVersionUID = 9023379394214691954L;
	private Long autoId;
	private String voucherNo;
	private String accNo;
	private String appId;
	private String opCode;  //操作柜员号
	private String opName;  //柜员名称
	private String opDate;
	private String dealResult; 
	private String logDesc;
	private Long docId;
	private String appIdCN;
	private String idBank;   
	private String bankName;   
	private String taskName;
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
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
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public String getDealResult() {
		return dealResult;
	}
	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}
	public String getLogDesc() {
		return logDesc;
	}
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getAppIdCN() {
		return appIdCN;
	}
	public void setAppIdCN(String appIdCN) {
		this.appIdCN = appIdCN;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}