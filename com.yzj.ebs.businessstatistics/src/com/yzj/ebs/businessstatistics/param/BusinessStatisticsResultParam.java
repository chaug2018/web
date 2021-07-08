package com.yzj.ebs.businessstatistics.param;

import java.io.Serializable;
/**
 * 催收统计结果集
 * @author swl
 *
 */
public class BusinessStatisticsResultParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3850451902232143818L;

	private long counterAccount; // 柜台对账账户数
	private long postAccount; //邮寄对账账户数
	private long netAcount; //网银对账账户数
	private long faceAcount; //面对面对账账户数
	private long otherAcount; //发送方式为空的对账账户数
	private long idCenterAcount; // 对账中心账户数
	private long shouldAcount; // 应对账账户数
	private String counterPercent; //柜台占应对账比例
	private String postPercent; //邮寄占应对账比例
	private String netPercent; //网银占应对账比例
	private String facePercent; //面对面占应对账比例
	private String otherPercent; //发送方式为空的应对账比例
	private String sPercent; //应对账账户数占对账中心账户数的比例
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String bankName;
	
	public BusinessStatisticsResultParam(long counterAccount, long postAccount,
			long idCenterAcount, long shouldAcount, String idCenter,
			String idBranch, String idBank, String bankName,long netAcount,
			long faceAcount,long otherAcount,String counterPercent,String postPercent,
			String netPercent,String facePercent,String otherPercent,String sPercent
			) {
		super();
		this.counterAccount = counterAccount;
		this.postAccount = postAccount;
		this.idCenterAcount = idCenterAcount;
		this.shouldAcount = shouldAcount;
		this.idCenter = idCenter;
		this.idBranch = idBranch;
		this.idBank = idBank;
		this.bankName = bankName;
		this.netAcount = netAcount;
		this.faceAcount = faceAcount;
		this.otherAcount = otherAcount;
		this.counterPercent = counterPercent;
		this.postPercent = postPercent;
		this.netPercent = netPercent;
		this.facePercent = facePercent;
		this.otherPercent = otherPercent;
		this.sPercent = sPercent;
		
	}

	public long getCounterAccount() {
		return counterAccount;
	}

	public void setCounterAccount(long counterAccount) {
		this.counterAccount = counterAccount;
	}

	public long getPostAccount() {
		return postAccount;
	}

	public void setPostAccount(long postAccount) {
		this.postAccount = postAccount;
	}

	public long getIdCenterAcount() {
		return idCenterAcount;
	}

	public void setIdCenterAcount(long idCenterAcount) {
		this.idCenterAcount = idCenterAcount;
	}

	public long getShouldAcount() {
		return shouldAcount;
	}

	public void setShouldAcount(long shouldAcount) {
		this.shouldAcount = shouldAcount;
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

	public long getNetAcount() {
		return netAcount;
	}

	public void setNetAcount(long netAcount) {
		this.netAcount = netAcount;
	}

	public long getFaceAcount() {
		return faceAcount;
	}

	public void setFaceAcount(long faceAcount) {
		this.faceAcount = faceAcount;
	}

	public long getOtherAcount() {
		return otherAcount;
	}

	public void setOtherAcount(long otherAcount) {
		this.otherAcount = otherAcount;
	}

	public String getCounterPercent() {
		return counterPercent;
	}

	public void setCounterPercent(String counterPercent) {
		this.counterPercent = counterPercent;
	}

	public String getPostPercent() {
		return postPercent;
	}

	public void setPostPercent(String postPercent) {
		this.postPercent = postPercent;
	}

	public String getNetPercent() {
		return netPercent;
	}

	public void setNetPercent(String netPercent) {
		this.netPercent = netPercent;
	}

	public String getFacePercent() {
		return facePercent;
	}

	public void setFacePercent(String facePercent) {
		this.facePercent = facePercent;
	}

	public String getOtherPercent() {
		return otherPercent;
	}

	public void setOtherPercent(String otherPercent) {
		this.otherPercent = otherPercent;
	}

	public String getsPercent() {
		return sPercent;
	}

	public void setsPercent(String sPercent) {
		this.sPercent = sPercent;
	}

	
}
