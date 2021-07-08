package com.yzj.ebs.common;


/**
 * ParamIdcenter entity. @author MyEclipse Persistence Tools
 */

public class IdCenterParam implements java.io.Serializable {
	private static final long serialVersionUID = -5094449354080241112L;
	private long id;
	private String idCenterNo;
	private String idCenterName;
	private String isCheck;
	private Float maxSingleCredit_m;  //最大单笔发生额(月)
	private Float maxSingleCredit_b_m;  //最大单笔借方发生额(月)
	private Float maxSingleCredit_l_m;  //最大单笔贷方发生额(月)
	private Float totalAmount_b_m;  //借方累计发生额(月)
	private Float totalAmount_l_m;    //贷方累计发生额（月）
	private Float totalAmount_m;      //累计发生额（月）
	private Float totalTimes_b_m;          //借方累计发生笔数（月）
	private Float totalTimes_l_m;      //贷方方累计发生笔数（月）
	private Float totalTimes_m;    //累计发生笔数（月）
	private Float balance_m;      //余额（月）
	private Float avBalance_m;      //平均余额（月）
	private Float maxSingleCredit_s;        //最大单笔借方发生额（季）
	private Float maxSingleCredit_l_s;  //最大单笔贷方发生额（季）
	private Float totalAmount_b_s;       //借方累计发生额（季）
	private Float totalAmount_l_s;     //贷方累计发生额（季）
	private Float totalAmount_s;      //累计发生额（季）(*)
	private Float totalTimes_b_s;          //借方累计发生笔 数（季）
	private Float totalTimes_l_s;           //贷方累计发生笔数（季）
	private Float totalTimes_s;         //累计发生笔数（季）
	private Float banlance_s;             //余额（季）
	private Float avBalance_s;         //平均余额（季）
	private String Address;
	private String zip;
	private String phone;
	private String imageUrl;
	private String storeUrl;
	private String imgSerUser;
	private String imgSerPass;
	private String activeTime;
	private String deactiveTime;
	private String lastUpdatedTime;
	private String updateOperator;
	private String sealType;
	private String notMatchInputType;

	// Constructors

	public String getNotMatchInputType() {
		return notMatchInputType;
	}

	public void setNotMatchInputType(String notMatchInputType) {
		this.notMatchInputType = notMatchInputType;
	}

	/** default constructor */
	public IdCenterParam() {
	}

	/** minimal constructor */
	public IdCenterParam(long id) {
		this.id = id;
	}




	public String getIdCenterNo() {
		return idCenterNo;
	}

	public void setIdCenterNo(String idCenterNo) {
		this.idCenterNo = idCenterNo;
	}

	public String getIdCenterName() {
		return idCenterName;
	}

	public void setIdCenterName(String idCenterName) {
		this.idCenterName = idCenterName;
	}

	public String getIsCheck() {
		return isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	
	

	public Float getMaxSingleCredit_m() {
		return maxSingleCredit_m;
	}

	public void setMaxSingleCredit_m(Float maxSingleCredit_m) {
		this.maxSingleCredit_m = maxSingleCredit_m;
	}

	public Float getMaxSingleCredit_b_m() {
		return maxSingleCredit_b_m;
	}

	public void setMaxSingleCredit_b_m(Float maxSingleCredit_b_m) {
		this.maxSingleCredit_b_m = maxSingleCredit_b_m;
	}

	public Float getMaxSingleCredit_l_m() {
		return maxSingleCredit_l_m;
	}

	public void setMaxSingleCredit_l_m(Float maxSingleCredit_l_m) {
		this.maxSingleCredit_l_m = maxSingleCredit_l_m;
	}

	public Float getTotalAmount_b_m() {
		return totalAmount_b_m;
	}

	public void setTotalAmount_b_m(Float totalAmount_b_m) {
		this.totalAmount_b_m = totalAmount_b_m;
	}

	public Float getTotalAmount_l_m() {
		return totalAmount_l_m;
	}

	public void setTotalAmount_l_m(Float totalAmount_l_m) {
		this.totalAmount_l_m = totalAmount_l_m;
	}

	public Float getTotalAmount_m() {
		return totalAmount_m;
	}

	public void setTotalAmount_m(Float totalAmount_m) {
		this.totalAmount_m = totalAmount_m;
	}

	public Float getTotalTimes_b_m() {
		return totalTimes_b_m;
	}

	public void setTotalTimes_b_m(Float totalTimes_b_m) {
		this.totalTimes_b_m = totalTimes_b_m;
	}

	public Float getTotalTimes_l_m() {
		return totalTimes_l_m;
	}

	public void setTotalTimes_l_m(Float totalTimes_l_m) {
		this.totalTimes_l_m = totalTimes_l_m;
	}

	public Float getTotalTimes_m() {
		return totalTimes_m;
	}

	public void setTotalTimes_m(Float totalTimes_m) {
		this.totalTimes_m = totalTimes_m;
	}

	public Float getBalance_m() {
		return balance_m;
	}

	public void setBalance_m(Float balance_m) {
		this.balance_m = balance_m;
	}

	public Float getAvBalance_m() {
		return avBalance_m;
	}

	public void setAvBalance_m(Float avBalance_m) {
		this.avBalance_m = avBalance_m;
	}

	public Float getMaxSingleCredit_s() {
		return maxSingleCredit_s;
	}

	public void setMaxSingleCredit_s(Float maxSingleCredit_s) {
		this.maxSingleCredit_s = maxSingleCredit_s;
	}

	public Float getMaxSingleCredit_l_s() {
		return maxSingleCredit_l_s;
	}

	public void setMaxSingleCredit_l_s(Float maxSingleCredit_l_s) {
		this.maxSingleCredit_l_s = maxSingleCredit_l_s;
	}

	public Float getTotalAmount_b_s() {
		return totalAmount_b_s;
	}

	public void setTotalAmount_b_s(Float totalAmount_b_s) {
		this.totalAmount_b_s = totalAmount_b_s;
	}

	public Float getTotalAmount_l_s() {
		return totalAmount_l_s;
	}

	public void setTotalAmount_l_s(Float totalAmount_l_s) {
		this.totalAmount_l_s = totalAmount_l_s;
	}

	public Float getTotalAmount_s() {
		return totalAmount_s;
	}

	public void setTotalAmount_s(Float totalAmount_s) {
		this.totalAmount_s = totalAmount_s;
	}

	public Float getTotalTimes_b_s() {
		return totalTimes_b_s;
	}

	public void setTotalTimes_b_s(Float totalTimes_b_s) {
		this.totalTimes_b_s = totalTimes_b_s;
	}

	public Float getTotalTimes_l_s() {
		return totalTimes_l_s;
	}

	public void setTotalTimes_l_s(Float totalTimes_l_s) {
		this.totalTimes_l_s = totalTimes_l_s;
	}

	public Float getTotalTimes_s() {
		return totalTimes_s;
	}

	public void setTotalTimes_s(Float totalTimes_s) {
		this.totalTimes_s = totalTimes_s;
	}

	public Float getBanlance_s() {
		return banlance_s;
	}

	public void setBanlance_s(Float banlance_s) {
		this.banlance_s = banlance_s;
	}

	public Float getAvBalance_s() {
		return avBalance_s;
	}

	public void setAvBalance_s(Float avBalance_s) {
		this.avBalance_s = avBalance_s;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImgSerUser() {
		return imgSerUser;
	}

	public void setImgSerUser(String imgSerUser) {
		this.imgSerUser = imgSerUser;
	}

	public String getImgSerPass() {
		return imgSerPass;
	}

	public void setImgSerPass(String imgSerPass) {
		this.imgSerPass = imgSerPass;
	}

	public String getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(String activeTime) {
		this.activeTime = activeTime;
	}

	public String getDeactiveTime() {
		return deactiveTime;
	}

	public void setDeactiveTime(String deactiveTime) {
		this.deactiveTime = deactiveTime;
	}

	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public String getUpdateOperator() {
		return updateOperator;
	}

	public void setUpdateOperator(String updateOperator) {
		this.updateOperator = updateOperator;
	}

	public String getSealType() {
		return sealType;
	}

	public void setSealType(String sealType) {
		this.sealType = sealType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getStoreUrl() {
		return storeUrl;
	}

	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}	
}