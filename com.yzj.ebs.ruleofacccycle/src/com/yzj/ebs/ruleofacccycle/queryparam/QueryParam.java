package com.yzj.ebs.ruleofacccycle.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

public class QueryParam extends PageParam implements Serializable {

	/** 账户类型 */
	private String accCycle;
	
	/** 科目号 */
	private String subjectNo;
	
	/** 账户状态 */
	private String accnoState;
	
	/** 当前页第一条记录 */
	private int firstResult;

	/** 每页显示结果条数 ,默认为每页显示10条记录 */
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
	

	public String getAccCycle() {
		return accCycle;
	}

	public void setAccCycle(String accCycle) {
		this.accCycle = accCycle;
	}

	public String getAccnoState() {
		return accnoState;
	}

	public void setAccnoState(String accnoState) {
		this.accnoState = accnoState;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
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

	public int getLastResult() {
		return lastResult;
	}

	public void setLastResult(int lastResult) {
		this.lastResult = lastResult;
	}
	
	
}
