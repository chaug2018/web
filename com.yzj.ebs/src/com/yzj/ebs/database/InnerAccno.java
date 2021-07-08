package com.yzj.ebs.database;

/**
 * 创建于:2016-02-18<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 内部账户（全量）
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class InnerAccno implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	//流水号
	protected Long autoId;
	
	//内部账号
	private String accNo;
	
	//录入日期
	private String inputDate;
	
	//录入柜员
	private String inputPeopleCode;
	
	
	public Long getAutoId() {
		return autoId;
	}

	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public String getInputPeopleCode() {
		return inputPeopleCode;
	}

	public void setInputPeopleCode(String inputPeopleCode) {
		this.inputPeopleCode = inputPeopleCode;
	}

}
