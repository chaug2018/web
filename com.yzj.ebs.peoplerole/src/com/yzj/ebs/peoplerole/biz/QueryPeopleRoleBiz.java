package com.yzj.ebs.peoplerole.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.peoplerole.dao.QueryPeopleRoleDao;
import com.yzj.ebs.peoplerole.param.PeopleRole;

/**
 * 创建于:2014-01-02<br>
 * 版权所有(C) 2014 深圳市银之杰科技股份有限公司<br>
 * 人员岗位查询
 * @author dengwu
 * @version 1.0.0
 */

public class QueryPeopleRoleBiz implements IQueryPeopleRoleBiz {
	private QueryPeopleRoleDao queryPeopleRoleDao;
	@Override
	public List<PeopleRole> getPeopleRoleList(Map<String, String> queryMap,PageParam pageParam,boolean isPage) throws XDocProcException {
		List<PeopleRole> list = null;
		list = queryPeopleRoleDao.getPeopleRoleList(queryMap, pageParam,isPage);
		return list;
	}
	
	public QueryPeopleRoleDao getQueryPeopleRoleDao() {
		return queryPeopleRoleDao;
	}
	public void setQueryPeopleRoleDao(QueryPeopleRoleDao queryPeopleRoleDao) {
		this.queryPeopleRoleDao = queryPeopleRoleDao;
	}
	
}
