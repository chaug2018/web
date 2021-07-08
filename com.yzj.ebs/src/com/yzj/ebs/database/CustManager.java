package com.yzj.ebs.database;

public class CustManager implements java.io.Serializable {
	/**
	 * 自动生成ID
	 */
	private static final long serialVersionUID = -3804162103360408148L;
	//主键，自动生成
	private long autoID;
	// 账号
    private String accNo;
    // 客户经理
    private String custManager;
    // 经理联系方式
    private String cusManPhone;
    // 对账日期
    private String docDate;
    
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getCustManager() {
		return custManager;
	}
	public void setCustManager(String custManager) {
		this.custManager = custManager;
	}
	public String getCusManPhone() {
		return cusManPhone;
	}
	public void setCusManPhone(String cusManPhone) {
		this.cusManPhone = cusManPhone;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getAutoID() {
		return autoID;
	}
	public void setAutoID(long autoID) {
		this.autoID = autoID;
	}
	

}
