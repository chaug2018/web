package com.yzj.ebs.database;
/**
 *创建于:2012-9-20<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据处理日志
 * @author qinjingfeng
 * @version 1.0.0
 */
public class EDatalog implements java.io.Serializable {
	
	private static long serialVersionUID = 7153411570608087393L;
	private Long autoId;
	private Long taskId; //批次号，每次重新导数自动增加
	private Long importType;   //导数方式  0自动导数，1手动导数
	private Long errFlag; //错误标志，0成功，1失败
	private String opDate; //导数日期
	private String opCoder;//导数柜员
	private String opDesc; //导数日志
	private String docDate;//对账日期
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public static void setSerialVersionUID(long serialVersionUID) {
		EDatalog.serialVersionUID = serialVersionUID;
	}
	public Long getAutoId() {
		return autoId;
	}
	public void setAutoId(Long autoId) {
		this.autoId = autoId;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getImportType() {
		return importType;
	}
	public void setImportType(Long importType) {
		this.importType = importType;
	}
	public Long getErrFlag() {
		return errFlag;
	}
	public void setErrFlag(Long errFlag) {
		this.errFlag = errFlag;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
	}
	public String getOpCoder() {
		return opCoder;
	}
	public void setOpCoder(String opCoder) {
		this.opCoder = opCoder;
	}
	public String getOpDesc() {
		return opDesc;
	}
	public void setOpDesc(String opDesc) {
		this.opDesc = opDesc;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	
	
}
