package com.yzj.ebs.database.temp.hbm;

/**
 * EbsUtblbrcdId entity. @author MyEclipse Persistence Tools
 */

public class EbsUtblbrcd implements java.io.Serializable {

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
	private String brcd;
	private String descnam;
	private Short zlvlcode;
	private String zrepbrcd;
	private String centtg;
	private String sid;
	private String address;
	private String phone;
	private String dataDate;//数据日期 
	private String bankid;
	// Constructors

	
	
	
	
	/** default constructor */
	public EbsUtblbrcd() {
	}

	public String getBankid() {
		return bankid;
	}

	public void setBankid(String bankid) {
		this.bankid = bankid;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	/** full constructor */
	public EbsUtblbrcd(String brcd, String descnam, Short zlvlcode,
			String zrepbrcd, String centtg, String sid) {
		this.brcd = brcd;
		this.descnam = descnam;
		this.zlvlcode = zlvlcode;
		this.zrepbrcd = zrepbrcd;
		this.centtg = centtg;
		this.sid = sid;
	}

	// Property accessors

	public String getBrcd() {
		return this.brcd;
	}

	public void setBrcd(String brcd) {
		this.brcd = brcd;
	}

	public String getDescnam() {
		return this.descnam;
	}

	public void setDescnam(String descnam) {
		this.descnam = descnam;
	}

	public Short getZlvlcode() {
		return this.zlvlcode;
	}

	public void setZlvlcode(Short zlvlcode) {
		this.zlvlcode = zlvlcode;
	}

	public String getZrepbrcd() {
		return this.zrepbrcd;
	}

	public void setZrepbrcd(String zrepbrcd) {
		this.zrepbrcd = zrepbrcd;
	}

	public String getCenttg() {
		return this.centtg;
	}

	public void setCenttg(String centtg) {
		this.centtg = centtg;
	}

	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EbsUtblbrcd))
			return false;
		EbsUtblbrcd castOther = (EbsUtblbrcd) other;

		return ((this.getBrcd() == castOther.getBrcd()) || (this.getBrcd() != null
				&& castOther.getBrcd() != null && this.getBrcd().equals(
				castOther.getBrcd())))
				&& ((this.getDescnam() == castOther.getDescnam()) || (this
						.getDescnam() != null && castOther.getDescnam() != null && this
						.getDescnam().equals(castOther.getDescnam())))
				&& ((this.getZlvlcode() == castOther.getZlvlcode()) || (this
						.getZlvlcode() != null
						&& castOther.getZlvlcode() != null && this
						.getZlvlcode().equals(castOther.getZlvlcode())))
				&& ((this.getZrepbrcd() == castOther.getZrepbrcd()) || (this
						.getZrepbrcd() != null
						&& castOther.getZrepbrcd() != null && this
						.getZrepbrcd().equals(castOther.getZrepbrcd())))
				&& ((this.getCenttg() == castOther.getCenttg()) || (this
						.getCenttg() != null && castOther.getCenttg() != null && this
						.getCenttg().equals(castOther.getCenttg())))
				&& ((this.getSid() == castOther.getSid()) || (this.getSid() != null
						&& castOther.getSid() != null && this.getSid().equals(
						castOther.getSid())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getBrcd() == null ? 0 : this.getBrcd().hashCode());
		result = 37 * result
				+ (getDescnam() == null ? 0 : this.getDescnam().hashCode());
		result = 37 * result
				+ (getZlvlcode() == null ? 0 : this.getZlvlcode().hashCode());
		result = 37 * result
				+ (getZrepbrcd() == null ? 0 : this.getZrepbrcd().hashCode());
		result = 37 * result
				+ (getCenttg() == null ? 0 : this.getCenttg().hashCode());
		result = 37 * result
				+ (getSid() == null ? 0 : this.getSid().hashCode());
		return result;
	}

}