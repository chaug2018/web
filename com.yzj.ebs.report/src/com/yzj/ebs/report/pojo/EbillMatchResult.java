package com.yzj.ebs.report.pojo;
/**
 * 
 *创建于:2012-11-13<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账有效率统计结果类，保存统计数据
 * @author 施江敏
 * @version 1.0.0
 */
public class EbillMatchResult implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String idCenter;//分行
	private String idBank;// 网点
	private String cname;// 网点名称
	private String docDate;// 对账日期
	private long SendCount1;
	private long MatchCount1;
	private long SendCount2;
	private long MatchCount2;
	private long SendCount3;
	private long MatchCount3;
	private long SendCount4;
	private long MatchCount4;
	private long SendCount5;
	private long MatchCount5;
	private long SendCount6;
	private long MatchCount6;
	private String fstMatchPercent;
	private String secMatchPercent;
	private String thrMatchPercent;
	private String fouMatchPercent;
	private String fifMatchPercent;
	private String sixMatchPercent;

	public EbillMatchResult(String idCenter,String idBank, String cName, long SendCount1,
			long MatchCount1, long SendCount2, long MatchCount2,
			long SendCount3, long MatchCount3, long SendCount4,
			long MatchCount4, long SendCount5, long MatchCount5,
			long SendCount6, long MatchCount6, String fstMatchPercent,
			String secMatchPercent, String thrMatchPercent,
			String fouMatchPercent, String fifMatchPercent,
			String sixMatchPercent) {
		this.idCenter = idCenter;
		this.idBank = idBank;
		this.cname = cName;
		this.SendCount1 = SendCount1;
		this.MatchCount1 = MatchCount1;
		this.SendCount2 = SendCount2;
		this.MatchCount2 = MatchCount2;
		this.SendCount3 = SendCount3;
		this.MatchCount3 = MatchCount3;
		this.SendCount4 = SendCount4;
		this.MatchCount4 = MatchCount4;
		this.SendCount5 = SendCount5;
		this.MatchCount5 = MatchCount5;
		this.SendCount6 = SendCount6;
		this.MatchCount6 = MatchCount6;
		this.fstMatchPercent = fstMatchPercent;
		this.secMatchPercent = secMatchPercent;
		this.thrMatchPercent = thrMatchPercent;
		this.fouMatchPercent = fouMatchPercent;
		this.fifMatchPercent = fifMatchPercent;
		this.sixMatchPercent = sixMatchPercent;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public long getSendCount1() {
		return SendCount1;
	}

	public void setSendCount1(long sendCount1) {
		SendCount1 = sendCount1;
	}

	public long getMatchCount1() {
		return MatchCount1;
	}

	public void setMatchCount1(long matchCount1) {
		MatchCount1 = matchCount1;
	}

	public long getSendCount2() {
		return SendCount2;
	}

	public void setSendCount2(long sendCount2) {
		SendCount2 = sendCount2;
	}

	public long getMatchCount2() {
		return MatchCount2;
	}

	public void setMatchCount2(long matchCount2) {
		MatchCount2 = matchCount2;
	}

	public long getSendCount3() {
		return SendCount3;
	}

	public void setSendCount3(long sendCount3) {
		SendCount3 = sendCount3;
	}

	public long getMatchCount3() {
		return MatchCount3;
	}

	public void setMatchCount3(long matchCount3) {
		MatchCount3 = matchCount3;
	}

	public long getSendCount4() {
		return SendCount4;
	}

	public void setSendCount4(long sendCount4) {
		SendCount4 = sendCount4;
	}

	public long getMatchCount4() {
		return MatchCount4;
	}

	public void setMatchCount4(long matchCount4) {
		MatchCount4 = matchCount4;
	}

	public long getSendCount5() {
		return SendCount5;
	}

	public void setSendCount5(long sendCount5) {
		SendCount5 = sendCount5;
	}

	public long getMatchCount5() {
		return MatchCount5;
	}

	public void setMatchCount5(long matchCount5) {
		MatchCount5 = matchCount5;
	}

	public long getSendCount6() {
		return SendCount6;
	}

	public void setSendCount6(long sendCount6) {
		SendCount6 = sendCount6;
	}

	public long getMatchCount6() {
		return MatchCount6;
	}

	public void setMatchCount6(long matchCount6) {
		MatchCount6 = matchCount6;
	}

	public String getFstMatchPercent() {
		return fstMatchPercent;
	}

	public void setFstMatchPercent(String fstMatchPercent) {
		this.fstMatchPercent = fstMatchPercent;
	}

	public String getSecMatchPercent() {
		return secMatchPercent;
	}

	public void setSecMatchPercent(String secMatchPercent) {
		this.secMatchPercent = secMatchPercent;
	}

	public String getThrMatchPercent() {
		return thrMatchPercent;
	}

	public void setThrMatchPercent(String thrMatchPercent) {
		this.thrMatchPercent = thrMatchPercent;
	}

	public String getFouMatchPercent() {
		return fouMatchPercent;
	}

	public void setFouMatchPercent(String fouMatchPercent) {
		this.fouMatchPercent = fouMatchPercent;
	}

	public String getFifMatchPercent() {
		return fifMatchPercent;
	}

	public void setFifMatchPercent(String fifMatchPercent) {
		this.fifMatchPercent = fifMatchPercent;
	}

	public String getSixMatchPercent() {
		return sixMatchPercent;
	}

	public void setSixMatchPercent(String sixMatchPercent) {
		this.sixMatchPercent = sixMatchPercent;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	

}
