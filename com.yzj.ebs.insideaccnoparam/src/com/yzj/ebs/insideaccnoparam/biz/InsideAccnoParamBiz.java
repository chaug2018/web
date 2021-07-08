package com.yzj.ebs.insideaccnoparam.biz;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.IBaseService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.InsideAccnoParam;
import com.yzj.ebs.impl.BaseService;
/**
 * 创建于:2013-08-16 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 *内部账户的信息维护
 * 
 * @author j_sun
 * @version 1.0.0
 */
public interface InsideAccnoParamBiz {
	
	/**
	 * 增加 内部账户账户信息
	 * 返回   1 编辑成功  2 记录已存在   0inner账号不存在   4 custId不存在   5 recheckCustId不存在   6 custId和recheckCustId不是同一部门
	 */
	public int inputInsideInfor(String accNo, String custId,String recheckCustId)throws XDocProcException, SQLException; 
	
	/**
	 * 删除内部账户账户信息
	 * return 0 增加失败  1 增加成功
	 */
	public int deleteInsideInfor(String accNo, String custId,String recheckCustId)throws SQLException;
	/**
	 * 修改内部账户账户信息
	 * 返回   1 编辑成功  0 记录已存在   2inner账号不存在   
	 */
	public int changeInsideInfor(String accNo1, String custId, String accNo2)throws SQLException,XDocProcException ;
	/**
	 * 查询内部账户账户新
	 */
	public List<InsideAccnoParam> queryInsideInfor(Map<String, String> queryMap,
			PageParam param)throws XDocProcException;
}
