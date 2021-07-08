package com.yzj.ebs.impl;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.dao.UniversalDao;
import com.yzj.ebs.util.GenericUtils;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 基本服务实现
 * 
 * @author WangXue
 * @version 1.0.0
 */
public class BaseService<T> implements IBaseService<T> {

	protected UniversalDao<T> dao;

	public UniversalDao<T> getDao() {
		return dao;
	}

	public void setDao(UniversalDao<T> dao) {
		this.dao = dao;
	}

	public T create(T t) throws XDocProcException {
		return dao.create(t);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void batchCreate(List<T> entityList) throws XDocProcException {
		dao.batchCreate(entityList);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public T update(T entity) throws XDocProcException {
		return dao.update(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public T saveOrUpdate(T entity) throws XDocProcException {
		return dao.saveOrUpdate(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void delete(T entity) throws XDocProcException {
		dao.delete(entity);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteAll(Class<T> clazz) throws XDocProcException {
		dao.deleteAll(clazz);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteAll(Collection<T> entities) throws XDocProcException {
		dao.deleteAll(entities);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<?> findByHql(String hql) throws XDocProcException {
		return dao.findByHql(hql);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findBySql(String sql) throws XDocProcException {
		return dao.findBySql(sql);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findbyPage(String queryString, int startRow, int pageSize)
			throws XDocProcException {
		return dao.findbyPage(queryString, startRow, pageSize);
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<?> findbyPage(DetachedCriteria detachedCriteria,
			int startRow, int pageSize)
			throws XDocProcException {
		return dao.findbyPage(detachedCriteria, startRow, pageSize);
	}
		
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findByNamedQuery(String queryName) throws XDocProcException {
		return dao.findByNamedQuery(queryName);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findByNamedQuery(String queryName, Object param)
			throws XDocProcException {
		return dao.findByNamedQuery(queryName, param);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findByNamedQuery(String queryName, Object[] params)
			throws XDocProcException {
		return dao.findByNamedQuery(queryName, params);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<?> findByDetachedCriteria(DetachedCriteria detachedCriteria)
			throws XDocProcException {
		return dao.findByDetachedCriteria(detachedCriteria);
	}

	// 分页获取业务数据，不确定参数个数的情况下
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<T> getObjectsByPageCondition(final int start, final int rows,
			final String condition, final Object[] values) {
		HibernateCallback callback = new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = "";
				String defaultHql = "from "
						+ GenericUtils.getSuperClassGenricType(getClass())
								.getName();
				boolean flag = StringUtils.isBlank(condition);
				hql = flag ? defaultHql : condition;
				Query q = session.createQuery(hql);
				if (!flag) {
					if (values != null) {
						for (int i = 0; i < values.length; i++) {
							q.setParameter(i, values[i]);
						}
					}
				}
				if (start > 0 && rows > 0) {
					q.setFirstResult(start);
					q.setMaxResults(rows);
				}
				return q.list();
			}
		};
		Object obj = dao.getHibernateTemplate().execute(callback);
		List<T> list = (List<T>) obj;
		return list;
	}

	@Override
	public Object ExecQuery(String QuerySql) throws XDocProcException {
		return dao.ExecQuery(QuerySql);
	}

	@Override
	public Object ExecQueryByParam(String QuerySql, List<Object> param)
			throws XDocProcException {
		return dao.ExecQueryByParam(QuerySql,param);
	}
	public void batchSaveOrUpdateByHql(final List<String> hqlList) throws XDocProcException
	{
		dao.batchSaveOrUpdateByHql(hqlList);
	}
	public List<?> findBySql(final String sql, final int startRow,
			final int pageSize) throws XDocProcException
	{
		return dao.findBySql(sql, startRow, pageSize);
	}
}
