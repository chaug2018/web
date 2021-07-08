package com.yzj.ebs.impl;


import java.util.List;
import java.util.Map;
import com.yzj.ebs.common.IFaceFlugAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.FaceFlug;

/**
 * 创建于:2013-9-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发送地址维护dao
 * 
 * @author j_sun
 * @version 1.0.0
 */
public class FaceFlugAdm extends BaseService<FaceFlug> implements IFaceFlugAdm{
	
	/**
	 * 新增一条FaceFlug记录
	 */
	public void addFaceFlug(FaceFlug faceFlug ){
		try {
			dao.saveOrUpdate(faceFlug);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定的FaceFlug记录
	 */
	public void delete(FaceFlug faceFlug){
		try {
			dao.delete(faceFlug);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据传入的查询参数、分页参数、数据源、排序字段查询具体的记录
	 * @param idCenter	：查询参数
	 * @param sendMode	：分页参数
	 * @return	List<FaceFlug> 查询的FaceFlug对象结果
	 * @throws Exception
	 */
	public List<?> selectRecords(Map<String,String> queryMap,PageParam pageParam,String dataSource,String orderByField) {
		List<?> list = null   ;
		try {
			//拼接查询语句
			String hql =  createFrom(dataSource) + createWhere(queryMap)+createOrderBy(orderByField);		
			// 获取分页参数
			//每页显示结果条数
			int pageSize = pageParam.getPageSize();
			// 分页时显示的第一条记录，默认从0开始
			int startRow = (pageParam.getCurPage()-1) * pageSize;
			list = (List<?>) dao.findbyPage(hql.toString(),startRow,pageSize);
		} catch (XDocProcException e) {
			e.printStackTrace();
		};
		return list;
	}
	
	/**
	 * 根据数据源参数和排序参数查询某数据源的总条目数：是用HQL
	 * @param queryMap	查询条件
	 * @param dataSource 数据源
	 * @param orderByField 排序字段	
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long selectCount(Map<String, String> queryMap,String dataSource,String orderByField)
	{
		Long count = 0L;
		try
		{
			String queryCount = "select count(*) " + createFrom(dataSource) + createWhere(queryMap)+createOrderBy(orderByField);
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
	 */
	public String createFrom(String dataSource){
		StringBuffer queryString=new StringBuffer();
		queryString.append("from ");
		queryString.append(dataSource);
		queryString.append(" where 1=1 ");
		return queryString.toString();
	}

	/**
	 * 排序分组
	 */
	public String createOrderBy(String orderByField){
		StringBuffer queryString=new StringBuffer();
		queryString.append("order by ");
		queryString.append(orderByField);
		return queryString.toString();
	}
}
