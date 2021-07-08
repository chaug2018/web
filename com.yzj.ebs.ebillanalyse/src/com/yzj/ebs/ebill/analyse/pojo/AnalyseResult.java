package com.yzj.ebs.ebill.analyse.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class AnalyseResult {
	private String idCenter; //分行
	private String docDate;	//对账日期
	private String idBank;	//机构号
	private String bankName; //机构名称
	private long sendCount;		//对账单发出数，按账单
	private long retreatCount;	//退信数
	private long backCount; //回收数
	private long proveMatchCount;	//验印相符数	
	private long proveNotMatchCount;		//验印不符数
	private long wjkCount;	//未建库数
	private long checkSuccessCount;	//余额相符数
	private long checkFailCount;	//余额不相符数
	private long ebillSuccessCount;//对账成功数
	private String checkSuccessPercent;//对账成功率
	private String backPercent;	//回收率
	private String proveMatchPercent;		//验印成功率
	private String proveNotMatchPercent;	//验印不符率
	
	public AnalyseResult(String idCenter,String docDate, String idBank, String bankName,
			long sendCount, long retreatCount, long backCount,
			long proveMatchCount, long proveNotMatchCount, long wjkCount,
			long checkSuccessCount, long checkFailCount,long ebillSuccessCount,
			String checkSuccessPercent, String backPercent,
			String proveMatchPercent, String proveNotMatchPercent) {
		super();
		this.idCenter=idCenter;
		this.docDate = docDate;
		this.idBank = idBank;
		this.bankName = bankName;
		this.sendCount = sendCount;
		this.retreatCount = retreatCount;
		this.backCount = backCount;
		this.proveMatchCount = proveMatchCount;
		this.proveNotMatchCount = proveNotMatchCount;
		this.wjkCount = wjkCount;
		this.checkSuccessCount = checkSuccessCount;
		this.checkFailCount = checkFailCount;
		this.ebillSuccessCount = ebillSuccessCount;
		this.checkSuccessPercent = checkSuccessPercent;
		this.backPercent = backPercent;
		this.proveMatchPercent = proveMatchPercent;
		this.proveNotMatchPercent = proveNotMatchPercent;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
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
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
	public long getRetreatCount() {
		return retreatCount;
	}
	public void setRetreatCount(long retreatCount) {
		this.retreatCount = retreatCount;
	}
	public long getBackCount() {
		return backCount;
	}
	public void setBackCount(long backCount) {
		this.backCount = backCount;
	}
	public long getProveMatchCount() {
		return proveMatchCount;
	}
	public void setProveMatchCount(long proveMatchCount) {
		this.proveMatchCount = proveMatchCount;
	}
	public long getProveNotMatchCount() {
		return proveNotMatchCount;
	}
	public void setProveNotMatchCount(long proveNotMatchCount) {
		this.proveNotMatchCount = proveNotMatchCount;
	}
	public long getWjkCount() {
		return wjkCount;
	}
	public void setWjkCount(long wjkCount) {
		this.wjkCount = wjkCount;
	}
	public long getCheckSuccessCount() {
		return checkSuccessCount;
	}
	public void setCheckSuccessCount(long checkSuccessCount) {
		this.checkSuccessCount = checkSuccessCount;
	}
	public long getCheckFailCount() {
		return checkFailCount;
	}
	public void setCheckFailCount(long checkFailCount) {
		this.checkFailCount = checkFailCount;
	}
	public String getCheckSuccessPercent() {
		return checkSuccessPercent;
	}
	public void setCheckSuccessPercent(String checkSuccessPercent) {
		this.checkSuccessPercent = checkSuccessPercent;
	}
	public String getBackPercent() {
		return backPercent;
	}
	public void setBackPercent(String backPercent) {
		this.backPercent = backPercent;
	}
	public String getProveMatchPercent() {
		return proveMatchPercent;
	}
	public void setProveMatchPercent(String proveMatchPercent) {
		this.proveMatchPercent = proveMatchPercent;
	}
	public String getProveNotMatchPercent() {
		return proveNotMatchPercent;
	}
	public void setProveNotMatchPercent(String proveNotMatchPercent) {
		this.proveNotMatchPercent = proveNotMatchPercent;
	}
	public long getEbillSuccessCount() {
		return ebillSuccessCount;
	}
	public void setEbillSuccessCount(long ebillSuccessCount) {
		this.ebillSuccessCount = ebillSuccessCount;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	

}
