package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账集中情况统计结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class FocusResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	//SendMode 1:柜台 2:邮寄 3:网银 4:面对面   null:其他
	private long mailCount;		//邮寄对账账户数
	private long netCount; 		//网银对账账户数
	private long faceCount;		//面对面对账账户数
	private long counterCount;	//柜台对账账户数
	private long otherCount;	//其它对账账户数
	private long checkCount; 	//需对账账户数
	private String focusPercent;	//集中度
	
	
	public FocusResult(String idCenter, String idBank, String bankName,
			long mailCount, long netCount, long faceCount, long counterCount,
			long otherCount, long checkCount, String focusPercent) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.mailCount = mailCount;
		this.netCount = netCount;
		this.faceCount = faceCount;
		this.counterCount = counterCount;
		this.otherCount = otherCount;
		this.checkCount = checkCount;
		this.focusPercent = focusPercent;
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
	public long getMailCount() {
		return mailCount;
	}
	public void setMailCount(long mailCount) {
		this.mailCount = mailCount;
	}
	public long getNetCount() {
		return netCount;
	}
	public void setNetCount(long netCount) {
		this.netCount = netCount;
	}
	public long getFaceCount() {
		return faceCount;
	}
	public void setFaceCount(long faceCount) {
		this.faceCount = faceCount;
	}
	public long getCounterCount() {
		return counterCount;
	}
	public void setCounterCount(long counterCount) {
		this.counterCount = counterCount;
	}
	public long getOtherCount() {
		return otherCount;
	}
	public void setOtherCount(long otherCount) {
		this.otherCount = otherCount;
	}
	public long getCheckCount() {
		return checkCount;
	}
	public void setCheckCount(long checkCount) {
		this.checkCount = checkCount;
	}
	public String getFocusPercent() {
		return focusPercent;
	}
	public void setFocusPercent(String focusPercent) {
		this.focusPercent = focusPercent;
	}
	
	
}
