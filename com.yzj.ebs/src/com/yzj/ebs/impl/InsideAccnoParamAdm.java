package com.yzj.ebs.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InsideAccnoParam;


/**
 * 创建于:2013-8-16<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * Basicinf表操作访问服务接口定义
 * 
 * @author j_sun
 * @version 1.0.0
 */
public class InsideAccnoParamAdm extends BaseService<InsideAccnoParam> implements IInsideAccnoParam{

	/**
	 * 查询InsideAccnoParam
	 */
	@SuppressWarnings("unchecked")
	public List<InsideAccnoParam> getInfor(Map<String, String> queryMap,
			PageParam param)throws XDocProcException{
		
		List<InsideAccnoParam> list  = new ArrayList<InsideAccnoParam>();
		StringBuffer hql = new StringBuffer();
		hql.append("from InsideAccnoParam where 1=1 ");	
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {			
				hql.append(" and ").append(entry.getKey()).append("='").append(entry.getValue()).append("'");
			}
		}	
		hql.append(" and flog = 0");
		hql.append(" order by insideuser,insideaccno");
		list = (List<InsideAccnoParam>) dao.findByHql(hql.toString());		
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
		
		
	}
		
	/**
	 * 修改InsideAccnoParam
	 *  
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public int changeInfor(String accNo1, String custId, String accNo2)throws SQLException{
		String sql = "update ebs_insideaccnoparam t set t.insideaccno=  '" +accNo1+"' "+
				 " where t.flog='0' and " +
				 " t.insideaccno=  '" +accNo2+"'"+
				 " and t.insideuser ='"+custId+"' ";
		update(sql);
		return 1;
	}
	 
	 /**
	 *  确认 accno 和 custid  的唯一性
	 *  如果 已存在 返回 1 不存在 0
	 * @throws SQLException 
	*/
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	 public int ifExistAccnoInParam(String accno) throws SQLException{
		 String sql = "select  t.insideaccno, t.insideuser  from ebs_insideaccnoparam t where t.flog='0' and" +
		 		" t.insideaccno = '" +accno+"' ";
		 String[] param = {"accno","custId"};
		 return queryObjectList(param,sql).size();
	 }
	 
	 
	/**
	 * 增加InsideAccnoParam 
	 */
	public void putInfor(InsideAccnoParam inside )throws XDocProcException{
		dao.saveOrUpdate(inside);		
	}
	
	/**
	 * 删除InsideAccnoParam
	 * flog 1删除 0存在
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteInfor(String accNo, String custId,String recheckCustId)throws SQLException{
		String sql = "update ebs_insideaccnoparam t set t.flog= '1' "+
					 " where t.insideaccno=  '" +accNo+"'"+
					 " and t.insideuser = '"+custId+"' and t.insiderecheckuser='"+recheckCustId+"'";
		update(sql);
	}
	
	/**
	 * 根据 输入的内部账号 查询 该账号是否存在于ebs_innerbasicinfo 中
	 *  不存在返回 0 , 存在 返回 大于0的数
	 */
	public int checkInnerAccNo(String accNo)throws XDocProcException{
		String sql = "select * from ebs_inneraccno t where t.accno= '"+accNo+"'";
		return dao.findBySql(sql).size();
	}
	
	/**
	 * 根据 输入的custId 查询 该custId是否存在于po_peopleinfo 中(peoplestate=0)
	 *  不存在返回 0 , 存在 返回 大于0的数
	 */
	public int checkCustId(String custId)throws XDocProcException{
		String sql = "select * from infotech.po_peopleinfo where peoplestate=0 and peoplecode= '"+custId+"'";
		return dao.findBySql(sql).size();
	}
	
	/**
	 * 检测custId和recheckCustId是否同一部门
	 */
	public int checkCustIdIDBank(String custId,String recheckCustId) throws XDocProcException{
		String sql = "select * from infotech.po_peopleinfo where peoplestate=0 and peoplecode= '"+custId
				+"' and organizeinfo=(select organizeinfo from infotech.po_peopleinfo where peoplestate=0 and peoplecode= '"+recheckCustId+"')";
		return dao.findBySql(sql).size();
	}
	
	/**
	 * 内部账户 核对  查询
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Map<String,Object>> queryInnerCheckResult(Map<String,String> queryMap,PageParam param) throws SQLException{
		String sql="select distinct t.accno,t.datadate,t.bal,t.abs ,t.result,t.recheck from ebs_inneraccnomaindata t "+ 
				  // "left join ebs_insideaccnoparam t1 on t.accno = t1.insideaccno "+
				   "where 1=1  ";
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {	
//				if(entry.getKey().equals("custId")){
//					sql+=" and t1.insideuser="+"'"+entry.getValue()+"'";
//				}else{
					sql+=" and t."+entry.getKey()+"="+"'"+entry.getValue()+"' ";
//				}
			}
		}
		sql+=" order by t.accno ";
		String[] paramStr = {"accno","datadate","bal","abs","result","recheck"};
		List<Map<String,Object>> list =  queryObjectList(paramStr ,sql);
		
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
	}
	
	/**
	 *  内部账户 核对 更新
	 * @throws XDocProcException 
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void innerAccnoCheck (Map<String,String> updateMap) throws SQLException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String nowTime = sf.format(new Date());
//		String sql = "update ebs_inneraccnomaindata t set t.result='" +updateMap.get("result")+ "',resultdate='"+nowTime
//				+ "',resultpeoplecode='" +updateMap.get("custId")+ "' ";
//		if(updateMap.get("abs")!=null && updateMap.get("abs").length()>0){
//			sql+=",t.abs='" +updateMap.get("abs")+ "' ";
//		}
//		sql+=",t.idcenter=(select organizeinfo from infotech.po_peopleinfo where peoplecode='" +updateMap.get("custId")+ "') ";
//		String wheresql=" where t.accno = '"+updateMap.get("accno")+"' and t.datadate='"+updateMap.get("datadate")+"'";
//		update(sql+wheresql);
		
		String sql = "update ebs_inneraccnomaindata t set t.result='" +updateMap.get("result")
					+ "',resultdate='"+nowTime+ "',t.abs='" +updateMap.get("abs")
					+ "' where t.accno = '"+updateMap.get("accno")+"' and t.datadate='"+updateMap.get("datadate")+"'";
		update(sql);
	}
	
	
	
	/**
	 * 公用 SQL update 方法  
	 */
	
	public void update(String sql)throws SQLException {
		try{
			SQLQuery query = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
			query.executeUpdate();
		}catch(Exception e){
			throw new SQLException();
		}
	}

	/**
	 * 公用查询list 方法
	 */
	public List<Map<String,Object>> queryObjectList(String[] resultPar,String sql)throws SQLException{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		
		SQLQuery sqlQuery = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Object[]> temp = sqlQuery.list();
		for(int i=0;i<temp.size();i++){
			Object[] obj = temp.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int j=0;j<obj.length;j++){
				map.put(resultPar[j], obj[j]);
			}
			list.add(map);
		}
		return list;
	}
	
	
	/**
	 * 内部账户 核对  查询
	 */
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Map<String,Object>> queryInnerRecheckResult(Map<String,String> queryMap,PageParam param) throws SQLException{
		String sql="select distinct t.accno,t.datadate,t.bal,t.abs ,t.result,t.recheck from ebs_inneraccnomaindata t "+ 
				  // "left join ebs_insideaccnoparam t1 on t.accno = t1.insideaccno "+
				   "where 1=1 ";
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {	
//				if(entry.getKey().equals("custId")){
//					sql+=" and t1.insideuser in (select p.peoplecode from infotech.po_peopleinfo p where p.peoplestate=0 " +
//							" and p.organizeinfo=(select organizeinfo from infotech.po_peopleinfo where peoplecode='"+entry.getValue()+"')) ";
//				}else{
					sql+=" and t."+entry.getKey()+"="+"'"+entry.getValue()+"' ";
//				}
			}
		}
		sql+=" order by t.accno ";
		String[] paramStr = {"accno","datadate","bal","abs","result","recheck"};
		List<Map<String,Object>> list =  queryObjectList(paramStr ,sql);
		
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
	}
	
	 
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updateRecheck (Map<String,String> updateMap) throws SQLException{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String nowTime = sf.format(new Date());
		String sql = "update ebs_inneraccnomaindata t set t.recheck='" +updateMap.get("recheck")+ "',recheckdate='"+nowTime
				//+ "',recheckpeoplecode='" +updateMap.get("custId")
				+ "' where t.accno = '"+updateMap.get("accno")+"' and t.datadate='"+updateMap.get("datadate")+"'";
		
		update(sql);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Map<String,Object>> queryInnerList(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException{
		String sql="select t.accno,t.datadate,t.bal,t.abs,t.result,t.resultpeoplecode,t.resultdate,t.recheck,t.recheckpeoplecode,t.recheckdate,t.idcenter from ebs_inneraccnomaindata t where 1=1 ";
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {
				if(entry.getKey() != null && ("begindatadate".equals(entry.getKey())||"enddatadate".equals(entry.getKey()))){//日期区间查询
					sql += " and " + entry.getValue();
				}else if(entry.getKey() != null && ("custId".equals(entry.getKey()))){
					sql += " and (t.resultpeoplecode='" + entry.getValue()+"' or t.recheckpeoplecode='" + entry.getValue()+"' )";
				}else{
					sql+=" and t."+entry.getKey()+"="+"'"+entry.getValue()+"' ";
				}
			}
		}
		sql+=" order by t.datadate,t.idcenter,t.accno";
		String[] paramStr = {"accno","datadate","bal","abs","result","resultpeoplecode","resultdate","recheck","recheckpeoplecode","recheckdate","idcenter"};
		List<Map<String,Object>> list = queryObjectList(paramStr ,sql);
		if(isPage){
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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Map<String,Object>> queryInnerAccnoList(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException{
		String sql="select t.accno,t.inputdate,t.inputpeoplecode from ebs_inneraccno t where 1=1 ";
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {	
					sql+=" and t."+entry.getKey()+"="+"'"+entry.getValue()+"' ";
			}
		}
		//sql+=" order by t.accno";
		String[] paramStr = {"accno","inputdate","inputpeoplecode"};
		List<Map<String,Object>> list = queryObjectList(paramStr ,sql);
		if(isPage){
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
	
	/**
	 *  确认 accno 的唯一性
	 *  如果 已存在 返回 1 不存在 0
	 * @throws SQLException 
	*/
	 @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	 public int ifExistAccno(String accno) throws SQLException{
		 String sql = "select t.accno,t.inputdate from ebs_inneraccno t where t.accno = '"+accno+"'";
		 String[] param = {"accno","inputdate"};
		 return queryObjectList(param,sql).size();
	 }
	 
	 /**
	 * 增加InnerAccno
	 */
	public void addInnerAccno(String accNo,String inputDate,String peopleCode) throws XDocProcException{
		String sql = "insert into ebs_inneraccno values (INNERACCNO_AUTOID.nextval,'"+accNo+"','"+inputDate+"','"+peopleCode+"')";
		dao.ExecSql(sql);
	}
	
	public void deleteInnerAccno(String accNo) throws XDocProcException{
		String sql = "delete ebs_inneraccno where accno='"+accNo+"'";
		dao.ExecSql(sql);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<Map<String,Object>> getInnerAccnoDetailData(Map<String, String> queryMap,PageParam param,boolean isPaged) throws SQLException{
		
		String sql = "select t.trad_date,t.acct,t.oppost_acct,t.oppost_acct_name,t.borrow_lend_sign,t.trad_amt,t.acct_bal,t.host_syst_time,t.summy " 
					+ " from ebs_inneraccnodetail_data t where 1=1 ";
		
		for (Map.Entry<String, String> entry : queryMap.entrySet()) {
			if (entry.getValue() != null
					&& entry.getValue().trim().length() != 0) {
				if("trad_date".equals(entry.getKey())){
					String traceDate = entry.getValue();
					String preDate = traceDate.substring(0,7)+"-"+"01";
					sql+= " and to_date(t.trad_date,'yyyy-mm-dd') >=to_date('"+preDate+"','yyyy-mm-dd') and to_date(t.trad_date,'yyyy-mm-dd')<= to_date('"+traceDate+"','yyyy-mm-dd') ";
				}else{
					sql+= " and t."+entry.getKey()+" = '"+entry.getValue()+"' ";
				}
				
			}
		}	
		
		sql+="order by t.trad_date,t.host_syst_time,t.seri_no";
		//System.out.println(sql);
		String[] paramStr = {"trad_date","acct","oppost_acct","oppost_acct_name","borrow_lend_sign","trad_amt","acct_bal","host_syst_time","summy"};
		List<Map<String,Object>> list = queryObjectList(paramStr ,sql);
		
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
}
