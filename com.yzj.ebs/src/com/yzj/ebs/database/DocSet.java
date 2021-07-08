package com.yzj.ebs.database;

import java.util.List;

import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.util.UtilBase;
import java.lang.reflect.InvocationTargetException;
import net.sf.cglib.beans.BeanGenerator;
import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 * 创建于:2012-9-20<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 回收处理基础表
 * 
 * @author qinjingfeng
 * @version 1.0.0
 */
public class DocSet implements java.io.Serializable {

	private static final long serialVersionUID = -4621096184454795967L;
	/** 流水号 */
	private Long docId;
	/** 账单类型 */
	private Integer docTypeId;
	/** 任务状态 */
	private Integer docFlag;
	/** 工作日期 */
	private String workDate;
	/** 对账日期 */
	private String docDate;

	/** 账单编号 */
	private String voucherNo;

	/** 页面显示账单编号 */

	private String myVoucherNo;
	/** 页面显示账号 */

	private String myAccNo;

	/** 页面显示余额 */
	private String myCredit;

	/** 页面显示币种 */
	private String myCurrency;
	/** 页面显示对账标记 */
	private String myCheckFlag;

	/** 账户名称 */
	private String accName;
	/** 机构号 */
	private String idBank;
	/** 上级管理行 */
	private String idBranch;
	/** 对账中心 */
	private String idCenter;
	/** 对账机构 */
	private String bankName;
	/** 币种代码 */
	private String currency;
	/** 金额 */
	private Double credit;
	/** 未达标记 */
	private Integer matchFlag;
	/** 验印状态 */
	private Integer proveFlag;
	/** 验印标记 */
	private Integer proveState;
	/** 扫描柜员 */
	private String opCode100;
	/** 补录柜员 */
	private String opCode111;
	/** 复核柜员 */
	private String opCode112;
	/** 初验柜员 */
	private String opCode114;
	/** 复验柜员 */
	private String opCode124;
	/** 主管复验柜员 */
	private String opCode134;

	/** 正面图像url */
	private String frontImagePath;
	/** 背面url */
	private String backImagePath;
	/**
	 * 存储id号
	 */
	private String storeId;
	/** XML文件名称 */
	private String docFileName;
	/** 描述 */
	private String description;
	/** 锁定标记 */
	private Integer isFree;
	/** 调用次数 */
	private Integer callTimes;
	/** 验印账号 */
	private String sealAccNo;

	/** 退信次数 */
	private int urgeTimes;// 退信次数

	/** 票据查询列表显示 */
	private AccNoMainData accNoMainData;

	private Short needNotMatch; // 目前是否需要做未达处理，1代表是，0代表否

	private String deleteReason; // 删除理由

	private String reInputReason; // 重录理由

	private String strcredit; // 页面展示金额String

	// 扩展字段
	/** 复选框 */
	private boolean selected;
	/** 任务状态 */
	private String docFlagCN;
	/** 验印状态 */
	private String proveFlagCN;
	/** 验印标记 */
	private String proveStateCN;
	/** 核对结果 */
	private String resultCN;
	/** Description展开的扩展类 */
	private Object m_objDescription = null;
	/** Description模板类 */
	private static Object m_DescriptionTemplet;
	/** Description节点名（数组，通过外部配置设定） */
	private static String[] m_DescriptionItemName;
	/** 票据查询明细页面显示用 */
	private List<AccNoMainData> accnoMainDataList;

	/**
	 * 功能:根据节点名得到节点值（Description字段，普适函数）
	 * 
	 * @param nodeName
	 * @return
	 * @throws ControlException
	 */
	public String getDescItem(String nodeName) throws XDocProcException {
		Document document;
		Node StorenameNode;
		if (null != getDescription() && !getDescription().equals("")) {
			try {
				document = DocumentHelper.parseText(getDescription());
				StorenameNode = document.selectSingleNode("//DESCRIPTION/"
						+ nodeName);
				if (null != StorenameNode) {
					return StorenameNode.getText();
				} else {
					return "";
				}
			} catch (DocumentException e) {
				throw new XDocProcException("解析Description串失败", e.getMessage());
			}

		} else {
			return "";
		}
	}

	/**
	 * 功能:根据节点名设置节点值（Description字段，普适函数）
	 * 
	 * @param nodeName
	 * @param nodeValue
	 * @throws ControlException
	 */
	public void setDescItem(String nodeName, String nodeValue) throws Exception {

		Document document;
		Element DescNode;
		Element ItemNode;

		if (null == getDescription()) {
			setDescription("<DESCRIPTION></DESCRIPTION>");
		}
		try {
			document = DocumentHelper.parseText(getDescription());
			DescNode = (Element) document.selectSingleNode("//DESCRIPTION");
			ItemNode = (Element) DescNode.selectSingleNode(nodeName);

			if (null != ItemNode) {
				ItemNode.setText(nodeValue);
			} else {
				ItemNode = (Element) DescNode.addElement(nodeName);
				ItemNode.setText(nodeValue);
			}
			setDescription(document.asXML());

		} catch (DocumentException e) {
			throw new XDocProcException("解析Description串失败", e.getMessage());
		}

	}

	/**
	 * 功能:得到扩展的XML属性类
	 * 
	 * @return
	 * @throws ControlException
	 */
	public Object getObjDescription() throws XDocProcException {
		Document document;
		Node StorenameNode;
		int i;
		try {
			m_objDescription = BeanUtils.cloneBean(m_DescriptionTemplet);
			for (i = 0; i < m_DescriptionItemName.length; i++) {
				if (m_DescriptionItemName[i].substring(0, 1).equals("P")) {
					BeanUtils.setProperty(m_objDescription,
							m_DescriptionItemName[i], "");
				} else {
					BeanUtils
							.setProperty(
									m_objDescription,
									m_DescriptionItemName[i].substring(0, 1)
											.toLowerCase()
											+ m_DescriptionItemName[i]
													.substring(1), "");
				}
			}
			if (null != getDescription() && !getDescription().equals("")) {
				try {
					document = DocumentHelper.parseText(getDescription());
				} catch (DocumentException e) {
					throw new XDocProcException("解析Description串失败",
							e.getMessage());
				}
				for (i = 0; i < m_DescriptionItemName.length; i++) {
					StorenameNode = document.selectSingleNode("//DESCRIPTION/"
							+ m_DescriptionItemName[i]);
					if (null != StorenameNode) {
						if (m_DescriptionItemName[i].substring(0, 1)
								.equals("P")) {
							BeanUtils.setProperty(m_objDescription,
									m_DescriptionItemName[i],
									StorenameNode.getText());
						} else {
							BeanUtils.setProperty(
									m_objDescription,
									m_DescriptionItemName[i].substring(0, 1)
											.toLowerCase()
											+ m_DescriptionItemName[i]
													.substring(1),
									StorenameNode.getText());
						}
					}
				}
			}

		} catch (IllegalAccessException e) {
			throw new XDocProcException("动态克隆Description类失败", e.getMessage());
		} catch (InstantiationException e) {

			throw new XDocProcException("动态克隆Description类失败", e.getMessage());
		} catch (InvocationTargetException e) {

			throw new XDocProcException("动态克隆Description类失败", e.getMessage());
		} catch (NoSuchMethodException e) {

			throw new XDocProcException("动态克隆Description类失败", e.getMessage());
		}
		return m_objDescription;
	}

	public void setObjDescription() {

	}

	public void RefreshObjDescription() {
		m_objDescription = null;
	}

	/**
	 * 设置Description中需要解析的项目
	 * 
	 * @param ItemNames
	 *            需要解析的项目名称 数组
	 */
	public static void setDescriptionItems(String[] ItemNames) {
		int i;
		BeanGenerator bg = new BeanGenerator();
		m_DescriptionItemName = new String[ItemNames.length];
		for (i = 0; i < ItemNames.length; i++) {
			m_DescriptionItemName[i] = ItemNames[i];
			bg.addProperty(ItemNames[i], String.class);
		}
		m_DescriptionTemplet = bg.create();
	}

	public int getUrgeTimes() {
		return urgeTimes;
	}

	public void setUrgeTimes(int urgeTimes) {
		this.urgeTimes = urgeTimes;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Integer getDocFlag() {
		return docFlag;
	}

	public void setDocFlag(Integer docFlag) {
		this.docFlag = docFlag;
	}

	public String getWorkDate() {
		return workDate;
	}

	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	public String getDocDate() {
		return docDate;
	}

	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public String getIdBank() {
		return idBank;
	}

	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}

	public String getIdBranch() {
		return idBranch;
	}

	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}

	public String getIdCenter() {
		return idCenter;
	}

	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getCredit() {
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Integer getMatchFlag() {
		return matchFlag;
	}

	public void setMatchFlag(Integer matchFlag) {
		this.matchFlag = matchFlag;
	}

	public Integer getProveFlag() {
		return proveFlag;
	}

	public void setProveFlag(Integer proveFlag) {
		this.proveFlag = proveFlag;
	}

	public Integer getProveState() {
		return proveState;
	}

	public void setProveState(Integer proveState) {
		this.proveState = proveState;
	}

	public String getOpCode100() {
		return opCode100;
	}

	public void setOpCode100(String opCode100) {
		this.opCode100 = opCode100;
	}

	public String getOpCode111() {
		return opCode111;
	}

	public void setOpCode111(String opCode111) {
		this.opCode111 = opCode111;
	}

	public String getOpCode112() {
		return opCode112;
	}

	public void setOpCode112(String opCode112) {
		this.opCode112 = opCode112;
	}

	public String getOpCode114() {
		return opCode114;
	}

	public void setOpCode114(String opCode114) {
		this.opCode114 = opCode114;
	}

	public String getOpCode124() {
		return opCode124;
	}

	public void setOpCode124(String opCode124) {
		this.opCode124 = opCode124;
	}

	public String getOpCode134() {
		return opCode134;
	}

	public void setOpCode134(String opCode134) {
		this.opCode134 = opCode134;
	}

	public String getFrontImagePath() {
		return frontImagePath;
	}

	public void setFrontImagePath(String frontImagePath) {
		this.frontImagePath = frontImagePath;
	}

	public String getBackImagePath() {
		return backImagePath;
	}

	public void setBackImagePath(String backImagePath) {
		this.backImagePath = backImagePath;
	}

	public String getDocFileName() {
		return docFileName;
	}

	public void setDocFileName(String docFileName) {
		this.docFileName = docFileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public Integer getCallTimes() {
		return callTimes;
	}

	public void setCallTimes(Integer callTimes) {
		this.callTimes = callTimes;
	}

	public String getSealAccNo() {
		return sealAccNo;
	}

	public void setSealAccNo(String sealAccNo) {
		this.sealAccNo = sealAccNo;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getDocFlagCN() {
		return docFlagCN;
	}

	public void setDocFlagCN(String docFlagCN) {
		this.docFlagCN = docFlagCN;
	}

	public String getProveFlagCN() {
		return proveFlagCN;
	}

	public void setProveFlagCN(String proveFlagCN) {
		this.proveFlagCN = proveFlagCN;
	}

	public String getProveStateCN() {
		return proveStateCN;
	}

	public void setProveStateCN(String proveStateCN) {
		this.proveStateCN = proveStateCN;
	}

	public String getResultCN() {
		return resultCN;
	}

	public void setResultCN(String resultCN) {
		this.resultCN = resultCN;
	}

	public AccNoMainData getAccNoMainData() {
		return accNoMainData;
	}

	public void setAccNoMainData(AccNoMainData accNoMainData) {
		this.accNoMainData = accNoMainData;
	}

	public Short getNeedNotMatch() {
		return needNotMatch;
	}

	public void setNeedNotMatch(Short needNotMatch) {
		this.needNotMatch = needNotMatch;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getReInputReason() {
		return reInputReason;
	}

	public void setReInputReason(String reInputReason) {
		this.reInputReason = reInputReason;
	}

	public List<AccNoMainData> getAccnoMainDataList() {
		return accnoMainDataList;
	}

	public void setAccnoMainDataList(List<AccNoMainData> accnoMainDataList) {
		this.accnoMainDataList = accnoMainDataList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setDeleteReasons(String[] reasons) {
		if (reasons == null) {
			deleteReason = "";
			return;
		}
		deleteReason = ""; // 防止null+"xxx"="nullxxx"的情况
		for (int i = 0; i < reasons.length; i++) {
			deleteReason += reasons[i];
			if (i == reasons.length) {
				break;
			}
			deleteReason += "|";
		}
		deleteReason = deleteReason.substring(0, deleteReason.length() - 1);
	}

	public void setReInputReasons(String[] reasons) {
		if (reasons == null) {
			deleteReason = "";
			return;
		}
		reInputReason = ""; // 防止null+"xxx"="nullxxx"的情况
		for (int i = 0; i < reasons.length; i++) {
			reInputReason += reasons[i];
			if (i == reasons.length) {
				break;
			}
			reInputReason += "|";
		}
	}

	public String[] getDeleteReasons() {
		if (deleteReason == null || deleteReason.length() == 0) {
			return null;
		}
		return deleteReason.split("[|]");
	}

	public String[] getReInputReasons() {
		if (reInputReason == null || reInputReason.length() == 0) {
			return null;
		}
		return reInputReason.split("[|]");
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getMyVoucherNo() {
		return accNoMainData.getVoucherNo();
	}

	public void setMyVoucherNo(String myVoucherNo) {
		this.myVoucherNo = myVoucherNo;
	}

	public String getMyAccNo() {
		return accNoMainData.getAccNo();
	}

	public void setMyAccNo(String myAccNo) {
		this.myAccNo = myAccNo;
	}

	public String getMyCredit() {
		return accNoMainData.getStrcredit();
	}

	public void setMyCredit(String myCredit) {
		this.myCredit = myCredit;
	}

	public String getMyCurrency() {
		return accNoMainData.getCurrency();
	}

	public void setMyCurrency(String myCurrency) {
		this.myCurrency = myCurrency;
	}

	public String getMyCheckFlag() {
		return accNoMainData.getCheckFlag();
	}

	public void setMyCheckFlag(String myCheckFlag) {
		this.myCheckFlag = myCheckFlag;
	}

	public String getStrcredit() {
		strcredit = UtilBase.formatString(this.getCredit());
		return strcredit;
	}

	public void setStrcredit(String strcredit) {
		this.strcredit = strcredit;
	}

}