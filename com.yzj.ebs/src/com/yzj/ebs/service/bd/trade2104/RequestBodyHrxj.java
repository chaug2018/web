package com.yzj.ebs.service.bd.trade2104;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder = { "reponseBodyArray"})
@XmlRootElement(name = "BODY")
public class RequestBodyHrxj implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@XmlElement(name = "array", required = true, nillable = true, type = RequestBodyArray.class)
	private RequestBodyArray reponseBodyArray = new RequestBodyArray();
	public RequestBodyArray getReponseBodyArray() {
		return reponseBodyArray;
	}
	public void setReponseBodyArray(RequestBodyArray reponseBodyArray) {
		this.reponseBodyArray = reponseBodyArray;
	}
	
	
}
