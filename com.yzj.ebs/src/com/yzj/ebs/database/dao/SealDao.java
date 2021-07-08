package com.yzj.ebs.database.dao;

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
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yzj.ebs.common.XDocProcException;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 通用DAO，用于操作所有的表
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class SealDao extends HibernateDaoSupport {

	
	/**
	 * 执行本地分页查询Sql语句
	 * 
	 * @param sql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws XDocProcException
	 *             当执行本地Sql语句失败时抛出异常
	 */
	public List<Object> findBySql(final String sql) throws XDocProcException {
		try {
			return (List<Object>) getHibernateTemplate().execute(
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
	 * 功能：执行Sql语言
	 * 
	 * @param QuerySql
	 *            Sql语句
	 * @return 运行结果
	 * @throws DaoException
	 */
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
		} catch (DataAccessException e) {
			throw new XDocProcException("执行Sql失败 Sql=" + sql, e);
		}
	}

}