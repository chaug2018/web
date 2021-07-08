/**
 * Trade6004.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:Jiangzhengqiu 2012-11-8
 */
package com.yzj.ebs.impl.socketservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import com.yzj.common.file.service.FileOperatorService;
import com.yzj.common.file.service.IFileOperator;
import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.CustomerSignInfo;
import com.yzj.wf.com.ibank.common.TradeSet;
import com.yzj.wf.com.ibank.common.server.IBankProcessException;

/**
 * 创建于:2012-11-8<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 签约接口
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public class Trade6004 {
	
	public TradeSet execTrade(TradeSet tradeSet,Element element,IBaseService<Object> baseService,Document doc,String unUploadPath,String ftpPath) throws IBankProcessException {


		// 交易执行结果
		Element result = element.addElement("RSPCD");
		// 返回失败消息，默认为空
		Element error = element.addElement("RSPMSG");
		String errMsg = "";
		// 查询对账数据
		if (element.element("TRNDT") == null
				|| StringUtils.isEmpty(element.element("TRNDT")
						.getStringValue())) {
			errMsg = "对账日期不能为空！";
			error.addText(errMsg);
			if (doc != null) {
				tradeSet.getUpParams().put("OUTPUT",
						new StickerProcessor().checkMessageLeng(doc.asXML()));
			}
			return tradeSet;
		} else {
			String signDate = element.element("TRNDT").getStringValue();
			// 判断日期格式
			try {
				new SimpleDateFormat("yy-MM-dd").parse(signDate);
				// 根据日期查询签约信息
				try {
					StringBuffer sb = new StringBuffer("");
					List<CustomerSignInfo> list = (List<CustomerSignInfo>) baseService
							.findByHql("from CustomerSignInfo where signdate = '"
									+ signDate + "'");
					if(list == null || list.size() < 1)
					{
						result.addText("000000");
						element.addElement("FILENAME");
						Element fileFlagE = element.addElement("FILEFLAG");
						fileFlagE.addText("0");
						element.addElement("FILEPATH");
					}else
					{
					for(CustomerSignInfo ci : list)
					{
						sb.append(ci.getBrcNo());
						sb.append(",");
						// 柜员号在签约表中没有 TODO
						sb.append(ci.getPeopleNo());
						sb.append(",");
						sb.append(ci.getSendMode());
						sb.append(",");
						sb.append(ci.getCounterID());
						sb.append(",");
						sb.append(ci.getSignID());
						sb.append(",");
						sb.append(ci.getSigndate());
						sb.append("\r\n");
					}
					String fileName = saveTxtFile(sb.toString(),unUploadPath);
					// ftp路径
					// 如果返回的文件名为空则没有导出的文件
					String isHaveFile = "1";
					if(StringUtils.isEmpty(fileName))
					{
						isHaveFile = "0";
					}
					Element fileNameE = element.addElement("FILENAME");
					fileNameE.addText(fileName);
					Element isHaveFileE = element.addElement("FILEFLAG");
					isHaveFileE.addText(isHaveFile);
					String filePath = ftpPath;
					// 上传FTP
					putFtp(fileName,unUploadPath,ftpPath);
					Element pathE = element.addElement("FILEPATH");
					pathE.addText(filePath);
					result.addText("000000");
				}
				} catch (XDocProcException e) {
					error.addText("出现异常!");
					if (doc != null) {
						tradeSet.getUpParams().put("OUTPUT",
								new StickerProcessor().checkMessageLeng(doc.asXML()));
					}
					e.printStackTrace();
					return tradeSet;
				}
				
			} catch (ParseException e) {
				errMsg = "对账日期格式不正确！";
				error.addText(errMsg);
				if (doc != null) {
					tradeSet.getUpParams().put("OUTPUT",
							new StickerProcessor().checkMessageLeng(doc.asXML()));
				}
				e.printStackTrace();
				return tradeSet;

			}
		}
		// 返回请求结果
	
	
		return tradeSet;
	}

	/***
	 * 将字符串保存txt文件
	 * 
	 * @return 将签约信息保存至临时文件，并返回文件名
	 */
	private String saveTxtFile(String fileFormat,String unUploadPath)
	{
		boolean isSave = true;
		String fileName = "accnosigndata_"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";
		  File fp=new File(unUploadPath+fileName);
	       PrintWriter pfp;
		try {
			pfp = new PrintWriter(fp);
			  pfp.print(fileFormat);
		       pfp.close();
		} catch (FileNotFoundException e) {
			isSave = false;
			e.printStackTrace();
		}
		if(isSave)
		{
			return fileName;
		}else
		{
			return "";
		}
	}
	
	/***
	 * 上传FTP
	 * @param fileName 文件名  unUploadPath 临时文件目录路径  ftpPath 上传路径
	 */
	private void putFtp(String fileName,String unUploadPath,String ftpPath)
	{
		try {
			IFileOperator fo = new FileOperatorService();
			// 取得当前文件
			InputStream in = fo.get(unUploadPath+fileName); 
			// 上传至FTP
			fo.put(in,ftpPath+fileName);
			fo.closeInputStream(in);
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
