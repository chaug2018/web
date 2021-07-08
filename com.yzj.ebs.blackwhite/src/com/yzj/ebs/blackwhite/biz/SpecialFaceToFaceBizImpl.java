package com.yzj.ebs.blackwhite.biz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

import com.yzj.ebs.common.ISpecialFaceToFace;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.SpecialFaceToFace;
import com.yzj.wf.common.WFLogger;

/**
 * 创建于:2015-12-31<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * 特殊面对面账单维护 业务实现
 * 
 * @version 1.0.0
 * 
 */
public class SpecialFaceToFaceBizImpl implements ISpecialFaceToFaceBiz {

	
	private static WFLogger logger = WFLogger.getLogger(SpecialFaceToFaceBizImpl.class);
	
	private ISpecialFaceToFace specialFaceToFaceAdm;
	
	@Override
	public List<SpecialFaceToFace> getDataList(Map<String, String> queryMap,
			PageParam queryParam, boolean isPaged) throws XDocProcException {
		List<SpecialFaceToFace> queryList = new ArrayList<SpecialFaceToFace>();
		if (isPaged) {
			//分页查询
			queryList = specialFaceToFaceAdm.getDataList(queryMap, queryParam);
		} else {
			//导出
			queryList = specialFaceToFaceAdm.getAllDataList(queryMap);
		}
		return queryList;
	}
	/**
	 * 根据导入的excel 批量维护抽查账户
	 * 
	 */
	@SuppressWarnings("unused")
	public String completeBatchInput(File upFile) throws XDocProcException {
		String flag="";
		String msg = "";
		
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

		//读取文件
		Sheet sheet = book.getSheet(0);
		int rows = sheet.getRows(); // 总行数
		int columns = sheet.getColumns(); // 总列数 
		if ("账号".equals(sheet.getCell(0, 0).getContents().trim())
				&& "对账日期（yyyymmdd）".equals(sheet.getCell(1, 0).getContents().trim()) ) { // 检查表头是否正确
		} else {
			msg = "您导入的数据列名不正确！请检查后重新导入！";
			logger.error(msg);
			return msg;
		}
		
		String accNo = "";
		String docDate ="";
		
		for (int i = 1; i < rows; i++) {
			
			accNo = sheet.getCell(0, i).getContents().trim().toString();
			docDate = sheet.getCell(1, i).getContents().trim().toString();
			
			if(docDate==null || "".equals(docDate.trim())){
				msg = "对账日期未填写，放弃第"+i+"条信息";
				logger.error(msg);
				continue;
			}else if(docDate.trim().length()!=8){
				msg = "对账日期格式不正确，放弃第"+i+"条信息";
				logger.error(msg);
				continue;
			}
			
			if(accNo!=null && !"".equals(accNo.trim())){
				// 判断账号是否在basicinfo表中存在
				if(specialFaceToFaceAdm.ifExistInBasicInfo(accNo)){
					if(specialFaceToFaceAdm.ifExistInSpecialFaceToFace(accNo,docDate)){
						msg = "账号"+accNo+",日期"+docDate+"已存在，放弃第"+i+"条信息";
						logger.error(msg);
						continue;
					}else{
						SpecialFaceToFace s = new SpecialFaceToFace();
						s.setAccno(accNo);
						s.setDocDate(docDate);
						
						specialFaceToFaceAdm.addInfo(s);
					}
				}else{
					msg = "账号不存在或者账户不对账,放弃第"+i+"条信息";
					logger.error(msg);
					continue;
				}
			}else{
				msg = "账号未填写，放弃第"+i+"条信息";
				logger.error(msg);
				continue;
			}
		}
		
		return flag;
	}

	/**
	 * 删除信息
	 */
	public void deleteInfo(String accno,String docdate) throws XDocProcException{
		specialFaceToFaceAdm.deleteInfo(accno, docdate) ;
	}
	
	
	public ISpecialFaceToFace getSpecialFaceToFaceAdm() {
		return specialFaceToFaceAdm;
	}
	
	public void setSpecialFaceToFaceAdm(ISpecialFaceToFace specialFaceToFaceAdm) {
		this.specialFaceToFaceAdm = specialFaceToFaceAdm;
	}

}
