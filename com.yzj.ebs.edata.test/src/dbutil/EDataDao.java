/**
 *创建于:2012-9-29<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 操作数据库，jdbc
 * @author Lif
 * @version 1.0.0
 */
package dbutil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EDataDao{
	public static Connection conn=null;
	static {
		try {
			conn =DBUtil.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 批量插入数据
	 * @param tableName
	 * @param dataValues
	 * @return
	 * @throws DaoException
	 */
	@SuppressWarnings("rawtypes")
	public int ExecuteSql(String tableName,List dataValues,String docdate) throws Exception {
		//Connection conn =null;
		int result = 0;
		try {
			conn = DBUtil.open(); //这个影响到速度
			Statement pre=conn.createStatement();
			//测试极限数据直接直插  事物去掉  10W条
			for (int i = 0; i < dataValues.size(); i++) {
				if("EBS_accnodetaildata".equals(tableName)){
					System.out.println("insert into "+tableName+"_"+docdate.substring(4, 6)+"(autoid,WORKDATE,TRACENO,ACCNO,ACCSON,DCFLAG,TRACEBAL,CREDIT) values(ACCNODETAILDATA_"+docdate.substring(4, 6)+"_AUTOID.nextval,"+dataValues.get(i)+")");
					pre.addBatch("insert into "+tableName+"_"+docdate.substring(4, 6)+"(autoid,DOCDATE,WORKDATE,TRACENO,ACCNO,ACCSON,DCFLAG,TRACEBAL,CREDIT) values(ACCNODETAILDATA_"+"06"+"_AUTOID.nextval,'"+docdate+"',"+dataValues.get(i)+")");
				}else{
					pre.addBatch("insert into "+tableName+" values("+dataValues.get(i)+")");
				}
			}
			pre.executeBatch();
			conn.commit(); 
			pre.clearBatch();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new Exception(e.getMessage(),e);
			}
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				conn.close();
				DBUtil.close(conn);
			} catch (SQLException e) {
				throw new Exception(e.getMessage(),e);
			}
		}
		return result;
	}
	
	/**
	 * 删除表数据
	 * @param tableName
	 * @return
	 * @throws DaoException
	 */
	public int delTable(String tableName) throws Exception {
		int result = 0;
		Connection conn = null;
		try {
			conn = DBUtil.open();
			Statement pre=conn.createStatement();
			result=pre.executeUpdate("delete "+tableName);
			conn.commit();
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage(),e);
			}
		}
		return result;
	}	
	
	/**
	 * 查找表总数
	 * @param tableName
	 * @return
	 * @throws DaoException
	 */
	public long queryTableCount(String tableName)throws Exception {
		ResultSet res = null;
		Connection conn = null;
		long cou=0;
		try {
			conn = DBUtil.open();
			Statement pre=conn.createStatement();
			res=pre.executeQuery("select count(*) cou from "+tableName);
			while(res.next()){
				cou=res.getLong("cou");
			}
			conn.commit();
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage(),e);
			}
		}
		return cou;
	}
	
	/**
	 * 根据docdate查找临时主表数据
	 * @param docdate
	 * @return
	 * @throws DaoException
	 */
	public long queryTableCountByDocdate(String docdate)throws Exception {
		ResultSet res = null;
		Connection conn = null;
		long cou=0;
		try {
			conn = DBUtil.open();
			Statement pre=conn.createStatement();
			res=pre.executeQuery("select count(*) cou from EBS_MAINDATA where docdate='"+docdate+"'");
			while(res.next()){
				cou=res.getLong("cou");
			}
			conn.commit();
		} catch (Exception e) {
			throw new Exception(e.getMessage(),e);
		}finally{
			try {
				conn.close();
			} catch (SQLException e) {
				throw new Exception(e.getMessage(),e);
			}
		}
		return cou;
	}

}
