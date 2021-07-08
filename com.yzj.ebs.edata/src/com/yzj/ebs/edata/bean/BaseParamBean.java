package com.yzj.ebs.edata.bean;

import java.io.Serializable;

/**
 *创建于:2012-10-10<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 网点bean
 * @author Lif
 * @version 1.0.0
 */
public class BaseParamBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3401187427103689639L;
	/**
	 * 
	 */
	public String idbranch;  //分行
	public String idcenter;  //对账中心
	public String cname;     //机构名称
	public String nlevel;    //机构级别
	
	
	public String subnoc;     //科目号
	public String subnoctype; //科目类型
	public String memo;       //客户中文描述
	public String ischeck;    //是否参加对账
	
	public double maxSingleCredit_m;   //最大单笔发生额（月）
	public double maxSingleCredit_b_m; //最大单笔借方发生额（月）
	public double maxSingleCredit_l_m; //最大单笔贷方发生额（月）
	public double totalAmount_b_m;  //借方累计发生额（月）
	public double totalAmount_l_m;  //贷方累计发生额（月）
	public double totalAmount_m;    //累计发生额（月）(*)
	public double totalTimes_b_m;   //借方累计发生笔数（月）
	public double totalTimes_l_m;   //贷方方累计发生笔数（月）
	public double totalTimes_m;     //累计发生笔数（月）(*)
	public double balance_m;        //余额（月）
	public double avBalance_m;      //平均余额（月）(*)
	
	public double maxSingleCredit_s;   //最大单笔发生额（季）
	public double maxSingleCredit_b_s; //最大单笔借方发生额（季）
	public double maxSingleCredit_l_s; //最大单笔贷方发生额（季）
	public double totalAmount_b_s;  //借方累计发生额（季）
	public double totalAmount_l_s;  //贷方累计发生额（季）
	public double totalAmount_s;    //累计发生额（季）(*)
	public double totalTimes_b_s;   //借方累计发生笔数（季）
	public double totalTimes_l_s;   //贷方方累计发生笔数（季）
	public double totalTimes_s;     //累计发生笔数（季）(*)
	public double balance_s;        //余额（季）
	public double avBalance_s;      //平均余额（季）(*)
	
	public String phone;       //电话
	public String address;     //对账中心地址
	
	public double balancepercent;     //余额21对账规则比率
	public double totalamountpercent;     //累计发生额21对账规则比率
	


	
	public double getBalancepercent() {
		return balancepercent;
	}
	public void setBalancepercent(double balancepercent) {
		this.balancepercent = balancepercent;
	}
	public double getTotalamountpercent() {
		return totalamountpercent;
	}
	public void setTotalamountpercent(double totalamountpercent) {
		this.totalamountpercent = totalamountpercent;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIdbranch() {
		return idbranch;
	}
	public void setIdbranch(String idbranch) {
		this.idbranch = idbranch;
	}
	public String getIdcenter() {
		return idcenter;
	}
	public void setIdcenter(String idcenter) {
		this.idcenter = idcenter;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getNlevel() {
		return nlevel;
	}
	public void setNlevel(String nlevel) {
		this.nlevel = nlevel;
	}
	public String getSubnoc() {
		return subnoc;
	}
	public void setSubnoc(String subnoc) {
		this.subnoc = subnoc;
	}
	public String getSubnoctype() {
		return subnoctype;
	}
	public void setSubnoctype(String subnoctype) {
		this.subnoctype = subnoctype;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getIscheck() {
		return ischeck;
	}
	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	public double getTotalAmount_m() {
		return totalAmount_m;
	}
	public void setTotalAmount_m(double totalAmount_m) {
		this.totalAmount_m = totalAmount_m;
	}
	public double getMaxSingleCredit_m() {
		return maxSingleCredit_m;
	}
	public void setMaxSingleCredit_m(double maxSingleCredit_m) {
		this.maxSingleCredit_m = maxSingleCredit_m;
	}
	public double getTotalTimes_m() {
		return totalTimes_m;
	}
	public void setTotalTimes_m(double totalTimes_m) {
		this.totalTimes_m = totalTimes_m;
	}
	public double getBalance_m() {
		return balance_m;
	}
	public void setBalance_m(double balance_m) {
		this.balance_m = balance_m;
	}
	public double getAvBalance_m() {
		return avBalance_m;
	}
	public void setAvBalance_m(double avBalance_m) {
		this.avBalance_m = avBalance_m;
	}
	public double getTotalAmount_s() {
		return totalAmount_s;
	}
	public void setTotalAmount_s(double totalAmount_s) {
		this.totalAmount_s = totalAmount_s;
	}
	public double getMaxSingleCredit_s() {
		return maxSingleCredit_s;
	}
	public void setMaxSingleCredit_s(double maxSingleCredit_s) {
		this.maxSingleCredit_s = maxSingleCredit_s;
	}
	public double getTotalTimes_s() {
		return totalTimes_s;
	}
	public void setTotalTimes_s(double totalTimes_s) {
		this.totalTimes_s = totalTimes_s;
	}
	public double getAvBalance_s() {
		return avBalance_s;
	}
	public void setAvBalance_s(double avBalance_s) {
		this.avBalance_s = avBalance_s;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public double getMaxSingleCredit_b_m() {
		return maxSingleCredit_b_m;
	}
	public void setMaxSingleCredit_b_m(double maxSingleCredit_b_m) {
		this.maxSingleCredit_b_m = maxSingleCredit_b_m;
	}
	public double getMaxSingleCredit_l_m() {
		return maxSingleCredit_l_m;
	}
	public void setMaxSingleCredit_l_m(double maxSingleCredit_l_m) {
		this.maxSingleCredit_l_m = maxSingleCredit_l_m;
	}
	public double getTotalAmount_b_m() {
		return totalAmount_b_m;
	}
	public void setTotalAmount_b_m(double totalAmount_b_m) {
		this.totalAmount_b_m = totalAmount_b_m;
	}
	public double getTotalAmount_l_m() {
		return totalAmount_l_m;
	}
	public void setTotalAmount_l_m(double totalAmount_l_m) {
		this.totalAmount_l_m = totalAmount_l_m;
	}
	public double getTotalTimes_b_m() {
		return totalTimes_b_m;
	}
	public void setTotalTimes_b_m(double totalTimes_b_m) {
		this.totalTimes_b_m = totalTimes_b_m;
	}
	public double getTotalTimes_l_m() {
		return totalTimes_l_m;
	}
	public void setTotalTimes_l_m(double totalTimes_l_m) {
		this.totalTimes_l_m = totalTimes_l_m;
	}
	public double getMaxSingleCredit_b_s() {
		return maxSingleCredit_b_s;
	}
	public void setMaxSingleCredit_b_s(double maxSingleCredit_b_s) {
		this.maxSingleCredit_b_s = maxSingleCredit_b_s;
	}
	public double getMaxSingleCredit_l_s() {
		return maxSingleCredit_l_s;
	}
	public void setMaxSingleCredit_l_s(double maxSingleCredit_l_s) {
		this.maxSingleCredit_l_s = maxSingleCredit_l_s;
	}
	public double getTotalAmount_b_s() {
		return totalAmount_b_s;
	}
	public void setTotalAmount_b_s(double totalAmount_b_s) {
		this.totalAmount_b_s = totalAmount_b_s;
	}
	public double getTotalAmount_l_s() {
		return totalAmount_l_s;
	}
	public void setTotalAmount_l_s(double totalAmount_l_s) {
		this.totalAmount_l_s = totalAmount_l_s;
	}
	public double getTotalTimes_b_s() {
		return totalTimes_b_s;
	}
	public void setTotalTimes_b_s(double totalTimes_b_s) {
		this.totalTimes_b_s = totalTimes_b_s;
	}
	public double getTotalTimes_l_s() {
		return totalTimes_l_s;
	}
	public void setTotalTimes_l_s(double totalTimes_l_s) {
		this.totalTimes_l_s = totalTimes_l_s;
	}
	public double getBalance_s() {
		return balance_s;
	}
	public void setBalance_s(double balance_s) {
		this.balance_s = balance_s;
	}

}
