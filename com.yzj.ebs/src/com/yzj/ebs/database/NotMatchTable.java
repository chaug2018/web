
package com.yzj.ebs.database;

import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

import com.yzj.ebs.util.UtilBase;


/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 未达账明细
 * @author qinjingfeng
 * @version 1.0.0
 */
public class NotMatchTable implements java.io.Serializable {

	private static final long serialVersionUID = 3617128238783586474L;
	/** 自动编号 */
	private Long autoId;
	/** 流水号 */
	private Long docId;
	/** 账单编号 */
	private String voucherNo;
	/** 对账日期 */
	private String docDate;
	/** 账号 */
	private String accNo;
	/** 未达方向 */
	private String direction;
	/** 交易时间 */
	private String traceDate;
	/** 交易凭证号 */
	private String traceNo;
	/** 交易金额 */
	private Double traceCredit;
	/** 交易金额 获得页面*/
	private String strtraceCredit;
	/** 未达明细结果 */
	private String checkFlag;
	/** 录入柜员 */
	private String inputOpCode;
	/** 录入时间 */
	private String inputOpTime;
	/** 录入备注 */
	private String inputDesc;
	/** 复核柜员 */
	private String checkOpCode;
	/** 复核时间 */
	private String checkOpTime;
	/** 复核处理标志 */
	private String authFlag;
	/** 行号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/** 对账机构 */
	private String bankName;
	/** 最终结果*/
	private String finalCheckFlag;

	// ================ 扩展字段 =====================
	/** 复核备注，废弃未用 */
	private String checkDesc;
	/** 未达方向 */
	private String directionCN;
	/** 未达明细结果 */
	private String resultCN;
	/** 交易金额数值转换 */
	private String traceCreditTran;
	/**交易金额格式化显示值 1000,000,000*/
	private String strTraceCredit;

	public String getStrtraceCredit() {
		return strtraceCredit = UtilBase.formatString(this.getTraceCredit());
	}
	public void setStrtraceCredit(String strtraceCredit) {
		this.strtraceCredit = strtraceCredit;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
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
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}
	public String getTraceDate() {
		return traceDate;
	}
	public void setTraceDate(String traceDate) {
		this.traceDate = traceDate;
	}
	public String getTraceNo() {
		return traceNo;
	}
	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}
	
	public Double getTraceCredit() {
		return traceCredit;
	}
	public void setTraceCredit(Double traceCredit) {
		this.traceCredit = traceCredit;
	}
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public String getInputOpCode() {
		return inputOpCode;
	}
	public void setInputOpCode(String inputOpCode) {
		this.inputOpCode = inputOpCode;
	}
	public String getInputOpTime() {
		return inputOpTime;
	}
	public void setInputOpTime(String inputOpTime) {
		this.inputOpTime = inputOpTime;
	}
	public String getInputDesc() {
		return inputDesc;
	}
	public void setInputDesc(String inputDesc) {
		this.inputDesc = inputDesc;
	}
	public String getCheckOpCode() {
		return checkOpCode;
	}
	public void setCheckOpCode(String checkOpCode) {
		this.checkOpCode = checkOpCode;
	}
	public String getCheckOpTime() {
		return checkOpTime;
	}
	public void setCheckOpTime(String checkOpTime) {
		this.checkOpTime = checkOpTime;
	}
	public String getCheckDesc() {
		return checkDesc;
	}
	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getDirectionCN() {
		return directionCN;
	}
	public void setDirectionCN(String directionCN) {
		this.directionCN = directionCN;
	}
	public String getResultCN() {
		return resultCN;
	}
	public void setResultCN(String resultCN) {
		this.resultCN = resultCN;
	}
	public String getTraceCreditTran() {
		return traceCreditTran;
	}
	public void setTraceCreditTran(String traceCreditTran) {
		this.traceCreditTran = traceCreditTran;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAuthFlag() {
		return authFlag;
	}
	public void setAuthFlag(String authFlag) {
		this.authFlag = authFlag;
	}
	
	public String getStrTraceCredit() {
		strTraceCredit = formatString(this.getTraceCredit());
		if(StringUtils.isNotEmpty(strTraceCredit))
		{
			strTraceCredit = strTraceCredit.replace(",", "");
		}
		return strTraceCredit;
	}
	public void setStrTraceCredit(String strTraceCredit) {
		this.strTraceCredit = strTraceCredit;
	}
	// 将Double类型转换为String类型
	public  String formatString(Double retStr) {
		// 小数格式化，引号中的0.000表示保留小数点后三位（第四位四舍五入）
		DecimalFormat df = new DecimalFormat("#,##0");
		String retString = df.format(retStr);
		return retString;
	}

	public String getFinalCheckFlag() {
		return finalCheckFlag;
	}
	public void setFinalCheckFlag(String finalCheckFlag) {
		this.finalCheckFlag = finalCheckFlag;
	}
}