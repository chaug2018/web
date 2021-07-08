package com.yzj.ebs.peoplerole.dao;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.peoplerole.param.PeopleRole;

public interface IQueryPeopleRoleDao extends IBaseService<PeopleRole> {
	//获得数据库查询List<?>
	List<?> getPeopleRoleList(Map<String, String> queryMap,PageParam pageParam,boolean isPage) throws XDocProcException;;
}
