package com.yzj.ebs.service.bd.trade2104;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DtlArry", propOrder = { "accNo",
		"traceDt", "traceNo", "smy", "traceAmt", "flg", "chkRslt" })
@XmlRootElement(name = "DtlArry")
public class RequestDtlArry implements java.io.Serializable{
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "TraceDt", required = true, nillable = true)
	private String traceDt = "";
	@XmlElement(name = "TraceNo", required = true, nillable = true)
	private String traceNo = "";
	@XmlElement(name = "Smy", required = true, nillable = true)
	private String smy = "";
	@XmlElement(name = "TraceAmt", required = true, nillable = true)
	private String traceAmt = "";
	@XmlElement(name = "Flg", required = true, nillable = true)
	private String flg = "";
	@XmlElement(name = "ChkRslt", required = true, nillable = true)
	private String chkRslt = "";
	
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
	public String getTraceAmt() {
		return traceAmt;
	}
	public void setTraceAmt(String traceAmt) {
		this.traceAmt = traceAmt;
	}
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public String getChkRslt() {
		return chkRslt;
	}
	public void setChkRslt(String chkRslt) {
		this.chkRslt = chkRslt;
	}

}
