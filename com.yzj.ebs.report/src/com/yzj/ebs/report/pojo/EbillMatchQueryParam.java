package com.yzj.ebs.report.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账有效率统计查询类
 * @author 施江敏
 * @version 1.0.0
 */
public class EbillMatchQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 1L;
	//对账渠道
	private String sendMode;
	//对账日期
	private String docDate;
	//对账中心
	private String idCenter;
	//对账支行
	private String idBranch;
	//机构
	private String idBank;
	//机构
	private String idBank1;
	
	//科目号
	private String accType;
	//账户类型
	private String accCycle;

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
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

	public String getIdBank1() {
		return idBank1;
	}

	public void setIdBank1(String idBank1) {
		this.idBank1 = idBank1;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getSendMode() {
		return sendMode;
	}

	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	public String getAccCycle() {
		return accCycle;
	}

	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	
	
	
}
