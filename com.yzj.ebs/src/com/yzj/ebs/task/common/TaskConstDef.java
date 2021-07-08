package com.yzj.ebs.task.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建于:2012-9-25<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 任务相关常量定义
 * 
 * @author 陈林江
 * @version 1.0.0
 */
public class TaskConstDef {

	/**
	 * 创建于:2012-9-25<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 流程定义标识
	 * 
	 * @author 陈林江
	 * @version 1.0.0
	 */
	public static class ProcessDefKey {

		/**
		 * 回收流程
		 */
		public static final String EBS_RECOVERY = "EBS_RECOVERY";

	}

	/**
	 * 创建于:2012-9-25<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 应用程序标识常量定义
	 * 
	 * @author 陈林江
	 * @version 1.0.0
	 */
	public class AppId {

		/**
		 * 自动匹配
		 */
		public static final String AUTO_VERIFYDATA = "com.yzj.ebs.auto.autoverifydata";

		/**
		 * 数据补录
		 */
		public static final String MANUAL_INPUT = "com.yzj.ebs.back.manualinput";

		/**
		 * 录入审核
		 */
		public static final String MANUAL_AUTH = "com.yzj.ebs.back.manualauth";

		/**
		 * 自动验印
		 */
		public static final String AUTO_PROVE = "com.yzj.ebs.auto.autoprove";

		/**
		 * 自动记账
		 */
		public static final String AUTO_TALLY = "com.yzj.ebs.auto.autotally";

		/**
		 * 人工初验
		 */
		public static final String MANUALPROVE_FST = "com.yzj.ebs.back.manualprovefst";

		/**
		 * 人工复验
		 */
		public static final String MANUALPROVE_SND = "com.yzj.ebs.back.manualprovesnd";

		/**
		 * 主管验印
		 */
		public static final String MANUALPROVE_AUTH = "com.yzj.ebs.back.manualproveauth";

		/**
		 * 删除审核
		 */
		public static final String DELETE_AUTH = "com.yzj.ebs.back.deleteauth";

		/**
		 * 未达录入
		 */
		public static final String NOTMATCH_INPUT = "com.yzj.ebs.notmatch.input";

		/**
		 * 未达审核
		 */
		public static final String NOTMATCH_AUTH = "com.yzj.ebs.notmatch.auth";

	}

	/**
	 * 创建于:2012-8-21<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 流程变量的Key常量定义
	 * 
	 * @author 陈林江
	 * @version 1.0.0
	 */
	public static class ProcessVariableKey {
		
		
		/**
		 * 业务主键
		 */
		public static final String BUSINESSKEY="BUSINESSKEY";
		
		/**
		 * 机构号
		 */
		public static final String ORGID="ORGID";
		
		/**
		 * 任务来自何方
		 */
		public static final String TASKSOURCE="TASKSOURCE";

		/**
		 * 任务处理结果
		 */
		public static final String RESULT = "RESULT";

		/**
		 * 业务金额信息
		 */
		public static final String CREDIT = "CREDIT";

		/**
		 * 人工录入额度
		 */
		public static final String CREDIT_MI_INPT = "CREDIT_MI_INPT";

		/**
		 * 录入审核额度
		 */
		public static final String CREDIT_MI_AUTH = "CREDIT_MI_AUTH";

		/**
		 * 人工初验额度
		 */
		public static final String CREDIT_MP_FST = "CREDIT_MP_FST";

		/**
		 * 人工复验额度
		 */
		public static final String CREDIT_MP_SND = "CREDIT_MP_SND";

		/**
		 * 主管验印额度
		 */
		public static final String CREDIT_MP_AUTH = "CREDIT_MP_AUTH";

		/**
		 * 未达录入类型
		 */
		public static final String NOTMATCHINPUTTYPE = "NOTMATCHINPUTTYPE";

		/**
		 * 未达处理情况
		 */
		public static final String NOTMATCHRESULT = "NOTMATCHRESULT";

		/**
		 * 发起删除的那个节点
		 */
		public static final String PRENODE = "PRENODE";

		/**
		 * 验印类型
		 */
		public static final String SEALTYPE = "SEALTYPE";

		/**
		 * 获取所有可能值
		 * 
		 * @return 可能值集合
		 */
		public static String[] getAllValues() {
			return new String[] { RESULT, CREDIT, CREDIT_MP_FST, CREDIT_MP_SND,
					CREDIT_MP_AUTH, NOTMATCHINPUTTYPE, NOTMATCHRESULT, PRENODE,
					SEALTYPE, CREDIT_MI_INPT, CREDIT_MI_AUTH };
		}
	}

	/**
	 * 
	 * 创建于:2012-10-10<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 发起删除的节点流程变量定义
	 * 
	 * @author 陈林江
	 * @version 1.0
	 */
	public class PreNodeVar {
		/**
		 * 数据补录
		 */
		public static final String MANUALINPUT = "MANUALINPUT";
		/**
		 * 录入审核
		 */
		public static final String MANUALAUTH = "MANUALAUTH";
		/**
		 * 人工初验
		 */
		public static final String SEALFST = "SEALFST";
		/**
		 * 人工复验
		 */
		public static final String SEALSND = "SEALSND";
		/**
		 * 主管验印
		 */
		public static final String SEALAUTH = "SEALAUTH";
		/**
		 * 未达录入
		 */
		public static final String NOTMATCHINPUT = "NOTMATCHINPUT";
		/**
		 * 未达审核
		 */
		public static final String NOTMATCHAUTH = "NOTMATCHAUTH";
		
	}

	/**
	 * 
	 * 创建于:2012-10-10<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 发起删除的节点对应的任务名称
	 * 
	 * @author 陈林江
	 * @version 1.0
	 */
	public class PreNodeTaskName {
		/**
		 * 数据补录
		 */
		public static final String MANUALINPUT = "数据补录";
		/**
		 * 录入审核
		 */
		public static final String MANUALAUTH = "数据复核";
		/**
		 * 人工初验
		 */
		public static final String SEALFST = "人工初验";
		/**
		 * 人工复验
		 */
		public static final String SEALSND = "人工复验";
		/**
		 * 主管验印
		 */
		public static final String SEALAUTH = "主管验印";
		/**
		 * 未达录入
		 */
		public static final String NOTMATCHINPUT = "未达录入";
		/**
		 * 未达审核
		 */
		public static final String NOTMATCHAUTH = "未达审核";
		
	}

	/**
	 * 创建于:2012-9-25<br>
	 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
	 * 任务处理结果
	 * 
	 * @author 陈林江
	 * @version 1.0.0
	 */
	public enum TaskResult {

		/**
		 * 成功通过
		 */
		SUCCESS("SUCCESS"),

		/**
		 * 不通过
		 */
		FAIL("FAIL"),

		/**
		 * 不通过
		 */
		DELETE("DELETE");

		private String value;

		private TaskResult(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}

	}

		/**
		 * 创建于:2012-10-15<br>
		 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
		 * 未达处理结果
		 * 
		 * @author 陈林江
		 * @version 1.0.0
		 */
		public class NotMatchResult {

			/**
			 * 完成
			 */
			public static final String FINISH = "FINISH";

			/**
			 * 未完成
			 */
			public static final String UNFINISH = "UNFINISH";
		}
		
		/**
		 * 创建于:2012-12-10<br>
		 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
		 * 任务来源
		 * 
		 * @author 陈林江
		 * @version 1.0.0
		 */
		public class TaskSource {

			/**
			 * 扫描
			 */
			public static final String SCAN = "SCAN";

			/**
			 * 网银
			 */
			public static final String EBANK = "EBANK";
		}
		
		
//		public  class REFParam{
//			
//			public static final Map<String,String> getrefpp = new HashMap<String,String>();
//			
//		
//		}
		
	
		
}
