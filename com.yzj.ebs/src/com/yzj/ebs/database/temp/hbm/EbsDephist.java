package com.yzj.ebs.database.temp.hbm;

import java.math.BigDecimal;

/**
 * EbsTempaccnodetaildataId entity. @author MyEclipse Persistence Tools
 */

public class EbsDephist implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long autoid;
	private String txdate;
	private String traceno;
	private String accno;
	private String accson;
	private Double ioind;
	private String tranamt;
	private String bal;
	private String currtype;
	private String toaccno;
	private String toaccname;
	private String abs;
	private Double docnum;
	private String dataDate;
	private String importdate;
	private String traceNoSon;
	private long pageNum1;
	private long pageNum2;
	private String unusedDate;
	private String unusedTime;
	private String traceTime;
	
	public Long getAutoid() {
		return autoid;
	}
	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}
	public String getTxdate() {
		return txdate;
	}
	public void setTxdate(String txdate) {
		this.txdate = txdate;
	}
	public String getTraceno() {
		return traceno;
	}
	public void setTraceno(String traceno) {
		this.traceno = traceno;
	}
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public String getAccson() {
		return accson;
	}
	public void setAccson(String accson) {
		this.accson = accson;
	}
	public Double getIoind() {
		return ioind;
	}
	public void setIoind(Double ioind) {
		this.ioind = ioind;
	}
	public String getTranamt() {
		return tranamt;
	}
	public void setTranamt(String tranamt) {
		this.tranamt = tranamt;
	}
	public String getBal() {
		return bal;
	}
	public void setBal(String bal) {
		this.bal = bal;
	}
	public String getCurrtype() {
		return currtype;
	}
	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}
	public String getToaccno() {
		return toaccno;
	}
	public void setToaccno(String toaccno) {
		this.toaccno = toaccno;
	}
	public String getToaccname() {
		return toaccname;
	}
	public void setToaccname(String toaccname) {
		this.toaccname = toaccname;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public Double getDocnum() {
		return docnum;
	}
	public void setDocnum(Double docnum) {
		this.docnum = docnum;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public String getImportdate() {
		return importdate;
	}
	public void setImportdate(String importdate) {
		this.importdate = importdate;
	}
	public String getTraceNoSon() {
		return traceNoSon;
	}
	public void setTraceNoSon(String traceNoSon) {
		this.traceNoSon = traceNoSon;
	}
	public long getPageNum1() {
		return pageNum1;
	}
	public void setPageNum1(long pageNum1) {
		this.pageNum1 = pageNum1;
	}
	public long getPageNum2() {
		return pageNum2;
	}
	public void setPageNum2(long pageNum2) {
		this.pageNum2 = pageNum2;
	}
	public String getUnusedDate() {
		return unusedDate;
	}
	public void setUnusedDate(String unusedDate) {
		this.unusedDate = unusedDate;
	}
	public String getUnusedTime() {
		return unusedTime;
	}
	public void setUnusedTime(String unusedTime) {
		this.unusedTime = unusedTime;
	}
	public String getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}

}