package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 网银对账率统计结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class NetCheckResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	
	private long netSignCount;		//网银对账签约数
	private long netCheckSuccessCount; 	//网银对账成功数
	private String netCheckSuccessPercent;	//网银对账率
	
	
	public NetCheckResult(String idCenter, String idBank, String bankName,
			long netSignCount, long netCheckSuccessCount,
			String netCheckSuccessPercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.netSignCount = netSignCount;
		this.netCheckSuccessCount = netCheckSuccessCount;
		this.netCheckSuccessPercent = netCheckSuccessPercent;
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
	public long getNetSignCount() {
		return netSignCount;
	}
	public void setNetSignCount(long netSignCount) {
		this.netSignCount = netSignCount;
	}
	public long getNetCheckSuccessCount() {
		return netCheckSuccessCount;
	}
	public void setNetCheckSuccessCount(long netCheckSuccessCount) {
		this.netCheckSuccessCount = netCheckSuccessCount;
	}
	public String getNetCheckSuccessPercent() {
		return netCheckSuccessPercent;
	}
	public void setNetCheckSuccessPercent(String netCheckSuccessPercent) {
		this.netCheckSuccessPercent = netCheckSuccessPercent;
	}
	
	
	
}
