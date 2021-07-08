/**
 * 创建于:2012-10-08<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数据库连接
 * 
 * @author lif
 * @version 1.0
 */
package dbutil;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
	private static final String URL;
	private static final String DRIVER;
	private static final String USERNAME;
	private static final String PASSWORD;

	static {
		try {
			Properties prop = new Properties();
			InputStream in = DBUtil.class.getResourceAsStream("/db.properties");
			prop.load(in);
			in.close();

			URL = prop.getProperty("url");
			DRIVER = prop.getProperty("driver");
			USERNAME = prop.getProperty("username");
			PASSWORD = prop.getProperty("password");

			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("database connection config error!!!", e);
		}
	}

	public static Connection open() throws SQLException {
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
