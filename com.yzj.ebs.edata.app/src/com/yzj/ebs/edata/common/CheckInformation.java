package com.yzj.ebs.edata.common;

/**
 *创建于:2012-9-25<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 提示信息对象类
 * @author Lif
 * @version 1.0.0
 */
public class CheckInformation {
	public enum InfoType{
		WARN,ERROR,SUCCESS
	}
	private InfoType type;
	private String msg;
    private boolean result;
	
	public CheckInformation(InfoType type, String msg) {
		super();
		this.type = type;
		this.msg = msg;
	}
	public InfoType getType() {
		return type;
	}
	public void setType(InfoType type) {
		this.type = type;
	}
	public boolean isResult() {
		return result;
	}
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
