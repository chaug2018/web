package com.yzj.ebs.blackwhite.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 
 *创建于:2012-10-18<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 黑白名单查询参数类
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class BWQueryParam extends PageParam implements Serializable{

	private static final long serialVersionUID = -8507822126050316080L;
	private String idCenter;	//清算中心
	private String idBank; // 支行号
	private String signTime;	//维护日期
	private String custId; // 客户号
	private String accNo;	//账号
	private String accName;		//户名
	private String accCycle;	//账户类型
	private String ischeck; //是否对账


	public String getSignTime() {
		return signTime;
	}
	public void setSignTime(String signTime) {
		this.signTime = signTime;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getidCenter() {
		return idCenter;
	}
	public void setidCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
