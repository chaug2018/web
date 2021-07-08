package com.yzj.ebs.service.bd.trade2104;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "array", propOrder = { "accNoArry","dtlArry"})
@XmlRootElement(name = "array")
public class RequestBodyArray implements java.io.Serializable{
	@XmlElement(name = "AccNoArry", required = true, nillable = true, type = RequestAccNoArry.class)
	private RequestAccNoArry[] accNoArry ;
	@XmlElement(name = "DtlArry", required = true, nillable = true, type = RequestDtlArry.class)
	private RequestDtlArry[] dtlArry ;
	public RequestAccNoArry[] getAccNoArry() {
		return accNoArry;
	}
	public void setAccNoArry(RequestAccNoArry[] accNoArry) {
		this.accNoArry = accNoArry;
	}
	public RequestDtlArry[] getDtlArry() {
		return dtlArry;
	}
	public void setDtlArry(RequestDtlArry[] dtlArry) {
		this.dtlArry = dtlArry;
	}
	

}
