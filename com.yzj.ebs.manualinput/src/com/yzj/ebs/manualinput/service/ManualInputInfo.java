package com.yzj.ebs.manualinput.service;

import java.io.Serializable;

/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据补录界面元素封装类
 * @author 陈林江
 * @version 1.0
 */
public class ManualInputInfo implements Serializable{
	
	private static final long serialVersionUID = -3228943528295474788L;


	public ManualInputInfo(int i){
		
	}
	public ManualInputInfo(){
	}
	
	/**
	 * 业务流水
	 */
    private String docId;  
	
    /**
     * 账单编号
     */
	private String billNo;  
	
	/**
	 * 账号1
	 */
	private String accNo1;
	
	/**
	 * 分账号1
	 */
	private String accNo_1;
	
	/**
	 * 余额结果1
	 */
	private String result1="0";
	
	/**
	 * 账号2
	 */
	private String accNo2;
	
	/**
	 * 分账号2
	 */
	private String accNo_2;
	
	/**
	 * 余额结果2
	 */
	private String result2 ="0";
	/**
	 * 账号3
	 */
	private String accNo3;
	
	/**
	 * 分账号3
	 */
	private String accNo_3;
	
	/**
	 * 余额结果3
	 */
	private String result3 ="0";
	/**
	 * 账号4
	 */
	private String accNo4 ;
	
	/**
	 * 分账号4
	 */
	private String accNo_4;
	
	/**
	 * 余额结果4
	 */
	private String result4 ="0";
	/**
	 * 账号5
	 */
	private String accNo5;
	
	/**
	 * 分账号5
	 */
	private String accNo_5;
	
	/**
	 * 余额结果5
	 */
	private String result5 ="0";

	private String errMsg="";
	

	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getAccNo1() {
		return accNo1;
	}

	public void setAccNo1(String accNo1) {
		this.accNo1 = accNo1;
	}

	public String getAccNo_1() {
		return accNo_1;
	}

	public void setAccNo_1(String accNo_1) {
		this.accNo_1 = accNo_1;
	}

	public String getResult1() {
		return result1;
	}

	public void setResult1(String result1) {
		this.result1 = result1;
	}

	public String getAccNo2() {
		return accNo2;
	}

	public void setAccNo2(String accNo2) {
		this.accNo2 = accNo2;
	}

	public String getAccNo_2() {
		return accNo_2;
	}

	public void setAccNo_2(String accNo_2) {
		this.accNo_2 = accNo_2;
	}

	public String getResult2() {
		return result2;
	}

	public void setResult2(String result2) {
		this.result2 = result2;
	}

	public String getAccNo3() {
		return accNo3;
	}

	public void setAccNo3(String accNo3) {
		this.accNo3 = accNo3;
	}

	public String getAccNo_3() {
		return accNo_3;
	}

	public void setAccNo_3(String accNo_3) {
		this.accNo_3 = accNo_3;
	}

	public String getResult3() {
		return result3;
	}

	public void setResult3(String result3) {
		this.result3 = result3;
	}

	public String getAccNo4() {
		return accNo4;
	}

	public void setAccNo4(String accNo4) {
		this.accNo4 = accNo4;
	}

	public String getAccNo_4() {
		return accNo_4;
	}

	public void setAccNo_4(String accNo_4) {
		this.accNo_4 = accNo_4;
	}

	public String getResult4() {
		return result4;
	}

	public void setResult4(String result4) {
		this.result4 = result4;
	}

	public String getAccNo5() {
		return accNo5;
	}

	public void setAccNo5(String accNo5) {
		this.accNo5 = accNo5;
	}

	public String getAccNo_5() {
		return accNo_5;
	}

	public void setAccNo_5(String accNo_5) {
		this.accNo_5 = accNo_5;
	}

	public String getResult5() {
		return result5;
	}

	public void setResult5(String result5) {
		this.result5 = result5;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	

}
