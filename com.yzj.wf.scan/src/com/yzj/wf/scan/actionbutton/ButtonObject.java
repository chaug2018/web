/**
 * ButtonObject.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-14
 */
package com.yzj.wf.scan.actionbutton;

import com.yzj.wf.scan.util.IConfirmAction;

/**
 * 按钮对象
 * @author HuLiang
 * @version 1.0.0
 */
/**
 * 支持不同的形式的modifiers，增加属性modifiers
 * @author LiuQiangQiang
 * @version 1.0.1
 */
/**
 * 修改原属性toolTipMsg为buttonName，增加属性toolTipText
 * @author Heisenberg
 * @version 1.0.2
 */
public class ButtonObject {
	/**
	 * 处理的事件
	 */
	private IConfirmAction action;
	/**
	 * 按钮名称
	 */
	private String buttonName;
	/**
	 * 按钮的浮动提示
	 */
	private String toolTipText;
	/**
	 * 图标的相对路径
	 */
	private String iconAddr;
	/**
	 * 快捷键
	 */
	private int keyEvent;
	/**
	 * 快捷键组合键
	 */
	private int modifiers;
	
	public ButtonObject() {
	}
	
	public ButtonObject(IConfirmAction action, String buttonName, String toolTipText, String iconAddr, int keyEvent) {
		this(action, buttonName, toolTipText, iconAddr, keyEvent, 0);
	}
	
	public ButtonObject(IConfirmAction action, String buttonName, String toolTipText, String iconAddr, int keyEvent, int modifiers) {
		this.action = action;
		this.buttonName = buttonName;
		this.toolTipText = toolTipText;
		this.iconAddr = iconAddr;
		this.keyEvent = keyEvent;
		this.modifiers = modifiers;
	}

	public IConfirmAction getAction() {
		return action;
	}

	public void setAction(IConfirmAction action) {
		this.action = action;
	}

	public String getButtonName() {
		return buttonName;
	}

	public void setButtonName(String buttonName) {
		this.buttonName = buttonName;
	}

	public String getIconAddr() {
		return iconAddr;
	}

	public void setIconAddr(String iconAddr) {
		this.iconAddr = iconAddr;
	}

	public int getKeyEvent() {
		return keyEvent;
	}

	public void setKeyEvent(int keyEvent) {
		this.keyEvent = keyEvent;
	}

	public int getModifiers() {
		return modifiers;
	}

	public void setModifiers(int modifiers) {
		this.modifiers = modifiers;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
}
