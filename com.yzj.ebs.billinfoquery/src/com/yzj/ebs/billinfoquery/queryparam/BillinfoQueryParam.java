package com.yzj.ebs.billinfoquery.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2012-10-2
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司
 *  账户查询查询参数
 * @author SWL
 * @version 1.0.0
 */
public class BillinfoQueryParam extends PageParam implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1139689747987590173L;

	/** 对账日期 */
	private String docDate;

	/** 对账单编号 */
	private String voucherNo;

	/** 账户类型 */
	private String accType;

	/** 支行 */
	private String idBank;
	
	/** 清算中心 */
	private String idCenter;

	/** 验印状态 */
	private String proveFlag;

	/** 账号 */
	private String accNo;

	/** 余额状态 */
	private String checkFlag;
	/** 余额状态 */
	private String finalCheckFlag;

	/** 账户名称 */
	private String accName;

	/** 查询方式 */
	private String queryType;
	
	private String sendMode;

	private String checkResult;
	
	private String faceFlag;
	
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

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	
	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getProveFlag() {
		return proveFlag;
	}

	public void setProveFlag(String proveFlag) {
		this.proveFlag = proveFlag;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getSendMode() {
		return sendMode;
	}

	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}

	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public String getFaceFlag() {
		return faceFlag;
	}

	public void setFaceFlag(String faceFlag) {
		this.faceFlag = faceFlag;
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

	public String getFinalCheckFlag() {
		return finalCheckFlag;
	}

	public void setFinalCheckFlag(String finalCheckFlag) {
		this.finalCheckFlag = finalCheckFlag;
	}
	
}