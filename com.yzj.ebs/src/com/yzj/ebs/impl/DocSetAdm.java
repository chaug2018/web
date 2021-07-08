package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.DocSet;

/**
 * 创建于:2012-9-27<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * DocSet表操作服务实现
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class DocSetAdm extends BaseService<DocSet> implements IDocSetAdm {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.yzj.dps.common.IDocSetAdm#findByBranchNoAndWorkDate(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<DocSet> findByBranchNoAndWorkDate(String branchNo,
			String workdate) throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forClass(DocSet.class);
		dc.add(Restrictions.eq("branchNo", branchNo));
		dc.add(Restrictions.eq("workDate", workdate));
		return (List<DocSet>) dao.findByDetachedCriteria(dc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.common.IDocSetAdm#queryOneByID(java.lang.Long)
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public DocSet queryOneByID(Long docId) throws XDocProcException {
		List<?> list = dao.findByHql("from DocSet where docID=" + docId);
		if (list != null && list.size() != 0) {
			return (DocSet) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public DocSet findEBankDocSetByVoucherNo(String voucherNo)
			throws XDocProcException {
		String hql = "from DocSet where voucherNo='"+voucherNo+"' and docFlag ='16'" ;
		@SuppressWarnings("unchecked")
		List<DocSet> docSetList =  (List<DocSet>) dao.findByHql(hql);
		if(docSetList.size() != 0){
			return docSetList.get(0);
		}else {
			return null;
		}
	}
	
}
