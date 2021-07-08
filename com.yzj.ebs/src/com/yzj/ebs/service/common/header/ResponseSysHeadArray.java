/**
 * RequestHeader.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.yzj.ebs.service.common.header;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "array", propOrder = { "reponseRet" })
@XmlRootElement(name = "array")
public class ResponseSysHeadArray implements java.io.Serializable {
	@XmlElement(name = "Ret", required = true, nillable = true, type = ResponseRet.class)
	private ResponseRet reponseRet;

	public ResponseRet getReponseRet() {
		return reponseRet;
	}

	public void setReponseRet(ResponseRet reponseRet) {
		this.reponseRet = reponseRet;
	}
	

}
