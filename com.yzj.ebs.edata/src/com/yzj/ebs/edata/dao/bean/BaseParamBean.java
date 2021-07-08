package com.yzj.ebs.edata.dao.bean;

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
	private static final long serialVersionUID = 1L;
	public String idbranch; //分行
	public String idcenter;//对账中心
	public String cname;//机构名称
	public String nlevel;//机构级别
	
	public String subnoc;//科目号
	public String subnoctype;//科目类型
	public String memo;//客户中文描述
	public String ischeck;//是否参加对账
	
	public int msumcount;//累计发生笔数（月）(*) 
	public double msumcredit;//累计发生额
	public double macredit;//单笔发生额
	public double mcredit;//存款余额
	public double mavrcredit;//平均余额（月）(*)  --日均余额
	//日均余额
	
	public double getMavrcredit() {
		return mavrcredit;
	}
	public void setMavrcredit(double mavrcredit) {
		this.mavrcredit = mavrcredit;
	}
	public String getSubnoc() {
		return subnoc;
	}
	public int getMsumcount() {
		return msumcount;
	}
	public void setMsumcount(int msumcount) {
		this.msumcount = msumcount;
	}
	public double getMsumcredit() {
		return msumcredit;
	}
	public void setMsumcredit(double msumcredit) {
		this.msumcredit = msumcredit;
	}
	public double getMacredit() {
		return macredit;
	}
	public void setMacredit(double macredit) {
		this.macredit = macredit;
	}
	public double getMcredit() {
		return mcredit;
	}
	public void setMcredit(double mcredit) {
		this.mcredit = mcredit;
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


}
