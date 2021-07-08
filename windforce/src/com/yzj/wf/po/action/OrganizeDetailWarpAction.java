/**
 * OrganizeListAction.java
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司
 * 创建:jiangzhengqiu 2012-4-19
 */
package com.yzj.wf.po.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.common.util.StringUtils;
import com.yzj.wf.core.model.po.OrgType;
import com.yzj.wf.core.model.po.OrganizeInfo;
import com.yzj.wf.core.model.po.PeopleInfo;
import com.yzj.wf.core.model.po.common.PODefine;
import com.yzj.wf.core.model.po.common.POException;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.core.service.po.IOrganizeInfoAdm;
import com.yzj.wf.core.service.po.IPeopleInfoAdm;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;
import com.yzj.wf.po.biz.OrganizeDetailWarpBiz;

/**
 * 机构详细信息
 * 
 * @author jiangzhengqiu
 * @version 1.0
 */
public class OrganizeDetailWarpAction extends ActionSupport {

	private static final long serialVersionUID = 8930170635627203801L;
	private static WFLogger logger = WFLogger.getLogger(OrganizeDetailAction.class);
	// 机构信息查询接口,具体实例用于注入
	private IOrganizeInfoAdm biz;

	private IPeopleInfoAdm peopleAdm;
	private int startRow = 0;
	private int pageSize = 30; // 每页条数暂时写死
	private int totalPage = 0;
	private int curPage = 0;
	private String showPageType = "1"; // 界面显示类型，1代表不关联子机构，2代表关联子机构
	private String peopleName; // 查询时的用户名称
	private String peopleCode; // 查询时的用户代码
	private String useLike = "0"; // 是否使用模糊查询,0代表否，1代表是
	private String isClickButton = "0"; // 是否为点击后台查询按钮触发的请求，0代表否，1代表是
	// 子机构总数
	Integer childOrgs = 0;
	// 被冻结的机构数
	Integer freeze;

	// 包含用户个数
	Integer peopleCount = 0;

	// 机构对象
	OrganizeInfo organizeInfo;

	// 机构类型
	OrgType orgType;

	// 冻结的机构个数
	Integer congOrgs = 0;

	// 所属机构的代码
	String orgNo;

	// 用户列表
	List<PeopleInfo> userList = new ArrayList<PeopleInfo>();

	// 条件ID值
	String sid;
	
	private OrganizeDetailWarpBiz organizeDetailWarpBiz;
	private String containOrg ="1"; //是否含有岗位  默认含有岗位  0为不含岗位
	private IPublicTools tools;

	public String getOrganizeDetail() throws IOException, POException {
		XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext.getRequest().getSession().getAttribute("XPEOPLEINFO");
		long start = System.currentTimeMillis();
		startRow = 0;
		childOrgs = 0;
		peopleCount = 0;
		congOrgs = 0;
		curPage = 1;
		if ("0".equals(isClickButton)) { // 如果是点击查询按钮进来的条件，不清空查询条件，否则就清空
			peopleName = null;
			peopleCode = null;
			useLike = "0";
		}
		// 从数据库查询详细信息
		OrganizeInfo info = null;
		if (StringUtils.isNullOrBlank(sid) || sid.equals(PODefine.ROOTORGANIZESID)) {
			sid = userInfo.getOrganizeSid();
			info = biz.findOrganizeInfoBySid(sid, false);
		} else {
			info = biz.findOrganizeInfoBySid(sid, false);
		}
		List<PeopleInfo> peoples = null;
		//Object obj = null;
		Object obj_ = null;
		PageParam pageParam = new PageParam();
		pageParam.setCurPage(curPage);
		pageParam.setPageSize(pageSize);
		
		if ("1".equals(showPageType)) {// 不关联子机构
			Map<String, String> equals = new HashMap<String, String>();
			Map<String, String> likes = new HashMap<String, String>();
			
			//String sql = "select count(sid) from PeopleInfo  where organizeInfo='" + sid + "' and peopleState!=-1";
			String sql1 = "select count(sid) from OrganizeInfo  where parentorg='" + sid + "' and orgState=0";
			
			if ("0".equals(useLike)) { // 不使用模糊查询
				equals.put("organizeInfo", sid);
				if (peopleName != null && !"".equals(peopleName.trim())) {
					equals.put("peopleName", peopleName.trim());
					//sql += " and peopleName='" + peopleName + "'";
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					equals.put("peopleCode", peopleCode.trim());
					//sql += " and peopleCode='" + peopleCode + "'";
				}
				
				peoples = organizeDetailWarpBiz.getPeopleList(equals, pageParam, containOrg, true, false);
				peopleCount = organizeDetailWarpBiz.getPeopleListCount(equals, containOrg, false);
			} else {
				likes.put("organizeInfo", sid);
				if (peopleName != null && !"".equals(peopleName.trim())) {
					likes.put("peopleName", "%" + peopleName.trim() + "%");
					//sql += " and peopleName like '%" + peopleName + "%'";
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					likes.put("peopleCode", "%" + peopleCode.trim() + "%");
					//sql += " and peopleCode like '%" + peopleCode + "%'";
				}
				
				peoples = organizeDetailWarpBiz.getPeopleList(likes, pageParam, containOrg, true, true);
				peopleCount = organizeDetailWarpBiz.getPeopleListCount(likes, containOrg, true);
			}

			//peoples = peopleAdm.findByConditionsAndPage(equals, likes, 0, pageSize);
			
			//obj = peopleAdm.findByHQL(sql);
			obj_ = biz.findByHQL(sql1);
		} else {// 关联子机构
			//String sql = "select count(p.sid) from PeopleInfo p,OrganizeInfo o  where p.organizeInfo=o.sid and o.orgPath like '"
			//		+ info.getOrgPath() + "%' and  p.peopleState!=-1";
			String sql1 = "select count(sid) from OrganizeInfo  where orgPath like '" + info.getOrgPath()
					+ "%' and orgState=0";
			Map<String, String> equals = new HashMap<String, String>();
			Map<String, String> likes = new HashMap<String, String>();
			
			if ("0".equals(useLike)) { // 不使用模糊查询
				equals.put("orgPath", info.getOrgPath() + "%");
				if (peopleName != null && !"".equals(peopleName.trim())) {
					equals.put("peopleName", peopleName.trim());
					//sql += " and peopleName='" + peopleName + "'";
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					equals.put("peopleCode", peopleCode.trim());
					//sql += " and peopleCode='" + peopleCode + "'";
				}
				
				peoples = organizeDetailWarpBiz.getPeopleList(equals, pageParam, containOrg, true, false);
				peopleCount = organizeDetailWarpBiz.getPeopleListCount(equals, containOrg, false);
			} else {
				likes.put("orgPath", info.getOrgPath() + "%");
				if (peopleName != null && !"".equals(peopleName.trim())) {
					likes.put("peopleName", "%" + peopleName.trim() + "%");
					///sql += " and peopleName like '%" + peopleName + "%'";
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					likes.put("peopleCode", "%" + peopleCode.trim() + "%");
					//sql += " and peopleCode like '%" + peopleCode + "%'";
				}
				
				peoples = organizeDetailWarpBiz.getPeopleList(likes, pageParam, containOrg, true, true);
				peopleCount = organizeDetailWarpBiz.getPeopleListCount(likes, containOrg, true);
			}
			
			//peoples = peopleAdm.findByConditionsAndPageWithOrg(equals, likes, startRow, pageSize);
			
			
			//obj = peopleAdm.findByHQL(sql);
			obj_ = biz.findByHQL(sql1);
		}

		//peopleCount = Integer.valueOf(((List<?>) obj).get(0).toString());
		childOrgs = Integer.valueOf(((List<?>) obj_).get(0).toString());
		totalPage = peopleCount / pageSize;
		if (peopleCount % pageSize != 0) {
			totalPage++;
		}

		organizeInfo = info;
		orgType = biz.findOrgTypeBySid(organizeInfo.getOrgType(), false);
		userList = peoples;
		long end = System.currentTimeMillis();
		logger.info("查询机构的详细信息共用时:" + (end - start) + "ms");
		isClickButton = "0"; // 查询完后将isClickButton的值，置为0，不然下次通过菜单栏进来的请求也会认为是点击查询按钮进来的，导致进来是查询条件未被清空，按条件进行了查询
		return SUCCESS;
	}

	public String getPeoplesByPage() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		startRow = (curPage - 1) * pageSize;
		try {
			PageParam pageParam = new PageParam();
			pageParam.setCurPage(curPage);
			pageParam.setPageSize(pageSize);
			
			if ("1".equals(showPageType)) { // 不级联子机构
				Map<String, String> equals = new HashMap<String, String>();
				Map<String, String> likes = new HashMap<String, String>();
				
				if ("0".equals(useLike)) {// 非模糊查询
					equals.put("organizeInfo", sid);
					if (peopleName != null && !"".equals(peopleName.trim())) {
						equals.put("peopleName", peopleName.trim());
					}
					if (peopleCode != null && !"".equals(peopleCode.trim())) {
						equals.put("peopleCode", peopleCode.trim());
					}
					userList = organizeDetailWarpBiz.getPeopleList(equals, pageParam, containOrg, true, false);
				} else {
					likes.put("organizeInfo", sid);
					if (peopleName != null && !"".equals(peopleName.trim())) {
						likes.put("peopleName", "%" + peopleName.trim() + "%");
					}
					if (peopleCode != null && !"".equals(peopleCode.trim())) {
						likes.put("peopleCode", "%" + peopleCode.trim() + "%");
					}
					userList = organizeDetailWarpBiz.getPeopleList(likes, pageParam, containOrg, true, true);
				}
				
				
			} else {
				Map<String, String> equals = new HashMap<String, String>();
				Map<String, String> likes = new HashMap<String, String>();
				
				if ("0".equals(useLike)) {// 非模糊查询
					equals.put("orgPath", organizeInfo.getOrgPath() + "%");
					if (peopleName != null && !"".equals(peopleName.trim())) {
						equals.put("peopleName", peopleName.trim());
					}
					if (peopleCode != null && !"".equals(peopleCode.trim())) {
						equals.put("peopleCode", peopleCode.trim());
					}
					userList = organizeDetailWarpBiz.getPeopleList(equals, pageParam, containOrg, true, false);
				} else {
					likes.put("orgPath", organizeInfo.getOrgPath() + "%");
					if (peopleName != null && !"".equals(peopleName.trim())) {
						likes.put("peopleName", "%" + peopleName.trim() + "%");
					}
					if (peopleCode != null && !"".equals(peopleCode.trim())) {
						likes.put("peopleCode", "%" + peopleCode.trim() + "%");
					}
					userList = organizeDetailWarpBiz.getPeopleList(likes, pageParam, containOrg, true, true);
				}
				//userList = peopleAdm.findByConditionsAndPageWithOrg(equals, likes, startRow, pageSize);
				
			}
		} catch (Exception e) {
			logger.error("根据机构id对人员进行分页查询出现错误", e);
		}

		return "peoplesTable";
	}
	
	//导出
	public void exportData(){
		try {
			List<PeopleInfo> exportList = new ArrayList<PeopleInfo>();
			Map<String, String> equals = new HashMap<String, String>();
			Map<String, String> likes = new HashMap<String, String>();
			
			if ("1".equals(showPageType)) {// 不级联子机构
				equals.put("organizeinfo", sid);
				likes.put("organizeInfo", sid);
			}else {
				equals.put("orgPath", organizeInfo.getOrgPath() + "%");
				likes.put("orgPath", organizeInfo.getOrgPath() + "%");
			}
			
			if ("0".equals(useLike)) {// 非模糊查询
				equals.put("orgPath", organizeInfo.getOrgPath() + "%");
				if (peopleName != null && !"".equals(peopleName.trim())) {
					equals.put("peopleName", peopleName.trim());
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					equals.put("peopleCode", peopleCode.trim());
				}
				exportList =  organizeDetailWarpBiz.getPeopleList(equals, null, containOrg, false, false);	//全量查询
			} else {
				likes.put("orgPath", organizeInfo.getOrgPath() + "%");
				if (peopleName != null && !"".equals(peopleName.trim())) {
					likes.put("peopleName", "%" + peopleName.trim() + "%");
				}
				if (peopleCode != null && !"".equals(peopleCode.trim())) {
					likes.put("peopleCode", "%" + peopleCode.trim() + "%");
				}
				exportList =  organizeDetailWarpBiz.getPeopleList(likes, null, containOrg, false, true);	//全量查询
			}
			
			if(exportList.size()==0||exportList==null){
				tools.ajaxResult("查询列表为空");
			}else{
				ExportEntity entity=new ExportEntity();
				entity.setFileName("人员列表");
				entity.setTitle("人员列表");
				entity.setDataList(exportList);
				Map<String,String> pro_desc=new LinkedHashMap<String,String>();
				Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
				pro_desc.put("organizeInfo", "机构号");
				pro_desc.put("peopleName", "用户姓名");
				pro_desc.put("peopleCode", "用户代码");
				pro_desc.put("roleGroupStr", "用户岗位");
				entity.setPro_desc_map(pro_desc);
				entity.setParamsMap(paramsMap);
			    new DataExporterImpl().export(entity);
		    }
		} catch (Exception e) {
			logger.error("导出列表失败：" + e.getMessage());
			tools.ajaxResult("导出列表失败：" + e.getMessage());
		}
	}

	public IOrganizeInfoAdm getBiz() {
		return biz;
	}

	public void setBiz(IOrganizeInfoAdm biz) {
		this.biz = biz;
	}

	public Integer getChildOrgs() {
		return childOrgs;
	}

	public void setChildOrgs(Integer childOrgs) {
		this.childOrgs = childOrgs;
	}

	public Integer getFreeze() {
		return freeze;
	}

	public void setFreeze(Integer freeze) {
		this.freeze = freeze;
	}

	public OrganizeInfo getOrganizeInfo() {
		return organizeInfo;
	}

	public void setOrganizeInfo(OrganizeInfo organizeInfo) {
		this.organizeInfo = organizeInfo;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Integer getPeopleCount() {
		return peopleCount;
	}

	public void setPeopleCount(Integer peopleCount) {
		this.peopleCount = peopleCount;
	}

	public Integer getCongOrgs() {
		return congOrgs;
	}

	public void setCongOrgs(Integer congOrgs) {
		this.congOrgs = congOrgs;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public List<PeopleInfo> getUserList() {
		return userList;
	}

	public void setUserList(List<PeopleInfo> userList) {
		this.userList = userList;
	}

	public IPeopleInfoAdm getPeopleAdm() {
		return peopleAdm;
	}

	public void setPeopleAdm(IPeopleInfoAdm peopleAdm) {
		this.peopleAdm = peopleAdm;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	/**
	 * @return the orgType
	 */
	public OrgType getOrgType() {
		return orgType;
	}

	public String getShowPageType() {
		return showPageType;
	}

	public void setShowPageType(String showPageType) {
		this.showPageType = showPageType;
	}

	public String getPeopleName() {
		return peopleName;
	}

	public void setPeopleName(String peopleName) {
		this.peopleName = peopleName;
	}

	public String getPeopleCode() {
		return peopleCode;
	}

	public void setPeopleCode(String peopleCode) {
		this.peopleCode = peopleCode;
	}

	public String getUseLike() {
		return useLike;
	}

	public void setUseLike(String useLike) {
		this.useLike = useLike;
	}

	public String getIsClickButton() {
		return isClickButton;
	}

	public void setIsClickButton(String isClickButton) {
		this.isClickButton = isClickButton;
	}

	/**
	 * @param orgType
	 *            the orgType to set
	 */
	public void setOrgType(OrgType orgType) {
		this.orgType = orgType;
	}
	
	public OrganizeDetailWarpBiz getOrganizeDetailWarpBiz() {
		return organizeDetailWarpBiz;
	}

	public void setOrganizeDetailWarpBiz(OrganizeDetailWarpBiz organizeDetailWarpBiz) {
		this.organizeDetailWarpBiz = organizeDetailWarpBiz;
	}
	public String getContainOrg() {
		return containOrg;
	}
	public void setContainOrg(String containOrg) {
		this.containOrg = containOrg;
	}
	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
}
