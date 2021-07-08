/**
 * FontUtil.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:HuLiang 2011-4-14
 */
package com.yzj.wf.scan.ui.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import com.yzj.wf.scan.util.ImageStoreDef.MarkPositionConst;

/**
 * 处理批注类的字体 颜色的静态方法
 * 
 * @author HuLiang
 * @version 1.0.0
 */
public class FontUtil {

	// 字体名称
	private static String[] fontSizeItems = { "小七", "七号", "小六", "六号", "小五",
			"五号", "小四", "四号", "三号", "小二", "二号", "小一", "一号", "小初", "初号", "小特号",
			"特号", "特大号", "63磅", "72磅", "84磅", "96磅" };
	// 字符大小
	private static int[] iWordSize = new int[22];

	/**
	 * 字号线数表
	 */
	private static int[] iVertTable = { 54, 62, 72, 82, 92, 108, 124, 144, 162,
			186, 216, 246, 282, 324, 370, 432, 494, 576, 648, 740, 864, 988 };

	// 颜色中文描述
	private static String[] colorString = new String[] { "黑色", "深灰色", "灰色",
			"浅灰色", "白色", "红色", "洋红色", "粉红色", "桔黄色", "黄色", "绿色", "青色", "蓝色" };
	// 颜色
	private static Color[] colors = new Color[] { Color.BLACK, Color.DARK_GRAY,
			Color.GRAY, Color.LIGHT_GRAY, Color.WHITE, Color.RED,
			Color.MAGENTA, Color.PINK, Color.ORANGE, Color.YELLOW, Color.GREEN,
			Color.CYAN, Color.BLUE };

	private static String[] fontStyleItems;

	static {
		int dotsPerInch = Toolkit.getDefaultToolkit().getScreenResolution();

		for (int i = 0; i < 22; i++) {
			iWordSize[i] = (int) ((double) iVertTable[i] / 742.0 * dotsPerInch + 0.5);
		}

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] font = ge.getAllFonts();
		fontStyleItems = new String[font.length];
		for (int i = 0, n = font.length; i < n; i++) {
			fontStyleItems[i] = font[i].getFontName();
		}
	}
	
	public static String[] getFontSizeNames() {
		return fontSizeItems;
	}

	public static int getFontSizeNameIndex(String fontSizeName) {
		for (int i = 0; i < fontSizeItems.length; i++) {
			if (fontSizeItems[i].equals(fontSizeName)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getSizeOfWordByIndex(int fontSize) {
		if (fontSize >= 0 && fontSize < 22) {
			return iWordSize[fontSize];
		} else {
			return iWordSize[5];
		}
	}
	
	public static int getFontSize(String fontSizeName) {
		for (int i = 0; i < fontSizeItems.length; i++) {
			if (fontSizeItems[i].equals(fontSizeName)) {
				return iWordSize[i];
			}
		}
		return iWordSize[5];
	}
	
	public static int getFontSizeIndex(int fontSize) {
		int index = 5;
		int temp = 0;
		for (int i = 0; i < iWordSize.length; i++) {
			if (i == iWordSize.length - 1) {
				break;
			} else if (Math.abs(fontSize - iWordSize[i]) < Math.abs(fontSize
					- temp)) {
				temp = iWordSize[i];
				index = i;
			}
		}
		return index;
	}
	
	public static String[] getColorNames() {
		return colorString;
	}

	public static Color[] getColors() {
		return colors;
	}
	
	public static int getColorNameIndex(String colorName) {
		for (int i = 0; i < colorString.length; i++) {
			if (colorString[i].equals(colorName)) {
				return i;
			}
		}
		return -1;
	}

	public static int getColorIndex(Color color) {
		for (int i = 0; i < colors.length; i++) {
			if (colors[i].equals(color)) {
				return i;
			}
		}
		return -1;
	}

	public static Color getColorbyIndex(int index) {
		if (index < 0 || index >= colors.length) {
			return colors[0];
		}
		return colors[index];
	}
	
	public static String[] getFontNames() {
		return fontStyleItems;
	}
	
	public static int getFontNameIndex(String fontName) {
		for (int i = 0; i < fontStyleItems.length; i++) {
			if (fontStyleItems[i].equals(fontName)) {
				return i;
			}
		}
		return -1;
	}
	
	public static Point calcDisp(TextLayout layout, int markPos, int disp) {
		Point point = new Point();
		Rectangle2D rect = layout.getBounds();
		switch (markPos) {
		case MarkPositionConst.MIDDLE:
			point.x = (int) (-rect.getWidth() / 2);
			point.y = (int) (rect.getHeight() / 2);
			break;
		case MarkPositionConst.UPPER_LEFT:
			point.x = (int) (-rect.getWidth() - disp);
			point.y = -disp;
			break;
		case MarkPositionConst.UPPER:
			point.x = (int) (-rect.getWidth() / 2);
			point.y = -disp;
			break;
		case MarkPositionConst.UPPER_RIGHT:
			point.x = +disp;
			point.y = -disp;
			break;
		case MarkPositionConst.LEFT:
			point.x = (int) (-rect.getWidth() - disp);
			point.y = (int) (rect.getHeight() / 2);
			break;
		case MarkPositionConst.RIGHT:
			point.x = +disp;
			point.y = (int) (rect.getHeight() / 2);
			break;
		case MarkPositionConst.LOWER_LEFT:
			point.x = (int) (-rect.getWidth() - disp);
			point.y = (int) (rect.getHeight()) + disp;
			break;
		case MarkPositionConst.LOWER:
			point.x = (int) (-rect.getWidth() / 2);
			point.y = (int) (rect.getHeight()) + disp;
			break;
		case MarkPositionConst.LOWER_RIGHT:
			point.x = +disp;
			point.y = (int) (rect.getHeight()) + disp;
			break;
		default:
			point.x = (int) (-rect.getWidth() / 2);
			point.y = (int) (rect.getHeight() / 2);
			break;
		}
		return point;
	}
	
	public static Color getColor(int index) {
		Color color = null;
		switch (index) {
		case 0:
			color = Color.black;
			break;
		case 1:
			color = Color.darkGray;
			break;
		case 2:
			color = Color.gray;
			break;
		case 3:
			color = Color.lightGray;
			break;
		case 4:
			color = Color.white;
			break;
		case 5:
			color = Color.red;
			break;
		case 6:
			color = Color.magenta;
			break;
		case 7:
			color = Color.pink;
			break;
		case 8:
			color = Color.orange;
			break;
		case 9:
			color = Color.yellow;
			break;
		case 10:
			color = Color.green;
			break;
		case 11:
			color = Color.cyan;
			break;
		case 12:
			color = Color.blue;
			break;
		default:
			color = Color.black;
		}
		return color;

	}
	
	public static int getFontBoldItalic(boolean isBold, boolean isItalic) {
		int fontStyle = 0;
		if (isBold && !isItalic) {
			fontStyle = Font.BOLD;
		} else if (isItalic && !isBold) {
			fontStyle = Font.ITALIC;

		} else if (isItalic && isBold) {
			fontStyle = Font.BOLD | Font.ITALIC;
		} else
			fontStyle = Font.PLAIN;
		return fontStyle;
	}
}
