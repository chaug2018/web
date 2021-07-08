package com.yzj.ebs.partebill.param;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/*
 * 增量对账情况统计
 * @param
 */

public class QueryParam extends PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -832065903559041569L;
	
	//统计起期
	private String docDateStart;
	//统计止期
	private String docDateEnd;
	//对账中心
	private String idCenter;
	//对账支行
	private String idBranch;
	//机构
	private String idBank;
	
	
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

	

	public String getDocDateStart() {
		return docDateStart;
	}

	public void setDocDateStart(String docDateStart) {
		this.docDateStart = docDateStart;
	}

	public String getDocDateEnd() {
		return docDateEnd;
	}

	public void setDocDateEnd(String docDateEnd) {
		this.docDateEnd = docDateEnd;
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

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
