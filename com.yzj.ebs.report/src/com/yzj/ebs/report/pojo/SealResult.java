package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class SealResult {
	private String idCenter;	//对账中心
	private String idBranch;	//支行
	private String idBank;		//网点
	private String bankName;

	private long autoCount;		//自动验印通过数	
	private long manuCount;		//手动验印通过数
	private long notPassCount;	//未通过数
	
//	private long proveMatchCount;		//验印相符数	
//	private long proveNotMatchCount;	//验印不符数
	
	private long sendCount;		//账户数=验印总数+未验印数
	private long proveTotal;	//验印总数=自动验印通过数+手动验印通过数+验印不符数
	private long notProve;  	//未验印数
	
	private String provePercent;	//验印通过率=自动验印通过率+手动验印通过率
	private String autoPercent;		//自动验印通过率
//	private String manuPercent;		//手动验印通过率
	public SealResult(String idCenter, String idBranch, String idBank,
			String bankName, long autoCount, long manuCount, long notPassCount,
			long sendCount, long proveTotal, long notProve, String provePercent,
			String autoPercent) {
		super();
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		this.bankName = bankName;
		this.autoCount = autoCount;
		this.manuCount = manuCount;
		this.notPassCount = notPassCount;
		this.sendCount = sendCount;
		this.proveTotal = proveTotal;
		this.notProve = notProve;
		this.provePercent = provePercent;
		this.autoPercent = autoPercent;
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
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
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
	public String getProvePercent() {
		return provePercent;
	}
	public void setProvePercent(String provePercent) {
		this.provePercent = provePercent;
	}
	public String getAutoPercent() {
		return autoPercent;
	}
	public void setAutoPercent(String autoPercent) {
		this.autoPercent = autoPercent;
	}
	

}
