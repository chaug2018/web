/**
 * 创建于:2012-10-15<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 验印对象pojo
 *
 * @author LiuQF
 * @version 1.0
 */
package com.yzj.ebs.manuprove.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ManuProveJson {
	public String sealDBIp; // 验印服务器地址
	public String sealDBPort; // 验印服务器端口
	public String voucherNo; // 凭证号码
	public String operCode; // 操作柜员
	public String docDate; // 出票日期
	public String imageFile; // 正面图像路径
	public String uReason; // 退票理由
	public String accNoList; // 账号列表
	public String sealMode; // 验印模式
	public long docID;
	public String accNo;	//第一个账号
	public String credit;	
	private String message;
	private String taskId;
	

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getSealDBIp() {
		return sealDBIp;
	}

	public void setSealDBIp(String sealDBIp) {
		this.sealDBIp = sealDBIp;
	}

	public String getSealDBPort() {
		return sealDBPort;
	}

	public void setSealDBPort(String sealDBPort) {
		this.sealDBPort = sealDBPort;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getOperCode() {
		return operCode;
	}

	public void setOperCode(String operCode) {
		this.operCode = operCode;
	}

	public String getDocDate() {
		return docDate;
	}
	/**
	 * 为了解决调去新验印章
	 * @param docDate
	 */
	public void setDocDate(String docDate) {
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		this.docDate = format.format(new Date());
		//this.docDate = docDate;
	}

	public String getImageFile() {
		return imageFile;
	}

	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public String getuReason() {
		return uReason;
	}

	public void setuReason(String uReason) {
		this.uReason = uReason;
	}

	public String getAccNoList() {
		return accNoList;
	}

	public void setAccNoList(String accNoList) {
		this.accNoList = accNoList;
	}

	public String getSealMode() {
		return sealMode;
	}

	public void setSealMode(String sealMode) {
		this.sealMode = sealMode;
	}

	public long getDocID() {
		return docID;
	}

	public void setDocID(long docID) {
		this.docID = docID;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}
