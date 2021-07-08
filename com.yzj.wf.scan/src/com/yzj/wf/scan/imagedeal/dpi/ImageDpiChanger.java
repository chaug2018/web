
package com.yzj.wf.scan.imagedeal.dpi;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
*创建于:2012-11-1<br>
*版权所有(C) 2012 深圳市银之杰科技股份有限公司<br>
* 修改图片dpi
* @author 陈林江
* @version 1.0.0
*/
public class ImageDpiChanger {



	/**
	 * 报错指定dpi的图像
	 * @param image 原始图像
	 * @param path 目标路径
	 * @param dpi dpi值
	 * @throws IOException
	 */
	 public static synchronized void saveImage(BufferedImage image,File path,int dpi) throws IOException {
	    path.delete();

	    final String formatName = "jpeg";

	    for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName(formatName); iw.hasNext();) {
	       ImageWriter writer = iw.next();
	       ImageWriteParam writeParam = writer.getDefaultWriteParam();
	       ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
	       IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
	       if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
	          continue;
	       }

	       setDPI(metadata,dpi);

	       final ImageOutputStream stream = ImageIO.createImageOutputStream(path);
	       try {
	          writer.setOutput(stream);
	          writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
	       } finally {
	          stream.close();
	       }
	       break;
	    }
	 }

	 private static void setDPI(IIOMetadata metadata,int dpi) throws IIOInvalidTreeException {

	    // for PMG, it's dots per millimeter
	    double dotsPerMilli = 1.0 * dpi / 10 / 2.54;

	    IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
	    horiz.setAttribute("value", Double.toString(dotsPerMilli));

	    IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
	    vert.setAttribute("value", Double.toString(dotsPerMilli));

	    IIOMetadataNode dim = new IIOMetadataNode("Dimension");
	    dim.appendChild(horiz);
	    dim.appendChild(vert);

	    IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
	    root.appendChild(dim);

	    metadata.mergeTree("javax_imageio_1.0", root);
	 }
	 
	 /**
	  * 设置图片dpi
	  * @param bufferImage 原始图像
	  * @param outfile 输出图像
	  * @param dpi dpi值
	  * @throws Exception
	  */
	 public static void ChangeDpi4Jpg( BufferedImage bufferImage,File outfile,int dpi) throws Exception {  
		 FileOutputStream fos=null;
		 try{  
		    fos = new FileOutputStream(outfile);  
	        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(fos);  
	        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(bufferImage);  
	        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);  
	        jpegEncodeParam.setXDensity(dpi);  
	        jpegEncodeParam.setYDensity(dpi);  
	        jpegEncoder.encode(bufferImage, jpegEncodeParam);  
	     }catch(Exception e){
	    	 e.printStackTrace();
	    	 throw e;
	     }finally{
	    	 fos.close();  
	     }   
	        }  
	  
	      public static BufferedImage toBufferedImage(Image image, int type) {  
	            int w = image.getWidth(null);  
	            int h = image.getHeight(null);  
	            BufferedImage result = new BufferedImage(w, h, type);  
	            Graphics2D g = result.createGraphics();  
	            g.drawImage(image, 0, 0, null);  
	            g.dispose();  
	            return result;  
	        }  
	}  
