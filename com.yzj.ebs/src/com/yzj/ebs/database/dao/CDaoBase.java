package com.yzj.ebs.database.dao;


import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocLog;


public class CDaoBase extends HibernateDaoSupport {
	/**
	 * 向数据库添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws DaoException
	 *             当添加记录失败时抛出异常
	 */
	protected Object create(Object entity) throws DaoException {
		try {
			getHibernateTemplate().save(entity);
			return entity;
		} catch (DataAccessException e) {
			// log.error("保存 " + entity.getClass().getName() + " 实例到数据库失败", e);
			throw new DaoException("保存 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}

	/**
	 * 向数据库更新一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws DaoException
	 *             当更新记录失败时抛出异常
	 */
	protected void update(Object entity) throws DaoException {
		try {
			getHibernateTemplate().update(entity);
		} catch (DataAccessException e) {
			// log.error("更新 " + entity.getClass().getName() + " 实例到数据库失败", e);
			throw new DaoException("更新 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}
	
	public int ExecuteSql(String sql) throws DaoException {
		int result = 0;
		Connection conn = getHibernateTemplate().getSessionFactory().openSession().connection();
		
		try {
			PreparedStatement pre =  conn.prepareStatement(sql);
			result = pre.executeUpdate();			
			conn.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return result;
	}

	
	
	/**
	 * 向数据库更新或添加一条对应于一个业务对象实例的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws DaoException
	 *             当更新记录失败时抛出异常
	 */
	protected void saveOrUpdate(Object entity) throws DaoException {
		try {
			getHibernateTemplate().saveOrUpdate(entity);			
		} catch (DataAccessException e) {
			// log.error("存储 " + entity.getClass().getName() + " 实例到数据库失败", e);
			throw new DaoException("更新 " + entity.getClass().getName()
					+ " 实例到数据库失败", e);
		}
	}

	/**
	 * 从数据库删除一条对应于一个业务对象的记录
	 * 
	 * @param entity
	 *            业务对象实例
	 * @throws DaoException
	 *             当删除记录失败时抛出异常
	 */
	protected void delete(Object entity) throws DaoException {
		try {
			getHibernateTemplate().delete(entity);
		} catch (DataAccessException e) {
			// log.error("从数据库删除 " + entity.getClass().getName() + " 实例失败", e);
			throw new DaoException("从数据库删除 " + entity.getClass().getName()
					+ " 实例失败", e);
		}
	}

	/**
	 * 从数据库删除所有对应于一个业务对象的记录
	 * 
	 * @param clazz
	 *            指定类型的业务对象
	 * @throws DaoException
	 *             当删除记录失败时抛出异常
	 */
	public void deleteAll(Class clazz) throws DaoException {
		try {
			List result = getHibernateTemplate().loadAll(clazz);
			getHibernateTemplate().deleteAll(result);
		} catch (DataAccessException e) {
			// log.error("从数据库删除 " + clazz.getName() + " 的所有记录失败", e);
			throw new DaoException("从数据库删除 " + clazz.getName() + " 的所有记录失败", e);
		}
	}

	protected void deleteAll(Collection entities) throws DaoException {
		try {
			getHibernateTemplate().deleteAll(entities);
		} catch (DataAccessException e) {
			throw new DaoException("从数据库删除集合错误", e);
		}
	}

	/**
	 * 根据关键字从数据库加载指定类型的业务对象。
	 * 
	 * @param clazz
	 *            业务对象的Class
	 * @param keyName
	 *            指定关键字对应的字段名称
	 * @param keyValue
	 *            指定关键字的值
	 * @return
	 *            <ul>
	 *            <li>当关键字唯一并存在该记录时，返回该记录对应的业务对象</li>
	 *            <li>当关键字不唯一，返回查询结果的第一条记录所对应的业务对象</li>
	 *            <li>当不存在该记录时,返回null</li>
	 *            </ul>
	 * @throws DaoException
	 *             当加载记录失败时抛出异常
	 */
	protected Object loadByKey(Class clazz, String keyName, Object keyValue)
			throws DaoException {
		try {
			List result = getHibernateTemplate().find(
					"from " + clazz.getName() + " where " + keyName + " = ?",
					keyValue);
			if (result != null && result.size() > 0) {
				return result.get(0);
			} else {
				return null;
			}
		} catch (DataAccessException e) {
			// log.error("加载 " + keyName + " 为 " + keyValue + " 的 "
			// + clazz.getName() + " 实例失败", e);
			throw new DaoException("加载 " + keyName + " 为 " + keyValue + " 的 "
					+ clazz.getName() + " 实例失败", e);
		}
	}

	/**
	 * 从数据库加载指定类型的业务对象的所有记录。
	 * 
	 * @param clazz
	 *            业务对象的Class
	 * @return 返回数据库中对应该业务对象的所有记录的集合
	 * @throws DaoException
	 *             当加载记录失败时抛出异常
	 */
	protected List loadAll(Class clazz) throws DaoException {
		try {
			return getHibernateTemplate().loadAll(clazz);
		} catch (DataAccessException e) {
			// log.error("加载所有 " + clazz.getName() + " 实例时失败", e);
			throw new DaoException("加载所有 " + clazz.getName() + " 实例时失败", e);
		}
	}

	/**
	 * 根据查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryString
	 *            指定查询语句
	 * @return 返回查询结果包含的业务对象集合
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List find(String queryString) throws DaoException {
		try {
			return getHibernateTemplate().find(queryString);
		} catch (DataAccessException e) {
			// log.error("执行查询 " + queryString + " 失败", e);
			throw new DaoException("执行查询 " + queryString + " 失败", e);
		}
	}

	/**
	 * 根据带一个参数的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryString
	 *            指定查询语句
	 * @param param
	 *            指定所带参数
	 * @return 返回查询结果包含的业务对象集合
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List find(String queryString, Object param) throws DaoException {
		try {
			return getHibernateTemplate().find(queryString, param);
		} catch (DataAccessException e) {
			// log.error("执行参数为 " + param + " 的查询 " + queryString + " 失败", e);
			throw new DaoException("执行参数为 " + param + " 的查询 " + queryString
					+ " 失败", e);
		}
	}

	/**
	 * 根据带多个参数的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryString
	 *            指定查询语句
	 * @param params
	 *            指定参数数组
	 * @return 返回查询结果包含的业务对象集合
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List find(String queryString, Object[] params)
			throws DaoException {
		try {
			return getHibernateTemplate().find(queryString, params);
		} catch (DataAccessException e) {
			StringBuffer paramString = new StringBuffer("");
			for (int i = 0; i < params.length; i++) {
				paramString.append(params[i]);
				paramString.append(" ");
			}
			// log.error("执行参数为 " + paramString + "的查询 " + queryString + " 失败",
			// e);
			throw new DaoException("执行参数为 " + paramString + "的查询 "
					+ queryString + " 失败", e);
		}
	}

	/**
	 * 根据已定义的查询语句查询数据库并返回查询结果所包含的业务对象集合。
	 * 
	 * @param queryName
	 *            已定义查询语句的名称
	 * @return 返回查询结果包含的业务对象集合
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List findByNamedQuery(String queryName) throws DaoException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName);
		} catch (DataAccessException e) {
			// log.error("执行命名为 " + queryName + " 的查询失败");
			throw new DaoException("执行命名为 " + queryName + " 的查询失败");
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
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List findByNamedQuery(String queryName, Object param)
			throws DaoException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName, param);
		} catch (DataAccessException e) {
			// log.error("执行参数为 " + param + " 命名为 " + queryName + " 的查询失败");
			throw new DaoException("执行参数为 " + param + " 命名为 " + queryName
					+ " 的查询失败");
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
	 * @throws DaoException
	 *             当查询失败时抛出异常
	 */
	protected List findByNameQuery(String queryName, Object[] params)
			throws DaoException {
		try {
			return getHibernateTemplate().findByNamedQuery(queryName, params);
		} catch (DataAccessException e) {
			StringBuffer paramString = new StringBuffer("");
			for (int i = 0; i < params.length; i++) {
				paramString.append(params[i]);
				paramString.append(" ");
			}
			// log.error("执行参数为 " + paramString + "命名为 " + queryName + "
			// 的查询失败");
			throw new DaoException("执行参数为 " + paramString + "命名为 " + queryName
					+ " 的查询失败");
		}
	}
	
	public ResultSet executeSql(String sql){
		Connection con = getHibernateTemplate().getSessionFactory().openSession().connection();
		try {
			ResultSet rs = con.prepareStatement(sql).executeQuery();
			con.close();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
	}

	/**
	 * 功能：执行本地Sql语言
	 * 
	 * @param QuerySql
	 *            本地Sql语句
	 * @return 运行结果
	 * @throws DaoException
	 */
	protected Object ExecSqlQuery(String QuerySql) throws DaoException {
		final String sql;

		sql = QuerySql;
		try {
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {

					String querySentence = sql;
					Query query = session.createSQLQuery(querySentence);
					return query.list();
				}
			});
		} catch (DataAccessException e) {
			throw new DaoException("执行本地Sql失败 Sql=" + sql, e);
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
	protected Object ExecQuery(String QuerySql) throws DaoException {
		final String sql;
		sql = QuerySql;
		try {
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session)
						throws HibernateException {
					String querySentence = sql;
					Query query = session.createQuery(querySentence);
					return Integer.valueOf(query.executeUpdate());
				}
			});
		} catch (DataAccessException e) {
			throw new DaoException("执行Sql失败 Sql=" + sql, e);
		}
	}

	/**
	 * 功能：执行本地Sql语言
	 * 
	 * @param QuerySql
	 *            本地Sql语句
	 * @param ClassName别名名称
	 * @param SpecClass
	 *            结果类的class
	 * @return 运行结果
	 * @throws DaoException
	 */
	protected List ExecSqlQueryforSpecClass(String QuerySql,
			final String ClassName, final Class SpecClass) throws DaoException {
		final String sql;

		sql = QuerySql;
		try {

			return (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException {

							String querySentence = sql;
							Query query = session.createSQLQuery(querySentence)
									.addEntity(ClassName, SpecClass);
							return query.list();
						}
					});
		} catch (DataAccessException e) {
			throw new DaoException("执行本地Sql失败 Sql=" + sql, e);
		}
	}
	
	public int getTotalCountByTableName(String tableName,String condition){
		int result=0;
		try {
			List lst = (List) ExecSqlQuery("select count(*) from "+tableName + " where "+ condition);
			result = Integer.valueOf(lst.get(0).toString());			
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 功能：执行本地Sql语言
	 * @param QuerySql 本地Sql语句
	 * @param SpecClass 要映射的类的class
	 * @param NameArr Sql返回结果在影射类里的名字
	 * @return 运行结果
	 * @throws DaoException
	 */
	protected List ExecSqlQueryforSpecClassByNameArr(String QuerySql,
			Class SpecClass, final String[] NameArr) throws DaoException {
		final String sql;
		List rlist;
		int i, j;
		Object robject;
		Object[] valuearr;
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<Object> objectlist = new ArrayList<Object>();
		sql = QuerySql;
		try {
			rlist = (List) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException {
							int n;	
							String querySentence = sql;
							SQLQuery query = session.createSQLQuery(querySentence);
							for (n=0;n<NameArr.length;n++)
							{
								query=query.addScalar(NameArr[n],Hibernate.STRING);
							}
							
							return query.list();
						}
					});

			for (i = 0; i < rlist.size(); i++) {
				map.clear();
				
				valuearr = (Object[]) rlist.get(i);
				for (j = 0; j < NameArr.length; j++) {
					map.put(NameArr[j], valuearr[j]);
				}

				robject = SpecClass.newInstance();
				BeanUtils.populate(robject, map);
				objectlist.add(robject);
			}
			return objectlist;
		} catch (DataAccessException e) {
			throw new DaoException("执行本地Sql失败 Sql=" + sql, e);
		}catch (InvocationTargetException e) {
			// 
			throw new DaoException("无法为类" + SpecClass.getName()+"复值", e);
		}		catch (InstantiationException e) {
			// 
			throw new DaoException("无法产生类" + SpecClass.getName()+"的实例", e);
		} catch (IllegalAccessException e) {
			// 
			throw new DaoException("无法产生类" + SpecClass.getName()+"的实例", e);
		}
	}
	
	
	/**
	 * @author 许鑫
	 * 
	 * @category 更新日志
	 * 
	 * @param VOUCHERNO 账单编号
	 * @param ACCID	 账号ID
	 * @param APPID	 模块ID
	 * @param OPCODE	 操作人代码
	 * @param OPDATE	 操作时间（取服务器时间）
	 * @param DEALRESULT	处理结果
	 * @param LOGDESC	 处理备注
	 * 
	 */
	public void saveEbillLog(CheckMainData checkmaindata, String appid, String dealresult,
			String opCode, String opdate, String logDesc,String opname,String idbank,String bankname) throws DaoException {
		DocLog log = new DocLog();
		log.setVoucherNo(checkmaindata.getVoucherNo());
//		log.setAccno(checkmaindata.getAccno());
		log.setLogDesc(logDesc);
		log.setOpCode(opCode);
		log.setOpDate(opdate);
		log.setDealResult(dealresult);
		log.setAppId(appid);
		log.setOpName(opname);
		log.setIdBank(idbank);
		log.setBankName(bankname);
		super.getHibernateTemplate().save(log);
	}

	/**
	 * 获得当前对象
	 * 
	 * @return
	 */
	protected Session currentSession() {
		return getHibernateTemplate().getSessionFactory().openSession();
	}

	/**
	 * 关闭当前对象
	 * 
	 * @param session
	 */
	protected void closeSession() {
		getHibernateTemplate().getSessionFactory().close();
	}
	/**
	 * 批量创建，只要其中有一个创建失败，则批量全部失败
	 * 
	 * @param entityList
	 *            实例
	 * @throws XDocProcException
	 *             异常
	 */
	public void batchCreate(final List<Object> entityList) throws XDocProcException {
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
}
