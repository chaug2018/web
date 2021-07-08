package com.yzj.ebs.service.bd.trade2104;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccNoArry", propOrder = { "stmtNo",
		"docDt", "accNo", "chkRslt"})
@XmlRootElement(name = "AccNoArry")
public class RequestAccNoArry implements java.io.Serializable{
	@XmlElement(name = "StmtNo", required = true, nillable = true)
	private String stmtNo = "";
	@XmlElement(name = "DocDt", required = true, nillable = true)
	private String docDt = "";
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "ChkRslt", required = true, nillable = true)
	private String chkRslt = "";
	
	public String getStmtNo() {
		return stmtNo;
	}
	public void setStmtNo(String stmtNo) {
		this.stmtNo = stmtNo;
	}
	public String getDocDt() {
		return docDt;
	}
	public void setDocDt(String docDt) {
		this.docDt = docDt;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getChkRslt() {
		return chkRslt;
	}
	public void setChkRslt(String chkRslt) {
		this.chkRslt = chkRslt;
	}
	

}
