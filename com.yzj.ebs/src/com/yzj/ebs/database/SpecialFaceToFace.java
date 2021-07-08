package com.yzj.ebs.database;

public class SpecialFaceToFace implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long autoId;
	//账号
	private String accno;
	
	//对账日期
	private String docDate;

	
	public long getAutoId() {
		return autoId;
	}

	public void setAutoId(long autoId) {
		this.autoId = autoId;
	}

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	
	
}
