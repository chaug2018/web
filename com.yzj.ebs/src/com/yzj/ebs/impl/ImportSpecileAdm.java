package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IImportSpecile;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.ImportSpecile;

/**
 * 创建于:2013-8-21<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * ImportSpecile表操作访问服务
 * 
 * @author j_Sun
 * @ve
 * */
public class ImportSpecileAdm extends BaseService<ImportSpecile> implements IImportSpecile{
	
	/**
	 * 特殊账户信息表增加信息
	 * @throws  
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void putInfor(ImportSpecile importSpecile)throws XDocProcException{
			dao.saveOrUpdate(importSpecile);
	}
	
	/**
	 * 特殊账户信息表删除信息s
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteSpecile(String accno)throws XDocProcException{
			String	sql = "delete ebs_ImportSpecile where accno = "+accno;
			try{
				SQLQuery query = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
				query.uniqueResult();
			}catch  (Exception e) {
				throw new XDocProcException("执行查询 " + sql + " 失败", e);
			}
		
	}
	/**
	 * 根据 accno 查询 该账号是否存在于 特殊账户导入表中
	 */
	@SuppressWarnings("unchecked")
	public ImportSpecile getOneByAccno(String accNo){
		
			String hql = "from importspecile where accNo="+accNo;
			List<ImportSpecile> list =new ArrayList<ImportSpecile>();
			try {
				list = (List<ImportSpecile>) dao.findByHql(hql);
			} catch (XDocProcException e) {
				//如果账号不存在 则返回null;
				return null;
			}
			return list.get(0);
	}
	/**
	 *  查询    其中的时间条件未 月初1号到 选择的 日期
	 */
	@SuppressWarnings("unchecked")
	public List<ImportSpecile> getQueryList(Map<String,String> queryMap,PageParam param)throws XDocProcException{
		List<ImportSpecile> list = new ArrayList<ImportSpecile>();
		String preDate="";
		if(!queryMap.get("docdate").equals("") ){
			preDate = queryMap.get("docdate").substring(0, 6)+"01";      //得到 月初的 时间
		}
		String hql="from ImportSpecile where 1=1 ";
		
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("docdate")){
						//hibernate 用to_Date 失败 so 你懂的......
						hql+= " and "+ entry.getKey()+" >= '"+preDate+"'  and "+entry.getKey()+" <= '"+entry.getValue()+"' ";
					}else{
						hql+=" and "+entry.getKey()+" = '"+entry.getValue()+"' ";
					}
				}
			}
		}
		hql+=" order by accno";
		list = (List<ImportSpecile>) dao.findByHql(hql);
		Integer countNumber =list.size();   //得到数据的条数
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
	 * 执行sql 之后 显示分页 效果
	 */
	public List<Map<String,Object>> getPageList(String sql,String[] str,PageParam param){
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		SQLQuery sqlQuery = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		List<Object[]> temp = sqlQuery.list();
		for(int i=0;i<temp.size();i++){
			Object[] obj = temp.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			for(int j=0;j<obj.length;j++){
				map.put(str[j], obj[j]);
			}
		list.add(map);
		}
		
		
		
		
		return list;
				
		
	}
}
