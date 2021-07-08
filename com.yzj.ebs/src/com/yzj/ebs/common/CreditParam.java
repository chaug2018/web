package com.yzj.ebs.common;

/**
 *创建于:2012-10-9<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 各种额度信息实体类
 * @author 陈林江
 * @version 1.0
 */
public class CreditParam {
	
	private long manualInput;
	private long manualAuth;
	private long manualProve_fst;
	private long manualProve_snd;
	private long manualProve_auth;
	public long getManualInput() {
		return manualInput;
	}
	public void setManualInput(long manualInput) {
		this.manualInput = manualInput;
	}
	public long getManualAuth() {
		return manualAuth;
	}
	public void setManualAuth(long manualAuth) {
		this.manualAuth = manualAuth;
	}
	public long getManualProve_fst() {
		return manualProve_fst;
	}
	public void setManualProve_fst(long manualProve_fst) {
		this.manualProve_fst = manualProve_fst;
	}
	public long getManualProve_snd() {
		return manualProve_snd;
	}
	public void setManualProve_snd(long manualProve_snd) {
		this.manualProve_snd = manualProve_snd;
	}
	public long getManualProve_auth() {
		return manualProve_auth;
	}
	public void setManualProve_auth(long manualProve_auth) {
		this.manualProve_auth = manualProve_auth;
	}
 
}
