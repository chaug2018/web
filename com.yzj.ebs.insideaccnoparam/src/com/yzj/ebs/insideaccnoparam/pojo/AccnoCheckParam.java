package com.yzj.ebs.insideaccnoparam.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

public class AccnoCheckParam extends PageParam implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/** 账号*/
	private String accno;
	/** 日期*/
	private String datadate;
	/**对账中心号*/
	private String idcenter;
	/**勾兑结果*/
	private String result;
	/**复核结果*/
	private String recheck;
	
	/** 起始日期*/
	private String begindatadate;
	/** 终止日期*/
	private String enddatadate;
	
	
	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getDatadate() {
		return datadate;
	}

	public void setDatadate(String datadate) {
		this.datadate = datadate;
	}

	public String getIdcenter() {
		return idcenter;
	}

	public void setIdcenter(String idcenter) {
		this.idcenter = idcenter;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRecheck() {
		return recheck;
	}

	public void setRecheck(String recheck) {
		this.recheck = recheck;
	}

	public String getBegindatadate() {
		return begindatadate;
	}

	public void setBegindatadate(String begindatadate) {
		this.begindatadate = begindatadate;
	}

	public String getEnddatadate() {
		return enddatadate;
	}

	public void setEnddatadate(String enddatadate) {
		this.enddatadate = enddatadate;
	}

	
}
