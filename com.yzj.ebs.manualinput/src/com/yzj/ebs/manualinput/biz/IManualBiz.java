package com.yzj.ebs.manualinput.biz;

import java.util.List;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.manualinput.param.ManualAuthInfo;
import com.yzj.ebs.manualinput.param.ManualInputInfo;
/**
 * 
 *创建于:2013-4-11 <br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 数据补录及数据复核逻辑操作接口
 * @author 施江敏
 * @version 1.0.0
 */
public interface IManualBiz {

	/**
	 * 根据流水号查询docset
	 * @param docId 业务流水号
	 * @return DocSet
	 * @throws XDocProcException
	 */
	public DocSet queryOneByID(Long docId) throws XDocProcException ;

	/**
	 * 根据账单编号查询帐号信息
	 * @param voucherNo 账单编号
	 * @return 帐号信息
	 * @throws XDocProcException
	 */
	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherNo) throws XDocProcException;

	/**
	 * 根据帐号查询账单
	 * @param billNo 账单号
	 * @return
	 * @throws XDocProcException
	 */
	public CheckMainData getOneByVoucherNo(String billNo) throws XDocProcException;

	/**
	 * 保存账单信息
	 * @param data 账单实体类
	 * @param docSet 业务流水实体类
	 * @throws XDocProcException
	 */
	public void updateCheckMainDataByDocSet(CheckMainData data,DocSet docSet) throws XDocProcException;

	/**
	 * 
	 * 批量保存帐号信息
	 * @param accNolist 帐号信息对象list
	 * @throws XDocProcException
	 */
	public void batchUpdate(List<AccNoMainData> accNolist) throws XDocProcException;

	/**
	 * 根据帐号信息生成数据补录页面信息封装类
	 * @param accNolist 帐号信息list
	 * @param info 数据补录页面信息封装类
	 * @return
	 */
	public ManualInputInfo getManualInputInfo(List<AccNoMainData> accNolist,
			ManualInputInfo info);

	/**
	 * 根据帐号信息生成录入复核页面信息封装类
	 * @param accNolist 帐号信息list
	 * @param info 录入复核页面信息封装类
	 * @return
	 */
	public ManualAuthInfo getManualAuthInfo(List<AccNoMainData> accNolist,
			ManualAuthInfo info);

}
