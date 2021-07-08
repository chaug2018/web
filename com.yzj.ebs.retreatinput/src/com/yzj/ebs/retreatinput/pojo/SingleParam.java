package com.yzj.ebs.retreatinput.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2013-04-03<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 单笔录入 bean
 * @author 单伟龙
 * @version 1.0.0
 */
public class SingleParam extends PageParam implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//逐笔登记相关
	private String urgeType;
	private String urgeNote;
	private String voucherNo;
	private Integer urgeTimes;
	
	public String getUrgeType() {
		return urgeType;
	}
	public void setUrgeType(String urgeType) {
		this.urgeType = urgeType;
	}
	public String getUrgeNote() {
		return urgeNote;
	}
	public void setUrgeNote(String urgeNote) {
		this.urgeNote = urgeNote;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Integer getUrgeTimes() {
		return urgeTimes;
	}
	public void setUrgeTimes(Integer urgeTimes) {
		this.urgeTimes = urgeTimes;
	}
	
}
