package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InnerAccno;

public interface InnerAccnoBiz {
	
	public List<InnerAccno> queryAccnolist(Map<String,String> queryMap,PageParam param,boolean isPage) 
			throws SQLException; 
	
	public int addInnerAccno(String accNo,String peopleCode) throws XDocProcException, SQLException;
	
	public void deleteInnerAccno(String accNo) throws XDocProcException, SQLException;
}
