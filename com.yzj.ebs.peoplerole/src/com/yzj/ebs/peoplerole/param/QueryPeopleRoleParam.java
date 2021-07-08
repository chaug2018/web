package com.yzj.ebs.peoplerole.param;

import java.io.Serializable;
import com.yzj.ebs.common.param.PageParam;

/**
 * 创建于:2014-01-02<br>
 * 版权所有(C) 2014 深圳市银之杰科技股份有限公司<br>
 * 人员岗位查询
 * @author dengwu
 * @version 1.0.0
 */

public class QueryPeopleRoleParam extends PageParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3258066698387742040L;
	//对账中心
	private String idCenter;
	//网点
	private String idBank;
	//人员名字
	private String peopleName;
	//人员账号
	private String peopleCode;
	//岗位
	private String roleGroupName;
	
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
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
