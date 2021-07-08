package com.yzj.ebs.ibank.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.impl.BaseService;

public class IBankAdm extends BaseService<BasicInfo> {
	
	
	public List<BasicInfo> getBasicInfoByAccno(String accno) {
		List<BasicInfo> resultList = new ArrayList<BasicInfo>();
		resultList.clear();
		String hql = "from BasicInfo where 1=1 and accno = '"+accno+"'";
		List<?> list = null;
		try {
			list = dao.findByHql(hql);
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return resultList;
		}
		if(list!=null && list.size()>0){
			resultList.add((BasicInfo)list.get(0));
		}		
		return resultList;
	}
	
	public String updateBasicInfoByEntry(BasicInfo basicInfo){
		try {
			dao.update(basicInfo);
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	public String insertBasicInfoByEntry(BasicInfo basicInfo){
		try {
			dao.create(basicInfo);
		} catch (XDocProcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	public List<Map<String,String>> queryNocheckVoucherno(String accNo,String beginDt,String endDt,String startNum,String queryNum) throws XDocProcException{
		int stNum = Integer.parseInt(startNum);
		int pageSize = Integer.parseInt(queryNum);
//		String sql = "select * from (select tt.*,rownum from (select ec.voucherno,ec.docstate,ec.docdate,ec.accname,ec.address,ec.linkman,ec.phone from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno where ea.accno='"
//	         +accNo+"' and ec.docDate between to_date('"+beginDt+"','yyyy-mm-dd') and to_date('"+endDt+"','yyyy-mm-dd') order by ec.docDate desc) tt) tl where tl.rowno between "+stNum+" and "+endNum ;
		String sql ="select ec.voucherno,ec.docstate,ec.docdate,ec.accname,ec.address,ec.linkman,ec.phone from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno where ea.accno='" + 
				accNo+"' and ec.docDate between to_date('"+beginDt+"','yyyy-mm-dd') and to_date('"+endDt+"','yyyy-mm-dd') and docstate!='5' order by ec.docDate desc";
		List<Map<String,String>> list= (List<Map<String, String>>) dao.findbyPage(sql,stNum,pageSize);
		return list;
		
	}
	
	public int queryNocheckCount(String accNo,String beginDt,String endDt) throws XDocProcException {
		String sql ="select count(*) from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno where ea.accno='" + 
				accNo+"' and ec.docDate between to_date('"+beginDt+"','yyyy-mm-dd') and to_date('"+endDt+"','yyyy-mm-dd') and docstate!='5' order by ec.docDate desc";
		List<String> num=  (List<String>) dao.findBySql(sql);
		return Integer.parseInt(num.get(0));
	}
	
	public List<Map<String,String>> queryAllVoucherno(String accNo,String beginDt,String endDt,String startNum,String queryNum) throws XDocProcException{
		int stNum = Integer.parseInt(startNum);
		int pageSize = Integer.parseInt(queryNum);
		String sql ="select ec.voucherno,ec.docstate,ec.docdate,ec.accname,ec.address,ec.linkman,ec.phone from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno where ea.accno='" + 
				accNo+"' and ec.docDate between to_date('"+beginDt+"','yyyy-mm-dd') and to_date('"+endDt+"','yyyy-mm-dd') order by ec.docDate desc";
		List<Map<String,String>> list= (List<Map<String, String>>) dao.findbyPage(sql,stNum,pageSize);
		return list;
	}
	
	public int queryAllCount(String accNo,String beginDt,String endDt) throws XDocProcException {
		String sql ="select count(*) from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno where ea.accno='" + 
				accNo+"' and ec.docDate between to_date('"+beginDt+"','yyyy-mm-dd') and to_date('"+endDt+"','yyyy-mm-dd') order by ec.docDate desc";
		List<String> num=  (List<String>) dao.findBySql(sql);
		return Integer.parseInt(num.get(0));
	}
	
	public List<Map<String, String>> queryAccnoMainData(String tstmtNo,String docDt) throws XDocProcException {
		String sql = "select ea.voucherno,ea.checkflag,ea.docdate,ec.accname,ea.accno,ea.credit,ea.currtype from ebs_accnomaindata ea left join ebs_checkmaindata ec on ea.voucherno=ec.voucherno "
				+ "where ea.voucherno='"+tstmtNo+"' and ea.docDate = '"+docDt+"'";
		List<Map<String,String>> list=   (List<Map<String, String>>) dao.findBySql(sql);
		return list;
		
	}
}
