package com.yzj.ebs.database;

/**
 *创建于:2012-9-24<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 账单交接登记薄
 * @author qinjingfeng
 * @version 1.0.0
 */
public class ExChangeBook implements java.io.Serializable {

	private static final long serialVersionUID = 5996702664577592220L;
	protected Long autoid;
	protected String docDate;//对账日期
	protected String sendmode;//对账渠道
	protected String voucherNo;//账单编号
	protected String accName;//单位名称
	protected String sendDate;//发送日期

	protected String backDate;//收回日期
	protected String idCenter;//对账中心
	protected String idBranch;//支行
	protected String idBank;//网点
	protected String desc;//备注
	protected String  opcode;//登记柜员
	protected Long letterSum;//份数
	protected String opType;//登记类型
	
	public Long getAutoid() {
		return autoid;
	}
	public void setAutoid(Long autoid) {
		this.autoid = autoid;
	}
	public String getDocDate() {
		return docDate;
	}
	public void setDocDate(String docDate) {
		this.docDate = docDate;
	}
	public String getSendmode() {
		return sendmode;
	}
	public void setSendmode(String sendmode) {
		this.sendmode = sendmode;
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
	
	public String getIdCenter() {
		return idCenter;
	}
	public void setIdCenter(String idCenter) {
		this.idCenter = idCenter;
	}
	public String getIdBranch() {
		return idBranch;
	}
	public void setIdBranch(String idBranch) {
		this.idBranch = idBranch;
	}
	public String getIdBank() {
		return idBank;
	}
	public void setIdBank(String idBank) {
		this.idBank = idBank;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	public Long getLetterSum() {
		return letterSum;
	}
	public void setLetterSum(Long letterSum) {
		this.letterSum = letterSum;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
}