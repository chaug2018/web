package com.yzj.ebs.edata.action;

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
	private String docdate;


	/** 对账单编号 */
	private String voucherno;

	/** 账户类型 */
	private String acctype;

	/** 网点号 */
	private String idbank;

	/** 支行号 */
	private String idbranch;

	/** 对账中心号 */
	private String idcenter;

	/** 验印状态 */
	private String proveflag;

	/** 账号 */
	private String accno;

	/** 余额状态 */
	private String checkflag;

	/** 账户名称 */
	private String accname;

	/** 查询方式 */
	private String querytype;

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
	
	private String custId;
	
	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}


	public String getDocdate() {
		return docdate;
	}

	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

	public String getVoucherno() {
		return voucherno;
	}

	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public String getIdbank() {
		return idbank;
	}

	public void setIdbank(String idbank) {
		this.idbank = idbank;
	}

	public String getIdbranch() {
		return idbranch;
	}

	public void setIdbranch(String idbranch) {
		this.idbranch = idbranch;
	}

	public String getIdcenter() {
		return idcenter;
	}

	public void setIdcenter(String idcenter) {
		this.idcenter = idcenter;
	}

	public String getProveflag() {
		return proveflag;
	}

	public void setProveflag(String proveflag) {
		this.proveflag = proveflag;
	}

	public String getAccno() {
		return accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
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