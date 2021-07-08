package com.yzj.ebs.report.pojo;

/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况账户明细统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class SealAccDetailResult {

	private String accno;		//账号
	private String accname; 	//户名
	private String idCenter;	//对账中心
	private String idBranch;	//支行
	private String idBank;		//网点
	private String bankName;
	
	private long autoCount;		//自动验印通过数	
	private long manuCount;		//手动验印通过数
	private long notPassCount;	//未通过数
	private long proveTotal;	//验印总数
	private long notProve;  	//未验印数
	private long sendCount;		//账户数
	
	private String autoPercent;	//自动验印成功率
	private String manuPercent;	//手动验印成功率
	
	public SealAccDetailResult(String accno, String accname, String idCenter,
			String idBranch, String idBank, String bankName, long autoCount,
			long manuCount, long notPassCount, long proveTotal, long notProve,
			long sendCount, String autoPercent, String manuPercent) {
		super();
		this.accno = accno;
		this.accname = accname;
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		this.bankName = bankName;
		this.autoCount = autoCount;
		this.manuCount = manuCount;
		this.notPassCount = notPassCount;
		this.proveTotal = proveTotal;
		this.notProve = notProve;
		this.sendCount = sendCount;
		this.autoPercent = autoPercent;
		this.manuPercent = manuPercent;
	}
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public long getAutoCount() {
		return autoCount;
	}
	public void setAutoCount(long autoCount) {
		this.autoCount = autoCount;
	}
	public long getManuCount() {
		return manuCount;
	}
	public void setManuCount(long manuCount) {
		this.manuCount = manuCount;
	}
	public long getNotPassCount() {
		return notPassCount;
	}
	public void setNotPassCount(long notPassCount) {
		this.notPassCount = notPassCount;
	}
	public long getProveTotal() {
		return proveTotal;
	}
	public void setProveTotal(long proveTotal) {
		this.proveTotal = proveTotal;
	}
	public long getNotProve() {
		return notProve;
	}
	public void setNotProve(long notProve) {
		this.notProve = notProve;
	}
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
	public String getAutoPercent() {
		return autoPercent;
	}
	public void setAutoPercent(String autoPercent) {
		this.autoPercent = autoPercent;
	}
	public String getManuPercent() {
		return manuPercent;
	}
	public void setManuPercent(String manuPercent) {
		this.manuPercent = manuPercent;
	}
	
}
