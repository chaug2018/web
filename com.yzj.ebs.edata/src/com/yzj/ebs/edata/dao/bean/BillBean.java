package com.yzj.ebs.edata.dao.bean;

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
	private static final long serialVersionUID = 1L;
	private int autoid;
	private String customerid;
	private String idbank;
	private String sendmode;
	private String address;
	private String accno;
	private String docdate;
	private String voucherno;
	private int num;
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
}
