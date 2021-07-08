package com.yzj.ebs.database;

/**
 *创建于:2012-11-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账户信息维护日志
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class CustomerSignInfo implements java.io.Serializable {


	/**
	 * 自动生成ID
	 */
	private static final long serialVersionUID = -3804162103360408148L;
	// 签约号
	private Long signID;
	// 签约时间
    private String signdate;
    // 客户号
    private String custid;
    // 客户名
    private String accname;
    // 验印方式
    private String sealmode;
    // 地址
    private String address;
    // 邮编
    private String zip;
    // 联系人
    private String linkman;
    // 电话
    private String phone;
    // 机构号
    private String brcNo;
    // 账号
    private String accNo;
    // 柜员号
    private String peopleNo;

	private String mobilephone;
    private String sendMode;
    // 柜面流水
    private String counterID;
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getAccname() {
		return accname;
	}
	public void setAccname(String accname) {
		this.accname = accname;
	}
	public String getSealmode() {
		return sealmode;
	}
	public void setSealmode(String sealmode) {
		this.sealmode = sealmode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getSendMode() {
		return sendMode;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public String getCounterID() {
		return counterID;
	}
	public void setCounterID(String counterID) {
		this.counterID = counterID;
	}
	public Long getSignID() {
		return signID;
	}
	public void setSignID(Long signID) {
		this.signID = signID;
	}
	 public String getBrcNo() {
			return brcNo;
		}
		public void setBrcNo(String brcNo) {
			this.brcNo = brcNo;
		}
		 
		public String getAccNo() {
			return accNo;
		}
		public void setAccNo(String accNo) {
			this.accNo = accNo;
		}
		public String getPeopleNo() {
			return peopleNo;
		}
		public void setPeopleNo(String peopleNo) {
			this.peopleNo = peopleNo;
		}
}