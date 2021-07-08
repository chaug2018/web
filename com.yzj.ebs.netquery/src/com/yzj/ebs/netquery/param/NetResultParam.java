package com.yzj.ebs.netquery.param;

import com.yzj.ebs.util.UtilBase;

public class NetResultParam {
	//对账中心
	private String idCenter;
	//支行
	private String idBranch;
	//网点
	private String idBank;
	//网点名
	private String bankName;
	//账号
	private String accNo;
	//子账号
	private String accSon;
	//发送方式，也可称为对账方式
	private String sendMode;
	//账户名称
	private String accName;
	//对账日期
	private String docDate;
	//币种
	private String currency;
	//余额
	Double credit;
	//对账结果
	private String checkFlag;
	//对账次数
	private String billcount;
	//余额的转换string
    private String strcredit;
    
	
	public NetResultParam(String idCenter,String idBank,String bankName,String accNo,String accSon,String sendMode,String accName,
			String docDate,String currency,Double credit ,String checkFlag, String billcount){
		super();
		this.idCenter = idCenter;
		this.idBank=idBank;
		this.bankName=bankName;
		this.accNo=accNo;
		this.accSon=accSon;
		this.sendMode=sendMode;
		this.accName=accName;
		this.docDate=docDate;
		this.currency=currency;
		this.credit=credit;
		this.checkFlag=checkFlag;
		this.billcount=billcount;
		
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
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
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getCredit() {
		return credit;
	}
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getBillcount() {
		return billcount;
	}
	public void setBillcount(String billcount) {
		this.billcount = billcount;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}

}
