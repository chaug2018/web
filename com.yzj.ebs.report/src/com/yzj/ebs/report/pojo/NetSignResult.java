package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 网银对账签约率统计结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class NetSignResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	private long accCount;		//总账户数
	private long netCount; 		//网银开户数
	private long netSignCount;		//网银对账签约数=自助签约数+柜面签约数
	private long autoSignCount;		//自助签约数
	private long counterSignCount;	//柜面签约数
	private String netSignPercent;	//网银签约率
	
	
	public NetSignResult(String idCenter, String idBank, String bankName,
			long accCount, long netCount, long netSignCount,
			long autoSignCount, long counterSignCount, String netSignPercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.accCount = accCount;
		this.netCount = netCount;
		this.netSignCount = netSignCount;
		this.autoSignCount = autoSignCount;
		this.counterSignCount = counterSignCount;
		this.netSignPercent = netSignPercent;
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
	public long getAccCount() {
		return accCount;
	}
	public void setAccCount(long accCount) {
		this.accCount = accCount;
	}
	public long getNetCount() {
		return netCount;
	}
	public void setNetCount(long netCount) {
		this.netCount = netCount;
	}
	public long getNetSignCount() {
		return netSignCount;
	}
	public void setNetSignCount(long netSignCount) {
		this.netSignCount = netSignCount;
	}
	public long getAutoSignCount() {
		return autoSignCount;
	}
	public void setAutoSignCount(long autoSignCount) {
		this.autoSignCount = autoSignCount;
	}
	public long getCounterSignCount() {
		return counterSignCount;
	}
	public void setCounterSignCount(long counterSignCount) {
		this.counterSignCount = counterSignCount;
	}
	public String getNetSignPercent() {
		return netSignPercent;
	}
	public void setNetSignPercent(String netSignPercent) {
		this.netSignPercent = netSignPercent;
	}
	
	
}
