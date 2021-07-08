package com.yzj.ebs.urgestatistics.param;

import java.io.Serializable;

public class UrgeStatisticsResultParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5121004774114302813L;
	private long totalVouAmount; // 账单总数
	private long totalUrgeAmount; // 退信总数
	private long rejectedAmount;// 单位拒收
	private long addrChangedAmount;// 原址拆迁
	private long addrUnknownAmount;// 地址不详
	private long noRecieverAmount;// 投递无人
	private long unitNotExistAmount;// 无此单位
	private long addrNotExistAmount;// 无此地址
	private long noConnectionAmount;// 无法联系
	private long otherAmount;// 其他
	private String urgeRate;// 退信率
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String bankName;
	private String docDate;

	public UrgeStatisticsResultParam(String idCenter, String idBranch, String idBank,
			String bankName, String docDate, long totalVouAmount,
			long totalUrgeAmount, long rejectedAmount, long addrChangedAmount,long addrUnknownAmount,
			long noRecieverAmount,long unitNotExistAmount,long addrNotExistAmount,long noConnectionAmount,
			long otherAmount,String urgeRate) {
		super();
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		this.bankName = bankName;
		this.docDate = docDate;
		this.totalVouAmount = totalVouAmount;
		this.totalUrgeAmount = totalUrgeAmount;
		this.rejectedAmount = rejectedAmount;
		this.addrChangedAmount = addrChangedAmount;
		this.addrUnknownAmount = addrUnknownAmount;
		this.noRecieverAmount = noRecieverAmount;
		this.unitNotExistAmount = unitNotExistAmount;
		this.addrNotExistAmount = addrNotExistAmount;
		this.noConnectionAmount = noConnectionAmount;
		this.otherAmount = otherAmount;
		this.urgeRate=urgeRate;
		
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

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public long getTotalVouAmount() {
		return totalVouAmount;
	}

	public void setTotalVouAmount(long totalVouAmount) {
		this.totalVouAmount = totalVouAmount;
	}

	public long getTotalUrgeAmount() {
		return totalUrgeAmount;
	}

	public void setTotalUrgeAmount(long totalUrgeAmount) {
		this.totalUrgeAmount = totalUrgeAmount;
	}

	public long getRejectedAmount() {
		return rejectedAmount;
	}

	public void setRejectedAmount(long rejectedAmount) {
		this.rejectedAmount = rejectedAmount;
	}

	public long getAddrChangedAmount() {
		return addrChangedAmount;
	}

	public void setAddrChangedAmount(long addrChangedAmount) {
		this.addrChangedAmount = addrChangedAmount;
	}

	public long getAddrUnknownAmount() {
		return addrUnknownAmount;
	}

	public void setAddrUnknownAmount(long addrUnknownAmount) {
		this.addrUnknownAmount = addrUnknownAmount;
	}

	public long getNoRecieverAmount() {
		return noRecieverAmount;
	}

	public void setNoRecieverAmount(long noRecieverAmount) {
		this.noRecieverAmount = noRecieverAmount;
	}

	public long getUnitNotExistAmount() {
		return unitNotExistAmount;
	}

	public void setUnitNotExistAmount(long unitNotExistAmount) {
		this.unitNotExistAmount = unitNotExistAmount;
	}

	public long getAddrNotExistAmount() {
		return addrNotExistAmount;
	}

	public void setAddrNotExistAmount(long addrNotExistAmount) {
		this.addrNotExistAmount = addrNotExistAmount;
	}

	public long getNoConnectionAmount() {
		return noConnectionAmount;
	}

	public void setNoConnectionAmount(long noConnectionAmount) {
		this.noConnectionAmount = noConnectionAmount;
	}

	public long getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(long otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getUrgeRate() {
		return urgeRate;
	}

	public void setUrgeRate(String urgeRate) {
		this.urgeRate = urgeRate;
	}
}
