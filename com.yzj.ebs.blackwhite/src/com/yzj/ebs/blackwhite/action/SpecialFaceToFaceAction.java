package com.yzj.ebs.blackwhite.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yzj.ebs.blackwhite.biz.ISpecialFaceToFaceBiz;
import com.yzj.ebs.blackwhite.queryparam.SpecialQueryParam;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.SpecialFaceToFace;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.export.common.ExportEntity;
import com.yzj.wf.export.service.DataExporterImpl;

/**
 * 
 * 创建于:2015-12-31<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 
 * 特殊面对面账户维护<br>
 * 操作员上传账号和日期，下月初出账单时如果日期相符，这些账号就作为特殊面对面，
 * 修改 set ebs_accnomaindata faceflag=1,sendmode=4,acccycle=1
 * 
 * @author chenzg
 * @version 1.0.0
 */
public class SpecialFaceToFaceAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private List<SpecialFaceToFace> queryList = new ArrayList<SpecialFaceToFace>();
	private SpecialQueryParam queryParam;
	private Map<String, String> queryMap;
	
	private static WFLogger logger = WFLogger.getLogger(SpecialFaceToFaceAction.class);
	private String errMsg=null;
	
	private IPublicTools tools;
	private ISpecialFaceToFaceBiz specialFaceToFaceBizImpl;

	//批量上传文件的路径
	private File upFile;
	

	public String init() {
		queryParam = new SpecialQueryParam();
		queryList.clear();
		errMsg=null;
		
		return "initSuccess";
	}

	/**
	 * 查询账户信息
	 * @return
	 * @throws IOException
	 */
	public String getQueryData(){
		errMsg=null;
		queryList.clear();
		queryMap = createQueryMap();
		try {
			queryList = specialFaceToFaceBizImpl.getDataList(queryMap,queryParam,true);
			
		} catch (Exception e) {
			logger.error("特殊账户查询失败", e);
			errMsg="特殊账户查询失败";
		}
		return "initSuccess";
	}

	public Map<String,String> createQueryMap(){
		queryMap = new TreeMap<String,String>();
		String accno = queryParam.getAccno();
		String docDate = queryParam.getDocdate();
		
		if (accno != null && accno.trim().length() > 0) {
			queryMap.put("accno", accno.trim());
		}
		if (docDate != null && docDate.trim().length() > 0) {
			queryMap.put("docDate", docDate.replace("-", ""));
		}
		
		return queryMap;
	}

	/**
	 * 导出数据
	 * @return
	 * @throws IOException 
	 */
	public void exportData(){
		try {
			List<SpecialFaceToFace> exportList = new ArrayList<SpecialFaceToFace>();
			queryMap = createQueryMap();
			exportList =  specialFaceToFaceBizImpl.getDataList(queryMap,queryParam,false);	//全量查询
			if(exportList.size()==0||exportList==null){
				tools.ajaxResult("查询列表为空");
			}else{
				ExportEntity entity=new ExportEntity();
				entity.setFileName("特殊面对面账户列表");
				entity.setTitle("特殊面对面账户列表");
				entity.setDataList(exportList);
				Map<String,String> pro_desc=new LinkedHashMap<String,String>();
				pro_desc.put("accno", "账号");
				pro_desc.put("docDate", "对账日期");
				
				entity.setPro_desc_map(pro_desc);
			    new DataExporterImpl().export(entity);
		    }
		} catch (Exception e) {
			logger.error("导出列表失败：" + e.getMessage());
			tools.ajaxResult("导出列表失败：" + e.getMessage());
		}
	}


	/**
	 * 读取excel文件,插入到数据库
	 */
	public void excelBatchInput(){
		String flag = "";
		try {
			flag = specialFaceToFaceBizImpl.completeBatchInput(upFile);
		} catch (XDocProcException e) {
			// 读取 excel 失败
			flag = "抽查账户登记模块上传失败";
			logger.error(flag,e);
		}
		if(flag.equals("")){
			tools.ajaxResult("导入成功！");
		}else{
			tools.ajaxResult(flag);
		}
	}
	
	/**
	 * 下载批量导入模板
	 * 
	 * @return
	 * @throws IOException
	 */
	public String downloadTemplete() throws IOException {
		HttpServletResponse response = ServletActionContext.getResponse();
		String path;
		try {
			path = this.getClass().getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e1) {
			path="template";
		}
		if(path.contains("classes")){
			path = path.replace("classes", "template");
		}
		String fileName = "抽查账户登记模板.xls";
		String filenamedownload = path + fileName;
		// 设置输出的格式
		fileName = URLEncoder.encode(fileName, "UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.reset();
		response.setContentType("application/x-download");
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ fileName);
		// 循环取出流中的数据
		OutputStream output = null;
		FileInputStream fis = null;
		try {
			output = response.getOutputStream();
			fis = new FileInputStream(filenamedownload);
			byte[] b = new byte[1024];
			int i = 0;
			while ((i = fis.read(b)) > 0) {
				output.write(b, 0, i);
			}
			output.flush();
		} catch (Exception e) {
			logger.error("抽查账户登记模板 下载失败",e);
			return null;
		} finally {
			if (fis != null) {
				fis.close();
				fis = null;
			}
			if (output != null) {
				output.close();
				output = null;
			}
		}
		return null;
	}

	/**
	 * 删除操作  
	 * @return
	 */
	public void deleteInfo() throws IOException{
	
		HttpServletRequest request = ServletActionContext.getRequest();
		String deleteAccno = request.getParameter("accno");
		String deleteDocdate = request.getParameter("docdate");
		try {
			specialFaceToFaceBizImpl.deleteInfo(deleteAccno,deleteDocdate);
		}catch (XDocProcException e) {
			logger.error("特殊帐户信息删除失败！",e);
			tools.ajaxResult("false");
		}
		
		tools.ajaxResult("deleteSuccess");
	}
	
	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public File getUpFile() {
		return upFile;
	}

	public void setUpFile(File upFile) {
		this.upFile = upFile;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public ISpecialFaceToFaceBiz getSpecialFaceToFaceBizImpl() {
		return specialFaceToFaceBizImpl;
	}

	public void setSpecialFaceToFaceBizImpl(
			ISpecialFaceToFaceBiz specialFaceToFaceBizImpl) {
		this.specialFaceToFaceBizImpl = specialFaceToFaceBizImpl;
	}

	public List<SpecialFaceToFace> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<SpecialFaceToFace> queryList) {
		this.queryList = queryList;
	}

	public SpecialQueryParam getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(SpecialQueryParam queryParam) {
		this.queryParam = queryParam;
	}

	public Map<String, String> getQueryMap() {
		return queryMap;
	}

	public void setQueryMap(Map<String, String> queryMap) {
		this.queryMap = queryMap;
	}
	
	
}
