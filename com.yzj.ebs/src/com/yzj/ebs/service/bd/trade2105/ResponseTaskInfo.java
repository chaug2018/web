/**
 * RequestHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yzj.ebs.service.bd.trade2105;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DtlArry", propOrder = { "accNo",
		"traceDt", "traceNo", "smy", "dbFlg", "traceAmt", "amt","oppAccNo","oppAccNm" })
@XmlRootElement(name = "DtlArry")
public class ResponseTaskInfo implements java.io.Serializable {
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "TraceDt", required = true, nillable = true)
	private String traceDt = "";
	@XmlElement(name = "TraceNo", required = true, nillable = true)
	private String traceNo = "";
	@XmlElement(name = "Smy", required = true, nillable = true)
	private String smy = "";
	@XmlElement(name = "DbFlg", required = true, nillable = true)
	private String dbFlg = "";
	@XmlElement(name = "TraceAmt", required = true, nillable = true)
	private String traceAmt = "";
	@XmlElement(name = "Amt", required = true, nillable = true)
	private String amt = "";
	@XmlElement(name = "OppAccNo", required = true, nillable = true)
	private String oppAccNo = "";
	@XmlElement(name = "OppAccNm", required = true, nillable = true)
	private String oppAccNm = "";
	
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getTraceDt() {
		return traceDt;
	}
	public void setTraceDt(String traceDt) {
		this.traceDt = traceDt;
	}
	public String getTraceNo() {
		return traceNo;
	}
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}
	public String getSmy() {
		return smy;
	}
	public void setSmy(String smy) {
		this.smy = smy;
	}
	public String getDbFlg() {
		return dbFlg;
	}
	public void setDbFlg(String dbFlg) {
		this.dbFlg = dbFlg;
	}
	public String getTraceAmt() {
		return traceAmt;
	}
	public void setTraceAmt(String traceAmt) {
		this.traceAmt = traceAmt;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccNm() {
		return oppAccNm;
	}
	public void setOppAccNm(String oppAccNm) {
		this.oppAccNm = oppAccNm;
	}

}
