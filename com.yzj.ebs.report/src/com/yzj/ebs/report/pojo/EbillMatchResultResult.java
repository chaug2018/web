package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账户有效对账结果展示结果类，保存统计数据
 * @author chenzg
 * @version 1.0.0
 */
public class EbillMatchResultResult implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String idCenter; //分行
	private String idBank;// 机构号
	private String bankName;// 机构名称
	private String accNo;// 账号
	private String finalcheckflag;//对账结果
	private String proveflag;//验印结果
	private String succflag;//成功与否:勾兑结果为相符+验印结果为正常+流程结束
	private String docdate;
	
	
	public EbillMatchResultResult(String idCenter, String idBank,
			String bankName, String accNo, String finalcheckflag,
			String proveflag, String succflag, String docdate) {
		super();
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.accNo = accNo;
		this.finalcheckflag = finalcheckflag;
		this.proveflag = proveflag;
		this.succflag = succflag;
		this.docdate = docdate;
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
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getFinalcheckflag() {
		return finalcheckflag;
	}
	public void setFinalcheckflag(String finalcheckflag) {
		this.finalcheckflag = finalcheckflag;
	}
	public String getProveflag() {
		return proveflag;
	}
	public void setProveflag(String proveflag) {
		this.proveflag = proveflag;
	}
	public String getSuccflag() {
		return succflag;
	}
	public void setSuccflag(String succflag) {
		this.succflag = succflag;
	}
	public String getDocdate() {
		return docdate;
	}
	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

}
