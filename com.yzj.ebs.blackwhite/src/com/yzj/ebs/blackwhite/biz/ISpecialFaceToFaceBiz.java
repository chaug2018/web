package com.yzj.ebs.blackwhite.biz;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.SpecialFaceToFace;

/**
 * 创建于:2015-12-31<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 特殊账单维护及统计  业务实现
 * 
 * @version 1.0.0
 */
public interface ISpecialFaceToFaceBiz {
	
	/**
	 * 获取账户信息列表
	 * @param queryMap
	 * @param queryParam
	 * @param isPaged
	 * @return
	 * @throws XDocProcException 
	 */
	List<SpecialFaceToFace> getDataList(Map<String,String> queryMap,PageParam queryParam,boolean isPaged) throws XDocProcException;
	
	/**
	 * 读取excel数据，检测后插入数据库
	 * @param upFile
	 * @return
	 * @throws XDocProcException 
	 */
	String completeBatchInput(File upFile) throws XDocProcException;

	/**
	 * 删除帐户信息
	 */
	public void deleteInfo(String accno,String docdate) throws XDocProcException;
}
