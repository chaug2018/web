package com.yzj.ebs.database.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.Query;

import com.infotech.publiclib.Exception.DaoException;

/**
 * 
 *创建于:2012-9-21<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 日志操作Dao
 * @author pengwenxuan
 * @version  1.0.0
 */
public class LogInfoDao extends CDaoBase {
	/**
	 * 查询票据日志类表中数据
	 * 
	 * @param voucherno
	 *            对账单编号
	 * @param appid
	 *            操作id
	 * @return 信息集合
	 */
	@SuppressWarnings("rawtypes")
	public List getEbilllogQuery(String voucherno, String appid) {
		List li = null;

		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();

		String hql = "from Ebilllog  where 1=1 ";

		if ((voucherno != null) && (voucherno.length() > 0))
			hql = hql + " and voucherno = '" + voucherno + "'";

		if ((appid != null) && (appid.length() > 0)) {
			hql = hql + " and appid = '" + appid + "'";
		}
		Query q = session.createQuery(hql);
		li = q.list();
		session.close();
		return li;
	}

	/**
	 * 查询数据处理日志
	 * 
	 * @param opdate
	 *            操作日期
	 * @return 信息集合
	 */
	@SuppressWarnings("rawtypes")
	public List getEDatalogQuery(String opdate,String opcode) {
		List li = null;
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		String hql = "from EDatalog where 1=1 ";
		if ((opdate != null) && (opdate.length() > 0)) {
			hql = hql + " and opdate = '" + opdate + "'";
		}
		hql = hql + " order by opdate";

		Query q = session.createQuery(hql);
		li = q.list();
		session.close();
		return li;
	}

	/**
	 * 查找客户信息维护日志
	 * 
	 * @param opdate
	 *            操作日期
	 * @param opcode
	 *            操作人员
	 * @return 客户信息维护日志集合
	 */
	@SuppressWarnings("rawtypes")
	public List getBasicinfologQuery(String opdate, String opcode) {
		List li = null;
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		String hql = "from Basicinfolog where 1=1 ";
		if ((opdate != null) && (opdate.length() > 0)) {
			hql = hql + " and opdate = '" + opdate + "'";
		}
		if ((opcode != null) && (opcode.length() > 0)) {
			hql = hql + " and opcode = '" + opcode + "'";
		}
		hql = hql + " order by opdate";

		Query q = session.createQuery(hql);
		li = q.list();
		session.close();
		return li;
	}
	/**
	 * 查询验印日志信息
	 * @param opdate 操作日期
	 * @param opcode 操作员
	 * @return 信息集合
	 */
	@SuppressWarnings("rawtypes")
	public List getSeallogQuery(String opdate, String opcode) {
		List li = null;
		Session session = getHibernateTemplate().getSessionFactory()
				.openSession();
		String hql = "from Seallog where 1=1 ";
		if ((opdate != null) && (opdate.length() > 0)) {
			hql = hql + " and opdate = '" + opdate + "'";
		}
		if ((opcode != null) && (opcode.length() > 0)) {
			hql = hql + " and opcode = '" + opcode + "'";
		}
		hql = hql + " order by opdate";

		Query q = session.createQuery(hql);
		li = q.list();
		session.close();
		return li;
	}
	
	/**
	 * 保存日志信息
	 * 
	 * @param obj
	 *            传入的日志类
	 * @throws DaoException
	 *             数据库异常
	 */
	public void insertLog(Object obj) throws DaoException {
		super.saveOrUpdate(obj);
	}
}
