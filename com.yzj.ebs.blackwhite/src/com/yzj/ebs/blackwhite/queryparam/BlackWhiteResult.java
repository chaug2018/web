package com.yzj.ebs.blackwhite.queryparam;

import java.io.Serializable;
/**
 * 
 *创建于:2012-10-18<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 黑白名单查询参数类
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class BlackWhiteResult  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4649649419508964022L;
	//分行
	private String idCenter;
	// 机构名称
	private String bankName;
	// 机构号
	private String idBank;
	// 不对账数
	private Integer blackCount;
	// 必对账数
	private Integer whiteCount;
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public Integer getBlackCount() {
		return blackCount;
	}
	public void setBlackCount(Integer blackCount) {
		this.blackCount = blackCount;
	}

	public Integer getWhiteCount() {
		return whiteCount;
	}
	public void setWhiteCount(Integer whiteCount) {
		this.whiteCount = whiteCount;
	}

	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
