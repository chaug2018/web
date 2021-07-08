package com.yzj.ebs.service.bd.trade2102;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.yzj.ebs.service.common.header.RequestSysHead;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = { "requestSysHead", "requestBodyHrxj" })
@XmlRootElement(name = "service")
public class RequestService {
	@XmlElement(name = "SYS_HEAD", required = true, nillable = true, type = RequestSysHead.class)
	private RequestSysHead requestSysHead = new RequestSysHead();

	@XmlElement(name = "BODY", required = true, nillable = true, type = RequestBodyHrxj.class)
	private RequestBodyHrxj requestBodyHrxj = new RequestBodyHrxj();

	public RequestSysHead getRequestSysHead() {
		return requestSysHead;
	}

	public void setRequestSysHead(RequestSysHead requestSysHead) {
		this.requestSysHead = requestSysHead;
	}

	public RequestBodyHrxj getRequestBodyHrxj() {
		return requestBodyHrxj;
	}

	public void setRequestBodyHrxj(RequestBodyHrxj requestBodyHrxj) {
		this.requestBodyHrxj = requestBodyHrxj;
	}

}
