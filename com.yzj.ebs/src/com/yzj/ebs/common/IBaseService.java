package com.yzj.ebs.common;

import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 基本服务接口，定义基本的数据库增删改查方法
 * 
 * @author WangXue
 * @version 1.0.0
 */
public interface IBaseService<T extends Object> {

	/**
	 * 向数据库添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当添加记录失败时抛出异常
	 */
	T create(T entity) throws XDocProcException;

	/**
	 * 批量创建，只要其中有一个创建失败，则批量全部失败
	 * 
	 * @param entityList
	 *            实例
	 * @throws XDocProcXDocProcException
	 *             异常
	 */
	void batchCreate(final List<T> entityList) throws XDocProcException;

	/**
	 * 向数据库更新一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当更新记录失败时抛出异常
	 */
	T update(T entity) throws XDocProcException;

	/**
	 * 向数据库更新或添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当更新记录失败时抛出异常
	 */
	T saveOrUpdate(T entity) throws XDocProcException;

	/**
	 * 从数据库删除一条对应于一个业务对象的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	void delete(T entity) throws XDocProcException;

	/**
	 * 从数据库删除所有对应于一个业务对象的记录
	 * 
	 * @param clazz
	 *            指定类型的业务对象
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	void deleteAll(Class<T> clazz) throws XDocProcException;

	/**
	 * 从数据库删除集合
	 * 
	 * @param entities
	 *            数据库实体集合
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	void deleteAll(Collection<T> entities) throws XDocProcException;

	/**
	 * 根据查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param hql
	 *            指定查询语句
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */

	List<?> findByHql(String hql) throws XDocProcException;

	/**
	 * 根据查询语句查询数据库并返回查询结果所包含的业务对象集合(按分页形式)。
	 * 
	 * @param hql
	 *            指定的HQL查询语句
	 * @param startRow
	 *            开始行数(不包括开始行,从开始行数的下一行开始)
	 * @param pageSize
	 *            页长度
	 * @return 返回查询结果包含的DocSet对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */

	List<?> findbyPage(final String hql, final int startRow, final int pageSize)
			throws XDocProcException;

	/**
	 * 根据已定义的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryName
	 *            已定义查询语句的名称
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */

	List<?> findByNamedQuery(String queryName) throws XDocProcException;

	/**
	 * 根据已定义的带一个参数的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryName
	 *            已定义查询语句的名称
	 * @param param
	 *            指定的参数
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */

	List<?> findByNamedQuery(String queryName, Object param)
			throws XDocProcException;

	/**
	 * 根据已定义的带多个参数的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryName
	 *            已定义查询语句的名称
	 * @param params
	 *            指定的参数数组
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */

	List<?> findByNamedQuery(String queryName, Object[] params)
			throws XDocProcException;

	/**
	 * 执行本地查询Sql语句
	 * 
	 * @param sql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws XDocProcException
	 *             当执行本地Sql语句失败时抛出异常
	 */
	List<?> findBySql(final String sql) throws XDocProcException;

	/**
	 * 根据条件查询
	 * 
	 * @param detachedCriteria
	 *            离线查询条件,可使用DetachedCriteria.forEntityName()等方法初始化，
	 *            与Criteria拼装条件方式一样
	 * @return 查询得到的结果List
	 * @throws XDocProcException
	 *             异常
	 */

	List<?> findByDetachedCriteria(final DetachedCriteria detachedCriteria)
			throws XDocProcException;

	/***
	 * 分页获取业务数据，不确定参数个数的情况下 利用反射机制，动态生成T强类型hql语句 ，也可自定义查询语句
	 * 
	 * @param start
	 * @param rows
	 * @param condition
	 * @param values
	 * @return
	 */
	List<T> getObjectsByPageCondition(final int start, final int rows,
			final String condition, final Object[] values);
	
	/**
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句
	 * @return 运行结果
	 * @throws DaoException
	 */
	Object ExecQuery(String QuerySql) throws XDocProcException;

	/**
	 * 根据条件查询
	 * 
	 * @param detachedCriteria
	 *            离线查询条件,可使用DetachedCriteria.forEntityName()等方法初始化，
	 *            与Criteria拼装条件方式一样
	 * @param startRow
	 *            开始行数(不包括开始行,从开始行数的下一行开始)
	 * @param pageSize
	 *            页长度
	 * @return 查询得到的结果List
	 * @throws XDocProcException
	 *             异常
	 */
	List<?> findbyPage(DetachedCriteria detachedCriteria, int startRow,
			int pageSize) throws XDocProcException;
	
	
	/**
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句
	 *        param
	 *            列表参数
	 * @return 运行结果
	 * @throws DaoException
	 */
	public Object ExecQueryByParam(String QuerySql,final List<Object> param) throws XDocProcException;
	public void batchSaveOrUpdateByHql(final List<String> hqlList) throws XDocProcException;
	public List<?> findBySql(final String sql, final int startRow,
			final int pageSize) throws XDocProcException;
}
