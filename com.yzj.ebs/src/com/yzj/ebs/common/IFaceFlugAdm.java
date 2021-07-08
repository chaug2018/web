package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.FaceFlug;

public interface IFaceFlugAdm extends IBaseService<FaceFlug>{

	/**
	 * 新增一条指定FaceFlug
	 */ 
	public void addFaceFlug(FaceFlug faceFlug);
	
	/**
	 * 删除指定的FaceFlug记录
	 */
	public void delete(FaceFlug faceFlug);
	
	
	/**分页查询
	 * 根据传入的参数：查询参数、分页参数、数据源、排序字段分页查询具体的记录
	 * @param queryMap	:查询参数
	 * @param pageParam	：分页参数
	 * @param pageParam	：数据源
	 * @param pageParam	：排序字段
	 * @return	List<FaceFlug> 查询的FaceFlug对象结果
	 * @throws Exception
	 */
	public  List<?> selectRecords(Map<String,String> queryMap,PageParam pageParam,String dataSource,String orderByField);
	
	/**
	 * 根据数据源参数和排序参数查询某数据源的总条目数：是用HQL
	 * @param queryMap	查询条件
	 * @param dataSource 数据源
	 * @param orderByField 排序字段	
	 * @throws Exception
	 */
	public Long  selectCount(Map<String, String> queryMap,String dataSource,String orderByField) throws Exception;
}
