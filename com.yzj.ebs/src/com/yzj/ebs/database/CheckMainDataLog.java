package com.yzj.ebs.database;

/**
 *创建于:2012-1-11<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 对账主数据操作日志
 * @author 陈林江
 * @version 1.0.0
 */
public class CheckMainDataLog implements java.io.Serializable {



	private static final long serialVersionUID = 6965069823930046829L;
	private Long autoId;
	private String voucherNo;
	private String idCenter;
	private String idBranch;
	private String idBank;
	private Integer opMode;
	private String opDesc;
	private String opCode;
	private String opDate;
	private String chnOpMode;
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public Integer getOpMode() {
		return opMode;
	}
	public void setOpMode(Integer opMode) {
		this.opMode = opMode;
	}
	public String getOpDesc() {
		return opDesc;
	}
	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
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
	public String getChnOpMode() {
		return chnOpMode;
	}
	public void setChnOpMode(String chnOpMode) {
		this.chnOpMode = chnOpMode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
}