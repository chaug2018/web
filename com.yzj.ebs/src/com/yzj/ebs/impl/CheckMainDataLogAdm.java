package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.yzj.ebs.common.ICheckMainDataLogAdm;
import com.yzj.ebs.common.OperLogQueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainDataLog;

/**
 * 创建于:2013-1-12<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * CheckMaindataLog表操作访问服务接口定义
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class CheckMainDataLogAdm extends BaseService<CheckMainDataLog> implements
		ICheckMainDataLogAdm {

	@Override
	public CheckMainDataLog createCheckMainDataLog(CheckMainDataLog checkMaiDataLog)
			throws XDocProcException {
		return dao.create(checkMaiDataLog);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CheckMainDataLog> queryCheckMainDataLog(OperLogQueryParam param,boolean isPaged)
			throws XDocProcException {

		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(CheckMainDataLog.class);
		Criteria criteria1 = session.createCriteria(CheckMainDataLog.class);
		if (param.getIdBank() != null && param.getIdBank().length() > 0) {
			criteria.add(Restrictions.eq("idBank", param.getIdBank()));
			criteria1.add(Restrictions.eq("idBank", param.getIdBank()));
		}
		if (param.getIdBranch() != null && (param.getIdBranch().length() > 0)) {
			criteria.add(Restrictions.eq("idBranch",param.getIdBranch()));
			criteria1.add(Restrictions.eq("idBranch",param.getIdBranch()));
		}
		if (param.getIdCenter() != null && param.getIdCenter().length() > 0) {
			criteria.add(Restrictions.eq("idCenter",param.getIdCenter()));
			criteria1.add(Restrictions.eq("idCenter",param.getIdCenter()));
		}
		if (param.getIdBank1() != null && param.getIdBank1().length() > 0) {
			criteria.add(Restrictions.eq("idBank", param.getIdBank1()));
			criteria1.add(Restrictions.eq("idBank", param.getIdBank1()));

		}
		if (param.getVoucherNo() != null && param.getVoucherNo().length() > 0) {
			criteria.add(Restrictions.eq("voucherNo", param.getVoucherNo()));
			criteria1.add(Restrictions.eq("voucherNo", param.getVoucherNo()));
		}
		if (param.getStartTime() != null && param.getStartTime().length()>0) {
			criteria.add(Restrictions.ge("opDate", param.getStartTime()));
			criteria1.add(Restrictions.ge("opDate", param.getStartTime()));
		}
		if (param.getEndTime() != null && param.getEndTime().length()>0) {
			criteria.add(Restrictions.le("opDate", param.getEndTime()));
			criteria1.add(Restrictions.le("opDate", param.getEndTime()));
		}
		if (param.getOperLogModule() != null && param.getOperLogModule().length()>0) {
			criteria.add(Restrictions.eq("opMode", Integer.parseInt(param.getOperLogModule())));
			criteria1.add(Restrictions.eq("opMode", Integer.parseInt(param.getOperLogModule())));
		}
		if (param.getOpCode() != null && param.getOpCode().length() > 0) {
			criteria.add(Restrictions.eq("opCode", param.getOpCode()));
			criteria1.add(Restrictions.eq("opCode", param.getOpCode()));
		}
		List<CheckMainDataLog> result=null;
		if(isPaged){
		// 获取满足条件的数据总数
			Long longnumber = (Long)criteria.setProjection(
					Projections.rowCount()).uniqueResult();
		Integer countNumber	= Integer.parseInt(String.valueOf(longnumber));
//		Integer countNumber = (Integer) criteria.setProjection(
//				Projections.rowCount()).uniqueResult();
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		criteria1.setFirstResult(firstResult);
		criteria1.setMaxResults(pageSize);
		criteria1.addOrder(Order.asc("opDate"));
		result = criteria1.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		}else{
			result = criteria1.list();
		}
		session.close();
		return result;		
	}

	
}
