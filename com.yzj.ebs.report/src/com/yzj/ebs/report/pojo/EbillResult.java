package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 机构对账成功率统计结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class EbillResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	private long sendCount;		//对账单发出数，按账单
	private long backCount; //回收数
	
	private long checkSuccessCount;	//余额相符数
	private long checkFailCount;	//余额不相符数
	private long notCheckCount;	//尚未核对数
	private long ebillSuccessCount;//对账成功数
	private String checkSuccessPercent;//对账成功率
	private String backPercent;	//回收率
	
	
	public EbillResult(String idCenter, String idBank, String bankName,
			long sendCount, long backCount,
			long checkSuccessCount, long checkFailCount,
			long notCheckCount, long ebillSuccessCount,
			String checkSuccessPercent, String backPercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.sendCount = sendCount;
		this.backCount = backCount;
		this.checkSuccessCount = checkSuccessCount;
		this.checkFailCount = checkFailCount;
		this.notCheckCount = notCheckCount;
		this.ebillSuccessCount = ebillSuccessCount;
		this.checkSuccessPercent = checkSuccessPercent;
		this.backPercent = backPercent;
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
	public long getSendCount() {
		return sendCount;
	}
	public void setSendCount(long sendCount) {
		this.sendCount = sendCount;
	}
	
	public long getBackCount() {
		return backCount;
	}
	public void setBackCount(long backCount) {
		this.backCount = backCount;
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
	public long getNotCheckCount() {
		return notCheckCount;
	}
	public void setNotCheckCount(long notCheckCount) {
		this.notCheckCount = notCheckCount;
	}
	public long getEbillSuccessCount() {
		return ebillSuccessCount;
	}
	public void setEbillSuccessCount(long ebillSuccessCount) {
		this.ebillSuccessCount = ebillSuccessCount;
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
	
}
