package com.yzj.ebs.tool;

import java.io.File;
import org.apache.tools.zip.ZipOutputStream; //这个包在ant.jar里，要到官方网下载
import java.io.FileInputStream;
import java.io.FileOutputStream;

/***
 * 工具类 把文件夹 打成jar包
 * 
 * @author j_sun
 * 
 */
public class CompressBook {
	public CompressBook() {}

    /**
    * inputFileName 输入一个文件夹
    * zipFileName 输出一个压缩包名称
    */
    public void zip(String inputFileName,String path) throws Exception {
        String zipFileName = path; //打包后文件名字
        zip(zipFileName, new File(inputFileName));
    }

    private void zip(String zipFileName, File inputFile) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        zip(out, inputFile, "");
        out.close();
    }

    private void zip(ZipOutputStream out, File f, String base) throws Exception {
        if (f.isDirectory()) {
           File[] fl = f.listFiles();
           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
           base = base.length() == 0 ? "" : base + "/";
           for (int i = 0; i < fl.length; i++) {
        	   zip(out, fl[i], base + fl[i].getName());
           }
        }else {
           out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
           FileInputStream in = new FileInputStream(f);
           int b;
           while ( (b = in.read()) != -1) {
            out.write(b);
           }
         in.close();
       }
    }
}