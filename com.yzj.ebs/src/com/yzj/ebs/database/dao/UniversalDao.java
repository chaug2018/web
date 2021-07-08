package com.yzj.ebs.database.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 通用DAO，用于操作所有的表
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class UniversalDao<T extends Object> extends HibernateDaoSupport {

	/**
	 * 根据查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryString
	 *            指定查询语句
	 * @return 返回查询结果包含的业务对象集合
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	@SuppressWarnings("rawtypes")
	public List find(String queryString) throws XDocProcException {
		try {
			return getHibernateTemplate().find(queryString);
		} catch (DataAccessException e) {
			// log.error("执行查询 " + queryString + " 失败", e);
			throw new XDocProcException("执行查询 " + queryString + " 失败", e);
		}
	}

	/**
	 * 批量创建，只要其中有一个创建失败，则批量全部失败
	 * 
	 * @param entityList
	 *            实例
	 * @throws XDocProcException
	 *             异常
	 */
	public void batchCreate(final List<T> entityList) throws XDocProcException {
		try {
			getHibernateTemplate().execute(new HibernateCallback<Object>() {
				@Override
				public Object doInHibernate(Session session)
						throws HibernateException, SQLException {
					Transaction transaction = session.beginTransaction();
					try {
						for (int i = 0; i < entityList.size(); i++) {
							session.save(entityList.get(i));
							session.flush();
							session.clear();
						}
						transaction.commit();
						return null;
					} catch (HibernateException e) {
						transaction.rollback();
						throw e;
					}
				}
			});
		} catch (DataAccessException e) {
			throw new XDocProcException("批量创建到数据库失败", e);
		}
	}

	/**
	 * 批量创建，只要其中有一个创建失败，则批量全部失败
	 * 
	 * @param entityList
	 *            用hql语句执行的 实例
	 * @throws XDocProcException
	 *             异常
	 */
	public void batchSaveOrUpdateByHql(final List<String> hqlList)
			throws XDocProcException {
		Connection conn = this.getHibernateTemplate().getSessionFactory()
				.openSession().connection();
		Statement pre = null;
		try {
			// conn = DBUtil.open();
			pre = conn.createStatement();
			for (int i = 0; i < hqlList.size(); i++) {
				pre.addBatch(hqlList.get(i));
			}
			pre.executeBatch();
			conn.commit();
			pre.clearBatch();
			pre.close();
			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
				pre.close();
				conn.close();
			} catch (SQLException e1) {
				throw new XDocProcException("回滚失败，" + e.getMessage());
			}
			e.printStackTrace();
		}
	}

	/**
	 * 向数据库添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当添加记录失败时抛出异常
	 */
	public T create(T entity) throws XDocProcException {
		try {
			getHibernateTemplate().save(entity);
			return entity;
		} catch (DataAccessException e) {
			throw new XDocProcException("保存 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}

	/**
	 * 向数据库更新一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当更新记录失败时抛出异常
	 */
	public T update(T entity) throws XDocProcException {
		try {
			getHibernateTemplate().update(entity);
			return entity;
		} catch (DataAccessException e) {
			throw new XDocProcException("更新 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}

	/**
	 * 向数据库更新或添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当更新记录失败时抛出异常
	 */
	public T saveOrUpdate(T entity) throws XDocProcException {
		try {
			getHibernateTemplate().saveOrUpdate(entity);
			return entity;
		} catch (DataAccessException e) {
			throw new XDocProcException("更新 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}

	/**
	 * 从数据库删除一条对应于一个业务对象的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	public void delete(T entity) throws XDocProcException {
		try {
			getHibernateTemplate().delete(entity);
		} catch (DataAccessException e) {
			throw new XDocProcException("从数据库删除 " + entity.getClass().getName()
					+ " 实例失败", e);
		}
	}

	/**
	 * 从数据库删除所有对应于一个业务对象的记录
	 * 
	 * @param clazz
	 *            指定类型的业务对象
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	public void deleteAll(Class<T> clazz) throws XDocProcException {
		try {
			List<T> result = getHibernateTemplate().loadAll(clazz);
			getHibernateTemplate().deleteAll(result);
		} catch (DataAccessException e) {
			throw new XDocProcException("从数据库删除 " + clazz.getName()
					+ " 的所有记录失败", e);
		}
	}

	/**
	 * 从数据库删除集合
	 * 
	 * @param entities
	 *            数据库实体集合
	 * @throws XDocProcException
	 *             当删除记录失败时抛出异常
	 */
	public void deleteAll(Collection<T> entities) throws XDocProcException {
		try {
			getHibernateTemplate().deleteAll(entities);
		} catch (DataAccessException e) {
			throw new XDocProcException("从数据库删除集合错误", e);
		}
	}

	/**
	 * 根据查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param hql
	 *            指定查询语句
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */
	public List<?> findByHql(String hql) throws XDocProcException {
		try {
			return getHibernateTemplate().find(hql);
		} catch (DataAccessException e) {
			throw new XDocProcException("执行查询 " + hql + " 失败", e);
		}
	}

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
	public List<?> findbyPage(final String hql, final int startRow,
			final int pageSize) throws XDocProcException {
		try {
			List<?> list = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							Query query = session.createQuery(hql);
							query.setFirstResult(startRow);
							query.setMaxResults(pageSize);
							return query.list();
						}
					});
			return list;
		} catch (DataAccessException e) {
			throw new XDocProcException("执行Sql失败 Sql=" + hql, e);
		}
	}

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
	public List<?> findbyPage(final DetachedCriteria detachedCriteria,
			final int startRow, final int pageSize) throws XDocProcException {
		try {
			List<?> list = (List<?>) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Criteria criteria = detachedCriteria
									.getExecutableCriteria(session);
							criteria.setMaxResults(pageSize);
							criteria.setFirstResult(startRow);
							return criteria.list();
						}
					});
			return list;
		} catch (Exception e) {
			throw new XDocProcException("根据criteria进行分页查询异常" + e.getMessage());
		}
	}

	/**
	 * 根据已定义的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryName
	 *            已定义查询语句的名称
	 * @return 返回查询结果包含的业务对象集合
	 * @throws XDocProcException
	 *             当查询失败时抛出异常
	 */
	public List<?> findByNamedQuery(String queryName) throws XDocProcException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName);
		} catch (DataAccessException e) {
			throw new XDocProcException("执行命名为 " + queryName + " 的查询失败", e);
		}
	}

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
	public List<?> findByNamedQuery(String queryName, Object param)
			throws XDocProcException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName, param);
		} catch (DataAccessException e) {
			throw new XDocProcException("执行参数为 " + param + " 命名为 " + queryName
					+ " 的查询失败", e);
		}
	}

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
	public List<?> findByNamedQuery(String queryName, Object[] params)
			throws XDocProcException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName, params);

		} catch (DataAccessException e) {
			StringBuffer paramString = new StringBuffer("");
			for (int i = 0; i < params.length; i++) {
				paramString.append(params[i]);
				paramString.append(" ");
			}
			throw new XDocProcException("执行参数为 " + paramString + "命名为 "
					+ queryName + "的查询失败", e);
		}
	}

	/**
	 * 执行本地查询Sql语句
	 * 
	 * @param sql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws XDocProcException
	 *             当执行本地Sql语句失败时抛出异常
	 */
	public List<?> findBySql(final String sql, final int startRow,
			final int pageSize) throws XDocProcException {
		try {
			return (List<?>) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							Query query = session.createSQLQuery(sql);
							query.setFirstResult(startRow);
							query.setMaxResults(pageSize);
							return query.list();
						}
					});
		} catch (DataAccessException e) {
			throw new XDocProcException("执行本地Sql失败 Sql=" + sql, e);
		}
	}

	/**
	 * 执行本地分页查询Sql语句
	 * 
	 * @param sql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws XDocProcException
	 *             当执行本地Sql语句失败时抛出异常
	 */
	public List<?> findBySql(final String sql) throws XDocProcException {
		try {
			return (List<?>) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							Query query = session.createSQLQuery(sql);
							return query.list();
						}
					});
		} catch (DataAccessException e) {
			throw new XDocProcException("执行本地Sql失败 Sql=" + sql, e);
		}
	}

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
	public List<?> findByDetachedCriteria(
			final DetachedCriteria detachedCriteria) throws XDocProcException {
		try {
			return (List<?>) getHibernateTemplate().execute(
					new HibernateCallback<Object>() {
						@Override
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {
							Criteria criteria = detachedCriteria
									.getExecutableCriteria(session);
							return criteria.list();
						}
					});
		} catch (Exception e) {
			throw new XDocProcException("根据criteria进行查询异常" + e.getMessage());
		}
	}

	/**
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句
	 * @return 运行结果
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public Object ExecQuery(String QuerySql) throws XDocProcException {
		final String sql;
		sql = QuerySql;
		try {
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {
					Query query = session.createQuery(sql);
					return Integer.valueOf(query.executeUpdate());
				}
			});
		} catch (Exception e) {
			throw new XDocProcException("执行Sql失败 Sql=" + sql, e);
		}
	}

	/**
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句 param 列表参数
	 * @return 运行结果
	 * @throws DaoException
	 */
	@SuppressWarnings("unchecked")
	public Object ExecQueryByParam(String QuerySql, final List<Object> param)
			throws XDocProcException {
		final String sql;
		sql = QuerySql;
		try {
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {
					Query query = session.createQuery(sql);
					for (int i = 0; i < param.size(); i++) {
						query.setParameter(i, param.get(i));
					}
					return Integer.valueOf(query.executeUpdate());
				}
			});
		} catch (DataAccessException e) {
			throw new XDocProcException("执行Sql失败 Sql=" + sql, e);
		}
	}

	/***
	 * 分页查询 按HQL语句封装 多表联查
	 * 
	 * @throws XDocProcException
	 * @throws NumberFormatException
	 */
	public List<?> getPageByParamForSql(String querySql, String queryCount,
			PageParam param) throws NumberFormatException, XDocProcException {

		List<Long> countList = (List<Long>) this.findByHql(queryCount);
		Integer countNumber = 0;
		if (countList != null && countList.size() > 0) {
			countNumber = countList.get(0).intValue();

		}
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = this.findbyPage(querySql, firstResult, pageSize);
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal(countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录

		if (list != null && list.size() != 0) {

			return list;
		} else {
			return list;
		}
	}

	/***
	 * 分页查询 按HQL语句封装
	 * 
	 * @throws XDocProcException
	 * @throws NumberFormatException
	 */
	public List<?> getPageByCriteriaForSql(Criteria criteria,
			Criteria criteriaCount, PageParam param)
			throws NumberFormatException, XDocProcException {
		// 获取满足条件的数据总数

		criteriaCount.setProjection(Projections.rowCount());
		Object obj = criteriaCount.uniqueResult();
		Long countNumberTmp =  (Long) obj;
		int countNumber = countNumberTmp.intValue();

		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		criteria.addOrder(Order.asc("accNo"));
		criteria.addOrder(Order.asc("accSon"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(pageSize);
		List<?> result = criteria.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		return result;
	}

	/***
	 * 分页查询 按sql语句
	 * 
	 * @throws XDocProcException
	 * @throws NumberFormatException
	 * @return 返回一个结果集对象，在具体实现中进行解析
	 */
	public List<?> getByPageFromSql(String querySql, String queryCount,
			PageParam param) throws NumberFormatException, XDocProcException {
		List<BigDecimal> countList = (List<BigDecimal>) this.findBySql(queryCount);
		Integer countNumber = 0;
		if (countList != null && countList.size() > 0) {
			countNumber = countList.get(0).intValue();
		}
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = this.findBySql(querySql, firstResult, pageSize);
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal(countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录

		if (list != null && list.size() != 0) {
			return list;
		} else {
			return list;
		}
	}

	/**
	 * 废弃，容易出现线程阻塞
	 * @param sql
	 * @throws XDocProcException
	 */
	public void ExecSql_before(final String sql)
			throws XDocProcException {
		Session session = this.getHibernateTemplate().getSessionFactory()
				.openSession();
		Connection conn = session.connection();
		Statement pre = null;
		try {
			pre = conn.createStatement();
			pre.executeUpdate(sql);
			conn.commit();
			
//			pre.close();
//			conn.close();
		} catch (SQLException e) {
			try {
				conn.rollback();
//				pre.close();
//				conn.close();
			} catch (SQLException e1) {
				throw new XDocProcException("回滚失败，" + e.getMessage());
			}
			e.printStackTrace();
		}finally{
			try {
				if(pre!=null){
					pre.close();
				}
				if(session!=null){
					session.close();
				}
				if(conn!=null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 执行sql语句(纯sql)
	 * @param querySql
	 * @return Integer
	 * @throws XDocProcException
	 */
	@SuppressWarnings("unchecked")
	public Integer ExecSql(String querySql) throws XDocProcException {
		final String sql;
		sql = querySql;
		try {
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {
					Query query = session.createSQLQuery(sql);
					int i = query.executeUpdate();
					return i;
				}
			});
		} catch (DataAccessException e) {
			throw new XDocProcException(e);
		}
	}
}