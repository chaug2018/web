package com.yzj.ebs.param.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2012-10-10<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细查询参数
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class AccnoDetailQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 4470669405755590552L;
	private String accNo;	//账单编号
	private String workDate;	//对账日期
	private String checkFlag;   //对账结果
	private String idCenter;//对账中心
	private String idBranch;//支行
	private String idBank;//网点
	
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
}
