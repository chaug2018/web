package com.yzj.ebs.service.bd.trade2104;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.yzj.ebs.service.common.header.ResponseSysHead;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "service", propOrder = { "responseSysHead"})
@XmlRootElement(name = "service")
public class ResponseService {
    @XmlElement(name = "SYS_HEAD", required = true)
    private ResponseSysHead responseSysHead;

	public ResponseSysHead getResponseSysHead() {
		return responseSysHead;
	}

	public void setResponseSysHead(ResponseSysHead responseSysHead) {
		this.responseSysHead = responseSysHead;
	}

}
