package com.yzj.ebs.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.INotMatchTableAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.NotMatchTable;

/**
 * 
 * 创建于:2012-9-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据库更新服务类
 * 
 * @author ShiJiangmin
 * @version 1.0.0
 */
public class NotMatchTableAdm extends BaseService<NotMatchTable> implements
		INotMatchTableAdm {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#saveNotMatchItems(java.util
	 * .List<com.yzj.ebs.database.NotMatchTable>)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void saveNotMatchItems(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		for (NotMatchTable notMatchTable : notMatchList) {
			create(notMatchTable);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#deleteNotMatchList(java.util
	 * .List<com.yzj.ebs.database.NotMatchTable>)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		deleteAll(notMatchList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#getNotMatchListByDocId(
	 * java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<NotMatchTable> getNotMatchListByDocId(String docId)
			throws XDocProcException {
		String listHql = "from NotMatchTable where 1=1 ";
		String conditionStr = " and  docId = '" + docId + "'";
		listHql += conditionStr;
		List<NotMatchTable> resultList = new ArrayList<NotMatchTable>();
		resultList = (List<NotMatchTable>) findByHql(listHql);
		return resultList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#getNotMatchTableData(
	 * java.util.Map<String, String>,com.yzj.ebs.common.param.PageParam)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotMatchTable> getNotMatchTableData(
			Map<String, String> queryMap, PageParam param)
			throws XDocProcException {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(NotMatchTable.class);
		Criteria criteria1 = session.createCriteria(NotMatchTable.class);
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
		criteria1.addOrder(Order.asc("docId"));
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
		List<NotMatchTable> result = criteria1.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		session.close();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#getNotMatchTableData(
	 * java.util.Map<String, String>)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NotMatchTable> getAllNotMatchData(Map<String, String> queryMap)
			throws XDocProcException {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria1 = session.createCriteria(NotMatchTable.class);
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(!"inputOpTime".equals(entry.getKey())){
						criteria1.add(Restrictions.eq(entry.getKey(),
								entry.getValue()));
					}else{
						criteria1.add(Restrictions.like(entry.getKey(), entry.getValue()));
					}
				}
			}
		}
		criteria1.addOrder(Order.asc("docId"));
		List<NotMatchTable> result = criteria1.list();
		session.close();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.ebs.common.INotMatchTableAdm#updateNotMatchList(java.util
	 * .List<com.yzj.ebs.database.NotMatchTable>)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void updateNotMatchList(List<NotMatchTable> notMatchList)
			throws XDocProcException {
		String now = new SimpleDateFormat("yyyyMMdd").format(new Date());
		for (NotMatchTable notMatchTable : notMatchList) {
			update(notMatchTable);
			// 更新未达表后更新发生额明细表
						String accNo = notMatchTable.getAccNo();
						String traceNo = notMatchTable.getTraceNo();
						
						List<CheckMainData> list = (List<CheckMainData>) this.getDao().findByHql("from CheckMainData where voucherNo='"+notMatchTable.getVoucherNo()+"'");
						if(list != null && list.size() > 0)
						{
						String docDate = list.get(0).getDocDate();
						if(StringUtils.isNotEmpty(docDate) && docDate.length() == 8 )
						{
						String sql  = "update ebs_AccnoDetailData_"+docDate.substring(4,6) +" set checkdate='"+now+"', " +
								" checkFlag = '"+notMatchTable.getCheckFlag()+"' where accNo = '"+accNo+"' and traceNo = '"+traceNo+"'";
						ExecQuery(sql);
						}
						}
		}
	}
	
	//未达账情况统计
	@SuppressWarnings("rawtypes")
	@Override
	public List getAnalyseresults(Map<String, String> queryMap,
			PageParam pageParam,boolean isPaged) throws XDocProcException {
		String selectsql = "select t2.idCenter,t2.idBranch,t2.idBank,t2.bankName,t2.docDate,  "
				+ "count(case when t2.docstate > 1 and t1.matchFlag=1 then t1.accno end) as notMatchCount,"  //未达账户数目
				+ "count(case when t2.docstate > 1 and t1.finalcheckFlag=4  and t1.matchFlag=1 then t1.accno end) " +
				"as checkMatchCount," //人工调节余额相符
				+ "count(case when t2.docstate > 1 and t1.finalcheckFlag=5 and t1.matchFlag=1 then t1.accno end) " +
				"as checkNotMatchCount,"  //人工调节余额不符
				+ "count(case when t2.docstate > 1 then t1.accno end) as sendCount  ";
		String selectcount = " select count(distinct t2.idBank) ";
		String fromsql = " from autek.ebs_accnomaindata t1,autek.ebs_checkmaindata t2 ";
		
		String wheresql = " where t1.voucherno=t2.voucherno ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					wheresql += " and t2." + entry.getKey() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		String groupsql = " group by t2.idCenter,t2.idBranch,t2.idBank,t2.bankName,t2.docDate order by t2.idcenter,t2.idbank";
		
		String querysql = selectsql+fromsql+wheresql+groupsql;
		String countsql = "select  count(1) from ("+selectcount+fromsql+wheresql+groupsql+")";//用于查询记录数目的sql
		if(isPaged){
			return dao.getByPageFromSql(querysql, countsql, pageParam);
		}else{
			return dao.findBySql(querysql);
		}
	}
	
	//按单位统计未达账情况
	@Override
	public List<?> getAnalyseresultsCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String selectsql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectsql += "t2.idCenter, ";
		}
		selectsql += "t2.docDate,  "
					+ "count(case when t2.docstate > 1 and t1.matchFlag=1 then t1.accno end) as notMatchCount,"
					+ "count(case when t2.docstate > 1 and t1.finalcheckFlag=4  and t1.matchFlag=1 then t1.accno end) as checkMatchCount,"
					+ "count(case when t2.docstate > 1 and t1.finalcheckFlag=5 and t1.matchFlag=1 then t1.accno end) as checkNotMatchCount,"
					+ "count(case when t2.docstate > 1 then t1.accno end) as sendCount  ";
		
		String selectcount = " select  ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectcount += "count(distinct t2.idCenter) ";
		}else{
			selectcount += "count(1) ";
		}
		String fromsql = " from autek.ebs_accnomaindata t1,autek.ebs_checkmaindata t2 ";
		String wheresql = " where t1.voucherno=t2.voucherno  ";
		//遍历查询Map
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					wheresql += " and t2." + entry.getKey() + "='"
							+ entry.getValue().trim() + "'";
				}
			}
		}
		String groupsql = " group by t2.docDate";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupsql += ",t2.idCenter";
			orderSql += " order by t2.idcenter";
		}
		
		String querysql = selectsql+fromsql+wheresql+groupsql+orderSql;
		String countsql = "select  count(1) from ("+selectcount+fromsql+wheresql+groupsql+")";//用于查询记录数目的sql
		if(isPaged){
			return dao.getByPageFromSql(querysql, countsql, pageParam);
		}else{
			return dao.findBySql(querysql);
		}
	}
	
	//获得所有对账中心的名字
	@Override
	public List<?> getAllIdBranchName() throws XDocProcException {
		// TODO Auto-generated method stub
		//String sql = "select b.idbank, b.cname from param_bank b join (select t.idbranch from PARAM_BANK t group by t.idbranch) a on (a.idbranch = b.idbank)";
		String sql = "select b.idbank, b.cname from param_bank b where b.nlevel<3";
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}

	

	@Override
	public void updateAccnomaindata(String accno, String checkfalg,
			String voucherno) throws XDocProcException {
		String[] temp = accno.split("[-]");
		String sql;
		if (temp.length == 2) {
			sql = "update AccNoMainData set checkFlag='" + checkfalg
					+ "' where voucherNo='" + voucherno + "' and accNo='"
					+ temp[0] + "' and  accNoSon ='" + temp[1]
					+ "'";
		} else {
			sql = "update AccNoMainData set checkFlag='" + checkfalg
					+ "' where voucherNo='" + voucherno + "' and accNo='"
					+ temp[0] + "'";
		}
		ExecQuery(sql);
	}

	



}
