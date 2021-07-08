package com.yzj.ebs.auto.verifydata;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import com.yzj.ebs.auto.DownImage;
import com.yzj.ebs.auto.JacobLoader;
import com.yzj.ebs.auto.ResultBean;
import com.yzj.ebs.auto.Synchronizer;
import com.yzj.wf.as.common.IJobRegister;
import com.yzj.wf.as.common.JobLogic;
import com.yzj.wf.as.entity.BusinessInfo;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.pam.common.IParamManager;
import com.yzj.ebs.common.IServiceForRemote;
import com.yzj.ebs.common.OperLogModuleDefine;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;

/**
 * 创建于:2012-8-18<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自动验印
 * 
 * @author 秦靖锋
 * @version 1.0.0
 */
public class AutoVerifyData extends JobLogic {

	private String userId;
	private static final String AutoType = "AUTOVERIFY";
	private static final String ControlName = "XOCRXML.XOcrXmlCtrl.1";
	private static final String MethodName = "RecognizeBill";
	private static final String ParamGroupName = "DOWNPARAM";
	private static final String ParamName_ip = "ip";
	private static final String ParamName_port = "port";
	private static final String ParamName_imageLocalPath = "imageLocalPath";
	private static final String ParamName_imageServer = "imageServer";

	private CheckMainData checkmaindata;
	private IJobRegister jobRegister;
	private IServiceForRemote serviceForRemote;
	private IParamManager paramManager;
	private DownImage downImage;
	private DocSet docSet;

	public void init() throws Exception {
		if (jobRegister != null) {
			jobRegister.addJobLogic(AutoType, this);
			String ip = paramManager
					.getParamByName(ParamGroupName, ParamName_ip)
					.getParamValue().toString();
			String port = paramManager
					.getParamByName(ParamGroupName, ParamName_port)
					.getParamValue().toString();
			String imageLocalPath = paramManager
					.getParamByName(ParamGroupName, ParamName_imageLocalPath)
					.getParamValue().toString();
			String imageServer = paramManager
					.getParamByName(ParamGroupName, ParamName_imageServer)
					.getParamValue().toString();
			downImage = new DownImage(ip, Integer.valueOf(port),
					imageLocalPath, imageServer);
		} else {
			throw new Exception("未获取到IJobRegister服务");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.wf.as.common.JobLogic#action()
	 */
	@Override
	public BusinessInfo action() throws Exception {
		try {
			serviceForRemote.debug("开始获取自动匹配任务。。。",false);
			IBPMTask task = serviceForRemote.getTask(AppId.AUTO_VERIFYDATA,
					userId);
			String strFileName = null;
			String strBackFileName = null;
			if (task != null) {

				String taskId = task.getId();
				long bizKey = Long.valueOf(task.getBusinessKey());
				serviceForRemote.info("1、获取到一笔自动匹配任务,taskid:" + taskId + ",bizKey:"
						+ bizKey,String.valueOf(OperLogModuleDefine.autoOCR));
				serviceForRemote.debug("开始查询docset。。。",false);
				docSet = serviceForRemote.queryDocSetByID(bizKey);
				String idcenter = docSet.getIdCenter();
				String branchNo = docSet.getIdBank();
				String voucherNo = docSet.getVoucherNo();
				String credit = docSet.getStrcredit();

				try {
					serviceForRemote.debug("2、开始下载影像...",String.valueOf(OperLogModuleDefine.autoOCR));
					String imageUrl = docSet.getFrontImagePath(); //正面
					strFileName = downImage.downImage(imageUrl);
					imageUrl = docSet.getBackImagePath(); //反面
					strBackFileName = downImage.downImage(imageUrl);
					
					serviceForRemote.info("3、正面影像路径为:  strFileName=" +strFileName,String.valueOf(OperLogModuleDefine.autoOCR));
					serviceForRemote.info("4、背面影像路径为:  strBackFileName=" +strBackFileName,String.valueOf(OperLogModuleDefine.autoOCR));
					
				} catch (Exception e1) {
					e1.printStackTrace();
					serviceForRemote.error("5、下载图像时发生异常" + e1.getMessage(), e1,String.valueOf(OperLogModuleDefine.autoOCR));
					serviceForRemote.commitTask(task, docSet, userId,
							TaskResult.FAIL);
					ResultBean resultBean = new ResultBean(bizKey, voucherNo,
							branchNo, credit);
					// 封装转给页面的对象
					BusinessInfo businessInfo = new BusinessInfo(AutoType,
							false, resultBean, "");
					return businessInfo;
				}
				
				
				// 进行OCR识别
				Object[] param = new Object[] { strFileName };
				synchronized (Synchronizer.simpleSynchronizer) {
					JacobLoader loader = null;
					try {
						loader = new JacobLoader();
						serviceForRemote.debug("5、开始进行自动匹配...",String.valueOf(OperLogModuleDefine.autoOCR));
						if (loader.load(ControlName)) {
//							// 先把图像路径传进去纠偏
//							String value = loader.doMethod("Init", param);
//							String ocrStrResult = null;
//							if (value.equals("0")) {
//								// 再把图像传进去做OCR识别
//								ocrStrResult = loader.doMethod(MethodName,
//										param);
//							}
//							// 去做OCR识别结果判断
//							boolean result = parserOCRResult(ocrStrResult,
//									idcenter);
							
							//1.初始化控件
							//2.加载ocr文件
							//3.进行识别
							//4.封装数据
							//5.进行校验 返回OCR结果
							
							//1.加载控件 控件加载出错，则OCR为失败
							boolean result = false;
							if(InitOcx(loader)){
								//2.执行校验的影像，得到结果
									//2.1 得到正面信息
								String topMessage = readYingXFile(loader,strFileName,"1");
								
								serviceForRemote.info("6、校验 readYingXFile 正面 信息为 ： " + topMessage,String.valueOf(OperLogModuleDefine.autoOCR));
									//2.2 得到反面信息
								String backMessage = readYingXFile(loader,strBackFileName,"2");
								serviceForRemote.info("7、校验 readYingXFile 背面 信息为 ： " + backMessage,String.valueOf(OperLogModuleDefine.autoOCR));
								
								//3.对结果进行解析
								result = parserOCRResult(topMessage,backMessage,idcenter);
							}
							
							if (result) {
								serviceForRemote.debug("自动匹配成功，开始提交任务。。。",false);
								serviceForRemote.commitTask(task, docSet,
										userId, TaskResult.SUCCESS);
								serviceForRemote.info("自动匹配结束，结果为匹配,taskid:" + taskId
										+ ",bizKey:" + bizKey,String.valueOf(OperLogModuleDefine.autoOCR));
							} else {
								serviceForRemote.commitTask(task, docSet,
										userId, TaskResult.FAIL);
								serviceForRemote.info("自动匹配结束,结果为不匹配,taskid:" + taskId
										+ ",bizKey:" + bizKey,String.valueOf(OperLogModuleDefine.autoOCR));
							}
							voucherNo = docSet.getVoucherNo();
							credit = docSet.getStrcredit();
							ResultBean resultBean = new ResultBean(bizKey,
									voucherNo, branchNo, credit);
							// 封装转给页面的对象
							BusinessInfo businessInfo = new BusinessInfo(
									AutoType, result, resultBean, "");
							return businessInfo;
						} else {
							serviceForRemote.error("加载自动匹配控件失败:" + ControlName,false);
							throw new Exception("加载自动匹配控件失败:" + ControlName);
						}
					} catch (Exception e) {
						serviceForRemote.error("自动匹配发生异常," + e.getMessage(), e,String.valueOf(OperLogModuleDefine.autoOCR));
						serviceForRemote.commitTask(task, docSet, userId,
								TaskResult.FAIL);
						voucherNo = docSet.getVoucherNo();
						credit = docSet.getStrcredit();
						ResultBean resultBean = new ResultBean(bizKey,
								voucherNo, branchNo, credit);
						// 封装转给页面的对象
						BusinessInfo businessInfo = new BusinessInfo(AutoType,
								false, resultBean, "");
						return businessInfo;
					} catch (Throwable e) {
						serviceForRemote.error("自动匹配发生异常," + e.getMessage(), e,String.valueOf(OperLogModuleDefine.autoOCR));
						serviceForRemote.commitTask(task, docSet, userId,
								TaskResult.FAIL);
						voucherNo = docSet.getVoucherNo();
						credit = docSet.getStrcredit();
						ResultBean resultBean = new ResultBean(bizKey,
								voucherNo, branchNo, credit);
						// 封装转给页面的对象
						BusinessInfo businessInfo = new BusinessInfo(AutoType,
								false, resultBean, "");
						return businessInfo;
					} finally {
						if (loader != null) {
							loader.release();
						}
						//删除文件
						if(strFileName!=null){
							DeleteFileUtils.DeleteFile(strFileName);
							DeleteFileUtils.DeleteFile(strBackFileName);
						}
					}

				}
			} else {
				serviceForRemote.debug("未获取到自动匹配任务",false);
				return null;
			}
		} catch (Throwable e) {
			serviceForRemote.error("自动匹配超时",e,String.valueOf(OperLogModuleDefine.autoOCR));
			return null;
		}
	}
	
	/**
	 * 
	 * @param topMessage
	 * @param backMessage
	 * @param idcenter
	 * @return
	 * @throws RemoteException 
	 */
	private boolean parserOCRResult(String topMessage, String backMessage,
			String idcenter) throws RemoteException {
		//1.处理topMessage,backMessage
		boolean result = false;
		/**
		 * 2.逻辑
		 * 		2.1如果backMessage包含有值 则为不相符
		 * 		2.2为空
		 * 			2.2.1.对账单编号无---不相符
		 * 			2.2.2.对账单编码有
		 * 				2.2.2.1 如果没有勾选 
		 * 						2.2.2.1.1  余额和库里一直为---相符
		 * 						2.2.2.1.2 余额和库里不一直---不相符
		 * 				2.2.2.2 如果勾选
		 * 						2.2.2.2.1 勾选的 不相符 --- 不相符
		 * 						2.2.2.2.2 勾选相符
		 * 							2.2.2.2.1  余额和库里一直为---相符
		 * 							2.2.2.2.2  余额和库里不一直---不相符			
		 */
		String[] topMess = topMessage.split("\\$");
		String[] backMess = backMessage.split("\\$");
		
		//处理 背面
		String strBillNo = "";
		if(backMess.length==7){
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<backMess.length;i++){
				sb.append(backMess[i]);
			}
			if(sb.toString().trim().length()!=0){ 
				// 背面有值  不相符
				result = false;
				serviceForRemote.debug("8、对账单背面有信息，则按照不相符处理." ,String.valueOf(OperLogModuleDefine.autoOCR));
			}else{
				// 背面无值 				
				if(topMess.length==19){
					strBillNo = getTXM(topMess);
					serviceForRemote.debug("8、识别获取的账单编号为：" + strBillNo,String.valueOf(OperLogModuleDefine.autoOCR));
					if(strBillNo.trim().length()!=0){
						//识别出了对账单编号
						
						
						
						if (getCheckMaindata(strBillNo)) {
							if (!idcenter.equals(checkmaindata.getIdCenter())) {
								return false;// 不是本分行的账单直接不通过
							}
							docSet.setVoucherNo(strBillNo);
							docSet.setDocDate(checkmaindata.getDocDate());
							docSet.setIdBank(checkmaindata.getIdBank());
							docSet.setIdBranch(checkmaindata.getIdBranch());
							docSet.setIdCenter(checkmaindata.getIdCenter());
							docSet.setAccName(checkmaindata.getAccName());
							docSet.setBankName(checkmaindata.getBankName());
							List<AccNoMainData> accNoMainDataList = new ArrayList<AccNoMainData>();
							try {
								accNoMainDataList = serviceForRemote
										.getAccnoMainDataByVoucherNo(strBillNo);
								int accnum = accNoMainDataList.size();
								serviceForRemote.debug("获取对应账单账号数量为：" + accnum,false);
								
								//2 表示不相符 1 相符
								String[] Result = new String[6];  
								if ("1".equals(topMess[2])) {
									Result[1] = "1";
								} else {
									Result[1] = "0";
								}
								if ("1".equals(topMess[5])) {
									Result[2] = "1";
								} else {
									Result[2] = "0";
								}
								if ("1".equals(topMess[8])) {
									Result[3] = "1";
								} else {
									Result[3] = "0";
								}
								if ("1".equals(topMess[11])) {
									Result[4] = "1";
								} else {
									Result[4] = "0";
								}
								if ("1".equals(topMess[14])) {
									Result[5] = "1";
								} else {
									Result[5] = "0";
								}
								
								Double sumcredit = 0.00;
								result = true;
								DecimalFormat df = new DecimalFormat("0.00"); 
								for (int i = 0; i < accnum; i++) {
//									if (Result[i + 1].equals("1")) { // Result 0不相符，1相符,// 对应checkflag// 2不相符，3相符
//										result = result && true;
//										accNoMainDataList.get(i).setCheckFlag("3");
//									} else {
//										accNoMainDataList.get(i).setCheckFlag("2");
//										result = result && false;
//									}
									AccNoMainData accNoMainData = accNoMainDataList.get(i);
									String accno = accNoMainData.getAccNo();
									String acccredit = CharReplaceUtils.StrReplace(df.format(accNoMainData.getCredit()));
									boolean isbreak = false;
									for(int j=0;j<5;j++){
										if(accno.equals(topMess[j+2*j])){
											isbreak = true;
											if(CompareUtils.compateStr(topMess[j+2*j+1], acccredit)){
												//如果余额一直 则相符
												result = result && true;
												accNoMainDataList.get(i).setCheckFlag("3");
												serviceForRemote.debug("账号为："+accNoMainDataList.get(i).getAccNo()+"的账户余额和对账单金额一致",String.valueOf(OperLogModuleDefine.autoOCR));
											}else{
												//如果余额不一直 则不相符
												accNoMainDataList.get(i).setCheckFlag("2");
												result = result && false;
												serviceForRemote.debug("账号为："+accNoMainDataList.get(i).getAccNo()+"的账户余额和对账单金额不一致",String.valueOf(OperLogModuleDefine.autoOCR));
											}
										}
									}
									
									if(!isbreak){
										//遍历问 纸质对账单没有的账户 为不相符处理
										accNoMainDataList.get(i).setCheckFlag("2");
										result = result && false;
									}
									
									
									// 设置Description字段
									try {
										docSet.setDescItem("accno_" + i,
												accNoMainDataList.get(i).getAccNo());
										docSet.setDescItem("accson_" + i,
												accNoMainDataList.get(i).getAccNoSon());
										docSet.setDescItem("checkflag_" + i,
												accNoMainDataList.get(i).getCheckFlag());
									} catch (Exception e) {

									}
									sumcredit = sumcredit + accNoMainData.getCredit();
								}
								serviceForRemote
										.batchUpdateAccNoMainData(accNoMainDataList);
								docSet.setCredit(sumcredit);
							} catch (Exception e) {
								result = false;
								e.printStackTrace();
							}
						} else {
							serviceForRemote.debug("9、识别出的对账单编号和系统对账单不一致,做不相符处理.",String.valueOf(OperLogModuleDefine.autoOCR));
						}
						
						
						

						
					}else{
						//没有识别对账单编号
						result = false;
						serviceForRemote.debug("识别获得的账单条码不是数字或者长度不正确",String.valueOf(OperLogModuleDefine.autoOCR));
					}
				}
			}			
		}
		
		return result;
	}

	/**
	 * 解析OCR结果 返回结果字符串，如下例： 1110301200100016420$1$11$00$00$00$00$22$22$22
	 * 1110301200100016420为识别到的条码; 1为存在未达(若为0则不存在未达，2为不能正常定位);
	 * 后边的分别记录8个账号的相符不符的识别结果 00,10代表相符，其他代表不相符。
	 * 
	 * @param voucherno
	 *            账单编号
	 * @return
	 * @throws RemoteException 
	 * @throws XDocProcException
	 */

	private boolean parserOCRResult(String ocrStrResult, String idcenter) throws RemoteException {
		if (ocrStrResult == null) {
			return false;
		}
		String strBillNo = "";
		String[] strTemp = ocrStrResult.split("\\$");
		boolean billRusult = false;// 解析识别结果,最终的通过不通过

		if (strTemp.length == 10) {
			strBillNo = strTemp[0];
			serviceForRemote.debug("识别获取的账单编号为：" + strBillNo,String.valueOf(OperLogModuleDefine.autoOCR));
			if (strBillNo.length() == 20) {
				if (getCheckMaindata(strBillNo)) {
					if (!idcenter.equals(checkmaindata.getIdCenter())) {
						return false;// 不是本分行的账单直接不通过
					}
					docSet.setVoucherNo(strBillNo);
					docSet.setDocDate(checkmaindata.getDocDate());
					docSet.setIdBank(checkmaindata.getIdBank());
					docSet.setIdBranch(checkmaindata.getIdBranch());
					docSet.setIdCenter(checkmaindata.getIdCenter());
					docSet.setAccName(checkmaindata.getAccName());
					docSet.setBankName(checkmaindata.getBankName());
					List<AccNoMainData> accNoMainDataList = new ArrayList<AccNoMainData>();
					try {
						accNoMainDataList = serviceForRemote
								.getAccnoMainDataByVoucherNo(strBillNo);
						int accnum = accNoMainDataList.size();
						serviceForRemote.debug("获取对应账单账号数量为：" + accnum,false);

						String[] Result = new String[9];
						if (strTemp[2].equals("00") || strTemp[2].equals("10")) {
							Result[1] = "1";
						} else {
							Result[1] = "0";
						}
						if (strTemp[3].equals("00") || strTemp[3].equals("10")) {
							Result[2] = "1";
						} else {
							Result[2] = "0";
						}
						if (strTemp[4].equals("00") || strTemp[4].equals("10")) {
							Result[3] = "1";
						} else {
							Result[3] = "0";
						}
						if (strTemp[5].equals("00") || strTemp[5].equals("10")) {
							Result[4] = "1";
						} else {
							Result[4] = "0";
						}
						if (strTemp[6].equals("00") || strTemp[6].equals("10")) {
							Result[5] = "1";
						} else {
							Result[5] = "0";
						}
						if (strTemp[7].equals("00") || strTemp[7].equals("10")) {
							Result[6] = "1";
						} else {
							Result[6] = "0";
						}
						if (strTemp[8].equals("00") || strTemp[8].equals("10")) {
							Result[7] = "1";
						} else {
							Result[7] = "0";
						}
						if (strTemp[9].equals("00") || strTemp[9].equals("10")) {
							Result[8] = "1";
						} else {
							Result[8] = "0";
						}
						Double sumcredit = 0.00;
						billRusult = true;
						for (int i = 0; i < accnum; i++) {
							if (Result[i + 1].equals("1")) { // Result 0不相符，1相符,
																// 对应checkflag
																// 2不相符，3相符
								billRusult = billRusult && true;
								accNoMainDataList.get(i).setCheckFlag("3");
							} else {
								accNoMainDataList.get(i).setCheckFlag("2");
								billRusult = billRusult && false;
							}
							// 设置Description字段
							try {
								docSet.setDescItem("accno_" + i,
										accNoMainDataList.get(i).getAccNo());
								docSet.setDescItem("accson_" + i,
										accNoMainDataList.get(i).getAccNoSon());
								docSet.setDescItem("checkflag_" + i,
										accNoMainDataList.get(i).getCheckFlag());
							} catch (Exception e) {

							}
							// 计算汇总金额
							// update by lishuiye on 2013-6-8: 添加外币汇率计算
							AccNoMainData accNoMainData = accNoMainDataList
									.get(i);
							// 得到汇率
							double exchangeRate = serviceForRemote
									.getExchangeRate(accNoMainData
											.getCurrtype());
							sumcredit = sumcredit + accNoMainData.getCredit()
									* exchangeRate;
						}
						serviceForRemote
								.batchUpdateAccNoMainData(accNoMainDataList);
						docSet.setCredit(sumcredit);
					} catch (Exception e) {
						billRusult = false;
						e.printStackTrace();
					}
				} else {
					serviceForRemote.debug("找不到对应的账单数据记录",false);
				}
			} else {
				billRusult = false;
				serviceForRemote.debug("识别获得的账单条码不是数字或者长度不正确",false);
			}
		} else {
			billRusult = false;
			serviceForRemote.debug("识别获的信息域数目不正确",false);
		}
		return billRusult;
	}

	private boolean getCheckMaindata(String strBillNo) {
		boolean existCheck = true;
		checkmaindata = null;
		try {
			checkmaindata = serviceForRemote.queryCheckMainData(strBillNo);
			if (checkmaindata != null) {
				existCheck = true;
			} else {
				existCheck = false;
			}
		} catch (Exception e) {
			existCheck = false;
			e.printStackTrace();
		}

		return existCheck;
	}
	
	/**
	 * 初始化控件  成功 返回0  失败为其他值
	 * @return
	 * @throws RemoteException 
	 */
	private boolean InitOcx(JacobLoader loader) throws RemoteException{
		boolean result = false;
		
		try{
			String init = loader.doMethod("InitCtrl", new Object[]{});
			int i = Integer.valueOf(init);
			if(i==0){
				result= true;
			}
		}catch(Exception e){
			serviceForRemote.error("OCR控件 InitCtrl 方法出错 ",false);
		}
		if(result){
			//加载OCR文件
			try{
				String setinit = loader.doMethod("SetXmlFile", new Object[]{"C://HYXJDZD.xml"});
				int j = Integer.valueOf(setinit);
				if(j==0){
					result= true;
				}
			}catch(Exception e){	
				serviceForRemote.error("OCR控件 SetXmlFile 方法出错 ",false);
			}
		}	
		return result;
	} 
	
	/**
	 * 读取影像文件 识别影像文件 
	 * @param loader
	 * @param filePath  文件的全路径
	 * @param fileTop 文件的正反面   1 正面  2 反面
	 * @return 返回OCR结果
	 * @throws RemoteException 
	 */
	private String readYingXFile(JacobLoader loader,String filePath,String fileTop) throws RemoteException{
		String result = null;
		StringBuffer sb = new StringBuffer();
		try{
			loader.doMethod("OcrPro", new Object[]{filePath,fileTop});
			String lengthstr = loader.doMethod("GetAreaCount", new Object[]{});
			int length = Integer.valueOf(lengthstr);
			for (int i=1;i<=length;i++)
			{
				String strValue = loader.doMethod("GetAreaResult", new Object[]{i});
				if("".equals(strValue)){
					strValue = " ";
				}
				sb.append(strValue+"$");
			}
		}catch(Exception e){
			serviceForRemote.error("OCR控件 readYingXFile方法出错 ",false);
		}
		
		if("1".equals(fileTop)){
			result = CharReplaceUtils.CharReplace(sb.toString());
		}else{
			result = sb.toString();
		}
		
		return result;
	}
	
	public IJobRegister getJobRegister() {
		return jobRegister;
	}

	public void setJobRegister(IJobRegister jobRegister) {
		this.jobRegister = jobRegister;
	}

	public IServiceForRemote getServiceForRemote() {
		return serviceForRemote;
	}

	public void setServiceForRemote(IServiceForRemote serviceForRemote) {
		this.serviceForRemote = serviceForRemote;
	}

	public IParamManager getParamManager() {
		return paramManager;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	/**
	 * 拿条形码  条形码 是15,16,17,18中 只要有一个有值就行
	 * @param topMess
	 * @return
	 */
	private String getTXM(String[] topMess){
		String result = "";
		for(int i=15;i<topMess.length;i++){
			if(topMess[i].length()>2){
				result =  topMess[i];
				break;
			}
		}
		return result;
	}

}
