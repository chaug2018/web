package com.yzj.ebs.database.temp.hbm;

import com.yzj.ebs.util.UtilBase;


/**
 * EbsTempdetailmaindataId entity. @author MyEclipse Persistence Tools
 */

public class EbsMarginData implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -2212044147419281688L;
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
	private String acctypy;
	private String acctype;
	private String accnam;
	private String opendate;
	private String cdebflag;
	private String currtype;
	private String prodtype;
	private String proddesc;
	private String subnoc;
	private Double bal;
	private Double stamt;
	private Double maxtamt;
	private Double avbal;
	private String accstate;
	private String zip;
	private String address;
	private String respon;
	private String phone;
	private String docdate;
	private String idbranch;
	private String idcenterno;
	private String nfreeze;
	private String dzpl;
	private Double sumcount;
	private String voucherno;
	private String call;
	private String singleaccno;
	private String currency;
	private String keytype;
	private String acccycle;
	private String specialflag;
	private String signflag;
	private String signopcode;
	private String signtime;
	private String signcontractno;
	
	private String faceflag;
	private String sealmode;
	private String centerphone;
	private String centeraddress;
	private String sendmode;
	private String timelimit;
	private String accname;
	private String accnoindex;
	private String subnocflag;//识别按月出账单时 排除指定不需要的科目  ；1 是需要按照规则来按月出账单
	
	private String strcredit;
	


	public String getAccnoindex() {
		return accnoindex;
	}

	public void setAccnoindex(String accnoindex) {
		this.accnoindex = accnoindex;
	}

	public String getAccname() {
		return accname;
	}

	public void setAccname(String accname) {
		this.accname = accname;
	}

	public String getSubnocflag() {
		return subnocflag;
	}

	public void setSubnocflag(String subnocflag) {
		this.subnocflag = subnocflag;
	}

	public String getCenterphone() {
		return centerphone;
	}

	public void setCenterphone(String centerphone) {
		this.centerphone = centerphone;
	}

	public String getCenteraddress() {
		return centeraddress;
	}

	public void setCenteraddress(String centeraddress) {
		this.centeraddress = centeraddress;
	}

	
	
	public String getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}

	public String getSendmode() {
		return sendmode;
	}

	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
	}

	
	
	
	public String getSealmode() {
		return sealmode;
	}

	public void setSealmode(String sealmode) {
		this.sealmode = sealmode;
	}



	// Constructors

	public String getFaceflag() {
		return faceflag;
	}

	public void setFaceflag(String faceflag) {
		this.faceflag = faceflag;
	}

	/** default constructor */
	public EbsMarginData() {
	}

	/** minimal constructor */
	public EbsMarginData(String docdate) {
		this.docdate = docdate;
	}

	/** full constructor */
	public EbsMarginData(String idbank, String bankname,
			String custid, String accno, String accson, String acctypy,
			String accnam, String opendate, String cdebflag,
			String currtype, String prodtype, String proddesc, String subnoc,
			Double bal, Double stamt, Double maxtamt, Double avbal,
			String accstate, String zip, String address, String respon,
			String phone, String docdate, String idbranch, String idcenterno,
			String nfreeze, String dzpl, Double sumcount, String voucherno,
			String call, String singleaccno, String currency, String keytype,
			String acccycle, String specialflag, String signflag,
			String signopcode, String signtime, String signcontractno) {
		this.idbank = idbank;
		this.bankname = bankname;
		this.custid = custid;
		this.accno = accno;
		this.accson = accson;
		this.acctypy = acctypy;
		this.accnam = accnam;
		this.opendate = opendate;
		this.cdebflag = cdebflag;
		this.currtype = currtype;
		this.prodtype = prodtype;
		this.proddesc = proddesc;
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
		this.docdate = docdate;
		this.idbranch = idbranch;
		this.idcenterno = idcenterno;
		this.nfreeze = nfreeze;
		this.dzpl = dzpl;
		this.sumcount = sumcount;
		this.voucherno = voucherno;
		this.call = call;
		this.singleaccno = singleaccno;
		this.currency = currency;
		this.keytype = keytype;
		this.acccycle = acccycle;
		this.specialflag = specialflag;
		this.signflag = signflag;
		this.signopcode = signopcode;
		this.signtime = signtime;
		this.signcontractno = signcontractno;
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

	public String getAcctypy() {
		return this.acctypy;
	}

	public void setAcctypy(String acctypy) {
		this.acctypy = acctypy;
	}

	public String getAccnam() {
		return this.accnam;
	}

	public void setAccnam(String accnam) {
		this.accnam = accnam;
	}

	public String getOpendate() {
		return this.opendate;
	}

	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}

	public String getCdebflag() {
		return this.cdebflag;
	}

	public void setCdebflag(String cdebflag) {
		this.cdebflag = cdebflag;
	}

	public String getCurrtype() {
		return this.currtype;
	}

	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}

	public String getProdtype() {
		return this.prodtype;
	}

	public void setProdtype(String prodtype) {
		this.prodtype = prodtype;
	}

	public String getProddesc() {
		return this.proddesc;
	}

	public void setProddesc(String proddesc) {
		this.proddesc = proddesc;
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
	
	
	
	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getBal());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
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

	public String getAccstate() {
		return this.accstate;
	}

	public void setAccstate(String accstate) {
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

	public String getDocdate() {
		return this.docdate;
	}

	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

	public String getIdbranch() {
		return this.idbranch;
	}

	public void setIdbranch(String idbranch) {
		this.idbranch = idbranch;
	}

	public String getIdcenterno() {
		return this.idcenterno;
	}

	public void setIdcenterno(String idcenterno) {
		this.idcenterno = idcenterno;
	}

	public String getNfreeze() {
		return this.nfreeze;
	}

	public void setNfreeze(String nfreeze) {
		this.nfreeze = nfreeze;
	}

	public String getDzpl() {
		return this.dzpl;
	}

	public void setDzpl(String dzpl) {
		this.dzpl = dzpl;
	}

	public Double getSumcount() {
		return this.sumcount;
	}

	public void setSumcount(Double sumcount) {
		this.sumcount = sumcount;
	}

	public String getVoucherno() {
		return this.voucherno;
	}

	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}

	public String getCall() {
		return this.call;
	}

	public void setCall(String call) {
		this.call = call;
	}

	public String getSingleaccno() {
		return this.singleaccno;
	}

	public void setSingleaccno(String singleaccno) {
		this.singleaccno = singleaccno;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getKeytype() {
		return this.keytype;
	}

	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}

	public String getAcccycle() {
		return this.acccycle;
	}

	public void setAcccycle(String acccycle) {
		this.acccycle = acccycle;
	}

	public String getSpecialflag() {
		return this.specialflag;
	}

	public void setSpecialflag(String specialflag) {
		this.specialflag = specialflag;
	}

	public String getSignflag() {
		return this.signflag;
	}

	public void setSignflag(String signflag) {
		this.signflag = signflag;
	}

	public String getSignopcode() {
		return this.signopcode;
	}

	public void setSignopcode(String signopcode) {
		this.signopcode = signopcode;
	}

	public String getSigntime() {
		return this.signtime;
	}

	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}

	public String getSigncontractno() {
		return this.signcontractno;
	}

	public void setSigncontractno(String signcontractno) {
		this.signcontractno = signcontractno;
	}


	public String getAcctype() {
		return acctype;
	}

	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EbsMarginData))
			return false;
		EbsMarginData castOther = (EbsMarginData) other;

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
				&& ((this.getAcctypy() == castOther.getAcctypy()) || (this
						.getAcctypy() != null && castOther.getAcctypy() != null && this
						.getAcctypy().equals(castOther.getAcctypy())))
				&& ((this.getAccnam() == castOther.getAccnam()) || (this
						.getAccnam() != null && castOther.getAccnam() != null && this
						.getAccnam().equals(castOther.getAccnam())))
				&& ((this.getOpendate() == castOther.getOpendate()) || (this
						.getOpendate() != null
						&& castOther.getOpendate() != null && this
						.getOpendate().equals(castOther.getOpendate())))
				&& ((this.getCdebflag() == castOther.getCdebflag()) || (this
						.getCdebflag() != null
						&& castOther.getCdebflag() != null && this
						.getCdebflag().equals(castOther.getCdebflag())))
				&& ((this.getCurrtype() == castOther.getCurrtype()) || (this
						.getCurrtype() != null
						&& castOther.getCurrtype() != null && this
						.getCurrtype().equals(castOther.getCurrtype())))
				&& ((this.getProdtype() == castOther.getProdtype()) || (this
						.getProdtype() != null
						&& castOther.getProdtype() != null && this
						.getProdtype().equals(castOther.getProdtype())))
				&& ((this.getProddesc() == castOther.getProddesc()) || (this
						.getProddesc() != null
						&& castOther.getProddesc() != null && this
						.getProddesc().equals(castOther.getProddesc())))
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
				&& ((this.getDocdate() == castOther.getDocdate()) || (this
						.getDocdate() != null && castOther.getDocdate() != null && this
						.getDocdate().equals(castOther.getDocdate())))
				&& ((this.getIdbranch() == castOther.getIdbranch()) || (this
						.getIdbranch() != null
						&& castOther.getIdbranch() != null && this
						.getIdbranch().equals(castOther.getIdbranch())))
				&& ((this.getIdcenterno() == castOther.getIdcenterno()) || (this
						.getIdcenterno() != null
						&& castOther.getIdcenterno() != null && this
						.getIdcenterno().equals(castOther.getIdcenterno())))
				&& ((this.getNfreeze() == castOther.getNfreeze()) || (this
						.getNfreeze() != null && castOther.getNfreeze() != null && this
						.getNfreeze().equals(castOther.getNfreeze())))
				&& ((this.getDzpl() == castOther.getDzpl()) || (this.getDzpl() != null
						&& castOther.getDzpl() != null && this.getDzpl()
						.equals(castOther.getDzpl())))
				&& ((this.getSumcount() == castOther.getSumcount()) || (this
						.getSumcount() != null
						&& castOther.getSumcount() != null && this
						.getSumcount().equals(castOther.getSumcount())))
				&& ((this.getVoucherno() == castOther.getVoucherno()) || (this
						.getVoucherno() != null
						&& castOther.getVoucherno() != null && this
						.getVoucherno().equals(castOther.getVoucherno())))
				&& ((this.getCall() == castOther.getCall()) || (this.getCall() != null
						&& castOther.getCall() != null && this.getCall()
						.equals(castOther.getCall())))
				&& ((this.getSingleaccno() == castOther.getSingleaccno()) || (this
						.getSingleaccno() != null
						&& castOther.getSingleaccno() != null && this
						.getSingleaccno().equals(castOther.getSingleaccno())))
				&& ((this.getCurrency() == castOther.getCurrency()) || (this
						.getCurrency() != null
						&& castOther.getCurrency() != null && this
						.getCurrency().equals(castOther.getCurrency())))
				&& ((this.getKeytype() == castOther.getKeytype()) || (this
						.getKeytype() != null && castOther.getKeytype() != null && this
						.getKeytype().equals(castOther.getKeytype())))
				&& ((this.getAcccycle() == castOther.getAcccycle()) || (this
						.getAcccycle() != null
						&& castOther.getAcccycle() != null && this
						.getAcccycle().equals(castOther.getAcccycle())))
				&& ((this.getSpecialflag() == castOther.getSpecialflag()) || (this
						.getSpecialflag() != null
						&& castOther.getSpecialflag() != null && this
						.getSpecialflag().equals(castOther.getSpecialflag())))
				&& ((this.getSignflag() == castOther.getSignflag()) || (this
						.getSignflag() != null
						&& castOther.getSignflag() != null && this
						.getSignflag().equals(castOther.getSignflag())))
				&& ((this.getSignopcode() == castOther.getSignopcode()) || (this
						.getSignopcode() != null
						&& castOther.getSignopcode() != null && this
						.getSignopcode().equals(castOther.getSignopcode())))
				&& ((this.getSigntime() == castOther.getSigntime()) || (this
						.getSigntime() != null
						&& castOther.getSigntime() != null && this
						.getSigntime().equals(castOther.getSigntime())))
				&& ((this.getSigncontractno() == castOther.getSigncontractno()) || (this
						.getSigncontractno() != null
						&& castOther.getSigncontractno() != null && this
						.getSigncontractno().equals(
								castOther.getSigncontractno())));
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
				+ (getAcctypy() == null ? 0 : this.getAcctypy().hashCode());
		result = 37 * result
				+ (getAccnam() == null ? 0 : this.getAccnam().hashCode());
		result = 37 * result
				+ (getOpendate() == null ? 0 : this.getOpendate().hashCode());
		result = 37 * result
				+ (getCdebflag() == null ? 0 : this.getCdebflag().hashCode());
		result = 37 * result
				+ (getCurrtype() == null ? 0 : this.getCurrtype().hashCode());
		result = 37 * result
				+ (getProdtype() == null ? 0 : this.getProdtype().hashCode());
		result = 37 * result
				+ (getProddesc() == null ? 0 : this.getProddesc().hashCode());
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
				+ (getDocdate() == null ? 0 : this.getDocdate().hashCode());
		result = 37 * result
				+ (getIdbranch() == null ? 0 : this.getIdbranch().hashCode());
		result = 37
				* result
				+ (getIdcenterno() == null ? 0 : this.getIdcenterno()
						.hashCode());
		result = 37 * result
				+ (getNfreeze() == null ? 0 : this.getNfreeze().hashCode());
		result = 37 * result
				+ (getDzpl() == null ? 0 : this.getDzpl().hashCode());
		result = 37 * result
				+ (getSumcount() == null ? 0 : this.getSumcount().hashCode());
		result = 37 * result
				+ (getVoucherno() == null ? 0 : this.getVoucherno().hashCode());
		result = 37 * result
				+ (getCall() == null ? 0 : this.getCall().hashCode());
		result = 37
				* result
				+ (getSingleaccno() == null ? 0 : this.getSingleaccno()
						.hashCode());
		result = 37 * result
				+ (getCurrency() == null ? 0 : this.getCurrency().hashCode());
		result = 37 * result
				+ (getKeytype() == null ? 0 : this.getKeytype().hashCode());
		result = 37 * result
				+ (getAcccycle() == null ? 0 : this.getAcccycle().hashCode());
		result = 37
				* result
				+ (getSpecialflag() == null ? 0 : this.getSpecialflag()
						.hashCode());
		result = 37 * result
				+ (getSignflag() == null ? 0 : this.getSignflag().hashCode());
		result = 37
				* result
				+ (getSignopcode() == null ? 0 : this.getSignopcode()
						.hashCode());
		result = 37 * result
				+ (getSigntime() == null ? 0 : this.getSigntime().hashCode());
		result = 37
				* result
				+ (getSigncontractno() == null ? 0 : this.getSigncontractno()
						.hashCode());
		return result;
	}

}