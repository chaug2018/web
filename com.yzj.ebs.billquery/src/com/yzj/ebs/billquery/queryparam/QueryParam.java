package com.yzj.ebs.billquery.queryparam;

import java.io.Serializable;

/** 票据查询相关辅助查询参数 */
public class QueryParam implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -173937306644538607L;
	/** 开始日期 */
	private String idCenter;
	/** 开始日期 */
	private String idBranch;
	
	/** 开始日期 */
	private String startDate;
	
	/** 接收日期 */
	private String endDate;
	
	/** 对账日期 */
	private String checkDate;
	
	/** 业务流水 */
	private String docId;
	
	/** 账单编号 */
	private String voucherNo;
	
	/** 账号 */
	private String accountNo;
	
	/** 网点号 */
	private String idBank;
	
	/** 票据状态 */
	private String docFlag;
	
	/** 账户类型 */
	private String accType;
	
	/** 当前页第一条记录 */
	private int firstResult;
	
	/** 每页显示结果条数 ,默认为每页显示10条记录*/
	private int pageSize = 10;

	/** 总记录数 */
	private int total = 0;
	
	/** 当前页 */
	private int curPage = 1;
	
	/** 上一页 */
	private int lastPage;
	
	/** 下一页 */
	private int nextPage;
	
	/** 总页数 */
	private int totalPage = 1;
		
	/** 当前页最后一条记录 */
	private int lastResult = 0;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}


	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}


	public String getDocFlag() {
		return docFlag;
	}

	public void setDocFlag(String docFlag) {
		this.docFlag = docFlag;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}	

	public int getFirstResult() {
		return firstResult;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getLastResult() {
		return lastResult;
	}

	public void setLastResult(int lastResult) {
		this.lastResult = lastResult;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

}