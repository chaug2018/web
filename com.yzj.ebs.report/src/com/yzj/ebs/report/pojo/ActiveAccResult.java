package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 活跃账户情况统计结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class ActiveAccResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	private String accno;
	private String accname;
	private long creditCount;	//发生额明细数
	
	
	public ActiveAccResult(String idCenter, String idBank, String bankName,
			String accno, String accname, long creditCount) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.accno = accno;
		this.accname = accname;
		this.creditCount = creditCount;
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
	public long getCreditCount() {
		return creditCount;
	}
	public void setCreditCount(long creditCount) {
		this.creditCount = creditCount;
	}

	
}
