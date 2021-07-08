package com.yzj.ebs.edata.bean;

import java.io.Serializable;

/**
 *创建于:2012-11-01<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账单bean
 * @author Lif
 * @version 1.0.0
 */
public class BillBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4542645261549453129L;
	/**
	 * 
	 */
	//ebs_bill
	private int autoid;
	private String customerid;
	private String idbank;
	private String sendmode;
	private String address;
	private String accno;
	private String docdate;
	private String voucherno;
	private int num;
	
	//kub_user
	private String peoplecode;
	private String peoplename;
	private String orgno;
	private String sex;
	
	//EBS_tempdetailmaindata
	private String idcenterno;
	
	//PARAM_SUBNOC
	private String subnoc;//科目号
	private String subnoctype;//科目类型
	
	public String getIdcenterno() {
		return idcenterno;
	}
	public void setIdcenterno(String idcenterno) {
		this.idcenterno = idcenterno;
	}
	public String getPeoplecode() {
		return peoplecode;
	}
	public void setPeoplecode(String peoplecode) {
		this.peoplecode = peoplecode;
	}
	public String getPeoplename() {
		return peoplename;
	}
	public void setPeoplename(String peoplename) {
		this.peoplename = peoplename;
	}
	public String getOrgno() {
		return orgno;
	}
	public void setOrgno(String orgno) {
		this.orgno = orgno;
	}
	
	public int getAutoid() {
		return autoid;
	}
	public void setAutoid(int autoid) {
		this.autoid = autoid;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getIdbank() {
		return idbank;
	}
	public void setIdbank(String idbank) {
		this.idbank = idbank;
	}
	public String getSendmode() {
		return sendmode;
	}
	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAccno() {
		return accno;
	}
	public void setAccno(String accno) {
		this.accno = accno;
	}
	public String getDocdate() {
		return docdate;
	}
	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}
	public String getVoucherno() {
		return voucherno;
	}
	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	
}
