package com.yzj.ebs.exchangebook.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.SimpleOrg;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.ExChangeBook;
import com.yzj.ebs.exchangebook.biz.IExChangeBookBiz;
import com.yzj.ebs.util.DataExporter;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2012-11-22<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账单交接登记薄
 * 
 * @author qinjingfeng
 * @version 1.0.0
 */
public class ExChangeBookAction extends ActionSupport {

	private static final long serialVersionUID = -1151891819207985293L;
	private QueryParam queryParam; // 查询条件
	private List<ExChangeBook> resultList = new ArrayList<ExChangeBook>(); // 统计结果类集合，为了前台显示方便
	private String errMsg = "";
	private IPublicTools tools;
	private String orgTree;
	private static WFLogger logger = WFLogger.getLogger(ExChangeBook.class);
	private IExChangeBookBiz biz;

	// 对帐渠道
	private TreeMap<String, String> valRefCheckflagMap = new TreeMap<String, String>();

	// 查询下拉框的值
	private String idCenter;
	private String idBranch;
	private String idBank;
	// 登记类型
	private String bookType;
	// 初始查询信息
	String queryString;

	/**
	 * 初始化界面
	 * 
	 */
	public String init() {
		resultList.clear();
		errMsg = null;
		queryParam = new QueryParam();
		SimpleOrg org = null;
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		try {
			org = tools.getCurOrgTree(people.getOrgNo());
			List<SimpleOrg> list = new ArrayList<SimpleOrg>();
			list.add(org);
			List<SimpleOrg> rList = new ArrayList<SimpleOrg>();
			new ExChangeBookAction().getChildListByOrg(list, rList);
			StringBuffer sb = new StringBuffer("");
			for (SimpleOrg o : rList) {
				sb.append(o.getOrgId()).append(",");
			}
			if (StringUtils.isNotEmpty(sb.toString())) {
				queryString = sb.substring(0, sb.length() - 1);
			}
		} catch (XDocProcException e) {
			errMsg = "获取当前机构信息列表出现错误";
			logger.error("获取当前机构信息列表出现错误", e);
		}
		JSONObject json = JSONObject.fromObject(org);
		orgTree = json.toString();
		valRefCheckflagMap = RefTableTools.ValRefSendModeMap;
		return "initSuccess";
	}

	/**
	 * 查询数据
	 * 
	 */
	public String queryData() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		idBank = queryParam.getIdBank();
		Map<String, String> queryMap = createQueryMap();
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			XPeopleInfo people = (XPeopleInfo) request.getSession()
					.getAttribute(AMSecurityDefine.XPEOPLEINFO);
			BankParam param = tools.getBankParam(people.getOrgNo());

			resultList = biz.getExChangeBook(queryMap, queryParam,
					param);

		} catch (XDocProcException e) {
			errMsg = "查询数据库出现错误!";
			logger.error("对账率统计查询数据库错误", e);
			response.getWriter().write(errMsg);
			return "initSuccess";
		}
		return "initSuccess";
	}

	/**
	 * 导出统计结果
	 * 
	 */
	public String exportData() throws IOException {
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession()
				.getAttribute(AMSecurityDefine.XPEOPLEINFO);
		BankParam param = new BankParam();
		try {
			param = tools.getBankParam(people.getOrgNo());
		} catch (XDocProcException e1) {
			e1.printStackTrace();
		}
		idBank = queryParam.getIdBank();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		Map<String, String> queryMap = createQueryMap();
		List<ExChangeBook> exportList = biz.getAllExChangeBook(
				queryMap, param);
		if (exportList.size() == 0 || exportList == null) {
			response.getWriter().write("验印情况统计列表为空");
			return "initSuccess";
		} else {
			// 替换登记类型
			for (ExChangeBook ex : exportList) {
				if ("0".equals(ex.getOpType())) {
					ex.setOpType("回收");
				} else if ("1".equals(ex.getOpType())) {
					ex.setOpType("发送");
				}
			}
			String tableName = "账单交接登记薄";
			ExportEntity entity=new ExportEntity();
			entity.setFileName(tableName);
			entity.setTitle(tableName);
			entity.setDataList(exportList);
			Map<String,String> pro_desc=new LinkedHashMap<String,String>();
			Map<String,Map<String,String>> paramsMap=new HashMap<String,Map<String,String>>();
			pro_desc.put("idBank", "机构号");
			pro_desc.put("docDate", "对账日期");
			pro_desc.put("sendmode", "对账渠道");
			pro_desc.put("voucherNo", "账单编号");
			pro_desc.put("accName", "单位名称");
			pro_desc.put("sendDate", "发送日期");
			pro_desc.put("backDate", "回收日期");
			pro_desc.put("desc", "备注");
			pro_desc.put("opcode", "登记人员");
			pro_desc.put("letterSum", "账单份数");
			pro_desc.put("opType", "登记类型");
			paramsMap.put("sendmode", valRefCheckflagMap);
			
			paramsMap.put("sendmode", valRefCheckflagMap);
			entity.setPro_desc_map(pro_desc);
			entity.setParamsMap(paramsMap);
			try {
				new DataExporterImpl().export(entity);
				return null;
			} catch (Throwable e) {
				logger.error("导出列表失败：" + e.getMessage());
				response.getWriter().write("导出列表失败：" + e.getMessage());
			}
		}
		return "initSuccess";
	}

	/**
	 * 生成查询Map
	 * 
	 * @return
	 */
	public Map<String, String> createQueryMap() {
		Map<String, String> queryMap = new TreeMap<String, String>();
		String idBank = queryParam.getIdBank();
		String idBank1 = queryParam.getIdBank1();
		if (idBank != null && idBank.length() > 0) {
			queryMap.put("idBank", idBank);
		}
		if (queryParam.getIdBank1() != null
				&& queryParam.getIdBank1().length() > 0) {
			queryMap.put("idBank", idBank1);
		}
		if (idBranch != null && idBranch.length() > 0) {
			queryMap.put("idBranch", idBranch);
		}
		if (idCenter != null && idCenter.length() > 0) {
			queryMap.put("idCenter", idCenter);
		}

		String docDateTmp = queryParam.getDocDate();
		queryMap.put("docDate", docDateTmp);
		return queryMap;
	}

	/***
	 * 登记
	 * 
	 * @return
	 */
	public String bookIn() {
		String errorMsg = "";
		ExChangeBook book = new ExChangeBook();
		try {
			BeanUtils.copyProperties(book, queryParam);
			if (StringUtils.isEmpty(book.getDocDate())) {
				errorMsg = "对账日期不能为空！";
			} else if (StringUtils.isEmpty(book.getVoucherNo())) {
				errorMsg = "对账单号不能为空！";
			} else if (StringUtils.isEmpty(book.getSendmode())) {
				errorMsg = "对帐渠道不能为空！";
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		}
		// 设置回收柜员
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		if (people != null) {
			book.setOpcode(people.getPeopleCode());
		} else {
			errorMsg = "未取到登陆用户对象!";
		}
		// 判断交接登记不能跨机构登记
		try {
			if (biz.isQueryAllowed(queryParam)) {
				if (StringUtils.isEmpty(errorMsg)) {
					try {
					
						

						ExChangeBook newBook = biz.create(book);
						if (newBook != null) {
							errorMsg = "success";
						} else {
							errorMsg = "添加失败！";
						}
					} catch (XDocProcException e) {
						e.printStackTrace();
						errorMsg = "网络出现异常！";
					}
				}
				try {
					outputWrite(errorMsg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					outputWrite("账单编号输入有误！");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return null;
	}

	/***
	 * 输出方法
	 * 
	 * @param outString
	 *            需要转换成json的串
	 */
	public void outputWrite(String outString) throws IOException {
		OutputStream out = null;
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			out = response.getOutputStream();
			response.setContentType("text/html; charset=UTF-8");
			out = response.getOutputStream();

			byte[] jsonBytes = outString.getBytes("UTF-8");
			out.write(jsonBytes);
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public String getOrgTree() {
		return orgTree;
	}

	public void setOrgTree(String orgTree) {
		this.orgTree = orgTree;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}


	public List<ExChangeBook> getResultList() {
		return resultList;
	}

	public void setResultList(List<ExChangeBook> resultList) {
		this.resultList = resultList;
	}

	public QueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(QueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}

	public TreeMap<String, String> getValRefCheckflagMap() {
		return valRefCheckflagMap;
	}

	public void setValRefCheckflagMap(TreeMap<String, String> valRefCheckflagMap) {
		this.valRefCheckflagMap = valRefCheckflagMap;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	/***
	 * 根据一个机构 递归遍历出该机构和子机构列表
	 */
	public void getChildListByOrg(List<SimpleOrg> list, List<SimpleOrg> rList) {
		if (list != null && list.size() > 0) {
			for (SimpleOrg org : list) {
				boolean isContain = false;
				for (SimpleOrg result : rList) {
					if (result.getOrgId().equals(org.getOrgId())) {
						isContain = true;
					}
				}
				if (!isContain) {
					rList.add(org);
				}
				isContain = false;
				List<SimpleOrg> children = org.getChildren();
				if (children != null && children.size() > 0) {
					getChildListByOrg(children, rList);
				}
			}
		}
	}
	public IExChangeBookBiz getBiz() {
		return biz;
	}

	public void setBiz(IExChangeBookBiz biz) {
		this.biz = biz;
	}

	
}
