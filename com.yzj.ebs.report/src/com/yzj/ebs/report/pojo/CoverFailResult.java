package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 连续对账未成功账户明细(未覆盖的明细)结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class CoverFailResult {
	private String idCenter; //分行
	private String idBank;	//机构号
	private String bankName; //机构名称
	
	private String accno;//账号
	private String accname;//户名
	private String docState;//账单状态
	private String docdate;
	private String checkFlag;//对账结果
	private String sendmode;//发送方式
	private String accState;//账户状态
	
	private long notCheckCount;	//未对账次

	
	public CoverFailResult(String idCenter, String idBank, String bankName,
			String accno, String accname, String docState, String docdate, String checkFlag,
			String sendmode, String accState, long notCheckCount) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.accno = accno;
		this.accname = accname;
		this.docState = docState;
		this.docdate = docdate;
		this.checkFlag = checkFlag;
		this.sendmode = sendmode;
		this.accState = accState;
		this.notCheckCount = notCheckCount;
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

	public String getDocState() {
		return docState;
	}

	public void setDocState(String docState) {
		this.docState = docState;
	}

	public String getDocdate() {
		return docdate;
	}

	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getSendmode() {
		return sendmode;
	}

	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
	}

	public String getAccState() {
		return accState;
	}

	public void setAccState(String accState) {
		this.accState = accState;
	}

	public long getNotCheckCount() {
		return notCheckCount;
	}

	public void setNotCheckCount(long notCheckCount) {
		this.notCheckCount = notCheckCount;
	}

}
