/**
 * DrawLayer.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-14
 */
package com.yzj.wf.scan.ui.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取图层
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class DrawLayer {
	
	public static void drawText(Graphics2D g, LayerVO vo, boolean isfullScreen) {
		TextLayout layout = getTextLayout(g, vo);
		Point dispPoint = FontUtil.calcDisp(layout, vo.getMarkPosition(), vo
				.getMarkDisp());
		layout.draw(g, (float) vo.getX() + dispPoint.x, (float) vo.getY()
				+ dispPoint.y);
		if (vo.isSelect() && !isfullScreen) {
			Rectangle2D bounds = layout.getBounds();
			bounds.setRect((bounds.getX() - 2 + vo.getX() + dispPoint.x),
					(bounds.getY() - 2 + vo.getY() + dispPoint.y), (4 + bounds
							.getWidth()), (4 + bounds.getHeight()));
			g.setColor(Color.BLUE);
			g.draw(bounds);
		}
	}
	
	public static boolean isContainsXy(Graphics2D g, LayerVO vo, int x, int y) {
		boolean result = false;
		TextLayout layout = getTextLayout(g, vo);
		Point dispPoint = FontUtil.calcDisp(layout, vo.getMarkPosition(), vo
				.getMarkDisp());
		Rectangle2D bounds = layout.getBounds();
		double layerWidth = bounds.getWidth() + 4;
		double layerHeight = bounds.getHeight() + 4;
		double layerX = bounds.getX() + vo.getX() + dispPoint.getX() - 2;
		double layerY = bounds.getY() + vo.getY() + dispPoint.getY() - 2;
		bounds.setRect(layerX, layerY, layerWidth, layerHeight);
		result = bounds.contains(x, y);
		return result;
	}
	
	private static TextLayout getTextLayout(Graphics2D g, LayerVO vo) {
		int fontSize = vo.getFontSize();
		int bold = vo.isFontBold() ? Font.BOLD : 0;
		int italic = vo.isFontItalic() ? Font.ITALIC : 0;
		Font font = new Font(vo.getFontStyle(), bold | italic, fontSize);
		Map<TextAttribute, Object> attribs = new HashMap<TextAttribute, Object>();
		attribs.put(TextAttribute.FONT, font);
		attribs.put(TextAttribute.FOREGROUND, new Color(vo.getFontColor()));

		TextLayout layout = new TextLayout(vo.getDesc(), attribs, g
				.getFontRenderContext());
		return layout;
	}
}
