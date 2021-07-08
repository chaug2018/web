package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IExChangeBookAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.database.ExChangeBook;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * Basicinfo表操作访问服务接口定义
 * 
 * @author 秦靖锋
 * @version 1.0.0
 */
public class ExChangeBookAdm extends BaseService<ExChangeBook> implements
IExChangeBookAdm {

	@SuppressWarnings("unchecked")
	@Override
	public List<ExChangeBook> getExChangeBook(Map<String, String> queryMap,
			PageParam param,BankParam bankparam) throws XDocProcException {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(ExChangeBook.class);
		Criteria criteria1 = session.createCriteria(ExChangeBook.class);
		
		/***
		 * 控制
		 */
		Short level = bankparam.getLevel();
		if (level == 1) {//总行
			// 对账中心 
			criteria.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
			criteria1.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
		} else if (level == 2) {//分行
			// 对账中心 
			criteria.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
			criteria1.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
		} else if (level == 3) {//支行
			// 对账中心 
			criteria.add(Restrictions.eq("idBranch", bankparam.getIdBranch()));
			criteria1.add(Restrictions.eq("idBranch", bankparam.getIdBranch()));
		} else if (level == 4) {//网点
			criteria.add(Restrictions.eq("idBank", bankparam.getIdBank()));
			criteria1.add(Restrictions.eq("idBank", bankparam.getIdBank()));
		}
		
		
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					criteria.add(Restrictions.eq(entry.getKey(),
							entry.getValue()));
					criteria1.add(Restrictions.eq(entry.getKey(),
							entry.getValue()));
				}
			}
		}
		criteria1.addOrder(Order.desc("idBank"));
		Long countNumberTmp = (Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		int countNumber = countNumberTmp.intValue();

		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		criteria1.setFirstResult(firstResult);
		criteria1.setMaxResults(pageSize);
		List<ExChangeBook> result = criteria1.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		session.close();
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ExChangeBook> getAllExChangeBook(Map<String, String> queryMap,BankParam bankparam) {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria1 = session.createCriteria(ExChangeBook.class);
		/***
		 * 控制
		 */
		Short level = bankparam.getLevel();
		if (level == 1) {//总行
			// 对账中心 
			criteria1.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
		} else if (level == 2) {//分行
			// 对账中心 
			criteria1.add(Restrictions.eq("idCenter", bankparam.getIdCenter()));
		} else if (level == 3) {//支行
			// 对账中心 
			criteria1.add(Restrictions.eq("idBranch", bankparam.getIdBranch()));
		} else if (level == 4) {//网点
			criteria1.add(Restrictions.eq("idBank", bankparam.getIdBank()));
		}
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					criteria1.add(Restrictions.eq(entry.getKey(),
							entry.getValue()));
				}
			}
		}
		criteria1.addOrder(Order.desc("idBank"));
		List<ExChangeBook> result = criteria1.list();
		session.close();
		return result;
	}
}
