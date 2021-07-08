package com.yzj.ebs.edata.bean;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2012-11-23<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账号、对账单查询参数类
 * @author Lif
 * @version 1.0.0
 */
public class AccnoMainDataQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 4470669405755590552L;
	private String accNo;	
	private String docDate;	//对账日期
	
	private String idCenter;	//支行号
	private String idBank; // 网点号
	private String custId; // 客户号
	private String accName;		//户名
	private String voucherNo;
	private String proveFlag;	//验印状态
	private String docstateFlag;	//账单状态
	private String sendMode;	//发送方式
	

	public String getDocstateFlag() {
		return docstateFlag;
	}
	public void setDocstateFlag(String docstateFlag) {
		this.docstateFlag = docstateFlag;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getProveFlag() {
		return proveFlag;
	}
	public void setProveFlag(String proveFlag) {
		this.proveFlag = proveFlag;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
