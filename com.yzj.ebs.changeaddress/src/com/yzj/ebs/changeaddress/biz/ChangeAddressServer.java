package com.yzj.ebs.changeaddress.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.yzj.ebs.common.IBasicInfoAdm;
import com.yzj.ebs.common.IFaceFlugAdm;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BasicInfo;
import com.yzj.ebs.database.FaceFlug;
/**
 * 
 *创建于:2013-8-29<br>
 *版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 面对面 柜台 发送地址修改 实现
 * @author j_sun
 * @version 1.0.0
 */
public class ChangeAddressServer {

	private IFaceFlugAdm faceFlugAdm;
	private IBasicInfoAdm basicinfoAdm;
	
	/**
	 *  增加/修改发送地址FaceFlug记录
	 */
	public void addInfo(FaceFlug faceFlug){
		faceFlugAdm.addFaceFlug(faceFlug);
	}
	
	/**
	 * 删除指定的发送地址FaceFlug记录
	 * @param faceFlug
	 */
	public void deleteFaceFlug(FaceFlug faceFlug){
		faceFlugAdm.delete(faceFlug);
	}
	
	/**
	 * 根据传入的查询参数和分页参数查询对应的记录
	 * @param queryMap	查询参数
	 * @param pageParam	分页参数
	 * @return	List<FaceFlug> faceFlug对象集
	 * @throws Exception
	 */
	public List<FaceFlug> selectFaceFlugRecords(Map<String,String> queryMap,PageParam pageParam){
		return ((List<FaceFlug>) faceFlugAdm.selectRecords(queryMap,pageParam,"FaceFlug","idCenter"));
	} 
	
	/**
	 * 查找条目数
	 * @param queryMap
	 * @return
	 */
	public Long selectFaceFlugCount(Map<String,String> queryMap){
		Long result = 0L;
		try {
			result = faceFlugAdm.selectCount(queryMap,"FaceFlug","idCenter");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据传入的查询参数和分页参数分页查询出指定的BasicInfo记录
	 * @param queryMap	查询参数
	 * @param pageParam	分页参数
	 * @return	BasicInfo实体
	 */
	public List<BasicInfo> selectBasicInfoRecords(Map<String,String> queryMap,PageParam pageParam){
		return (List<BasicInfo>) faceFlugAdm.selectRecords(queryMap, pageParam, "BasicInfo", "idCenter");
	}
	
	/**
	 * 根据查询参数查询出符合要求的条目数
	 * @param queryMap	查询参数
	 * @return	记录数
	 */
	public Long selectBasicInfoCount(Map<String,String> queryMap){
		Long result = 0L;
		try {
			result = faceFlugAdm.selectCount(queryMap, "BasicInfo", "idCenter");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;
	}
	/**
	 * 根据传入的查询参数和分页参数查询BasicInfo记录
	 * @param queryMap	查询参数
	 * @param param		分页参数
	 * @return			BasicInfo记录集
	 */
	public List<BasicInfo> findBasciInfoData(Map<String,String> queryMap){
		List<BasicInfo> basicInfoList = new ArrayList<BasicInfo>();
		try {
			basicInfoList =  basicinfoAdm.getAllBasicInfo(queryMap);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return basicInfoList;
	}
	
	public IFaceFlugAdm getFaceFlugAdm() {
		return faceFlugAdm;
	}
	public void setFaceFlugAdm(IFaceFlugAdm faceFlugAdm) {
		this.faceFlugAdm = faceFlugAdm;
	}
	public IBasicInfoAdm getBasicinfoAdm() {
		return basicinfoAdm;
	}
	public void setBasicinfoAdm(IBasicInfoAdm basicinfoAdm) {
		this.basicinfoAdm = basicinfoAdm;
	}	
	
}
