package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.ImportSpecile;

/**
 * 创建于:2013-8-21<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * InsideAccnoParam表操作访问服务接口定义
 * 
 * @author j_sun
 * @version 1.0.0
 */
public interface IImportSpecile {
	
	/**
	 *  增加特殊账户过滤表 信息
	 */
	public void putInfor(ImportSpecile importSpecile)throws XDocProcException;
	
	/**
	 * 删除特殊账户过滤表信息
	 */
	public void deleteSpecile(String accno)throws XDocProcException;
	
	/**
	 * 根据 accno 查询 该账号是否存在于 特殊账户导入表中
	 */
	public ImportSpecile getOneByAccno(String accNo);
	/**
	 *  查询    其中的时间条件未 月初1号到 选择的 日期
	 */
	public List<ImportSpecile> getQueryList(Map<String,String> queryMap,PageParam param)throws XDocProcException;
}
