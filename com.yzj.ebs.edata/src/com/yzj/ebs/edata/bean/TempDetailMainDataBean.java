package com.yzj.ebs.edata.bean;

/**
 *创建于:2012-10-15<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 原始主数据bean
 * @author Lif
 * @version 1.0.0
 */
public class TempDetailMainDataBean implements java.io.Serializable {
	
	
	
	private static final long serialVersionUID = 1373645648444840547L;

	protected Long autoid;
	
	/** 规则匹配*/
	private String keytype;
	/** 月末余额 */
	private Double credit;
	/** 月累计发生额 */
	private Double statm;
	/** 月最大发生额*/
	private Double maxatm;
	/** 月均余额 */
	private Double avbal;
	/** 账单编号 */
	private String voucherno;
	/** 账号 */
	private String accno;
	/** 子账号 */
	private String accnson;
	/** 打印序号 */
	private String accnindex;
	/** 账户名称 */
	private String accname;
	/** 对账日期 */
	private String docdate;
	/** 币种*/
	private String currtype;
	/** 发送日期*/
	private String senddate;

	/** 数据处理日期 */
	private String dealdate;
	/** 发送方式 */
	private String sendmode;
	/** 对账频率*/
	private String acccycle;

	/** 行号 */
	private String idbank;
	/** 上级管理行 */
	private String idbranch;
	/** 对账中心 */
	private String idcenterno;
	/**行名*/
	private String bankname;

	/** 联系地址 */
	private String address;
	/** 邮编 */
	private String zip;
	/** 联系人 */
	private String linkman;
	/** 联系电话 */
	private String phone;
	/** 客户号 */
	private String custid;
	/** 签约标记 */
	private String signFlag;
	/**  账户类型 */
	protected String acctype; 
	/**  验印账号 */
	protected String sealaccno; 
	/** 科目号 */
	protected String subnoc;
	/**  重点账户标记*/
	protected String keyflag;
	/**  开户日期*/
	protected String opendate;
	/**  存贷标记*/
	protected String cdebflag;
	/**  产品号*/
	protected String prodtype;
	/**  产品号描述*/
	protected String proddesc;
	private String faceflafg;//面对面标志
	// 扩展字段
	/** 币种*/
	private String currtypeCN;
	/** 对账频率*/
	private String acccycleCN; 
	/** 签约标记 */
	private String signFlagCN;
	/**  账户类型 */
	protected String acctypeCN; 
	/**  重点账户标记*/
	protected String keyflagCN;
	/**  存贷标记*/
	protected String cdebflagCN;
	
	private String faceflafgCN;
	
	/**
	 * 
	 */
	public TempDetailMainDataBean() {
	}
	/**
	 * @param autoid
	 * @param keytype
	 * @param credit
	 * @param statm
	 * @param maxatm
	 * @param avbal
	 * @param voucherno
	 * @param accno
	 * @param accnson
	 * @param accname
	 * @param docdate
	 * @param currtype
	 * @param senddate
	 * @param dealdate
	 * @param dzpl
	 * @param idbank
	 * @param idbranch
	 * @param idcenterno
	 * @param bankname
	 * @param address
	 * @param zip
	 * @param linkman
	 * @param phone
	 * @param custid
	 * @param signFlag
	 * @param acctype
	 * @param subnoc
	 * @param keyflag
	 * @param opendate
	 * @param cdebflag
	 * @param prodtype
	 * @param proddesc
	 * @param currtypeCN
	 * @param dzplCN
	 * @param signFlagCN
	 * @param acctypeCN
	 * @param keyflagCN
	 * @param cdebflagCN
	 */
	public TempDetailMainDataBean(Long autoid, String keytype, Double credit,
			Double statm, Double maxatm, Double avbal, String voucherno,
			String accno, String accnson, String accname, String docdate,
			String currtype, String senddate, String dealdate, String acccycle,
			String idbank, String idbranch, String idcenterno, String bankname,
			String address, String zip, String linkman, String phone,
			String custid, String signFlag, String acctype, String subnoc,
			String keyflag, String opendate, String cdebflag, String prodtype,
			String proddesc, String currtypeCN, String dzplCN,
			String signFlagCN, String acctypeCN, String keyflagCN,
			String cdebflagCN) {
		this.autoid = autoid;
		this.keytype = keytype;
		this.credit = credit;
		this.statm = statm;
		this.maxatm = maxatm;
		this.avbal = avbal;
		this.voucherno = voucherno;
		this.accno = accno;
		this.accnson = accnson;
		this.accname = accname;
		this.docdate = docdate;
		this.currtype = currtype;
		this.senddate = senddate;
		this.dealdate = dealdate;
		this.acccycle= acccycle;
		this.idbank = idbank;
		this.idbranch = idbranch;
		this.idcenterno = idcenterno;
		this.bankname = bankname;
		this.address = address;
		this.zip = zip;
		this.linkman = linkman;
		this.phone = phone;
		this.custid = custid;
		this.signFlag = signFlag;
		this.acctype = acctype;
		this.subnoc = subnoc;
		this.keyflag = keyflag;
		this.opendate = opendate;
		this.cdebflag = cdebflag;
		this.prodtype = prodtype;
		this.proddesc = proddesc;
		this.currtypeCN = currtypeCN;
		this.acccycleCN = dzplCN;
		this.signFlagCN = signFlagCN;
		this.acctypeCN = acctypeCN;
		this.keyflagCN = keyflagCN;
		this.cdebflagCN = cdebflagCN;
	}
	
	
	
	
	
	
	/**
	 * @return the accnindex
	 */
	public String getAccnindex() {
		return accnindex;
	}
	/**
	 * @param accnindex the accnindex to set
	 */
	public void setAccnindex(String accnindex) {
		this.accnindex = accnindex;
	}
	/**
	 * @return the sealaccno
	 */
	public String getSealaccno() {
		return sealaccno;
	}
	/**
	 * @param sealaccno the sealaccno to set
	 */
	public void setSealaccno(String sealaccno) {
		this.sealaccno = sealaccno;
	}
	/**
	 * @return the sendmode
	 */
	public String getSendmode() {
		return sendmode;
	}
	/**
	 * @param sendmode the sendmode to set
	 */
	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
	}
	/**
	 * @return the autoid
	 */
	public Long getAutoid() {
		return autoid;
	}
	/**
	 * @param autoid the autoid to set
	 */
	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}
	/**
	 * @return the keytype
	 */
	public String getKeytype() {
		return keytype;
	}
	/**
	 * @param keytype the keytype to set
	 */
	public void setKeytype(String keytype) {
		this.keytype = keytype;
	}
	/**
	 * @return the credit
	 */
	public Double getCredit() {
		return credit;
	}
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(Double credit) {
		this.credit = credit;
	}
	/**
	 * @return the statm
	 */
	public Double getStatm() {
		return statm;
	}
	/**
	 * @param statm the statm to set
	 */
	public void setStatm(Double statm) {
		this.statm = statm;
	}
	/**
	 * @return the maxatm
	 */
	public Double getMaxatm() {
		return maxatm;
	}
	/**
	 * @param maxatm the maxatm to set
	 */
	public void setMaxatm(Double maxatm) {
		this.maxatm = maxatm;
	}
	/**
	 * @return the avbal
	 */
	public Double getAvbal() {
		return avbal;
	}
	/**
	 * @param avbal the avbal to set
	 */
	public void setAvbal(Double avbal) {
		this.avbal = avbal;
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
	 * @return the accno
	 */
	public String getAccno() {
		return accno;
	}
	/**
	 * @param accno the accno to set
	 */
	public void setAccno(String accno) {
		this.accno = accno;
	}
	/**
	 * @return the accnson
	 */
	public String getAccnson() {
		return accnson;
	}
	/**
	 * @param accnson the accnson to set
	 */
	public void setAccnson(String accnson) {
		this.accnson = accnson;
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
	 * @return the currtype
	 */
	public String getCurrtype() {
		return currtype;
	}
	/**
	 * @param currtype the currtype to set
	 */
	public void setCurrtype(String currtype) {
		this.currtype = currtype;
	}
	/**
	 * @return the senddate
	 */
	public String getSenddate() {
		return senddate;
	}
	/**
	 * @param senddate the senddate to set
	 */
	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}
	/**
	 * @return the dealdate
	 */
	public String getDealdate() {
		return dealdate;
	}
	/**
	 * @param dealdate the dealdate to set
	 */
	public void setDealdate(String dealdate) {
		this.dealdate = dealdate;
	}

	
	/**
	 * @return the acccycle
	 */
	public String getAcccycle() {
		return acccycle;
	}
	/**
	 * @param acccycle the acccycle to set
	 */
	public void setAcccycle(String acccycle) {
		this.acccycle = acccycle;
	}
	/**
	 * @return the idbank
	 */
	public String getIdbank() {
		return idbank;
	}
	/**
	 * @param idbank the idbank to set
	 */
	public void setIdbank(String idbank) {
		this.idbank = idbank;
	}
	/**
	 * @return the idbranch
	 */
	public String getIdbranch() {
		return idbranch;
	}
	/**
	 * @param idbranch the idbranch to set
	 */
	public void setIdbranch(String idbranch) {
		this.idbranch = idbranch;
	}
	/**
	 * @return the idcenterno
	 */
	public String getIdcenterno() {
		return idcenterno;
	}
	/**
	 * @param idcenterno the idcenterno to set
	 */
	public void setIdcenterno(String idcenterno) {
		this.idcenterno = idcenterno;
	}
	/**
	 * @return the bankname
	 */
	public String getBankname() {
		return bankname;
	}
	/**
	 * @param bankname the bankname to set
	 */
	public void setBankname(String bankname) {
		this.bankname = bankname;
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
	 * @return the custid
	 */
	public String getCustid() {
		return custid;
	}
	/**
	 * @param custid the custid to set
	 */
	public void setCustid(String custid) {
		this.custid = custid;
	}
	/**
	 * @return the signFlag
	 */
	public String getSignFlag() {
		return signFlag;
	}
	/**
	 * @param signFlag the signFlag to set
	 */
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
	/**
	 * @return the acctype
	 */
	public String getAcctype() {
		return acctype;
	}
	/**
	 * @param acctype the acctype to set
	 */
	public void setAcctype(String acctype) {
		this.acctype = acctype;
	}
	/**
	 * @return the subnoc
	 */
	public String getSubnoc() {
		return subnoc;
	}
	/**
	 * @param subnoc the subnoc to set
	 */
	public void setSubnoc(String subnoc) {
		this.subnoc = subnoc;
	}
	/**
	 * @return the keyflag
	 */
	public String getKeyflag() {
		return keyflag;
	}
	/**
	 * @param keyflag the keyflag to set
	 */
	public void setKeyflag(String keyflag) {
		this.keyflag = keyflag;
	}
	/**
	 * @return the opendate
	 */
	public String getOpendate() {
		return opendate;
	}
	/**
	 * @param opendate the opendate to set
	 */
	public void setOpendate(String opendate) {
		this.opendate = opendate;
	}
	/**
	 * @return the cdebflag
	 */
	public String getCdebflag() {
		return cdebflag;
	}
	/**
	 * @param cdebflag the cdebflag to set
	 */
	public void setCdebflag(String cdebflag) {
		this.cdebflag = cdebflag;
	}
	/**
	 * @return the prodtype
	 */
	public String getProdtype() {
		return prodtype;
	}
	/**
	 * @param prodtype the prodtype to set
	 */
	public void setProdtype(String prodtype) {
		this.prodtype = prodtype;
	}
	/**
	 * @return the proddesc
	 */
	public String getProddesc() {
		return proddesc;
	}
	/**
	 * @param proddesc the proddesc to set
	 */
	public void setProddesc(String proddesc) {
		this.proddesc = proddesc;
	}
	/**
	 * @return the currtypeCN
	 */
	public String getCurrtypeCN() {
		return currtypeCN;
	}
	/**
	 * @param currtypeCN the currtypeCN to set
	 */
	public void setCurrtypeCN(String currtypeCN) {
		this.currtypeCN = currtypeCN;
	}


	/**
	 * @return the acccycleCN
	 */
	public String getAcccycleCN() {
		return acccycleCN;
	}
	/**
	 * @param acccycleCN the acccycleCN to set
	 */
	public void setAcccycleCN(String acccycleCN) {
		this.acccycleCN = acccycleCN;
	}
	/**
	 * @return the signFlagCN
	 */
	public String getSignFlagCN() {
		return signFlagCN;
	}
	/**
	 * @param signFlagCN the signFlagCN to set
	 */
	public void setSignFlagCN(String signFlagCN) {
		this.signFlagCN = signFlagCN;
	}
	/**
	 * @return the acctypeCN
	 */
	public String getAcctypeCN() {
		return acctypeCN;
	}
	/**
	 * @param acctypeCN the acctypeCN to set
	 */
	public void setAcctypeCN(String acctypeCN) {
		this.acctypeCN = acctypeCN;
	}
	/**
	 * @return the keyflagCN
	 */
	public String getKeyflagCN() {
		return keyflagCN;
	}
	/**
	 * @param keyflagCN the keyflagCN to set
	 */
	public void setKeyflagCN(String keyflagCN) {
		this.keyflagCN = keyflagCN;
	}
	/**
	 * @return the cdebflagCN
	 */
	public String getCdebflagCN() {
		return cdebflagCN;
	}
	/**
	 * @param cdebflagCN the cdebflagCN to set
	 */
	public void setCdebflagCN(String cdebflagCN) {
		this.cdebflagCN = cdebflagCN;
	}
	/**
	 * @return the faceflafg
	 */
	public String getFaceflafg() {
		return faceflafg;
	}
	/**
	 * @param faceflafg the faceflafg to set
	 */
	public void setFaceflafg(String faceflafg) {
		this.faceflafg = faceflafg;
	}
	/**
	 * @return the faceflafgCN
	 */
	public String getFaceflafgCN() {
		return faceflafgCN;
	}
	/**
	 * @param faceflafgCN the faceflafgCN to set
	 */
	public void setFaceflafgCN(String faceflafgCN) {
		this.faceflafgCN = faceflafgCN;
	}

	
	
}
