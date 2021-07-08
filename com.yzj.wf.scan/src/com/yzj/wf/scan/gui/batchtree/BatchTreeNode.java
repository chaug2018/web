
package com.yzj.wf.scan.gui.batchtree;

import java.io.File;
import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

import com.yzj.wf.scan.paramdefine.ParamDefine;
import com.yzj.wf.scan.util.ImageNode;

/**
 * 
 *创建于:2012-8-16<br>
 *版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
 * 数节点定义
 * @author 陈林江
 * @version 1.0.1
 */
public class BatchTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -3133545725942315342L;
	
	/**
	 * 是否被选中
	 */
	private boolean selected = false;
	
	/**
	 * 关联的数据
	 */
	private ImageNode imageNode;
	
	/**
	 * 识别的条形码
	 */
	private String barCode;
	

	public BatchTreeNode() {
		this(ParamDefine.billListName);
	}
	
	public BatchTreeNode(String name){
		super(name);
	}

	public BatchTreeNode(ImageNode imageNode) {
		super(imageNode.getFile().getName());
		this.imageNode = imageNode;
	}
	public BatchTreeNode(ImageNode imageNode,String nodeName) {
		super(nodeName);
		this.imageNode = imageNode;
	}
	
	public BatchTreeNode(File file){
		super(file.getName());
		this.imageNode = new ImageNode(file);
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		
		BatchTreeNode node = null;
		Iterator<?> iterator = null;
		
		if(this.selected){
			//儿子全部选上
			if(this.children != null){
				iterator = children.iterator();
				while(iterator.hasNext()){
					node = (BatchTreeNode) iterator.next();
					node.setSelected(true);
				}
			}
			//如果它的兄弟都选上了，让它父亲选上
			node = (BatchTreeNode) parent;
			if(node != null && !node.isSelected()){
				boolean flag = true;
				iterator = node.children.iterator();
				while(iterator.hasNext()){
					node = (BatchTreeNode) iterator.next();
					if(!node.isSelected()){
						flag = false;
						break;
					}
				}
				if(flag){
					node = (BatchTreeNode) parent;
					node.setSelected(true);
				}			
			}			
		}else{
			//判断儿子是否需要取消
			if(this.children != null){
				boolean flag = true;
				iterator = children.iterator();
				while(iterator.hasNext()){
					node = (BatchTreeNode) iterator.next();
					if(!node.isSelected()){
						flag = false;
						break;
					}
				}
				//说明全部选中
				if(flag){
					iterator = children.iterator();
					while(iterator.hasNext()){
						node = (BatchTreeNode) iterator.next();
						node.setSelected(false);
					}
				}				
			}			
			//它父节点取消选择
			node = (BatchTreeNode) parent;			
			if(node != null && node.isSelected()){
				node.setSelected(selected);
			}
		}	
	}

	public boolean isSelected() {
		return selected;
	}

	public ImageNode getImageNode() {
		return imageNode;
	}

	public void setImageNode(ImageNode imageNode) {
		this.imageNode = imageNode;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	
	public String showName(){
			String name="";
			if(imageNode==null){//非影像节点
				if(this.getParent()==null){
					name=ParamDefine.billListName+"("+getAllSize()+")";
				}else{
					int index=this.getParent().getIndex(this)+1;
					name=ParamDefine.billName+index+"("+getAllSize()+")";
				}
			}else{ //影像节点
				int index=this.getParent().getIndex(this);
				int billIndex=index/2+1;
				name+=billIndex;
				if(index%2==0){
					name+=ParamDefine.positiveSide;
				}else{
					name+=ParamDefine.negativeSide;
				}
				this.setUserObject(name);
				if(imageNode.isUploaded()){
					name+=ParamDefine.uploaded;
				}else{
					name+=ParamDefine.unUploaded;
				}
			}
			return name;
	}
	
	public int getAllSize(){
		if(getLevel() == 2){
			return 1;
		}else {
			int allCount = 0;
			if(children != null){
				for(int i=0; i< children.size(); i++){
					allCount += ((BatchTreeNode)children.get(i)).getAllSize();
				}	
			}			
			return allCount;
		}
	}
	
	/**
	 * 获取所有子节点
	 * @return
	 */
	public BatchTreeNode[] getChildNodes() {
		BatchTreeNode[] childNodes = new BatchTreeNode[this.getChildCount()];
		for (int i = 0; i < childNodes.length; i++) {
			childNodes[i] = (BatchTreeNode) this.getChildAt(i);
		}
		return childNodes;
	}
	
	public BatchTreeNode getParent() {
		return (BatchTreeNode) parent;
	}
	
}
