/**
 * RequestHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yzj.ebs.service.bd.trade2103;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccNoArry", propOrder = { "stmtNo",
		"chkRslt", "docDt", "accNm", "accNo", "amt", "ccy" })
@XmlRootElement(name = "AccNoArry")
public class ResponseTaskInfo implements java.io.Serializable {
	@XmlElement(name = "StmtNo", required = true, nillable = true)
	private String stmtNo = "";
	@XmlElement(name = "ChkRslt", required = true, nillable = true)
	private String chkRslt = "";
	@XmlElement(name = "DocDt", required = true, nillable = true)
	private String docDt = "";
	@XmlElement(name = "AccNm", required = true, nillable = true)
	private String accNm = "";
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "Amt", required = true, nillable = true)
	private String amt = "";
	@XmlElement(name = "Ccy", required = true, nillable = true)
	private String ccy = "";
	
	public String getStmtNo() {
		return stmtNo;
	}
	public void setStmtNo(String stmtNo) {
		this.stmtNo = stmtNo;
	}
	public String getChkRslt() {
		return chkRslt;
	}
	public void setChkRslt(String chkRslt) {
		this.chkRslt = chkRslt;
	}
	public String getDocDt() {
		return docDt;
	}
	public void setDocDt(String docDt) {
		this.docDt = docDt;
	}
	public String getAccNm() {
		return accNm;
	}
	public void setAccNm(String accNm) {
		this.accNm = accNm;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getCcy() {
		return ccy;
	}
	public void setCcy(String ccy) {
		this.ccy = ccy;
	}
	
}
