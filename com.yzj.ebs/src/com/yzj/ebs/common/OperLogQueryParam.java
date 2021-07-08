package com.yzj.ebs.common;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2013-1-14
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 操作日志查询参数类
 * @author chender
 * @version 1.0.0
 */
public class OperLogQueryParam extends PageParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8857060125871447972L;

	/** 开始时间 */
	private String startTime;

	/** 结束时间 */
	private String endTime;
	/** 操作提示*/
	private String opDesc;

	/**
	 * 账单编号
	 */
	private String voucherNo;
	
	/**
	 * 客户账号
	 */
	private String accNo;
	
	/**
	 * 操作人员
	 */
	private String opCode;
	
	private String accName;
	
	/**
	 * 操作模块
	 */
	private String operLogModule;

	/** 网点号 */
	private String idBank;
	/** 网点号1 */
	private String idBank1;
	/** 对账中心 */
	private String idCenter;

	/** 支行号 */
	private String idBranch;
	// 为做分页的
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

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getIdBank1() {
		return idBank1;
	}

	public void setIdBank1(String idBank1) {
		this.idBank1 = idBank1;
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

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getOperLogModule() {
		return operLogModule;
	}

	public void setOperLogModule(String operLogModule) {
		this.operLogModule = operLogModule;
	}

	public String getOpDesc() {
		return opDesc;
	}

	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}
}