/**
 * ResultBean.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:donggenlong 2012-8-23
 */
package com.yzj.ebs.auto;

import java.io.Serializable;

/**
 * 返回结果集
 * 
 * @author Administrator
 * @version 1.0.0
 */
public class ResultBean implements Serializable {

	private static final long serialVersionUID = -166502619097412599L;
	private long docId;
	private String voucherNo;
	private String branchNo;
	private String credit;

	public ResultBean(long docId, String voucherNo, String branchNo, String credit) {
		super();
		this.docId = docId;
		this.voucherNo = voucherNo;
		this.branchNo = branchNo;
		this.credit = credit;
	}

	public long getDocId() {
		return docId;
	}

	public void setDocId(long docId) {
		this.docId = docId;
	}



	/**
	 * @return the voucherNo
	 */
	public String getVoucherNo() {
		return voucherNo;
	}

	/**
	 * @param voucherNo the voucherNo to set
	 */
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getBranchNo() {
		return branchNo;
	}

	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}
}
