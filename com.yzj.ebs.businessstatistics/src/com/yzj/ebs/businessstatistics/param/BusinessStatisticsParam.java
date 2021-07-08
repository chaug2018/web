package com.yzj.ebs.businessstatistics.param;

import java.io.Serializable;

import com.yzj.ebs.common.param.PageParam;
/**
 * 催收统计查询参数bean
 * @author swl
 *
 */
public class BusinessStatisticsParam extends PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6147196367194669184L;
	private String idCenter; // 对账中心
	private String idBranch; // 支行
	private String idBank; // 网点
	private String idBank1;
	private String docDate; // 对账日期

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

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getIdBank1() {
		return idBank1;
	}

	public void setIdBank1(String idBank1) {
		this.idBank1 = idBank1;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
