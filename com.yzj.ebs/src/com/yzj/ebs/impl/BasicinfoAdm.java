package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;

/**
 * 创建于:2012-9-29<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * Basicinf表操作访问服务接口定义
 * 
 * @author 秦靖锋,施江敏
 * @version 1.0.0
 */
public class BasicinfoAdm extends BaseService<BasicInfo> implements
		IBasicInfoAdm {
	
	//---------------------------------二期报表开发begin-----------------------------------
	/**
	 * 对账集中情况统计
	 * 
	 * 分页查询Basicinfo表
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 * 			  分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public List<?> getFocusReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException{
		
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "t.idCenter,t.idBank,t.bankName,";
		}
		//SendMode 1:柜台 2:邮寄 3:网银 4:面对面   null:其他
		selectSql +=  "count(case when t.sendmode=2 then t.accno end) as mailCount," // 邮寄对账账户数
					+ "count(case when t.sendmode=3 then t.accno end) as netCount," // 网银对账账户数
					+ "count(case when t.sendmode=4 then t.accno end) as faceCount," // 面对面对账账户数
					+ "count(case when t.sendmode=1 then t.accno end) as counterCount, " // 柜台对账账户数
					+ "count(case when t.sendmode is null then t.accno end) as otherCount, " // 其它对账账户数
					+ "count(t.accno) as checkCount " // 需对账账户数
					+ "from autek.ebs_basicinfo t where t.ischeck=0 ";//只统计对账的账户
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by t.idCenter ";
			orderSql += " order by t.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by t.idCenter,t.idBranch,t.idBank,t.bankName " ;
			orderSql = " order by t.idcenter,t.idbank";
		}
		sql = selectSql + groupSql +orderSql;
		System.out.println("对账集中情况统计sql:"+sql);
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
	 * 网银对账签约率
	 * 
	 * 分页查询Basicinfo表
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 * 			  分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public List<?> getNetSignReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException{
		
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "t.idCenter,t.idBank,t.bankName,";
		}
		
		selectSql +=  "count(distinct t.accno) as accCount," // 总账户数
					+ "count(distinct a.accno) as netCount," // 网银开户数
					+ "count(distinct case when t.sendmode=3 then t.accno end) as netSignCount," // 网银对账签约数
					+ "count(distinct case when t.sendmode=3 and signflag=1 then t.accno end) as autoSignCount, " // 自助签约数
					+ "count(distinct case when t.sendmode=3 and (signflag=0 or signflag is null) then t.accno end) as counterSignCount " // 柜面签约数
					+ "from autek.ebs_basicinfo t left join ebs_acctlist a on (t.accno=a.accno) where t.ischeck='0' ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by t.idCenter ";
			orderSql += " order by t.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by t.idCenter,t.idBranch,t.idBank,t.bankName " ;
			orderSql = " order by t.idcenter,t.idbank";
		}
		sql = selectSql + groupSql +orderSql;
		System.out.println("网银对账签约率sql:"+sql);
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
	 * 网银对账率统计
	 * 
	 * @param queryMap
	 *            查询条件Map
	 * @param param
	 * 			  分页查询相关条件
	 * @return 查询结果集
	 * @throws XDocProcException
	 *             执行时发生异常则抛出
	 */
	public List<?> getNetCheckReportList(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount) throws XDocProcException{
		
		String sql = "";
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += "t.idCenter, ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			selectSql += "t.idCenter,t.idBank,t.bankName,";
		}
		
		selectSql +=  "count(case when t.sendmode=3 then t.accno end) as netSignCount," // 网银对账签约数
					+ "count(case when t.sendmode=3 and t.finalCheckflag in('3','4') then t.accno end) as netCheckSuccessCount " // 网银对账成功数
					+ "from autek.ebs_accnomaindata t where 1=1 ";
		
		//遍历查询条件
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					if(entry.getKey() != null && "docDate".equals(entry.getKey())){//日期区间查询
						selectSql += " and " + entry.getValue();
					}else{
						selectSql += " and t." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
					
				}
			}
		}
		
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by t.idCenter ";
			orderSql += " order by t.idCenter ";
		}
		if(selectCount!=null && selectCount.equals("countIdBank")){
			groupSql += " group by t.idCenter,t.idBranch,t.idBank,t.bankName " ;
			orderSql = " order by t.idcenter,t.idbank";
		}
		sql = selectSql + groupSql +orderSql;
		System.out.println("网银对账率统计sql:"+sql);
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
	//---------------------------------二期报表开发end-----------------------------------
		
		
	/**
	 * 查询特殊账户 j_sun
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<BasicInfo> getBasicinfoData(Map<String, String> queryMap,
			PageParam param) throws XDocProcException {

		List<BasicInfo> list = new ArrayList<BasicInfo>();
		int pageSize = param.getPageSize();
		int startRow = (param.getCurPage()-1)*pageSize;
		StringBuffer hql = new StringBuffer();
		hql.append("from BasicInfo where 1=1 ");
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("accName") || entry.getKey().equals("sendAddress")){
						hql.append(" and ").append(entry.getKey()).append(" like '%")
						.append(entry.getValue().trim()).append("%' ");
					}else if("5".equals(entry.getValue()) 
							&& "sendMode".equals(entry.getKey())){
						hql.append(" and sendMode is null ");
					}else if("5".equals(entry.getValue()) 
							&& "sealMode".equals(entry.getKey())){
						hql.append(" and sealMode is null ");
					}else{
						hql.append(" and ").append(entry.getKey()).append("='")
						.append(entry.getValue().trim()).append("' ");
					}
				}
			}
		}
		hql.append("order by idbank,autoid");
		list = (List<BasicInfo>) dao.findbyPage(hql.toString(), startRow, pageSize);
		hql.insert(0, "select count(*) ");//统计数目hql语句
		List<?> countList = dao.findByHql(hql.toString());
		int countNumber = ((Long)countList.get(0)).intValue(); // 得到数据的条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal(countNumber);
		param.setTotalPage(totalPage);
		int lastResult = curPage * pageSize;
		if (lastResult > countNumber) {
			lastResult = countNumber;
		}
		param.setLastResult(lastResult);// 当前页显示的最后一条记录
		return list;
	}
	
	@Override
	/**
	 * 查询网银账户
	 */
	public List<?> getAcctListData(Map<String, String> queryMap, PageParam param)
			throws XDocProcException {
		int pageSize = param.getPageSize(); // 每页显示结果条数
		int startRow = (param.getCurPage()-1)*pageSize;// 分页时显示的第一条记录，默认从0开始
		String sql = "select b.* from EBS_BASICINFO b where b.accno in (select distinct accno from ebs_acctlist) ";
		String creatWhere = "";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if(entry.getValue() != null && entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("accName") || entry.getKey().equals("sendAddress")){
						creatWhere += " and b." + entry.getKey() + " like '%" + entry.getValue().trim() + "%' ";
					}else if("5".equals(entry.getValue())  && "sendMode".equals(entry.getKey())){
						creatWhere += " and b.sendMode is null ";
					}else {
						creatWhere += " and b." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
				}
			}
		}
		sql += creatWhere + "order by b.idbank,b.autoId";
		List<?> list = dao.findBySql(sql, startRow, pageSize);
		String countSql = "select count(*) from EBS_BASICINFO b where b.accno in (select distinct accno from ebs_acctlist) "+
				creatWhere+" order by b.idbank,b.autoId ";
		List<?> countList = dao.findBySql(countSql);
		int countNumber = Integer.parseInt(countList.get(0).toString()); // 得到数据的条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);
		param.setTotalPage(totalPage);
		int lastResult = curPage * pageSize;
		if (lastResult > countNumber) {
			lastResult = countNumber;
		}
		param.setLastResult(lastResult);// 当前页显示的最后一条记录
		return list;
	}
	
	@Override
	/**
	 * 导出所有网银账户
	 */
	public List<?> getAllAcctList(Map<String, String> queryMap)
			throws XDocProcException {
		String sql = "select b.* from EBS_BASICINFO b where b.accno in (select distinct accno from ebs_acctlist) ";
		String creatWhere = "";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if(entry.getValue() != null && entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("accName") || entry.getKey().equals("sendAddress")){
						creatWhere += " and b." + entry.getKey() + " like '%" + entry.getValue().trim() + "%' ";
					}else if("5".equals(entry.getValue())  && "sendMode".equals(entry.getKey())){
						creatWhere += " and b.sendMode is null ";
					}else {
						creatWhere += " and b." + entry.getKey() + "='" + entry.getValue().trim() + "' ";
					}
				}
				
			}
		}
		sql += creatWhere + "order by b.idbank,b.autoid ";
		List<?> list = dao.findBySql(sql);
		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BasicInfo> getAllBasicInfo(Map<String, String> queryMap)
			throws XDocProcException {
		List<BasicInfo> list = new ArrayList<BasicInfo>();
		StringBuffer hql = new StringBuffer();
		hql.append("from BasicInfo where 1=1 ");
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					if(entry.getKey().equals("accName") || entry.getKey().equals("sendAddress")){
						hql.append(" and ").append(entry.getKey()).append(" like '%")
						.append(entry.getValue().trim()).append("%' ");
					}else if("5".equals(entry.getValue()) 
							&& "sendMode".equals(entry.getKey())){
						hql.append(" and sendMode is null ");
					}else if("5".equals(entry.getValue()) 
							&& "sealMode".equals(entry.getKey())){
						hql.append(" and sealMode is null ");
					}else{
						hql.append(" and ").append(entry.getKey()).append("='")
						.append(entry.getValue().trim()).append("' ");
					}
				}
				
			}
		}
		hql.append(" order by idbank");
		list = (List<BasicInfo>) dao.findByHql(hql.toString());
		return list;
	}

	@Override
	public int deleteBasicInfoByAccNo(String accNo) throws XDocProcException {
		int result = (Integer) super
				.ExecQuery("delete from Basicinfo t where t.accno='" + accNo
						+ "'");
		return result;
	}

	@Override
	public BasicInfo getOneByAccNo(String accNo) throws XDocProcException {
		List<?> list = dao.findByHql("from BasicInfo where accNo='" + accNo
				+ "'");
		if (list != null && list.size() != 0) {
			return (BasicInfo) list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<?> getAllData(Map<String, String> queryMap,
			final String entityName, PageParam param) throws XDocProcException {
		// TODO Auto-generated method stub
		String selectsql = "select ab.idcenter,ab.idbank,ab.bankname,ab.accno,ab.accnoson,ab.sendmode,ab.accname,ab.docdate,ab.currency,ab.credit,ab.finalcheckflag ,ab.billcount ";
		String selectcount = "select count(*) ";
		String fromsql = "from (select a.voucherno ,b.idcenter,b.idbranch,b.idbank,b.bankname,a.accno,a.accnoson,a.sendmode,b.accname,a.docdate,a.currency,a.credit,a.finalcheckflag,c.billcount "
				+ "from ebs_checkmaindata b,ebs_accnomaindata a,"
				+ "(select d.voucherno, count(distinct e.CHECKDATE) as billcount from ebs_accnomaindata d left join "
				+ entityName
				+ " e on (d.accno = e.accno) where d.voucherno is not null group by d.voucherno) c "
				+ " where b.voucherno=a.voucherno and a.voucherno=c.voucherno(+) ) ab ";
		String wheresql = " where ab.sendmode='3' ";
		Set<Map.Entry<String, String>> key = queryMap.entrySet();
		Iterator<Map.Entry<String, String>> its = key.iterator();
		while (its.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) its
					.next();
			if (StringUtils.isNotEmpty(entry.getValue())) {
				if((entry.getKey().equals("finalcheckflag")) && (entry.getValue().equals("1"))){
					wheresql += "  and ab." + entry.getKey().trim() + " is null ";
				}else{
					wheresql += "  and ab." + entry.getKey().trim() + "='"
						+ entry.getValue().trim() + "'";
				}
			}
		}

		String sql = "";
		String countsql = "";
		String groupbysql = " group by ab.idcenter,ab.idbank,ab.accno,ab.accnoson,ab.bankName,ab.sendmode,ab.accname,ab.docdate,ab.currency,ab.credit,ab.finalcheckflag,ab.billcount order by ab.idcenter ";
		sql = selectsql + fromsql + wheresql + groupbysql;
		countsql = "select count(1) from (" + selectcount + fromsql + wheresql
				+ groupbysql + ")";
		return dao.getByPageFromSql(sql, countsql, param);
	}

	@Override
	public List<?> getOneData(Map<String, String> queryMap,
			final String entityName) throws XDocProcException {
		String selectsql = "select ab.idcenter,ab.idbank,ab.bankname,ab.accno,ab.accnoson,ab.sendmode,ab.accname,ab.docdate,ab.currency,ab.credit,ab.finalcheckflag ,ab.billcount ";
		String fromsql = "from (select a.voucherno ,b.idcenter,b.idbranch,b.idbank,b.bankname,a.accno,a.accnoson,a.sendmode,b.accname,a.docdate,a.currency,a.credit,a.finalcheckflag,c.billcount "
				+ "from ebs_checkmaindata b,ebs_accnomaindata a,"
				+ "(select d.voucherno, count(distinct e.CHECKDATE) as billcount from ebs_accnomaindata d left join "
				+ entityName
				+ " e on (d.accno = e.accno) where d.voucherno is not null group by d.voucherno) c "
				+ " where b.voucherno=a.voucherno and a.voucherno=c.voucherno(+) ) ab ";
		String wheresql = " where ab.sendmode='3'  ";
		Set<Map.Entry<String, String>> key = queryMap.entrySet();
		Iterator<Map.Entry<String, String>> its = key.iterator();
		while (its.hasNext()) {
			Map.Entry<String, String> entry = (Map.Entry<String, String>) its
					.next();
			if (StringUtils.isNotEmpty(entry.getValue())) {
				if((entry.getKey().equals("finalcheckflag")) && (entry.getValue().equals("1"))){
					wheresql += "  and ab." + entry.getKey().trim() + " is null ";
				}else{
					wheresql += "  and ab." + entry.getKey().trim() + "='"
						+ entry.getValue().trim() + "'";
				}
			}
		}

		String sql = "";
		String groupbysql = " group by ab.idcenter, ab.idbank,ab.accno,ab.accnoson,ab.bankName,ab.sendmode,ab.accname,ab.docdate,ab.currency,ab.credit,ab.finalcheckflag,ab.billcount order by ab.idcenter ";
		sql = selectsql + fromsql + wheresql + groupbysql;
		List<?> list = dao.findBySql(sql);
		return list;
	}

	/***
	 * 特殊账户统计
	 */
	public List<?> analyseBlackWhite(Map<String, String> queryMap,
			PageParam pageParam,boolean isPaged) throws XDocProcException {
		String sql = "select idcenter,idbank , bankname ,count(case when isspecile = '1' and ischeck='1' then AUTOID end) as blackCount , "
				+ "count(case when isspecile = '1' and ischeck='0' then AUTOID end) as whiteCount from ebs_basicinfo where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and " + entry.getKey() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		sql += "group by idcenter,idbank,bankname order by idCenter,idBank";
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			//查询全量,进行导出
			return dao.findBySql(sql);
		}
	}
	
	//特殊账户统计(按单位统计)
	@Override
	public List<?> analyseBlackWhiteCount(Map<String, String> queryMap,
			PageParam pageParam, boolean isPaged, String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		String sql = "";
		
		String selectSql = "select ";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			selectSql += " idCenter,";
		}
		selectSql += "count(case when isspecile = '1' and ischeck='1' then AUTOID end) as blackCount , "
					+ "count(case when isspecile = '1' and ischeck='0' then AUTOID end) as whiteCount from ebs_basicinfo where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					selectSql += " and " + entry.getKey() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		String groupSql = "";
		String orderSql = "";
		if(selectCount!=null && selectCount.equals("countIdCenter")){
			groupSql += " group by idCenter ";
			orderSql += " order by idCenter";
		}
		sql = selectSql + groupSql + orderSql;
		if(isPaged){
			Integer countNumber = dao.findBySql(sql).size();
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			if (curPage > totalPage) {
				curPage = totalPage;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			List<?> list = dao.findBySql(sql, firstResult, pageSize);
			pageParam.setFirstResult(firstResult);
			pageParam.setCurPage(curPage);
			pageParam.setTotal((int) countNumber);
			pageParam.setTotalPage(totalPage);
			pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			return list;
		}else{
			//查询全量,进行导出
			return dao.findBySql(sql);
		}
	}
	
	//获得所有对账中心的名字
	@Override
	public List<?> getAllIdBranchName() throws XDocProcException {
		// TODO Auto-generated method stub
		//String sql = "select b.idbank, b.cname from param_bank b join (select t.idbranch from PARAM_BANK t group by t.idbranch) a on (a.idbranch = b.idbank)";
		String sql = "select b.idbank, b.cname from param_bank b where b.nlevel<3";
		List<?> list = null;
		list = dao.findBySql(sql);
		return list;
	}


	/***
	 * 特殊帐号统计列表导出全部
	 */
	public List<?> exportanalyseBlackWhite(Map<String, String> queryMap,
			PageParam pageParam) throws XDocProcException {
		String sql = "select idcenter,idbank , bankname ,count(case when  isspecile = '1' and ischeck='1' then AUTOID end) as blackCount , "
				+ "count(case when isspecile = '1' and ischeck='0' then AUTOID end) as whiteCount from ebs_basicinfo where 1=1 ";
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().trim().length() != 0) {
					sql += " and " + entry.getKey() + "='"
							+ entry.getValue().trim() + "' ";
				}
			}
		}
		sql += "group by idcenter,idbank,bankname";
		List<?> list = dao.findBySql(sql);
		return list;
	}

	/**
	 * 把特殊帐户标识和不对账表示改成 正常账户标识
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void changeToNormal(String accNo) throws XDocProcException {
		String querySql = "update autek.ebs_basicinfo set ischeck=0,isspecile=0 where accno="
				+ accNo;
		// dao.ExecQuery(querySql);
		try {
			SQLQuery query = dao.getHibernateTemplate().getSessionFactory()
					.getCurrentSession().createSQLQuery(querySql);
			query.executeUpdate();
		} catch (Exception e) {
			throw new XDocProcException("执行查询 " + querySql + " 失败", e);
		}
	}

	/**
	 * 账户信息更新
	 */
	public void updateBasicInfo(BasicInfo basicInfo) throws XDocProcException {
		dao.saveOrUpdate(basicInfo);
	}

	


	

	

}
