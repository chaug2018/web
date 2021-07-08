package com.yzj.wf.scan.init;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.yzj.wf.scan.action.BatchAddFileAction;
import com.yzj.wf.scan.action.BatchAntiClockWiseRotateAction;
import com.yzj.wf.scan.action.BatchClockwiseRotateAction;
import com.yzj.wf.scan.action.BatchDeleteImageAction;
import com.yzj.wf.scan.action.ChangePozisionAction;
import com.yzj.wf.scan.action.SaveImageAction;
import com.yzj.wf.scan.actionbutton.ButtonObject;
import com.yzj.wf.scan.paramdefine.ParamDefine;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 生成按钮集合
 * @author 陈林江
 * @version 1.0.1
 */
public class ScanButtonAction {

	public static List<ButtonObject> initTools() throws Exception {
		List<ButtonObject> result = new ArrayList<ButtonObject>();
		result.add(new ButtonObject(new BatchAddFileAction(), ParamDefine.addImage, "CTRL + N", "/icons/image_add.png", KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
		result.add(new ButtonObject(new BatchAntiClockWiseRotateAction(), ParamDefine.reverseRotation, "CTRL + L", "/icons/image_spin_anticlockwise_90.png", KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
		result.add(new ButtonObject(new BatchClockwiseRotateAction(), ParamDefine.positiveRotation, "CTRL + R", "/icons/image_spin_clockwise_90.png", KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
		result.add(new ButtonObject(ChangePozisionAction.getInstance(), ParamDefine.changePozision, "CTRL + F", "/icons/changepozision.png", KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
//		result.add(new ButtonObject(ReplaceImageAction.getInstance(), ParamDefine.replaceImg, "CTRL + V", "/icons/image_spin_clockwise_90.png", KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		result.add(new ButtonObject(new BatchDeleteImageAction(), ParamDefine.deleteImage, "CTRL + D", "/icons/image_delete.png", KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
		result.add(new ButtonObject(SaveImageAction.getInstance(), ParamDefine.upload, "CTRL + U", "/icons/image_upload.png", KeyEvent.VK_U, KeyEvent.CTRL_DOWN_MASK));
		return result;
	}

}
