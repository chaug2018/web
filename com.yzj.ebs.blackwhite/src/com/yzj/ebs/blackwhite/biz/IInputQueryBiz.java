package com.yzj.ebs.blackwhite.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.ImportSpecile;

/**
 * 创建于:2013-09-24<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 特殊账户过滤表 biz
 * @author j_sun
 * @version 1.0.0
 */
public interface IInputQueryBiz {
	/**
	 * 查询 特殊账户过滤表信息
	 */
	public List<ImportSpecile> queryDate(Map<String,String> quertMap,PageParam param)throws XDocProcException;
}
