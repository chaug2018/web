package com.yzj.wf.scan.paramdefine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建于:2012-8-3<br>
 * 版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 参数定义
 * 
 * @author 陈林江
 * @version 1.0
 */
public class ParamDefine {
	
	/**
	 * 影像插入后是否按照每笔业务的影像数目进行自动格式化
	 */
	public static boolean autoFormat=true;
	
	/**
	 * 是否在上传动作完毕后自动删除图片及图片列表
	 */
	public static boolean isDelete=true;

	/**
	 * 每笔业务票据张数
	 */
	public static int billNum = 2;
	/**
	 * 票据类型
	 */
	public static String billType = "";
	public static List<String> busTypeNames;
	public static List<String> busTypes;
	

	public static String billName = "票据";
	public static String billListName = "票据列表";
	public static String selectDevice = "驱动选择";
	public static String scanSetting = "扫描设置";
	public static String scanImage = "影像扫描";
	public static String scanInsert = "扫描插入";
	public static String busType = "业务类型";
	public static String addImage = "添加影像";
	public static String reverseRotation = "反向旋转";
	public static String positiveRotation = "正向旋转";
	public static String changePozision = "交换位置";
	public static String replaceImg = "替换图像";
	public static String deleteImage = "删除影像";
	public static String upload = "上传存储";

	public static String voucherType = "凭证类型";
	public static String voucherPage = "凭证页数";
	public static String voucherPageDesc = "每笔任务包含的票据页数";

	public static String positiveSide = "_正";
	public static String negativeSide = "_反";
	public static String uploaded = "(已上传)";
	public static String unUploaded = "(未上传)";

	public static String fileSelection = "文件选择";
	public static String confireMessage = "确认信息";
	public static String confire = "确认";
	public static String noticeMessage = "提示信息";
	public static String notice = "提示";
	public static String pleaseWaiting = "请等待... ";
	public static String initFail = "初始化扫描控件失败:";

	public static String dealingImage = "影像处理中...";
	public static String addingImage = "影像添加中...";
	public static String rotating = "影像旋转中...";
	public static String uploading = "影像上传中...";
	public static String uploadFinish = "影像上传完毕!";
	public static String noImageToUpload = "没有需要上传的影像";
	public static String finshed = "(已成功:";
	public static String datas = "笔数据)";

	public static String addAtEnd = "确定在左侧列表的末尾添加新的影像吗?";
	public static String confireInsertAtNode_s = "<html>确定在节点<font color=red>";
	public static String confireInsertAtNode_e = "</font>后添加新的影像吗?</html>";

	public static String rotateNotice_a = "<html>请在左侧节点中选择至少<font color=red>1</font>张需要<font color=red>旋转</font>的影像资料！</html>";
	public static String rotateNotice_b = "<html>所选节点下没有可<font color=red>反向旋转</font>的影像资料！</html>";
	public static String rotateSuccess = "影像旋转成功！";
	public static String rotateFail = "影像正向旋转失败！失败原因：";

	public static String saveNotice_a = "<html>符合条件的影像共计<font color=red>";
	public static String saveNotice_b = "</font>张<font color=red>(";
	public static String saveNotice_c = "笔业务)</font>确定<font color=red>上传存储</font>吗？</html>";
	public static String saveNotice_d = "<html>符合条件的影像共计<font color=red>";
	public static String saveNotice_e = "</font>张,不是<font color=red>";
	public static String saveNotice_f = "(每笔业务包含的影像页数)</font>的倍数,请检查!</html>";
	public static String saveNotice_g = "<html>业务节点<font color=red>[";
	public static String saveNotice_h = "]</font>下只有<font color=red>";
	public static String saveNotice_i = "</font>张影像,请检查!</html>";

	public static String deleteImageNotice = "<html>请在左侧节点中选择至少<font color=red>1</font>张需要<font color=red>删除</font>的影像资料！</html>";
	public static String deleteConfire_a = "<html>已选择将要删除的影像资料<font color=red>";
	public static String deleteConfire_b = "</font>张，确认<font color=red>删除</font>吗？</html>";

	public static String noticeAtAddEnd_a = "<html>目前共有<font color=red>";
	public static String noticeAtAddEnd_b = "</font>页票据图像,不是<font color=red>";
	public static String noticeAtAddEnd_c = "(每笔业务票据数)</font>的倍数,请检查!!</html>";

	public static String insertConfire_a = "<html>确认在节点<font color=red>";
	public static String insertConfire_b = "</font>后插入扫描影像？</html>";
	public static String insertConfire_c = "<html>根节点<font color=red>";
	public static String insertConfire_d = "</font>不能作为<font color=red>扫描插入</font>点，请重新选择！</html>";
	public static String insertConfire_e = "</font>的第一个位置开始插入扫描影像？</html>";
	public static String insertConfire_f = "<html>请在左侧列表中选择<font color=red>1</font>个影像节点作为<font color=red>扫描插入</font>点！</html>";

	public synchronized static void initBusTypes(String string)
			throws Exception {
		busTypes = new ArrayList<String>();
		busTypeNames = new ArrayList<String>();
		String[] types = string.split(",");
		for (int i = 0; i < types.length; i = i + 2) {
			busTypes.add(types[i]);
			busTypeNames.add(types[i + 1]);
		}
		billType = busTypes.get(0);
	}

	/**
	 * 初始化语言参数
	 */
	public static void initLanguage(String languageName) {
		ButtonNameConfig config = ButtonNameConfig.getInstance("/"
				+ languageName + ".properties");
		init(config);
	}
	
	/**
	 * 初始化语言参数
	 */
	public static void initLanguage(File languageFile) {
		ButtonNameConfig config = ButtonNameConfig.getInstance(languageFile);
		init(config);
	}

	/**
	 * 根据配置文件初始化显示语言
	 * @param config
	 */
	private static void init(ButtonNameConfig config) {
		if (config.getPropertyValue("billName") != null)
			billName = config.getPropertyValue("billName");
		if (config.getPropertyValue("billListName") != null)
			billListName = config.getPropertyValue("billListName");
		if (config.getPropertyValue("selectDevice") != null)
			selectDevice = config.getPropertyValue("selectDevice");
		if (config.getPropertyValue("scanSetting") != null)
			scanSetting = config.getPropertyValue("scanSetting");
		if (config.getPropertyValue("scanImage") != null)
			scanImage = config.getPropertyValue("scanImage");
		if (config.getPropertyValue("scanInsert") != null)
			scanInsert = config.getPropertyValue("scanInsert");
		if (config.getPropertyValue("busType") != null)
			busType = config.getPropertyValue("busType");
		if (config.getPropertyValue("addImage") != null)
			addImage = config.getPropertyValue("addImage");
		if (config.getPropertyValue("reverseRotation") != null)
			reverseRotation = config.getPropertyValue("reverseRotation");
		if (config.getPropertyValue("positiveRotation") != null)
			positiveRotation = config.getPropertyValue("positiveRotation");
		if (config.getPropertyValue("deleteImage") != null)
			deleteImage = config.getPropertyValue("deleteImage");
		if (config.getPropertyValue("upload") != null)
			upload = config.getPropertyValue("upload");

		if (config.getPropertyValue("voucherType") != null)
			voucherType = config.getPropertyValue("voucherType");
		if (config.getPropertyValue("voucherPage") != null)
			voucherPage = config.getPropertyValue("voucherPage");
		if (config.getPropertyValue("voucherPageDesc") != null)
			voucherPageDesc = config.getPropertyValue("voucherPageDesc");

		if (config.getPropertyValue("positiveSide") != null)
			positiveSide = config.getPropertyValue("positiveSide");
		if (config.getPropertyValue("negativeSide") != null)
			negativeSide = config.getPropertyValue("negativeSide");
		if (config.getPropertyValue("uploaded") != null)
			uploaded = config.getPropertyValue("uploaded");
		if (config.getPropertyValue("unUploaded") != null)
			unUploaded = config.getPropertyValue("unUploaded");

		if (config.getPropertyValue("fileSelection") != null)
			fileSelection = config.getPropertyValue("fileSelection");
		if (config.getPropertyValue("confireMessage") != null)
			confireMessage = config.getPropertyValue("confireMessage");
		if (config.getPropertyValue("confire") != null)
			confire = config.getPropertyValue("confire");
		if (config.getPropertyValue("noticeMessage") != null)
			noticeMessage = config.getPropertyValue("noticeMessage");
		if (config.getPropertyValue("notice") != null)
			notice = config.getPropertyValue("notice");
		if (config.getPropertyValue("pleaseWaiting") != null)
			pleaseWaiting = config.getPropertyValue("pleaseWaiting");
		if (config.getPropertyValue("initFail") != null)
			initFail = config.getPropertyValue("initFail");

		if (config.getPropertyValue("dealingImage") != null)
			dealingImage = config.getPropertyValue("dealingImage");
		if (config.getPropertyValue("addingImage") != null)
			addingImage = config.getPropertyValue("addingImage");
		if (config.getPropertyValue("rotating") != null)
			rotating = config.getPropertyValue("rotating");
		if (config.getPropertyValue("uploading") != null)
			uploading = config.getPropertyValue("uploading");
		if (config.getPropertyValue("uploadFinish") != null)
			uploadFinish = config.getPropertyValue("uploadFinish");
		if (config.getPropertyValue("noImageToUpload") != null)
			noImageToUpload = config.getPropertyValue("noImageToUpload");
		if (config.getPropertyValue("finshed") != null)
			finshed = config.getPropertyValue("finshed");
		if (config.getPropertyValue("datas") != null)
			datas = config.getPropertyValue("datas");

		if (config.getPropertyValue("addAtEnd") != null)
			addAtEnd = config.getPropertyValue("addAtEnd");
		if (config.getPropertyValue("confireInsertAtNode_s") != null)
			confireInsertAtNode_s = config
					.getPropertyValue("confireInsertAtNode_s");
		if (config.getPropertyValue("confireInsertAtNode_e") != null)
			confireInsertAtNode_e = config
					.getPropertyValue("confireInsertAtNode_e");

		if (config.getPropertyValue("rotateNotice_a") != null)
			rotateNotice_a = config.getPropertyValue("rotateNotice_a");
		if (config.getPropertyValue("rotateNotice_b") != null)
			rotateNotice_b = config.getPropertyValue("rotateNotice_b");
		if (config.getPropertyValue("rotateSuccess") != null)
			rotateSuccess = config.getPropertyValue("rotateSuccess");
		if (config.getPropertyValue("rotateFail") != null)
			rotateFail = config.getPropertyValue("rotateFail");

		if (config.getPropertyValue("saveNotice_a") != null)
			saveNotice_a = config.getPropertyValue("saveNotice_a");
		if (config.getPropertyValue("saveNotice_b") != null)
			saveNotice_b = config.getPropertyValue("saveNotice_b");
		if (config.getPropertyValue("saveNotice_c") != null)
			saveNotice_c = config.getPropertyValue("saveNotice_c");
		if (config.getPropertyValue("saveNotice_d") != null)
			saveNotice_d = config.getPropertyValue("saveNotice_d");
		if (config.getPropertyValue("saveNotice_e") != null)
			saveNotice_e = config.getPropertyValue("saveNotice_e");
		if (config.getPropertyValue("saveNotice_f") != null)
			saveNotice_f = config.getPropertyValue("saveNotice_f");
		if (config.getPropertyValue("saveNotice_g") != null)
			saveNotice_g = config.getPropertyValue("saveNotice_g");
		if (config.getPropertyValue("saveNotice_h") != null)
			saveNotice_h = config.getPropertyValue("saveNotice_h");
		if (config.getPropertyValue("saveNotice_i") != null)
			saveNotice_i = config.getPropertyValue("saveNotice_i");

		if (config.getPropertyValue("deleteImageNotice") != null)
			deleteImageNotice = config.getPropertyValue("deleteImageNotice");
		if (config.getPropertyValue("deleteConfire_a") != null)
			deleteConfire_a = config.getPropertyValue("deleteConfire_a");
		if (config.getPropertyValue("deleteConfire_b") != null)
			deleteConfire_b = config.getPropertyValue("deleteConfire_b");

		if (config.getPropertyValue("noticeAtAddEnd_a") != null)
			noticeAtAddEnd_a = config.getPropertyValue("noticeAtAddEnd_a");
		if (config.getPropertyValue("noticeAtAddEnd_b") != null)
			noticeAtAddEnd_b = config.getPropertyValue("noticeAtAddEnd_b");
		if (config.getPropertyValue("noticeAtAddEnd_c") != null)
			noticeAtAddEnd_c = config.getPropertyValue("noticeAtAddEnd_c");

		if (config.getPropertyValue("insertConfire_a") != null)
			insertConfire_a = config.getPropertyValue("insertConfire_a");
		if (config.getPropertyValue("insertConfire_b") != null)
			insertConfire_b = config.getPropertyValue("insertConfire_b");
		if (config.getPropertyValue("insertConfire_c") != null)
			insertConfire_c = config.getPropertyValue("insertConfire_c");
		if (config.getPropertyValue("insertConfire_d") != null)
			insertConfire_d = config.getPropertyValue("insertConfire_d");
		if (config.getPropertyValue("insertConfire_ed") != null)
			insertConfire_e = config.getPropertyValue("insertConfire_e");
		if (config.getPropertyValue("insertConfire_f") != null)
			insertConfire_f = config.getPropertyValue("insertConfire_f");
	}
}
