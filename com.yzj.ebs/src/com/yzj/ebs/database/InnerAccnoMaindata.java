package com.yzj.ebs.database;

/**
 * 创建于:2015-12-09<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 内部账户账单表
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class InnerAccnoMaindata implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;

	//流水号
	protected Long autoId;
	
	//内部账号
	private String accNo;
	
	//账单日期
	private String dataDate;
	
	//余额
	private String bal;
	
	//备注(存放余额调节信息)
	private String abs;
	
	//核对结果(0：相符 1:不相符)
	private String result;
	
	//核对柜员号
	private String resultPeopleCode;
	
	//核对日期
	private String resultDate;
	
	//复核结果(0：复核通过 1:复核不通过)
	private String reCheck;

	//复核柜员号
	private String reCheckPeopleCode;
	
	//复核日期
	private String reCheckDate;
	
	//对账中心号
	private String idCenter;

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

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public String getAbs() {
		return abs;
	}

	public void setAbs(String abs) {
		this.abs = abs;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResultPeopleCode() {
		return resultPeopleCode;
	}

	public void setResultPeopleCode(String resultPeopleCode) {
		this.resultPeopleCode = resultPeopleCode;
	}

	public String getResultDate() {
		return resultDate;
	}

	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}

	public String getReCheck() {
		return reCheck;
	}

	public void setReCheck(String reCheck) {
		this.reCheck = reCheck;
	}

	public String getReCheckPeopleCode() {
		return reCheckPeopleCode;
	}

	public void setReCheckPeopleCode(String reCheckPeopleCode) {
		this.reCheckPeopleCode = reCheckPeopleCode;
	}

	public String getReCheckDate() {
		return reCheckDate;
	}

	public void setReCheckDate(String reCheckDate) {
		this.reCheckDate = reCheckDate;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	
}
