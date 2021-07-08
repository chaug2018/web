package com.yzj.ebs.auto.verifydata;
/**
 * OCR字母替换
 * 	替换规则：
 * 	strResult.Replace(" ", "");
	strResult.Replace("o", "0");
	strResult.Replace("O", "0");
	strResult.Replace("()", "0");
	strResult.Replace("D", "0");
	strResult.Replace("Q", "0");
	strResult.Replace("a", "0");
	strResult.Replace("l", "1");
	strResult.Replace("L", "1");
	strResult.Replace("[", "1");
	strResult.Replace("]", "1");
	strResult.Replace("i", "1");
	strResult.Replace("z", "2");
	strResult.Replace("Z", "2");
	strResult.Replace("A", "4");
	strResult.Replace("s", "5");
	strResult.Replace("S", "5");
	strResult.Replace("C", "5");
	strResult.Replace("G", "6");
	strResult.Replace("b", "6");
	strResult.Replace("I", "7");
	strResult.Replace("T", "7");
	strResult.Replace("B", "8");
	strResult.Replace("q", "9");
	strResult.Replace("g", "9");
 * @author Administrator
 *
 */
public class CharReplaceUtils {
	
	/**
	 * 将str串中字母替换为数字
	 * @param str
	 * @return
	 */
	public static String CharReplace(String str){
		//130988020068186$8lo40311000000180$20000000$ $ $ $ $ $ $ $ $ $ $ $ $ $
		//str ="130I88A2a068186$8lo40[11000000180$2]000000$ $ $ $ $ $ $ $ $ $ $ $ $ $";
		String result = str.replace('o', '0').
							replace('O', '0').
							replace("()", "0").		
							replace('D', '0').
							replace('Q', '0').
							replace('a', '0').
							replace('l', '1').
							replace('L', '1').
							replace('[', '1').
							replace(']', '1').
							replace('i', '1').
							replace('z', '2').
							replace('Z', '2').
							replace('A', '4').
							replace('s', '5').
							replace('S', '5').
							replace('C', '5').
							replace('G', '6').
							replace('b', '6').
							replace('I', '7').
							replace('T', '7').
							replace('B', '8').
							replace('q', '9').
							replace('g', '9');
		return result;
	}
	 
	/**
	 * 替换数据库拿出的 金额
	 * @param str
	 * @return
	 */
	public static String StrReplace(String str){
		String result = str.replace(".", "");
		if(result.length()==1 || "0".equals(result)){
			result = "000";
		}
		if(result.length()==2 || "00".equals(result)){
			result = "000";
		}
		return result;
	}

}
