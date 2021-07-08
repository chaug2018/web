package com.yzj.ebs.blackwhite.biz;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;
import jxl.Sheet;
import jxl.Workbook;
import com.yzj.ebs.blackwhite.queryparam.BlackWhiteResult;
import com.yzj.ebs.blackwhite.queryparam.QueryParam;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.IBasicInfoLogAdm;
import com.yzj.ebs.common.IImportSpecile;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.ebs.database.ImportSpecile;
import com.yzj.ebs.util.FinalConstant;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-11<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 特殊账单维护及统计 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class BlackWhiteBizImpl implements IBlackWhiteBiz {

	private static SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private List<BasicInfo> queryList = new ArrayList<BasicInfo>();
	private static WFLogger logger = WFLogger
			.getLogger(BlackWhiteBizImpl.class);

	private IBasicInfoAdm basicinfoAdm;
	private IBasicInfoLogAdm basicInfoLogAdm;
	private IPublicTools tools;
	
	private List<BlackWhiteResult> resultList = new ArrayList<BlackWhiteResult>();
	private List<BlackWhiteResult> exportList = new ArrayList<BlackWhiteResult>();
	private IImportSpecile importSpecileAdm;
	// 页面提示信息
	private String msg = "";
	String errMsg = null;
	
	@Override
	public List<BasicInfo> getBasicinfoData(Map<String, String> queryMap,
			PageParam bwQueryParam, boolean isPaged) throws XDocProcException {
		// TODO Auto-generated method stub
		queryList.clear();
		if (isPaged) {
			//分页查询
			queryList = basicinfoAdm.getBasicinfoData(queryMap, bwQueryParam);
		} else {
			//导出
			queryList = basicinfoAdm.getAllBasicInfo(queryMap);
		}
		return queryList;
	}

	/**
	 * 维护账户信息表
	 */
	@Override
	public String modifyByOne(BasicInfo info, String index)
			throws XDocProcException {
		// TODO Auto-generated method stub
		XPeopleInfo people = tools.getCurrLoginPeople();
		BasicInfoLog infoLog = new BasicInfoLog();
		infoLog.setAccNo(info.getAccNo());
		infoLog.setAccName(info.getAccName());
		infoLog.setOpCode(people.getPeopleCode());
		infoLog.setIdBank(info.getIdBank());
		infoLog.setIdBranch(info.getIdCenter());
		infoLog.setIdCenter(info.getIdCenter());
		infoLog.setOpDate(sdf.format(new Date()));
		infoLog.setOpMode(OperLogModuleDefine.specialAcc);
		infoLog.setChnOpMode(OperLogModuleDefine
				.getModuleName(OperLogModuleDefine.specialAcc));
		String desc = "特殊账户维护成功!特殊账户标识:" + info.getAccCycle() + "("
				+ RefTableTools.ValRefAccCycleMap.get(info.getSpecialFlag())
				+ ")";
		infoLog.setOpDesc(desc);
		String inputTime = matter1.format(new Date()).toString(); // 录入日期
		info.setSignTime(inputTime);
		try {
			String currDate = new SimpleDateFormat("yyyyMMdd")
					.format(new Date());
			info.setSignTime(currDate);
			getBasicinfoAdm().update(info);
			getBasicInfoLogAdm().create(infoLog);
		} catch (XDocProcException e) {
			logger.error("维护特殊账户失败", e);
			return null;
		}
		return null;
	}

	/**
	 * 获取黑白名单统计数据
	 * 
	 * @throws XDocProcException
	 */
	@Override
	public List<BlackWhiteResult> getAnalyseResult(QueryParam queryParam,
			Map<String, String> queryMap, boolean isPaged,String selectCount)
			throws XDocProcException {
		// TODO Auto-generated method stub
		resultList.clear();
		exportList.clear();
		List<?> list = null;
		try{
			List<?> idBranchNameList = null;
			Map<String,String> idBranchNameMap = new HashMap<String,String>();
			//获得所有清算中心的名字
			idBranchNameList = basicinfoAdm.getAllIdBranchName();
			for (int i = 0; i < idBranchNameList.size(); i++) {
				String idBranch = "";
				String idBranchName = "";
				Object[] obj = (Object[]) idBranchNameList.get(i);
				for (int j = 0; j < obj.length; j++) {
					switch (j) {
					case 0:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranch = obj[j].toString();
						}
						break;
					case 1:
						if (obj[j] != null
								&& obj[j].toString().trim().length() > 0) {
							idBranchName = obj[j].toString();
						}
						break;
					}
				}
				idBranchNameMap.put(idBranch, idBranchName);
			}
			
			//查询数据库数据
			if(selectCount!=null && selectCount.equals("countIdBank")){
				list = basicinfoAdm.analyseBlackWhite(queryMap, queryParam,isPaged);
			}else{
				list = basicinfoAdm.analyseBlackWhiteCount(queryMap, queryParam, 
						isPaged, selectCount);
			}
			
			//按网点遍历List
			if(selectCount!=null && selectCount.equals("countIdBank")){
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						BlackWhiteResult result = new BlackWhiteResult();
						
						String idCenter = String.valueOf(obj[0]);
						String idBank = String.valueOf(obj[1]);
						String bankName = String.valueOf(obj[2]);
						Integer blackCount = Integer.valueOf(obj[3].toString());
						Integer whiteCount = Integer.valueOf(obj[4].toString());
						
						result.setIdCenter(idCenter);
						result.setIdBank(idBank);
						result.setBankName(bankName);
						result.setBlackCount(blackCount);
						result.setWhiteCount(whiteCount);
						
						resultList.add(result);
						exportList.add(result);
					}
				} 
			}
			
			//按分行遍历List
			if(selectCount!=null && selectCount.equals("countIdCenter")){
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						BlackWhiteResult result = new BlackWhiteResult();
						
						String idCenter = String.valueOf(obj[0]);
						String idBank = idCenter;
						String bankName = idBranchNameMap.get(idCenter);
						Integer blackCount = Integer.valueOf(obj[1].toString());
						Integer whiteCount = Integer.valueOf(obj[2].toString());
						
						result.setIdCenter(idCenter);
						result.setIdBank(idBank);
						result.setBankName(bankName);
						result.setBlackCount(blackCount);
						result.setWhiteCount(whiteCount);
						
						resultList.add(result);
						exportList.add(result);
					}
				} 
			}
			
			//按总行遍历List
			if(selectCount!=null && selectCount.equals("countIdBranch")){
				if (list != null && list.size() != 0) {
					for (int i = 0; i < list.size(); i++) {
						Object[] obj = (Object[]) list.get(i);
						BlackWhiteResult result = new BlackWhiteResult();
						
						String idCenter = FinalConstant.ROOTORG;
						String idBank = FinalConstant.ROOTORG;
						String bankName = "华融湘江总行";
						Integer blackCount = Integer.valueOf(obj[0].toString());
						Integer whiteCount = Integer.valueOf(obj[1].toString());
						
						result.setIdCenter(idCenter);
						result.setIdBank(idBank);
						result.setBankName(bankName);
						result.setBlackCount(blackCount);
						result.setWhiteCount(whiteCount);
						
						resultList.add(result);
						exportList.add(result);
					}
				} 
			}
		}catch (XDocProcException e) {
			errMsg = "查询数据库出现错误!";
			logger.error("特殊账户统计查询数据库错误", e);
		}
		if(isPaged){
			return resultList;
		}else{
			return exportList;
		}
	}

	/**
	 * 根据导入的excel 批量维护特殊账户
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public String completeBatchInput(File upFile) throws XDocProcException {
		 String notIdcenter="";
		//获取当前日期
		String currDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
		currDate.replace("-", "");
		 Map<String, String> accCycleMap =RefTableTools.TxtRefAccCycleMap;
		 Map<String, String> sealModeMap =RefTableTools.TxtRefSealModeMap;
		 Map<String, String> sendModeMap =RefTableTools.TxtRefSendModeMap;	
		// 判断 1.是否是跨行 2.账户是否存在
		String accCycle ="";
		String sealMode = "";
		String sendMode = "";
		String accNo = "";
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		BankParam param = null;
		try {
			param = tools.getBankParam(people.getOrgNo());
		} catch (XDocProcException e1) {
			e1.printStackTrace();
		}
		String idCenter = param.getIdCenter();   //得到登录操作员的 对账中心机构号
		Workbook book;
		try {
			// 获取文件
			book = Workbook.getWorkbook(upFile);
		} catch (Exception e) {
			// 写入日志
			msg = "未选择文件或文件上传失败，请确认文件格式为*.xls！";
			logger.error(msg + e.getMessage());
			return msg;
		}

		// 读取文件
		Sheet sheet = book.getSheet(0);
		int rows = sheet.getRows(); // 总行数
		int columns = sheet.getColumns(); // 总列数 
		if ("账号".equals(sheet.getCell(0, 0).getContents().trim())
				&& "账户维护类型".equals(sheet.getCell(1, 0).getContents().trim())
				&& "验印模式".equals(sheet.getCell(2, 0).getContents().trim())
				&& "投递方式".equals(sheet.getCell(3, 0).getContents().trim())
				) { // 检查表头是否正确
		} else {
			msg = "您导入的数据列名不正确！请检查后重新导入！";
			logger.error(msg);
			return msg;
		}	
		for (int i = 1; i < rows; i++) { // 遍历行
			StringBuffer desc = new StringBuffer();
			BasicInfo basicInfo = new BasicInfo();
			accNo = sheet.getCell(0, i).getContents().trim().toString();
			accCycle = sheet.getCell(1, i).getContents().trim().toString();
			sealMode = sheet.getCell(2, i).getContents().trim().toString();
			sendMode = sheet.getCell(3, i).getContents().trim().toString();
			ImportSpecile imp = new ImportSpecile();
			imp.setAccCycle(accCycleMap.get(accCycle));
			imp.setAccno(accNo);
			imp.setSealMode(sealModeMap.get(sealMode));
			imp.setSendMode(sendModeMap.get(sendMode));
			imp.setDocDate(currDate);
			//如果 accno为空 则不录入
			if(null == accNo || !accNo.trim().equals("")){
				//判断该账号是否存在与basicinfo
				basicInfo = basicinfoAdm.getOneByAccNo(accNo);
				if(basicInfo != null){	      //存在于basicinfo  把basicinfo中的信息做维护 
					if(idCenter.equals(basicInfo.getIdCenter()) || idCenter.equals("1000000000")){    //判断是否为本分行的账户，不是本分行的不维护
						desc.append("特殊账户批量维护成功!");
						desc.append("账号:");
						desc.append(accNo);
						
						if(!accCycle.equals("")){
							basicInfo.setAccCycle(accCycleMap.get(accCycle));
							desc.append("账户类型:");
							desc.append(accCycle);
						}
						if(!sealMode.equals("")){
							basicInfo.setSealMode(sealModeMap.get(sealMode));
							desc.append("验印模式:");
							desc.append(sealMode);
						}
						if(!sendMode.equals("")){
							basicInfo.setSendMode(sendModeMap.get(sendMode));
							if("3".equals(sendMode)){
								basicInfo.setSignFlag("0");//柜面签约网银
							}
							desc.append("发送方式:");
							desc.append(sendMode);
						}
						basicInfo.setIsSpecile("1");
						desc.append("特殊帐户标识 isspecile置为1");
						//跟新数据库
						basicinfoAdm.saveOrUpdate(basicInfo);
						//xie操作日志
						putBasicInfoLog(basicInfo,desc.toString());
						//该账号是否 已存在于 特殊账户导入表中,如果存在 则 更新 特殊账户导入表信息
						if(null != importSpecileAdm.getOneByAccno(accNo)){
							imp.setIsImport("1");
							importSpecileAdm.putInfor(imp);
						}
					}else{
						msg=accNo+"第"+String.valueOf(i)+"行不是本行的账户   ,导入失败，请重新导入！"+sdf.format(new Date());
						logger.error(msg);
						notIdcenter= msg;
						break;
					}	
				}else{
					//账号不在数据库中，存入到特殊帐户信息表中
						//并且把 isimport 字段改为"0" 未导入
					imp.setIsImport("0");
					importSpecileAdm.putInfor(imp);
					msg = accNo+"数据库中没查到该账号，放入到特殊账户导入表中"+sdf.format(new Date());
					logger.error(msg);
			
			}
			//不做判断	
			}else{
				msg = "账号未填写，放弃第"+String.valueOf(i)+"条信息"+sdf.format(new Date());
				logger.error(msg);
			}
			
		}
		return notIdcenter;
	}

		
	/**
	 * basicInfoLog日志记录	
	 */
	public void putBasicInfoLog(BasicInfo info,String desc)throws XDocProcException{
		XPeopleInfo people = tools.getCurrLoginPeople();
		BasicInfoLog infoLog = new BasicInfoLog();
		infoLog.setAccNo(info.getAccNo());
		infoLog.setAccName(info.getAccName());
		infoLog.setOpCode(people.getPeopleCode());
		infoLog.setIdBank(info.getIdBank());
		infoLog.setIdBranch(info.getIdCenter());
		infoLog.setIdCenter(info.getIdCenter());
		infoLog.setOpDate(sdf.format(new Date()));
		infoLog.setOpMode(OperLogModuleDefine.specialAcc);
		infoLog.setChnOpMode(OperLogModuleDefine
				.getModuleName(OperLogModuleDefine.specialAcc));
		infoLog.setOpDesc(desc);
		String inputTime = matter1.format(new Date()).toString(); // 录入日期
		info.setSignTime(inputTime);
		basicInfoLogAdm.saveOrUpdate(infoLog);
	}
	/**
	 * 删除信息
	 */
	public void deleteInfor(String accNo)throws XDocProcException{
		ImportSpecile iSpecile = new ImportSpecile();
		iSpecile =importSpecileAdm.getOneByAccno(accNo) ;
		//如果在特殊帐户信息表中存在此账号信息，则删除特殊帐户信息表和basicinfo表中的信息
		if(iSpecile != null){
			basicinfoAdm.deleteBasicInfoByAccNo(accNo);
			importSpecileAdm.deleteSpecile(accNo);
		}else{
			//如果在账户信息表中不存在此账号的信息，则把basicinfo中的不对账和特殊帐户的标志都该为正常账户标识
			basicinfoAdm.changeToNormal(accNo);
		}
	}
	
	public static SimpleDateFormat getMatter1() {
		return matter1;
	}

	public static void setMatter1(SimpleDateFormat matter1) {
		BlackWhiteBizImpl.matter1 = matter1;
	}

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public static void setSdf(SimpleDateFormat sdf) {
		BlackWhiteBizImpl.sdf = sdf;
	}

	public List<BasicInfo> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<BasicInfo> queryList) {
		this.queryList = queryList;
	}

	public IBasicInfoAdm getBasicinfoAdm() {
		return basicinfoAdm;
	}

	public void setBasicinfoAdm(IBasicInfoAdm basicinfoAdm) {
		this.basicinfoAdm = basicinfoAdm;
	}

	public IBasicInfoLogAdm getBasicInfoLogAdm() {
		return basicInfoLogAdm;
	}

	public void setBasicInfoLogAdm(IBasicInfoLogAdm basicInfoLogAdm) {
		this.basicInfoLogAdm = basicInfoLogAdm;
	}

	public static WFLogger getLogger() {
		return logger;
	}

	public static void setLogger(WFLogger logger) {
		BlackWhiteBizImpl.logger = logger;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public List<BlackWhiteResult> getResultList() {
		return resultList;
	}

	public void setResultList(List<BlackWhiteResult> resultList) {
		this.resultList = resultList;
	}

	public IImportSpecile getImportSpecileAdm() {
		return importSpecileAdm;
	}

	public void setImportSpecileAdm(IImportSpecile importSpecileAdm) {
		this.importSpecileAdm = importSpecileAdm;
	}

	public List<BlackWhiteResult> getExportList() {
		return exportList;
	}

	public void setExportList(List<BlackWhiteResult> exportList) {
		this.exportList = exportList;
	}
	
}
