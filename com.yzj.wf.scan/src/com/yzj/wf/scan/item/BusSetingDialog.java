/**
 * 
 */
package com.yzj.wf.scan.item;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.yzj.wf.scan.gui.batchtree.BatchTree;
import com.yzj.wf.scan.paramdefine.ParamDefine;

/**
 *创建于:2012-8-8<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 业务类型设置窗口
 * @author 陈林江
 * @version 1.0.1
 */
public class BusSetingDialog extends JDialog  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BusSetingDialog(){
		JPanel panel=new JPanel();
		panel.setSize(350,30);
		JLabel blank=new JLabel("  ");
		JLabel typeLabel=new JLabel(ParamDefine.voucherType);
		JLabel pageLabel=new JLabel(ParamDefine.voucherPage);
		pageLabel.setToolTipText(ParamDefine.voucherPageDesc);
		final JComboBox combo=new JComboBox();	
		if(ParamDefine.busTypeNames!=null)
		for(int i=0;i<ParamDefine.busTypeNames.size();i++){
			combo.addItem(ParamDefine.busTypeNames.get(i));
		}
		final JComboBox combo1=new JComboBox();		
		combo1.addItem("1");
		combo1.addItem("2");
		combo1.addItem("3");
		combo1.addItem("4");
		combo1.addItem("5");
		combo1.addItem("6");
		combo1.addItem("7");
		combo1.addItem("8");
		combo1.setSelectedIndex(ParamDefine.billNum/2-1);
		JButton commit=new JButton(ParamDefine.confire);;
		
		panel.add(typeLabel);
		panel.add(combo);
		panel.add(pageLabel);
		panel.add(combo1);
		panel.add(blank);
		panel.add(commit);
		
		this.add(panel);
		this.setLayout(new FlowLayout());
		this.pack();
		this.setResizable(false);	
		this.setAlwaysOnTop(true);
		Dimension ds  =  Toolkit.getDefaultToolkit().getScreenSize(); 
        setLocation(ds.width / 3,   ds.height / 4); 
		commit.addMouseListener(new MouseAdapter() {
			 public void mousePressed(MouseEvent e){
				 int billNum=Integer.parseInt(combo1.getSelectedItem().toString());
				 int index=combo.getSelectedIndex();
				 if(ParamDefine.busTypes==null){
					 dispose();
					 return;
				 }
				 String billType=ParamDefine.busTypes.get(index);  
				 ParamDefine.billNum=billNum;
				 ParamDefine.billType=billType;
				 BatchTree.getInstance().formatAllNode();
				 dispose();
			 }
		});
		setVisible(true);
	}
	
	public static void main(String args[]){
		new BusSetingDialog();
	}
	protected void processWindowEvent(WindowEvent e) {
	     if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	      dispose();
	     }
	 }
	
}
