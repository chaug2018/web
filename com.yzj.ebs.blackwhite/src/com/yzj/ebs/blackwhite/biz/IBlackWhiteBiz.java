package com.yzj.ebs.blackwhite.biz;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.blackwhite.queryparam.BlackWhiteResult;
import com.yzj.ebs.blackwhite.queryparam.QueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;

/**
 * 创建于:2013-04-11<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 特殊账单维护及统计  业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public interface IBlackWhiteBiz {
	/**
	 * 获取账户信息列表
	 * @param queryMap
	 * @param bwQueryParam
	 * @param isPaged
	 * @return
	 * @throws XDocProcException 
	 */
	List<BasicInfo> getBasicinfoData(Map<String,String> queryMap,PageParam bwQueryParam,boolean isPaged) throws XDocProcException;

	/**
	 * 维护数据
	 * @param basicInfo
	 * @param index
	 * @return
	 * @throws XDocProcException 
	 */
	String modifyByOne(BasicInfo basicInfo,String index) throws XDocProcException;
	/**
	 * 获取黑白名单统计数据
	 * @param queryParam
	 * @param queryMap
	 * @param isPaged
	 * @return
	 * @throws XDocProcException 
	 */
	List<BlackWhiteResult> getAnalyseResult(QueryParam queryParam,Map<String,String> queryMap,
			boolean isPaged,String selectCount) throws XDocProcException;
	/**
	 * 维护数据
	 * @param upFile
	 * @return
	 * @throws XDocProcException 
	 */
	String completeBatchInput(File upFile)throws XDocProcException;
	/**
	 * 日志记录
	 */
	public void putBasicInfoLog(BasicInfo info,String desc)throws XDocProcException;
	/**
	 * 删除特殊帐户信息
	 */
	public void deleteInfor(String accNo)throws XDocProcException;
}
