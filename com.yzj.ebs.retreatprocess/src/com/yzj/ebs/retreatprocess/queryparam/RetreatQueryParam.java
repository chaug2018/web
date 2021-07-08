package com.yzj.ebs.retreatprocess.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 
 *创建于:2012-11-15<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 退信处理参数类
 * @author 施江敏
 * @version 1.0.0
 */
public class RetreatQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 8597701730290902674L;
	private String idCenter;
	private String idBranch;
	private String idBank;
	private String idBank1;
	private String docDate;//对账日期
	private String voucherNo;//账单编号
	private String urgeDate;//催收日期
	private String urgeType;//处理类型

	private String urgeFlag; // 处理状态

	private String sendModeFlag; //发送方式
	
	public String getSendModeFlag() {
		return sendModeFlag;
	}

	public void setSendModeFlag(String sendModeFlag) {
		this.sendModeFlag = sendModeFlag;
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

	public String getIdBank1() {
		return idBank1;
	}

	public void setIdBank1(String idBank1) {
		this.idBank1 = idBank1;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getUrgeDate() {
		return urgeDate;
	}

	public void setUrgeDate(String urgeDate) {
		this.urgeDate = urgeDate;
	}

	public String getUrgeType() {
		return urgeType;
	}

	public void setUrgeType(String urgeType) {
		this.urgeType = urgeType;
	}

	public String getUrgeFlag() {
		return urgeFlag;
	}

	public void setUrgeFlag(String urgeFlag) {
		this.urgeFlag = urgeFlag;
	}

}
