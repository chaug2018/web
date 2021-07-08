package com.yzj.ebs.common;

import java.util.List;

/**
 * 创建于:2012-12-14<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * newseal操作访问服务接口定义
 * 
 * @author qq
 * @version 1.0.0
 */
public interface INewSealAdm {

	/**
	 * 执行本地查询Sql语句
	 * 
	 * @param sql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws XDocProcException
	 *             当执行本地Sql语句失败时抛出异常
	 */
	List<Object> findBySql(final String sql) throws XDocProcException;
	
	/**
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句
	 * @return 运行结果
	 * @throws DaoException
	 */
	Object ExecQuery(String QuerySql) throws XDocProcException;
}
