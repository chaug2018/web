package com.yzj.ebs.blackwhite.queryparam;

import java.io.Serializable;
/**
 * 批量导入excel模板的 bean
 * @author Administrator
 *
 */
public class ExcelImport implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//账号
	private String accNo;
	//账户类型
	private String accCycle;
	//验印模式
	private String sealMode;
	//发送方式
	private String sendMode;
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
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
