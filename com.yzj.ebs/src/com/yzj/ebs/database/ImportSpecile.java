package com.yzj.ebs.database;

public class ImportSpecile implements java.io.Serializable{

	//账号
	private String accno;
	//账户类型
	private String accCycle;
	//验印模式
	private String sealMode;
	//发送方式
	private String sendMode;
	//判断是否已导入  0 未导入  1 导入
	private String isImport;
	//导入的时间
	private String docDate;
	

	public String getIsImport() {
		return isImport;
	}
	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	public String getSealMode() {
		return sealMode;
	}
	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	
	
}
