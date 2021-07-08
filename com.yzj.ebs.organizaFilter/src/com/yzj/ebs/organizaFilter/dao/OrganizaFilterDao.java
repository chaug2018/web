package com.yzj.ebs.organizaFilter.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.OrganizaFilter;
import com.yzj.ebs.impl.BaseService;

public class OrganizaFilterDao extends BaseService<OrganizaFilter>{
	
	/**
	 * 根据机构号查询数据
	 * @param queryMap
	 * @param idBank
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public OrganizaFilter findOrgFilterInfoByIdBank(String idBank){
		String queryString="from OrganizaFilter where idBank='"+idBank+"'";
		OrganizaFilter organizaFilter=null;
		try {
			List<OrganizaFilter> list=dao.find(queryString);
			if(list.size()==1){
				organizaFilter=list.get(0);
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return organizaFilter;
	}
	
	
	/**
	 * 根据机构号在机构信息表中查询数据
	 * @param queryMap
	 * @param idBank
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> findBankInfoByIdBank(String idBank){
		Map<String,String> map = new HashMap<String,String>();
		String queryString="select idBank,cname,idbranch,idcenter from param_bank where idBank='"+idBank+"'";
		try {
			List<Object[]> list=(List<Object[]>) dao.findBySql(queryString);
			if(list.size()==1){
				Object[] obj=list.get(0);
				map.put("idBank",(String)obj[0]);
				map.put("bankName",(String)obj[1]);
				map.put("idBranch",(String)obj[2]);
				map.put("idCenter",(String)obj[3]);
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrganizaFilter> selectRecords(Map<String, String> queryMap,PageParam param){
		List<OrganizaFilter> list = new ArrayList<OrganizaFilter>();
		try {
			String hql= createFrom() + createWhere(queryMap)+createOrderBy();
			// 每页显示结果条数
			int pageSize = param.getPageSize();
			// 分页时显示的第一条记录，默认从0开始
			int startRow = (param.getCurPage()-1) * pageSize;
			list = (List<OrganizaFilter>) dao.findbyPage(hql.toString(),startRow,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 查询总条目数
	 * @param queryMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long selectCount(Map<String, String> queryMap)
	{
		Long count = 0L;
		try
		{
			String queryCount = "select count(*) " + createFrom() + createWhere(queryMap)+createOrderBy();
			List<Object> list = (List<Object>) dao.findByHql(queryCount);
			if(list.size()==1){
				count=(Long) list.get(0);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * 查询条件
	 * @return
	 */
	public String createWhere(Map<String, String> queryMap){
		StringBuffer queryWhere=new StringBuffer();
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue()!= null&& entry.getValue().trim().length()!= 0) {
					queryWhere.append(" and "+entry.getKey()+"='"+entry.getValue().trim()+"'");
				}
			}
		}
		return queryWhere.toString();
	} 
	
	/**
	 * 查询的数据源
	 * @return
	 */
	public String createFrom(){
		StringBuffer queryString=new StringBuffer();
		queryString.append("from OrganizaFilter where 1=1 ");
		return queryString.toString();
	}
	
	/**
	 * 排序分组
	 * @return
	 */
	public String createOrderBy(){
		StringBuffer queryString=new StringBuffer();
		queryString.append(" order by idBank");
		return queryString.toString();
	}
	
}
