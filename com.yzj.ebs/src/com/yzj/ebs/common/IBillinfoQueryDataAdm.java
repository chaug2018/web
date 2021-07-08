package com.yzj.ebs.common;

import java.util.List;
import java.util.Map;

import com.yzj.ebs.common.param.PageParam;
import com.yzj.ebs.database.BillinfoQueryData;

public interface IBillinfoQueryDataAdm extends IBaseService<BillinfoQueryData>{
	/**
	 * 
	 * @param sql
	 * @return 
	 */

	List<?> getBillinfoQueryData(Map<String, String> queryMap, final String queryType,
			PageParam pageParam) throws XDocProcException;
  /**
   * @
   * 
   */
	List<?> getAllBillinfoQueryData(Map<String, String> queryMap, final String queryType) throws XDocProcException;
}
