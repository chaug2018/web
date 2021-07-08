package com.yzj.ebs.database;

import com.yzj.ebs.util.UtilBase;

/**
 * 创建于:2012-9-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细主表，根据账单编号存放账户交易明细
 * 
 * @author qinjingfeng
 * @version 1.0.0
 */
public class AccNoDetailData implements java.io.Serializable {

	private static final long serialVersionUID = 5912516791225803624L;
	protected Long autoid;
	protected Long nTaskId;// 批次号
	protected String docDate;// 对账日期
	protected String voucherNo;// 账单编号
	protected String accNo;// 账号
	protected Double credit;// 余额
	protected String idCenter;// 对账中心
	protected String idBranch;// 支行
	protected String idBank;// 网点
	protected String vouNoType;// 凭证类型
	protected String dcFlag;// 借贷方向
	protected Double traceBal;// 交易金额
	protected String workDate;// 交易日期
	private String vouNo;// 凭证号
	private String accSon;// 子账号
	private long traceNo;// 交易流水号
	private long traceNoSon;// 子交易流水号
	private String strcredit; // 页面展示金额String
	private String strtraceBal;// 页面显示发生额
	protected Double traceCBal;// 贷方发生额
	protected Double traceDBal;// 借方发生额
	private String strTraceCBal; // 页面展示金额String
	private String strtraceDBal; // 页面展示金额String
	private String currType;// 币种
	private String to_Accno;// 对方账号
	private String to_Accname;// 对方户名
	private String abs;// 摘要
	protected String importDate;// 导数日期
	private String checkDate; // 核对日期
	private String checkFlag;// 核对结果
	private long pageNum1;
	private long pageNum2;
	private String traceTime;// 交易时间

	private String dataDate;// 数据日期
	
	private String trace_code;
	
	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public String getCurrType() {
		return currType;
	}

	public void setCurrType(String currType) {
		this.currType = currType;
	}

	public String getTo_Accno() {
		return to_Accno;
	}

	public void setTo_Accno(String to_Accno) {
		this.to_Accno = to_Accno;
	}

	public String getTo_Accname() {
		return to_Accname;
	}

	public void setTo_Accname(String to_Accname) {
		this.to_Accname = to_Accname;
	}

	public String getAbs() {
		if(abs==null){
			return "";
		}else{
			return abs;
		}
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public Long getAutoid() {
		return autoid;
	}

	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}

	public Long getnTaskId() {
		return nTaskId;
	}

	public void setnTaskId(Long nTaskId) {
		this.nTaskId = nTaskId;
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

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}

	public String getStrtraceBal() {
		strtraceBal = UtilBase.formatString(this.getTraceBal());
		return strtraceBal;
	}

	public void setStrtraceBal(String strtraceBal) {
		this.strtraceBal = strtraceBal;
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

	public String getVouNoType() {
		return vouNoType;
	}

	public void setVouNoType(String vouNoType) {
		this.vouNoType = vouNoType;
	}

	public String getDcFlag() {

		return dcFlag;
	}

	public void setDcFlag(String dcFlag) {
		this.dcFlag = dcFlag;
	}

	public Double getTraceBal() {
		return traceBal;
	}

	public void setTraceBal(Double traceBal) {
		this.traceBal = traceBal;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getVouNo() {
		return vouNo;
	}

	public void setVouNo(String vouNo) {
		this.vouNo = vouNo;
	}

	public String getAccSon() {
		return accSon;
	}

	public void setAccSon(String accSon) {
		this.accSon = accSon;
	}



	public long getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(long traceNo) {
		this.traceNo = traceNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Double getTraceCBal() {
		if (dcFlag.equals("C")) {
			return traceBal;
		} else {
			return 0.00;
		}
	}

	public Double getTraceDBal() {
		if (dcFlag.equals("D")) {
			return traceBal;
		} else {
			return 0.00;
		}
	}

	public String getStrTraceCBal() {
		return UtilBase.formatString(this.getTraceCBal());
	}


	public String getStrtraceDBal() {
		return UtilBase.formatString(this.getTraceDBal());
	}

	public long getTraceNoSon() {
		return traceNoSon;
	}

	public void setTraceNoSon(long traceNoSon) {
		this.traceNoSon = traceNoSon;
	}

	public long getPageNum1() {
		return pageNum1;
	}

	public void setPageNum1(long pageNum1) {
		this.pageNum1 = pageNum1;
	}

	public long getPageNum2() {
		return pageNum2;
	}

	public void setPageNum2(long pageNum2) {
		this.pageNum2 = pageNum2;
	}

	public String getTrace_code() {
		return trace_code;
	}

	public void setTrace_code(String trace_code) {
		this.trace_code = trace_code;
	}

	public String getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}
	

}