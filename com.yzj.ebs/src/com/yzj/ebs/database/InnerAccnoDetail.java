package com.yzj.ebs.database;

public class InnerAccnoDetail implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	private Long autoid;
	private String trad_date;//交易日期
	private String seri_no;//流水号
	private String curr_code;//币种
	private String acct;//交易账号
	private String oppost_acct;//对方账号
	private String oppost_acct_name;//对方户名
	private String vouch_no;//凭证号码
	private String summy;//摘要
	private String borrow_lend_sign;//借贷标志
	private String trad_amt;//发生额
	private String acct_bal;//实时余额
	private String trad_code;//交易码
	private String host_syst_time;//主机系统时间
	private String dataDate;//数据日期
	private String importDate;//导入日期
	
	public Long getAutoid() {
		return autoid;
	}
	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}
	public String getTrad_date() {
		return trad_date;
	}
	public void setTrad_date(String trad_date) {
		this.trad_date = trad_date;
	}
	public String getSeri_no() {
		return seri_no;
	}
	public void setSeri_no(String seri_no) {
		this.seri_no = seri_no;
	}
	public String getCurr_code() {
		return curr_code;
	}
	public void setCurr_code(String curr_code) {
		this.curr_code = curr_code;
	}
	public String getAcct() {
		return acct;
	}
	public void setAcct(String acct) {
		this.acct = acct;
	}
	public String getOppost_acct() {
		return oppost_acct;
	}
	public void setOppost_acct(String oppost_acct) {
		this.oppost_acct = oppost_acct;
	}
	public String getOppost_acct_name() {
		return oppost_acct_name;
	}
	public void setOppost_acct_name(String oppost_acct_name) {
		this.oppost_acct_name = oppost_acct_name;
	}
	public String getVouch_no() {
		return vouch_no;
	}
	public void setVouch_no(String vouch_no) {
		this.vouch_no = vouch_no;
	}
	public String getSummy() {
		return summy;
	}
	public void setSummy(String summy) {
		this.summy = summy;
	}
	public String getBorrow_lend_sign() {
		return borrow_lend_sign;
	}
	public void setBorrow_lend_sign(String borrow_lend_sign) {
		this.borrow_lend_sign = borrow_lend_sign;
	}
	public String getTrad_amt() {
		return trad_amt;
	}
	public void setTrad_amt(String trad_amt) {
		this.trad_amt = trad_amt;
	}
	public String getAcct_bal() {
		return acct_bal;
	}
	public void setAcct_bal(String acct_bal) {
		this.acct_bal = acct_bal;
	}
	public String getTrad_code() {
		return trad_code;
	}
	public void setTrad_code(String trad_code) {
		this.trad_code = trad_code;
	}
	public String getHost_syst_time() {
		return host_syst_time;
	}
	public void setHost_syst_time(String host_syst_time) {
		this.host_syst_time = host_syst_time;
	}
	public String getDataDate() {
		return dataDate;
	}
	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}
	public String getImportDate() {
		return importDate;
	}
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}
	
}