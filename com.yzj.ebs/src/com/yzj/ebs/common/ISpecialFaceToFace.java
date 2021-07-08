package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.SpecialFaceToFace;

public interface ISpecialFaceToFace {
	
	/**
	 * accno是否在bacisinfo表中存在，并且状态为0:正常
	 * @param accno
	 * @return
	 * @throws XDocProcException
	 */
	public boolean ifExistInBasicInfo(String accno) throws XDocProcException;
	
	/**
	 * accno,docdate 是否在SpecialFaceToFace表中存在
	 * @param accno
	 * @param docdate
	 * @return
	 * @throws XDocProcException
	 */
	public boolean ifExistInSpecialFaceToFace(String accno,String docdate) throws XDocProcException;
	
	/**
	 *  增加SpecialFaceToFace
	 */
	public void addInfo(SpecialFaceToFace s) throws XDocProcException;
	
	
	/**
	 * 删除特殊账户过滤表信息
	 */
	public void deleteInfo(String accno,String docdate) throws XDocProcException;
	

	public List<SpecialFaceToFace> getDataList(Map<String,String> queryMap,PageParam param) throws XDocProcException;
	public List<SpecialFaceToFace> getAllDataList(Map<String,String> queryMap) throws XDocProcException;
}
