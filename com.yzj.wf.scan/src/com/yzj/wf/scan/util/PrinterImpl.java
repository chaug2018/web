/**
 * PrinterImpl.java
 * 版权所有(C) 2011 深圳市银之杰科技股份有限公司
 * 创建:LiuQiangQiang 2011-4-14
 */
package com.yzj.wf.scan.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.util.Locale;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * 打印服务
 * 
 * @author LiuQiangQiang
 * @version 1.0.0
 */
public class PrinterImpl extends JPanel implements Printable {
	
	private static final long serialVersionUID = -4792293512902680568L;

	private Image image;
	
	private PrinterJob printJob;

	private double x, y, w, h;
	
	private int imagew, imageh;
	
	private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PrinterImpl.class);
	
	public PrinterImpl(){
		printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.print.Printable#print(java.awt.Graphics, java.awt.print.PageFormat, int)
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException {
		
		if(pageIndex >= 1){
			return Printable.NO_SUCH_PAGE;
		}
		
		x = pageFormat.getImageableX();
		y = pageFormat.getImageableY();
		w = pageFormat.getImageableWidth();
		h = pageFormat.getImageableHeight();
		
		if (imagew >= imageh) {
			h = w * imageh / imagew;
		} else {
			w = h * imagew / imageh;
		}
		log.debug("print x is "+ x +"and y is" + y);
		log.debug("print w is "+ w + "and h is " + h);
		
		drawGraphics(graphics);
		
		return Printable.PAGE_EXISTS;
	}
	
	public void paint(Graphics graphics) {
		drawGraphics(graphics);
	}
	
	private void drawGraphics(Graphics graphics) {
		graphics.drawImage(image, (int) x, (int) y, (int) w, (int) h, this);
	}
	
	public void print(byte[] byteArrayOfJPEGFile) throws Exception{
		this.image = (new ImageIcon(byteArrayOfJPEGFile)).getImage();
		imagew = image.getWidth(null);
		imageh = image.getHeight(null);
		log.debug("start print image");
		printJob.print();
		log.debug("print was spppled to the printer");
	}
	
	public void print(Image image) throws Exception{
		boolean flag = printJob.printDialog();
		if(flag){
			this.image = image;
			imagew = image.getWidth(null);
			imageh = image.getHeight(null);
			log.debug("start print image");
			printJob.print();
			log.debug("print was spppled to the printer");
		}
	}
	
	public void print(File file, boolean deleteflag) throws Exception{
		boolean flag = printJob.printDialog();
		if(flag){
			FileInputStream inputStream = new FileInputStream(file);
			byte[] array = new byte[inputStream.available()];
			inputStream.read(array);
			print(array);
			if(inputStream != null)
				inputStream.close();
		}
		if(deleteflag)
			file.delete();
	}
	
	public void print(String filepath) throws Exception{
		boolean flag = printJob.printDialog();
		if(flag){
			FileInputStream inputStream = new FileInputStream(new File(filepath));
			byte[] array = new byte[inputStream.available()];
			inputStream.read(array);
			print(array);
			if(inputStream != null)
				inputStream.close();
		}
	}
	
	/* 打印指定的文件 */
	public void printFileAction(String fileAddr) {
		// 构建打印请求属性集
		File file = new File(fileAddr);
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(new JobName(file.getName(), Locale.CHINA));
		// 设置打印格式，因为未确定文件类型，这里选择AUTOSENSE
		DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
		// 查找所有的可用打印服务
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(
				flavor, pras);
		// 定位默认的打印服务
		PrintService defaultService = PrintServiceLookup
				.lookupDefaultPrintService();
		// 显示打印对话框
		PrintService service = ServiceUI.printDialog(null, 200, 200,
				printService, defaultService, flavor, pras);
		if (service != null) {
			try {
				DocPrintJob job = service.createPrintJob(); // 创建打印作业
				FileInputStream fis = new FileInputStream(file); // 构造待打印的文件流
				DocAttributeSet das = new HashDocAttributeSet();
				Doc doc = new SimpleDoc(fis, flavor, das); // 建立打印文件格式
				job.print(doc, pras); // 进行文件的打印
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 *
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		PrinterImpl print = new PrinterImpl();
		print.print("D:\\ScanTest\\IBS0001\\00001(1).jpg");
//		print.printFileAction("D:\\ScanTest\\IBS0001\\00001(1).jpg");
	}

}
