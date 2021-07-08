package com.yzj.ebs.insideaccnoparam.pojo;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;


public class QueryParam extends PageParam implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**账户号*/
	private String accno;
	
	/**操作员用户号*/
	private String custId;
	
	/**复核员用户号*/
	private String recheckCustId;
	
	/**截止交易日期（开始日期为截止日期的yyyymm01）*/
	private String traceDate;
	

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getRecheckCustId() {
		return recheckCustId;
	}

	public void setRecheckCustId(String recheckCustId) {
		this.recheckCustId = recheckCustId;
	}

	public String getTraceDate() {
		return traceDate;
	}

	public void setTraceDate(String traceDate) {
		this.traceDate = traceDate;
	}

}
