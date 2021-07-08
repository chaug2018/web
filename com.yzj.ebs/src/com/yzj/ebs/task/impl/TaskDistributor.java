
package com.yzj.ebs.task.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.ITaskDistributor;
import com.yzj.ebs.task.common.TaskConstDef.AppId;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.PeopleInfo;
import com.yzj.wf.core.model.po.common.POException;
import com.yzj.wf.core.service.po.IPeopleInfoAdm;
import com.yzj.wf.pam.common.IParamManager;
import com.yzj.wf.pam.db.BaseParam;
import com.yzj.wf.pam.query.ParamQuery;
/**
 * 
 * 创建于:2012-12-11<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 自定义任务分配器实现
 * @author 陈林江
 * @version 1.0
 */
public class TaskDistributor implements ITaskDistributor {
	
	private IPublicTools publicTools;
	private IDocSetAdm docSetAdm;
	private IPeopleInfoAdm peopleAdm;
	private IParamManager paramManager;
	private static final String Param_ModuleFlow="Param_ModuleFlow";
	private Map<String,BaseParam> paramsCache=new HashMap<String, BaseParam>();
	private static WFLogger logger=WFLogger.getLogger(TaskDistributor.class);

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getManualInputUsers(java.lang.String)
	 */
	@Override
	public List<String> getManualInputUsers(String businessKey)
			throws Exception {
		return this.getUsersByAppId(AppId.MANUAL_INPUT,businessKey);	
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getManualAuthUsers(java.lang.String)
	 */
	@Override
	public List<String> getManualAuthUsers(String businessKey) throws Exception {
		return this.getUsersByAppId(AppId.MANUAL_AUTH,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getProveFstUsers(java.lang.String)
	 */
	@Override 
	public List<String> getProveFstUsers(String businessKey) throws Exception {
		return this.getUsersByAppId(AppId.MANUALPROVE_FST,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getProveSndUsers(java.lang.String)
	 */
	@Override
	public List<String> getProveSndUsers(String businessKey) throws Exception {
		return this.getUsersByAppId(AppId.MANUALPROVE_SND,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getProveAuthUsers(java.lang.String)
	 */
	@Override
	public List<String> getProveAuthUsers(String businessKey) throws Exception {
		return this.getUsersByAppId(AppId.MANUALPROVE_AUTH,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getNotMatchInputUsers(java.lang.String)
	 */
	@Override
	public List<String> getNotMatchInputUsers(String businessKey)
			throws Exception {
		return this.getUsersByAppId(AppId.NOTMATCH_INPUT,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getNotMatchAuthUsers(java.lang.String)
	 */
	@Override
	public List<String> getNotMatchAuthUsers(String businessKey)
			throws Exception {
		return this.getUsersByAppId(AppId.NOTMATCH_AUTH,businessKey);
	}

	/* (non-Javadoc)
	 * @see com.yzj.ebs.task.common.ITaskDistributor#getDeleteAuthUsers(java.lang.String)
	 */
	@Override
	public List<String> getDeleteAuthUsers(String businessKey) throws Exception {
		return this.getUsersByAppId(AppId.DELETE_AUTH,businessKey);
	}
	
	
	/**
	 * 根据appId和业务号获取具有该模块访问权限且属于特定对账中心的人员列表
	 * @param appId
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	private List<String> getUsersByAppId(String appId,String businessKey)throws Exception{
		String moduleId=null;
		BaseParam bp=paramsCache.get(appId);
		if(bp==null){
//		DetachedCriteria dc = DetachedCriteria.forEntityName(Param_ModuleFlow);
//		dc.add(Restrictions.eq(BaseParam.getFiledName("appId"), appId));
//		List<BaseParam> bps=null;
//		List<BaseParam> bps = paramManager.getParamsByCon(dc);
		ParamQuery pq = new ParamQuery();
		pq.addEqPropery(BaseParam.getFiledName("appId"), appId)	;
		List<BaseParam> bps = paramManager.getParamByCondition(Param_ModuleFlow, pq);
		if(bps!=null&&bps.size()!=0){
		  bp=bps.get(0);
		  paramsCache.put(appId, bp);
		}else{
			return null;
		}
		}		
		moduleId=bp.getExtField("moduleID").toString();
        List<PeopleInfo> infos=this.getUsersByModuleId(moduleId);
		DocSet docSet=docSetAdm.queryOneByID(Long.valueOf(businessKey));
		String idcenter=null;
		if(docSet!=null){
			idcenter=docSet.getIdCenter();
		}else{
			idcenter=AppTaskAdm.idCenterMap.get(businessKey);
		}
		if(idcenter==null){
			throw new Exception("未获取到对账中心号，无法进行任务分配!");
		}
		return this.getUserByIdcenter(idcenter,infos);
	}
	
	/**
	 * 获取对该模块有访问权限的用户
	 * @param moduleId 模块号
	 * @return 人员集合
	 */
	private List<PeopleInfo> getUsersByModuleId(String moduleId){
		List<PeopleInfo> result=null;
		try {
			result= peopleAdm.getPeoplesByResourceId(moduleId);
		} catch (POException e) {
		logger.error("根据模块id获取人员信息出现错误",e);
		}
		return result;
	}
	
	/**
	 * 根据对账中心对人员进行过滤,返回人员列表
	 * @param idCener 对账中心号
	 * @param users 过滤前的人员
	 * @return 过滤后的人员
	 */
	private List<String> getUserByIdcenter(String idCenter,List<PeopleInfo> users)throws Exception{
		List<String> result=new ArrayList<String>();
		Map<String,BankParam> map=publicTools.getAllBankParam();
		List<String> orgSidList=new ArrayList<String>();
		Set<String> keys=map.keySet();
		for (String string : keys) {
			if(map.get(string).getIdCenter().equals(idCenter)){ //对账中心或是其的子机构
				orgSidList.add(map.get(string).getOrgSid());
			}
		}
		for(int i=0;i<users.size();i++){
			PeopleInfo user=users.get(i);
			if(orgSidList.contains(user.getOrganizeInfo())){
				result.add(user.getSid());
			}
		}
		return result;
		
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}

	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}


	public IParamManager getParamManager() {
		return paramManager;
	}

	public IPeopleInfoAdm getPeopleAdm() {
		return peopleAdm;
	}

	public void setPeopleAdm(IPeopleInfoAdm peopleAdm) {
		this.peopleAdm = peopleAdm;
	}

	public void setParamManager(IParamManager paramManager) {
		this.paramManager = paramManager;
	}

	
}
