package com.yzj.ebs.ibank.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.yzj.ebs.service.bd.trade2103.RequestBodyHrxj;
import com.yzj.ebs.service.bd.trade2103.RequestService;
import com.yzj.ebs.service.bd.trade2103.ResponseBodyArray;
import com.yzj.ebs.service.bd.trade2103.ResponseBodyHrxj;
import com.yzj.ebs.service.bd.trade2103.ResponseService;
import com.yzj.ebs.service.bd.trade2103.ResponseTaskInfo;
import com.yzj.ebs.service.common.header.RequestSysHead;
import com.yzj.ebs.service.common.header.ResponseRet;
import com.yzj.ebs.service.common.header.ResponseSysHead;
import com.yzj.ebs.service.common.header.ResponseSysHeadArray;
import com.yzj.ebs.service.utils.ServiceCommonTool;
import com.yzj.ebs.service.utils.WSFinalConstant;
import com.yzj.ebs.service.utils.XMLAndObjectConvert;
import com.yzj.wf.common.WFLogger;

public class Trade2103 {
	private com.yzj.wf.common.WFLogger logger = WFLogger.getLogger(getClass());
	
	public byte[] execRequest(String request,IBankAdm ibankAdm,StrToFile strToFile) throws Exception {
		logger.info("账单信息查询请求报文：" + request);
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
		ResponseTaskInfo[] taskInfoArrayInit =new ResponseTaskInfo[] {};
		responseBodyArray.setAccNoArry(taskInfoArrayInit);
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
				logger.info("账单信息查询解析请求报文出现异常：" + e.getMessage());
				throw new JAXBException("账单信息查询解析请求报文出现异常：" + e.getMessage());
			}
		try { 
			logger.info("开始获取请求信息...");
			String stmtNo = requestBodyHrxj.getStmtNo().trim();
			String docDt = requestBodyHrxj.getDocDt().trim();
		
			if(stmtNo!=null && "".equals(stmtNo) && docDt!=null && "".equals(docDt) ) {
				List<Map<String,String>> list =new ArrayList<Map<String,String>>();
				list = ibankAdm.queryAccnoMainData(stmtNo,docDt);
				if(list.size()!=0) {
					//返回头
					responseRet.setReturnCode(WSFinalConstant.WSCODE000000);
					responseRet.setReturnMsg("查询成功！");
					responseSysHeadArray.setReponseRet(responseRet);
					responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
					
					ResponseTaskInfo[] taskInfoArray =new ResponseTaskInfo[list.size()];
					for(int i=0;i<list.size();i++) {
						
						taskInfo.setStmtNo(list.get(i).get("voucherno")==null?"":list.get(i).get("voucherno"));
						taskInfo.setChkRslt(list.get(i).get("checkflag")==null?"":list.get(i).get("checkflag"));
						taskInfo.setDocDt(list.get(i).get("docdate")==null?"":list.get(i).get("docdate"));
						taskInfo.setAccNm(list.get(i).get("accname")==null?"":list.get(i).get("accname"));
						taskInfo.setAccNo(list.get(i).get("accno")==null?"":list.get(i).get("accno"));
						taskInfo.setAmt(list.get(i).get("credit")==null?"":list.get(i).get("credit"));
						taskInfo.setCcy(list.get(i).get("currtype")==null?"":list.get(i).get("currtype"));
						taskInfoArray[i]=taskInfo;
					}
					//返回body
					responseBodyArray.setAccNoArry(taskInfoArray);
					responseBodyHrxj.setReponseBodyArray(responseBodyArray);
					responseService.setResponseBodyHrxj(responseBodyHrxj);
					responseService.setResponseSysHead(responseSysHead);
				}else {
					responseRet.setReturnCode(WSFinalConstant.WSCODE000002);
					responseRet.setReturnMsg("传入的参数查询不到数据！");
					responseSysHeadArray.setReponseRet(responseRet);
					responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
				}
			}else {
				responseRet.setReturnCode(WSFinalConstant.WSCODE000004);
				responseRet.setReturnMsg("账单编号："+stmtNo + "账单日期："+docDt+",参数异常！");
				responseSysHeadArray.setReponseRet(responseRet);
				responseSysHead.setResponseSysHeadArray(responseSysHeadArray);
				logger.info("账单编号："+stmtNo + "账单日期："+docDt+",参数异常！");
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
