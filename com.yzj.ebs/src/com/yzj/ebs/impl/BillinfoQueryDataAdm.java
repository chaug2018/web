package com.yzj.ebs.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.yzj.ebs.common.IBillinfoQueryDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BillinfoQueryData;

public class BillinfoQueryDataAdm extends BaseService<BillinfoQueryData> implements

 IBillinfoQueryDataAdm{	
	@Override
	public List<?> getBillinfoQueryData(
			Map<String, String> queryMap, final String queryType,PageParam pageParam)
			throws XDocProcException {
		String sql="";
		String listsql1 = "select c.voucherno,c.idbank,c.idbranch,c.idcenter,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.finalcheckflag,c.docstate,a.acctype,a.accCycle " 
				+ "from ebs_checkmaindata c ,ebs_accnomaindata a  where  c.voucherno=a.voucherno " ;
		String billinfosql ="select ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.finalcheckflag,ac.docstate,ac.accCycle"
				+ " from (" +listsql1.trim() +")  ac ";
		//将按账单编号查询到的表，作为一个ac表
		String listsql2="select c.voucherno,c.idbank,c.idbranch,c.idcenter,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.finalcheckflag,c.docstate ,a.acctype ,a.accCycle "
				+" from ebs_checkmaindata c ,(select  * from ebs_accnomaindata a where a.autoid=(select min(autoid) from  ebs_accnomaindata aa where aa.voucherno=a.voucherno)) a  where  c.voucherno=a.voucherno ";
		String basicinfosql="select ac.voucherno,ac.idbank,ac.accname,ac.docdate,ac.proveflag,ac.docstate "
				+ " from (" +listsql2.trim() +")  ac ";	 
		//将按账号信息查询的表，作为一个ac表
		
		String conditionsql="where 1=1 ";
		
		Set<Map.Entry<String,String>> key = queryMap.entrySet();
		Iterator<Map.Entry<String, String>> its = key.iterator();
        while( its.hasNext())	
	    {
	    	 Map.Entry<String, String> entry = (Map.Entry<String, String>) its.next();
	    	 if (StringUtils.isNotEmpty(entry.getValue())) {
	    		conditionsql+="  and ac."+entry.getKey().trim()+"='"+entry.getValue()+"'"; 
	    	 }
	    }
	    if(queryType.equals("2")){
	      sql=billinfosql+conditionsql;
	      sql +=" group by ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.finalcheckflag,ac.docstate ,ac.accCycle order by ac.voucherno "; 
	    }else if(queryType.equals("1")){
	      sql=basicinfosql+conditionsql;
	      sql +=" group by ac.voucherno,ac.idbank,ac.docdate,ac.proveflag,ac.docstate,ac.accname order by ac.voucherno ";  
	    }
		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = pageParam.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = pageParam.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = dao.findBySql(sql,firstResult,pageSize);
		pageParam.setFirstResult(firstResult);
		pageParam.setCurPage(curPage);
		pageParam.setTotal((int) countNumber);
		pageParam.setTotalPage(totalPage);
		pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		return list;
	}

	@Override
	public List<?> getAllBillinfoQueryData(Map<String, String> queryMap, final String queryType)
			throws XDocProcException {
		// TODO Auto-generated method stub
		
		String sql="";
		String listsql1 = "select c.voucherno,c.idbank,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.checkflag,c.docstate,a.acctype,a.accCycle " 
				+ "from ebs_checkmaindata c ,ebs_accnomaindata a   where c.voucherno=a.voucherno " ;
		String billinfosql ="select ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate ,ac.accCycle"
				+ " from (" +listsql1.trim() +")  ac ";
		//将按账单编号查询到的表，作为一个ac表
		String listsql2="select c.voucherno,c.idbank,a.accno,c.accname,a.credit,a.docdate,a.currency,c.proveflag,a.checkflag,c.docstate ,a.acctype,a.accCycle "
				+" from ebs_checkmaindata c ,(select  * from ebs_accnomaindata a where a.autoid=(select min(autoid) from  ebs_accnomaindata aa where aa.voucherno=a.voucherno)) a  where  c.voucherno=a.voucherno ";
		String basicinfosql="select ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate,ac.accCycle"
				+ " from (" +listsql2.trim() +")  ac ";		 
		//将按账号信息查询的表，作为一个ac表
		
		String conditionsql="where 1=1 ";
		
		Set<Map.Entry<String,String>> key = queryMap.entrySet();
		Iterator<Map.Entry<String, String>> its = key.iterator();
        while( its.hasNext())	
	    {
	    	 Map.Entry<String, String> entry = (Map.Entry<String, String>) its.next();
	    	 if (StringUtils.isNotEmpty(entry.getValue())) {
	    		conditionsql+="  and ac."+entry.getKey().trim()+"='"+entry.getValue()+"'"; 
	    	 }
	    }
	    if(queryType.equals("2")){
	      sql=billinfosql+conditionsql;
	    }else if(queryType.equals("1")){
	      sql=basicinfosql+conditionsql;
	    }    
		sql +=" group by ac.voucherno,ac.idbank,ac.accno,ac.accname,ac.credit,ac.docdate,ac.currency,ac.proveflag,ac.checkflag,ac.docstate,ac.accCycle order by ac.voucherno "; 
	    List<?> list= dao.findBySql(sql);
		return list;
	}
    
	
}
 