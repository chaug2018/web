/**
 * ImageStoreDef.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-9
 */
package com.yzj.wf.scan.util;

/**
 * 影像控件枚举值
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class ImageStoreDef {

	/**
	 * 控件显示的相关名称
	 */
	public static class ShowName {
		
		public static final String ROOTNODENAME = "影像结构树";
		
		public static final String ROOTNODE = "影像查看";

		public static final String SHOWTITLE = "影像质检";
	}

	/**
	 * ImageNode变更状态
	 */
	public static class ImageState {
		/**
		 * 正常未变更
		 */
		public static int NORMAL = 0;
		/**
		 * 添加新节点
		 */
		public static int APPEND = 1;
		/**
		 * 更新图片
		 */
		public static int UPDATEPIC = 2;
		/**
		 * 更新属性
		 */
		public static int UPDATEPROPERTY = 3;
		/**
		 * 删除图片
		 */
		public static int DELETE = 4;
	}

	/**
	 * 方便扩展
	 */
	public static class TreePaneState {
		/**
		 * 树tab
		 */
		public static int TREE_STATE = 0;
	}

	/**
	 * 
	 * 文档集常用属性定义
	 */
	public static class ObjectProperty {
		/**
		 * 业务类型
		 */
		public static final String QUERYTYPE = "ob_yxzx";
		/**
		 * 业务ID
		 */
		public static final String IMAGEINDEXID = "imageIndexId";
		/**
		 * 批注
		 */
		public static final String POSTILLAYER = "postillayer";
		/**
		 * 自定义节点
		 */
		public static final String USERDEFIND = "userdefind";
		/**
		 * 位置索引
		 */
		public static final String INDEXNO = "indexno";		
		/**
		 * 扩展信息
		 */
		public static final String EXTENDINFO = "extendinfo";
		/**
		 * 图片状态
		 */
		public static final String IMAGESTATE = "imagestate";
		/**
		 * 客户编号
		 */
		public static final String CUSTOMERID = "customerId";
		/**
		 * 系统编号
		 */
		public static final String SYSTEMNO = "SystemNo";
	}
	
	public static class ImageType{
		/**
		 * 扫描后票据状体
		 */
		public static final String SCANTYPE = "0";
		/**
		 * 质检后票据状态
		 */
		public static final String IMAGECHECKTYPE = "1";
		/**
		 * 票据删除状态
		 */
		public static final String IMAGEDELETE = "0";
		
	}

	/**
	 * 图片列表面板模式
	 * 
	 */
	public static class FileView {

		public static final String PREVIEW_VIEW = "PREVIEW_VIEW";

		public static final String THUMB_VIEW = "THUMB_VIEW";
	}

	/**
	 * 批注位置常量
	 * 
	 */
	public static class MarkPositionConst {

		public final static int UPPER_LEFT = 0; // 左上角

		public final static int UPPER = 1; // 上侧

		public final static int UPPER_RIGHT = 2; // 右上角

		public final static int LEFT = 3; // 左侧

		public final static int MIDDLE = 4;// 居中

		public final static int RIGHT = 5;// 右侧

		public final static int LOWER_LEFT = 6;// 左下角

		public final static int LOWER = 7;// 下侧

		public final static int LOWER_RIGHT = 8;// 右下角
	}

	/**
	 * 系统操作类型
	 * 
	 * @author HuLiang
	 * @version 1.0.0
	 */
	public static class OperationType {
		/**
		 * 扫描类型
		 */
		public final static String ScanType = "ScanType";
		/**
		 * 影像查看类型
		 */
		public final static String ImageSeeType = "ImageSeeType";
		/**
		 * 影像操作类型
		 */
		public final static String ImageOperateType = "ImageOperateType";

	}

	/**
	 * 文件存取方式类型
	 * 
	 * @author HuLiang
	 * @version 1.0.0
	 */
	public static class FileStoreType {
		/**
		 * 存储方式
		 */
		public final static String StoreType = "StoreType";
		/**
		 * 共享目录方式
		 */
		public final static String ShareDir = "ShareDir";
		/**
		 * FTP方式
		 */
		public final static String FtpType = "FtpType";

	}

	/**
	 * Applet参数Key
	 * 
	 * @author HuLiang
	 * @version 1.0.0
	 */
	public static class AppletParamKey {
		/**
		 * 操作类型
		 */
		public final static String OperationType = "OperationType";
		/**
		 * 文件存取方式
		 */
		public final static String FileStoreType = "FileStoreType";
		
		/**
		 * 传入查看业务类型树
		 */
		public final static String PURVIEW = "PurView";
		
		/**
		 * 批注标注
		 */
		public final static String ALLOWPOSTIL = "AllowPostil";
		
		/**
		 * 删除标志
		 */
		public final static String ALLOWDELETE = "AllowDelete";
		
		/**
		 * 系统类型
		 */
		public final static String SYSTEMTYPE = "SystemType";
		
		/**
		 * 系统代码
		 */
		public final static String SYSTEMNO = "SystemNo";
		
		/**
		 * 图片显示标志
		 */
		public final static String ALLIMAGESHOW = "AllImageShow";
		
		/**
		 * 机构人员
		 */
		public final static String OPCODE = "OpCode";
		
		/**
		 * 机构号
		 */
		public final static String POINTNO = "PointNo";
	}

	/**
	 * PropertyKey参数Key
	 * 
	 * @author HuLiang
	 * @version 1.0.0
	 */
	public static class PropertyKey {
		/**
		 * 操作类型
		 */
		public final static String ShareDir = "ShareDir";
	}
}
