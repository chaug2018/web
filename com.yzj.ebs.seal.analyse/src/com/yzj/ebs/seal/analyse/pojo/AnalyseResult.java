package com.yzj.ebs.seal.analyse.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印情况统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class AnalyseResult {
	private String idCenter;	//对账中心
	private String idBranch;	//支行
	private String idBank;	//网点
	private String bankName;
	private String docDate;	//对账日期
	private long sendCount;		//对账单发出数，按账单
	private long proveMatchCount;	//验印相符数	
	private long proveNotMatchCount;		//验印不符数
	private String provePercent;		//验印成功率
	private long notProve;
	

	public AnalyseResult(String idCenter, String idBranch, String idBank,
			String bankName, String docDate, long sendCount,
			long proveMatchCount, long proveNotMatchCount, String provePercent,long notProve) {
		super();
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		this.bankName = bankName;
		this.docDate = docDate;
		this.sendCount = sendCount;
		this.proveMatchCount = proveMatchCount;
		this.proveNotMatchCount = proveNotMatchCount;
		this.provePercent = provePercent;
		this.notProve = notProve;
	}
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
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
	public String getProvePercent() {
		return provePercent;
	}
	public void setProvePercent(String provePercent) {
		this.provePercent = provePercent;
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
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public long getNotProve() {
		return notProve;
	}
	public void setNotProve(long notProve) {
		this.notProve = notProve;
	}
}
