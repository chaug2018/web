package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.ISealLogAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.SealLog;

/**
 * 创建于:2012-9-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * SealLog表操作服务实现
 *
 * @author 刘奇峰
 * @version 1.0.0
 */
public class SealLogAdm extends BaseService<SealLog> implements ISealLogAdm {
	/*
	 * (non-Javadoc)
	 *
	 * @see com.yinzhijie.dps.common.IDocLogAdm#queryListByDocID(long)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<SealLog> queryListByDocID(long docID) throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forClass(SealLog.class);
		dc.add(Restrictions.eq("docID", docID));
		dc.addOrder(Order.asc("autoID"));
		return (List<SealLog>) dao.findByDetachedCriteria(dc);
	}
}
