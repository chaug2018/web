package com.yzj.ebs.edata.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 
 *创建于:2012-11-24<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账单批量打印的实体类
 * @author Lif
 * @version 1.0.0
 */
public class BatchPrintBean implements Serializable{	
	
	private static final long serialVersionUID = 1387743882981540421L;
	private String voucherno="";		// 账单编号
	private String docdate="";			// 对账日期
	private String customerid="";     // 客户号
	private String accname="";			// 账户名称
	private String zip="";				// 邮编
	private String address="";			// 联系地址
	private String linkman="";			// 联系人
	private String phone="";			// 联系电话
	
	
	/** 帐号1 */
	private String accno1="";			// 账号1
	private String subjectNo1="";		// 账户类型1
	private String idbank1="";			// 行号1
	private String bankname1="";		// 开户行名1
	private String credit1="";			// 金额1
	private String currtypeCN1="";		// 币种1
	/** 帐号2 */
	private String accno2="";			// 账号2
	private String subjectNo2="";		// 账户类型2
	private String idbank2="";			// 行号2
	private String bankname2="";		// 开户行名2
	private String credit2="";			// 金额2
	private String currtypeCN2="";		// 币种2
	/** 帐号3 */
	private String accno3="";			// 账号3
	private String subjectNo3="";		// 账户类型3
	private String idbank3="";			// 行号3
	private String bankname3="";		// 开户行名3
	private String credit3="";			// 金额3
	private String currtypeCN3="";		// 币种3
	/** 帐号4 */
	private String accno4="";			// 账号4
	private String subjectNo4="";		// 账户类型4
	private String idbank4="";			// 行号4
	private String bankname4="";		// 开户行名4
	private String credit4="";			// 金额4
	private String currtypeCN4="";		// 币种4
	/** 帐号5 */
	private String accno5="";			// 账号5
	private String subjectNo5="";		// 账户类型5
	private String idbank5="";			// 行号5
	private String bankname5="";		// 开户行名5
	private String credit5="";			// 金额5
	private String currtypeCN5="";		// 币种5
	
	private String sealmode="";		// 验印模式
	private String centerphone; //对账中心电话
	private String centeraddress; //对账中心地址
	private String sendAddress;   //对账单发送地址
				
	private Map<String,String> map;
	private List<Map<String,String>> accDetails;	//明细
	private String idCenterName;  //分行名称

	/** ============ 扩展字段 ===================*/
	private boolean selected;

	public BatchPrintBean() {		
	}

	public BatchPrintBean(String voucherno, String docdate, String customerid,
			String accname, String zip, String address, String linkman,
			String phone, String accno1, String subjectNo1, String idbank1,
			String bankname1, String credit1, String currtypeCN1,
			String accno2, String subjectNo2, String idbank2, String bankname2,
			String credit2, String currtypeCN2, String accno3, String subjectNo3,
			String idbank3, String bankname3, String credit3,
			String currtypeCN3, String accno4, String subjectNo4, String idbank4,
			String bankname4, String credit4, String currtypeCN4,
			String accno5, String subjectNo5, String idbank5, String bankname5,
			String credit5, String currtypeCN5,String sealmode, boolean selected,String centerphone,String centeraddress) {
		super();
		this.voucherno = voucherno;
		this.docdate = docdate;
		this.customerid = customerid;
		this.accname = accname;
		this.zip = zip;
		this.address = address;
		this.linkman = linkman;
		this.phone = phone;
		this.accno1 = accno1;
		this.subjectNo1 = subjectNo1;
		this.idbank1 = idbank1;
		this.bankname1 = bankname1;
		this.credit1 = credit1;
		this.currtypeCN1 = currtypeCN1;
		this.accno2 = accno2;
		this.subjectNo2 = subjectNo2;
		this.idbank2 = idbank2;
		this.bankname2 = bankname2;
		this.credit2 = credit2;
		this.currtypeCN2 = currtypeCN2;
		this.accno3 = accno3;
		this.subjectNo3 = subjectNo3;
		this.idbank3 = idbank3;
		this.bankname3 = bankname3;
		this.credit3 = credit3;
		this.currtypeCN3 = currtypeCN3;
		this.accno4 = accno4;
		this.subjectNo4 = subjectNo4;
		this.idbank4 = idbank4;
		this.bankname4 = bankname4;
		this.credit4 = credit4;
		this.currtypeCN4 = currtypeCN4;
		this.accno5 = accno5;
		this.subjectNo5 = subjectNo5;
		this.idbank5 = idbank5;
		this.bankname5 = bankname5;
		this.credit5 = credit5;
		this.currtypeCN5 = currtypeCN5;
		this.sealmode = sealmode;
		this.selected = selected;
		this.centerphone=centerphone;
		this.centeraddress=centeraddress;
	}


	public String getAccno1() {
		return accno1;
	}

	public void setAccno1(String accno1) {
		this.accno1 = accno1;
	}

	public String getAccno2() {
		return accno2;
	}

	public void setAccno2(String accno2) {
		this.accno2 = accno2;
	}

	public String getAccno3() {
		return accno3;
	}

	public void setAccno3(String accno3) {
		this.accno3 = accno3;
	}

	public String getAccno4() {
		return accno4;
	}

	public void setAccno4(String accno4) {
		this.accno4 = accno4;
	}

	public String getAccno5() {
		return accno5;
	}

	public void setAccno5(String accno5) {
		this.accno5 = accno5;
	}

	/**
	 * @return the voucherno
	 */
	public String getVoucherno() {
		return voucherno;
	}

	/**
	 * @param voucherno the voucherno to set
	 */
	public void setVoucherno(String voucherno) {
		this.voucherno = voucherno;
	}

	/**
	 * @return the docdate
	 */
	public String getDocdate() {
		return docdate;
	}

	/**
	 * @param docdate the docdate to set
	 */
	public void setDocdate(String docdate) {
		this.docdate = docdate;
	}

	/**
	 * @return the customerid
	 */
	public String getCustomerid() {
		return customerid;
	}

	/**
	 * @param customerid the customerid to set
	 */
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	/**
	 * @return the accname
	 */
	public String getAccname() {
		return accname;
	}

	/**
	 * @param accname the accname to set
	 */
	public void setAccname(String accname) {
		this.accname = accname;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the linkman
	 */
	public String getLinkman() {
		return linkman;
	}

	/**
	 * @param linkman the linkman to set
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the subjectNo1
	 */
	public String getSubjectNo1() {
		return subjectNo1;
	}

	/**
	 * @param subjectNo1 the subjectNo1 to set
	 */
	public void setSubjectNo1(String subjectNo1) {
		this.subjectNo1 = subjectNo1;
	}

	/**
	 * @return the idbank1
	 */
	public String getIdbank1() {
		return idbank1;
	}

	/**
	 * @param idbank1 the idbank1 to set
	 */
	public void setIdbank1(String idbank1) {
		this.idbank1 = idbank1;
	}

	/**
	 * @return the bankname1
	 */
	public String getBankname1() {
		return bankname1;
	}

	/**
	 * @param bankname1 the bankname1 to set
	 */
	public void setBankname1(String bankname1) {
		this.bankname1 = bankname1;
	}

	/**
	 * @return the credit1
	 */
	public String getCredit1() {
		return credit1;
	}

	/**
	 * @param credit1 the credit1 to set
	 */
	public void setCredit1(String credit1) {
		this.credit1 = credit1;
	}

	/**
	 * @return the currtypeCN1
	 */
	public String getCurrtypeCN1() {
		return currtypeCN1;
	}

	/**
	 * @param currtypeCN1 the currtypeCN1 to set
	 */
	public void setCurrtypeCN1(String currtypeCN1) {
		this.currtypeCN1 = currtypeCN1;
	}

	/**
	 * @return the subjectNo2
	 */
	public String getSubjectNo2() {
		return subjectNo2;
	}

	/**
	 * @param subjectNo2 the subjectNo2 to set
	 */
	public void setSubjectNo2(String subjectNo2) {
		this.subjectNo2 = subjectNo2;
	}

	/**
	 * @return the idbank2
	 */
	public String getIdbank2() {
		return idbank2;
	}

	/**
	 * @param idbank2 the idbank2 to set
	 */
	public void setIdbank2(String idbank2) {
		this.idbank2 = idbank2;
	}

	/**
	 * @return the bankname2
	 */
	public String getBankname2() {
		return bankname2;
	}

	/**
	 * @param bankname2 the bankname2 to set
	 */
	public void setBankname2(String bankname2) {
		this.bankname2 = bankname2;
	}

	/**
	 * @return the credit2
	 */
	public String getCredit2() {
		return credit2;
	}

	/**
	 * @param credit2 the credit2 to set
	 */
	public void setCredit2(String credit2) {
		this.credit2 = credit2;
	}

	/**
	 * @return the currtypeCN2
	 */
	public String getCurrtypeCN2() {
		return currtypeCN2;
	}

	/**
	 * @param currtypeCN2 the currtypeCN2 to set
	 */
	public void setCurrtypeCN2(String currtypeCN2) {
		this.currtypeCN2 = currtypeCN2;
	}

	/**
	 * @return the subjectNo3
	 */
	public String getSubjectNo3() {
		return subjectNo3;
	}

	/**
	 * @param subjectNo3 the subjectNo3 to set
	 */
	public void setSubjectNo3(String subjectNo3) {
		this.subjectNo3 = subjectNo3;
	}

	/**
	 * @return the idbank3
	 */
	public String getIdbank3() {
		return idbank3;
	}

	/**
	 * @param idbank3 the idbank3 to set
	 */
	public void setIdbank3(String idbank3) {
		this.idbank3 = idbank3;
	}

	/**
	 * @return the bankname3
	 */
	public String getBankname3() {
		return bankname3;
	}

	/**
	 * @param bankname3 the bankname3 to set
	 */
	public void setBankname3(String bankname3) {
		this.bankname3 = bankname3;
	}

	/**
	 * @return the credit3
	 */
	public String getCredit3() {
		return credit3;
	}

	/**
	 * @param credit3 the credit3 to set
	 */
	public void setCredit3(String credit3) {
		this.credit3 = credit3;
	}

	/**
	 * @return the currtypeCN3
	 */
	public String getCurrtypeCN3() {
		return currtypeCN3;
	}

	/**
	 * @param currtypeCN3 the currtypeCN3 to set
	 */
	public void setCurrtypeCN3(String currtypeCN3) {
		this.currtypeCN3 = currtypeCN3;
	}

	/**
	 * @return the subjectNo4
	 */
	public String getSubjectNo4() {
		return subjectNo4;
	}

	/**
	 * @param subjectNo4 the subjectNo4 to set
	 */
	public void setSubjectNo4(String subjectNo4) {
		this.subjectNo4 = subjectNo4;
	}

	/**
	 * @return the idbank4
	 */
	public String getIdbank4() {
		return idbank4;
	}

	/**
	 * @param idbank4 the idbank4 to set
	 */
	public void setIdbank4(String idbank4) {
		this.idbank4 = idbank4;
	}

	/**
	 * @return the bankname4
	 */
	public String getBankname4() {
		return bankname4;
	}

	/**
	 * @param bankname4 the bankname4 to set
	 */
	public void setBankname4(String bankname4) {
		this.bankname4 = bankname4;
	}

	/**
	 * @return the credit4
	 */
	public String getCredit4() {
		return credit4;
	}

	/**
	 * @param credit4 the credit4 to set
	 */
	public void setCredit4(String credit4) {
		this.credit4 = credit4;
	}

	/**
	 * @return the currtypeCN4
	 */
	public String getCurrtypeCN4() {
		return currtypeCN4;
	}

	/**
	 * @param currtypeCN4 the currtypeCN4 to set
	 */
	public void setCurrtypeCN4(String currtypeCN4) {
		this.currtypeCN4 = currtypeCN4;
	}
	/**
	 * @return the subjectNo5
	 */
	public String getSubjectNo5() {
		return subjectNo5;
	}

	/**
	 * @param subjectNo5 the subjectNo5 to set
	 */
	public void setSubjectNo5(String subjectNo5) {
		this.subjectNo5 = subjectNo5;
	}

	/**
	 * @return the idbank5
	 */
	public String getIdbank5() {
		return idbank5;
	}

	/**
	 * @param idbank5 the idbank5 to set
	 */
	public void setIdbank5(String idbank5) {
		this.idbank5 = idbank5;
	}

	/**
	 * @return the bankname5
	 */
	public String getBankname5() {
		return bankname5;
	}

	/**
	 * @param bankname5 the bankname5 to set
	 */
	public void setBankname5(String bankname5) {
		this.bankname5 = bankname5;
	}

	/**
	 * @return the credit5
	 */
	public String getCredit5() {
		return credit5;
	}

	/**
	 * @param credit5 the credit5 to set
	 */
	public void setCredit5(String credit5) {
		this.credit5 = credit5;
	}

	/**
	 * @return the currtypeCN5
	 */
	public String getCurrtypeCN5() {
		return currtypeCN5;
	}

	/**
	 * @param currtypeCN5 the currtypeCN5 to set
	 */
	public void setCurrtypeCN5(String currtypeCN5) {
		this.currtypeCN5 = currtypeCN5;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
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

	public String getSendAddress() {
		return sendAddress;
	}

	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}

	public List<Map<String, String>> getAccDetails() {
		return accDetails;
	}

	public void setAccDetails(List<Map<String, String>> accDetails) {
		this.accDetails = accDetails;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public String getIdCenterName() {
		return idCenterName;
	}

	public void setIdCenterName(String idCenterName) {
		this.idCenterName = idCenterName;
	}

	public String getSealmode() {
		return sealmode;
	}

	public void setSealmode(String sealmode) {
		this.sealmode = sealmode;
	}


	
}
