/**
 * StackLayout.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-21
 */
package com.yzj.wf.scan.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

/**
 * 模拟SWT中StackLayout的使用
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class StackLayout implements LayoutManager2 {
	public static final String BOTTOM = "bottom";
	public static final String TOP = "top";
	private List<Component> components;

	public StackLayout() {
		this.components = new LinkedList<Component>();
	}

	public void addLayoutComponent(Component comp, Object constraints) {
		synchronized (comp.getTreeLock()) {
			if ("bottom".equals(constraints))
				this.components.add(0, comp);
			else if ("top".equals(constraints))
				this.components.add(comp);
			else
				this.components.add(comp);
		}
	}

	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, "top");
	}

	public void removeLayoutComponent(Component comp) {
		synchronized (comp.getTreeLock()) {
			this.components.remove(comp);
		}
	}

	public float getLayoutAlignmentX(Container target) {
		return 0.5F;
	}

	public float getLayoutAlignmentY(Container target) {
		return 0.5F;
	}

	public void invalidateLayout(Container target) {
	}

	public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			int width = 0;
			int height = 0;

			for (Component comp : this.components) {
				Dimension size = comp.getPreferredSize();
				width = Math.max(size.width, width);
				height = Math.max(size.height, height);
			}

			Insets insets = parent.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			return new Dimension(width, height);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			int width = 0;
			int height = 0;

			for (Component comp : this.components) {
				Dimension size = comp.getMinimumSize();
				width = Math.max(size.width, width);
				height = Math.max(size.height, height);
			}

			Insets insets = parent.getInsets();
			width += insets.left + insets.right;
			height += insets.top + insets.bottom;

			return new Dimension(width, height);
		}
	}

	public Dimension maximumLayoutSize(Container target) {
		return new Dimension(2147483647, 2147483647);
	}

	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			int width = parent.getWidth();
			int height = parent.getHeight();

			Rectangle bounds = new Rectangle(0, 0, width, height);

			int componentsCount = this.components.size();

			for (int i = 0; i < componentsCount; ++i) {
				Component comp = (Component) this.components.get(i);
				comp.setBounds(bounds);
				parent.setComponentZOrder(comp, componentsCount - i - 1);
			}
		}
	}
}
