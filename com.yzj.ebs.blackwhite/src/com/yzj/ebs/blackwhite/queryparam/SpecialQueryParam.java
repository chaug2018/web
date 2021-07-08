package com.yzj.ebs.blackwhite.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

public class SpecialQueryParam extends PageParam implements Serializable{

	private static final long serialVersionUID = 1L;
	private String accno;	//账号
	private String docdate;
	
	public String getAccno() {
		return accno;
	}
	
	public void setAccno(String accno) {
		this.accno = accno;
	}
	
	public String getDocdate() {
		return docdate;
	}
	
	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}	


}
