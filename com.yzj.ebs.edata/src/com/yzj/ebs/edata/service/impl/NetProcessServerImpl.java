package com.yzj.ebs.edata.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.back.notmatch.biz.INotMatchBiz;
import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.BasicInfoLog;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.NotMatchTable;
import com.yzj.ebs.edata.bean.BatchPrintBean;
import com.yzj.ebs.edata.dao.IEdataDao;
import com.yzj.ebs.edata.service.NetProcessServer;
import com.yzj.ebs.impl.CheckMainDataAdm;
import com.yzj.ebs.util.FTPUtils;
import com.yzj.ebs.util.UtilBase;
import com.yzj.ebs.util.WebBankUtil;
/**
 * 业务层 网银处理实现类
 * @author Administrator
 *
 */
public class NetProcessServerImpl implements NetProcessServer {
	private IPublicTools tools;
	private IEdataDao EdataDao;
	
	//对账单查询服务类（需要注入）
	private CheckMainDataAdm checkMainDataAdm;
	private IAccnoMainDataAdm accnoMainDataAdm;
	private INotMatchBiz biz;
	private WebBankUtil webBankUtil;

	/**
	 * 从网银FTP服务器下载，并处理每天网银传来的对账单结果和调节表
	 * @return
	 */
	@Override
	public int exchangeResultFormNet() {
		//存在日志记录则更新，没有则插入
		String  dataDate = getbeforeDate();		
		String sql=" ebs_basicinfolog where opdate='"+dataDate+"' and opmode='"+OperLogModuleDefine.DATARESULT+"'";
		Long couFlag=0l;
		try {
			couFlag = EdataDao.queryTableCount(sql);
		} catch (DaoException e1) {
			e1.printStackTrace();
		}
		if(couFlag==0){
			tools.getDataLogUtils().info("下载网银对账结果失败!",String.valueOf(OperLogModuleDefine.DATARESULT),dataDate);
		}
		//1.1.从日志表中查找当天执行结果情况
		sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opdesc='"+OperLogModuleDefine.ACCEPTSUCCESS+"'";
		try {
			couFlag=EdataDao.queryTableCount(sql);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		//1.2 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行,并且FTP服务器当天文件夹中有OK文件了，则开始执行	
		try{
			if(couFlag==0 && netSwitch){
				//2.2.1 下载文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				Map<String,String> paramMap = creatFTPMap("3",filePath,dataDate);
				//FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
				//boolean isOk = ftpUtils.down(paramMap);
				boolean isOk = this.createInFile(paramMap);
				//有OK文件再执行
				if(isOk){
					//2.2.2 解析文件
					List<Map<String, String>> resultList = analysisResult(filePath+File.separator+dataDate+"_EBILL_RESULT.txt",dataDate); //对对账结果解析
					List<Map<String, String>> detailList = analysisDetail(filePath+File.separator+dataDate+"_EBILL_DETAIL.txt",dataDate); //对调节单解析			
					// 2.2.3 对结果处理
					webBankUtil.proVoucherResulte(resultList, detailList);		
					//2.2.4 记录日志
					tools.getDataLogUtils().infoUpdate("下载网银对账结果失败!",OperLogModuleDefine.ACCEPTSUCCESS,null,String.valueOf(OperLogModuleDefine.DATARESULT),dataDate);
				}else{
					//记录日志 执行失败
					tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.ACCEPTSUCCESS,"下载网银对账结果失败!",null,String.valueOf(OperLogModuleDefine.DATARESULT),dataDate);
					return 0;
				}
			}
		}catch(Exception e){
			//记录日志 执行失败
			tools.getDataLogUtils().infoUpdate(OperLogModuleDefine.ACCEPTSUCCESS,"下载网银对账结果失败!",null,String.valueOf(OperLogModuleDefine.DATARESULT),dataDate);
			return 0;
		}
		return 1;
	}
	


	/**
	 * 每月月底，生成完对账单，抓取网银对账单，生成文件上传到网银FTP服务器
	 * @return
	 */
	@Override
	public int fetchEbillFromEbill() {
		
		String  dataDate = getLastdateOFmonth();
		
		//1.1 从日志表中查找当天执行结果情况
		String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opmode='"+OperLogModuleDefine.DATAPAPER+"'";
		Long couFlag=0l;
		try {
			couFlag=EdataDao.queryTableCount(sql);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		//1.2 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行，则开始执行
		try{
			if(couFlag==0 && netSwitch){
				//2.2.1 抓取发送方式为网银的账户对账单，生成文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				String  filecachePath = creatCheckMainDataFile(dataDate,filePath);
				//2.2.2 上传文件
				Map<String,String> paramMap = creatFTPMap("1",filecachePath,dataDate);
//				FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
//				ftpUtils.upload(paramMap);
				createOutFile(paramMap);
				//2.2.3 记录日志
				tools.getDataLogUtils().error("上传对账单成功!",String.valueOf(OperLogModuleDefine.DATAPAPER),dataDate);				   
			}
		}catch(Exception e){
			//记录日志 执行失败
			tools.getDataLogUtils().error("上传对账单失败!",String.valueOf(OperLogModuleDefine.DATAPAPER),dataDate);
			return 0;
		}
		return 1;
	}
	
	/**
	 * 生成上传的参数
	 * 	1：上传对账单
	 *  2：上传每天结果
	 * @param str
	 * @return
	 */
	private Map<String, String> creatFTPMap(String str,String fileLocalPath,String dataDate) {
		Map<String, String> paramMap  = new HashMap<String, String>();
		
		if("1".equals(str)){
			//对账单
			String FTPIP = EdataDao.findSysbaseParam("FTPIP"); //ftp地址
			String FTPPORT = EdataDao.findSysbaseParam("FTPPORT"); //ftp 端口号
			String FTPUSER = EdataDao.findSysbaseParam("FTPUSER"); //ftp 用户名
			String FTPPASSWORD = EdataDao.findSysbaseParam("FTPPASSWORD"); //tfp 密码
			
			String remoteFileName = "EBILL.txt"; //上传后的文件名
			
			//生成对账单上传路径  格式： /home/../2013/12
			String remotePath = EdataDao.findSysbaseParam("EBILLDATABILLSPATH"); //对账单结果存放路径（FTP）
			remotePath=remotePath+"/"+dataDate.substring(0,4)+"/"+dataDate.substring(4,6); //待上传的文件夹 EBILL/2013/12
			
			
			String[] entrySplits = fileLocalPath.split("\\|@\\|");
			
			paramMap.put("FTPIP", FTPIP);
			paramMap.put("FTPPORT", FTPPORT);
			paramMap.put("FTPUSER", FTPUSER);
			paramMap.put("FTPPASSWORD", FTPPASSWORD);
			paramMap.put("remotePath", remotePath);
			paramMap.put("remoteFileName", remoteFileName);
			paramMap.put("localPath", entrySplits[0]);	
			paramMap.put("OKPath", entrySplits[1]);	
			
		}else if("2".equals(str)){
			//每天未达结果
			String FTPIP = EdataDao.findSysbaseParam("FTPIP"); //ftp地址
			String FTPPORT = EdataDao.findSysbaseParam("FTPPORT"); //ftp 端口号
			String FTPUSER = EdataDao.findSysbaseParam("FTPUSER"); //ftp 用户名
			String FTPPASSWORD = EdataDao.findSysbaseParam("FTPPASSWORD"); //tfp 密码
			String remoteFileName = "EBILL_CHECK.txt"; //上传后的文件名
			
			String[] localPaths = fileLocalPath.split("\\|@\\|");
			String localPath = localPaths[0]; //待上传的本地文件
			String okPath = localPaths[1]; //OK文件存放路径
			
			String remotePath =EdataDao.findSysbaseParam("EBILLCHECKPATH");
			remotePath=remotePath+"/"+dataDate.substring(0,4)+"/"+dataDate.substring(4,6)+"/"+dataDate.substring(6,8); //待上传的文件夹 EBILL_CHECK/2013/12/28
		
			paramMap.put("FTPIP", FTPIP);
			paramMap.put("FTPPORT", FTPPORT);
			paramMap.put("FTPUSER", FTPUSER);
			paramMap.put("FTPPASSWORD", FTPPASSWORD);
			paramMap.put("remotePath", remotePath);
			paramMap.put("remoteFileName", remoteFileName);
			paramMap.put("localPath", localPath);
			paramMap.put("OKPath", okPath);	
			
		}else if("3".endsWith(str)){
			//文件下载使用 
		 	String FTPIP = EdataDao.findSysbaseParam("FTPIP"); //ftp地址
			String FTPPORT = EdataDao.findSysbaseParam("FTPPORT"); //ftp 端口号
			String FTPUSER = EdataDao.findSysbaseParam("FTPUSER"); //ftp 用户名
			String FTPPASSWORD = EdataDao.findSysbaseParam("FTPPASSWORD"); //tfp 密码
			String remotePath =EdataDao.findSysbaseParam("NETRESULTPATH"); //网银存放对账单结果和调节表结果的文件目录 格式如： /home/ebill_detail
			remotePath=remotePath+"/"+dataDate.substring(0,4)+"/"+dataDate.substring(4,6)+"/"+dataDate.substring(6,8);
			
			String resultFileLocalPath = fileLocalPath + File.separator + dataDate+"_EBILL_RESULT.txt"; // 对账单结果缓冲路径
			String detailFileLocalPath = fileLocalPath + File.separator + dataDate+"_EBILL_DETAIL.txt"; //对账单调节表缓冲路径
			
			String OKFilePath = fileLocalPath + File.separator +"EBILLOK.txt"; //标示文件缓冲路径
			paramMap.put("FTPIP", FTPIP);
			paramMap.put("FTPPORT", FTPPORT);
			paramMap.put("FTPUSER", FTPUSER);
			paramMap.put("FTPPASSWORD", FTPPASSWORD);
			paramMap.put("remotePath", remotePath);
			paramMap.put("resultFileLocalPath", resultFileLocalPath);
			paramMap.put("detailFileLocalPath", detailFileLocalPath);
			paramMap.put("OKFileLocalPath", OKFilePath);
		}
		return paramMap;
	}



	/**
	 * 每天抓取对账系统未达处理结果，生成文件，上传到网银FTP服务器
	 * @return
	 */
	@Override
	public int fetchResultFromEbill() {
		String  dataDate = getbeforeDate();
		
		//1.1 从日志表中查找当天执行结果情况
		String sql=" ebs_basicinfolog  where opdate='"+dataDate+"' and opmode='"+OperLogModuleDefine.NOTMATCH+"'";
		Long couFlag=0l;
		try {
			couFlag=EdataDao.queryTableCount(sql);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		
		//1.2 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行，则开始执行
		try{
			if(couFlag==0 && netSwitch){
				//2.2.1 抓取当天处理的未达数据，生成文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				String  filecachePath = creatNotMatchDataFile(dataDate,filePath);
				//2.2.2 上传文件
		
				Map<String,String> paramMap = creatFTPMap("2",filecachePath,dataDate);
//				FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
//				ftpUtils.upload(paramMap);
				createOutFile(paramMap);
				
				//2.2.3 记录日志
				tools.getDataLogUtils().error(OperLogModuleDefine.SENDSUCCESS,String.valueOf(OperLogModuleDefine.NOTMATCH),dataDate);				
			}
		}catch(Exception e){
			//记录日志 执行失败
			tools.getDataLogUtils().error("上传未达结果失败!",String.valueOf(OperLogModuleDefine.NOTMATCH),dataDate);
			return 0;
		}
		return 1;
	}
	
	/**
	 * 抓取当天处理后的未达结果，生成文件
	 * @param dataDate
	 * @param filePath
	 * @return
	 * @throws XDocProcException 
	 * @throws IOException 
	 */
	public String creatNotMatchDataFile(String dataDate, String filePath) throws XDocProcException, IOException {		
		Map<String, String> queryMap = new HashMap<String, String>();
		String dateTemp = changeTimeFormat(dataDate,"yyyy-MM-dd");
		queryMap.put("inputOpTime",dateTemp+"%"); //当天时间
		List<NotMatchTable> resultList=  biz.getAllNotMatchData(queryMap);
		String resultFilePath = filePath+File.separator+dataDate+"_EBILL_CHECK.txt";
		//生成打印数据
		List<String> fileStr = getNotMatchDataStrList(resultList);
		
		FileOutputStream fos = new FileOutputStream(new File(resultFilePath));
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
		if (fileStr != null && fileStr.size() != 0) {				
			for (int i = 0; i < fileStr.size(); i++) {			
				writer.write(fileStr.get(i));
			}
			writer.close();
			fos.flush();
			fos.close();
		}
		
		//生成EBILLOK文件
		File file = new File(filePath+File.separator+"EBILLOK.txt");
		FileOutputStream fosOK = new FileOutputStream(file);
		fosOK.flush();
		fosOK.close();
		return resultFilePath+"|@|"+filePath+File.separator+"EBILLOK.txt";
		
	}

	/**
	 * 将未达明细生成打印结果
	 * 		根据未达明细结果，按照账号将其分类，如果账号的未达明细都是人工调节相符 则结果为相符；有人工不相符 则不相符
	 * 
	 * 		最终给网银的根据文档：
	 * 			3 ：人工调节相符
	 * 			4：人工调节不符
	 * @param resultList
	 * @return
	 */
	public List<String> getNotMatchDataStrList(List<NotMatchTable> resultList) {
		HashMap<String, String> accnoCheckflagMap = new HashMap<String, String>();
		HashMap<String,String> resultLMap = new HashMap<String, String>();
		List<String> notMatchResultList = null;
		
		for (NotMatchTable notMatchTable : resultList) {
			if (!accnoCheckflagMap.containsKey(notMatchTable.getVoucherNo()+"|"+notMatchTable.getDocDate()+ "|"+notMatchTable.getAccNo() + "|"
					+ notMatchTable.getCheckFlag())) {
				// 循环未达明细，把账号和余额结果放到MAP中
				accnoCheckflagMap.put(notMatchTable.getVoucherNo()+"|"+notMatchTable.getDocDate()+ "|"+notMatchTable.getAccNo() + "|"
						+ notMatchTable.getCheckFlag(), "");
			}
		}
		// 更新checkflag
		String accno;
		String checkfalg;
		if (!accnoCheckflagMap.isEmpty()) {
			for (Map.Entry<String, String> entry : accnoCheckflagMap.entrySet()) {
				if (entry.getKey() != null
						&& entry.getKey().trim().length() != 0) {
					accno = entry.getKey().split("[|]")[2];
					checkfalg = entry.getKey().split("[|]")[3];
					boolean isRight = true;
					// 如果未达全部为相符时才调节为相符，只要有一笔不符则改成不相符
					for (NotMatchTable notMatchTable : resultList) {
						if (accno.equals(notMatchTable.getAccNo())) {
							// 只要有1笔为不相符则视为不符 2:对账不符 5:人工调节不符
							if ("2".equals(notMatchTable.getCheckFlag())
									|| "5".equals(notMatchTable.getCheckFlag())) {
								isRight = false;
							}
						}
					}

					String[] strs =  entry.getKey().split("[|]");
					if (isRight) {
						// 全部为相符的情况
						if(resultLMap.containsKey(entry.getKey())){
							//有的话 ，则删除，添加
							resultLMap.remove(entry.getKey());
							resultLMap.put(strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"3", strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"3");							
						}else{
							//直接添加
							resultLMap.put(strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"3", strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"3");
						}
					} else {
						//为不相符的情况
						if(resultLMap.containsKey(entry.getKey())){
							//有的话 ，则删除，添加
							resultLMap.remove(entry.getKey());
							resultLMap.put(strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"4", strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"4");							
						}else{
							//直接添加
							resultLMap.put(strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"4", strs[0] + "|"+"\\N"+"|"+strs[2]+"|"+"4");
						}
					}
					isRight = true;
				}
			}
			
			//对resultLMap（未达结果集合）进行处理 生成每行数据  现格式为：对账编号|日期|账号|结果
			notMatchResultList = creatNotMatchStrs(resultLMap);
		}
		return notMatchResultList;
	}


	/**
	 * 将结果resultMap【每个对账单，一个账号，一条记录】集合（对账编号|日期|账号|结果）的结果 生成文件的每行记录
	 * @param resultLMap
	 * @return
	 */
	public List<String> creatNotMatchStrs(HashMap<String, String> resultLMap) {
		List<String> resultList = new ArrayList<String>();
		List<String> keyList  = new ArrayList<String>();// 要删除的key
		HashMap<String, String> catchMap = new HashMap<String, String>(); //key:编号|日期   value
		if(resultLMap!=null){
			for(Map.Entry<String, String> entry : resultLMap.entrySet()){
				String[] strs = entry.getKey().split("[|]");				
				if(catchMap.containsKey(strs[0])){
					//包含的话 说明，一张对账单有多个账户，则不是网银的，剔除！
					//这里可能会多拿出：一张对账单只有一个账户的对账可能不是网银账户
					String value = catchMap.get(strs[0]);
					value = strs[0]+"|@|"+strs[1]+"|@|"+strs[2]+"|@|"+strs[3];
					//删除catchMap中的数据 将新的数据添加
					catchMap.remove(strs[0]);
					catchMap.put(strs[0]+"|", value);
				}else if(catchMap.containsKey(strs[0]+"|")){
					String value = catchMap.get(strs[0]+"|");
					value = strs[0]+"|@|"+strs[1]+"|@|"+strs[2]+"|@|"+strs[3];
					//删除catchMap中的数据 将新的数据添加
					catchMap.remove(strs[0]+"|");
					catchMap.put(strs[0]+"|", value);
				}else{
					//不包含则想catchMap添加
					catchMap.put(strs[0], strs[0]+"|@|"+strs[1]+"|@|"+strs[2]+"|@|"+strs[3]);
				}
			}
			
			//删除有"|",说明一个账单有多个账户，则删除
			for(Map.Entry<String, String> entry : catchMap.entrySet()){
				if(entry.getKey().contains("|")){
					keyList.add(entry.getKey());
				}
			}
			for(int i=0;i<keyList.size();i++){
				catchMap.remove(keyList.get(i));
			}
			
			
			//对catchMap数据格式不全
			for(Map.Entry<String, String> entry : catchMap.entrySet()){
				String entryValue = entry.getValue();
				String[] entrySplits = entryValue.split("\\|@\\|");
				if(entrySplits.length<12){
					//不全
					for(int i=entrySplits.length;i<12;i++){
						entryValue = entryValue+"|@|\\N";
					}
				}
				//补上回车
				entryValue = entryValue+"\n";
				resultList.add(entryValue);
			}
		}
		return resultList;
	}



	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public IEdataDao getEdataDao() {
		return EdataDao;
	}

	public void setEdataDao(IEdataDao edataDao) {
		EdataDao = edataDao;
	}
	
	/**
	 * 将yyyyMMdd格式的时间转换为指定格式的时间串
	 * @param time
	 * @param format
	 * @return
	 */
	public String  changeTimeFormat(String time,String format){
		SimpleDateFormat fo = new SimpleDateFormat("yyyyMMdd");
		Date dateTemp = null;
		try {
			dateTemp = fo.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		fo = new SimpleDateFormat(format);
		return fo.format(dateTemp);
	}
	
	/**
	 * 当前时间的前一天
	 * @return yyyyMMdd
	 */
	public String getbeforeDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        calendar.set(Calendar.DATE, calendar.get(Calendar.DAY_OF_MONTH)-1);
        return format.format(calendar.getTime());
	}
	/**
	 * 当前时间的上个月最后一天
	 * @return
	 */
	public String getLastdateOFmonth(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return format.format(calendar.getTime());
	}
	
	
	/**
	 * 给定时间的上个月最后一天
	 * @return
	 * @throws ParseException 
	 */
	public String getLastdateOFmonth(String dateStr) throws ParseException{
		Calendar calendar = Calendar.getInstance();		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");		
		calendar.setTime(format.parse(dateStr));
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));  
        return format.format(calendar.getTime());
	}
	
	/**
	 * 生成对账单文件
	 * @param dataDte
	 * @param filePath (将文件暂时保存的路劲)
	 * @return 缓存文件的全路径
	 * @throws XDocProcException 
	 * @throws IOException 
	 */
	public String creatCheckMainDataFile(String dataDate,String filePath) throws XDocProcException, IOException{
		Map<String, String> queryMap = new HashMap<String, String>();
		queryMap.put("docDate",dataDate); //当月的对账单
		queryMap.put("sendMode","3"); //为网银账户
		List<CheckMainData> resultList = checkMainDataAdm.getExportCheckMainData(queryMap);
		
		String resultFilePath = filePath+File.separator+dataDate+"_EBILL.txt";
		
		FileOutputStream fos = new FileOutputStream(new File(resultFilePath));
		OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
		if (resultList != null && resultList.size() != 0) {				
			for (int i = 0; i < resultList.size(); i++) {
				CheckMainData data = (CheckMainData) resultList.get(i);					
				BatchPrintBean batchPrintBean;
				batchPrintBean = getBatchPrintList(data);	
				String filedate = getPrintDataByStr(batchPrintBean);
				writer.write(filedate);
			}
			writer.close();
			fos.flush();
			fos.close();
		}
		
		//生成EBILLOK文件
		File file = new File(filePath+File.separator+"EBILLOK.txt");
		FileOutputStream fosOK = new FileOutputStream(file);
		fosOK.flush();
		fosOK.close();
		return resultFilePath+"|@|"+filePath+File.separator+"EBILLOK.txt";
	}
	
	/**
	 * 导出对账单（账户）的源数据列表
	 * 
	 * @return
	 * @throws XDocProcException 
	 * @throws DaoException
	 */
	public BatchPrintBean getBatchPrintList(CheckMainData data) throws XDocProcException {
		/** 账单信息：账单编号、对账日期、客户号、户名、邮政编码、地址、联系人、联系电话 */
		BatchPrintBean total = new BatchPrintBean();
		total.setVoucherno(data.getVoucherNo());
		String date = data.getDocDate().substring(0, 4) + "-"
				+ data.getDocDate().substring(4, 6) + "-"
				+ data.getDocDate().substring(6, 8);
		total.setDocdate(date);
		total.setCustomerid(data.getCustId());

		if (data.getZip() != null && !"null".equals(data.getZip())) {
			total.setZip(data.getZip());
		} else {
			total.setZip("");
		}
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setAddress(data.getSendAddress()+"("+data.getIdBank()+")");
		} else {
			total.setAddress("");
		}
		if (data.getLinkMan() != null && !"null".equals(data.getLinkMan())) {
			total.setLinkman(data.getLinkMan());
		} else {
			total.setLinkman("");
		}
		if (data.getPhone() != null && !"null".equals(data.getPhone())) {
			total.setPhone(data.getPhone());
		} else {
			total.setPhone("");
		}
		if (data.getAccName() != null && !"null".equals(data.getAccName())) {
			total.setAccname( data.getAccName());
		} else {
			total.setAccname("");
		}
		
		List<AccNoMainData> docList = accnoMainDataAdm
				.getAccnoMainDataByVoucherNo(data.getVoucherNo());     //根据对账单编号得到 其下面的1-5个 明细信息
		for (int j = 0; j < docList.size(); j++) {
			AccNoMainData doc = (AccNoMainData) docList.get(j);
			Map<String,String> subMap = RefTableTools.ValParamSubnocMap;
			Map<String,String> currType = RefTableTools.valParamCurrtypeMap;
			/** 账号信息：账号、账户类型、开户行名、余额、币种 */
			if (j == 0) {
				if (doc.getAccNo() != null) {
					total.setAccno1(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo1(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank1(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname1(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit1(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN1(currType.get(doc.getCurrency()));
				}
			}
			if (j == 1) {
				if (doc.getAccNo() != null) {
					total.setAccno2(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo2(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank2(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname2(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit2(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN2(currType.get(doc.getCurrency()));
				}
			}
			if (j == 2) {
				if (doc.getAccNo() != null) {
					total.setAccno3(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo3(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank3(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname3(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit3(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN3(currType.get(doc.getCurrency()));
				}
			}
			if (j == 3) {
				if (doc.getAccNo() != null) {
					total.setAccno4(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo4(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank4(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname4(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit4(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN4(currType.get(doc.getCurrency()));
				}
			}
			if (j == 4) {
				if (doc.getAccNo() != null) {
					total.setAccno5(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo5(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank5(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname5(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit5(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN5(currType.get(doc.getCurrency()));
				}
			}
		}
		return total;
	}
	
		// 字段间以||分隔，生成对账单文件的格式
	public String getPrintDataByStr(BatchPrintBean data) {
			StringBuffer returnStr = new StringBuffer();
			returnStr.append("");
			String cutFlag ="|@|";
			returnStr.append(data.getVoucherno()); 		 //账单编号
			returnStr.append(cutFlag + data.getIdbank1()); 		 //机构号
			returnStr.append(cutFlag + data.getDocdate());		 	 //对账日期		
			returnStr.append(cutFlag + data.getAccno1());          //账号1
			returnStr.append(cutFlag + data.getAccname());         //户名1
			returnStr.append(cutFlag+ data.getCredit1());          //余额1
			returnStr.append(cutFlag+ data.getCurrtypeCN1());      //币种1
			
			if(data.getAccno2()==null || "".endsWith(data.getAccno2())){
				returnStr.append( cutFlag + "\\N");          //账号2
				returnStr.append( cutFlag + "\\N");         //户名2
				returnStr.append( cutFlag+ "\\N");          //余额2
				returnStr.append( cutFlag+ "\\N");      //币种2
			}else{
				returnStr.append( cutFlag + data.getAccno2());          //账号2
				returnStr.append( cutFlag + data.getAccname());         //户名2
				returnStr.append( cutFlag+ data.getCredit2());          //余额2
				returnStr.append( cutFlag+ data.getCurrtypeCN2());      //币种2
			}
		
			
			if(data.getAccno3()==null || "".endsWith(data.getAccno3())){
				returnStr.append( cutFlag + "\\N");          //账号3
				returnStr.append( cutFlag + "\\N");         //户名3
				returnStr.append( cutFlag+ "\\N");          //余额3
				returnStr.append( cutFlag+ "\\N");      //币种3
			}else{
				returnStr.append( cutFlag + data.getAccno3());          //账号3
				returnStr.append( cutFlag + data.getAccname());         //户名3
				returnStr.append( cutFlag+ data.getCredit3());          //余额3
				returnStr.append( cutFlag+ data.getCurrtypeCN3());      //币种3
			}
			
			if(data.getAccno4()==null || "".endsWith(data.getAccno4())){
				returnStr.append( cutFlag + "\\N");          //账号4
				returnStr.append( cutFlag + "\\N");         //户名4
				returnStr.append( cutFlag+ "\\N");          //余额4
				returnStr.append( cutFlag+ "\\N");      //币种4
			}else{
				returnStr.append( cutFlag + data.getAccno4());          //账号4
				returnStr.append( cutFlag + data.getAccname());         //户名4
				returnStr.append( cutFlag+ data.getCredit4());          //余额4
				returnStr.append( cutFlag+ data.getCurrtypeCN4());      //币种4
			}
			
			if(data.getAccno5()==null || "".endsWith(data.getAccno5())){
				returnStr.append( cutFlag + "\\N");          //账号5
				returnStr.append( cutFlag + "\\N");         //户名5
				returnStr.append( cutFlag+ "\\N");          //余额5
				returnStr.append( cutFlag+ "\\N");      //币种5
			}else{
				returnStr.append( cutFlag + data.getAccno5());          //账号5
				returnStr.append( cutFlag + data.getAccname());         //户名5
				returnStr.append( cutFlag+ data.getCredit5());          //余额5
				returnStr.append( cutFlag+ data.getCurrtypeCN5());      //币种5
			}		
			returnStr.append("\n");
			return returnStr.toString();
	}
	
	
	/**
	 * 解析网银传来的对账结果
	 * @param filePath date
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String,String>> analysisResult(String filePath,String date) throws IOException{
		List<Map<String,String>> resultMap = new ArrayList<Map<String,String>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
		
		String readLine;
		long lineNumber=0;
		while((readLine = reader.readLine())!=null){
			String[] temp = readLine.split("\\|@\\|");
			Map<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("VOUCHERNO", temp[0]);
			tempMap.put("DOCDATE", temp[1]);
			tempMap.put("ACCNO1", temp[2]);
			tempMap.put("RESULT1", temp[3]);
			tempMap.put("ACCNO2", temp[4]);
			tempMap.put("RESULT2", temp[5]);
			tempMap.put("ACCNO3", temp[6]);
			tempMap.put("RESULT3", temp[7]);
			tempMap.put("ACCNO4", temp[8]);
			tempMap.put("RESULT4", temp[9]);
			tempMap.put("ACCNO5", temp[10]);
			tempMap.put("RESULT5", temp[11]);
			resultMap.add(tempMap);
			lineNumber++;
		}
		
		tools.getDataLogUtils().info("数据日期："+date+", 信息：文件:EBILL_RESULT.dat 处理成功，共处理"+lineNumber+"条",String.valueOf(OperLogModuleDefine.DATARESULT),date);
		return resultMap;
	}
	
	/**
	 * 解析网银传来的调节表结果
	 * @param filePath date
	 * @return
	 * @throws IOException 
	 */
	public List<Map<String,String>> analysisDetail(String filePath,String date) throws IOException{
		List<Map<String,String>> resultMap = new ArrayList<Map<String,String>>();
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), "UTF-8"));
		
		String readLine;
		long lineNumber=0;
		while((readLine = reader.readLine())!=null){
			String[] temp = readLine.split("\\|@\\|");
			Map<String,String> tempMap = new HashMap<String,String>();
			tempMap.put("VOUCHERNO", temp[0]);
			tempMap.put("ACCNO", temp[1]);
			tempMap.put("TRACEDATE", temp[2]);
			tempMap.put("TRACENO", temp[3]);
			tempMap.put("MEMO", temp[4]);
			tempMap.put("CREDIT1", temp[5]);
			tempMap.put("CREDIT2", temp[6]);
			tempMap.put("CREDIT3", temp[7]);
			tempMap.put("CREDIT4", temp[8]);
			tempMap.put("RESULT", temp[9]);
			resultMap.add(tempMap);
			lineNumber++;
		}
		
		tools.getDataLogUtils().info("数据日期："+date+", 信息：文件:EBILL_DETAIL.dat 处理成功，共处理"+lineNumber+"条",String.valueOf(OperLogModuleDefine.DATARESULT),date);
		return resultMap;
	}
	
	public CheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}
	public void setCheckMainDataAdm(CheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}
	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}
	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}
	public INotMatchBiz getBiz() {
		return biz;
	}
	public void setBiz(INotMatchBiz biz) {
		this.biz = biz;
	}public WebBankUtil getWebBankUtil() {
		return webBankUtil;
	}
	public void setWebBankUtil(WebBankUtil webBankUtil) {
		this.webBankUtil = webBankUtil;
	}


	/**
	 * 手动触发 
	 * 	从网银FTP服务器下载，并处理每天网银传来的对账单结果和调节表
	 * @param autoId
	 * @param date
	 * @return
	 */
	@Override
	public int exchangeResultFormNet(String autoId, String date) {	
		//1.2 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行,并且FTP服务器当天文件夹中有OK文件了，则开始执行	
		try{
			if(netSwitch){
				//2.2.1 下载文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				Map<String,String> paramMap = creatFTPMap("3",filePath,date);
				//FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
				//boolean isOk = ftpUtils.down(paramMap);
				boolean isOk = this.createInFile(paramMap);
				//有OK文件再执行
				if(isOk){
					//2.2.2 解析文件
					List<Map<String, String>> resultList = analysisResult(filePath+File.separator+date+"_EBILL_RESULT.txt",date); //对对账结果解析
					List<Map<String, String>> detailList = analysisDetail(filePath+File.separator+date+"_EBILL_DETAIL.txt",date); //对调节单解析			
					// 2.2.3 对结果处理
					webBankUtil.proVoucherResulte(resultList, detailList);		
					//2.2.4 记录日志 更新
					BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("下载网银对账结果结果成功!", String.valueOf(OperLogModuleDefine.DATARESULT), date);
					basic.setAutoId(Long.valueOf(autoId));
					tools.getDataLogUtils().updateBasicInfoLog(basic);			
				}else{
					//记录日志 执行失败 更新
					BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("下载网银对账结果失败!", String.valueOf(OperLogModuleDefine.DATARESULT), date);
					basic.setAutoId(Long.valueOf(autoId));
					tools.getDataLogUtils().updateBasicInfoLog(basic);
					return 0;
				}
			}
		}catch(Exception e){
			//记录日志 执行失败 更新
			BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("下载网银对账结果失败!", String.valueOf(OperLogModuleDefine.DATARESULT), date);
			basic.setAutoId(Long.valueOf(autoId));
			try {
				tools.getDataLogUtils().updateBasicInfoLog(basic);
			} catch (XDocProcException e1) {
			}
			return 0;
		}
		return 1;
	}


	/**
	 * 手动触发
	 * 	每月月底，生成完对账单，抓取网银对账单，生成文件上传到网银FTP服务器
	 * @param autoId
	 * @param date
	 * @return
	 */
	@Override
	public int fetchEbillFromEbill(String autoId, String date) {
		
		//1.1 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行，则开始执行
		try{
			String  dataDate = date;
			if(netSwitch){
				//2.2.1 抓取发送方式为网银的账户对账单，生成文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				String  filecachePath = creatCheckMainDataFile(dataDate,filePath);
				//2.2.2 上传文件
				Map<String,String> paramMap = creatFTPMap("1",filecachePath,dataDate);
				//FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
				//ftpUtils.upload(paramMap);
				this.createOutFile(paramMap);
				//2.2.3 记录日志  更新
				BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("上传对账单成功!", String.valueOf(OperLogModuleDefine.DATAPAPER), date);
				basic.setAutoId(Long.valueOf(autoId));
				tools.getDataLogUtils().updateBasicInfoLog(basic);	   
			}
		}catch(Exception e){ 
			//记录日志 执行失败  更新
			BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("上传对账单失败!", String.valueOf(OperLogModuleDefine.DATAPAPER), date);
			basic.setAutoId(Long.valueOf(autoId));
			try {
				tools.getDataLogUtils().updateBasicInfoLog(basic);
			} catch (XDocProcException e1) {
			}	
			return 0;
		}
		return 1;
	}


	/**
	 * 手动触发
	 * 	每天抓取对账系统未达处理结果，生成文件，上传到网银FTP服务器
	 * @param autoId
	 * @param date
	 * @return
	 */
	@Override
	public int fetchResultFromEbill(String autoId, String date) {
		
		//1.1 查询网银开关  如果为FALSE 则不执行网银程序，为TRUE则执行
		boolean netSwitch = false;
		String netSwitchStr = EdataDao.findSysbaseParam("NETSWITCH");
		if("TRUE".endsWith(netSwitchStr)){
			netSwitch= true;
		}
		
		//2.1 如果当天结果 有执行结果， 则结束（不管成功，失败）
		//2.2 如果当天没有执行，则开始执行
		try{
			if(netSwitch){
				//2.2.1 抓取当天处理的未达数据，生成文件
				String filePath = EdataDao.findSysbaseParam("NETCACHEFILEPATH");
				String  filecachePath = creatNotMatchDataFile(date,filePath);
				//2.2.2 上传文件
		
				Map<String,String> paramMap = creatFTPMap("2",filecachePath,date);
				//FTPUtils ftpUtils = new FTPUtils(paramMap.get("FTPIP"), paramMap.get("FTPUSER"), paramMap.get("FTPPASSWORD"), Integer.valueOf(paramMap.get("FTPPORT")));
				//ftpUtils.upload(paramMap);
				this.createOutFile(paramMap);
				
				//2.2.3 记录日志  更新
				BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("上传未达结果成功!", String.valueOf(OperLogModuleDefine.NOTMATCH), date);
				basic.setAutoId(Long.valueOf(autoId));
				tools.getDataLogUtils().updateBasicInfoLog(basic);				
			}
		}catch(Exception e){
			//记录日志 执行失败  更新
			BasicInfoLog basic = tools.getDataLogUtils().creatBasicInfoLog("上传未达结果失败!", String.valueOf(OperLogModuleDefine.NOTMATCH), date);
			basic.setAutoId(Long.valueOf(autoId));
			try {
				tools.getDataLogUtils().updateBasicInfoLog(basic);
			} catch (XDocProcException e1) {
			}	
			return 0;
		}
		return 1;
	}
	
	
	public void createOutFile(Map<String,String> paramMap){
		//2.上传文件
		 String remotePath = paramMap.get("remotePath"); //FTP服务器指定路径  如：/home/dataFile/2013/12/11
		 String remoteFileName = paramMap.get("remoteFileName"); //上传后命的文件名
		 String localPath = paramMap.get("localPath"); //待上传的本地文件所在路径+文件名
		 String OKPath =  paramMap.get("OKPath"); //待上传的本地文件OK所在路径+文件名 D://dd/EBILLOK.dat
		 //判断本地缓存文件是否存在
		 File localFile = new File(localPath);
		 if(localFile.exists()){
			 //如果目标文件夹路径不存在，则创建文件夹路径
			 File outFile = new File(remotePath);
			 if(!outFile.exists()){
				 outFile.mkdirs();
			 }
			 //如果目标文件存在，则删除
			 File outFileName = new File(remotePath+"/"+remoteFileName);
			 if(outFileName.exists()){
				 outFileName.delete();
			 }
			 //将缓存结果放在out文件目录中
			 this.copyFile(localFile, outFileName);
			 //将outFileName的文件权限修改为777
			 try {
				Runtime.getRuntime().exec("chmod 777 "+remotePath+"/"+remoteFileName);
			} catch (IOException e) {
			}
		 }
		 //判断本地ok文件是否存在
		 File okLocalFile = new File(OKPath);
		 if(okLocalFile.exists()){
			 //如果ok文件存在，则删除
			 File okOutFile = new File(remotePath+"/EBILLOK.txt");
			 if(okOutFile.exists()){
				 okOutFile.delete();
			 }
			 //将缓存ok文件放在out文件目录中
			 this.copyFile(okLocalFile, okOutFile);
			 //将okOutFile的文件权限修改为777
			 try {
				Runtime.getRuntime().exec("chmod 777 "+remotePath+"/EBILLOK.txt");
			} catch (IOException e) {
			}
		 }
	}
		
	public boolean createInFile(Map<String,String> paramMap){
		//2.下载文件
		String remotePath = paramMap.get("remotePath"); //FTP服务器指定路径  如：/home/dataFile/2013/12/11
		String resultFileLocalPath = paramMap.get("resultFileLocalPath"); //对账单文件的缓存路径  // d：//1/a/result.txt
		String detailFileLocalPath = paramMap.get("detailFileLocalPath"); //调节单文件的缓存路径  // d：//1/a/detail.txt
		String OKPath = paramMap.get("OKFileLocalPath"); //ok的缓存路径  // d：//1/a/detail.txt
		//判断ok存文件是否存在
		File okFile = new File(remotePath+"/EBILLOK.txt");
		if(okFile.exists()){
			//将核对结果复制到缓存路径
			File resultLocalFile = new File(resultFileLocalPath);
			File resultFile = new File(remotePath+"/EBILL_RESULT.txt");
			if(resultFile.exists()){
				this.copyFile(resultFile, resultLocalFile);
			}
			//将未达明细复制到缓存路径
			File detailFile = new File(remotePath+"/EBILL_DETAIL.txt");
			File detailLocalFile = new File(detailFileLocalPath);
			if(detailFile.exists()){
				this.copyFile(detailFile, detailLocalFile);
			}
			//将ok文件复制到缓存路径
			File okLocalFile = new File(OKPath);
			this.copyFile(okFile, okLocalFile);
			return true;
		}else{
			return false;
		}	
	}
	
	private synchronized void copyFile(File srcFile,File desFile){
		//如果目标文件不存在，则创建
		if(!desFile.exists()){
			desFile.mkdirs();
		}
		//如果目标文件存在，则删除内容
		if(desFile.exists()){
			desFile.delete();
		}
		
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try {
			//创建一个源文件的输入流
			reader = new BufferedReader(new InputStreamReader(
						new FileInputStream(srcFile), "UTF-8"));
			//创建一个目标文件的输出流
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(desFile),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		 //读取源文件内容,并写入目标文件中
		String readLine="";
		long lineTemp = 0; //临时存储行数
		try {
			while((readLine = reader.readLine())!=null){
				if(readLine.length()>0){
					writer.write(readLine);
					writer.newLine();
				}
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(reader!=null){
					reader.close();
				}
				if(writer != null){
					writer.close();
				} 
			}catch (IOException e) {
				 e.printStackTrace();
			}
		}
	}
}
