package com.yzj.ebs.peoplerole.param;

/**
 * 创建于:2014-01-02<br>
 * 版权所有(C) 2014 深圳市银之杰科技股份有限公司<br>
 * 人员岗位查询
 * @author dengwu
 * @version 1.0.0
 */

public class PeopleRole {
	//人员名字
	private String peopleName;
	//人员账号
	private String peopleCode;
	//人员岗位
	private String roleGroupName;
	//人员机构号
	private String orgid; 
	
	
	public String getOrgid() {
		return orgid;
	}
	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}
	public String getPeopleName() {
		return peopleName;
	}
	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}
	public String getPeopleCode() {
		return peopleCode;
	}
	public void setPeopleCode(String peopleCode) {
		this.peopleCode = peopleCode;
	}
	public String getRoleGroupName() {
		return roleGroupName;
	}
	public void setRoleGroupName(String roleGroupName) {
		this.roleGroupName = roleGroupName;
	}
	
}
