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
@XmlType(name = "SYS_HEAD", propOrder = { "tranDate", "tranTime",
		"serviceCode", "serviceScene", "consumerId" })
@XmlRootElement(name = "SYS_HEAD")
public class RequestSysHead implements java.io.Serializable {
	@XmlElement(name = "TranDate", required = true, nillable = true)
	private String tranDate = "";
	@XmlElement(name = "TranTime", required = true, nillable = true)
	private String tranTime = "";
	@XmlElement(name = "ServiceCode", required = true, nillable = true)
	private String serviceCode = "";
	@XmlElement(name = "ServiceScene", required = true, nillable = true)
	private String serviceScene = "";
	@XmlElement(name = "ConsumerId", required = true, nillable = true)
	private String consumerId = "";

	public String getTranDate() {
		return tranDate;
	}

	public void setTranDate(String tranDate) {
		this.tranDate = tranDate;
	}

	public String getTranTime() {
		return tranTime;
	}

	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceScene() {
		return serviceScene;
	}

	public void setServiceScene(String serviceScene) {
		this.serviceScene = serviceScene;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(String consumerId) {
		this.consumerId = consumerId;
	}

}
