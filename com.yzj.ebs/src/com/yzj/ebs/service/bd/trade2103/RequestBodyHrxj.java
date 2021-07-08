package com.yzj.ebs.service.bd.trade2103;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder = { "stmtNo", "docDt"})
@XmlRootElement(name = "BODY")
public class RequestBodyHrxj implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "StmtNo", required = true, nillable = true)
	private String stmtNo = "";
	@XmlElement(name = "DocDt", required = true, nillable = true)
	private String docDt = "";
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
	
}
