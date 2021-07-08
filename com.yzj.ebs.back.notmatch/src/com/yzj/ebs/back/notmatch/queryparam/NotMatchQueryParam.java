package com.yzj.ebs.back.notmatch.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2012-10-10<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达项查询参数
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class NotMatchQueryParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3202759154840004978L;

	private String idCenter;	//对账中心

	private String idBranch;	//支行号

	private String idBank; // 网点号
	
	private String idBank1; // 网点号

	private String voucherNo; // 对账单编号

	private String accNo; // 账号

	private String docDate; // 对账日期

	private String result; // 余额状态（相符/不符）

	private String direction; // 未达方向

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

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
