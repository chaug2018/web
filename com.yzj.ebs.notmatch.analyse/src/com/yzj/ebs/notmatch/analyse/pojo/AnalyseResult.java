package com.yzj.ebs.notmatch.analyse.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达情况统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class AnalyseResult {
	private String idCenter;	//对账中心
	private String idBank;	//网点
	private String bankName;
	private String docDate;	//对账日期
	private long sendCount;	//对账单发出数，按账单
	private long notMatchCount;	//未达数
	private long checkMatchCount;	//未达核对相符数
	private long checkNotMatchCount; //未达核对不符数
	private String notMatchPercent; //未达率
	
	private String tunePercent; //调平率
	
	public AnalyseResult(String idCenter, String idBank,
			String bankName, String docDate, long sendCount,
			long notMatchCount, long checkMatchCount, long checkNotMatchCount,
			String notMatchPercent,String tunePercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.docDate = docDate;
		this.sendCount = sendCount;
		this.notMatchCount = notMatchCount;
		this.checkMatchCount = checkMatchCount;
		this.checkNotMatchCount = checkNotMatchCount;
		this.notMatchPercent = notMatchPercent;
		this.tunePercent=tunePercent;
	}
	public String getTunePercent() {
		return tunePercent;
	}
	public void setTunePercent(String tunePercent) {
		this.tunePercent = tunePercent;
	}
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
	public long getNotMatchCount() {
		return notMatchCount;
	}
	public void setNotMatchCount(long notMatchCount) {
		this.notMatchCount = notMatchCount;
	}
	public long getCheckMatchCount() {
		return checkMatchCount;
	}
	public void setCheckMatchCount(long checkMatchCount) {
		this.checkMatchCount = checkMatchCount;
	}
	public long getCheckNotMatchCount() {
		return checkNotMatchCount;
	}
	public void setCheckNotMatchCount(long checkNotMatchCount) {
		this.checkNotMatchCount = checkNotMatchCount;
	}
	public String getNotMatchPercent() {
		return notMatchPercent;
	}
	public void setNotMatchPercent(String notMatchPercent) {
		this.notMatchPercent = notMatchPercent;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
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
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	
}
