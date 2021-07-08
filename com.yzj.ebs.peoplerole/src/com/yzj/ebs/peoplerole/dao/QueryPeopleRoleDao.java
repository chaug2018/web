package com.yzj.ebs.peoplerole.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.impl.BaseService;
import com.yzj.ebs.peoplerole.param.PeopleRole;

public class QueryPeopleRoleDao extends BaseService<PeopleRole> implements
		IQueryPeopleRoleDao {
	
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	
	@Override
	public List<PeopleRole> getPeopleRoleList(Map<String, String> queryMap,PageParam pageParam,boolean isPage) throws XDocProcException {
		
		List<PeopleRole> list = new ArrayList<PeopleRole>();
		String sql = "select p.peoplename, p.peoplecode, r.rolegroupname,p.organizeinfo  "
				+ "from PO_PEOPLEINFO p join AM_ROLEGROUPPEOPLE rp on (p.sid = rp.peoplesid) join AM_ROLEGROUPINFO r on (rp.rolegroupsid = r.sid) ";
		String whereSql=" where p.peoplestate<>-1 ";
		if(queryMap!=null){
			for(Map.Entry<String, String> entry:queryMap.entrySet()){
				if(entry.getValue()!=null && entry.getValue().trim().length()>0){
					//organizeinfo=8851
					if("organizeinfo".equals(entry.getKey())){
						whereSql += "and " +"p." +entry.getKey()+" in (select o.orgno from po_organizeinfo o where o.orgno ='"+entry.getValue().trim()+"' or o.parentorg='"+entry.getValue().trim()+"')";
					}else{
						whereSql += "and " +"p." +entry.getKey()+"=" +"'"+entry.getValue().trim()+"'";
					}
				}
			}
		}
		String orderSql = " order by p.organizeinfo desc";
		sql += whereSql + orderSql;
		try {
			//加载MySql的驱动类
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}

		//连接MySql数据库，用户名和密码都是root
		try {
			Connection con = DriverManager.getConnection(jdbcUrl, user,
					password);
			PreparedStatement pstmt = con.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			ResultSet rs = pstmt.executeQuery();
			
			if(isPage){
				//结果集指针指向最后一行数据   
				rs.last();
				int total = rs.getRow();
				//总页数
				int totalPage = (int) ((total - 1) / pageSize + 1);
				if (curPage > totalPage) {
					curPage = totalPage;
				}
				//将游标移动到第一条记录
				rs.first();
				//游标移动到要输出的第一条记录
				rs.relative(firstResult-1);
				int i = firstResult;
				while(i < (firstResult + pageSize) && i < total){ 
					rs.next(); 
					PeopleRole pr = new PeopleRole();

					pr.setPeopleName(rs.getString(1));
					pr.setPeopleCode(rs.getString(2));
					pr.setRoleGroupName(rs.getString(3));
					pr.setOrgid(rs.getString(4));
					list.add(pr); 
					i++; 
				} 
				pageParam.setFirstResult(firstResult);
				pageParam.setCurPage(curPage);
				pageParam.setTotal(total);
				pageParam.setTotalPage(totalPage);
				pageParam.setLastResult(firstResult + list.size());// 当前页显示的最后一条记录
			}else{
				//全量查询
				while(rs.next()){ 
					PeopleRole pr = new PeopleRole();
					pr.setPeopleName(rs.getString(1));
					pr.setPeopleCode(rs.getString(2));
					pr.setRoleGroupName(rs.getString(3));
					pr.setOrgid(rs.getString(4));
					list.add(pr); 
				} 
			}
			
			// 关闭记录集
			if (rs != null) { 
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// 关闭连接对象
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}

		return list;
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
