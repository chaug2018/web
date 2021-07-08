package com.yzj.ebs.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *创建于:2012-10-23<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 建档机构模型
 * @author 陈林江
 * @version 1.0
 */
public class SimpleOrg implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 9021804048604349574L;
	private String orgId;
	private String orgName;
	private SimpleOrg parent;
	private String parentOrgNo;
	private List<SimpleOrg> children=new ArrayList<SimpleOrg>();
	private int level;
	private int curLevel;//当前登录人员所属机构的级别
	public SimpleOrg(String orgId,String orgName,int level){
		this.orgId=orgId;
		this.orgName=orgName;
		this.level=level;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public SimpleOrg getParent() {
		return parent;
	}
	public void setParent(SimpleOrg parent) {
		this.parent = parent;
	}
	public List<SimpleOrg> getChildren() {
		return children;
	}
	public void setChildren(List<SimpleOrg> children) {
		this.children = children;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void addChild(SimpleOrg org){
		children.add(org);
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getCurLevel() {
		return curLevel;
	}
	public void setCurLevel(int curLevel) {
		this.curLevel = curLevel;
	}
	public String getParentOrgNo() {
		return parentOrgNo;
	}
	public void setParentOrgNo(String parentOrgNo) {
		this.parentOrgNo = parentOrgNo;
	}
	
}
