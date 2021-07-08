package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IDocLogAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocLog;
import com.yzj.ebs.database.DocSet;

/**
 * 创建于:2012-9-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * DocLog表操作服务实现
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class DocLogAdm extends BaseService<DocLog> implements IDocLogAdm {

	/* (non-Javadoc)
	 * @see com.yzj.dps.common.IDocLogAdm#queryListByDocID(long)
	 */
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<DocLog> queryListByDocID(long docID) throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forClass(DocLog.class);
		dc.add(Restrictions.eq("docID", docID));
		dc.addOrder(Order.asc("autoID"));
		return (List<DocLog>) dao.findByDetachedCriteria(dc);
	}
}
