package com.yzj.ebs.common;

/**
 *创建于:2012-10-10<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构参数实体类
 * @author 陈林江
 * @version 1.0
 */
public class BankParam {

	private long id;
	private String idBank;
	private String idBranch;
	private String idCenter;
	private String name;
	private Short level;
	private String updateTime;
	private String updateOper;
	private String orgSid;
	private String phone;
	private String address;
	private String cName;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Short getLevel() {
		return level;
	}
	public void setLevel(Short level) {
		this.level = level;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateOper() {
		return updateOper;
	}
	public void setUpdateOper(String updateOper) {
		this.updateOper = updateOper;
	}
	public String getOrgSid() {
		return orgSid;
	}
	public void setOrgSid(String orgSid) {
		this.orgSid = orgSid;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	
}
