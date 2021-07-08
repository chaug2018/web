package com.yzj.wf.po.biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.wf.core.model.po.PeopleInfo;

/**
 * 查询人员岗位信息biz类
 * @author Administrator
 *
 */
public class OrganizeDetailWarpBiz {
	
	private String driverClass;
	private String jdbcUrl;
	private String user;
	private String password;
	
	/**
	 * 获取人员总数
	 * @param queryMap
	 * @param containOrg
	 * @param islike
	 * @return
	 */
	public int getPeopleListCount(Map<String, String> queryMap,String containOrg,boolean islike) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			
			StringBuffer sqlBuff = new StringBuffer();
			
			sqlBuff.append("select count(distinct p.sid) as peopleCount from po_peopleinfo p left join am_rolegrouppeople r on r.peoplesid = p.sid ");
			sqlBuff.append("left join am_rolegroupinfo f on f.sid = r.rolegroupsid left join po_organizeinfo o on o.sid=p.organizeinfo ");
			sqlBuff.append("where p.peopleState!=-1 ");
			//拼接条件
			if(queryMap!=null){
				for(Map.Entry<String, String> entry:queryMap.entrySet()){
					if(entry.getValue()!=null && entry.getValue().trim().length()>0){
						if("orgPath".equals(entry.getKey())){
							sqlBuff.append("and o."+entry.getKey()+" like '"+entry.getValue().trim()+"'");
						}else{
						
							if(islike){
								sqlBuff.append("and p."+entry.getKey()+" like '"+entry.getValue().trim()+"'");
							}else{
								sqlBuff.append("and p."+entry.getKey()+" = '"+entry.getValue().trim()+"'");
							}
							
						}
					}
				}
			}
			
			//查询带有岗位查询
			if("1".equals(containOrg)){
				sqlBuff.append(" and f.rolegroupname is not null ");
			}else{
				sqlBuff.append(" and f.rolegroupname is null ");
			}
			
			String sqlCount = sqlBuff.toString();
			System.out.println("sqlCount:"+sqlCount);
			st = conn.prepareStatement(sqlCount);
			rs = st.executeQuery();
			
			int count = 0;
			while(rs.next()){
				count = rs.getInt("peopleCount");
				//System.out.println("peopleCount="+count);
			}
			return count;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			realase(conn, st, rs);
		}
	}
	
	/**
	 * 获取用户信息，包含其岗位信息
	 * @param queryMap
	 * @param pageParam
	 * @param containOrg true 包含岗位；false 不包含岗位
	 * @param ispage true 分页查询；false 不分页查询
	 * @param islike true 模糊查询；false 不模糊查询
	 * @return
	 * @throws XDocProcException
	 */
	public List<PeopleInfo> getPeopleList(Map<String, String> queryMap,PageParam pageParam,String containOrg,boolean ispage,boolean islike) {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			String sql = makeOrganizeDetailSQL(queryMap,pageParam,containOrg,ispage,islike);
			System.out.println("getPeopleList.sql:"+sql);
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			List<PeopleInfo> list = new ArrayList<PeopleInfo>();
			
			Map<String,String> roleMap = new HashMap<String, String>(); //人员对应的岗位集合
			//查询出人员的岗位名称
			if("1".equals(containOrg)){
				//如果查询岗位的，则查询出岗位信息
				roleMap = getRoleData();
			}
			
			while(rs.next()){
				PeopleInfo p = new PeopleInfo();
				p.setSid(rs.getString("sid"));
				p.setPeopleCode(rs.getString("peoplecode"));
				p.setPeopleName(rs.getString("peoplename"));
				p.setPeopleGender(rs.getByte("peoplegender"));
				p.setOrganizeInfo(rs.getString("organizeinfo"));
				
				//设置岗位名称
				if("1".equals(containOrg) && roleMap.containsKey(rs.getString("sid"))){
					p.setRoleGroupStr(roleMap.get(rs.getString("sid")));
				}
	
				list.add(p);
			}
			return list;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			realase(conn, st, rs);
		}
	}
	
	/**
	 * 获取所有人员对应的岗位信息
	 * @return
	 */
	private Map<String, String> getRoleData() {
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			String sql = " select r.peoplesid sid, f.rolegroupname rolegroupname from am_rolegrouppeople r join am_rolegroupinfo f on f.sid = r.rolegroupsid ";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			Map<String,String> roleMap = new HashMap<String, String>(); //人员对应的岗位集合
			
			while(rs.next()){
				String sid = rs.getString("sid");
				String rolegroupname = rs.getString("rolegroupname");
				
				//如果集合中已经含有对应人员的岗位，则将后续岗位添加到集合中 分隔符逗号
				if(roleMap.containsKey(sid)){
					String newrolegroupname = roleMap.get(sid)+","+rolegroupname;
					roleMap.remove(sid);
					roleMap.put(sid, newrolegroupname);
				}else{
					//不存在 则直接将用户岗位放入集合中
					roleMap.put(sid, rolegroupname);
				}
			}
			return roleMap;
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			realase(conn, st, rs);
		}
	}

	/**
	 * 生成查询sql
	 * @param queryMap
	 * @param pageParam
	 * @param containOrg
	 * @param islike
	 * @return
	 */
	private String makeOrganizeDetailSQL(Map<String, String> queryMap,PageParam pageParam, String containOrg ,boolean ispage,boolean islike) {
		StringBuffer sqlBuff = new StringBuffer();
		if(ispage){
			sqlBuff.append("SELECT * FROM (SELECT ROWNUM AS MYROMNUM, T1.* FROM (SELECT * FROM ( ");
		}
		
		sqlBuff.append("select distinct p.sid, p.peoplecode, p.peoplename, p.peoplegender,p.organizeinfo ");
		sqlBuff.append("from po_peopleinfo p ");
		sqlBuff.append("left join am_rolegrouppeople r on r.peoplesid = p.sid ");
		sqlBuff.append("left join am_rolegroupinfo f on f.sid = r.rolegroupsid left join po_organizeinfo o on o.sid=p.organizeinfo where p.peopleState!=-1 ");
		
		//拼接条件
		if(queryMap!=null){
			for(Map.Entry<String, String> entry:queryMap.entrySet()){
				if(entry.getValue()!=null && entry.getValue().trim().length()>0){
					if("orgPath".equals(entry.getKey())){
						sqlBuff.append("and o."+entry.getKey()+" like '"+entry.getValue().trim()+"'");
					}else{
					
						if(islike){
							sqlBuff.append("and p."+entry.getKey()+" like '"+entry.getValue().trim()+"'");
						}else{
							sqlBuff.append("and p."+entry.getKey()+" = '"+entry.getValue().trim()+"'");
						}
						
					}
				}
			}
		}
		
		
		//查询带有岗位查询
		if("1".equals(containOrg)){
			sqlBuff.append(" and f.rolegroupname is not null ");
		}else{
			sqlBuff.append(" and f.rolegroupname is null ");
		}
		
		if(ispage){
			int pageSize = pageParam.getPageSize();// 每页显示结果条数
			int curPage = pageParam.getCurPage();// 当前要显示的页
			
			if(curPage<=0){
				curPage= 1;
			}
			int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始
			int topnum = Integer.valueOf(firstResult)+Integer.valueOf(pageSize);
			sqlBuff.append("  )) t1) WHERE MYROMNUM <= '"+topnum+"' AND MYROMNUM > '"+firstResult+"'");
		}
		
		return sqlBuff.toString();
	}

	private Connection getConnection() throws Exception{
		return DriverManager.getConnection(jdbcUrl,user,password);	
	}

	private void realase(Connection conn,Statement st,ResultSet rs){
		if(rs!=null){
			try{
				rs.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			rs=null;
		}
		if(st!=null){
			try{
				st.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			st=null;
		}
		if(conn!=null){
			try{
				conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			conn=null;
		}
	}
	
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
		try {
			Class.forName(driverClass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
