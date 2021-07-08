/**
 * LayerVO.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-13
 */
package com.yzj.wf.scan.ui.viewer;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;

import com.yzj.wf.scan.util.ImageStoreDef.*;

/**
 * 批注类
 * 
 * @author HuLiang
 * @version 1.0.0
 * 
 * 添加equals方法
 * @author LiuQiangQiang
 * @version 1.0.1
 */
public class LayerVO implements Cloneable {

	// 标注的颜色
	private int fontColor = 0;
	// 标注的字体大小
	private int fontSize = 20;
	// 标注的位置
	private int markPosition = MarkPositionConst.MIDDLE;
	// 标注偏离
	private int markDisp = 5;

	private int width;

	private int hight;
	// 是否是粗体
	private boolean fontBold = false;
	// 是否是斜体
	private boolean fontItalic = false;
	// 标注坐标x
	private double x;
	// 标注坐标y
	private double y;
	// 标注的字体
	private String fontStyle = "宋体";
	// 图层描述
	private String desc = "";
	// 是否被选中
	private boolean isSelect = false;
	//格式化坐标
	private static final DecimalFormat format = (DecimalFormat) NumberFormat.getInstance();

	public LayerVO() {

	}

	/**
	 * full construct
	 * 
	 * @param desc
	 *            图层描述
	 * @param fontStyle
	 *            标注的字体
	 * @param fontSize
	 *            标注的字体大小
	 * @param fontColor
	 *            标注的颜色
	 * @param fontBold
	 *            是否是粗体
	 * @param fontItalic
	 *            是否是斜体
	 * @param markPosition
	 *            标注的位置
	 * @param markDisp
	 *            标注偏离
	 * @param x
	 *            X坐标
	 * @param y
	 *            Y坐标
	 */
	public LayerVO(String desc, String fontStyle, int fontSize, int fontColor,
			boolean fontBold, boolean fontItalic, int markPosition,
			int markDisp, int x, int y) {
		this.desc = desc;
		this.fontStyle = fontStyle;
		this.fontSize = fontSize;
		this.fontColor = fontColor;
		this.fontBold = fontBold;
		this.fontItalic = fontItalic;
		this.markPosition = markPosition;
		this.markDisp = markDisp;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 初始化数据<br>
	 * 图层描述,标注的字体,标注的字体大小,标注的颜色,是否是粗体,是否是斜体,标注的位置,标注偏离,X坐标,Y坐标
	 * 
	 * @param msg 单个批注描述
	 */
	public void initData(String msg) {
		try{
			StringTokenizer layerSt = new StringTokenizer(msg, ",");
			this.desc = layerSt.nextToken();
			this.fontStyle = layerSt.nextToken();
			this.fontSize = Integer.valueOf(layerSt.nextToken());
			this.fontColor = Integer.valueOf(layerSt.nextToken());
			this.fontBold = Boolean.valueOf(layerSt.nextToken());
			this.fontItalic = Boolean.valueOf(layerSt.nextToken());
			this.markPosition = Integer.valueOf(layerSt.nextToken());
			this.markDisp = Integer.valueOf(layerSt.nextToken());
			this.x = Double.valueOf(layerSt.nextToken());
			this.y = Double.valueOf(layerSt.nextToken());
			this.width = Integer.valueOf(layerSt.nextToken());
			this.hight = Integer.valueOf(layerSt.nextToken());
		}catch(Exception e){
			System.out.println("图层描述初始化数据失败 "+e.getMessage());
		}
	}
	
	public String getDataMsg(){
		
		StringBuilder result = new StringBuilder();
		format.applyLocalizedPattern("####0.000");
		result.append(desc);
		result.append(",");
		result.append(fontStyle);
		result.append(",");
		result.append(fontSize);
		result.append(",");
		result.append(fontColor);
		result.append(",");
		result.append(fontBold);
		result.append(",");
		result.append(fontItalic);
		result.append(",");
		result.append(markPosition);
		result.append(",");
		result.append(markDisp);
		result.append(",");
		result.append(format.format(x));
		result.append(",");
		result.append(format.format(y));
		result.append(",");
		result.append(width);
		result.append(",");
		result.append(hight);
		
		return result.toString();
	}

	public int getFontColor() {
		return fontColor;
	}

	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getMarkPosition() {
		return markPosition;
	}

	public void setMarkPosition(int markPosition) {
		this.markPosition = markPosition;
	}

	public int getMarkDisp() {
		return markDisp;
	}

	public void setMarkDisp(int markDisp) {
		this.markDisp = markDisp;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}

	public boolean isFontBold() {
		return fontBold;
	}

	public void setFontBold(boolean fontBold) {
		this.fontBold = fontBold;
	}

	public boolean isFontItalic() {
		return fontItalic;
	}

	public void setFontItalic(boolean fontItalic) {
		this.fontItalic = fontItalic;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
	public boolean equals(LayerVO layer){
		if(this.desc.equals(layer.getDesc())
				&& this.fontBold == layer.isFontBold()
				&& this.fontItalic == layer.isFontItalic()
				&& this.fontColor == layer.getFontColor()
				&& this.fontSize == layer.getFontSize()
				&& this.fontStyle == layer.getFontStyle()
				&& this.hight == layer.getHight()
				&& this.markDisp == layer.getMarkDisp()
				&& this.markPosition == layer.getMarkPosition()
				&& this.width == layer.getWidth()
				&& this.x == layer.getX()
				&& this.y == layer.getY())
			return true;
		else 
			return false;
	}
	
	public LayerVO clone() {
		try {
			return (LayerVO) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
