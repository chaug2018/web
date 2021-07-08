package com.yzj.ebs.organizaFilter.server;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.OrganizaFilter;
import com.yzj.ebs.organizaFilter.dao.OrganizaFilterDao;

public class OrganizaFilterServer {
	private OrganizaFilterDao organizaFilterDao;
	
	public OrganizaFilter findOrgFilterInfoByIdBank(String idBank){
		return organizaFilterDao.findOrgFilterInfoByIdBank(idBank);
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	public void deleteOrganizaFilter(OrganizaFilter entity){
		try {
			organizaFilterDao.delete(entity);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过机构ID查询机构信息
	 * @param idBank
	 * @return
	 */
	public Map<String,String> findBankInfoByIdBank(String idBank){
		return organizaFilterDao.findBankInfoByIdBank(idBank);
	}
	
	/**
	 * 保存数据
	 * @param entity
	 */
	public void saveOrUpdate(OrganizaFilter entity){
		try {
			organizaFilterDao.saveOrUpdate(entity);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分页查询数据
	 * @param queryMap
	 * @param param
	 * @return
	 */
	public List<OrganizaFilter> selectRecords(Map<String, String> queryMap,PageParam param){
		return organizaFilterDao.selectRecords(queryMap,param);
	}
	
	
	/**
	 * 查找条目数
	 * @param queryMap
	 * @return
	 */
	public int selectCount(Map<String, String> queryMap){
		return organizaFilterDao.selectCount(queryMap).intValue();
	}
	
	public OrganizaFilterDao getOrganizaFilterDao() {
		return organizaFilterDao;
	}

	public void setOrganizaFilterDao(OrganizaFilterDao organizaFilterDao) {
		this.organizaFilterDao = organizaFilterDao;
	}
	
	
}
