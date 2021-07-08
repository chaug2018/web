package com.yzj.ebs.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IDetailMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.DetailMainData;
import com.yzj.ebs.util.UtilBase;

/**
 *创建于:2012-11-14<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 所有对账不对账账户dao操作实现
 * @author lif
 * @version 1.0
 */
public class DetailMainDataAdm extends BaseService<DetailMainData> implements IDetailMainDataAdm{

	

	@Override
	public List<DetailMainData> getDetailMainDataByDocDate(
			Map<String, String> queryMap, PageParam param)
			throws XDocProcException {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(DetailMainData.class);
		Criteria criteria1 = session.createCriteria(DetailMainData.class);
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if("accCycle".equals(entry.getKey())){
						criteria.add(Restrictions.ne(entry.getKey(),
								entry.getValue()));
						criteria1.add(Restrictions.ne(entry.getKey(),
								entry.getValue()));
					}else{
						criteria.add(Restrictions.eq(entry.getKey(),
								entry.getValue()));
						criteria1.add(Restrictions.eq(entry.getKey(),
								entry.getValue()));
						//criteria1.setProjection(Projections.groupProperty(""));
					}
				}
				
			}
		}
		criteria.add(Restrictions.in("sendMode", new String[]{"1","2","0"}));
		criteria1.add(Restrictions.in("sendMode", new String[]{"1","2","0"}));
		criteria.add(Restrictions.isNotNull("voucherNo"));
		criteria1.add(Restrictions.isNotNull("voucherNo"));
		criteria.addOrder(Order.asc("voucherNo"));
		criteria1.addOrder(Order.asc("voucherNo"));
		criteria.addOrder(Order.asc("accNoIndex"));
		criteria1.addOrder(Order.asc("accNoIndex"));
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
		List<DetailMainData> result = criteria1.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		session.close();
		return result;
	}
	
	/**
	 * 导出数据
	 * @param docdate
	 * @return
	 * @throws XDocProcException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DetailMainData> exportTxtDate(Map<String,String> queryMap) throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forClass(DetailMainData.class);
		dc.add(Restrictions.ne("accCycle", "0"));
		dc.add(Restrictions.isNotNull("voucherNo"));
		dc.add(Restrictions.in("sendMode", new String[]{"1","2","0"}));
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					dc.add(Restrictions.eq(entry.getKey(),
								entry.getValue()));
				}
				
			}
		}
		dc.addOrder(Order.asc("voucherNo"));
		dc.addOrder(Order.asc("idBank"));
		dc.addOrder(Order.asc("accNoIndex"));
		
		return (List<DetailMainData>) dao.findByDetachedCriteria(dc);
	}
	/**
	 *   把账单的发送方式 改为以发送 更新发送日期 
	 */
	@Override  
	public void updateCheckmaindataByDocdate(String docdate,Map<String,String>mapdata) throws XDocProcException {
		
 		String docState ="update CheckMainData set docState = '2', sendDate = '"+
				     UtilBase.getNowDate() + "'"+
				     "  where docState<>'3' and docDate = '" + docdate.replace("-", "")+"' " ;
		String query="";
		if (mapdata != null) {
			for (Map.Entry<String, String> entry : mapdata.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0)
					if ("sendModeType".equals(entry.getKey())) {
						query +="and sendmode in ("+entry.getValue()+")";
					}else{
						query+=" and "+entry.getKey()+"="+entry.getValue();
					}
			}
		}
		docState+=query;
		super.ExecQuery(docState);
	}
	/**
	 *  把对账单的 发送次数+1
	 * @param sendmode
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updatePrintTime (Map<String,String>mapdata)throws XDocProcException{		
		String printTimes ="update ebs_checkmaindata set printtimes= printtimes+1 where 1=1 ";
		String strQuery="";
		if (mapdata != null) {
			for (Map.Entry<String, String> entry : mapdata.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0)
					
					if ("sendModeType".equals(entry.getKey())) {
						strQuery +="and sendmode in ("+entry.getValue()+")";
					}else{
						strQuery+=" and "+entry.getKey()+"="+entry.getValue();
					}
			}
		}
		printTimes+=strQuery;		
		try{
			SQLQuery query = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(printTimes);
			query.executeUpdate();			
		}catch  (Exception e) {
			throw new XDocProcException("执行查询 " + printTimes + " 失败", e);
		}
	}
}
