package com.yzj.ebs.auto.verifydata;

import com.yzj.ebs.auto.JacobLoader;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(System.getProperty("java.library.path"));
		JacobLoader loader = new JacobLoader();
		
		try{
			if(loader.load("XOCRXML.XOcrXmlCtrl.1")){
				System.out.println("1");
				Object[] param = new Object[]{};
				loader.doMethod("InitCtrl", param);
				
				//1.模板的位置
				param=new Object[]{"C://HYXJDZD.xml"};
				System.out.println(loader.doMethod("SetXmlFile", param));
				
				//2.加载影像的位置
				param=new Object[]{"C://1110.jpg","1"};
				System.out.println(loader.doMethod("OcrPro", param));
				
				param = new Object[]{};
				String str = loader.doMethod("GetAreaCount", param);
				System.out.println(str);
				
				
				// 获得识别区别的值
				int length = Integer.valueOf(str);
				StringBuffer sb = new StringBuffer();
				for (int i=1;i<=length;i++)
				{	
					String strValue = loader.doMethod("GetAreaResult", new Object[]{i});
					if("".equals(strValue)){
						strValue = " ";
					}
					sb.append(strValue+"$");
				}
				//打印识别区域的值
				System.out.println(sb.toString());
				
				
				String[] ss = sb.toString().split("\\$");
				System.out.println(ss.length);
				
				
				param = new Object[]{};
				loader.doMethod("UnInitCtrl", param);
				loader.release();
			}else{
				System.out.println("0");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		

	}

}
