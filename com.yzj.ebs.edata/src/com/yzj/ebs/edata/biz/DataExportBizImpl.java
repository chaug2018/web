/**
 * DataExportBizImpl.java
 * 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 创建:秦靖锋 2013-03-29
 */
package com.yzj.ebs.edata.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.struts2.ServletActionContext;

import com.infotech.publiclib.Exception.DaoException;
import com.yzj.ebs.common.BankParam;
import com.yzj.ebs.common.IAccnoDetailAdm;
import com.yzj.ebs.common.IAccnoMainDataAdm;
import com.yzj.ebs.common.ICheckMainDataAdm;
import com.yzj.ebs.common.IDetailMainDataAdm;
import com.yzj.ebs.common.IParamBank;
import com.yzj.ebs.common.IPublicTools;
import com.yzj.ebs.common.RefTableTools;
import com.yzj.ebs.common.XDocProcException;
import com.yzj.ebs.database.AccNoMainData;
import com.yzj.ebs.database.CheckMainData;
import com.yzj.ebs.edata.bean.AccnoMainDataQueryParam;
import com.yzj.ebs.edata.bean.BatchPrintBean;
import com.yzj.ebs.tool.CompressBook;
import com.yzj.ebs.util.DataExporter;
import com.yzj.ebs.util.UtilBase;
import com.yzj.wf.common.WFLogger;
import com.yzj.wf.ebs.security.tool.SecurityTool;

/**
 * 创建于:2013-03-29 版权所有(C) 2013 深圳市银之杰科技股份有限公司
 * 
 * 账单打印业务逻辑处理
 * 
 * @author j秦靖锋
 * @version 1.0.0
 */
public class DataExportBizImpl implements IDataExportBiz {

	private static WFLogger logger = WFLogger
			.getLogger(DataExportBizImpl.class);
	private TreeMap<String, String> refAccTypeMap = new TreeMap<String, String>();
	private IDetailMainDataAdm detailMainDataAdm;
	private ICheckMainDataAdm checkMainDataAdm;
	private IAccnoDetailAdm accnoDetailAdm;
	public BatchPrintBean total = null;// jiangwang
	private IPublicTools tools;// jiangwang
	private IAccnoMainDataAdm accnoMainDataAdm;// jiangwang
	private String sendmode;
	private IParamBank  paramBank;
	private Map<String, String> mapdata = new HashMap<String, String>();
	
	
	public List<CheckMainData> queryAccNoMainDataInfo(
			AccnoMainDataQueryParam accnoMainDataQueryParam)
			throws XDocProcException {
		Map<String, String>sendMap =RefTableTools.ValRefSendModeMap;
		mapdata.clear();
		Map<String, String> accQueryMap = new HashMap<String, String>();
		String docdate = accnoMainDataQueryParam.getDocDate();// 对账日期
		mapdata.put("idCenter", accnoMainDataQueryParam.getIdCenter());
		
		 sendmode=accnoMainDataQueryParam.getSendMode();
		//如果是导出模块的复选 sendmode
		if("all".equals(sendmode)){
			sendmode="'1','2','3','4'";
		}else{
			sendmode = sendmode.replace("+", "','").substring(6);
			sendmode = "'"+sendmode+"'";
		}
		mapdata.put("sendModeType", sendmode);

		if (accnoMainDataQueryParam.getIdBank() != null
				&& accnoMainDataQueryParam.getIdBank().length() > 0) {
			mapdata.put("idBank", accnoMainDataQueryParam.getIdBank());
		}
		mapdata.put("docDate", docdate.replace("-", ""));
		List<CheckMainData> resultList = new ArrayList<CheckMainData>();
		// 分页查询
		List<Object[]> list = checkMainDataAdm.getCheckMaindata(mapdata,
				accnoMainDataQueryParam, accQueryMap);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CheckMainData cmd = new CheckMainData();
				if (list.get(i)[0] == null) {
					cmd.setVoucherNo("");
				} else {
					cmd.setVoucherNo(String.valueOf(list.get(i)[0] + ""));
				}
				if (list.get(i)[1] == null) {
					cmd.setIdCenter("");
				} else {
					cmd.setIdCenter(String.valueOf(list.get(i)[1] + ""));
				}
				if (list.get(i)[2] == null) {
					cmd.setBankName("");
				} else {
					cmd.setBankName(String.valueOf(list.get(i)[2] + ""));
				}
				if (list.get(i)[3] == null) {
					cmd.setAccName("");
				} else {
					cmd.setAccName(String.valueOf(list.get(i)[3] + ""));
				}
				if (list.get(i)[4] == null) {
					cmd.setCustId("");
				} else {
					cmd.setCustId(String.valueOf(list.get(i)[4] + ""));
				}
				if (list.get(i)[5] == null) {
					cmd.setLinkMan("");
				} else {
					cmd.setLinkMan(String.valueOf(list.get(i)[5] + ""));
				}
				if (list.get(i)[6] == null) {
					cmd.setAddress("");
				} else {
					cmd.setAddress(String.valueOf(list.get(i)[6] + ""));
				}
				if (list.get(i)[7] == null) {
					cmd.setPhone("");
				} else {
					cmd.setPhone(String.valueOf(list.get(i)[7] + ""));
				}
				cmd.setDocState(String.valueOf(list.get(i)[8] + ""));
				if (list.get(i)[9] != null) {
					cmd.setPrintTimes(new Integer(String.valueOf(list.get(i)[9]
							+ "")).intValue());
				} else {
					cmd.setPrintTimes(0);
				}
				cmd.setDocDate(String.valueOf(list.get(i)[10] + ""));
				if (list.get(i)[11] == null) {
					cmd.setZip("");
				} else {
					cmd.setZip(String.valueOf(list.get(i)[11] + ""));
				}
				if (list.get(i)[12] == null) {
					cmd.setSendMode("");
				} else {
					cmd.setSendMode(sendMap.get(String.valueOf(list.get(i)[12])));
				}
				if (list.get(i)[13] == null) {
					cmd.setSendAddress("");
				} else {
					cmd.setSendAddress(String.valueOf(list.get(i)[13] + ""));
				}
				resultList.add(cmd);
			}
		}
		return resultList;
	}

	/**
	 * 返回打印的源数据列表
	 * 
	 * @return
	 * @throws XDocProcException 
	 * @throws DaoException
	 */
	public BatchPrintBean getBatchPrintList(CheckMainData data) throws XDocProcException {
		/** 账单信息：账单编号、对账日期、客户号、户名、邮政编码、地址、联系人、联系电话 */
		total = new BatchPrintBean();
		total.setVoucherno(data.getVoucherNo());
		String date = data.getDocDate().substring(0, 4) + "-"
				+ data.getDocDate().substring(4, 6) + "-"
				+ data.getDocDate().substring(6, 8);
		total.setDocdate(date);
		total.setCustomerid(data.getCustId());

		if (data.getZip() != null && !"null".equals(data.getZip())) {
			total.setZip(data.getZip());
		} else {
			total.setZip("");
		}
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setAddress(data.getSendAddress()+"("+data.getIdBank()+")");
		} else {
			total.setAddress("");
		}
		if (data.getLinkMan() != null && !"null".equals(data.getLinkMan())) {
			total.setLinkman(data.getLinkMan());
		} else {
			total.setLinkman("");
		}
		if (data.getPhone() != null && !"null".equals(data.getPhone())) {
			total.setPhone(data.getPhone());
		} else {
			total.setPhone("");
		}
		if (data.getAccName() != null && !"null".equals(data.getAccName())) {
			total.setAccname( data.getAccName());
		} else {
			total.setAccname("");
		}
		// 对账中心电话 地址
		BankParam cp = paramBank.getBankParam(data.getIdCenter());
		
		if (cp.getPhone() != null && !"null".equals(cp.getPhone())) {
			total.setCenterphone(cp.getPhone());
		} else {
			total.setCenterphone("");
		}
		if (cp.getAddress() != null && !"null".equals(cp.getAddress())) {
			total.setCenteraddress( cp.getAddress());
		} else {
			total.setCenteraddress("");
		}
		if (cp.getcName() != null && !"null".equals(cp.getcName())) {
			total.setIdCenterName(cp.getcName());
		} else {
			total.setIdCenterName("");
		}
		//发送地址
		if (data.getSendAddress() != null && !"null".equals(data.getSendAddress())) {
			total.setSendAddress(data.getSendAddress()+"("+data.getIdBank()+")");
		} else {
			total.setSendAddress("");
		}	
		List<AccNoMainData> docList = accnoMainDataAdm
				.getAccnoMainDataByVoucherNo(data.getVoucherNo());     //根据对账单编号得到 其下面的1-5个 明细信息
		for (int j = 0; j < docList.size(); j++) {
			AccNoMainData doc = (AccNoMainData) docList.get(j);
			Map<String,String> subMap = RefTableTools.ValParamSubnocMap;
			Map<String,String> sealModeMap = RefTableTools.ValRefSealModeMap;
			Map<String,String> currType = RefTableTools.valParamCurrtypeMap;
			/** 账号信息：账号、账户类型、开户行名、余额、币种 */
			if (j == 0) {
				if (doc.getAccNo() != null) {
					total.setAccno1(doc.getAccNo());
					//add 20151222 打印模板增加验印模式
					String sealmode = accnoMainDataAdm.getSealmodeByAccNo(doc.getAccNo());
					String chnSealmode=(sealModeMap.get(sealmode)==null)?"":sealModeMap.get(sealmode);
					total.setSealmode(chnSealmode);
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo1(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank1(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname1(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit1(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN1(currType.get(doc.getCurrency()));
				}
			}
			if (j == 1) {
				if (doc.getAccNo() != null) {
					total.setAccno2(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo2(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank2(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname2(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit2(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN2(currType.get(doc.getCurrency()));
				}
			}
			if (j == 2) {
				if (doc.getAccNo() != null) {
					total.setAccno3(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo3(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank3(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname3(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit3(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN3(currType.get(doc.getCurrency()));
				}
			}
			if (j == 3) {
				if (doc.getAccNo() != null) {
					total.setAccno4(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo4(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank4(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname4(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit4(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN4(currType.get(doc.getCurrency()));
				}
			}
			if (j == 4) {
				if (doc.getAccNo() != null) {
					total.setAccno5(doc.getAccNo());
				}
				if (doc.getSubjectNo() != null) {
					total.setSubjectNo5(subMap.get(doc.getSubjectNo()));
				}
				if (doc.getIdBank() != null) {
					total.setIdbank5(doc.getIdBank());
				}
				if (doc.getBankName() != null) {
					total.setBankname5(doc.getBankName());
				}
				if (doc.getCredit() != null) {
					total.setCredit5(UtilBase.formatString(doc.getCredit()));
				}
				if (doc.getCurrency() != null) {
					total.setCurrtypeCN5(currType.get(doc.getCurrency()));
				}
			}
		}
		return total;
	}

	// 字段间以||分隔，字段顺序查看BatchPrint.xml-><VoucherCfg>/<DataDefine>/<Fields>节点字段顺序
	private String getPrintDataByStr(BatchPrintBean data) {
		String returnStr;
		returnStr = data.getDocdate();                         		//对账日期
		String cutFlag ="|";
		returnStr = returnStr +cutFlag + data.getVoucherno(); 		 //账单编号
		returnStr = returnStr + cutFlag+ data.getAccname();           //账户户名
		returnStr = returnStr + cutFlag + data.getZip();               //客户邮编
		returnStr = returnStr + cutFlag + data.getAddress();           //客户地址
		returnStr = returnStr + cutFlag+ data.getPhone();               //客户联系电话
		returnStr = returnStr + cutFlag + data.getLinkman();             //联系人
		returnStr = returnStr + cutFlag + data.getIdCenterName();       //分行名称
		returnStr = returnStr + cutFlag+ data.getCenterphone();         //分行电话
		returnStr = returnStr + cutFlag + data.getCenteraddress();       //分行地址
		returnStr = returnStr + cutFlag + data.getSendAddress();          //账单投递投递地址
		returnStr = returnStr + cutFlag + data.getAccno1();               //账号
		returnStr = returnStr + cutFlag+ data.getCredit1();                //余额
		returnStr = returnStr + cutFlag+ data.getCurrtypeCN1();           //币种
		returnStr = returnStr + cutFlag+ data.getSubjectNo1();            //科目号
		returnStr = returnStr + cutFlag+ data.getBankname1();             //开户网点名称
		returnStr = returnStr + cutFlag + data.getAccno2();                 
		returnStr = returnStr + cutFlag + data.getCredit2();
		returnStr = returnStr + cutFlag + data.getCurrtypeCN2();
		returnStr = returnStr + cutFlag + data.getSubjectNo2();
		returnStr = returnStr + cutFlag+ data.getBankname2();
		returnStr = returnStr + cutFlag + data.getAccno3();
		returnStr = returnStr + cutFlag+ data.getCredit3();
		returnStr = returnStr + cutFlag + data.getCurrtypeCN3();
		returnStr = returnStr + cutFlag + data.getSubjectNo3();
		returnStr = returnStr + cutFlag + data.getBankname3();
		returnStr = returnStr + cutFlag + data.getAccno4();
		returnStr = returnStr + cutFlag + data.getCredit4();
		returnStr = returnStr + cutFlag+ data.getCurrtypeCN4();
		returnStr = returnStr + cutFlag + data.getSubjectNo4();
		returnStr = returnStr + cutFlag + data.getBankname4();
		returnStr = returnStr + cutFlag + data.getAccno5();
		returnStr = returnStr + cutFlag+ data.getCredit5();
		returnStr = returnStr + cutFlag + data.getCurrtypeCN5();
		returnStr = returnStr + cutFlag+ data.getSubjectNo5();
		returnStr = returnStr + cutFlag + data.getBankname5();
		returnStr = returnStr + cutFlag + data.getSealmode();
		return returnStr;
	}

	/**
	 * 对账单导出
	 */
	public List<CheckMainData> makeDataInfo(List<CheckMainData> resultList,
			String password, AccnoMainDataQueryParam accnoMainDataQueryParam)throws IOException {
		try {
			String docdate = accnoMainDataQueryParam.getDocDate();// 对账日期
			mapdata.put("docDate", "'"+docdate.replace("-", "")+"'");
			mapdata.put("idCenter", "'"+accnoMainDataQueryParam.getIdCenter()+"'");
			
			sendmode=accnoMainDataQueryParam.getSendMode();
			//如果是导出模块的复选 sendmode
			if("all".equals(sendmode)){
				sendmode="'1','2','3','4'";
			}else{
				sendmode = sendmode.replace("+", "','").substring(6);
				sendmode = "'"+sendmode+"'";
			}
			mapdata.put("sendModeType", sendmode);	
			if (accnoMainDataQueryParam.getIdBank() != null
					&& accnoMainDataQueryParam.getIdBank().length() > 0) {
				mapdata.put("idBank", "'"+accnoMainDataQueryParam.getIdBank()+"'");
			}
			resultList = checkMainDataAdm.getExportCheckMainData(mapdata);
			//导出 对账单
			StringBuffer str = new StringBuffer();
			if (resultList != null && resultList.size() != 0) {
				//记录当前的 对账中心名字
				String idcenterName="idcenterName";                          
				// 记录先前的对账中心的名字
				String preIdecenterName = "";					
				List<String>strList = new ArrayList<String>();
				for (int i = 0; i < resultList.size(); i++) {
					CheckMainData data = (CheckMainData) resultList.get(i);					
					BatchPrintBean batchPrintBean;
					batchPrintBean = getBatchPrintList(data);	
					preIdecenterName = batchPrintBean.getIdCenterName();
					if(idcenterName.equals(preIdecenterName) || idcenterName.equals("idcenterName")){           
						//如果相等则继续 相加
						if(idcenterName.equals("idcenterName")){
							str.append(getPrintDataByStr(batchPrintBean));	
						}else{
							str.append("\r\n");
							str.append(getPrintDataByStr(batchPrintBean));
						}
					}else{
						if(str.length()>0){
							strList.add(str.toString());                
						} 
						str.setLength(0);
						str.append("\r\n");
						str.append(getPrintDataByStr(batchPrintBean));
					}		
					idcenterName = preIdecenterName;
				}
				strList.add(str.toString());      //加入最后一个分行的 信息 
				//打印对账单信息
				exportData(strList,password,"EbillData");
				//修改对账单状态为 等待回收  发送次数+1；
				detailMainDataAdm.updateCheckmaindataByDocdate(accnoMainDataQueryParam.getDocDate(),mapdata);
				detailMainDataAdm.updatePrintTime(mapdata);
			}else{
				logger.error("未获取对账单信息！");
			}
		} catch (Exception e) {
			logger.error("对账单导出异常！");
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 *  明细导出
	 * @throws XDocProcException 
	 */
	public void exportDetail(String password,String month) throws Exception{
		List<Object[]> detailList = new ArrayList<Object[]>();      //当月活期明细
		detailList = checkMainDataAdm.getDeatil(mapdata,month,false,0,0);
		// 导出 活期明细
		if (detailList != null && detailList.size() != 0) {
			String detail = getDetailStr(detailList);
			List<String> list = new ArrayList<String>();
			list.add(detail);
			try {
				exportData(list,password,"DetailData");
			}catch (IOException e) {
				logger.error("对账单明细打印出错！"+e);
			}	
		}else{
			logger.error("未获取活期明细信息！");
		}
	}
	
	/**
	 *  根据String 导出加密数据
	 * @return
	 * @throws IOException 
	 */
	public void exportData (List<String> list,String password,String name) throws IOException{
		 
		DataExporter dataExporter = new DataExporter(null, null, null, null);
		//临时存放数据导出打印文件的路径
		String dir=ServletActionContext.getServletContext().getRealPath("/")+"impoteDir";
		//数据文件的存放路径
		String tempDir=dir+"/temp";
		String dataName="";
		//如果目录存在 则先删除
		try {
			File fileTemp = new File(tempDir);
			if(fileTemp.exists()){
				this.deleteFile(fileTemp);
			}
			//创建文件夹
			fileTemp.mkdirs();
			
			String info = "";
			int length = list.size();
			for(int i=0;i<length;i++){
				info = list.get(i);         //得到一个分行的对账数据
				SecurityTool st = null;
				FileOutputStream fos = null;
				if(!name.equals("DetailData")){
					dataName = UtilBase.getNowDate() +"_"+ name+"_"+paramBank.getIdcenterCode(getStrIdecenterName(info));
					
				}else{
					dataName  = UtilBase.getNowDate() + name;
				}
				String dataFileName = dataName + ".txt";
				String path = tempDir+"/"+dataFileName;
				String charEncoding = "UTF-8";
				OutputStreamWriter writer = null;
				st = new SecurityTool(SecurityTool.base64);
				
				fos = new FileOutputStream(new File(path));
				writer = new OutputStreamWriter(fos, charEncoding);
				try {
					writer.write(st.encrypt(new String(password.getBytes("ISO8859-1"),"UTF-8")).toString()+ "\r\n");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				long begin4 = System.currentTimeMillis();
				writer.write(st.encrypt(info).toString());
				writer.close();
				fos.flush();
				fos.close();
				long end4 = System.currentTimeMillis();
				logger.info("数据导出执行耗时:" + (end4 - begin4) + " 豪秒");
				
			}
			/** 把存放 对账单txt的文件夹打成压缩包 */
			CompressBook book = new CompressBook();
			book.zip(tempDir, dir +"/"+name+".zip");
			String fileName = dir+"/"+name+".zip";	
			/** 下载数据 */
			dataExporter.downloadTxtDataToLocal(fileName,name+".zip");
			deleteFile(new File(dir));
		} catch (Exception e) {
			logger.error("数据导出异常", e);
		}
	}
	
	
	/**
	 * 根据明细list 获得 导出的String
	 * 
	 * @return
	 */
	public String getDetailStr( List<Object[]> detailList ){
		StringBuffer str = new StringBuffer();
		Iterator<Object[]> it =detailList.iterator();
		//xx1||xx2||xx3
		while(it.hasNext()){
			Object[] ob= (Object[]) it.next();
			for(int i=0;i<8;i++){
				if(ob[i] == null){
					ob[i]="";
				}
				str.append(ob[i]); 
					if(7!=i){
						str.append("|");    //最后一个w尾巴不加 ||
					}
					if(7==i){
						str.append("\r\n");    //换行
					}
			}
		}
		String str1 = str.toString();
		return str1;
	}
	/**
	 *   用于 在一个string中取得 分行的名称
	 * @return
	 */
	public String getStrIdecenterName (String str){
		int i=1;         //数'|'个数
		int j=0;         //记录最后一个'|'位置
		int first = 0;   //记录前一个'|'位置
		while(true){
			if(str.charAt(j)== '|'){
				i++;
			}
			j++;
			if(i==7){
				first = j;
			}
			if(i== 9){
				break;
			}	
		}
		return  str.substring(first+1, j-1);
		
	}
	/**
	 * 删除文件夹 
	 * @return
	 */
	public void deleteFile(File fileDelete){
		if(fileDelete.exists()){
			if(fileDelete.isFile()){
				fileDelete.delete();
			}else if(fileDelete.isDirectory()){
				File[] files = fileDelete.listFiles();
				for(int i=0;i<files.length;i++){
					this.deleteFile(files[i]);
				}
			}
			fileDelete.delete();
		}
	}
	
	public TreeMap<String, String> getRefAccTypeMap() {
		if (null == refAccTypeMap || refAccTypeMap.size() == 0) {
			refAccTypeMap = RefTableTools.ValRefAcctypeMap;
		}
		return refAccTypeMap;
	}

	public void setRefAccTypeMap(TreeMap<String, String> refAccTypeMap) {
		this.refAccTypeMap = refAccTypeMap;
	}

	public IDetailMainDataAdm getDetailMainDataAdm() {
		return detailMainDataAdm;
	}

	public void setDetailMainDataAdm(IDetailMainDataAdm detailMainDataAdm) {
		this.detailMainDataAdm = detailMainDataAdm;
	}

	public IAccnoDetailAdm getAccnoDetailAdm() {
		return accnoDetailAdm;
	}

	public void setAccnoDetailAdm(IAccnoDetailAdm accnoDetailAdm) {
		this.accnoDetailAdm = accnoDetailAdm;
	}

	public void setAccnoMainDataAdm(IAccnoMainDataAdm accnoMainDataAdm) {
		this.accnoMainDataAdm = accnoMainDataAdm;
	}

	public void setCheckMainDataAdm(ICheckMainDataAdm checkMainDataAdm) {
		this.checkMainDataAdm = checkMainDataAdm;
	}

	public IPublicTools getTools() {
		return tools;
	}

	public void setTools(IPublicTools tools) {
		this.tools = tools;
	}

	public IParamBank getParamBank() {
		return paramBank;
	}

	public void setParamBank(IParamBank paramBank) {
		this.paramBank = paramBank;
	}

}
