package com.yzj.ebs.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.util.UtilBase;

/**
 * 创建于:2012-10-10<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账号明细主表dao操作实现
 * 
 * @author 陈林江
 * @version 1.0
 */
public class AccnoMainDataAdm extends BaseService<AccNoMainData> implements
		IAccnoMainDataAdm {

	//---------------------------------二期报表开发begin-----------------------------------
	//对账有效率统计
	public List<?> getEbillMatchReportResult(Map<String, String> queryMap,PageParam param, 
			String docDate,boolean isPaged,String selectCount) throws XDocProcException {
		String sql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			sql += "a.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			sql += "a.idCenter,a.idbank,a.bankname,";
		}
		
		sql += " count(case when a.docdate='"
				+ docDate.trim()
				+ "'"
				+ " then a.accno end ) as "
				+ " SendCount"
				+ String.valueOf(6)
				+ "," // 账户数
				+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim() + "' " 
				+ "then a.accno end) as MatchCount"
				+ String.valueOf(6) + ",";// 有效数
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			sql += " count(case when a.docdate='"
					+ docDate.trim()
					+ "'"
					+ " then a.accno end ) as "
					+ " SendCount"
					+ String.valueOf(i)
					+ "," // 账单数
					+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ " or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim()+ "' "
					+ "then a.accno end) as MatchCount" + String.valueOf(i)
					+ ",";// 有效数
		}
		int lastIndex = sql.lastIndexOf(',');
		sql = sql.substring(0, lastIndex);
		
		sql += " from autek.ebs_accnomaindata a left join autek.ebs_checkmaindata c on( a.voucherno = c.voucherno) where 1=1" ;
		
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3)='"+entry.getValue()+"' ";
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						sql += " and a.sendMode in ('1','2','3') ";
					}else{	
						sql += " and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql = " group by a.idCenter ";
			orderSql = " order by a.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql = " group by a.idCenter,a.idbank,a.bankName " ;
			orderSql = " order by a.idCenter,a.idbank ";
		}
		sql = sql + groupSql +orderSql;
		System.out.println("对账有效率统计sql:"+sql);
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			list = dao.findBySql(sql);
			return list;
		}
		
	}
	
	//对账有效明细统计
	public List<?> getEbillMatchDetail(Map<String, String> queryMap,
			PageParam param, String docDate,boolean isPaged) throws XDocProcException {
		Map<String, String> myMap = new HashMap<String, String>();
		String docDate6 = "";
		String docDate5 = "";
		String docDate4 = "";
		String docDate3 = "";
		String docDate2 = "";
		String docDate1 = "";
		docDate6 = docDate;
		String selectSql = "select a.idcenter,a.idbank,a.bankname,a.accno,c.accname,";
		selectSql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
				+ docDate.trim()
				+ "'"
				+ " then a.voucherno end) as MatchCount6,";
		selectSql += "count(case when a.docdate='" + docDate.trim()
				+ "' then a.voucherno end) as MatchCount61,";//当月是否有账单
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			myMap.put(String.valueOf(i), docDate);
			selectSql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ " or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
					+ docDate.trim()
					+ "'"
					+ " then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + ",";
			selectSql += "count(case when  a.docdate='" + docDate.trim()
					+ "' then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + "1,";
		}

		//selectSql += "b.accname ";
		
		String whereSql = "";
		whereSql += " from autek.ebs_accnomaindata a left join autek.ebs_checkmaindata c on (a.voucherno=c.voucherno) where a.docdate in ";
		// 循环Map取出条件
		if (myMap != null) {
			for (Map.Entry<String, String> entry : myMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("5")) {
						docDate5 = entry.getValue().trim();
					}
					if (entry.getKey().equals("4")) {
						docDate4 = entry.getValue().trim();
					}
					if (entry.getKey().equals("3")) {
						docDate3 = entry.getValue().trim();
					}
					if (entry.getKey().equals("2")) {
						docDate2 = entry.getValue().trim();
					}
					if (entry.getKey().equals("1")) {
						docDate1 = entry.getValue().trim();
					} else {
					}
				}
			}
		}
		whereSql += "(" + "'" + docDate6 + "'" + "," + "'" + docDate5 + "'" + ","
				+ "'" + docDate4 + "'" + "," + "'" + docDate3 + "'" + "," + "'"
				+ docDate2 + "'" + "," + "'" + docDate1 + "'" + ")";
		//whereSql += " and  a.voucherno=c.voucherno and a.accno=b.accno  ";
		
		selectSql += " count(case when (((a.sendmode !='3' or a.sendmode is null) and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null or c.proveflag in ('0','40','41','42','43','44','51') or c.proveflag is null)) "
								+ "	or (a.sendmode ='3' and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null))) " 
								+ " and a.docdate>='"+ docDate1+ "' and a.docdate<='" + docDate6+"' "
								+ " then a.voucherno end) as failCount ";
		
		String groupSql = " group by a.idcenter,a.idbank,a.bankname,a.accno,c.accname ";
		String havingSql = "";
		String orderSql = " order by a.idcenter,a.idbank,a.accno ";
		
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						whereSql+=" and substr(a.subjectNo,1,3) ='"+entry.getValue()+"' ";
					}else if(entry.getKey().equals("failCount")){//未成功次数
						havingSql = " having count(case when (((a.sendmode !='3' or a.sendmode is null) and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null or c.proveflag in ('0','40','41','42','43','44','51') or c.proveflag is null)) "
								+ "	or (a.sendmode ='3' and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null))) " 
								+ " and a.docdate>='"+ docDate1+ "' and a.docdate<='" + docDate6+"' "
								+ " then a.voucherno end) = "+entry.getValue();
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						whereSql += " and a.sendMode in ('1','2','3') ";
					}else{
						whereSql += "and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		
		selectSql += whereSql + groupSql + havingSql + orderSql;
		System.out.println("对账有效明细统计sql:"+selectSql);
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(selectSql).size();
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			
			list = dao.findBySql(selectSql, firstResult, pageSize);
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			list = dao.findBySql(selectSql);
			return list;
		}
		
	}
	
	//账户有效对账结果展示 (废除)
	public List<?> getEbillMatchResult(Map<String, String> queryMap,
			PageParam param,boolean isPaged) throws XDocProcException {
		String selectSql = "select a.idcenter,a.idbank,a.bankname,a.accno,a.finalcheckflag,c.proveflag,a.docdate,"
						+ "count(case when a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31') then a.voucherno end) as successFlag ";
		selectSql += " from autek.ebs_accnomaindata a left join autek.ebs_checkmaindata c on (a.voucherno=c.voucherno) ";
		String whereSql = " where 1=1 ";
		
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						whereSql+=" and substr(a.subjectNo,1,3) ='"+entry.getValue()+"' ";
					}else if(entry.getKey().equals("succFlag")){//成功与否标志
						if("1".equals(entry.getValue())){//1:成功
							whereSql += " and (a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) ";
						}else{//0:失败
							whereSql += " and (a.finalcheckflag not in ('3','4') or a.finalcheckflag is null or c.proveflag not in ('20','22','31') or c.proveflag is null) ";
						}
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						whereSql += " and a.sendMode in ('1','2','3') ";
					}else{
						whereSql += " and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		
		String groupSql = " group by a.idcenter,a.idbank,a.bankname,a.accno,a.finalcheckflag,c.proveflag,a.docdate ";
		String orderSql = " order by a.idcenter,a.idbank,a.accno ";
		String sql = selectSql + whereSql + groupSql + orderSql;
		System.out.println("账户有效对账结果展示sql:"+sql);
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			
			list = dao.findBySql(sql, firstResult, pageSize);
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			list = dao.findBySql(sql);
			return list;
		}
		
	}
		
	//覆盖率统计
	@Override
	public List<?> getCoverReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount,String isThree)
			throws XDocProcException {
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "c.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "c.idCenter,c.idBank,c.bankName,";
		}

		selectSql +=  "count(distinct a.accno) as sendCount," // 总账户数
					+ "count(distinct case when ((c.proveflag in('20','22','31') and a.finalCheckflag in('3','4')) " 
					+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) then a.accno end) as successCount, " // 有过成功对账数
					+ "count(distinct case when (((a.sendmode !='3' or a.sendmode is null) and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null or c.proveflag in ('0','40','41','42','43','44','51') or c.proveflag is null)) "
					+ "or (a.sendmode ='3' and (a.finalcheckflag in ('1','2','5') or a.finalcheckflag is null))) then a.accno end) as failCount" // 都未成功对账数
					+ " from autek.ebs_checkmaindata c left join autek.ebs_accnomaindata a on (a.voucherno=c.voucherno) where 1=1 ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						selectSql += " and " + entry.getValue();
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						selectSql += " and a.sendMode in ('1','2','3') ";
					}else{
						selectSql += " and a." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
					
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by c.idCenter ";
			orderSql += " order by c.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by c.idCenter,c.idBranch,c.idBank,c.bankName " ;
			orderSql = " order by c.idcenter,c.idbank";
		}
		sql = selectSql + groupSql +orderSql;
		System.out.println("覆盖率统计sql:"+sql);
		
		if("1".equals(isThree)){//如果选择日期区间是3个月，则需查询一次未成功对账数、两次未成功对账数、三次未成功对账数
			dao.ExecSql("BEGIN EXECUTE IMMEDIATE 'DROP TABLE ACCNOCHECKTEMP'; EXCEPTION WHEN OTHERS THEN NULL; END;");
			dao.ExecSql("create table ACCNOCHECKTEMP(IDCENTER VARCHAR2(12 CHAR),IDBANK VARCHAR2(12 CHAR)," +
							"ACCNO VARCHAR2(32 CHAR),SUCCESSFLAG VARCHAR2(2 CHAR)) tablespace EBS_STANDARD");//SUCCESSFLAG 0:success 1:fail
			String insertSql = "insert into ACCNOCHECKTEMP select a.idCenter,a.idBank,a.accno," +
					"case when ((c.proveflag in('20','22','31') and a.finalCheckflag in('3','4')) or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) then '0' else '1' end  as successflag " +
					" from autek.ebs_checkmaindata c left join autek.ebs_accnomaindata a on (a.voucherno=c.voucherno) where 1=1 ";
			if (queryMap != null) {
				for (Map.Entry<String, String> entry : queryMap.entrySet()) {
					if (entry.getValue() != null
							&& entry.getValue().trim().length() != 0) {
						if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
							insertSql += " and " + entry.getValue();
						}else if("5".equals(entry.getValue().trim()) 
								&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
							insertSql += " and a.sendMode in ('1','2','3') ";
						}else{
							insertSql += " and a." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
						}
						
					}
				}
			}
			dao.ExecSql(insertSql);
			
			//删除有过对账成功的账号的记录,剩下的都是未对账的
			dao.ExecSql("delete from ACCNOCHECKTEMP where accno in (select accno from ACCNOCHECKTEMP where SUCCESSFLAG='0')");
		}
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;	
		}else{
			list = dao.findBySql(sql);
			return list;
		}
	}
	
	/**
	 * 获取连续未对账 次数
	 */
	public List<?> getNotCheckCount(String selectCount,String idBank) throws XDocProcException{
		String selectSql = "select count(case when count(*)=1 then accno end) as oneFailCount,"+
							"count(case when count(*)=2 then accno end) as twoFailCount,"+
							"count(case when count(*)=3 then accno end) as threeFailCount " +
							"from ACCNOCHECKTEMP ";
		
		String whereSql = " where 1=1 ";
		
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			whereSql += " and idCenter='"+idBank+"'";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			whereSql += " and idBank='"+idBank+"'";
		}
		
		String groupbySql = " group by accno ";
		String sql = selectSql + whereSql +groupbySql;
		
		return dao.findBySql(sql);
	}
	
	//连续对账未成功账户明细
	@Override
	public List<?> getCoverFailReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged) throws XDocProcException {
		
		dao.ExecSql("BEGIN EXECUTE IMMEDIATE 'DROP TABLE ACCNONOTCHECKTEMP'; EXCEPTION WHEN OTHERS THEN NULL; END;");
		dao.ExecSql("create table ACCNONOTCHECKTEMP(idCenter VARCHAR2(12 CHAR),idBank VARCHAR2(12 CHAR),bankName VARCHAR2(128 CHAR)," +
					"ACCNO VARCHAR2(32 CHAR),accname VARCHAR2(128 CHAR),docState VARCHAR2(2 CHAR),docdate VARCHAR2(10 CHAR)," +
					"sendmode VARCHAR2(2 CHAR),SUCCESSFLAG VARCHAR2(2 CHAR)) tablespace EBS_STANDARD");//SUCCESSFLAG 0:success 1:fail
		String insertSql = "insert into ACCNONOTCHECKTEMP select a.idCenter,a.idBank,a.bankName,a.accno,c.accname,c.docState,c.docdate,a.sendmode," +
				"case when ((c.proveflag in('20','22','31') and a.finalCheckflag in('3','4')) or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) then '0' else '1' end  as successflag " +
				" from autek.ebs_checkmaindata c left join autek.ebs_accnomaindata a on (a.voucherno=c.voucherno) where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						insertSql += " and " + entry.getValue();
					}else if(entry.getKey() != null && "failCount".equals(entry.getKey())){//失败次数
						//不做查询条件
					}else if("5".equals(entry.getValue().trim()) 
							&& "sendMode".equals(entry.getKey())){//余额 (邮寄+柜台+网银)
						insertSql += " and a.sendMode in ('1','2','3') ";
					}else{
						insertSql += " and a." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
					
				}
			}
		}
		System.out.println(insertSql);
		dao.ExecSql(insertSql);
		
		//删除有过对账成功的账号的记录,剩下的都是未对账的
		dao.ExecSql("delete from ACCNONOTCHECKTEMP where accno in (select accno from ACCNONOTCHECKTEMP where SUCCESSFLAG='0')");
		//按账号统计每个账号未对账的次数，记录在SUCCESSFLAG字段里
		dao.ExecSql("merge into accnonotchecktemp a "
					+"using (select accno,count(*) as num from accnonotchecktemp group by accno) t "
					+"on (a.accno=t.accno) "
					+"when matched then  "
					+"update set successflag=t.num");
		
		String sql = "";
		String selectSql = "select t.idCenter,t.idBank,t.bankName,t.accno,t.accname,t.docState,t.docdate,t.sendmode,b.accState,t.successflag "
				 		+ "from ACCNONOTCHECKTEMP t left join autek.ebs_basicinfo b on (t.accno=b.accno) where 1=1 ";
		
		String whereSql = "";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "failCount".equals(entry.getKey())){//失败次数
						whereSql += " and successflag=" + entry.getValue();
					}
				}
			}
		}
		
		String orderSql = " order by b.idcenter,b.idbank,t.accno ";
		
		sql = selectSql + whereSql + orderSql;
		System.out.println("连续对账未成功账户明细sql:"+sql);
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;	
		}else{
			list = dao.findBySql(sql);
			return list;
		}
	}
	

	//活跃账户情况统计
	public List<?> getActiveAccReportList(Map<String, String> queryMap,
			PageParam param,boolean isPaged,String tableName,String workDate) throws XDocProcException {
		String preWorkDate = "";
		
		String selectSql = "select b.idcenter,b.idbank,b.bankname,a.accno,b.accname,count(a.accno) from "
							+tableName+" a left join ebs_basicinfo b on (a.accno=b.accno) ";
		String whereSql = " where 1=1 ";
		if(!workDate.equals("")){
			preWorkDate = workDate.substring(0,7)+"-"+"01";
			whereSql+= " and to_date(a.workDate,'yyyy-mm-dd') >= to_date('"+preWorkDate+"','yyyy-mm-dd') " +
						"and to_date(a.workdate,'yyyy-mm-dd') <= to_date('"+workDate+"','yyyy-mm-dd') ";
		}
		
		String groupSql = " group by a.accno, b.idcenter,b.idbank,b.bankname ,b.accname ";
		String havingSql = "";
		String orderSql = " order by b.idcenter,b.idbank,a.accno ";
		
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("beginNum")){//发生额明细开始次数
						if("".equals(havingSql)) havingSql = " having count(a.accno)>= " + entry.getValue();
						else havingSql += " and count(a.accno)>= " + entry.getValue();
					}else if(entry.getKey().equals("endNum")){//发生额明细开始次数
						if("".equals(havingSql)) havingSql = " having count(a.accno)<= " + entry.getValue();
						else havingSql += " and count(a.accno)<= " + entry.getValue();
					}else{
						whereSql += "and b." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		
		selectSql += whereSql + groupSql + havingSql + orderSql;
		System.out.println("活跃账户情况统计sql:"+selectSql);
		
		List<?> list = null;
		if(isPaged){
			Integer countNumber = dao.findBySql(selectSql).size();
			int pageSize = param.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = param.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			
			list = dao.findBySql(selectSql, firstResult, pageSize);
			param.setFirstResult(firstResult);
			param.setCurPage(curPage);
			param.setTotal((int) countNumber);
			param.setTotalPage(totalPage);
			param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			list = dao.findBySql(selectSql);
			return list;
		}
		
	}
	
	//---------------------------------二期报表开发end-------------------------------------
	
	
	public String getSealmodeByAccNo(String accno) throws XDocProcException{
		String sql = "select sealmode from ebs_basicinfo where accno='"+accno+"'";
		String sealmode = "";
		List<?> list = dao.findBySql(sql);
		if(list!=null && list.size() != 0){
			sealmode=(list.get(0)==null) ? "" : list.get(0).toString();
		}
		return sealmode;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException {
		DetachedCriteria dc = DetachedCriteria.forClass(AccNoMainData.class);
		dc.add(Restrictions.eq("voucherNo", voucherno));
		dc.addOrder(Order.asc("accNoIndex"));
		return (List<AccNoMainData>) dao.findByDetachedCriteria(dc);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public List<AccNoMainData> batchUpdate(List<AccNoMainData> list)
			throws XDocProcException {
		for (AccNoMainData accnoMainData : list) {
			super.update(accnoMainData);
		}
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AccNoMainData> getAccnoMainDataByDocDate(
			Map<String, String> queryMap, PageParam param)
			throws XDocProcException {
		Session session = dao.getHibernateTemplate().getSessionFactory()
				.openSession();
		Criteria criteria = session.createCriteria(AccNoMainData.class);
		Criteria criteria1 = session.createCriteria(AccNoMainData.class);
		// 循环Map取出查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					criteria.add(Restrictions.eq(entry.getKey(),
							entry.getValue()));
					criteria1.add(Restrictions.eq(entry.getKey(),
							entry.getValue()));
				}
			}
		}
		Integer countNumber = (Integer) criteria.setProjection(
				Projections.rowCount()).uniqueResult();
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		criteria1.setFirstResult(firstResult);
		criteria1.setMaxResults(pageSize);
		List<AccNoMainData> result = criteria1.list();

		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + result.size());// 当前页显示的最后一条记录
		session.close();
		return result;
	}
	
	//对账有效率统计
	public List<?> getEbillMatchAnalyseResult(Map<String, String> queryMap,
			PageParam param, String docDate) throws XDocProcException {
		String sql = "select a.idCenter,a.idbank, ";
		sql += " count(case when a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.accno end ) as "
				+ " SendCount"
				+ String.valueOf(6)
				+ "," // 账户数
				+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim() + "' " 
				+ "then a.accno end) as MatchCount"
				+ String.valueOf(6) + ",";// 有效数
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			sql += " count(case when a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "then a.accno end ) as "
					+ " SendCount"
					+ String.valueOf(i)
					+ "," // 账单数
					+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ " or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim()+ "' "
					+ "then a.accno end) as MatchCount" + String.valueOf(i)
					+ ",";// 有效数
		}
		sql += "  a.bankname ";
		sql += " from autek.ebs_checkmaindata c " 
				+ "left join autek.ebs_accnomaindata a " 
				+ "on( a.voucherno = c.voucherno) where 1=1  ";
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3)='"+entry.getValue()+"' ";
					}else{	
					sql += " and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		sql += " group by a.idCenter,a.idbank,a.bankName order by a.idCenter ";
		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = dao.findBySql(sql, firstResult, pageSize);
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		return list;

	}
	
	//对账有效率统计(按单位)
	@Override
	public List<?> getEbillMatchAnalyseResultCount(
			Map<String, String> queryMap, PageParam param, String docDate,String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "select ";
		if(selectCount.equals("countIdCenter")){
			sql += "c.idCenter,";
		}
		sql += " count(case when a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.accno end ) as "
				+ " SendCount"
				+ String.valueOf(6)
				+ "," // 账单数
				+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim() + "' " 
				+ "then a.accno end) as MatchCount"
				+ String.valueOf(6) + ",";// 有效数
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			sql += " count(case when a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "then a.accno end ) as "
					+ " SendCount"
					+ String.valueOf(i)
					+ "," // 账单数
					+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ " or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim()+ "' "
					+ "then a.accno end) as MatchCount" + String.valueOf(i)
					+ ",";// 有效数
		}
		int lastIndex = sql.lastIndexOf(',');
		sql = sql.substring(0, lastIndex);
		sql += " from autek.ebs_checkmaindata c " 
				+ " left join autek.ebs_accnomaindata a " 
				+ "on( a.voucherno = c.voucherno) where 1=1 ";
		
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3)='"+entry.getValue()+"' ";
					}else{	
					sql += " and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		String groupSql = "";
		if(selectCount.equals("countIdCenter")){
			groupSql += " group by c.idCenter order by c.idCenter ";
		}
		sql += groupSql;
		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		List<?> list = dao.findBySql(sql, firstResult, pageSize);
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		return list;
	}
	
	@Override
	public List<?> getAllIdBranchName() throws XDocProcException {
		// TODO Auto-generated method stub
		//String sql = "select b.idbank, b.cname from param_bank b join (select t.idbranch from PARAM_BANK t group by t.idbranch) a on (a.idbranch = b.idbank)";
		String sql = "select b.idbank, b.cname from param_bank b where b.nlevel<3";
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}
	
	//对账有效率统计(导出)
	public List<?> getAllEbillMatchAnalyseResult(Map<String, String> queryMap,
			String docDate) throws XDocProcException {
		String sql = "select a.idcenter, a.idbank, ";
		sql += " count(case when a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.accno end ) as "
				+ " SendCount"
				+ String.valueOf(6)
				+ "," // 账户数
				+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
				+ docDate.trim() + "'" + "then a.accno end) as MatchCount"
				+ String.valueOf(6) + ",";// 有效数
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			sql += " count(case when a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "then a.accno end ) as "
					+ " SendCount"
					+ String.valueOf(i)
					+ "," // 账户数
					+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
					+ docDate.trim() + "'"
					+ "then a.accno end) as MatchCount" + String.valueOf(i)
					+ ",";// 有效数
		}
		sql += " a.bankName ";
		sql += " from autek.ebs_checkmaindata c " 
				+ " left join autek.ebs_accnomaindata a " 
				+ "on( a.voucherno = c.voucherno) where 1=1  ";
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3) = '"+entry.getValue()+"' ";
					}else{
					sql += "and a." + entry.getKey() + "='" + entry.getValue() + "' ";
					}
				}
			}
		}
		sql += " group by a.idcenter,a.idbank,a.bankName order by a.idcenter ";
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}
	
	//对账有效率统计(按单位导出)
	@Override
	public List<?> getAllEbillMatchAnalyseResultCount(
			Map<String, String> queryMap, String docDate, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "select ";
		if(selectCount.equals("countIdCenter")){
			sql += "a.idCenter,";
		}
		sql += " count(case when a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.accno end ) as "
				+ " SendCount"
				+ String.valueOf(6)
				+ "," // 账单数
				+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim() + "' " 
				+ "then a.accno end) as MatchCount"
				+ String.valueOf(6) + ",";// 有效数
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			sql += " count(case when a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "then a.accno end ) as "
					+ " SendCount"
					+ String.valueOf(i)
					+ "," // 账单数
					+ "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ " or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"+ docDate.trim()+ "' "
					+ "then a.accno end) as MatchCount" + String.valueOf(i)
					+ ",";// 有效数
		}
		int lastIndex = sql.lastIndexOf(',');
		sql = sql.substring(0, lastIndex);
		sql += " from autek.ebs_checkmaindata c " 
				+ " left join autek.ebs_accnomaindata a " 
				+ "on( a.voucherno = c.voucherno) where 1=1  ";
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3)='"+entry.getValue()+"' ";
					}else{	
					sql += " and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		String groupSql = "";
		if(selectCount.equals("countIdCenter")){
			groupSql += " group by a.idCenter order by a.idCenter ";
		}
		sql += groupSql;
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}

	//对账有效明细统计
	public List<?> getEbillMatchParam(Map<String, String> queryMap,
			PageParam param, String docDate) throws XDocProcException {
		Map<String, String> myMap = new HashMap<String, String>();
		String docDate6 = "";
		String docDate5 = "";
		String docDate4 = "";
		String docDate3 = "";
		String docDate2 = "";
		String docDate1 = "";
		docDate6 = docDate;
		String selectSql = "select a.idcenter,a.idbank, b.bankname,a.accno,a.accnoson, ";
		selectSql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.voucherno  end) as MatchCount6,";
		selectSql += "count(case when  a.docdate='" + docDate.trim()
				+ "' then a.voucherno  end) as MatchCount61,";//当月是否有账单
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			myMap.put(String.valueOf(i), docDate);
			selectSql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ "	or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "	then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + ",";
			selectSql += "count(case when  a.docdate='" + docDate.trim()
					+ "' then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + "1,";
		}

		selectSql += "b.accname ";
		
		String whereSql = "";
		whereSql += " from autek.ebs_accnomaindata a,autek.ebs_checkmaindata c,autek.ebs_basicinfo b where a.docdate in ";
		// 循环Map取出条件
		if (myMap != null) {
			for (Map.Entry<String, String> entry : myMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("5")) {
						docDate5 = entry.getValue().trim();
					}
					if (entry.getKey().equals("4")) {
						docDate4 = entry.getValue().trim();
					}
					if (entry.getKey().equals("3")) {
						docDate3 = entry.getValue().trim();
					}
					if (entry.getKey().equals("2")) {
						docDate2 = entry.getValue().trim();
					}
					if (entry.getKey().equals("1")) {
						docDate1 = entry.getValue().trim();
					} else {

					}
				}
			}
		}
		whereSql += "(" + "'" + docDate6 + "'" + "," + "'" + docDate5 + "'" + ","
				+ "'" + docDate4 + "'" + "," + "'" + docDate3 + "'" + "," + "'"
				+ docDate2 + "'" + "," + "'" + docDate1 + "'" + ")";
		whereSql += " and  a.voucherno=c.voucherno and a.accno=b.accno  ";
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						whereSql+=" and substr(a.subjectNo,1,3) ='"+entry.getValue()+"' ";
					}else{
						whereSql += "and a." + entry.getKey() + "='" + entry.getValue()+ "' ";
					}
				}
			}
		}
		
		String groupSql = " group by a.idcenter,a.idbank, b.bankname,a.accno,a.accnoson,b.accname ";
		String orderSql = " order by a.idcenter, a.idbank,a.accno ";
		
		String countSql = "select count(1) " + whereSql + orderSql;
		List<?> countList = dao.findBySql(countSql);
		int countNumber = Integer.parseInt(countList.get(0).toString()); // 得到数据的总条数
//		Integer countNumber = dao.findBySql(sql).size();
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		
		selectSql += whereSql + groupSql + orderSql;
		List<?> list = dao.findBySql(selectSql, firstResult, pageSize);
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		param.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
		return list;

	}
	
	//对账有效明细统计(导出)
	public List<?> getAllEbillMatchParam(Map<String, String> queryMap,
			String docDate) throws XDocProcException {
		Map<String, String> myMap = new HashMap<String, String>();
		String docDate6 = "";
		String docDate5 = "";
		String docDate4 = "";
		String docDate3 = "";
		String docDate2 = "";
		String docDate1 = "";
		docDate6 = docDate;
		String sql = "select a.idCenter,a.idbank, b.bankname,a.accno,a.accnoson, ";
		sql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31')) "
				+ "or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
				+ docDate.trim()
				+ "'"
				+ "then a.voucherno  end) as MatchCount6,";
		sql += "count(case when  a.docdate='" + docDate.trim()
				+ "' then a.voucherno  end) as MatchCount61,";//当月是否有账单
		UtilBase u = new UtilBase();
		for (int i = 5; i > 0; i--) {
			docDate = u.getMothLastDate(docDate);
			myMap.put(String.valueOf(i), docDate);
			sql += "count(case when ((a.finalcheckflag in ('3','4') and c.proveflag in ('20','22','31'))"
					+ "	or (a.sendmode ='3' and a.finalcheckflag in ('3','4'))) and a.docdate='"
					+ docDate.trim()
					+ "'"
					+ "	then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + ",";
			sql += "count(case when  a.docdate='" + docDate.trim()
					+ "' then a.voucherno  end) as MatchCount"
					+ String.valueOf(i) + "1,";
		}

		sql += "b.accname ";
		sql += " from autek.ebs_accnomaindata a,autek.ebs_checkmaindata c, autek.param_bank p,autek.ebs_basicinfo b where a.docdate in ";
		// 循环Map取出条件
		if (myMap != null) {
			for (Map.Entry<String, String> entry : myMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if (entry.getKey().equals("5")) {
						docDate5 = entry.getValue().trim();
					}
					if (entry.getKey().equals("4")) {
						docDate4 = entry.getValue().trim();
					}
					if (entry.getKey().equals("3")) {
						docDate3 = entry.getValue().trim();
					}
					if (entry.getKey().equals("2")) {
						docDate2 = entry.getValue().trim();
					}
					if (entry.getKey().equals("1")) {
						docDate1 = entry.getValue().trim();
					} else {

					}
				}
			}
		}
		sql += "(" + "'" + docDate6 + "'" + "," + "'" + docDate5 + "'" + ","
				+ "'" + docDate4 + "'" + "," + "'" + docDate3 + "'" + "," + "'"
				+ docDate2 + "'" + "," + "'" + docDate1 + "'" + ")";
		sql += " and  a.voucherno=c.voucherno and a.idbank=p.idbank and a.accno=b.accno ";
		// 循环Map取出条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("subjectNo")){
						sql += " and substr(a.subjectNo,1,3) = '"+entry.getValue()+"' ";
					}else{
					sql += "and a." + entry.getKey() + "='" + entry.getValue() + "' ";
					}
				}
			}
		}

		sql += " group by a.idCenter,a.idbank, b.bankname,a.accno,a.accnoson,b.accname order by a.idCenter,a.idbank,a.accno ";
		List<?> list = dao.findBySql(sql);
		return list;

	}

	@SuppressWarnings("unchecked")
	@Override
	public AccNoMainData getAccnoMainDataByAccno(String accNo)
			throws XDocProcException {
		AccNoMainData accNoMainData = new AccNoMainData();
		String hql = " from AccNoMainData where accNo='"+accNo+"'";
		List<AccNoMainData> accNoMainDataList =(List<AccNoMainData>) dao.findByHql(hql);
		if(accNoMainDataList.size()!=0){
			accNoMainData = accNoMainDataList.get(0);
		}
		return accNoMainData;
	}

	
	
}
