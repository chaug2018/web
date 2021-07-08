package com.yzj.ebs.manuprove.biz;

import java.util.List;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.SealLog;
import com.yzj.ebs.manuprove.action.ManuProveJson;


/**
 * 创建于:2013-04-11<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 特殊账单维护及统计  业务实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public interface IManuProveBiz {
	public DocSet queryOneByID(Long docId) throws XDocProcException;
	public void create(String sealLogStr,DocSet docSet,ManuProveJson manuProveJsonObj) throws Exception;
	
	/**
	 * 根据账单编号获取账号列表
	 * 
	 * @return List<Object[]>
	 * @throws XDocProcException
	 */
	public List<Object[]> getSealAccno(String voucherno) throws XDocProcException;
	
	/**
	 * 获取账号列表
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	public String getAccNoList(DocSet docSet,ManuProveJson manuProveJsonObj) throws XDocProcException;
	/**
	 * 获取验印不过理由参数
	 * 
	 */
	public String getUntreadList() throws Exception;
}
