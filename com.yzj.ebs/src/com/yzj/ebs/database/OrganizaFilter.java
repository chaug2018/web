package com.yzj.ebs.database;

import java.io.Serializable;

public class OrganizaFilter implements Serializable{
	private static final long serialVersionUID = -2379548965972709693L;
	
	//主键
	private Long autoId;
	//机构号
	private String idBank;
	//上级机构
	private String idBranch;
	//机构名称
	private String bankName;
	//对账中心
	private String idCenter;
	
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
	
}
