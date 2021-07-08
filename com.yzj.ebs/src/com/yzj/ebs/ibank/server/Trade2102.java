package com.yzj.ebs.ibank.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.yzj.ebs.service.bd.trade2102.RequestBodyHrxj;
import com.yzj.ebs.service.bd.trade2102.RequestService;
import com.yzj.ebs.service.bd.trade2102.ResponseBodyArray;
import com.yzj.ebs.service.bd.trade2102.ResponseBodyHrxj;
import com.yzj.ebs.service.bd.trade2102.ResponseService;
import com.yzj.ebs.service.bd.trade2102.ResponseTaskInfo;
import com.yzj.ebs.service.common.header.RequestSysHead;
import com.yzj.ebs.service.common.header.ResponseRet;
import com.yzj.ebs.service.common.header.ResponseSysHead;
import com.yzj.ebs.service.common.header.ResponseSysHeadArray;
import com.yzj.ebs.service.utils.ServiceCommonTool;
import com.yzj.ebs.service.utils.WSFinalConstant;
import com.yzj.ebs.service.utils.XMLAndObjectConvert;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;
import com.yzj.wf.common.WFLogger;

public class Trade2102 {
	private com.yzj.wf.common.WFLogger logger = WFLogger.getLogger(getClass());
	
	public byte[] execRequest(String request,IBankAdm ibankAdm,StrToFile strToFile) throws Exception {
		logger.info("账单列表信息查询请求报文：" + request);
		RequestService requestService=null;
		RequestSysHead requestSysHead=null;
		RequestBodyHrxj requestBodyHrxj=null;
		//初始化返回报文实体
		ResponseService responseService = new ResponseService();
		ResponseSysHead responseSysHead = new ResponseSysHead();
		ResponseBodyHrxj responseBodyHrxj = new ResponseBodyHrxj();
		ResponseBodyArray responseBodyArray = new ResponseBodyArray();
		
		ResponseTaskInfo taskInfo = new ResponseTaskInfo();
		ResponseSysHeadArray responseSysHeadArray = new ResponseSysHeadArray();
		ResponseRet responseRet = new ResponseRet();
		responseSysHeadArray.setReponseRet(responseRet);
		ResponseTaskInfo[] taskInfoArrayInit =new ResponseTaskInfo[] {};
		responseBodyArray.setBillDataArry(taskInfoArrayInit);
		responseBodyHrxj.setReponseBodyArray(responseBodyArray);
		responseService.setResponseBodyHrxj(responseBodyHrxj);
		responseService.setResponseSysHead(responseSysHead);
		try {
		logger.info("开始组装请求报文对象...");
		requestService = (RequestService)XMLAndObjectConvert.createObjectByXML(request, RequestService.class);
    	requestSysHead = requestService.getRequestSysHead();
		requestBodyHrxj=requestService.getRequestBodyHrxj();
		ServiceCommonTool sct = new ServiceCommonTool();
		responseSysHead = sct.createResponseHead(requestSysHead);
		responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
		}catch (Exception e) {
			logger.info("账单列表信息查询解析请求报文出现异常：" + e.getMessage());
			throw new JAXBException("账单列表信息查询解析请求报文出现异常：" + e.getMessage());
		}
		try { 
			logger.info("开始获取请求信息...");
			String accNo = requestBodyHrxj.getAccNo().trim();
			String flg = requestBodyHrxj.getFlg().trim();
			String beginDt = requestBodyHrxj.getBeginDt().trim();
			String endDt = requestBodyHrxj.getEndDt().trim();
			String startNum = requestBodyHrxj.getStartNum().trim();
			String QueryNum = requestBodyHrxj.getQueryNum().trim();
			if(accNo!=null && "".equals(accNo) && flg!=null && "".equals(flg) 
					&& beginDt!=null && "".equals(beginDt) && endDt!=null && "".equals(endDt)
					&& startNum!=null && "".equals(startNum) && QueryNum!=null && "".equals(QueryNum)) {
				List<Map<String,String>> list =new ArrayList<Map<String,String>>();
				int num=0;
				//查询标识1：未完成对账账单查询2：所有账单查询
				if("1".equals(flg)) {
					num = ibankAdm.queryNocheckCount(accNo,beginDt,endDt);
					list = ibankAdm.queryNocheckVoucherno(accNo,beginDt,endDt,startNum,QueryNum);
					
				}else if("2".equals(flg)){
					num = ibankAdm.queryAllCount(accNo,beginDt,endDt);
					list = ibankAdm.queryAllVoucherno(accNo,beginDt,endDt,startNum,QueryNum);
				}else {
					responseRet.setReturnCode(WSFinalConstant.WSCODE000004);
					responseRet.setReturnMsg("查询标识为"+flg+"，参数值异常！");
					responseSysHeadArray.setReponseRet(responseRet);
					responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
				}
				if("1".equals(flg)||"2".equals(flg)) {
					responseRet.setReturnCode(WSFinalConstant.WSCODE000000);
					responseRet.setReturnMsg("查询成功！");
					responseSysHeadArray.setReponseRet(responseRet);
					responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
					logger.info("查询成功");
					if(list.size()!=0) {
						ResponseTaskInfo[] taskInfoArray =new ResponseTaskInfo[list.size()];
						for(int i=0;i<list.size();i++) {
							
							taskInfo.setStmtNo(list.get(i).get("voucherno")==null?"":list.get(i).get("voucherno"));
							taskInfo.setStmtFlg(list.get(i).get("docstate")==null?"":list.get(i).get("docstate"));
							taskInfo.setDocDt(list.get(i).get("docdate")==null?"":list.get(i).get("docdate"));
							taskInfo.setAccNm(list.get(i).get("accname")==null?"":list.get(i).get("accname"));
							taskInfo.setAdr(list.get(i).get("address")==null?"":list.get(i).get("address"));
							taskInfo.setCtcPrsn(list.get(i).get("linkman")==null?"":list.get(i).get("linkman"));
							taskInfo.setCtcTel(list.get(i).get("phone")==null?"":list.get(i).get("phone"));
							taskInfoArray[i]=taskInfo;
						}
						responseBodyArray.setBillDataArry(taskInfoArray);
						responseBodyHrxj.setReponseBodyArray(responseBodyArray);
						responseBodyHrxj.setTotalNum(num+"");
						responseService.setResponseBodyHrxj(responseBodyHrxj);
						responseService.setResponseSysHead(responseSysHead);
					}
				}
			}else {
				responseRet.setReturnCode(WSFinalConstant.WSCODE000004);
				responseRet.setReturnMsg("账号："+accNo + "查询标识："+flg+"起始日期："+beginDt+"结束日期："+endDt+"开始记录："+startNum+"查询条数："+QueryNum+"参数异常！");
				responseSysHeadArray.setReponseRet(responseRet);
				responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
				logger.info("账号："+accNo + "查询标识："+flg+"起始日期："+beginDt+"结束日期："+endDt+"开始记录："+startNum+"查询条数："+QueryNum+"参数异常！");
			}
			
		}catch (Exception e) {
			responseRet.setReturnCode(WSFinalConstant.WSCODE999999);
			responseRet.setReturnMsg("查询出现异常");
			responseSysHeadArray.setReponseRet(responseRet);
			responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
			logger.info("查询出现异常"+e.getMessage());
		}
		String str = null;
		logger.info("账单列表信息查询开始生成返回报文...");
		try {
			str = XMLAndObjectConvert.createXMLByObject(responseService, ResponseService.class);
			logger.info("账单列表信息查询返回报文："+str);
			return str.getBytes("UTF-8");
		} catch (JAXBException e) {
			logger.info("账单列表信息查询生成回应报文出现异常：" + e.getMessage());
			throw new JAXBException("账单列表信息查询生成回应报文出现异常：" + e.getMessage());
		}
		
	}

}
