package com.yzj.ebs.service.bd.trade2102;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.yzj.ebs.service.common.header.ResponseSysHead;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = { "responseSysHead", "responseBodyHrxj" })
@XmlRootElement(name = "service")
public class ResponseService {
    @XmlElement(name = "SYS_HEAD", required = true)
    private ResponseSysHead responseSysHead;

    @XmlElement(name = "BODY", required = true)
    private ResponseBodyHrxj responseBodyHrxj;

	public ResponseSysHead getResponseSysHead() {
		return responseSysHead;
	}

	public void setResponseSysHead(ResponseSysHead responseSysHead) {
		this.responseSysHead = responseSysHead;
	}

	public ResponseBodyHrxj getResponseBodyHrxj() {
		return responseBodyHrxj;
	}

	public void setResponseBodyHrxj(ResponseBodyHrxj responseBodyHrxj) {
		this.responseBodyHrxj = responseBodyHrxj;
	}

}
