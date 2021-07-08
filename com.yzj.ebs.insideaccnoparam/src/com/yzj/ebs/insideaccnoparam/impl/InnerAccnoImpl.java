package com.yzj.ebs.insideaccnoparam.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccno;
import com.yzj.ebs.insideaccnoparam.biz.InnerAccnoBiz;

public class InnerAccnoImpl implements InnerAccnoBiz{
	private IInsideAccnoParam  insideAccnoParamAdm;
	
	public List<InnerAccno> queryAccnolist(Map<String,String> queryMap,PageParam param,boolean isPage) throws SQLException{
		List<Map<String,Object>> list = insideAccnoParamAdm.queryInnerAccnoList(queryMap,param,isPage);
		List<InnerAccno> resultList = new ArrayList<InnerAccno>();
		for(int i=0;i<list.size();i++){
			InnerAccno inner =new InnerAccno();
			inner.setAccNo((String)list.get(i).get("accno"));
			inner.setInputDate((String)list.get(i).get("inputdate"));
			inner.setInputPeopleCode((String)list.get(i).get("inputpeoplecode"));
			
			resultList.add(inner);
		}
		return resultList;
	}
	
	/**
	 * 增加 内部账户
	 * 返回   1 增加成功  2 记录已存在
	 */
	public int addInnerAccno(String accNo,String peopleCode)throws XDocProcException, SQLException{
		//accno 不存在的情况下 才能添加记录 
		if(insideAccnoParamAdm.ifExistAccno(accNo) == 0){
			SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
			String inputDate = sf.format(new Date());
			
			insideAccnoParamAdm.addInnerAccno(accNo,inputDate,peopleCode);
			return 1;
		}else{
			return 2;
		}
	}
	
	public void deleteInnerAccno(String accNo) throws XDocProcException, SQLException{
		insideAccnoParamAdm.deleteInnerAccno(accNo);
	}
	
	public IInsideAccnoParam getInsideAccnoParamAdm() {
		return insideAccnoParamAdm;
	}

	public void setInsideAccnoParamAdm(IInsideAccnoParam insideAccnoParamAdm) {
		this.insideAccnoParamAdm = insideAccnoParamAdm;
	}
	
	
}
