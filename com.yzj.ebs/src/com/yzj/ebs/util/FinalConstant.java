package com.yzj.ebs.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 湘江银行 常量类
 * @author lixiangjun
 *
 */
public class FinalConstant {
	
	
	/**
	 * 定义数据处理文件名称前缀 --2期改造添加 "XTPF20","NETACRELATION"两文件;INNERACCNODETAIL:内部账户交易明细
	 */
	public static String[] FILES_NAME = new String[] {"UTBLBRCD","KUB_USER","INNERBASICINFO",
		"MAINDATA","XDCKLIST","ACCTLIST","KNP_EXRT","DEPHIST","AAPFZ0","NETACRELATION","INNERACCNODETAIL"};
	
	/**
	 *  定义数据导入中每个数据文件中字段取值的位数--2期改造添加6,2,13
	 */
	public static int [] FILES_SIZE = new int[]{7,4,3,26,1,3,4,16,6,2,13};
	
	/**
	 * 总行的机构号
	 */
	public static final String ROOTORG="1000000000";
	
	/**
	 * 单位协定存款账户科目号
	 */
	public static final String XDCKSUB="00000";
	/**
	 * 发送方式  1：柜台， 2：邮寄，3：网银，4: 面对面
	 */
	public static Map<String,String> sendModelMap = new HashMap<String,String>();
	public static Map<String,String> sendModelName = new HashMap<String,String>();
	
	/**
	 * 对账周期 1：一类账户，2：二类账户，3：三类账户，4：四类账户
	 */
	public static Map<String,String> acccycleMap = new HashMap<String,String>();
	public static Map<String,String> acccycleName = new HashMap<String,String>();
	/**
	 * 是否是总行对账  正常:0; 总行:1
	 */
	public static Map<String,String> isHBcheckMap = new HashMap<String,String>();
	public static Map<String,String> isHBcheckName = new HashMap<String,String>();	
	
	/**
	 * 借贷标志 0：双方  1 借  2 贷
	 */
	public static Map<String,String> dcflagMap = new HashMap<String,String>();
	public static Map<String,String> dcflagName = new HashMap<String,String>();	
	
	/**
	 * 发送方式  0 相符  1不相符
	 */
	public static Map<String,String> accordMap = new HashMap<String,String>();
	public static Map<String,String> accordName= new HashMap<String,String>();
	
	/**
	 * 验印状态  1 等待打印  2 等待回收  3 已回收  4 退信  5 再次发出  6 放弃发出 
	 */
	public static Map<String,String> docStateMap = new HashMap<String,String>();
	public static Map<String,String> docStateName= new HashMap<String,String>();
	
	/**
	 * 0 对账  1不对账 
	 */
	public static Map<String,String> isCheckMap = new HashMap<String,String>();
	public static Map<String,String> isCheckName= new HashMap<String,String>();
	
	/**
	 * 0 普通用户  1 特殊用户
	 */
	public static Map<String,String> isSpecileMap = new HashMap<String,String>();
	public static Map<String,String> isSpecileName= new HashMap<String,String>();
	
	/**
	 * 账户状态 0 正常  1 销户 2 长期不动户 3 不动转收益
	 */
	public static Map<String,String> accStateMap = new HashMap<String,String>();
	public static Map<String,String> accStateName= new HashMap<String,String>();

	/**
	 * 特殊账户过滤 0 未导入  1已导入
	 */
	public static Map<String,String> refImportName= new HashMap<String,String>();
	
	/**
	 * 币种01为人民币，非01为外币
	 */
	public static Map<String,String> currencyMap= new HashMap<String,String>();
	public static Map<String,String> currencyName= new HashMap<String,String>();
	
	/**  对参数赋值    **/
	static{
		//发送方式
		sendModelMap.put("柜台","1");
		sendModelMap.put("邮寄","2");
		sendModelMap.put("网银","3");
		sendModelMap.put("面对面","4");
		
		sendModelName.put("1","柜台");
		sendModelName.put("2","邮寄");
		sendModelName.put("3","网银");
		sendModelName.put("4","面对面");
		
		
		//对账周期
		acccycleMap.put("一类账户","1");
		acccycleMap.put("二类账户","2");
		acccycleMap.put("三类账户","3");
		acccycleMap.put("四类账户","4");
		
		acccycleName.put("1","一类账户");
		acccycleName.put("2","二类账户");
		acccycleName.put("3","三类账户");
		acccycleName.put("4","四类账户");
		
		
		//是否是总行对账
		isHBcheckMap.put("正常","0");
		isHBcheckMap.put("总行","1");
		
		isHBcheckName.put("0","正常");
		isHBcheckName.put("1","总行");
		
		//借贷标志 0：双方  1 借  2 贷
		dcflagMap.put("双方","0");
		dcflagMap.put("借","1");
		dcflagMap.put("贷","2");
		
		dcflagName.put("0","双方");
		dcflagName.put("1","借");
		dcflagName.put("2","贷");
		
		//相符 标志 0 相符 1不相符
		accordMap.put("相符", "0");
		accordMap.put("不相符", "1");
		
		accordName.put("0", "相符");
		accordName.put("1", "不相符");
		
		//验印状态  1 等待打印  2 等待回收  3 已回收  4 退信  5 再次发出  6 放弃发出 
		docStateMap.put("等待打印", "1");
		docStateMap.put("等待回收", "2");
		docStateMap.put("已回收", "3");
		docStateMap.put("退信", "4");
		docStateMap.put("再次发出", "5");
		docStateMap.put("放弃发出", "6");
		
		docStateName.put("1", "等待打印");
		docStateName.put("2", "等待回收");
		docStateName.put("3", "已回收");
		docStateName.put("4", "退信");
		docStateName.put("5", "再次发出");
		docStateName.put("6", "放弃发出");
		
		//0对账  1不对帐
		isCheckMap.put("对账", "0");
		isCheckMap.put("不对账", "1");
		
		isCheckName.put("0", "对账");
		isCheckName.put("1", "不对账");
		
		//1 特殊账户  0普通账户
		isSpecileMap.put("普通账户", "0");
		isSpecileMap.put("特殊账户", "1");
		
		isSpecileName.put("0", "普通账户");
		isSpecileName.put("1", "特殊账户");
		
		//账户状态 0 正常  1 销户 2 长期不动户 3 不动转收益
		accStateMap.put("正常", "0");
		accStateMap.put("销户", "1");
		accStateMap.put("长期不动户", "2");
		accStateMap.put("不动转收益", "3");
	
		accStateName.put("0", "正常");
		accStateName.put("1", "销户");
		accStateName.put("2", "长期不动户");
		accStateName.put("3", "不动转收益");
		
		//特殊账户过滤 0 未导入  1以导入
		refImportName.put("0", "未导入");
		refImportName.put("1", "以导入");
		
		//币种01为人民币，币种非01为外币
		currencyMap.put("人民币", "01");
		currencyName.put("01", "人民币");
	}

}
