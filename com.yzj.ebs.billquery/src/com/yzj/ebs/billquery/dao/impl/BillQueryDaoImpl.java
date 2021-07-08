package com.yzj.ebs.billquery.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.yzj.ebs.billquery.dao.IBillQueryDao;
import com.yzj.ebs.billquery.dao.pojo.QueryParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public class BillQueryDaoImpl extends HibernateDaoSupport  implements IBillQueryDao {
	
	/** 分页查询票据信息列表  ebs_docset*/
	public List<DocSet> queryBillListByPage(QueryParam queryParam) {
		
		
		 try {
			 
			String docId = queryParam.getDocId();//业务流水
			String voucherNo = queryParam.getVoucherNo();//账单编号 
			String docFlag = queryParam.getDocFlag();//票据状态 
			String idCenter =queryParam.getIdCenter();
			String idBranch=queryParam.getIdBranch();
			String idBank = queryParam.getIdBank();//网点号
			String countQueryString = " select count(*) from DocSet a ";
			String listQueryString = " from DocSet a ";
			String conditionString = " where 1=1 ";
			// 拼装查询条件
			if(!isEmpty(queryParam.getStartDate().trim())) {
				
				conditionString += "and a.workDate >= '" + queryParam.getStartDate().trim() + "'";
			}
			
			if(!isEmpty(queryParam.getEndDate().trim())) {
				 
				conditionString += " and a.workDate <= '" + queryParam.getEndDate().trim() + "'";
			}
			
			if(!isEmpty(queryParam.getCheckDate().trim())) {
			    String DocDate = queryParam.getCheckDate().substring(0, 4).trim()
						+ queryParam.getCheckDate().substring(5, 7).trim() + queryParam.getCheckDate().substring(8).trim();
				conditionString += " and a.docDate = '" + DocDate + "'";
			}
			
			if(!isEmpty(docId)) {
				conditionString += " and a.docId = '" + docId + "'";
			}
			
			if(!isEmpty(voucherNo)) {
				conditionString += " and a.voucherNo = '" + voucherNo + "'";
			}
			
			
			if(!isEmpty(idBank)) {
				conditionString += " and a.idBank = '" + idBank + "'";
			}
			
			if(!isEmpty(idBranch)) {
				conditionString += " and a.idBranch = '" + idBranch + "'";
			}
			if(!isEmpty(idCenter)) {
				conditionString += " and a.idCenter = '" + idCenter + "'";
			}
			if(!isEmpty(docFlag)) {
				conditionString += " and a.docFlag = '" + docFlag + "'";
			}
			
			conditionString+=" order by a.docId ";
			listQueryString += conditionString;//票据列表查询sql
			countQueryString += conditionString;//票据列表查询总数统计sql
			Session session = getSession();
			Query countQueryObject = (Query) session.createQuery(countQueryString);
			List countList = countQueryObject.list();
			Object countObject = countList.get(0);
			String countStr = String.valueOf(countObject);
			int countNumber = Integer.valueOf(countStr);
			
			int pageSize = queryParam.getPageSize();//每页显示结果条数
			int totalPage = (countNumber - 1) /pageSize + 1;//总页数
			int curPage = queryParam.getCurPage();//当前要显示的页
			if(curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;//分页时显示的第一条记录，默认从0开始
			queryParam.setFirstResult(firstResult);
			queryParam.setCurPage(curPage);
			Query listQueryObject = session.createQuery(listQueryString);
			listQueryObject.setFirstResult(firstResult);
			listQueryObject.setMaxResults(pageSize);
			List<DocSet> billList = new ArrayList<DocSet>();
			billList = listQueryObject.list();

			queryParam.setTotal(countNumber);//所有符合条件的票据总数
			queryParam.setTotalPage(totalPage);
			queryParam.setLastResult(firstResult + billList.size());//当前页显示的最后一条记录
						
			session.close();
			return billList;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/** 查询票据信息列表  ebs_docset*/
	public List<DocSet> queryBillList(QueryParam queryParam) {
		 try {
			
			String docId = queryParam.getDocId();//业务流水
			String voucherNo = queryParam.getVoucherNo();//账单编号 
			String idCenter =queryParam.getIdCenter();
			String idBranch=queryParam.getIdBranch();
			String idBank = queryParam.getIdBank();//
			String docFlag = queryParam.getDocFlag();//票据状态 
			
			String countQueryString = "select count(*) from DocSet a  ";
			String listQueryString = "from DocSet a  ";
			String conditionString = "where 1=1  ";
			if(!isEmpty(queryParam.getStartDate().trim())) {
				
				conditionString += "and a.workDate >= '" + queryParam.getStartDate().trim() + "'";
			}
			
			if(!isEmpty(queryParam.getEndDate().trim())) {
				 
				conditionString += " and a.workDate <= '" + queryParam.getEndDate().trim() + "'";
			}
			
			if(!isEmpty(queryParam.getCheckDate().trim())) {
			    String DocDate = queryParam.getCheckDate().substring(0, 4).trim()
						+ queryParam.getCheckDate().substring(5, 7).trim() + queryParam.getCheckDate().substring(8).trim();
				conditionString += " and a.docDate = '" + DocDate + "'";
			}
			
			if(!isEmpty(docId)) {
				conditionString += " and a.docId = '" + docId + "'";
			}
			
			if(!isEmpty(voucherNo)) {
				conditionString += " and a.voucherNo = '" + voucherNo + "'";
			}
		
			
			if(!isEmpty(idBank)) {
				conditionString += " and a.idBank = '" + idBank + "'";
			}
			
			if(!isEmpty(idBranch)) {
				conditionString += " and a.idBranch = '" + idBranch + "'";
			}
			if(!isEmpty(idCenter)) {
				conditionString += " and a.idCenter = '" + idCenter + "'";
			}
			
			if(!isEmpty(docFlag)) {
				conditionString += " and a.docFlag = '" + docFlag + "'";
			}
			
			
			conditionString+=" order by a.docId ";
			listQueryString += conditionString;//票据列表查询sql
			countQueryString += conditionString;//票据列表查询总数统计sql
			Session session = getSession();
			Query countQueryObject = (Query) session.createQuery(countQueryString);
			List countList = countQueryObject.list();
			Object countObject = countList.get(0);
			Query listQueryObject = session.createQuery(listQueryString);
			
			List<DocSet> billList = new ArrayList<DocSet>();
			billList = listQueryObject.list();
			session.close();
			return billList;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/** 根据docId获取账单明细信息 */
	public DocSet queryDocSetByVoucherNo(String docId) {
		DocSet docSet = null;
		String queryString = "from DocSet a where a.docId='" + docId + "'";
		List<Object[]> MyList =(List<Object[]>)getHibernateTemplate().find(queryString);
			Object obj = MyList.get(0);
			docSet=(DocSet)obj;
		
		if(docSet!=null){
			if (StringUtils.isNotEmpty(docSet.getVoucherNo())) {
				docSet=null;
				String myString ="from DocSet d ,AccNoMainData a where a.voucherNo=d.voucherNo and d.docId='"+docId+"'"+" and order by a.accNoIndex";//增加按账号打印序号排序
				List<?> resultList=(List<?>)getHibernateTemplate().find(myString);
				for(int i = 0 ; i < resultList.size(); i++) {
					Object[] obj1 = (Object[])resultList.get(i);
					    if(docSet==null){
					    	
					    	docSet = (DocSet)obj1[0];
							List<AccNoMainData> accnoMainDataList = new ArrayList<AccNoMainData>();
							docSet.setAccnoMainDataList(accnoMainDataList);
					    }
					
					    AccNoMainData accnoMainData = (AccNoMainData)obj1[1];
					    docSet.getAccnoMainDataList().add(accnoMainData);
			}
		}
		}
		return docSet;
	}
	/** 根据账单编号获取账单明细信息 */
	public DocSet queryDocSetByDocId(String docId) {
		String queryString = "from DocSet a where  a.docId = '" + docId +"'";
		DocSet docSet=null;
		List<DocSet> billList = new ArrayList<DocSet>();
		List<Object[]> resultList = (List<Object[]>)getHibernateTemplate().find(queryString);
		if(resultList != null && resultList.size() > 0){
			Object[] obj = resultList.get(0);
                
		}

		return docSet;
	}
	/** 获取日志列表 */
	public List<DocLog> queryDocLogList(String docId) {
		List<DocLog> docLogList = new ArrayList<DocLog>();
		String queryString = "from DocLog where docId = '" + docId+"' order by autoId asc ";
		docLogList = (List<DocLog>)getHibernateTemplate().find(queryString);
		return docLogList;
	}
	
	/** 执行hql语句 */
	public void executeHql(String hql) {
		final String sql;
		sql = hql;
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException {
				String querySentence = sql;
				Query query = session.createQuery(querySentence);
				return Integer.valueOf(query.executeUpdate());
			}
		});
	}
	
	/** 判断是否为空 **/
	private boolean isEmpty(String str) {
		boolean isEmpty = true;
		if(str != null && str.trim().length() > 0) {
			isEmpty = false;
		}
		return isEmpty;
	}
}