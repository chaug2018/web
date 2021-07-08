package com.yzj.ebs.service.bd.trade2105;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder = { "accNo", "docDt","startNum","queryNum"})
@XmlRootElement(name = "BODY")
public class RequestBodyHrxj implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AccNo", required = true, nillable = true)
	private String accNo = "";
	@XmlElement(name = "DocDt", required = true, nillable = true)
	private String docDt = "";
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
	public String getDocDt() {
		return docDt;
	}
	public void setDocDt(String docDt) {
		this.docDt = docDt;
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
