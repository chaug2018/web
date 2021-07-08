package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IAccnoDetailAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoDetailData;
import com.yzj.ebs.database.temp.hbm.EbsMarginData;

/**
 * 
 * 创建于:2012-10-16<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细查询数据查询类
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class AccnoDetailAdm extends BaseService<AccNoDetailData> implements
		IAccnoDetailAdm {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.IAccnoDetailAdm#queryAccnoDetailList(com.yzj
	 * .ebs.common.param.AccnoDetailQueryParam)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<AccNoDetailData> getAccnoDetailData(
			Map<String, String> queryMap, PageParam param, String entityName)
			throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forEntityName(entityName);
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					dc.add(Restrictions.eq(entry.getKey(), entry.getValue()));
				}
			}
		}
		// 获取结果条数
		dc.setProjection(Projections.rowCount());
		long countNumber = Long.parseLong(dao.getHibernateTemplate()
				.findByCriteria(dc).get(0).toString());
		dc.setProjection(null);

		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		param.setTotal((int) countNumber);// 所有符合条件的票据总数
		param.setTotalPage(totalPage);
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		// 分页查询。
		List<AccNoDetailData> resultList = (List<AccNoDetailData>) findbyPage(
				dc, firstResult, pageSize);
		param.setLastResult(firstResult + resultList.size());// 当前页显示的最后一条记录
		return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccNoDetailData> getAllAccnoDetailData(
			Map<String, String> queryMap, String entityName)
			throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forEntityName(entityName);
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					dc.add(Restrictions.eq(entry.getKey(), entry.getValue()));
				}
			}
		}
		List<AccNoDetailData> resultList = (List<AccNoDetailData>) findByDetachedCriteria(dc);
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.IAccnoDetailAdm#queryAccnoDetailList(com.yzj
	 * .ebs.common.param.AccnoDetailQueryParam)
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Object[]> getAccnoDetailDataByDocDate(
			Map<String, String> queryMap, PageParam param, String entityName,
			String workDate, boolean isPaged) throws XDocProcException {
		String preWorkDate = "";
	
		String sql = "select t.workdate,t.tracebal,t.dcflag,t.to_accno,t.to_accname,t.credit,t.idbank,t.traceno from "+
				 entityName+
				" t where 1=1 ";
		if(!workDate.equals("")){
			preWorkDate = workDate.substring(0,7)+"-"+"01";
			sql+= " and to_date(t.workDate,'yyyy-mm-dd') >=to_date('"+preWorkDate+"','yyyy-mm-dd') and to_date(t.workdate,'yyyy-mm-dd')<= to_date('"+workDate+"','yyyy-mm-dd') ";
		}
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {			
				sql+=" and t."+entry.getKey()+" = '"+entry.getValue()+"' ";
			}
		}	
		
		
		sql+="order by t.workdate,t.tracetime,t.traceno";
		SQLQuery sqlQuery = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object[]> list = sqlQuery.list();
		
		if(isPaged){
			int countNumber = list.size();	
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);     //总共几条
			param.setTotalPage(totalPage);
			if((countNumber - firstResult)>pageSize){
				param.setLastResult(firstResult + pageSize);// 当前页显示的最后一条记录
			}else{
				param.setLastResult( countNumber);// 当前页显示的最后一条记录
			}		
			int lastNumber = countNumber - firstResult;	
			if(lastNumber >= pageSize){
				return	list.subList(firstResult, firstResult+pageSize); 
			}else{
				return	list.subList(firstResult, firstResult+lastNumber);
			}	
		}else{
			return list;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AccNoDetailData> getAccnoDetailDataByVoucherno(
			String voucherno, String docdate)
			throws XDocProcException {
		AccNoDetailData ad = new AccNoDetailData();
		ad.setAbs("abs");
		ad.setAccNo("accNo");
		ad.setAccSon("accSon");
		ad.setAutoid((long) 1);
		ad.setCheckDate("checkDate");
		ad.setCheckFlag("checkFlag");
		ad.setCredit(2.0);
		ad.setCurrType("currType");
		ad.setDataDate("dataDate");
		ad.setDcFlag("dcFlag");
		ad.setIdBank("idBank");
		ad.setIdBranch("idBranch");
		ad.setIdCenter("idCenter");
		ad.setIdCenter("idCenter");
		ad.setVoucherNo("voucherNo");
		ad.setAccNo("accNo");
		ad.setStrtraceBal("strtraceBal");
		
		AccNoDetailData ad1 = new AccNoDetailData();
		ad1.setAbs("abs");
		ad1.setAccNo("accNo");
		ad1.setAccSon("accSon");
		ad1.setAutoid((long) 2);
		ad1.setCheckDate("checkDate");
		ad1.setCheckFlag("checkFlag");
		ad1.setCredit(2.0);
		ad1.setCurrType("currType");
		ad1.setDataDate("dataDate");
		ad1.setDcFlag("dcFlag");
		ad1.setIdBank("idBank");
		ad1.setIdBranch("idBranch");
		ad1.setIdCenter("idCenter");
		ad1.setIdCenter("idCenter");
		ad1.setVoucherNo("voucherNo");
		ad1.setAccNo("accNo");
		ad1.setStrtraceBal("strtraceBal");
		
		
		AccNoDetailData ad2 = new AccNoDetailData();
		ad2.setAbs("abs");
		ad2.setAccNo("accNo12");
		ad2.setAccSon("accSon");
		ad2.setAutoid((long) 3);
		ad2.setCheckDate("checkDate");
		ad2.setCheckFlag("checkFlag");
		ad2.setCredit(2.0);
		ad2.setCurrType("currType");
		ad2.setDataDate("dataDate");
		ad2.setDcFlag("dcFlag");
		ad2.setIdBank("idBank");
		ad2.setIdBranch("idBranch");
		ad2.setIdCenter("idCenter");
		ad2.setIdCenter("idCenter");
		ad2.setVoucherNo("voucherNo");
		ad2.setAccNo("accNo");
		ad2.setStrtraceBal("strtraceBal");
		
		List<AccNoDetailData> list = new ArrayList<AccNoDetailData>();
		list.add(ad);
		list.add(ad1);
		list.add(ad2);
		return  list;
//		String hql = "from ebs_AccnoDetailData_06 where voucherNo ='"+voucherno+"'";
//		return (List<AccNoDetailData>) dao.findByHql(hql);
		
//		DetachedCriteria dc = DetachedCriteria
//				.forEntityName("ebs_AccnoDetailData_" + docdate.substring(5, 7));
//		dc.add(Restrictions.eq("voucherNo", voucherno));
//		dc.addOrder(Order.asc("accNo"));
//		dc.addOrder(Order.asc("workDate"));
//		dc.addOrder(Order.asc("page_Num1"));// 按交易序号排序
//		dc.addOrder(Order.asc("page_Num2"));// 按交易序号排序
//		return (List<AccNoDetailData>) dao.findByDetachedCriteria(dc);
	}

	@Override
	public List<EbsMarginData> getMarginDataByVoucherno(String voucherno)
			throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria
				.forEntityName("Ebs_MarginData");
		dc.add(Restrictions.eq("voucherno", voucherno));
		dc.addOrder(Order.asc("acctype"));
		dc.addOrder(Order.asc("accno"));
		return (List<EbsMarginData>) dao.findByDetachedCriteria(dc);
	}

	@Override
	public List<AccNoDetailData> getAccnoDetailDataByWhere(
			String where,  String entityName)
			throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria
				.forEntityName(entityName);
		 dc.add(Restrictions
		 .sqlRestriction(where));
		dc.addOrder(Order.asc("accNo"));
		dc.addOrder(Order.asc("workDate"));
		dc.addOrder(Order.asc("pageNum1"));// 按交易序号排序
		dc.addOrder(Order.asc("pageNum2"));// 按交易序号排序
		return (List<AccNoDetailData>) dao.findByDetachedCriteria(dc);
	}
	
	
}
