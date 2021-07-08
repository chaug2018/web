package com.yzj.ebs.database.temp.hbm;

/**
 * EbsKubUserId entity. @author MyEclipse Persistence Tools
 */

public class EbsKubUser implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3991208554050055415L;
	private Long autoid;
	public Long getAutoid() {
		return autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}

	private String peoplecode;
	private String peoplename;
	private String orgno;
	private String sex;

	// Constructors

	/** default constructor */
	public EbsKubUser() {
	}

	/** full constructor */
	public EbsKubUser(String peoplecode, String peoplename, String orgno) {
		this.peoplecode = peoplecode;
		this.peoplename = peoplename;
		this.orgno = orgno;
	}

	// Property accessors

	public String getPeoplecode() {
		return this.peoplecode;
	}

	public void setPeoplecode(String peoplecode) {
		this.peoplecode = peoplecode;
	}

	public String getPeoplename() {
		return this.peoplename;
	}

	public void setPeoplename(String peoplename) {
		this.peoplename = peoplename;
	}

	public String getOrgno() {
		return this.orgno;
	}

	public void setOrgno(String orgno) {
		this.orgno = orgno;
	}
	

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EbsKubUser))
			return false;
		EbsKubUser castOther = (EbsKubUser) other;

		return ((this.getPeoplecode() == castOther.getPeoplecode()) || (this
				.getPeoplecode() != null && castOther.getPeoplecode() != null && this
				.getPeoplecode().equals(castOther.getPeoplecode())))
				&& ((this.getPeoplename() == castOther.getPeoplename()) || (this
						.getPeoplename() != null
						&& castOther.getPeoplename() != null && this
						.getPeoplename().equals(castOther.getPeoplename())))
				&& ((this.getOrgno() == castOther.getOrgno()) || (this
						.getOrgno() != null && castOther.getOrgno() != null && this
						.getOrgno().equals(castOther.getOrgno())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getPeoplecode() == null ? 0 : this.getPeoplecode()
						.hashCode());
		result = 37
				* result
				+ (getPeoplename() == null ? 0 : this.getPeoplename()
						.hashCode());
		result = 37 * result
				+ (getOrgno() == null ? 0 : this.getOrgno().hashCode());
		return result;
	}

}