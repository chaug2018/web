package com.yzj.ebs.database;

/**
 * 创建于:2013-8-16<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细主表，根据账单编号存放账户交易明细
 * 
 * @author j_sun
 * @version 1.0.0
 */
public class InsideAccnoParam implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	protected Long autoId;
	//内部账户账号
	private String accNo;
	//内部账户操作员
	private String custId;
	//内部账户复核员
	private String recheckCustId;
	//删除标示符   1 删除  0 存在
	private String flog;
	
	
	/*
	 * set  get
	 */

	public String getFlog() {
		return flog;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getRecheckCustId() {
		return recheckCustId;
	}
	public void setRecheckCustId(String recheckCustId) {
		this.recheckCustId = recheckCustId;
	}
	public void setFlog(String flog) {
		this.flog = flog;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
}
