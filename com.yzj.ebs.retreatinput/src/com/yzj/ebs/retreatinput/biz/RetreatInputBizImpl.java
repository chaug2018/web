package com.yzj.ebs.retreatinput.biz;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.struts2.ServletActionContext;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataLogAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.CheckMainDataLog;
import com.yzj.ebs.retreatinput.pojo.BatchParam;
import com.yzj.ebs.retreatinput.pojo.ImportData;
import com.yzj.ebs.retreatinput.pojo.SingleParam;
import com.yzj.wf.am.security.common.AMSecurityDefine;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2013-04-01<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 退信登记 业务实现
 * 
 * @author 单伟龙
 * @version 1.0.0
 */
public class RetreatInputBizImpl implements IRetreatInputBiz{
	
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//日期格式转换
	private ICheckMainDataAdm checkMainDataAdm; //账单操作实现
	private ICheckMainDataLogAdm checkMainDataLogAdm; // 日志操作
	private IPublicTools tools;//工具类
	
	private static WFLogger logger = WFLogger
			.getLogger(RetreatInputBizImpl.class);
	

	private static String URGEFLAG_NOTPROCESS = "0"; // 退信未处理
	private static String URGESTATE_INPUTED = "1"; // 退信已登记
	private String msg; // 页面提示信息
	private BatchParam result = new BatchParam();
	
	private static final SimpleDateFormat matter1 = new SimpleDateFormat("yyyyMMdd");
	private TreeMap<String, String> refUrgeTypeMap = new TreeMap<String, String>();
	
	// 上传文件相关
	private File upFile;
	private String userId; // 人员编号
	private String inputTime=""; // 登记或处理时间
	

	
	//批量登记相关
	private int successCount=0;// 登记成功数
	private int failCount=0;//  登记失败数
	private List<ImportData> failList = new ArrayList<ImportData>();
	/**
	 * 逐笔登记业务实现，并写入日志
	 * @throws XDocProcException 
	 * @throws IOException 
	 */
	public String completeSingleInput(CheckMainData info) throws XDocProcException, IOException{
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");

		XPeopleInfo people = tools.getCurrLoginPeople();
		userId = people.getPeopleCode();
		BankParam param;
		String voucherNo=info.getVoucherNo();
		try {
			CheckMainData checkMainData = new CheckMainData();
			//通过账单编号查询该条记录
			checkMainData = checkMainDataAdm.getOneByVoucherNo(voucherNo);
			
			if (isQueryAllowed(voucherNo)) {
				CheckMainDataLog log=new CheckMainDataLog();
				log.setOpMode(OperLogModuleDefine.reTreatInput);
				log.setChnOpMode(OperLogModuleDefine.getModuleName(OperLogModuleDefine.reTreatInput));
				log.setIdBank(checkMainData.getIdBank());
				log.setIdBranch(checkMainData.getIdBranch());
				log.setIdCenter(checkMainData.getIdCenter());
				log.setOpCode(people.getPeopleCode());
				log.setVoucherNo(voucherNo);
				log.setOpDate(sdf.format(new Date()));
				String desc="退信登记成功!";
				checkMainData.setUrgeNote(info.getUrgeNote());
				desc+="备注:"+info.getUrgeNote();
				checkMainData.setUrgeType(info.getUrgeType());
				desc+=",退信类型:"+info.getUrgeType()+"("+RefTableTools.ValRefUrgeTypeMap.get(info.getUrgeType())+")";
				checkMainData.setUrgePeople(userId);
				
				inputTime = matter1.format(new Date()).toString();
				checkMainData.setUrgeDate(inputTime);
				checkMainData.setUrgeFlag(URGEFLAG_NOTPROCESS); // 设置处理标志位0，未处理
				checkMainData.setUrgeState(URGESTATE_INPUTED); // 设置退信登记标志1，已登记
				desc+=",处理情况:"+URGEFLAG_NOTPROCESS+"(退信未处理),登记情况:"+URGESTATE_INPUTED+"(退信已登记)";
				if (!checkMainData.getDocState().equals("3")){
				checkMainData.setDocState("4");		//设置账单状态为退信
				desc+=",账单状态:4(退信)";
				}
				if(checkMainData.getUrgeTimes()==null){
					checkMainData.setUrgeTimes(1);
				}else{				
					checkMainData.setUrgeTimes(checkMainData.getUrgeTimes()+1);//退信次数加1
				}
				desc+=",退信次数:"+checkMainData.getUrgeTimes();
				log.setOpDesc(desc);
				checkMainDataAdm.updateCheckMainData(checkMainData);
				checkMainDataLogAdm.create(log);
				msg = "该笔退信登记成功^__^";
				logger.info(msg);
				return msg;
			}else {
				msg = "登记失败，本机构不存在此对账单！";
				logger.error(msg);
				return msg;
			}
		} catch (XDocProcException e) {
			msg = "单笔退信登记失败：保存数据失败";
			logger.error(msg);
			return msg;
		}
	}
	@Override
	public BatchParam completeBatchInput(File upFile)
			throws XDocProcException, IOException {
		// TODO Auto-generated method stub
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		HttpServletRequest request = ServletActionContext.getRequest();
		XPeopleInfo people = (XPeopleInfo) request.getSession().getAttribute(
				AMSecurityDefine.XPEOPLEINFO);
		failList = new ArrayList<ImportData>();
		successCount=0;
		failCount=0;
		msg = "";
		Workbook book;
		try {
			//获取上传的文件
			book = Workbook.getWorkbook(upFile);
		} catch (Exception e) {
			msg = "未选择文件或文件上传失败，请确认文件格式为*.xls！";
			result.setMsg(msg);
			result.setSuccessCount(successCount);
			result.setFailCount(failCount);
			result.setFailList(failList);
			logger.error(msg + e.getMessage());
			return result;
		}
		Sheet sheet = book.getSheet(0);
		int rows = sheet.getRows();// 总行数
		int columns = sheet.getColumns();// 总列数 ,退信类型、账单编号、备注3列
		if (columns != 3) {
			msg = "您导入的数据列不正确！请检查后重新导入！";
			result.setMsg(msg);
			result.setSuccessCount(successCount);
			result.setFailCount(failCount);
			result.setFailList(failList);
			return result;
		}
		String strc0 = sheet.getCell(0, 0).getContents().trim();
		String strc1 = sheet.getCell(1, 0).getContents().trim();
		String strc2 = sheet.getCell(2, 0).getContents().trim();
		if (strc0.equals("退信类型") && strc1.equals("账单编号") && strc2.equals("备注")) {
		} else {
			msg = "您导入的数据列名不正确！请检查后重新导入！";
			result.setMsg(msg);
			result.setSuccessCount(successCount);
			result.setFailCount(failCount);
			result.setFailList(failList);
			return result;
		}
		BankParam param = null;
		try {
			param = tools.getBankParam(people.getOrgNo());
		} catch (XDocProcException e1) {
			e1.printStackTrace();
		}
		String idCenter = param.getIdCenter();
		String idBranch = param.getIdBranch();
		String idBank = param.getIdBank();
		for (int i = 1; i < rows; i++) { // 遍历行
			String urgeTypeTmp = sheet.getCell(0, i).getContents().trim() // 模板中的退信描述
					.toString();
			String voucherNo = sheet.getCell(1, i).getContents().trim()
					.toString();
			String urgeNote = sheet.getCell(2, i).getContents().trim()
					.toString();
			
			if (urgeTypeTmp.trim().equals("")) {
				failCount++;
				failList.add(new ImportData(voucherNo,"退信理由为空"));
				continue;
			} else if (voucherNo.trim().equals("")) {
				failCount++;
				failList.add(new ImportData("", "对账单编号为空"));
				continue;
			}else {
					CheckMainData checkMainData = new CheckMainData();
					//TODO
					try {
						checkMainData = checkMainDataAdm.getOneByVoucherNo(voucherNo);
					} catch (XDocProcException e) {
						failCount++;
						failList.add(new ImportData(voucherNo, "查询对账单编号失败"));
						continue;
					}
					if(checkMainData==null){
						failCount++;
						failList.add(new ImportData(voucherNo, "对账单编号不存在"));
						continue;
					} 
					 if(!isQueryAllowed2(voucherNo)){
						failCount++;
						failList.add(new ImportData(voucherNo, "不能跨机构登记！ "));
						continue;
					}else{
						Set<Map.Entry<String, String>> set = RefTableTools.ValRefUrgeTypeMap.entrySet();
						boolean isFindType = false;
						for (Iterator<Map.Entry<String, String>> it = set
								.iterator(); it.hasNext();) {
							Map.Entry<String, String> entry = (Map.Entry<String, String>) it
									.next();
							if (urgeTypeTmp.equals(entry.getValue())) {
								urgeTypeTmp = entry.getKey();
								isFindType = true;
								break;
							}
						}
						if(!isFindType)
						{
							failCount++;
							failList.add(new ImportData(voucherNo, "请选择正确的退信类型！ "));
							continue;
						}
						//写入操作日志
						CheckMainDataLog log=new CheckMainDataLog();
						log.setOpMode(OperLogModuleDefine.reTreatInput);
						log.setChnOpMode(OperLogModuleDefine.getModuleName(OperLogModuleDefine.reTreatInput));
						log.setIdBank(checkMainData.getIdBank());
						log.setIdBranch(checkMainData.getIdBranch());
						log.setIdCenter(checkMainData.getIdBranch());
						log.setOpCode(people.getPeopleCode());
						log.setVoucherNo(voucherNo);
						log.setOpDate(sdf.format(new Date()));
						String desc="退信登记成功!";
						checkMainData.setUrgeType(urgeTypeTmp);
						checkMainData.setUrgeNote(urgeNote);
						desc+="备注:"+urgeNote;
					    desc+=",退信类型:"+RefTableTools.ValRefUrgeTypeMap.get(urgeTypeTmp);
						checkMainData.setUrgePeople(userId);
						inputTime = matter1.format(new Date()).toString();
						checkMainData.setUrgeDate(inputTime);
						checkMainData.setUrgeFlag(URGEFLAG_NOTPROCESS); // 设置处理标志位0，未处理
						checkMainData.setUrgeState(URGESTATE_INPUTED); // 设置退信处理标志1，已登记
						desc+=",处理情况:"+URGEFLAG_NOTPROCESS+"(退信未处理),登记情况:"+URGESTATE_INPUTED+"(退信已登记)";
						if (!checkMainData.getDocState().equals("3")){
							checkMainData.setDocState("4");		//设置账单状态为退信	
							desc+=",账单状态:4(退信)";
						}
						if(checkMainData.getUrgeTimes()==null){
							checkMainData.setUrgeTimes(1);
						}else{				
							checkMainData.setUrgeTimes(checkMainData.getUrgeTimes()+1);//退信次数加1
						}
						desc+=",退信次数:"+checkMainData.getUrgeTimes();
						try {
							//更新账单表状态
							checkMainDataAdm.updateCheckMainData(checkMainData);
							log.setOpDesc(desc);
							//账单日志表增加一条记录
							checkMainDataLogAdm.create(log);
							successCount++;
							continue;
						} catch (XDocProcException e) {
							failCount++;
							failList.add(new ImportData(voucherNo, "登记到数据库失败"));
							continue;
						}
					}
			}
		}
		logger.info("退信登记成功"+successCount+"笔，失败"+failCount+"笔");
		msg=null;
		result.setMsg(msg);
		result.setSuccessCount(successCount);
		result.setFailCount(failCount);
		result.setFailList(failList);
		return result;
	}
	/**
	 * 确认当前登录柜员是否有权限查看录入账号的明细
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public boolean isQueryAllowed(String voucherNo) throws XDocProcException {
		CheckMainData checkMainData = new CheckMainData();
		checkMainData = checkMainDataAdm.getOneByVoucherNo(voucherNo);
		if (checkMainData == null) {
			return false; // 若accno不存在则返回false
		} else {
			XPeopleInfo userInfo = tools.getCurrLoginPeople(); // 获取当前登录柜员所在机构的的机构信息
			String idBank = userInfo.getOrgNo();
			BankParam bankParam = tools.getBankParam(idBank);
			Short level = bankParam.getLevel();
			if (level == 1) {//总行
				return true;
			} else if (level == 2) {//分行
				if (checkMainData.getIdCenter().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 3) {//支行
				if (checkMainData.getIdBranch().equals(idBank)||checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 4) {//网点
				if (checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * 确认当前登录柜员是否有权限查看录入账号的明细
	 * 批量登记
	 * @return
	 * @throws XDocProcException
	 */
	public boolean isQueryAllowed2(String voucherNo) throws XDocProcException {
//		String accNoTmp = accnoDetailQueryParam.getAccNo();
		CheckMainData checkMainData = new CheckMainData();
		checkMainData = checkMainDataAdm.getOneByVoucherNo(voucherNo);
//		BasicInfo basicInfo = basicInfoAdm.getOneByAccNo(accNoTmp);
		if (checkMainData == null) {
			return false; // 若accno不存在则返回false
		} else {
			XPeopleInfo userInfo = (XPeopleInfo) ServletActionContext
					.getRequest().getSession()
					.getAttribute(AMSecurityDefine.XPEOPLEINFO); // 获取当前登录柜员所在机构的的机构信息
			String idBank = userInfo.getOrgNo();
			BankParam bankParam = tools.getBankParam(idBank);
			Short level = bankParam.getLevel();
			if (level == 1) {//总行
				return true;
			} else if (level == 2) {//分行
				if (checkMainData.getIdCenter().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 3) {//支行
				if (checkMainData.getIdBranch().equals(idBank)||checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else if (level == 4) {//网点
				if (checkMainData.getIdBank().equals(idBank)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	public static SimpleDateFormat getSdf() {
		return sdf;
	}
	public static void setSdf(SimpleDateFormat sdf) {
		RetreatInputBizImpl.sdf = sdf;
	}
	public ICheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public ICheckMainDataLogAdm getCheckMainDataLogAdm() {
		return checkMainDataLogAdm;
	}
	public void setCheckMainDataLogAdm(ICheckMainDataLogAdm checkMainDataLogAdm) {
		this.checkMainDataLogAdm = checkMainDataLogAdm;
	}
	public IPublicTools getTools() {
		return tools;
	}
	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}
	public static WFLogger getLogger() {
		return logger;
	}
	public static void setLogger(WFLogger logger) {
		RetreatInputBizImpl.logger = logger;
	}
	
	
}
