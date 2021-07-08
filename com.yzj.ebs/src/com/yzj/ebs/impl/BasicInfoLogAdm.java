package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.yzj.ebs.common.IBasicInfoLogAdm;
import com.yzj.ebs.common.OperLogQueryParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfoLog;

/**
 * 创建于:2012-10-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * BasicInfoLog表操作访问服务
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class BasicInfoLogAdm extends BaseService<BasicInfoLog> implements 
		IBasicInfoLogAdm {

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3163887083224158878L;

	@Override
	public BasicInfoLog createBasicInfoLog(BasicInfoLog basicInfoLog)
			throws XDocProcException {
		return dao.create(basicInfoLog);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicInfoLog> queryBasicInfoLog(OperLogQueryParam param,
			boolean isPaged) throws XDocProcException {

		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(BasicInfoLog.class);
		Criteria criteria1 = session.createCriteria(BasicInfoLog.class);
		if (param.getIdBank() != null && param.getIdBank().length() > 0) {
			criteria.add(Restrictions.eq("idBank", param.getIdBank()));
			criteria1.add(Restrictions.eq("idBank", param.getIdBank()));
		}
		if (param.getIdBranch() != null && (param.getIdBranch().length() > 0)) {
			criteria.add(Restrictions.eq("idBranch", param.getIdBranch()));
			criteria1.add(Restrictions.eq("idBranch", param.getIdBranch()));
		}
		if (param.getIdCenter() != null && param.getIdCenter().length() > 0) {
			criteria.add(Restrictions.eq("idCenter", param.getIdCenter()));
			criteria1.add(Restrictions.eq("idCenter", param.getIdCenter()));
		}
		if (param.getIdBank1() != null && param.getIdBank1().length() > 0) {
			criteria.add(Restrictions.eq("idBank", param.getIdBank1()));
			criteria1.add(Restrictions.eq("idBank", param.getIdBank1()));

		}
		if (param.getAccName() != null && param.getAccName().length() > 0) {
			criteria.add(Restrictions.eq("accName", param.getAccName()));
			criteria1.add(Restrictions.eq("accName", param.getAccName()));
		}
		if (param.getAccNo() != null && param.getAccNo().length() > 0) {
			criteria.add(Restrictions.eq("accNo", param.getAccNo().trim()));
			criteria1.add(Restrictions.eq("accNo", param.getAccNo().trim()));
		}
		if (param.getStartTime() != null && param.getStartTime().length() > 0) {
			criteria.add(Restrictions.ge("opDate", param.getStartTime()));
			criteria1.add(Restrictions.ge("opDate", param.getStartTime()));
		}
		if (param.getEndTime() != null && param.getEndTime().length() > 0) {
			criteria.add(Restrictions.le("opDate", param.getEndTime()));
			criteria1.add(Restrictions.le("opDate", param.getEndTime()));
		}
		if (param.getOpCode() != null && param.getOpCode().length() > 0) {
			criteria.add(Restrictions.eq("opCode", param.getOpCode()));
			criteria1.add(Restrictions.eq("opCode", param.getOpCode()));
		}
		if (param.getOperLogModule() != null
				&& param.getOperLogModule().length() > 0) {
			criteria.add(Restrictions.eq("opMode",
					Integer.parseInt(param.getOperLogModule())));
			criteria1.add(Restrictions.eq("opMode",
					Integer.parseInt(param.getOperLogModule())));
		}
		//like
		if (param.getOpDesc() != null && param.getOpDesc().length() > 0) {
			criteria.add(Restrictions.like("opDesc", "%"+param.getOpDesc()+"%"));
			criteria1.add(Restrictions.like("opDesc", "%"+param.getOpDesc()+"%"));
		}
		
		criteria1.addOrder(Order.asc("autoId"));
		List<BasicInfoLog> result=null;
		if (isPaged) {
			// 获取满足条件的数据总数
			
			criteria.setProjection(Projections.rowCount());
			Object obj = criteria.uniqueResult();
			Long countNumberTmp =  (Long) obj;
			int countNumber = countNumberTmp.intValue();
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}

			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			criteria1.setFirstResult(firstResult);
			criteria1.setMaxResults(pageSize);
			criteria1.addOrder(Order.asc("opDate"));
			result = criteria1.list();

			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		}else{
			result = criteria1.list();
		}
		session.close();
		return result;
	}
	
	/**
	 * 更新日志
	 */
	@Override
	public void updateBasicInfoLog(BasicInfoLog basicInfoLog)
			throws XDocProcException {
		// TODO Auto-generated method stub
		dao.update(basicInfoLog);
	}
	
	/**
	 * 查找记录
	 */
	@Override
	public List<BasicInfoLog> findBasicInfoLog(BasicInfoLog basicInfoLog) {
		Session session = dao.getHibernateTemplate().getSessionFactory().openSession();
		List<BasicInfoLog> resultList = null;
		Criteria cri =createCriteria(basicInfoLog, session);
		
		try{
			resultList = cri.list();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			session.close();
		}
		
		return resultList;
	}
	
	/**
	 * 创建查询条件
	 * @param basicInfoLog
	 * @param session
	 * @return
	 */
	private Criteria createCriteria(BasicInfoLog basicInfoLog,Session session){
		Criteria cri = session.createCriteria(BasicInfoLog.class);
		
		if (basicInfoLog.getAutoId() != null ) {
			cri.add(Restrictions.eq("autoId", basicInfoLog.getAutoId()));
		}		
		if (basicInfoLog.getAccNo() != null && basicInfoLog.getAccNo().length() > 0) {
			cri.add(Restrictions.eq("accNo", basicInfoLog.getAccNo()));
		}		
		if (basicInfoLog.getAccName() != null && basicInfoLog.getAccName().length() > 0) {
			cri.add(Restrictions.eq("accName", basicInfoLog.getAccName()));
		}		
		if (basicInfoLog.getIdCenter() != null && basicInfoLog.getIdCenter().length() > 0) {
			cri.add(Restrictions.eq("idCenter", basicInfoLog.getIdCenter()));
		}		
		if (basicInfoLog.getIdBranch() != null && basicInfoLog.getIdBranch().length() > 0) {
			cri.add(Restrictions.eq("idBranch", basicInfoLog.getIdBranch()));
		}		
		if (basicInfoLog.getIdBank() != null && basicInfoLog.getIdBank().length() > 0) {
			cri.add(Restrictions.eq("idBank", basicInfoLog.getIdBank()));
		}		
		if (basicInfoLog.getOpMode() != null ) {
			cri.add(Restrictions.eq("opMode", basicInfoLog.getOpMode()));
		}
		if (basicInfoLog.getOpDesc() != null && basicInfoLog.getOpDesc().length() > 0) {
			cri.add(Restrictions.eq("opDesc", basicInfoLog.getOpDesc()));
		}
		if (basicInfoLog.getOpCode() != null && basicInfoLog.getOpCode().length() > 0) {
			cri.add(Restrictions.eq("opCode", basicInfoLog.getOpCode()));
		}
		if (basicInfoLog.getOpDate() != null && basicInfoLog.getOpDate().length() > 0) {
			cri.add(Restrictions.eq("opDate", basicInfoLog.getOpDate()));
		}
		if (basicInfoLog.getChnOpMode() != null && basicInfoLog.getChnOpMode().length() > 0) {
			cri.add(Restrictions.eq("chnOpMode", basicInfoLog.getChnOpMode()));
		}
		
		return cri;
	}
	
	
}
