package com.yzj.ebs.impl;

import java.util.List;
import com.yzj.ebs.common.INewSealAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.dao.SealDao;

/**
 * 创建于:2012-12-14<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * newseal操作服务实现
 * 
 * @author qq
 * @version 1.0.0
 */
public class NewSealAdm   implements INewSealAdm {
	protected SealDao dao;

	@Override
	public List<Object> findBySql(String sql) throws XDocProcException {
		return dao.findBySql(sql);
	}

	@Override
	public Object ExecQuery(String QuerySql) throws XDocProcException {
		return dao.ExecQuery(QuerySql);
	}

	public SealDao getDao() {
		return dao;
	}

	public void setDao(SealDao dao) {
		this.dao = dao;
	}

}
