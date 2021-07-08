package com.yzj.ebs.database.temp.hbm;


/**
 * EbsBill entity. @author MyEclipse Persistence Tools
 */

public class EbsBill implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 2814923131040399007L;
	private Long autoid;
	private String accno;
	private String docdate;
	private String idbank;
	private String customerid;
	private String address;
	private String sendmode;
	private String voucherno;
	private String accnoindex;
	private String accson;
	private String singleaccno;
	
	private String idcenterno;

	// Constructors

	public String getIdcenterno() {
		return idcenterno;
	}

	public void setIdcenterno(String idcenterno) {
		this.idcenterno = idcenterno;
	}

	/** default constructor */
	public EbsBill() {
	}

	/** full constructor */
	public EbsBill(String accno, String docdate, String idbank,
			String customerid, String address, String sendmode,
			String voucherno, String accnoindex, String accson,
			String singleaccno) {
		this.accno = accno;
		this.docdate = docdate;
		this.idbank = idbank;
		this.customerid = customerid;
		this.address = address;
		this.sendmode = sendmode;
		this.voucherno = voucherno;
		this.accnoindex = accnoindex;
		this.accson = accson;
		this.singleaccno = singleaccno;
	}

	// Property accessors

	public Long getAutoid() {
		return this.autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}

	public String getAccno() {
		return this.accno;
	}

	public void setAccno(String accno) {
		this.accno = accno;
	}

	public String getDocdate() {
		return this.docdate;
	}

	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

	public String getIdbank() {
		return this.idbank;
	}

	public void setIdbank(String idbank) {
		this.idbank = idbank;
	}

	public String getCustomerid() {
		return this.customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSendmode() {
		return this.sendmode;
	}

	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
	}

	public String getVoucherno() {
		return this.voucherno;
	}

	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}

	public String getAccnoindex() {
		return this.accnoindex;
	}

	public void setAccnoindex(String accnoindex) {
		this.accnoindex = accnoindex;
	}

	public String getAccson() {
		return this.accson;
	}

	public void setAccson(String accson) {
		this.accson = accson;
	}

	public String getSingleaccno() {
		return this.singleaccno;
	}

	public void setSingleaccno(String singleaccno) {
		this.singleaccno = singleaccno;
	}

}