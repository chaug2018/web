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
@XmlType(name = "array", propOrder = { "billDataArry" })
@XmlRootElement(name = "array")
public class ResponseBodyArray implements java.io.Serializable {
	@XmlElement(name = "BillDataArry", required = true, nillable = true, type = ResponseTaskInfo.class)
	//private RequestTaskInfo sealChkDtlArry = new RequestTaskInfo();
	private ResponseTaskInfo[] billDataArry ;

	public ResponseTaskInfo[] getBillDataArry() {
		return billDataArry;
	}

	public void setBillDataArry(ResponseTaskInfo[] billDataArry) {
		this.billDataArry = billDataArry;
	}
	

}
