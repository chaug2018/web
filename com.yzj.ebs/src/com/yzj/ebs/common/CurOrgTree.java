package com.yzj.ebs.common;

import java.io.Serializable;
import java.util.List;

/**
 *创建于:2012-10-23<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 当前登录人员可以访问的机构树信息
 * @author 陈林江
 * @version 1.0
 */
public class CurOrgTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8423235964464322235L;
	private List<SimpleOrg> idCenters;
	private List<SimpleOrg> idBranches;
	private List<SimpleOrg> idBanks;
	private int curLevel;
	public List<SimpleOrg> getIdCenters() {
		return idCenters;
	}
	public void setIdCenters(List<SimpleOrg> idCenters) {
		this.idCenters = idCenters;
	}
	public List<SimpleOrg> getIdBranches() {
		return idBranches;
	}
	public void setIdBranches(List<SimpleOrg> idBranches) {
		this.idBranches = idBranches;
	}
	public List<SimpleOrg> getIdBanks() {
		return idBanks;
	}
	public void setIdBanks(List<SimpleOrg> idBanks) {
		this.idBanks = idBanks;
	}
	public int getCurLevel() {
		return curLevel;
	}
	public void setCurLevel(int curLevel) {
		this.curLevel = curLevel;
	}

}
