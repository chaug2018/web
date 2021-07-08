package com.yzj.ebs.service.bd.trade2103;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BODY", propOrder = {"requestBodyArray" })
@XmlRootElement(name = "BODY")
public class ResponseBodyHrxj implements java.io.Serializable {

    @XmlElement(name = "array", required = true, nillable = true, type = ResponseBodyArray.class)
	private ResponseBodyArray reponseBodyArray = new ResponseBodyArray();

	public ResponseBodyArray getReponseBodyArray() {
		return reponseBodyArray;
	}

	public void setReponseBodyArray(ResponseBodyArray reponseBodyArray) {
		this.reponseBodyArray = reponseBodyArray;
	}

}
