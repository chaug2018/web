package com.yzj.ebs.impl;

import java.util.List;

import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IParamBank;
import com.yzj.ebs.common.XDocProcException;

public class ParamBankAdm extends BaseService<BankParam> implements IParamBank{
	
	/**
	 *  得到分行的 电话 和 地址
	 */
	public BankParam getBankParam(String idCenter)throws XDocProcException{
		String sql = "select t.address , t.phone, t.cname  from param_bank t where t.idcenter= '" +idCenter+"'" +
				" and t.idbank = '"+idCenter+"'";
		BankParam bk = new BankParam();
		@SuppressWarnings("unchecked")
		List<Object[]> resultList =  (List<Object[]>) dao.findBySql(sql);
		if(resultList!=null && resultList.size()>0){
			if((String)resultList.get(0)[0] != null){			
				bk.setAddress((String)resultList.get(0)[0]);
			}else{
				bk.setAddress("");
			}
			
			if((String)resultList.get(0)[1] != null){
				bk.setPhone((String)resultList.get(0)[1]);
			}else{
				bk.setPhone("");
			}
			
			if((String)resultList.get(0)[2] != null){
				bk.setcName((String)resultList.get(0)[2]);
			}else{
				bk.setcName("");
			}
			
		}
		return bk;
	}
	
	/**
	 * 根据 分行名字获得分行的 机构号
	 * @throws XDocProcException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public String getIdcenterCode(String idcenterName) throws XDocProcException{
		String sql = "select t.idbank from param_bank t where t.cname='"+idcenterName+"'";
		SQLQuery sqlQuery = dao.getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql);
		@SuppressWarnings("unchecked")
		List<Object> temp = sqlQuery.list();
		return (String)temp.get(0);
	}
}
