package com.yzj.ebs.insideaccnoparam.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccnoMaindata;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoQueryBiz;

public class InsideAccnoQueryImpl implements InsideAccnoQueryBiz{
	private IInsideAccnoParam  insideAccnoParamAdm;
	
	public List<InnerAccnoMaindata> queryAccnolist(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException{
		List<Map<String,Object>> list = insideAccnoParamAdm.queryInnerList(queryMap,param,isPage);
		List<InnerAccnoMaindata> resultList = new ArrayList<InnerAccnoMaindata>();
		for(int i=0;i<list.size();i++){
			InnerAccnoMaindata inner =new InnerAccnoMaindata();
			inner.setAbs((String)list.get(i).get("abs"));
			inner.setAccNo((String)list.get(i).get("accno"));
			inner.setBal((String)list.get(i).get("bal"));
			inner.setDataDate((String)list.get(i).get("datadate"));
			inner.setResult((String)list.get(i).get("result"));
			inner.setResultPeopleCode((String)list.get(i).get("resultpeoplecode"));
			inner.setResultDate((String)list.get(i).get("resultdate"));
			inner.setReCheck((String)list.get(i).get("recheck"));
			inner.setReCheckPeopleCode((String)list.get(i).get("recheckpeoplecode"));
			inner.setReCheckDate((String)list.get(i).get("recheckdate"));
			inner.setIdCenter((String)list.get(i).get("idcenter"));
			resultList.add(inner);
		}
		return resultList;
	}
	
	
	public IInsideAccnoParam getInsideAccnoParamAdm() {
		return insideAccnoParamAdm;
	}

	public void setInsideAccnoParamAdm(IInsideAccnoParam insideAccnoParamAdm) {
		this.insideAccnoParamAdm = insideAccnoParamAdm;
	}
	
	
}
