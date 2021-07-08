/**
 * CustomTreeNode.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-5
 */
package com.yzj.wf.scan.tree;

import java.util.Iterator;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 扩展树节点功能
 * <P>
 * <li>扩展属性--是否为自定义节点，自定义节点可以做修改和删除，非自定义叶子节点可以增加一级自定义节点</li>
 * <li>扩展属性--保存关联的ImageNode节点</li>
 * <li>扩展属性--计算总关联的ImageNode节点数</li>
 * <li>扩展属性--是否为根节点</li>
 * <li>扩展方法--通过子节点的名称获取子节点</li>
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class CustomTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1074293390962664355L;

	/**
	 * 是否为根节点
	 */
	private boolean root = false;
	
	/**
	 * 自定义节点(true为自定义节点) 
	 */
	private boolean userDefine = false;
	
	/**
	 * 树文档集属性
	 */
	private String value = null;
	
	/**
	 * 改节点下 至少需要的图片数量
	 */
	private int snum = -1;
	
	/**
	 * 该节点的编码
	 */
	private String code;

	/**
	 * 保存当前节点关联ImageNode
	 */
	private Object[] elements;

	/**
	 * 容量增长量
	 */
	private int increams = 10;

	/**
	 * 数组使用量
	 */
	private int elementCount = 0;

	/**
	 * @param name 树名称
	 * @param value 树属性
	 */
	public CustomTreeNode(String name, String value) {
		this(name, value, false);
	}
	
	/**
	 * @param name 树名称
	 * @param value 树属性
	 * @param userDefine 自定义节点
	 */
	public CustomTreeNode(String name, String value, boolean userDefine){
		this(name, value, userDefine, -1, "");
	}
	
	/**
	 * 新增构造函数
	 * @param name 树名称
	 * @param value 树属性
	 * @param userDefine 自定义节点 true 否则 false
	 * @param num 默认-1;
	 * @param code 默认 empty string
	 */
	public CustomTreeNode(String name, String value, boolean userDefine, int snum,String code){
		super(name);
		this.value = value;
		this.userDefine = userDefine;
		this.snum = snum;
		this.code = code;		
		elements = new Object[1];		
	}
	
	/**
	 * @return object[]
	 */
	public Object[] getElements() {
		return this.elements;
	}

	/**
	 * 同时添加多个引用
	 * @param objs 不能为空
	 * @return 如果传入数组长度为零,返回false
	 */
	public synchronized boolean addObject(Object[] objs) {
		if(objs == null)
			return false;
		checkCapacity(elementCount + objs.length);
		System.arraycopy(objs, 0, this.elements, elementCount, objs.length);
		elementCount += objs.length;

		return objs.length > 0;
	}

	/**
	 * 申请容量
	 * @param minCapacity
	 */
	private void checkCapacity(int minCapacity) {
		int oldCapacity = this.elements.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = increams > 0 ? (oldCapacity + increams) : (oldCapacity + oldCapacity);
			if (minCapacity > newCapacity)
				newCapacity = minCapacity;
			Object[] tmp_obj = this.elements;
			this.elements = new Object[newCapacity];
			System.arraycopy(tmp_obj, 0, elements, 0, tmp_obj.length);
			tmp_obj = null;
		}
	}

	/**
	 * 同时移除多个引用
	 * @param objs 不能为空
	 */
	public synchronized boolean removeObject(Object[] objs) {
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] == null)
				continue;
			removeObject(objs[i]);
		}
		return true;
	}

	/**
	 * 移除单个引用
	 * @param obj 不能为空 
	 * @return 如果数组中存在返回TRUE,否则返回FALSE
	 */
	public synchronized boolean removeObject(Object obj) {
		int i = indexOf(obj);
		if (i >= 0 && i < elementCount) {
			int j = this.elementCount - i - 1;
			if (j > 0)
				System.arraycopy(this.elements, i + 1, this.elements, i, j);
			this.elements[--elementCount] = null;
			return true;
		}
		return false;
	}
	
	public synchronized boolean clearElement(){
		for(int i = 0; i < elements.length; i++)
			this.elements[i] = null;
		return true;
	}
	/**
	 * 查找在数组的位置
	 * @param obj 不能为空
	 * @return 返回对象在数组中的位置，如果不存在返回-1
	 * @see Object#equals(Object)
	 */
	public synchronized int indexOf(Object obj) {
		for (int i = 0; i < this.elementCount; i++) {
			if (this.elements[i].equals(obj))
				return i;
		}
		return -1;
	}

	/**
	 * @return the root
	 */
	@Override
	public final boolean isRoot() {
		return this.root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public final void setRoot(final boolean root) {
		this.root = root;
	}

	public String getValue() {
		return value;
	}

	/**
	 * @return 返回当前影像树直接关联的节点
	 */
	public synchronized int getElementCount() {
		return elementCount;
	}
	
	/**
	 * @return 统计节点树下面关联的所有图像节点个数 
	 */
	public int getRelateNodeAll(){
		if(this.children == null)
			return this.elementCount;
		int i = this.elementCount;
		Iterator<?> iterator = children.iterator();
		CustomTreeNode tmp_treeNode;
		while(iterator.hasNext()){
			tmp_treeNode = (CustomTreeNode) iterator.next();
			i +=  tmp_treeNode.getRelateNodeAll();
		}
		return i;
	}
	
	@Override
	public boolean isLeaf() {
		if ((this.children != null) && (this.children.size() > 0)) {
			return false;
		}
		return true;
	}
	
	/**
	 * @return 自定义类型返回true
	 */
	public boolean isUserDefine() {
		return userDefine;
	}
	
	/**
	 * 设置节点类型
	 * @param userDefine 
	 */
	public void setUserDefine(boolean userDefine) {
		this.userDefine = userDefine;
	}
	
	/**
	 * 根据子节点名称获取子节点
	 * @param obj 子节点名称
	 * @return 如果不存在返回NULL
	 */
	public CustomTreeNode getChildrenByName(Object obj){
		if(this.children == null)
			return null;
		Iterator<?> iterator = this.children.iterator();
		while(iterator.hasNext()){
			CustomTreeNode temp_Node = (CustomTreeNode) iterator.next();
			if(temp_Node.getUserObject().toString().equals(obj.toString())){
				return temp_Node;
			}
		}
		return null;
	}
	
	/**
	 * 判断本节点是否为叶子节点
	 * @return <0 说明为本节点为自定义节点，=0 说明为子节点， >0 说明为非叶子节点
	 */
	public int getUnuserDefineChildren(){
		if(this.userDefine)
			return -1;
		if(this.children == null)
			return 0;
		Iterator<?> iterator = this.children.iterator();
		int i = 0;
		while(iterator.hasNext()){
			if(!((CustomTreeNode) iterator.next()).isUserDefine()){
				i++;
				break;
			}
		}
		return i;
	}

	public int getSnum() {
		return snum;
	}

	public void setSnum(int snum) {
		this.snum = snum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
