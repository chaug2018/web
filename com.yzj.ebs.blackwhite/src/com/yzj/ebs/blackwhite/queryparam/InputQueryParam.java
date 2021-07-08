package com.yzj.ebs.blackwhite.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 特殊账户过滤表查询bean
 * @author j_sun
 *
 */
public class InputQueryParam extends PageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 账号
	private String accno;
	//日期
	private String date;
	//是否以导入
	private String flag;
	//账户类型
	private String accCycle;
	//验印模式
	private String sealMode;
	//发送方式
	private String sendMode;
	
	/*set get*/
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getAccCycle() {
		return accCycle;
	}
	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}
	public String getSealMode() {
		return sealMode;
	}
	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	
}
