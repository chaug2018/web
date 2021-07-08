package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账有效明细统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class EbillMatchDetailResult implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String idCenter; //分行
	private String idBank;// 机构号
	private String bankName;// 机构名称
	private String accNo;// 账号
	private String accname;
	private long failCount;//未成功次数
	private String MatchCount1;
	private String MatchCount2;
	private String MatchCount3;
	private String MatchCount4;
	private String MatchCount5;
	private String MatchCount6;

	public EbillMatchDetailResult() {
	}

	public EbillMatchDetailResult(String idCenter,String idBank, String bankName, String accNo,
			String accname,long failCount, String MatchCount1, String MatchCount2, String MatchCount3, 
			String MatchCount4,String MatchCount5, String MatchCount6) {
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.bankName = bankName;
		this.accNo = accNo;
		this.accname = accname;
		this.failCount = failCount;
		this.MatchCount1 = MatchCount1;
		this.MatchCount2 = MatchCount2;
		this.MatchCount3 = MatchCount3;
		this.MatchCount4 = MatchCount4;
		this.MatchCount5 = MatchCount5;
		this.MatchCount6 = MatchCount6;
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

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public long getFailCount() {
		return failCount;
	}

	public void setFailCount(long failCount) {
		this.failCount = failCount;
	}

	public String getMatchCount1() {
		return MatchCount1;
	}

	public void setMatchCount1(String matchCount1) {
		MatchCount1 = matchCount1;
	}

	public String getMatchCount2() {
		return MatchCount2;
	}

	public void setMatchCount2(String matchCount2) {
		MatchCount2 = matchCount2;
	}

	public String getMatchCount3() {
		return MatchCount3;
	}

	public void setMatchCount3(String matchCount3) {
		MatchCount3 = matchCount3;
	}

	public String getMatchCount4() {
		return MatchCount4;
	}

	public void setMatchCount4(String matchCount4) {
		MatchCount4 = matchCount4;
	}

	public String getMatchCount5() {
		return MatchCount5;
	}

	public void setMatchCount5(String matchCount5) {
		MatchCount5 = matchCount5;
	}

	public String getMatchCount6() {
		return MatchCount6;
	}

	public void setMatchCount6(String matchCount6) {
		MatchCount6 = matchCount6;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
	

}
