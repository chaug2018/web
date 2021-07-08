package com.yzj.ebs.insideaccnoparam.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.database.InnerAccnoDetail;
import com.yzj.ebs.insideaccnoparam.biz.InnerAccnoDetailQueryBiz;
import com.yzj.ebs.insideaccnoparam.pojo.QueryParam;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 发生额明细查询 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class InnerAccnoDetailQueryImpl implements InnerAccnoDetailQueryBiz {
	private IInsideAccnoParam insideAccnoParamAdm;

	// 查询全量数据方法实现
	@Override
	public List<InnerAccnoDetail> getAccnoDetailData(Map<String, String> queryMap,
			QueryParam queryParam,boolean isPaged) throws SQLException {
		
		List<Map<String,Object>> list = insideAccnoParamAdm.getInnerAccnoDetailData(queryMap,queryParam,isPaged);
		
		List<InnerAccnoDetail> resultList = new ArrayList<InnerAccnoDetail>();
		for(int i=0;i<list.size();i++){
			InnerAccnoDetail accNo = new InnerAccnoDetail();
			//t.trad_date,t.acct,t.oppost_acct,t.oppost_acct_name,t.borrow_lend_sign,t.trad_amt,t.acct_bal,t.host_syst_time,t.summy 
			
			accNo.setTrad_date((String)list.get(i).get("trad_date"));
			accNo.setAcct((String)list.get(i).get("acct"));
			accNo.setOppost_acct((String)list.get(i).get("oppost_acct"));
			accNo.setOppost_acct_name((String)list.get(i).get("oppost_acct_name"));
			accNo.setBorrow_lend_sign((String)list.get(i).get("borrow_lend_sign"));
			accNo.setTrad_amt((String)list.get(i).get("trad_amt"));
			accNo.setAcct_bal((String)list.get(i).get("acct_bal"));
			accNo.setHost_syst_time((String)list.get(i).get("host_syst_time"));
			accNo.setSummy((String)list.get(i).get("summy"));
			resultList.add(accNo);
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