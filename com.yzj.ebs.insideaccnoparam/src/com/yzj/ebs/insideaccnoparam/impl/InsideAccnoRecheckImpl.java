package com.yzj.ebs.insideaccnoparam.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccnoMaindata;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoRecheckBiz;

public class InsideAccnoRecheckImpl implements InsideAccnoRecheckBiz{
	private IInsideAccnoParam  insideAccnoParamAdm;
	
	/**
	 * 查询 内部账户对账单信息
	 * throws XDocProcException, SQLException
	 */
	public List<InnerAccnoMaindata> queryAccnoRecheck(Map<String,String> queryMap,PageParam param) throws SQLException{
		return typeChange(insideAccnoParamAdm.queryInnerRecheckResult(queryMap,param));
		
	}
	
	/**
	 *  核对 内部账户对账单信息
	 *  0 复核通过  1复核不通过
	 * @throws SQLException 
	 */
	public void updateRecheck(Map<String,String> updateMap) throws SQLException{
		insideAccnoParamAdm.updateRecheck(updateMap);
	}
	
	/**
	 *  把  List<Map<String,Object>> 转成 List<InnerAccnoMaindata>
	 */
	private List<InnerAccnoMaindata> typeChange( List<Map<String,Object>> list) {
		List<InnerAccnoMaindata> resultList = new ArrayList<InnerAccnoMaindata>();
		for(int i=0;i<list.size();i++){
			InnerAccnoMaindata inner =new InnerAccnoMaindata();
			inner.setAbs((String)list.get(i).get("abs"));
			inner.setAccNo((String)list.get(i).get("accno"));
			inner.setBal((String)list.get(i).get("bal"));
			inner.setDataDate((String)list.get(i).get("datadate"));
			inner.setResult((String)list.get(i).get("result"));
			inner.setReCheck((String)list.get(i).get("recheck"));
			
			resultList.add(inner);
		}
		return resultList;
	}
	
	
	/*get set*/
	public IInsideAccnoParam getInsideAccnoParamAdm() {
		return insideAccnoParamAdm;
	}

	public void setInsideAccnoParamAdm(IInsideAccnoParam insideAccnoParamAdm) {
		this.insideAccnoParamAdm = insideAccnoParamAdm;
	}
	
	
}
