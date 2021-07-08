package com.yzj.ebs.impl;

import java.rmi.RemoteException;
import java.util.List;

import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.IDocSetAdm;
import com.yzj.ebs.common.INewSealAdm;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.IServiceForRemote;
import com.yzj.ebs.common.ITallyService;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.database.DocSet;
import com.yzj.ebs.task.common.IAppTaskAdm;
import com.yzj.ebs.task.common.TaskConstDef.TaskResult;
import com.yzj.ebs.util.DataLogUtils;
import com.yzj.wf.bpm.engine.task.IBPMTask;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.core.model.po.wrapper.XPeopleInfo;

/**
 * 创建于:2012-8-24<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 远程服务实现
 * 
 * @author 秦靖锋， WangXue
 * @version 1.0.0
 */
public class ServiceForRemote implements IServiceForRemote {
	private CheckMainDataAdm checkMainDataAdm;
	private IAppTaskAdm appTaskAdm;
	private IDocSetAdm docSetAdm;
	private ITallyService tallyService;
	private IAccnoMainDataAdm accnoMainDataAdm;
	private INewSealAdm newSealAdm;

	private IPublicTools publicTools;

	/**
	 * 图像的相对路径，tomcat的映射路径，映射到存储的存储根目录
	 */
	private String imageRelativePath = "imageSave";

	private WFLogger logger = WFLogger.getLogger(ServiceForRemote.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.common.IServiceForRemote#getTask(java.lang.String,
	 * java.lang.String, java.lang.Object[])
	 */
	@Override
	public IBPMTask getTask(String appId, String userId, Object... extParams)
			throws XDocProcException, RemoteException {
		XPeopleInfo people = new XPeopleInfo();
		people.setPeopleCode(userId);
		people.setSid(userId);
		return appTaskAdm.getTask(appId, people, extParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.common.IServiceForRemote#commitTask(com.yzj.wf
	 * .bpm.engine.task.IBPMTask, java.lang.Object, java.lang.String,
	 * com.yzj.dps.task.common.TaskConstDef.TaskResult, java.lang.Object[])
	 */
	@Override
	public void commitTask(IBPMTask taskInfo, DocSet ywInfo, String userId,
			TaskResult taskResult, Object... extParams)
			throws XDocProcException, RemoteException {
		XPeopleInfo people = new XPeopleInfo();
		people.setPeopleCode(userId);
		people.setSid(userId);
		appTaskAdm.commitTask(taskInfo, ywInfo, people, taskResult, extParams);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yzj.dps.common.IServiceForRemote#queryDocSetByID(java.lang.
	 * Long)
	 */
	@Override
	public DocSet queryDocSetByID(Long docId) throws XDocProcException,
			RemoteException {
		return docSetAdm.queryOneByID(docId);
	}

	@Override
	public boolean tallyDebit(DocSet docSet) throws XDocProcException,
			RemoteException {
		// return tallyService.tallyDebit(docSet);暂不提供记账至主机或后台
		return checkMainDataAdm.upDateCheckmaindata(docSet);
	}

	public String getImageRelativePath() {
		return imageRelativePath;
	}

	public void setImageRelativePath(String imageRelativePath) {
		this.imageRelativePath = imageRelativePath;
	}

	public IAppTaskAdm getAppTaskAdm() {
		return appTaskAdm;
	}

	public void setAppTaskAdm(IAppTaskAdm appTaskAdm) {
		this.appTaskAdm = appTaskAdm;
	}

	public IDocSetAdm getDocSetAdm() {
		return docSetAdm;
	}

	public void setDocSetAdm(IDocSetAdm docSetAdm) {
		this.docSetAdm = docSetAdm;
	}

	public ITallyService getTallyService() {
		return tallyService;
	}

	public void setTallyService(ITallyService tallyService) {
		this.tallyService = tallyService;
	}

	/**
	 * @return the checkMainDataAdm
	 */
	public CheckMainDataAdm getCheckMainDataAdm() {
		return checkMainDataAdm;
	}

	/**
	 * @param checkMainDataAdm
	 *            the checkMainDataAdm to set
	 */
	public void setCheckMainDataAdm(CheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public CheckMainData queryCheckMainData(String voucherno)
			throws XDocProcException, RemoteException {
		return checkMainDataAdm.getOneByVoucherNo(voucherno);
	}

	public void batchUpdateAccNoMainData(List<AccNoMainData> list)
			throws XDocProcException {
		accnoMainDataAdm.batchUpdate(list);
	}

	public List<AccNoMainData> getAccnoMainDataByVoucherNo(String voucherno)
			throws XDocProcException {
		return accnoMainDataAdm.getAccnoMainDataByVoucherNo(voucherno);
	}

	public IAccnoMainDataAdm getAccnoMainDataAdm() {
		return accnoMainDataAdm;
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}

	public List<Object[]> getSealAccno(String voucherno)
			throws XDocProcException, Exception, RemoteException {
		return checkMainDataAdm.getSealAccno(voucherno);
	}

	@Override
	public List<Object> checkSealAccno(String sql) throws XDocProcException,
			Exception, RemoteException {
		return newSealAdm.findBySql(sql);
	}

	public INewSealAdm getNewSealAdm() {
		return newSealAdm;
	}

	public void setNewSealAdm(INewSealAdm newSealAdm) {
		this.newSealAdm = newSealAdm;
	}

	public IPublicTools getPublicTools() {
		return publicTools;
	}

	public void setPublicTools(IPublicTools publicTools) {
		this.publicTools = publicTools;
	}

	public double getExchangeRate(String currtype) throws RemoteException{
		try {
			return publicTools.getExchangeRate(currtype);
		} catch (XDocProcException e) {
			e.printStackTrace();
		}
		return 0;
	}


	@Override
	public void error(String OPDESC, Throwable t, boolean istoData) throws RemoteException {
		publicTools.getDataLogUtils().error(OPDESC, t, istoData);
		
	}

	@Override
	public void info(String OPDESC, Throwable t, boolean istoData) throws RemoteException {
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().info(OPDESC, t, istoData);
	}

	@Override
	public void error(String OPDESC, boolean istoData) throws RemoteException {
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().error(OPDESC, istoData);
	}

	@Override
	public void info(String OPDESC, boolean istoData) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().info(OPDESC, istoData);
	}

	@Override
	public void debug(String OPDESC, boolean istoData) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().debug(OPDESC, istoData);
	}

	@Override
	public void error(String OPDESC, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().error(OPDESC, OPMODE);
	}

	@Override
	public void info(String OPDESC, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().info(OPDESC, OPMODE);
	}

	@Override
	public void debug(String OPDESC, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().debug(OPDESC, OPMODE);
	}

	@Override
	public void info(String OPDESC, Throwable t, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().info(OPDESC, t, OPMODE);
	}

	@Override
	public void error(String OPDESC, Throwable t, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().error(OPDESC, t, OPMODE);
	}

	@Override
	public void debug(String OPDESC, Throwable t, String OPMODE) throws RemoteException{
		// TODO Auto-generated method stub
		publicTools.getDataLogUtils().debug(OPDESC, t, OPMODE);
	}


}
