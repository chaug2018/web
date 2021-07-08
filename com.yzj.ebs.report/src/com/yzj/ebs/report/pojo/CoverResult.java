package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 覆盖率统计结果类，保存统计数据
 * 一段时间内需对账账户对账覆盖情况，只要一段时间内有过成功对账即有覆盖
 * @author chenzg
 * @version 1.0.0
 */
public class CoverResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	private long sendCount;		//发出数，按账单
	private long successCount;	//有过成功对账数，即覆盖数
	private long failCount;		//都未成功对账数
	private long oneFailCount;	//一次未成功对账数
	private long twoFailCount;	//两次未成功对账数
	private long threeFailCount;	//三次未成功对账数
	private String coverPercent;	//覆盖率
	
	
	public CoverResult(String idCenter, String idBank, String bankName,
			long sendCount, long successCount, long failCount,
			long oneFailCount, long twoFailCount, long threeFailCount,
			String coverPercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.sendCount = sendCount;
		this.successCount = successCount;
		this.failCount = failCount;
		this.oneFailCount = oneFailCount;
		this.twoFailCount = twoFailCount;
		this.threeFailCount = threeFailCount;
		this.coverPercent = coverPercent;
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
	public long getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(long successCount) {
		this.successCount = successCount;
	}
	public long getFailCount() {
		return failCount;
	}
	public void setFailCount(long failCount) {
		this.failCount = failCount;
	}
	public long getOneFailCount() {
		return oneFailCount;
	}
	public void setOneFailCount(long oneFailCount) {
		this.oneFailCount = oneFailCount;
	}
	public long getTwoFailCount() {
		return twoFailCount;
	}
	public void setTwoFailCount(long twoFailCount) {
		this.twoFailCount = twoFailCount;
	}
	public long getThreeFailCount() {
		return threeFailCount;
	}
	public void setThreeFailCount(long threeFailCount) {
		this.threeFailCount = threeFailCount;
	}
	public String getCoverPercent() {
		return coverPercent;
	}
	public void setCoverPercent(String coverPercent) {
		this.coverPercent = coverPercent;
	}

}
