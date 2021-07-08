package com.yzj.ebs.retreatinput.pojo;
/**
 * 
 *创建于:2012-11-9<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 导入失败信息类
 * @author 施江敏
 * @version 1.0.0
 */
public class ImportData {
	private String voucherNo;
	private String failReason;
	
	public ImportData(String voucherNo,String failReason){
		this.voucherNo = voucherNo;
		this.failReason = failReason;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
}
