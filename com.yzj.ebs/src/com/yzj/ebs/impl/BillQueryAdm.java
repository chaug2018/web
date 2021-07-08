package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.yzj.ebs.common.IBillQueryAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

public class BillQueryAdm  extends BaseService<CheckMainData>  implements IBillQueryAdm {
	
	/** 分页查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillListByPage(Map<String, String> queryMap,PageParam queryParam) throws XDocProcException {
		
		
		 try {
			 
			String docId = queryMap.get("docId");//业务流水
			String voucherNo = queryMap.get("voucherNo");//账单编号 
			String docFlag = queryMap.get("docFlag");//票据状态 
			String idCenter =queryMap.get("idCenter");
			String idBranch=queryMap.get("idBranch");
			String idBank = queryMap.get("idBank");//网点号
			String countQueryString = " select count(*) from DocSet a ";
			String listQueryString = " from DocSet a ";
			String conditionString = " where 1=1 ";
			// 拼装查询条件
			if(!isEmpty(queryMap.get("startDate").trim())) {
				
				conditionString += "and a.workDate >= '" + queryMap.get("startDate").trim() + "'";
			}
			
			if(!isEmpty(queryMap.get("endDate").trim())) {
				 
				conditionString += " and a.workDate <= '" + queryMap.get("endDate").trim() + "'";
			}
			
			if(!isEmpty(queryMap.get("checkDate").trim())) {
			    String DocDate = queryMap.get("checkDate").substring(0, 4).trim()
						+ queryMap.get("checkDate").substring(5, 7).trim() + queryMap.get("checkDate").substring(8).trim();
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
            
			List<DocSet> billList = new ArrayList<DocSet>();
			billList = (List<DocSet>) dao
			.getPageByParamForSql(listQueryString+conditionString, countQueryString+conditionString, queryParam);
			return billList;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/** 查询票据信息列表  ebs_docset
	 * @throws XDocProcException */
	public List<DocSet> queryBillList(Map<String, String> queryMap) throws XDocProcException {
		 try {
			 String docId = queryMap.get("docId");//业务流水
				String voucherNo = queryMap.get("voucherNo");//账单编号 
				String docFlag = queryMap.get("docFlag");//票据状态 
				String idCenter =queryMap.get("idCenter");
				String idBranch=queryMap.get("idBranch");
				String idBank = queryMap.get("idBank");//网点号
			
			String listQueryString = "from DocSet a  ";
			String conditionString = "where 1=1  ";
			if(!isEmpty(queryMap.get("startDate").trim())) {
				
				conditionString += "and a.workDate >= '" + queryMap.get("startDate").trim() + "'";
			}
			
			if(!isEmpty(queryMap.get("endDate").trim())) {
				 
				conditionString += " and a.workDate <= '" + queryMap.get("endDate").trim() + "'";
			}
			
			if(!isEmpty(queryMap.get("checkDate").trim())) {
			    String DocDate = queryMap.get("checkDate").substring(0, 4).trim()
						+ queryMap.get("checkDate").substring(5, 7).trim() + queryMap.get("checkDate").substring(8).trim();
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
		
			List<DocSet> billList = new ArrayList<DocSet>();
			billList = (List<DocSet>) dao.findByHql(listQueryString);
			return billList;
		} catch (RuntimeException re) {
			throw re;
		}
	}
	/** 根据docId获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByVoucherNo(String docId) throws XDocProcException {
		DocSet docSet = null;
		String queryString = "from DocSet a where a.docId='" + docId + "'";
		List<Object[]> MyList =(List<Object[]>)dao.find(queryString);
			Object obj = MyList.get(0);
			docSet=(DocSet)obj;
		
		if(docSet!=null){
			if (StringUtils.isNotEmpty(docSet.getVoucherNo())) {
				docSet=null;
				String myString ="from DocSet d ,AccNoMainData a where a.voucherNo=d.voucherNo and d.docId='"+docId+"'"+" and order by a.accNoIndex";//增加按账号打印序号排序
				List<?> resultList=(List<?>)dao.find(myString);
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
	/** 根据账单编号获取账单明细信息 
	 * @throws XDocProcException */
	public DocSet queryDocSetByDocId(String docId) throws XDocProcException {
		String queryString = "from DocSet a where  a.docId = '" + docId +"'";
		DocSet docSet=null;
		List<DocSet> billList = new ArrayList<DocSet>();
		List<Object[]> resultList = (List<Object[]>)dao.find(queryString);
		if(resultList != null && resultList.size() > 0){
			Object[] obj = resultList.get(0);
                
		}

		return docSet;
	}
	/** 获取日志列表 
	 * @throws XDocProcException */
	public List<DocLog> queryDocLogList(String docId) throws XDocProcException {
		List<DocLog> docLogList = new ArrayList<DocLog>();
		String queryString = "from DocLog where docId = '" + docId+"' order by autoId asc ";
		docLogList = (List<DocLog>)dao.find(queryString);
		return docLogList;
	}
	
	/** 执行hql语句 */
	public void executeHql(String hql) {
		final String sql;
		sql = hql;
		dao.getHibernateTemplate().execute(new HibernateCallback() {
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