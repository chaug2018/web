/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.edata.bean.BillBean;
import com.yzj.ebs.edata.dao.IEdataDao;
import com.yzj.ebs.edata.service.IDataProcessService;
import com.yzj.wf.common.WFLogger;

public class DataProcessServiceImpl implements IDataProcessService {
	private static WFLogger logger = WFLogger
			.getLogger(DataProcessServiceImpl.class);
	protected IEdataDao EdataDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void batchInsertDate(String tableName, List dataValues,
			String dataDate) throws Exception {
		EdataDao.executeSql(tableName, dataValues, dataDate);

	}
	
	/**
	 * 对一条数据进行解析 此方法用于数据处理
	 * @param str 需要解析的字符串
	 * @param splitchar 分隔符 
	 * @return 解析完成后的list
	 */
	public List<String> analyzeDate(String str,String splitchar,int paramNum){
		List<String> result = new ArrayList<String>();
		str=str.replace(splitchar+splitchar, " "+splitchar+" "+splitchar+" ");
		//将数据中的特殊字符进行过滤掉
		str=str.replace("'","");
		while(str.length()>0){
			int index = str.indexOf(splitchar);
			String value="";
			if(index>0){
				value = str.substring(0,index);
				result.add(value);
				str=str.substring(index+1,str.length());
				//如果一条记录最后一个分隔符后面为空，则为空串
				if("".equals(str)){
					result.add(str);
				}
			}else{
				value=str;
				str="";
				result.add(value);
			}
			if(result.size()==paramNum){
				break;
			}
		}
		return result;
	}
	
	/**
	 * 查询ParamSysbase表中的数据
	 * @param parma
	 * @return
	 */
	public String findSysbaseParam(String parma){
		return EdataDao.findSysbaseParam(parma);
	}
	

	@Override
	public long queryTableCount(String tableName) throws DaoException {
		return EdataDao.queryTableCount(tableName);
	}
	
	@Override
	public boolean processParamBankData() {
		boolean b = true;
		try {
			EdataDao.processParamBankData();
		} catch (Exception e) {
			b = false;
			logger.error("机构迁移到正式表异常" + e.getMessage());
		}
		return b;
	}

	

	@Override
	public void insertOrUpdateDate(String sql) throws DaoException {
		EdataDao.insertOrUpdateDate(sql);
	}

	/**
	 * 数据库查询 obj为需要查询的字段，sql为sql语句，返回值为List<Map>  一个map存放的为一条数据
	 * @param obj
	 * @param sql
	 * @return List<Map<String, Object>> 
	 */
	public List<Map<String, Object>> queryObjectList(String[] obj, String sql){
		return EdataDao.queryObjectList(obj, sql);
	}
	

	@Override
	public List<BillBean> queryUsers() throws DaoException {
		return EdataDao.queryKubUser();
	}

	
	public IEdataDao getEEdataDao() {
		return EdataDao;
	}

	public void setEdataDao(IEdataDao EdataDao) {
		this.EdataDao = EdataDao;
	}

}
