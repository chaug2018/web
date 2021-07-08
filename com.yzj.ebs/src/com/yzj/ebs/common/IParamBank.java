package com.yzj.ebs.common;

public interface IParamBank extends IBaseService<BankParam>{
	
/**
 *   得到 分行的 电话 和地址
 */
	public BankParam getBankParam(String idCenter)throws XDocProcException;
	
/**
 *  根据分行名称得到分行的 机构号
 */
	public String getIdcenterCode(String idcenterName) throws XDocProcException;
}
