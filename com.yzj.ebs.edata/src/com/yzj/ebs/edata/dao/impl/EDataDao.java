/**
 * 创建于:2013-9-20
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * @author lixiangjun
 * @version 1.1
 */
package com.yzj.ebs.edata.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.edata.bean.BaseParamBean;
import com.yzj.ebs.edata.bean.BillBean;
import com.yzj.ebs.edata.dao.IEdataDao;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.common.util.SIDCreator;

public class EDataDao implements IEdataDao {
	private static WFLogger logger = WFLogger.getLogger(EDataDao.class);
	public Connection connImport = null; // 数据导入连接
	public Connection connProcess = null; // 数据处理连接
	protected String driverClass;
	protected String jdbcUrl;
	protected String user;
	protected String password;

	/**
	 * 批量插入数据
	 * 
	 * @param tableName
	 * @param dataValues
	 * @return
	 * @throws DaoDaoException
	 */
	@SuppressWarnings("rawtypes")
	public void executeSql(String tableName, List dataValues, String dataDate)
			throws Exception {
		Statement pre = null;
		DateFormat formatDate = new SimpleDateFormat("yyyyMMdd");
		try {
			if (connImport == null || connImport.isClosed()) {
				connImport = DBUtil.open(jdbcUrl, driverClass, user, password);
			}
			connImport.setAutoCommit(false);
			pre = connImport.createStatement();
			for (int i = 0; i < dataValues.size(); i++) {
			  	if ("EBS_UTBLBRCD".equals(tableName)) {
					String sid = SIDCreator.getUUID();
					pre.addBatch("insert into "
							+ tableName
							+ "(AUTOID,BRCD,DESCNAM,ZLVLCODE,ZREPBRCD,PHONE,ADDRESS,ZIP,SID,DATADATE)"
							+ " values(EBS_UTBLBRCD_AUTOID.nextval,"
							+ dataValues.get(i) + ",'" + sid + "','"+dataDate+"' )");
				} 
			  	else if ("EBS_KUB_USER".equals(tableName)) {
					pre.addBatch("insert into " + tableName
							+ "(AUTOID,PEOPLECODE,PEOPLENAME,ORGNO,SEX)"
							+ " values(EBS_KUB_USER_AUTOID.nextval,"
							+ dataValues.get(i) + ")");
				}
				else if ("EBS_INNERBASICINFO".equals(tableName)) {
					pre.addBatch("insert into " + tableName
							+ "(AUTOID,ACCNO,DATADATE,BAL)"
							+ " values(INNERBASICINFO_AUTOID.nextval,"
							+ dataValues.get(i) + ")");
				}
				else if ("EBS_MAINDATA".equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(AUTOID,IDBANK,BANKNAME,CUSTID,ACCNO,ACCSON,ACCTYPE,ACCNAME,OPENDATE,CLOSEDATE,CDEBFLAG," +
							"CURRTYPE,PRODTYPE,PRODDESC,SUBNOC,BAL,STAMT,MAXTAMT,AVBAL,ACCSTATE,ZIP,ADDRESS,RESPON,PHONE," +
							"SEALMODE,ACCCYCLE,SENDMODE,DATADATE,IMPORTDATE)"
							+ " values(EBS_MAINDATA_AUTOID.nextval,"
							+ dataValues.get(i) + ",'" + dataDate + "', '"
							+ formatDate.format(new Date()) + "')");
				}
			  	else if ("EBS_XDCKLIST".equals(tableName)) {
			  		pre.addBatch("insert into "
			  				+ tableName
			  				+ "(AUTOID,ACCNO)"
			  				+ " values(EBS_XDCKLIST_AUTOID.nextval, "
			  				+ dataValues.get(i) + ")");
			  	}
				else if ("EBS_ACCTLIST".equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(CUSTID,ACCNO,SINGFLAG )"
							+ " values("
							+ dataValues.get(i) + ")");
				}
				else if ("EBS_KNP_EXRT".equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(CURRTYPE,CHNNAME,ENGNAME,EXCHANGERATE)"
							+ " values("
							+ dataValues.get(i) + ")");
				}
				else if (("EBS_DEPHIST").equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(AUTOID,TX_DATE,TRACE_NO,AC_NO,AC_SEQN,IO_IND,TRAN_AMT,BAL,CURRTYPE,TO_ACCNO,TO_ACCNAME,ABS," +
							"DOCNUM,TRACE_CODE,UNUSEDDATE,UNUSEDTIME,TRACETIME,DATADATE,IMPORTDATE) " +
							"values( EBS_DEPHIST_AUTOID.nextval,"
							+ dataValues.get(i) + ",'"+ dataDate + "', '"
							+ formatDate.format(new Date()) + "' )");
				}
			  	
			  	//--2期改造添加修改修改定期子账户账号
				else if ("EBS_AAPFZ0".equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(ACCNO,ACCNOID,ACCSONNUM,ACCSON,ACCSONID,ACCSTATE)"
							+ " values("
							+ dataValues.get(i) + ")");
				}
				else if ("EBS_NETACRELATION".equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(DQACCNO,HQACCNO)"
							+ " values("
							+ dataValues.get(i) + ")");
				}
				else if (("EBS_INNERACCNODETAIL").equals(tableName)) {
					pre.addBatch("insert into "
							+ tableName
							+ "(AUTOID,trad_date,seri_no,curr_code,acct,oppost_acct,oppost_acct_name,vouch_no," 
							+ "summy,borrow_lend_sign,trad_amt,acct_bal,trad_code,host_syst_time,DATADATE,IMPORTDATE) "
							+ "values( EBS_INNERACCNODETAIL_AUTOID.nextval,"
							+ dataValues.get(i) + ",'"+ dataDate + "', '"
							+ formatDate.format(new Date()) + "' )");
				}
				else{
					throw new DaoException("没有找到匹配的表:"+tableName);
				}
			}
			
			pre.executeBatch();
			connImport.commit();
			pre.clearBatch();
			pre.close();
			
			//最后一个文件导完再关闭连接
			if ("DEPHIST".equals(tableName)) {
				connImport.close();
			}
		} catch (Exception e) {
			try {
				connImport.rollback();
				if(pre!=null){
					pre.close();
				}
				if(connImport!=null){
					connImport.close();
				}
			} catch (Exception e1) {
				throw new Exception("回滚失败，" + e.getMessage());
			}
			e.printStackTrace();
			throw new Exception(""+tableName+"的数据导入失败，" + e.getMessage());
		}
	}
	
	
	/**
	 * 查找表数据总数
	 * 
	 * @param tableName
	 * @return
	 * @throws DaoDaoException
	 */
	public long queryTableCount(String tableName) throws DaoException {
		ResultSet res = null;
		Statement pre = null;
		long cou = 0;
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user, password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery("select count(*) cou from " + tableName);
			while (res.next()) {
				cou = res.getLong("cou");
			}
			pre.clearBatch();
			res.close();
			pre.close();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e1) {
			}
			throw new DaoException("连接数据库失败，" + e.getMessage());
		}
		return cou;
	}

	/**
	 * lixiangjun
	 * 查询 PARAM_SYSBASE 表中的参数参数值
	 * @param parma
	 * @return
	 */
	@Override
	public List<BaseParamBean> findParamTable(String sql) throws DaoException {
		ResultSet res = null;
		Statement pre = null;
		List<BaseParamBean> paramList = new ArrayList<BaseParamBean>();
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user,
						password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery(sql);
			while (res.next()) {
				BaseParamBean paramBean = new BaseParamBean();
				paramBean.setIdcenter(res.getString("idcenterno"));
				paramBean.setMaxSingleCredit_m(res
						.getDouble("maxSingleCredit_m"));
				paramBean.setMaxSingleCredit_b_m(res
						.getDouble("maxSingleCredit_b_m"));
				paramBean.setMaxSingleCredit_l_m(res
						.getDouble("maxSingleCredit_l_m"));
				paramBean.setTotalAmount_b_m(res.getDouble("totalAmount_b_m"));
				paramBean.setTotalAmount_l_m(res.getDouble("totalAmount_l_m"));
				paramBean.setTotalAmount_m(res.getDouble("totalAmount_m"));
				paramBean.setTotalTimes_b_m(res.getDouble("totalTimes_b_m"));
				paramBean.setTotalTimes_l_m(res.getDouble("totalTimes_l_m"));
				paramBean.setTotalTimes_m(res.getDouble("totalTimes_m"));
				paramBean.setBalance_m(res.getDouble("balance_m"));
				paramBean.setAvBalance_m(res.getDouble("avBalance_m"));
				paramBean.setMaxSingleCredit_s(res
						.getDouble("maxSingleCredit_s"));
				paramBean.setMaxSingleCredit_b_s(res
						.getDouble("maxSingleCredit_b_s"));
				paramBean.setMaxSingleCredit_l_s(res
						.getDouble("maxSingleCredit_l_s"));
				paramBean.setTotalAmount_b_s(res.getDouble("totalAmount_b_s"));
				paramBean.setTotalAmount_l_s(res.getDouble("totalAmount_l_s"));
				paramBean.setTotalAmount_s(res.getDouble("totalAmount_s"));
				paramBean.setTotalTimes_b_s(res.getDouble("totalTimes_b_s"));
				paramBean.setTotalTimes_l_s(res.getDouble("totalTimes_l_s"));
				paramBean.setTotalTimes_s(res.getDouble("totalTimes_s"));
				paramBean.setBalance_s(res.getDouble("banlance_s"));
				paramBean.setAvBalance_s(res.getDouble("avBalance_s"));

				paramBean.setAddress(res.getString("ADDRESS"));
				paramBean.setPhone(res.getString("PHONE"));

				paramBean.setBalancepercent(res.getDouble("BALANCEPERCENT"));
				paramBean.setTotalamountpercent(res
						.getDouble("TOTALAMOUNTPERCENT"));
				paramList.add(paramBean);
			}
			pre.clearBatch();
			res.close();
			pre.close();
		} catch (SQLException e) {
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e1) {
			}
			throw new DaoException(e.getMessage(), e);
		}
		return paramList;
	}

	@Override
	public void insertOrUpdateDate(String sql) throws DaoException {
		Statement pre = null;
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user,
						password);
			}
			pre = connProcess.createStatement();
			pre.executeUpdate(sql);
			connProcess.commit();
			pre.clearBatch();
		}catch (Exception e) {
			try {
				connProcess.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			logger.error("数据插入或更新异常",e);
			throw new DaoException(e.getMessage(), e);
		}finally{
			try {
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public List<BillBean> queryBillTable(String sql) throws DaoException {
		ResultSet res = null;
		Statement pre = null;
		List<BillBean> billList = new ArrayList<BillBean>();
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user,
						password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery(sql);
			while (res.next()) {
				BillBean billBean = new BillBean();
				billBean.setAutoid(res.getInt("autoid"));
				billBean.setCustomerid(res.getString("Customerid"));
				billBean.setIdcenterno(res.getString("idcenterno"));
				billBean.setIdbank(res.getString("idbank"));
				billBean.setAddress(res.getString("Address"));
				billBean.setSendmode(res.getString("Sendmode"));
				billBean.setDocdate(res.getString("Docdate"));
				billBean.setVoucherno(res.getString("Voucherno"));
				billBean.setAccno(res.getString("Accno"));
				billBean.setNum(res.getInt("Num"));
				billList.add(billBean);
			}
			pre.clearBatch();
		} catch (Exception e) {
			logger.error("数据查询异常",e);
			throw new DaoException(e.getMessage(), e);
		}finally{
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return billList;
	}


	/**
	 * 处理机构
	 */
	@Override
	public void processParamBankData() throws DaoException {
		String sql;
		try {
			
			//虚拟出一个级别为1的总行（湘江银行）
			if(queryDataCount("param_bank where nlevel = '1' and idbank ='"+FinalConstant.ROOTORG+"'")==0){
				sql = "insert into param_bank(id,idbank,idbranch,idcenter,orgsid,cname,nlevel,address,phone) " +
						"values (param_bank_autoid.nextval,'"+FinalConstant.ROOTORG+"','"+FinalConstant.ROOTORG+"','"+FinalConstant.ROOTORG+"','"+FinalConstant.ROOTORG+"','华融湘江银行',1,'华融湘江银行','')";
				insertOrUpdateDate(sql);
			};
			
			
			//处理清算中心
			//--2期改造  不更新机构名称  bank.cname=u.descnam,
			sql="merge into param_bank bank " +
					" using (select t.* from ebs_utblbrcd t where 1=1 and t.zlvlcode = '1') u " +
					" on (bank.idbank = u.brcd) " +
					" when matched then  " +
					" update set bank.idbranch='"+FinalConstant.ROOTORG+"', bank.idcenter = u.brcd,bank.orgsid=u.brcd "+
					" when not matched then " +
					" insert values (param_bank_autoid.nextval,u.brcd,'"+FinalConstant.ROOTORG+"',u.brcd,u.brcd,u.descnam,2,'','',''," +
						" '','',u.phone,u.address,u.zip)";
			insertOrUpdateDate(sql);
			
			//处理支行
			//--2期改造  不更新机构名称  bank.cname=u.descnam,
			sql="merge into param_bank bank " +
					"using (select t.* from ebs_utblbrcd t where 1=1 and t.zlvlcode = '2') u " +
					"on (bank.idbank = u.brcd) " +
					"when matched then  " +
					"update set  bank.idbranch=u.zrepbrcd, bank.idcenter=u.zrepbrcd,bank.orgsid=u.brcd  "+
					"when not matched then " +
					"insert values (param_bank_autoid.nextval,u.brcd,u.zrepbrcd,u.zrepbrcd,u.brcd,u.descnam,3,'','',''," +
						"'','',u.phone,u.address,u.zip)";
			insertOrUpdateDate(sql);
			
			logger.info("机构参数表更新成功！");
		} catch (Exception e) {
			throw new DaoException(e.getMessage(), e);
		}
	}

	
	/**
	 * 处理人员
	 */
	@Override
	public List<BillBean> queryKubUser() throws DaoException {
		ResultSet res = null;
		Statement pre = null;
		List<BillBean> billList = new ArrayList<BillBean>();
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user, password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery("select * from ebs_kub_user");
			while (res.next()) {
				BillBean billBean = new BillBean();
				billBean.setPeoplecode(res.getString("Peoplecode"));
				billBean.setPeoplename(res.getString("Peoplename"));
				billBean.setOrgno(res.getString("Orgno"));
				if (res.getString("SEX")!=null&&res.getString("SEX").equals("0")) {
					billBean.setSex("0");
				} else {
					billBean.setSex("1");
				}
				billList.add(billBean);
			}
			pre.clearBatch();
			res.close();
			pre.close();
		} catch (SQLException e) {
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e1) {
			}
			throw new DaoException(e.getMessage(), e);
		}
		return billList;
	}
	

	/**
	 * 查询数据总条目数
	 * lixiangjun
	 * @param sql
	 * @return
	 */
	public long queryDataCount(String sql){
		ResultSet res = null;
		Statement pre = null;
		long cou = 0;
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user, password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery("select count(*) cou from "+sql);
			while (res.next()) {
				cou = res.getLong("cou");
			}
			pre.clearBatch();
			res.close();
			pre.close();
		} catch (SQLException e) {
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e1) {
				logger.info("连接数据库失败，" + e);
			}
		}
		return cou;
	}

	@Override
	public List<BillBean> querySubNoc(String sql) throws DaoException {
		ResultSet res = null;
		Statement pre = null;
		List<BillBean> billList = new ArrayList<BillBean>();
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user, password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery(sql);
			while (res.next()) {
				BillBean billBean = new BillBean();
				billBean.setSubnoc(res.getString("SUBNOC"));
				billBean.setSubnoctype(res.getString("SUBNOCTYPE"));
				billList.add(billBean);
			}
			pre.clearBatch();
			res.close();
			pre.close();
		} catch (SQLException e) {
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e1) {
			}
			throw new DaoException(e.getMessage(), e);
		}
		return billList;
	}
	
	/**
	 * lixinagjun
	 * 数据库查询 obj为需要查询的字段，sql为sql语句，返回值为List<Map>  一个map存放的为一条数据
	 * @param obj
	 * @param sql
	 * @return List<Map<String, Object>> 
	 */
	@Override
	public List<Map<String,Object>> queryObjectList(String[] resultPar,String sql){
		ResultSet res = null;
		Statement pre = null;
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			if (connProcess == null || connProcess.isClosed()) {
				connProcess = DBUtil.open(jdbcUrl, driverClass, user,
						password);
			}
			pre = connProcess.createStatement();
			res = pre.executeQuery(sql);
			while (res.next()) {
				Map<String ,Object> map =  new HashMap<String, Object>();
				for(int i=0;i<resultPar.length;i++){
					Object object =res.getObject(i+1);
					if(object !=null){
						map.put(resultPar[i],object);
					}
				}
				list.add(map);
			}
			pre.clearBatch();
		} catch (Exception e) {
			logger.error("数据查询异常",e);
		}finally{
			try {
				if(res!=null){
					res.close();
				}
				if(pre!=null){
					pre.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * lixiangjun
	 * 查询 PARAM_SYSBASE 表中的参数参数值
	 * @param parma
	 * @return
	 */
	public String findSysbaseParam(String parma){
		String sysbasevalue =null;
		String sql ="select sysbasevalue from param_sysbase where sysbaseid = '"+parma+"'";
		List<Map<String,Object>>list = queryObjectList(new String[]{parma}, sql);
		if(list.size()>0){
			sysbasevalue = (String) list.get(0).get(parma);
		}
		return sysbasevalue;
	}
	
	/**
	 * 关闭连接
	 */
	public void closeConnProcess(){
		if(connProcess!=null){
			try {
				connProcess.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
