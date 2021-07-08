package com.yzj.ebs.database;


/**
 * 创建于:2012-9-20<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账户信息类
 *
 * @author LiuQiFeng
 * @version 1.0.0
 */

public class SealLog implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = 2951443872614600196L;
	private Long autoId;
	private String accNo;
	private String voucherNo;
	private Long docId;
	private Integer ljyjdm;
	private Long yjdm;
	private Integer result;
	private Integer sealMode;
	private String opCode;
	private String opDate;
	private String opTime;
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public Integer getLjyjdm() {
		return ljyjdm;
	}
	public void setLjyjdm(Integer ljyjdm) {
		this.ljyjdm = ljyjdm;
	}
	public Long getYjdm() {
		return yjdm;
	}
	public void setYjdm(Long yjdm) {
		this.yjdm = yjdm;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public Integer getSealMode() {
		return sealMode;
	}
	public void setSealMode(Integer sealMode) {
		this.sealMode = sealMode;
	}
	public String getOpCode() {
		return opCode;
	}
	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}