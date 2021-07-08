package com.yzj.ebs.database.temp.hbm;


/**
 * EbsMaindataId entity. @author MyEclipse Persistence Tools
 */

public class EbsMaindata implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long autoid;
	public Long getAutoid() {
		return autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}
	private String idbank;
	private String bankname;
	private String custid;
	private String accno;
	private String accson;
	private String cdebflag;
	private String accname;
	private String opendate;
	private String acctype;
	private String currtype;
	private String subnoc;
	private Double bal;
	private Double stamt;
	private Double maxtamt;
	private Double avbal;
	private Boolean accstate;
	private String zip;
	private String address;
	private String respon;
	private String phone;
	private String call;
	private String dataDate;//数据日期 
	private String importdate;
	private String timelimt;
	

	// Constructors

	/** default constructor */
	public EbsMaindata() {
	}

	/** full constructor */
	public EbsMaindata(String idbank, String bankname, String custid,
			String accno, String accson, String cdebflag, String accnam,
			String opendate, String acctypy, String currtype, String subnoc,
			Double bal, Double stamt, Double maxtamt, Double avbal,
			Boolean accstate, String zip, String address, String respon,
			String phone, String call, String dataDate, String importdate) {
		this.idbank = idbank;
		this.bankname = bankname;
		this.custid = custid;
		this.accno = accno;
		this.accson = accson;
		this.cdebflag = cdebflag;
		this.accname = accnam;
		this.opendate = opendate;
		this.acctype = acctypy;
		this.currtype = currtype;
		this.subnoc = subnoc;
		this.bal = bal;
		this.stamt = stamt;
		this.maxtamt = maxtamt;
		this.avbal = avbal;
		this.accstate = accstate;
		this.zip = zip;
		this.address = address;
		this.respon = respon;
		this.phone = phone;
		this.call = call;
		this.dataDate = dataDate;
		this.importdate = importdate;
	}

	// Property accessors

	public String getIdbank() {
		return this.idbank;
	}

	public void setIdbank(String idbank) {
		this.idbank = idbank;
	}

	public String getBankname() {
		return this.bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getCustid() {
		return this.custid;
	}

	public void setCustid(String custid) {
		this.custid = custid;
	}

	public String getAccno() {
		return this.accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getAccson() {
		return this.accson;
	}

	public void setAccson(String accson) {
		this.accson = accson;
	}

	public String getCdebflag() {
		return this.cdebflag;
	}

	public void setCdebflag(String cdebflag) {
		this.cdebflag = cdebflag;
	}

	public String getAccname() {
		return this.accname;
	}

	public void setAccnam(String accnam) {
		this.accname = accnam;
	}

	public String getOpendate() {
		return this.opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getAcctypy() {
		return this.acctype;
	}

	public void setAcctype(String acctypy) {
		this.acctype = acctypy;
	}

	public String getCurrtype() {
		return this.currtype;
	}

	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}

	public String getSubnoc() {
		return this.subnoc;
	}

	public void setSubnoc(String subnoc) {
		this.subnoc = subnoc;
	}

	public Double getBal() {
		return this.bal;
	}

	public void setBal(Double bal) {
		this.bal = bal;
	}

	public Double getStamt() {
		return this.stamt;
	}

	public void setStamt(Double stamt) {
		this.stamt = stamt;
	}

	public Double getMaxtamt() {
		return this.maxtamt;
	}

	public void setMaxtamt(Double maxtamt) {
		this.maxtamt = maxtamt;
	}

	public Double getAvbal() {
		return this.avbal;
	}

	public void setAvbal(Double avbal) {
		this.avbal = avbal;
	}

	public Boolean getAccstate() {
		return this.accstate;
	}

	public void setAccstate(Boolean accstate) {
		this.accstate = accstate;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRespon() {
		return this.respon;
	}

	public void setRespon(String respon) {
		this.respon = respon;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCall() {
		return this.call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getImportdate() {
		return this.importdate;
	}

	public void setImportdate(String importdate) {
		this.importdate = importdate;
	}
	
	public String getTimelimt() {
		return timelimt;
	}

	public void setTimelimt(String timelimt) {
		this.timelimt = timelimt;
	}

	public String getAcctype() {
		return acctype;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EbsMaindata))
			return false;
		EbsMaindata castOther = (EbsMaindata) other;

		return ((this.getIdbank() == castOther.getIdbank()) || (this
				.getIdbank() != null && castOther.getIdbank() != null && this
				.getIdbank().equals(castOther.getIdbank())))
				&& ((this.getBankname() == castOther.getBankname()) || (this
						.getBankname() != null
						&& castOther.getBankname() != null && this
						.getBankname().equals(castOther.getBankname())))
				&& ((this.getCustid() == castOther.getCustid()) || (this
						.getCustid() != null && castOther.getCustid() != null && this
						.getCustid().equals(castOther.getCustid())))
				&& ((this.getAccno() == castOther.getAccno()) || (this
						.getAccno() != null && castOther.getAccno() != null && this
						.getAccno().equals(castOther.getAccno())))
				&& ((this.getAccson() == castOther.getAccson()) || (this
						.getAccson() != null && castOther.getAccson() != null && this
						.getAccson().equals(castOther.getAccson())))
				&& ((this.getCdebflag() == castOther.getCdebflag()) || (this
						.getCdebflag() != null
						&& castOther.getCdebflag() != null && this
						.getCdebflag().equals(castOther.getCdebflag())))
				&& ((this.getAccname() == castOther.getAccname()) || (this
						.getAccname() != null && castOther.getAccname() != null && this
						.getAccname().equals(castOther.getAccname())))
				&& ((this.getOpendate() == castOther.getOpendate()) || (this
						.getOpendate() != null
						&& castOther.getOpendate() != null && this
						.getOpendate().equals(castOther.getOpendate())))
				&& ((this.getAcctypy() == castOther.getAcctypy()) || (this
						.getAcctypy() != null && castOther.getAcctypy() != null && this
						.getAcctypy().equals(castOther.getAcctypy())))
				&& ((this.getCurrtype() == castOther.getCurrtype()) || (this
						.getCurrtype() != null
						&& castOther.getCurrtype() != null && this
						.getCurrtype().equals(castOther.getCurrtype())))
				&& ((this.getSubnoc() == castOther.getSubnoc()) || (this
						.getSubnoc() != null && castOther.getSubnoc() != null && this
						.getSubnoc().equals(castOther.getSubnoc())))
				&& ((this.getBal() == castOther.getBal()) || (this.getBal() != null
						&& castOther.getBal() != null && this.getBal().equals(
						castOther.getBal())))
				&& ((this.getStamt() == castOther.getStamt()) || (this
						.getStamt() != null && castOther.getStamt() != null && this
						.getStamt().equals(castOther.getStamt())))
				&& ((this.getMaxtamt() == castOther.getMaxtamt()) || (this
						.getMaxtamt() != null && castOther.getMaxtamt() != null && this
						.getMaxtamt().equals(castOther.getMaxtamt())))
				&& ((this.getAvbal() == castOther.getAvbal()) || (this
						.getAvbal() != null && castOther.getAvbal() != null && this
						.getAvbal().equals(castOther.getAvbal())))
				&& ((this.getAccstate() == castOther.getAccstate()) || (this
						.getAccstate() != null
						&& castOther.getAccstate() != null && this
						.getAccstate().equals(castOther.getAccstate())))
				&& ((this.getZip() == castOther.getZip()) || (this.getZip() != null
						&& castOther.getZip() != null && this.getZip().equals(
						castOther.getZip())))
				&& ((this.getAddress() == castOther.getAddress()) || (this
						.getAddress() != null && castOther.getAddress() != null && this
						.getAddress().equals(castOther.getAddress())))
				&& ((this.getRespon() == castOther.getRespon()) || (this
						.getRespon() != null && castOther.getRespon() != null && this
						.getRespon().equals(castOther.getRespon())))
				&& ((this.getPhone() == castOther.getPhone()) || (this
						.getPhone() != null && castOther.getPhone() != null && this
						.getPhone().equals(castOther.getPhone())))
				&& ((this.getCall() == castOther.getCall()) || (this.getCall() != null
						&& castOther.getCall() != null && this.getCall()
						.equals(castOther.getCall())))
				&& ((this.getDataDate() == castOther.getDataDate()) || (this
						.getDataDate() != null && castOther.getDataDate() != null && this
						.getDataDate().equals(castOther.getDataDate())))
				&& ((this.getImportdate() == castOther.getImportdate()) || (this
						.getImportdate() != null
						&& castOther.getImportdate() != null && this
						.getImportdate().equals(castOther.getImportdate())));
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIdbank() == null ? 0 : this.getIdbank().hashCode());
		result = 37 * result
				+ (getBankname() == null ? 0 : this.getBankname().hashCode());
		result = 37 * result
				+ (getCustid() == null ? 0 : this.getCustid().hashCode());
		result = 37 * result
				+ (getAccno() == null ? 0 : this.getAccno().hashCode());
		result = 37 * result
				+ (getAccson() == null ? 0 : this.getAccson().hashCode());
		result = 37 * result
				+ (getCdebflag() == null ? 0 : this.getCdebflag().hashCode());
		result = 37 * result
				+ (getAccname() == null ? 0 : this.getAccname().hashCode());
		result = 37 * result
				+ (getOpendate() == null ? 0 : this.getOpendate().hashCode());
		result = 37 * result
				+ (getAcctypy() == null ? 0 : this.getAcctypy().hashCode());
		result = 37 * result
				+ (getCurrtype() == null ? 0 : this.getCurrtype().hashCode());
		result = 37 * result
				+ (getSubnoc() == null ? 0 : this.getSubnoc().hashCode());
		result = 37 * result
				+ (getBal() == null ? 0 : this.getBal().hashCode());
		result = 37 * result
				+ (getStamt() == null ? 0 : this.getStamt().hashCode());
		result = 37 * result
				+ (getMaxtamt() == null ? 0 : this.getMaxtamt().hashCode());
		result = 37 * result
				+ (getAvbal() == null ? 0 : this.getAvbal().hashCode());
		result = 37 * result
				+ (getAccstate() == null ? 0 : this.getAccstate().hashCode());
		result = 37 * result
				+ (getZip() == null ? 0 : this.getZip().hashCode());
		result = 37 * result
				+ (getAddress() == null ? 0 : this.getAddress().hashCode());
		result = 37 * result
				+ (getRespon() == null ? 0 : this.getRespon().hashCode());
		result = 37 * result
				+ (getPhone() == null ? 0 : this.getPhone().hashCode());
		result = 37 * result
				+ (getCall() == null ? 0 : this.getCall().hashCode());
		result = 37 * result
				+ (getDataDate() == null ? 0 : this.getDataDate().hashCode());
		result = 37
				* result
				+ (getImportdate() == null ? 0 : this.getImportdate()
						.hashCode());
		return result;
	}

}