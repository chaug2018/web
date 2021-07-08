package com.yzj.ebs.exchangebook.biz;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.ExChangeBook;
import com.yzj.ebs.exchangebook.action.QueryParam;

/**
 * 创建于:2013-04-11<br>
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司<br>
 * 账单交接登记薄业务层实现
 * 
 * @author jiangzhengqiu
 * @version 1.0.0
 */
public interface IExChangeBookBiz {
	
	/**
	 * 查询账单交接列表,按分页查询
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	public List<ExChangeBook> getExChangeBook(Map<String, String> queryMap,
			PageParam param,BankParam bankparam) throws XDocProcException;
	/**
	 * 查询账单交接列表,查询所有
	 * 
	 * @return List<ExChangeBook>
	 * @throws XDocProcException
	 */
	public List<ExChangeBook> getAllExChangeBook(Map<String, String> queryMap,BankParam bankparam);
	/**
	 * 确认当前登录柜员是否有权限查看录入账号的明细
	 * 
	 * @return
	 * @throws XDocProcException
	 */
	public boolean isQueryAllowed(QueryParam queryParam) throws XDocProcException ;
	public ExChangeBook create(ExChangeBook book) throws XDocProcException;

	
}
