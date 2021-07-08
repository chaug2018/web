package com.yzj.ebs.database;

/**
 * 
 *创建于:2013-8-23<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账户类型定制 实体类
 * @author j_sun
 * @version 1.0.0
 */
public class RuleOfAccCycle implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	/** 序号*/
	protected Long autoId;
	/** 账户类型*/
	private String accCycle;
	/** 科目号*/
	private String subjectNo;
	/** 余额1*/
	private String minBal;	
	/** 余额2*/
	private String maxBal;
	/** 单笔发生额1*/
	private String oneMinAccrual;
	/** 单笔发生额2*/
	private String oneMaxAccrual;
	/** 累计发生额1*/
	private String totalMinAccrual;
	/** 累计发生额2*/
	private String totalMaxAccrual;
	/** 账户状态*/
	private String accnoState;
	/** execute 0执行  1 废弃*/
	private String executeFlog;
	
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public String getAccnoState() {
		return accnoState;
	}
	public void setAccnoState(String accnoState) {
		this.accnoState = accnoState;
	}
	public String getExecuteFlog() {
		return executeFlog;
	}
	public void setExecuteFlog(String executeFlog) {
		this.executeFlog = executeFlog;
	}
	public String getMinBal() {
		return minBal;
	}
	public void setMinBal(String minBal) {
		this.minBal = minBal;
	}
	public String getMaxBal() {
		return maxBal;
	}
	public void setMaxBal(String maxBal) {
		this.maxBal = maxBal;
	}
	public String getOneMinAccrual() {
		return oneMinAccrual;
	}
	public void setOneMinAccrual(String oneMinAccrual) {
		this.oneMinAccrual = oneMinAccrual;
	}
	public String getOneMaxAccrual() {
		return oneMaxAccrual;
	}
	public void setOneMaxAccrual(String oneMaxAccrual) {
		this.oneMaxAccrual = oneMaxAccrual;
	}
	public String getTotalMinAccrual() {
		return totalMinAccrual;
	}
	public void setTotalMinAccrual(String totalMinAccrual) {
		this.totalMinAccrual = totalMinAccrual;
	}
	public String getTotalMaxAccrual() {
		return totalMaxAccrual;
	}
	public void setTotalMaxAccrual(String totalMaxAccrual) {
		this.totalMaxAccrual = totalMaxAccrual;
	}
	
	
}
