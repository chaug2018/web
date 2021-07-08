/**
 * OrderImageNodeList.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-3-15
 */
package com.yzj.wf.scan.util;

import java.util.Vector;

/**
 * 保存索引信息
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class OrderImageNodeList extends Vector<ImageNode> {

	private static final long serialVersionUID = 2463358214475828749L;
	
	private int currentIndex = 0;
	
	public OrderImageNodeList(){
		super();
	}
	
	public OrderImageNodeList(int size){
		super(size);
	}
	
	public OrderImageNodeList(final ImageNode[] imageNodes){
		super(imageNodes.length);
		this.elementData = imageNodes;
		this.setSize(imageNodes.length);
	}
	
	public OrderImageNodeList(final ImageNode[] imageNodes, int index){
		super(imageNodes.length);
		this.elementData = imageNodes;
		this.setSize(imageNodes.length);
		this.currentIndex = index;
	}
	
	public final boolean isEmpty() {
		return (size() > 0) ? false : true;
	}
	
	protected final int nextImageIndex(){
		currentIndex = (currentIndex < size() -1) ? ++currentIndex : (currentIndex = 0);
		
		return currentIndex;
	}
	
	protected final int previousImageIndex(){
		currentIndex = currentIndex > 0 ? --currentIndex : (currentIndex = size()-1);
		return currentIndex;
	}
	
	public final ImageNode nextImage(){
		if(isEmpty())
			return null;
		return elementAt(nextImageIndex());
	}
	
	public final ImageNode previousImage(){
		if(isEmpty())
			return null;
		return elementAt(previousImageIndex());
	}
	
	public final ImageNode randImage(){
		currentIndex = (int) Math.round((size() -1) * Math.random());
		return elementAt(currentIndex);
	}

	public final int getCurrentIndex() {
		return currentIndex;
	}
	
	public final ImageNode currentImage(){
		if(isEmpty())
			return null;
		return elementAt(currentIndex);
	}
	
	public synchronized final void setCurrentIndex(final int currentIndex) {
		this.currentIndex = currentIndex;
	}
}
