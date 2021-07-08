
package com.yzj.wf.scan.gui.batchtree;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.util.ImageIconFactory;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 树的样式
 * @author 陈林江
 * @version 1.0.1
 */
public class BatchTreeCellRenderer extends JPanel implements TreeCellRenderer {

	private static final long serialVersionUID = 8442555821273304525L;

	private JCheckBox box;
	private BatchTreeLabel label;

	public BatchTreeCellRenderer() {
		setLayout(null);
		add(box = new JCheckBox());
		add(label = new BatchTreeLabel());
		box.setBackground(Color.WHITE);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		setEnabled(tree.isEnabled());
		BatchTreeNode node = (BatchTreeNode) value;
		box.setSelected(node.isSelected());
		label.setText(node.showName());
		label.setFont(tree.getFont());
		label.setSelected(sel);
		label.setFocus(hasFocus);
		if (leaf && node.getImageNode() != null) {
			label.setIcon(ImageIconFactory.getImageIcon("/icons/bill.png"));
		} else if (node.getAllSize() % ParamDefine.billNum != 0) {
			label.setIcon(ImageIconFactory
					.getImageIcon("/icons/wareFolder.png"));
		} else if (isAllUpload(node)) { // 任务已上传
			label.setIcon(ImageIconFactory
					.getImageIcon("/icons/finish.png"));
		} else if (expanded) {
			label.setIcon(UIManager.getIcon("Tree.openIcon"));
		} else {
			label.setIcon(UIManager.getIcon("Tree.closedIcon"));
		}
		return this;
	}

	private boolean isAllUpload(BatchTreeNode node) {

		BatchTreeNode[] nodes = node.getChildNodes();
		if (nodes == null||node.getParent()==null) {
			return false;
		}
		for (int i = 0; i < nodes.length; i++) {
			if (!nodes[i].getImageNode().isUploaded()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dCheck = box.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		return new Dimension(dCheck.width + dLabel.width,
				dCheck.height < dLabel.height ? dLabel.height : dCheck.height);
	}

	@Override
	public void doLayout() {
		Dimension dCheck = box.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;
		if (dCheck.height < dLabel.height)
			yCheck = (dLabel.height - dCheck.height) / 2;
		else
			yLabel = (dCheck.height - dLabel.height) / 2;
		box.setLocation(0, yCheck);
		box.setBounds(0, yCheck - 1, dCheck.width, dCheck.height);
		label.setLocation(dCheck.width, yLabel);
		label.setBounds(dCheck.width, yLabel, dLabel.width - 3,
				dLabel.height - 2);
	}

	@Override
	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

	final class BatchTreeLabel extends JLabel {
		private static final long serialVersionUID = 1L;
		private boolean isSelected;
		private boolean hasFocus;

		public BatchTreeLabel() {

		}

		@Override
		public void setBackground(Color color) {
			if (color instanceof ColorUIResource)
				color = null;
			super.setBackground(color);
		}

		@Override
		public void paint(Graphics g) {
			String str;
			if ((str = getText()) != null) {
				if (0 < str.length()) {
					if (isSelected)
						g.setColor(UIManager
								.getColor("Tree.selectionBackground"));
					else
						g.setColor(UIManager.getColor("Tree.textBackground"));
					Dimension d = getPreferredSize();
					int imageOffset = 0;
					Icon currentIcon = getIcon();
					if (currentIcon != null)
						imageOffset = currentIcon.getIconWidth()
								+ Math.max(0, getIconTextGap() - 1);
					g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
							d.height);
					if (hasFocus) {
						g.setColor(UIManager
								.getColor("Tree.selectionBorderColor"));
						g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
								d.height - 1);
					}
				}
			}
			super.paint(g);
		}

		@Override
		public Dimension getPreferredSize() {
			Dimension retDimension = super.getPreferredSize();
			if (retDimension != null)
				retDimension = new Dimension(retDimension.width + 3,
						retDimension.height);
			return retDimension;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public void setFocus(boolean hasFocus) {
			this.hasFocus = hasFocus;
		}
	}

}
