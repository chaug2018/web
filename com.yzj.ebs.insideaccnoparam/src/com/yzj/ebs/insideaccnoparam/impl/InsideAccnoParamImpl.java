package com.yzj.ebs.insideaccnoparam.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.yzj.ebs.common.IInsideAccnoParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InsideAccnoParam;
import com.yzj.ebs.insideaccnoparam.biz.InsideAccnoParamBiz;

public class InsideAccnoParamImpl  implements   InsideAccnoParamBiz  {
	
	private IInsideAccnoParam  insideAccnoParamAdm;
	
	/**
	 * 增加 内部账户和用户 关联信息
	 * 返回   1 编辑成功  2 记录已存在   0 inner账号不存在    4 custId不存在  5 recheckCustId不存在   6 custId和recheckCustId不是同一部门
	 */
	public int inputInsideInfor(String accNo, String custId,String recheckCustId)throws XDocProcException, SQLException{
		//accno不存在的情况下 才能加 记录 
		if(insideAccnoParamAdm.ifExistAccnoInParam(accNo) == 0){
			// 表ebs_inneraccno 中有accno的记录 才加记录
			if(insideAccnoParamAdm.checkInnerAccNo(accNo)>0){
				//检测custid是否在po_peopleinfo表中有记录
				if(insideAccnoParamAdm.checkCustId(custId)>0){
					//检测recheckCustId是否在po_peopleinfo表中有记录
					if(insideAccnoParamAdm.checkCustId(recheckCustId)>0){
						//检测custId和recheckCustId是同一部门
						if(insideAccnoParamAdm.checkCustIdIDBank(custId,recheckCustId)>0){
							InsideAccnoParam inside = new InsideAccnoParam();
							inside.setAccNo(accNo);
							inside.setCustId(custId);
							inside.setRecheckCustId(recheckCustId);
							inside.setFlog("0");
							insideAccnoParamAdm.putInfor( inside);
							return 1;
						}else {
							return 6;
						}
					}else{
						return 5;
					}
				}else{
					return 4;
				}
			}else{
				return 0;
			}
		}else{
				return 2;
		}
	}
	
	/**
	 * 删除内部账户账户和用户 关联信息
	 * @throws SQLException 
	 */
	 
	public int deleteInsideInfor(String accNo, String custId,String recheckCustId) throws SQLException {
		insideAccnoParamAdm.deleteInfor(accNo ,custId ,recheckCustId);
		return 1;
	}
	/**
	 * 修改内部账户账户和 用户关联 信息
	 * 返回   1 编辑成功  0 记录已存在   2inner账号不存在   
	 * @throws Exception 
	 * @throws SQLException 
	 */
	
	public int changeInsideInfor(String accNo1, String custId,String accNo2) throws SQLException,XDocProcException {
		// 账号和custid不存在在的 情况下 才能修改
		if(insideAccnoParamAdm.ifExistAccnoInParam(accNo1) == 0){
			//innerbasic有账号信息才能修改
			if(insideAccnoParamAdm.checkInnerAccNo(accNo1)>0){
				return insideAccnoParamAdm.changeInfor(accNo1 ,custId, accNo2);
			}else{
				return 0;
			}
		}else{
				return 2;
		}
	}
	/**
	 * 查询内部账户账信息
	 */
	public List<InsideAccnoParam> queryInsideInfor(Map<String, String> queryMap,
			PageParam param)throws XDocProcException{
		
		return insideAccnoParamAdm.getInfor(queryMap, param);
	}
	
	
	/*
	 * set  get
	 */
	public IInsideAccnoParam getInsideAccnoParamAdm() {
		return insideAccnoParamAdm;
	}
	public void setInsideAccnoParamAdm(IInsideAccnoParam insideAccnoParamAdm) {
		this.insideAccnoParamAdm = insideAccnoParamAdm;
	}
	
}
