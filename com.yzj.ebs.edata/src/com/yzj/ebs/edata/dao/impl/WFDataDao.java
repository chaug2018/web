package com.yzj.ebs.edata.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import com.infotech.publiclib.Exception.DaoException;

/**
 * 加载WF数据库连接
 * @author Administrator
 *
 */
public class WFDataDao {
	public Connection connProcess = null; // 数据处理连接
	protected String driverClass;
	protected String jdbcUrl;
	protected String user;
	protected String password;
	
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
}
