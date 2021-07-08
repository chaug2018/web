/**
 * 创建于:2012-10-08<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据库连接
 * 
 * @author lif
 * @version 1.0
 */
package com.yzj.ebs.edata.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	private static String URL;
	private static String DRIVER;
	private static String USERNAME;
	private static String PASSWORD;

	public static Connection open(String url,String driver,String username,String password) throws SQLException {
		try {
			URL = url;
			DRIVER = driver;
			USERNAME = username;
			PASSWORD = password;
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("database connection config error!!!", e);
		}
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	public static void close(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (Exception e) {
		}
	}

	public static void close(Statement stmt) {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
		}
	}

	public static void close(Connection con) {
		if (con == null)
			return;
		try {
			con.close();
		} catch (Exception e) {
		}
	}

	public static void close(ResultSet rs, Statement stmt) {
		close(rs, stmt, null);
	}

	public static void close(Statement stmt, Connection con) {
		close(null, stmt, con);
	}

	public static void close(ResultSet rs, Statement stmt, Connection con) {
		close(rs);
		close(stmt);
		close(con);
	}

}
