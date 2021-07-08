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
@XmlType(name = "array", propOrder = { "dtlArry" })
@XmlRootElement(name = "array")
public class ResponseBodyArray implements java.io.Serializable {
	@XmlElement(name = "DtlArry", required = true, nillable = true, type = ResponseTaskInfo.class)
	private ResponseTaskInfo[] dtlArry ;

	public ResponseTaskInfo[] getDtlArry() {
		return dtlArry;
	}

	public void setDtlArry(ResponseTaskInfo[] dtlArry) {
		this.dtlArry = dtlArry;
	}

}
