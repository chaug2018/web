package com.yzj.ebs.rushstatistics.param;

import java.io.Serializable;
/**
 * 催收统计结果集
 * @author swl
 *
 */
public class RushStatisticsResultParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850451902232143818L;

	private long totalAmount; // 对账单总数
	private long rushedAmount; // 已催收账单数
	private long successAmount; // 催收成功数
	private long telAmount; // 0.电话催收数
	private long emailAmount; // 1.邮件催收数
	private long faceAmount; // 2.面对面催收数
	private String successRate; // 催收成功率
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String bankName;
	private String docDate;
	
	public RushStatisticsResultParam(String idCenter,String idBranch,String idBank ,String bankName,String docDate,
			long totalAmount,long rushedAmount,long successAmount,long telAmount,long emailAmount,
			long faceAmount,String successRate){
		super();
		this.idCenter=idCenter;
		this.idBranch=idBranch;
		this.idBank=idBank;
		this.bankName=bankName;
		this.docDate=docDate;
		this.totalAmount=totalAmount;
		this.rushedAmount=rushedAmount;
		this.successAmount=successAmount;
		this.telAmount=telAmount;
		this.emailAmount=emailAmount;
		this.faceAmount=faceAmount;
		this.successRate=successRate;
		
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

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getRushedAmount() {
		return rushedAmount;
	}

	public void setRushedAmount(long rushedAmount) {
		this.rushedAmount = rushedAmount;
	}

	public long getSuccessAmount() {
		return successAmount;
	}

	public void setSuccessAmount(long successAmount) {
		this.successAmount = successAmount;
	}

	public long getTelAmount() {
		return telAmount;
	}

	public void setTelAmount(long telAmount) {
		this.telAmount = telAmount;
	}

	public long getEmailAmount() {
		return emailAmount;
	}

	public void setEmailAmount(long emailAmount) {
		this.emailAmount = emailAmount;
	}

	public long getFaceAmount() {
		return faceAmount;
	}

	public void setFaceAmount(long faceAmount) {
		this.faceAmount = faceAmount;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

}
