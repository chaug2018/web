package com.yzj.ebs.service.bd.trade2102;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder = { "accNo", "flg", "beginDt",
		"endDt", "startNum", "queryNum"})
@XmlRootElement(name = "BODY")
public class RequestBodyHrxj implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "Flg", required = true, nillable = true)
	private String flg = "";
	@XmlElement(name = "BeginDt", required = true, nillable = true)
	private String beginDt = "";
	@XmlElement(name = "EndDt", required = false, nillable = false)
	private String endDt = "";
	@XmlElement(name = "StartNum", required = true, nillable = true)
	private String startNum = "";
	@XmlElement(name = "QueryNum", required = true, nillable = true)
	private String queryNum = "";
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public String getBeginDt() {
		return beginDt;
	}
	public void setBeginDt(String beginDt) {
		this.beginDt = beginDt;
	}
	public String getEndDt() {
		return endDt;
	}
	public void setEndDt(String endDt) {
		this.endDt = endDt;
	}
	public String getStartNum() {
		return startNum;
	}
	public void setStartNum(String startNum) {
		this.startNum = startNum;
	}
	public String getQueryNum() {
		return queryNum;
	}
	public void setQueryNum(String queryNum) {
		this.queryNum = queryNum;
	}
	
}
