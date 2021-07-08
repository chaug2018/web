package com.yzj.ebs.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.yzj.ebs.common.ISpecialFaceToFace;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.SpecialFaceToFace;

/**
 * 创建于:2015-12-31<br>
 * 版权所有(C) 2015 深圳市银之杰科技股份有限公司<br>
 * SpecialFaceToFace表操作访问服务
 * 
 * */

public class SpecialFaceToFaceAdm extends BaseService<SpecialFaceToFace> implements ISpecialFaceToFace{
	
	@SuppressWarnings("unchecked")
	public boolean ifExistInBasicInfo(String accno) throws XDocProcException{
		String hql = "from BasicInfo where accNo = '"+accno+"' and isCheck='0'";
		List<BasicInfo> list = new ArrayList<BasicInfo>();
		try {
			list = (List<BasicInfo>) dao.findByHql(hql);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		
		if(list!=null && list.size()>0){
			return true;
		}else return false;
	}
	
	@SuppressWarnings("unchecked")
	public boolean ifExistInSpecialFaceToFace(String accno,String docdate) throws XDocProcException{
		String hql = "from SpecialFaceToFace where accno = '"+accno+"' and docDate='"+docdate+"'";
		List<SpecialFaceToFace> list = new ArrayList<SpecialFaceToFace>();
		try {
			list = (List<SpecialFaceToFace>) dao.findByHql(hql);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		
		if(list!=null && list.size()>0){
			return true;
		}else return false;
	}
	
	/**
	 * 特殊账户信息表增加信息
	 * @throws  
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void addInfo(SpecialFaceToFace s)throws XDocProcException{
			dao.saveOrUpdate(s);
	}
	
	@SuppressWarnings("unchecked")
	public List<SpecialFaceToFace> getDataList(Map<String,String> queryMap,PageParam param) throws XDocProcException{
		List<SpecialFaceToFace> list = new ArrayList<SpecialFaceToFace>();
		
		String hql="from SpecialFaceToFace where 1=1 ";
		
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					hql+=" and "+entry.getKey()+" = '"+entry.getValue()+"' ";
				}
			}
		}
		
		hql+=" order by accno";
		
		list = (List<SpecialFaceToFace>) dao.findByHql(hql);
		
		Integer countNumber =list.size();   //得到数据的条数
		int pageSize = param.getPageSize();// 每页显示结果条数
		int totalPage = (int) ((countNumber - 1) / pageSize + 1);// 总页数
		int curPage = param.getCurPage();// 当前要显示的页
		if (curPage > totalPage) {
			curPage = totalPage;
		}
		int firstResult = (curPage - 1) * pageSize;// 分页时显示的第一条记录，默认从0开始	
		param.setFirstResult(firstResult);
		param.setCurPage(curPage);
		param.setTotal((int) countNumber);     //总共几条
		param.setTotalPage(totalPage);
		if((countNumber - firstResult)>pageSize){
			param.setLastResult(firstResult + pageSize);// 当前页显示的最后一条记录
		}else{
			param.setLastResult( countNumber);// 当前页显示的最后一条记录
		}	
		int lastNumber = countNumber - firstResult;
		
		if(lastNumber >= pageSize){
			return	list.subList(firstResult, firstResult+pageSize); 
		}else{
			return	list.subList(firstResult, firstResult+lastNumber);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SpecialFaceToFace> getAllDataList(Map<String,String> queryMap) throws XDocProcException{
		List<SpecialFaceToFace> list = new ArrayList<SpecialFaceToFace>();
		
		String hql="from SpecialFaceToFace where 1=1 ";
		
		if (queryMap != null) {
			for (Map.Entry<String, String> entry : queryMap.entrySet()) {
				if (entry.getValue() != null && entry.getValue().trim().length() != 0) {
					hql+=" and "+entry.getKey()+" = '"+entry.getValue()+"' ";
				}
			}
		}
		
		hql+=" order by accno";
		
		list = (List<SpecialFaceToFace>) dao.findByHql(hql);
		return list;
	}
	
	
	/**
	 * 特殊账户信息表删除信息s
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void deleteInfo(String accno,String docdate)throws XDocProcException{
			String sql = "delete SpecialFaceToFace where accno = '"+accno+"' and docDate='"+docdate+"'";
			try{
				dao.ExecQuery(sql);
			}catch  (Exception e) {
				throw new XDocProcException("执行查询 " + sql + " 失败", e);
			}
		
	}
	
}
