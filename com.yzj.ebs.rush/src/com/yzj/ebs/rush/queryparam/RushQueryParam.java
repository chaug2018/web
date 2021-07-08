package com.yzj.ebs.rush.queryparam;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;

/**
 * 
 *创建于:2012-11-15<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 催收处理查询参数类
 * @author 施江敏
 * @version 1.0.0
 */
public class RushQueryParam extends PageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1111545121533711937L;
	private String idCenter;//清算中心
	private String idBank;//支行
	private String idBank1;
	private String rushFlag;//催收标志
	private String rushMethod;//催收方式
	private String voucherNo;//账单编号
	private String docDate;//对账日期
	private String sendModeFlag; //发送方式
	public String getSendModeFlag() {
		return sendModeFlag;
	}
	public void setSendModeFlag(String sendModeFlag) {
		this.sendModeFlag = sendModeFlag;
	}
	public String getidCenter() {
		return idCenter;
	}
	public void setidCenter(String idCenter) {
		this.idCenter = idCenter;
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
	public String getRushFlag() {
		return rushFlag;
	}
	public void setRushFlag(String rushFlag) {
		this.rushFlag = rushFlag;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getRushMethod() {
		return rushMethod;
	}
	public void setRushMethod(String rushMethod) {
		this.rushMethod = rushMethod;
	}

}
