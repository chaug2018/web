/**
 * FilePathChangeAction.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-18
 */
package com.yzj.wf.scan.action;

import com.yzj.wf.scan.ui.selection.StructuredSelection;
import com.yzj.wf.scan.util.IConfirmAction;
import com.yzj.wf.scan.util.ToolBarPanel;

/**
 * 改变目录
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class FilePathChangeAction implements IConfirmAction {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public boolean confirm() {
		/*
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			String shareDir = ReadConfig.getInstance().getPropertyValue(PropertyKey.ShareDir);
			fileChooser.setCurrentDirectory(new File(shareDir));
			int restut = fileChooser.showDialog(null, "选择路径");
			if (restut == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				ReadConfig.getInstance().setPropertyValue(PropertyKey.ShareDir, file.getAbsolutePath());
				File selectFile = new File(file.getAbsolutePath() + File.separator + SysCacheContext.instance().getDealAppletParam().getStringByParamName(ObjectProperty.IMAGEINDEXID));
				if (!selectFile.exists()) {
					selectFile.mkdirs();
				}
				// 保存选择路径放入配置文件
				// 获取业务ID
				// 通知其他面板，做了修改
				SysCacheContext.instance().appendImageNodebyDir(selectFile.getAbsolutePath(), "");
				return true;
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "共享目录设置失败! 失败原因" + ex.getMessage(), "提示信息", JOptionPane.INFORMATION_MESSAGE);
		}*/
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public StructuredSelection getStructuredSelection() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public boolean isNotifyListener() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.yinzhijie.util.IConfirmAction#isNotifyListener()
	 */
	public void setToolBarPanel(ToolBarPanel toolBarPanel) {
		
	}

}
