/**
 * RequestHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yzj.ebs.service.bd.trade2102;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BillDataArry", propOrder = { "stmtNo",
		"stmtFlg", "docDt", "accNm", "adr", "ctcPrsn", "ctcTel" })
@XmlRootElement(name = "BillDataArry")
public class ResponseTaskInfo implements java.io.Serializable {
	@XmlElement(name = "StmtNo", required = true, nillable = true)
	private String stmtNo = "";
	@XmlElement(name = "StmtFlg", required = true, nillable = true)
	private String stmtFlg = "";
	@XmlElement(name = "DocDt", required = true, nillable = true)
	private String docDt = "";
	@XmlElement(name = "AccNm", required = true, nillable = true)
	private String accNm = "";
	@XmlElement(name = "Adr", required = true, nillable = true)
	private String adr = "";
	@XmlElement(name = "CtcPrsn", required = true, nillable = true)
	private String ctcPrsn = "";
	@XmlElement(name = "CtcTel", required = true, nillable = true)
	private String ctcTel = "";
	public String getStmtNo() {
		return stmtNo;
	}
	public void setStmtNo(String stmtNo) {
		this.stmtNo = stmtNo;
	}
	public String getStmtFlg() {
		return stmtFlg;
	}
	public void setStmtFlg(String stmtFlg) {
		this.stmtFlg = stmtFlg;
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
	public String getAdr() {
		return adr;
	}
	public void setAdr(String adr) {
		this.adr = adr;
	}
	public String getCtcPrsn() {
		return ctcPrsn;
	}
	public void setCtcPrsn(String ctcPrsn) {
		this.ctcPrsn = ctcPrsn;
	}
	public String getCtcTel() {
		return ctcTel;
	}
	public void setCtcTel(String ctcTel) {
		this.ctcTel = ctcTel;
	}
	

}
