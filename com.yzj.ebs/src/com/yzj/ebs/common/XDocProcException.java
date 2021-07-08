/**
 * XDocProcException.java
 * 版权所有(C) 2009 深圳市银之杰科技股份有限公司
 * 创建:Caichaowei 2009-10-30
 */
package com.yzj.ebs.common;

/**
 * 系统异常定义类
 * @author CaiChaowei
 * @modify by Gavin 增加了ErrorCode设置
 * @version 1.0.0
 */
public class XDocProcException extends Exception {
	private static final long serialVersionUID = 1688498623269247061L;
	private String errorCode = null;

	public XDocProcException() {
		super();
	}

	public XDocProcException(String errorMsg) {
		super(errorMsg);
	}
	
	public XDocProcException(String errorMsg, String errorCode) {
		super(errorMsg);
		this.errorCode = errorCode;
	}

	public XDocProcException(Throwable throwable) {
		super(throwable.getMessage(),throwable);
	}

	public XDocProcException(String errorMsg, Throwable throwable) {
		super(errorMsg+";"+throwable.getMessage(), throwable);
	}

	@Override
	public String getMessage() {
		if(errorCode == null) {
			return super.getMessage();
		}
		
		return "[" + this.errorCode + "]" + super.getMessage();
	}
}
